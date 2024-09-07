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
import java.io.ByteArrayOutputStream
import java.util.UUID

class InscribirArbitro : AppCompatActivity() {
    val codigo_opcion_galeria_arb = 102
    val STORAGE_REQUEST_CODE_ARB = 1

    lateinit var imageViewArb: ImageView
    lateinit var miPathArb: String

    val uuidArb = UUID.randomUUID().toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscribir_arbitro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imageViewArb = findViewById(R.id.img_Arbitro)
        val btnSubirImgArbitro = findViewById<Button>(R.id.btnSubirImgArbitro)
        val txtNombreArbitro = findViewById<EditText>(R.id.txtNombreArbitro)
        val txtApellidoArbitro = findViewById<EditText>(R.id.txtApellidoArbitro)
        val txtEdadArbitro = findViewById<EditText>(R.id.txtEdadArbitro)
        val txtTelefonoArbitro = findViewById<EditText>(R.id.txtTelefonoArbitro)
        val btnInscribirArbitro = findViewById<Button>(R.id.btnInscribirArbitro)

        btnSubirImgArbitro.setOnClickListener {
            checkStoragePermission()
        }

        btnInscribirArbitro.setOnClickListener {

            //Guardo en una variable los valores que escribió el usuario

            val nombre = txtNombreArbitro.text.toString()
            val apellido = txtApellidoArbitro.text.toString()
            val edad = txtEdadArbitro.text.toString()
            val telefono = txtTelefonoArbitro.text.toString()

            // Variable para verificar si hay errores
            //La inicializamos en false

            var hayErrores = false

            //Valido que los campos no estén vacíos

            if (nombre.isEmpty()) {
                txtNombreArbitro.error = "El nombre es obligatorio"
                hayErrores = true
            }
            else {
                txtApellidoArbitro.error = null
            }

            if (apellido.isEmpty()) {
                txtApellidoArbitro.error = "El apellido es obligatorio"
                hayErrores = true
            } else {
                txtApellidoArbitro.error = null
            }

            if (edad.isEmpty()) {
                txtEdadArbitro.error = "La edad es obligatorio"
                hayErrores = true
            } else {
                txtEdadArbitro.error = null
            }

            if (telefono.isEmpty()) {
                txtTelefonoArbitro.error = "El teléfono es obligatorio"
                hayErrores = true
            } else {
                txtTelefonoArbitro.error = null
            }

                if (hayErrores) {
                    Toast.makeText(this@InscribirArbitro, "Datos ingresados incorrectamente", Toast.LENGTH_SHORT).show()
                } else {
                    val intent= Intent(this, Arbitros::class.java)

                    CoroutineScope(Dispatchers.IO).launch {
                        val objConexion = ClaseConexion().cadenaConexion()

                        val addArbitro = objConexion?.prepareStatement("insert into tbArbitros (UUID_Arbitro, Nombre_Arbitro, Apellido_Arbitro, Edad_Arbitro, Telefono_Arbitro, Foto_Arbitro) values (?,?,?,?,?,?)")!!
                        addArbitro.setString(1, uuidArb)
                        addArbitro.setString(2, txtNombreArbitro.text.toString())
                        addArbitro.setString(3, txtApellidoArbitro.text.toString())
                        addArbitro.setInt(4, txtEdadArbitro.text.toString().toInt())
                        addArbitro.setString(5, txtTelefonoArbitro.text.toString())
                        addArbitro.setString(6, miPathArb)
                        addArbitro.executeUpdate()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@InscribirArbitro, "Arbitro Inscrito", Toast.LENGTH_SHORT).show()
                            txtNombreArbitro.setText("")
                            txtApellidoArbitro.setText("")
                            txtEdadArbitro.setText("")
                            txtTelefonoArbitro.setText("")
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
            startActivityForResult(intent, codigo_opcion_galeria_arb)
        }
    }

    private fun pedirPermisoAlmacenamiento() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE_ARB)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_REQUEST_CODE_ARB -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, codigo_opcion_galeria_arb)
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
                codigo_opcion_galeria_arb -> {
                    val imageUri: Uri? = data?.data
                    imageUri?.let {
                        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                        subirimagenFirebase(imageBitmap) { url ->
                            miPathArb = url
                            imageViewArb.setImageURI(it)
                        }
                    }
                }
            }
        }
    }

    private fun subirimagenFirebase(bitmap: Bitmap, onSucces: (String) -> Unit) {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("images/${uuidArb}.jpg ")
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
