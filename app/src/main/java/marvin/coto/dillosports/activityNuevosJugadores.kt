package marvin.coto.dillosports

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import modelos.ClaseConexion
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

        val txtNombre = findViewById<TextView>(R.id.txtNombre)
        val txtApellido = findViewById<TextView>(R.id.txtApellido)
        val txtFNacimiento = findViewById<TextView>(R.id. txtFNacimiento)
        val txtEdad = findViewById<TextView>(R.id. txtEdad)
        val txtTelefono = findViewById<TextView>(R.id. txtTelefono)
        val txtNumJugador = findViewById<TextView>(R.id.txtNumJugador)
        val txtPosicion = findViewById<TextView>(R.id.txtPosicion)
        val txtEstado = findViewById<TextView>(R.id.txtEstado)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = ClaseConexion().cadenaConexion()

                val addJugadores = objConexion?.prepareStatement("insert into tbJugadores (UUID_Jugador, Nombre_Jugador, Apellido_Jugador, FNacimiento_Jugador, Edad_Jugador, Telefono_Jugador, Numero_Jugador, Posicion_Jugador, Estado_Jugador) values (?,?,?,?,?,?,?,?,?)")!!

                addJugadores.setString(1, UUID.randomUUID().toString())
                addJugadores.setString(2, txtNombre.text.toString())
                addJugadores.setString(3, txtApellido.text.toString())
                addJugadores.setInt(4, txtFNacimiento.text.toString().toInt())
                addJugadores.setInt(5, txtEdad.text.toString().toInt())
                addJugadores.setInt(5, txtTelefono.text.toString().toInt())
                addJugadores.setInt(6, txtNumJugador.text.toString().toInt())
                addJugadores.setString(7, txtPosicion.text.toString())
                addJugadores.setString(8, txtEstado.text.toString())
                addJugadores.executeUpdate()


            }
        }



    }
}