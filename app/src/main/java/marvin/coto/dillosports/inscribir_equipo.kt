package marvin.coto.dillosports

import android.app.Activity
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
import modelos.tbEstadoEquipos
import java.io.ByteArrayOutputStream
import java.util.UUID

class inscribir_equipo : AppCompatActivity() {
    val codigo_opcion_galeria_equi = 102
    val STORAGE_REQUEST_CODE_EQUI = 1

    lateinit var imageViewEqui: ImageView
    lateinit var miPathEqui: String

    val uuidEqui = UUID.randomUUID().toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscribir_equipo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imageViewEqui = findViewById(R.id.img_Equipo)
        val btnSubirImgEquipo = findViewById<Button>(R.id.btnSubirImgEquipo)
        val txtNombreEquipo = findViewById<EditText>(R.id.txtNombreEquipo)
        val txtDescripcionEquipo = findViewById<EditText>(R.id.txtDescripcionEquipo)
        val txtUbicacionEquipo = findViewById<EditText>(R.id.txtUbicacionEquipo)
        val spEstadoEquipo = findViewById<Spinner>(R.id.spEstadoEquipo)
        val btnInscribirEquipo = findViewById<Button>(R.id.btnInscribirEquipo)
        val imgAtrasDeInsEquip = findViewById<ImageView>(R.id.imgAtrasDeInsEquip)

        imgAtrasDeInsEquip.setOnClickListener {
            val intent = Intent(this, Equipos::class.java)
            startActivity(intent)
        }

        fun obtenerEstado(): List<tbEstadoEquipos>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbEstadoEquipo")!!
            val listaEstadoEquipo = mutableListOf<tbEstadoEquipos>()

            while (resultSet.next()) {
                val uuidEstadoEquipo = resultSet.getString("UUID_Estado_Equipo")
                val estadoEquipo = resultSet.getString("Estado_Equipo")
                val unEstadoEquipo = tbEstadoEquipos(uuidEstadoEquipo, estadoEquipo)
                listaEstadoEquipo.add(unEstadoEquipo)
            }
            return listaEstadoEquipo
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listaEstadoEquipo = obtenerEstado()
            val nombreEstadoEquipo = listaEstadoEquipo.map { it.Estado_Equipo }

            withContext(Dispatchers.Main){
                val miAdaptador = ArrayAdapter(this@inscribir_equipo, android.R.layout.simple_spinner_dropdown_item, nombreEstadoEquipo)
                spEstadoEquipo.adapter = miAdaptador
            }
        }

        btnSubirImgEquipo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, codigo_opcion_galeria_equi)
            checkStoragePermission()
        }

        btnInscribirEquipo.setOnClickListener {

            // Guardo en una variable los valores que escribió el usuario
            val nombre = txtNombreEquipo.text.toString()
            val descripcion = txtDescripcionEquipo.text.toString()
            val ubicacion = txtUbicacionEquipo.text.toString()


            // Variable para verificar si hay errores
            //La inicializamos en false
            var hayErrores = false

            //1-
            // Validar que los campos no estén vacíos

            if (nombre.isEmpty()) {
                txtNombreEquipo.error = "El nombre es obligatorio"
                hayErrores = true
            }
            else {
                txtNombreEquipo.error = null
            }

            if (descripcion.isEmpty()) {
                txtDescripcionEquipo.error = "La descripción es obligatorio"
                hayErrores = true
            }
            else {
                txtDescripcionEquipo.error = null
            }

            if (ubicacion.isEmpty()) {
                txtUbicacionEquipo.error = "La ubicación es obligatorio"
                hayErrores = true
            }
            else {
                txtUbicacionEquipo.error = null
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
                    val intent = Intent(this, Equipos::class.java)

                    CoroutineScope(Dispatchers.IO).launch {
                        val objConexion = ClaseConexion().cadenaConexion()
                        val estado = obtenerEstado()

                        val addEquipo = objConexion?.prepareStatement("insert into tbEquipos (UUID_Equipo, Nombre_Equipo, Descripcion_Equipo, Ubicacion_Equipo, Logo_Equipo, UUID_Estado_Equipo) values (?,?,?,?,?,?)")!!
                        addEquipo.setString(1, uuidEqui)
                        addEquipo.setString(2, txtNombreEquipo.text.toString())
                        addEquipo.setString(3, txtDescripcionEquipo.text.toString())
                        addEquipo.setString(4, txtUbicacionEquipo.text.toString())
                        addEquipo.setString(5, miPathEqui)
                        addEquipo.setString(6, estado[spEstadoEquipo.selectedItemPosition].UUID_Estado_Equipo)
                        addEquipo.executeUpdate()

                        withContext(Dispatchers.Main) {
                            val inflater = layoutInflater
                            val layout = inflater.inflate(R.layout.toast_good, null)

                            val toast = Toast(applicationContext)
                            toast.duration = Toast.LENGTH_LONG
                            toast.view = layout

                            val text = layout.findViewById<TextView>(R.id.text)
                            text.text = "Equipo inscrito"

                            toast.show()
                            txtNombreEquipo.setText("")
                            txtDescripcionEquipo.setText("")
                            txtUbicacionEquipo.setText("")
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
            startActivityForResult(intent, codigo_opcion_galeria_equi)
        }
    }

    private fun pedirPermisoAlmacenamiento() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE_EQUI)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_REQUEST_CODE_EQUI -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, codigo_opcion_galeria_equi)
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
                codigo_opcion_galeria_equi -> {
                    val imageUri: Uri? = data?.data
                    imageUri?.let {
                        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                        subirimagenFirebase(imageBitmap) { url ->
                            miPathEqui = url
                            imageViewEqui.setImageURI(it)
                        }
                    }
                }
            }
        }
    }

    private fun subirimagenFirebase(bitmap: Bitmap, onSucces: (String) -> Unit) {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("images/${uuidEqui}.jpg ")
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
