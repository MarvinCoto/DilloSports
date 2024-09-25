package marvin.coto.dillosports

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.ClaseConexion
import modelos.tbDeportes
import modelos.tbGeneroUsuario
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import java.util.Calendar
import java.util.UUID

class activity_login : AppCompatActivity() {
    val codigo_opcion_galeria_user = 101
    val STORAGE_REQUEST_CODE_USER = 2

    lateinit var imageViewUser: ImageView
    lateinit var miPathUser: String

    val uuidUser = UUID.randomUUID().toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageViewUser = findViewById(R.id.imgUser1)
        val txtNombreRegistro = findViewById<EditText>(R.id.txtNombreRegistro)
        val txtApellidoRegistro = findViewById<EditText>(R.id.txtApellidoRegistro)
        val txtUsernameRegistro = findViewById<EditText>(R.id.txtUsernameRegistro)
        val txtContraseniaRegistro = findViewById<EditText>(R.id.txtContraseniaRegistro)
        val txtCorreoRegistro = findViewById<EditText>(R.id.txtCorreoRegistro)
        val txtNacimientoRegistro = findViewById<EditText>(R.id.txtNacimientoRegistro)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val spGenero = findViewById<Spinner>(R.id.spGenero)
        val IrALogin = findViewById<TextView>(R.id.textViewIrALogin)
        val imgVerPassword = findViewById<ImageView>(R.id.imgVerContraseñaR)
        val btnSubirImgUser = findViewById<Button>(R.id.btnSubirImgUser1)

        fun obtenerGenero(): List<tbGeneroUsuario>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbGeneroUsuario")!!
            val listaGenero = mutableListOf<tbGeneroUsuario>()

            while (resultSet.next()) {
                val uuidGenero = resultSet.getString("UUID_Genero")
                val nombreGenero = resultSet.getString("Nombre_Genero")
                val unGenero = tbGeneroUsuario(uuidGenero, nombreGenero)
                listaGenero.add(unGenero)
            }
            return listaGenero
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listaGenero = obtenerGenero()
            val nombreGenero = listaGenero.map { it.Nombre_Genero }

