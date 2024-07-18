package marvin.coto.dillosports

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.ClaseConexion
import java.util.Calendar
import java.util.UUID

class activityNuevosJugadores : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nuevos_jugadores)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityJugadores)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val txtNombreJugador = findViewById<EditText>(R.id.txtNombreJugador)
        val txtApellidoJugador = findViewById<EditText>(R.id.txtApellidoJugador)
        val txtEdadJugador = findViewById<EditText>(R.id. txtEdadJugadores)
        val txtTelefonoJugador = findViewById<EditText>(R.id. txtTelefonoJugador)
        val txtNumJugador = findViewById<EditText>(R.id.txtNumJugador)
        val txtPosicionJugador = findViewById<EditText>(R.id.txtPosicionJugador)
        val txtFechaJugador = findViewById<EditText>(R.id.txtFechaJugador)
        val txtEstadoJugador = findViewById<EditText>(R.id.txtEstadoJugador)
        val btnInscribirJugador = findViewById<Button>(R.id.btnInscribirJugador)

        btnInscribirJugador.setOnClickListener {

            // Guardo en una variable los valores que escribió el usuario

            val nombre = txtNombreJugador.text.toString()
            val apellido = txtApellidoJugador.text.toString()
            val edad = txtEdadJugador.text.toString()
            val telefono = txtTelefonoJugador.text.toString()
            val numJugador = txtNumJugador.text.toString()
            val posicion = txtPosicionJugador.text.toString()
            val fecha = txtFechaJugador.text.toString()
            val estado = txtEstadoJugador.text.toString()


            // Variable para verificar si hay errores
            //La inicializamos en false
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

            if (edad.isEmpty()) {
                txtEdadJugador.error = "La edad es obligatorio"
                hayErrores = true
            } else {
                txtEdadJugador.error = null
            }

            if (telefono.isEmpty()) {
                txtTelefonoJugador.error = "El teléfono es obligatorio"
                hayErrores = true
            } else {
                txtTelefonoJugador.error = null
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

            if (estado.isEmpty()) {
                txtEstadoJugador.error = "El estado del jugador es obligatorio"
                hayErrores = true
            } else {
                txtEstadoJugador.error = null
            }


            //Funcion para limpiar campos







                if (hayErrores) {
                    Toast.makeText(this@activityNuevosJugadores, "Datos ingresados incorrectamente", Toast.LENGTH_SHORT).show()
                } else {
                    val intent: Intent = Intent(this, mostrarJugadores::class.java)
                    startActivity(intent)

                    CoroutineScope(Dispatchers.IO).launch {
                        val objConexion = ClaseConexion().cadenaConexion()

                        val addJugadores = objConexion?.prepareStatement("insert into tbJugadores (UUID_Jugador, Nombre_Jugador, Apellido_Jugador, FNacimiento_Jugador, Edad_Jugador, Telefono_Jugador, Numero_Jugador, Posicion_Jugador, Estado_Jugador) values (?,?,?,?,?,?,?,?,?)")!!
                        addJugadores.setString(1, UUID.randomUUID().toString())
                        addJugadores.setString(2, txtNombreJugador.text.toString())
                        addJugadores.setString(3, txtApellidoJugador.text.toString())
                        addJugadores.setString(4, txtFechaJugador.text.toString())
                        addJugadores.setInt(5, txtEdadJugador.text.toString().toInt())
                        addJugadores.setString(6, txtTelefonoJugador.text.toString())
                        addJugadores.setInt(7, txtNumJugador.text.toString().toInt())
                        addJugadores.setString(8, txtPosicionJugador.text.toString())
                        addJugadores.setString(9, txtEstadoJugador.text.toString())
                        addJugadores.executeUpdate()

                        withContext(Dispatchers.Main){
                            Toast.makeText(this@activityNuevosJugadores, "Jugador Inscrito", Toast.LENGTH_SHORT).show()
                            txtNombreJugador.setText("")
                            txtApellidoJugador.setText("")
                            txtEdadJugador.setText("")
                            txtTelefonoJugador.setText("")
                            txtNumJugador.setText("")
                            txtPosicionJugador.setText("")
                            txtFechaJugador.setText("")
                            txtEstadoJugador.setText("")
                        }
                    }
                }

            }
        }



    }
