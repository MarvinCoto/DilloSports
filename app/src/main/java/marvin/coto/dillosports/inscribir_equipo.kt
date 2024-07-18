package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

class inscribir_equipo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscribir_equipo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtNombreEquipo = findViewById<EditText>(R.id.txtNombreEquipo)
        val txtDescripcionEquipo = findViewById<EditText>(R.id.txtDescripcionEquipo)
        val txtUbicacionEquipo = findViewById<EditText>(R.id.txtUbicacionEquipo)
        val txtEstadoEquipo = findViewById<EditText>(R.id.txtEstadoEquipo)
        val btnInscribirEquipo = findViewById<Button>(R.id.btnInscribirEquipo)

        btnInscribirEquipo.setOnClickListener {

            // Guardo en una variable los valores que escribió el usuario
            val nombre = txtNombreEquipo.text.toString()
            val descripcion = txtDescripcionEquipo.text.toString()
            val ubicacion = txtUbicacionEquipo.text.toString()
            val estado = txtEstadoEquipo.text.toString()


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

            if (estado.isEmpty()) {
                txtEstadoEquipo.error = "El estado es obligatorio"
                hayErrores = true
            }
            else {
                txtEstadoEquipo.error = null
            }

                if (hayErrores) {
                    Toast.makeText(this@inscribir_equipo, "Datos ingresados incorrectamente", Toast.LENGTH_SHORT).show()
                } else {
                    val intent: Intent = Intent(this, Equipos::class.java)
                    startActivity(intent)

                    CoroutineScope(Dispatchers.IO).launch {
                        val objConexion = ClaseConexion().cadenaConexion()

                        val addEquipo = objConexion?.prepareStatement("insert into tbEquipos (UUID_Equipo, Nombre_Equipo, Descripcion_Equipo, Ubicacion_Equipo, Estado_Equipo) values (?,?,?,?,?)")!!
                        addEquipo.setString(1, UUID.randomUUID().toString())
                        addEquipo.setString(2, txtNombreEquipo.text.toString())
                        addEquipo.setString(3, txtDescripcionEquipo.text.toString())
                        addEquipo.setString(4, txtUbicacionEquipo.text.toString())
                        addEquipo.setString(5, txtEstadoEquipo.text.toString())
                        addEquipo.executeUpdate()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@inscribir_equipo, "Equipo Inscrito", Toast.LENGTH_SHORT).show()
                            txtNombreEquipo.setText("")
                            txtDescripcionEquipo.setText("")
                            txtUbicacionEquipo.setText("")
                            txtEstadoEquipo.setText("")
                        }
                    }
                }

            }
        }
    }
