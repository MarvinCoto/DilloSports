package marvin.coto.dillosports

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import modelos.ClaseConexion
import modelos.tbDeportes
import modelos.tbEquipos
import modelos.tbEstadoTorneo

class Crear_Torneo : AppCompatActivity() {
    val codigo_opcion_galeria_torn = 101
    val STORAGE_REQUEST_CODE_TORN = 2

    lateinit var imageViewTorn: ImageView
    lateinit var miPathTorn: String

    val uuidTorn = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_torneo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imageViewTorn = findViewById(R.id.img_torneo)
        val btnSubirImgTorneo = findViewById<Button>(R.id.btnSubirImgTorneo)
        val txtNombreTorneo = findViewById<EditText>(R.id.txtNombreTorneo)
        val txtDescripcionTorneo = findViewById<EditText>(R.id.txtDescripcionTorneo)
        val txtUbicacionTorneo = findViewById<EditText>(R.id.txtUbicacionTorneo)
        val spTipoDeporte = findViewById<Spinner>(R.id.spTipoDeporte)
        val spEstadoTorneo = findViewById<Spinner>(R.id.spEstadoTorneo)
        val btnCrearTorneo = findViewById<Button>(R.id.btnCrearTorneo)
        val imgAtras = findViewById<ImageView>(R.id.imgAtraaaaaaas)

        imgAtras.setOnClickListener {
            val intent = Intent(this, Torneos::class.java)
            startActivity(intent)
        }

        fun obtenerDeporte(): List<tbDeportes>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbTipoDeporte")!!
            val listaDeportes = mutableListOf<tbDeportes>()

            while (resultSet.next()) {
                val uuidDeporte = resultSet.getString("UUID_Tipo_Deporte")
                val nombreDeporte = resultSet.getString("Nombre_Tipo_Deporte")
                val unDeporte = tbDeportes(uuidDeporte, nombreDeporte)
                listaDeportes.add(unDeporte)
            }
            return listaDeportes
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listaDeportes = obtenerDeporte()
            val nombreDeporte = listaDeportes.map { it.Nombre_Tipo_Deporte }

            withContext(Dispatchers.Main){
                val miAdaptador = ArrayAdapter(this@Crear_Torneo, android.R.layout.simple_spinner_dropdown_item, nombreDeporte)
                spTipoDeporte.adapter = miAdaptador
            }
        }

        fun obtenerEstado(): List<tbEstadoTorneo>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbEstadoTorneo")!!
            val listaEstados = mutableListOf<tbEstadoTorneo>()

            while (resultSet.next()) {
                val uuidEstado = resultSet.getString("UUID_Estado_Torneo")
                val nombreEstado = resultSet.getString("Nombre_Estado")
                val unEstado = tbEstadoTorneo(uuidEstado, nombreEstado)
                listaEstados.add(unEstado)
            }
            return listaEstados
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listaEstados = obtenerEstado()
            val nombreEstado = listaEstados.map { it.Nombre_Estado }

            withContext(Dispatchers.Main){
                val miAdaptador = ArrayAdapter(this@Crear_Torneo, android.R.layout.simple_spinner_dropdown_item, nombreEstado)
                spEstadoTorneo.adapter = miAdaptador
            }
        }


        btnSubirImgTorneo.setOnClickListener {
            println("le dieron clic al subir imagen")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, codigo_opcion_galeria_torn)
            checkStoragePermission()
        }


        btnCrearTorneo.setOnClickListener {
            val nombre = txtNombreTorneo.text.toString()
            val descripcion = txtDescripcionTorneo.text.toString()
            val ubicacion = txtUbicacionTorneo.text.toString()

            var hayErrores = false

            if (nombre.isEmpty()) {
                txtNombreTorneo.error = "El nombre es obligatorio"
                hayErrores = true
            }
            else {
                txtNombreTorneo.error = null
            }


            if (descripcion.isEmpty()) {
                txtDescripcionTorneo.error = "La descripción es obligatorio"
                hayErrores = true
            }
            else {
                txtDescripcionTorneo.error = null
            }

            if (ubicacion.isEmpty()) {
                txtUbicacionTorneo.error = "La ubicación es obligatorio"
                hayErrores = true
            }
            else {
                txtUbicacionTorneo.error = null
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
                    val intent = Intent(this, Torneos::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        val objConexion = ClaseConexion().cadenaConexion()
                        val deporte = obtenerDeporte()
                        val estado = obtenerEstado()

                        val addTorneo = objConexion?.prepareStatement("insert into tbTorneos (UUID_Torneo, Nombre_Torneo, Ubicacion_Torneo, Descripcion_Torneo, Logo_Torneo, UUID_Estado_Torneo, UUID_Tipo_Deporte) values (?,?,?,?,?,?,?)")!!
                        addTorneo.setString(1, uuidTorn)
                        addTorneo.setString(2, txtNombreTorneo.text.toString())
                        addTorneo.setString(3, txtUbicacionTorneo.text.toString())
                        addTorneo.setString(4, txtDescripcionTorneo.text.toString())
                        addTorneo.setString(5, miPathTorn)
                        addTorneo.setString(6, estado[spEstadoTorneo.selectedItemPosition].UUID_Estado_Torneo)
                        addTorneo.setString(7, deporte[spTipoDeporte.selectedItemPosition].UUID_Tipo_Deporte)
                        addTorneo.executeUpdate()

                        withContext(Dispatchers.Main){
                            val inflater = layoutInflater
                            val layout = inflater.inflate(R.layout.toast_good, null)

                            val toast = Toast(applicationContext)
                            toast.duration = Toast.LENGTH_LONG
                            toast.view = layout

                            val text = layout.findViewById<TextView>(R.id.text)
                            text.text = "Torneo Creado"

                            toast.show()
                            txtNombreTorneo.setText("")
                            txtDescripcionTorneo.setText("")
                            txtUbicacionTorneo.setText("")
                        }

                    }

                    startActivity(intent)
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
            startActivityForResult(intent, codigo_opcion_galeria_torn)
        }
    }

    private fun pedirPermisoAlmacenamiento() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE_TORN)
        }
    }

   override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_REQUEST_CODE_TORN -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, codigo_opcion_galeria_torn)
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
                codigo_opcion_galeria_torn -> {
                    val imageUri: Uri? = data?.data
                    imageUri?.let {
                        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                        subirimagenFirebase(imageBitmap) { url ->
                            miPathTorn = url
                            imageViewTorn.setImageURI(it)
                        }
                    }
                }
            }
        }
    }

    private fun subirimagenFirebase(bitmap: Bitmap, onSucces: (String) -> Unit) {
    val storageRef = Firebase.storage.reference
    val imageRef = storageRef.child("images/${uuidTorn}.jpg ")
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