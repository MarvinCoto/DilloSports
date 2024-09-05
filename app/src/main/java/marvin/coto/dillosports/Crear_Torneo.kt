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
import kotlinx.coroutines.CoroutineScope
import modelos.ClaseConexion

class Crear_Torneo : AppCompatActivity() {
    val codigo_opcion_galeria = 102
    val STORAGE_REQUEST_CODE = 1

    lateinit var imageView: ImageView
    lateinit var miPath: String

    val uuid = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_torneo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imageView = findViewById(R.id.img_torneo)
        val btnSubirImgTorneo = findViewById<Button>(R.id.btnSubirImgTorneo)
        val txtNombreTorneo = findViewById<EditText>(R.id.txtNombreTorneo)
        val txtDescripcionTorneo = findViewById<EditText>(R.id.txtDescripcionTorneo)
        val txtUbicacionTorneo = findViewById<EditText>(R.id.txtUbicacionTorneo)
        val spTipoDeporte = findViewById<Spinner>(R.id.spTipoDeporte)
        val btnCrearTorneo = findViewById<Button>(R.id.btnCrearTorneo)

        CoroutineScope(Dispatchers.IO).launch {
            val listaTipoDeporte = arrayOf("Seleccionar Tipo de Deporte","Fútbol","Básquetbol","Voleibol")

            withContext(Dispatchers.Main){
                val miAdaptador = ArrayAdapter(this@Crear_Torneo, android.R.layout.simple_spinner_dropdown_item, listaTipoDeporte)
                spTipoDeporte.adapter = miAdaptador
            }
        }


        btnSubirImgTorneo.setOnClickListener {
            checkStoragePermission()
        }

        btnCrearTorneo.setOnClickListener {

            // Guardo en una variable los valores que escribió el usuario

            val nombre = txtNombreTorneo.text.toString()
            val descripcion = txtDescripcionTorneo.text.toString()
            val ubicacion = txtUbicacionTorneo.text.toString()

            // Variable para verificar si hay errores
            //La inicializamos en false
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
                    Toast.makeText(this@Crear_Torneo, "Datos ingresados incorrectamente", Toast.LENGTH_SHORT).show()
                } else {
                    val intent: Intent = Intent(this, Torneos::class.java)
                    startActivity(intent)

                    CoroutineScope(Dispatchers.IO).launch {
                        val objConexion = ClaseConexion().cadenaConexion()

                        val addTorneo = objConexion?.prepareStatement("insert into tbTorneos (UUID_Torneo, Nombre_Torneo, Ubicacion_Torneo, Descripcion_Torneo, UUID_Tipo_Deporte, Logo_Torneo) values (?,?,?,?,?,?)")!!
                        addTorneo.setString(1, uuid)
                        addTorneo.setString(2, txtNombreTorneo.text.toString())
                        addTorneo.setString(3, txtUbicacionTorneo.text.toString())
                        addTorneo.setString(4, txtDescripcionTorneo.text.toString())
                        addTorneo.setString(5, spTipoDeporte.selectedItemPosition.toString())
                        addTorneo.setString(6, miPath)
                        addTorneo.executeUpdate()

                        withContext(Dispatchers.Main){
                            Toast.makeText(this@Crear_Torneo, "Torneo Creado", Toast.LENGTH_SHORT).show()
                            txtNombreTorneo.setText("")
                            txtDescripcionTorneo.setText("")
                            txtUbicacionTorneo.setText("")
                        }
                    }
                }



            }
    }
    private fun pedirPermisoAlmacenamiento() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)
        }
    }

    private fun checkStoragePermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            pedirPermisoAlmacenamiento()
        }
        else{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, codigo_opcion_galeria)
        }
    }

   override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, codigo_opcion_galeria)
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
                codigo_opcion_galeria -> {
                    val imageUri: Uri? = data?.data
                    imageUri?.let {
                        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                        subirimagenFirebase(imageBitmap) { url ->
                            miPath = url
                            imageView.setImageURI(it)
                        }
                    }
                }
            }
        }
    }

    private fun subirimagenFirebase(bitmap: Bitmap, onSucces: (String) -> Unit) {
    val storageRef = Firebase.storage.reference
    val imageRef = storageRef.child("images/${uuid}.jpg ")
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