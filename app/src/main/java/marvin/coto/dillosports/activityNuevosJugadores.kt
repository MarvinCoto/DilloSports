package marvin.coto.dillosports

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.ClaseConexion
import modelos.tbEstadoJugador
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.UUID

class activityNuevosJugadores : AppCompatActivity() {
    val codigo_opcion_galeria_jug = 102
    val STORAGE_REQUEST_CODE_JUG = 1

    lateinit var imageViewJug: ImageView
    lateinit var miPathJug: String

    val uuidJug = UUID.randomUUID().toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nuevos_jugadores)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityJugadores)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imageViewJug = findViewById(R.id.img_Jugador)
        val btnSubirImgJugador = findViewById<Button>(R.id.btnSubirImgJugador)
        val txtNombreJugador = findViewById<EditText>(R.id.txtNombreJugador)
        val txtApellidoJugador = findViewById<EditText>(R.id.txtApellidoJugador)
        val txtNumJugador = findViewById<EditText>(R.id.txtNumJugador)
        val txtPosicionJugador = findViewById<EditText>(R.id.txtPosicionJugador)
        val txtFechaJugador = findViewById<EditText>(R.id.txtFechaJugador)
        val spEstadoJugador = findViewById<Spinner>(R.id.spEstadoJugador)
        val btnInscribirJugador = findViewById<Button>(R.id.btnInscribirJugador)

        val imgAtrasdeJugadoresjsjs = findViewById<ImageView>(R.id.imgAtrasdeJugadoresjsjs)
        imgAtrasdeJugadoresjsjs.setOnClickListener {
            val intent = Intent(this, mostrarJugadores::class.java)
            startActivity(intent)
        }

        fun obtenerEstado(): List<tbEstadoJugador>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbEstadoJugador")!!
            val listaEstadoJugador = mutableListOf<tbEstadoJugador>()

            while(resultSet.next()){
                val UUID_Estado = resultSet.getString("UUID_Estado_Jugador")
                val Nombre_Estado = resultSet.getString("Estado_Jugador")
                val unEstado = tbEstadoJugador(UUID_Estado, Nombre_Estado)
                listaEstadoJugador.add(unEstado)
            }
            return listaEstadoJugador
        }

            CoroutineScope(Dispatchers.IO).launch {
                val listaEstadoJugador = obtenerEstado()
                val nombreEstado = listaEstadoJugador.map { it.Estado_Jugador }

                withContext(Dispatchers.Main){
                    val miAdaptador = ArrayAdapter(this@activityNuevosJugadores, android.R.layout.simple_spinner_dropdown_item, nombreEstado)
                    spEstadoJugador.adapter = miAdaptador
                }
            }



        btnSubirImgJugador.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, codigo_opcion_galeria_jug)
            checkStoragePermission()
        }

        txtFechaJugador.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada =
                        "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtFechaJugador.setText(fechaSeleccionada)
                },
                anio, mes, dia
            )
            datePickerDialog.show()
        }

        btnInscribirJugador.setOnClickListener {
            val nombre = txtNombreJugador.text.toString()
            val apellido = txtApellidoJugador.text.toString()
            val numJugador = txtNumJugador.text.toString()
            val posicion = txtPosicionJugador.text.toString()
            val fecha = txtFechaJugador.text.toString()

            var hayErrores = false

            if (nombre.isEmpty()) {
                txtNombreJugador.error = "El nombre es obligatorio"
                hayErrores = true
            } else {
                txtNombreJugador.error = null
            }

            if (apellido.isEmpty()) {
                txtApellidoJugador.error = "El apellido es obligatorio"
                hayErrores = true
            } else {
                txtApellidoJugador.error = null
            }
            if (numJugador.isEmpty()) {
                txtNumJugador.error = "El número de camiseta es obligatorio"
                hayErrores = true
            } else {
                txtNumJugador.error = null
            }

            if (posicion.isEmpty()) {
                txtPosicionJugador.error = "La posición de juego es obligatorio"
                hayErrores = true
            } else {
                txtPosicionJugador.error = null
            }

            if (fecha.isEmpty()) {
                txtFechaJugador.error = "La fecha es obligatoria es obligatorio"
                hayErrores = true
            } else {
                txtFechaJugador.error = null
            }


                if (hayErrores) {
                    val inflater = layoutInflater
                    val layout = inflater.inflate(R.layout.toast_bad, null)

                    val toast = Toast(applicationContext)
                    toast.duration = Toast.LENGTH_LONG
                    toast.view = layout

                    val text = layout.findViewById<TextView>(R.id.text2)
                    text.text = "Datos ingresados incorrectamente"

                    toast.show()
                } else {
                    val intent = Intent(this, mostrarJugadores::class.java)

                    CoroutineScope(Dispatchers.IO).launch {
                        val objConexion = ClaseConexion().cadenaConexion()
                        val estado = obtenerEstado()

                        val addJugadores = objConexion?.prepareStatement("insert into tbJugadores (UUID_Jugador, Nombre_Jugador, Apellido_Jugador, FNacimiento_Jugador, Numero_Jugador, Posicion_Jugador, Foto_Jugador, UUID_Estado_Jugador) values (?,?,?,?,?,?,?,?)")!!
                        addJugadores.setString(1, uuidJug)
                        addJugadores.setString(2, txtNombreJugador.text.toString())
                        addJugadores.setString(3, txtApellidoJugador.text.toString())
                        addJugadores.setString(4, txtFechaJugador.text.toString())
                        addJugadores.setInt(5, txtNumJugador.text.toString().toInt())
                        addJugadores.setString(6, txtPosicionJugador.text.toString())
                        addJugadores.setString(7, miPathJug)
                        addJugadores.setString(8, estado[spEstadoJugador.selectedItemPosition].UUID_Estado_Jugador)
                        addJugadores.executeUpdate()

                        withContext(Dispatchers.Main){
                            val inflater = layoutInflater
                            val layout = inflater.inflate(R.layout.toast_good, null)

                            val toast = Toast(applicationContext)
                            toast.duration = Toast.LENGTH_LONG
                            toast.view = layout

                            val text = layout.findViewById<TextView>(R.id.text)
                            text.text = "Jugador Inscrito"

                            toast.show()
                            txtNombreJugador.setText("")
                            txtApellidoJugador.setText("")
                            txtNumJugador.setText("")
                            txtPosicionJugador.setText("")
                            txtFechaJugador.setText("")
                        }
                        startActivity(intent)
                    }
                }

            }
        }

    private fun checkStoragePermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            pedirPermisoAlmacenamiento()
        }
        else{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, codigo_opcion_galeria_jug)
        }
    }

    private fun pedirPermisoAlmacenamiento() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // hola
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE_JUG)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_REQUEST_CODE_JUG -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, codigo_opcion_galeria_jug)
                } else {
                    Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else -> {
                // como estoy?
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                codigo_opcion_galeria_jug -> {
                    val imageUri: Uri? = data?.data
                    imageUri?.let {
                        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                        subirimagenFirebase(imageBitmap) { url ->
                            miPathJug = url
                            imageViewJug.setImageURI(it)
                        }
                    }
                }
            }
        }
    }

    private fun subirimagenFirebase(bitmap: Bitmap, onSucces: (String) -> Unit) {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("images/${uuidJug}.jpg ")
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
