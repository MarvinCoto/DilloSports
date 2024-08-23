package marvin.coto.dillosports

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
        val txtNumJugador = findViewById<EditText>(R.id.txtNumJugador)
        val txtPosicionJugador = findViewById<EditText>(R.id.txtPosicionJugador)
        val txtFechaJugador = findViewById<EditText>(R.id.txtFechaJugador)
        val spEstadoJugador = findViewById<Spinner>(R.id.spEstadoJugador)
        val btnInscribirJugador = findViewById<Button>(R.id.btnInscribirJugador)

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

            // Guardo en una variable los valores que escribió el usuario

            val nombre = txtNombreJugador.text.toString()
            val apellido = txtApellidoJugador.text.toString()
            val numJugador = txtNumJugador.text.toString()
            val posicion = txtPosicionJugador.text.toString()
            val fecha = txtFechaJugador.text.toString()


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
                    Toast.makeText(this@activityNuevosJugadores, "Datos ingresados incorrectamente", Toast.LENGTH_SHORT).show()
                } else {
                    val intent: Intent = Intent(this, mostrarJugadores::class.java)
                    startActivity(intent)

                    CoroutineScope(Dispatchers.IO).launch {
                        val objConexion = ClaseConexion().cadenaConexion()

                        val addJugadores = objConexion?.prepareStatement("insert into tbJugadores (UUID_Jugador, Nombre_Jugador, Apellido_Jugador, FNacimiento_Jugador, Numero_Jugador, Posicion_Jugador, Estado_Jugador) values (?,?,?,?,?,?,?)")!!
                        addJugadores.setString(1, UUID.randomUUID().toString())
                        addJugadores.setString(2, txtNombreJugador.text.toString())
                        addJugadores.setString(3, txtApellidoJugador.text.toString())
                        addJugadores.setString(4, txtFechaJugador.text.toString())
                        addJugadores.setInt(5, txtNumJugador.text.toString().toInt())
                        addJugadores.setString(6, txtPosicionJugador.text.toString())
                        //addJugadores.setString(7, txtEstadoJugador.text.toString()) //spinner
                        addJugadores.executeUpdate()

                        withContext(Dispatchers.Main){
                            Toast.makeText(this@activityNuevosJugadores, "Jugador Inscrito", Toast.LENGTH_SHORT).show()
                            txtNombreJugador.setText("")
                            txtApellidoJugador.setText("")
                            txtNumJugador.setText("")
                            txtPosicionJugador.setText("")
                            txtFechaJugador.setText("")
                        }
                    }
                }

            }
        }



    }
