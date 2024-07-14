package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        val txtNombreJugador = findViewById<TextView>(R.id.txtNombreJugador)
        val txtApellidoJugador = findViewById<TextView>(R.id.txtApellidoJugador)
        val txtEdadJugador = findViewById<TextView>(R.id. txtEdadJugadores)
        val txtTelefonoJugador = findViewById<TextView>(R.id. txtTelefonoJugador)
        val txtNumJugador = findViewById<TextView>(R.id.txtNumJugador)
        val txtPosicionJugador = findViewById<TextView>(R.id.txtPosicionJugador)
        val txtFechaJugador = findViewById<TextView>(R.id.txtFechaJugador)
        val txtEstadoJugador = findViewById<TextView>(R.id.txtEstadoJugador)
        val btnInscribirJugador = findViewById<Button>(R.id.btnInscribirJugador)

        btnInscribirJugador.setOnClickListener {
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
            val intent: Intent = Intent(this, mostrarJugadores::class.java)
            startActivity(intent)
        }



    }
}