package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.ClaseConexion
import java.util.UUID

class Crear_Torneo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_torneo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtNombreTorneo = findViewById<EditText>(R.id.txtNombreTorneo)
        val txtDescripcionTorneo = findViewById<EditText>(R.id.txtDescripcionTorneo)
        val txtUbicacionTorneo = findViewById<EditText>(R.id.txtUbicacionTorneo)
        val txtTipoDeporte = findViewById<EditText>(R.id.txtTipoDeporte)
        val txtEstadoTorneo = findViewById<EditText>(R.id.txtEstadoTorneo)
        val btnCrearTorneo = findViewById<Button>(R.id.btnCrearTorneo)

        btnCrearTorneo.setOnClickListener {

            // Guardo en una variable los valores que escribió el usuario

            val nombre = txtNombreTorneo.text.toString()
            val descripcion = txtDescripcionTorneo.text.toString()
            val ubicacion = txtUbicacionTorneo.text.toString()
            val tipoDeporte = txtTipoDeporte.text.toString()
            val estado = txtEstadoTorneo.text.toString()


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

            if (tipoDeporte.isEmpty()) {
                txtTipoDeporte.error = "El deporte es obligatorio"
                hayErrores = true
            }
            else {
                txtTipoDeporte.error = null
            }

            if (estado.isEmpty()) {
                txtEstadoTorneo.error = "El estado del torneo es obligatorio"
                hayErrores = true
            }
            else {
                txtEstadoTorneo.error = null
            }


            //Funcion para limpiar campos
            fun limpiarCampos(){
                txtNombreTorneo.text.clear()
                txtDescripcionTorneo.text.clear()
                txtUbicacionTorneo.text.clear()
                txtTipoDeporte.text.clear()
                txtEstadoTorneo.text.clear()
            }

            //Funcion para guardar datos
            fun guardarTorneos(
                nombre: String,
                descripcion: String,
                ubicacion: String,
                tipoDeporte: String,
                estado: String
            )
            {




                CoroutineScope(Dispatchers.IO).launch {
                    val objConexion = ClaseConexion().cadenaConexion()

                    val addTorneo = objConexion?.prepareStatement("insert into tbTorneos (UUID_Torneo, Nombre_Torneo, Ubicacion_Torneo, Descripcion_Torneo, Deporte, Estado_Toneo) values (?,?,?,?,?,?)")!!
                    addTorneo.setString(1, UUID.randomUUID().toString())
                    addTorneo.setString(2, txtNombreTorneo.text.toString())
                    addTorneo.setString(3, txtUbicacionTorneo.text.toString())
                    addTorneo.setString(4, txtDescripcionTorneo.text.toString())
                    addTorneo.setString(5, txtEstadoTorneo.text.toString())
                    addTorneo.setString(6, txtTipoDeporte.text.toString())
                    addTorneo.executeUpdate()

                    withContext(Dispatchers.Main){
                        Toast.makeText(this@Crear_Torneo, "Torneo Creado", Toast.LENGTH_SHORT).show()
                        txtNombreTorneo.setText("")
                        txtDescripcionTorneo.setText("")
                        txtUbicacionTorneo.setText("")
                        txtTipoDeporte.setText("")
                        txtEstadoTorneo.setText("")
                    }
                }

                val intent: Intent = Intent(this, Torneos::class.java)
                startActivity(intent)

                if (hayErrores) {
                    Toast.makeText(this@Crear_Torneo, "Datos ingresados incorrectamente", Toast.LENGTH_SHORT).show()
                } else {
                    // Si todas las validaciones son correctas, procede a guardar los datos
                    guardarTorneos(nombre, descripcion, ubicacion, tipoDeporte, estado)
                    limpiarCampos()
                }

            }

        }
    }
}