            withContext(Dispatchers.Main){
                val miAdaptador = ArrayAdapter(this@activity_login, android.R.layout.simple_spinner_dropdown_item, nombreGenero)
                spGenero.adapter = miAdaptador
            }
        }

        btnSubirImgUser.setOnClickListener {
            println("le dieron clic al subir imagen")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, codigo_opcion_galeria_user)
            checkStoragePermission()
        }


        imgVerPassword.setOnClickListener {
            if (txtContraseniaRegistro.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                txtContraseniaRegistro.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            else {
                txtContraseniaRegistro.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }


        txtNacimientoRegistro.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada =
                        "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtNacimientoRegistro.setText(fechaSeleccionada)
                },
                anio, mes, dia
            )
            datePickerDialog.show()
        }


        fun hashSHA256(contraseniaEscrita: String): String {
            val bytes =
                MessageDigest.getInstance("SHA-256").digest(contraseniaEscrita.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

        btnCrearCuenta.setOnClickListener {
            val intent = Intent(this, Home::class.java)

            val nombre = txtNombreRegistro.text.toString()
            val apellido = txtApellidoRegistro.text.toString()
            val username = txtUsernameRegistro.text.toString()
            val contrasenia = txtContraseniaRegistro.text.toString()
            val correo = txtCorreoRegistro.text.toString()
            val fecha = txtNacimientoRegistro.text.toString()

            var hayErrores = false

            if (nombre.isEmpty()) {
                txtNombreRegistro.error = "El nombre es obligatorio"
                hayErrores = true
            } else {
                txtNombreRegistro.error = null
            }

            if (apellido.isEmpty()) {
                txtApellidoRegistro.error = "El apellido es obligatorio"
                hayErrores = true
            } else {
                txtApellidoRegistro.error = null
            }

            if (username.isEmpty()) {
                txtUsernameRegistro.error = "El username es obligatorio"
                hayErrores = true
            } else {
                txtUsernameRegistro.error = null
            }

            if (contrasenia.isEmpty()) {
                txtContraseniaRegistro.error = "El contraseña es obligatorio"
                hayErrores = true
            } else {
                txtContraseniaRegistro.error = null
            }

            if (correo.isEmpty()) {
                txtCorreoRegistro.error = "El correo electrónico es obligatorio"
                hayErrores = true
            } else {
                txtCorreoRegistro.error = null
            }

            if (fecha.isEmpty()) {
                txtNacimientoRegistro.error = "La fecha de nacimiento es obligatorio"
                hayErrores = true
            } else {
                txtNacimientoRegistro.error = null
            }
            //Validar que el correo sea correo

            if (!correo.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+[.][a-z]+"))) {
                txtCorreoRegistro.error = "El correo no tiene un formato válido"
                hayErrores = true
            } else {
                txtCorreoRegistro.error = null
            }

            if (contrasenia.length <= 7) {
                txtContraseniaRegistro.error = "La contraseña debe tener más de 7 caracteres"
                hayErrores = true
            } else {
                txtContraseniaRegistro.error = null
            }

                if (hayErrores) {
                    Toast.makeText(this@activity_login, "Datos ingresados incorrectamente", Toast.LENGTH_SHORT).show()
                } else {
                    GlobalScope.launch(Dispatchers.IO) {
                        val objConexion = ClaseConexion().cadenaConexion()
                        val genero = obtenerGenero()

                        val contraseniaEncriptada = hashSHA256(txtContraseniaRegistro.text.toString())

                        val crearUsuario = objConexion?.prepareStatement("INSERT INTO tbUsuarios (UUID_Usuario, Nombre_Usuario, Apellido_Usuario, User_name, Contrasena_Usuario, Correo_Usuario, FNacimiento_Usuario, Foto_Usuario, UUID_Tipo_Usuario, UUID_Genero) VALUES (?,?,?,?,?,?,?,?,?,?)")!!
                        crearUsuario.setString(1, uuidUser)
                        crearUsuario.setString(2, txtNombreRegistro.text.toString())
                        crearUsuario.setString(3, txtApellidoRegistro.text.toString())
                        crearUsuario.setString(4, txtUsernameRegistro.text.toString())
                        crearUsuario.setString(5, contraseniaEncriptada)
                        crearUsuario.setString(6, txtCorreoRegistro.text.toString())
                        crearUsuario.setString(7, txtNacimientoRegistro.text.toString())
                        crearUsuario.setString(8, miPathUser)
                        crearUsuario.setInt(9, 1)
                        crearUsuario.setString(10, genero[spGenero.selectedItemPosition].UUID_Genero)
                        crearUsuario.executeUpdate()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@activity_login, "Usuario creado con éxito", Toast.LENGTH_SHORT).show()

                            txtNombreRegistro.setText("")
                            txtApellidoRegistro.setText("")
                            txtUsernameRegistro.setText("")
                            txtContraseniaRegistro.setText("")
                            txtCorreoRegistro.setText("")
                            txtNacimientoRegistro.setText("")
                        }
                        startActivity(intent)
                    }
                }
        }

        IrALogin.setOnClickListener{
            val pantallaLogin = Intent(this, activity_iniciar_sesion::class.java)
            startActivity(pantallaLogin)
        }

    }

    private fun checkStoragePermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            pedirPermisoAlmacenamiento()
        }
        else{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, codigo_opcion_galeria_user)
        }
    }

    private fun pedirPermisoAlmacenamiento() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE_USER)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_REQUEST_CODE_USER -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, codigo_opcion_galeria_user)
                } else {
                    Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else -> {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                codigo_opcion_galeria_user -> {
                    val imageUri: Uri? = data?.data
                    imageUri?.let {
                        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                        subirimagenFirebase(imageBitmap) { url ->
                            miPathUser = url
                            imageViewUser.setImageURI(it)
                        }
                    }
                }
            }
        }
    }

    private fun subirimagenFirebase(bitmap: Bitmap, onSucces: (String) -> Unit) {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("images/${uuidUser}.jpg ")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)

        uploadTask.addOnFailureListener {
            Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                onSucces(uri.toString())
            }
        }

    }
}
