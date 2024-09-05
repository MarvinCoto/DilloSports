package marvin.coto.dillosports

import RecyclerView.Adapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.ClaseConexion
import modelos.tbJugadores

class mostrarJugadores : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mostrar_jugadores)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgAtras = findViewById<ImageView>(R.id.imgAtras5)
        imgAtras.setOnClickListener {
            val intent = Intent(this, VerEquipo::class.java)
            startActivity(intent)
        }

        val btnVerCrearJugador = findViewById<Button>(R.id.btnVerCrearJugador)
        btnVerCrearJugador.setOnClickListener {
            val intent = Intent(this, activityNuevosJugadores::class.java)
            startActivity(intent)
        }

        val rcvMostrarJugador = findViewById<RecyclerView>(R.id.rcvMostrarJugador)
        rcvMostrarJugador.layoutManager = LinearLayoutManager(this)

        fun obtenerJugadores(): List<tbJugadores>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbJugadores")!!
            val listaJugadores = mutableListOf<tbJugadores>()

            while(resultSet.next()){
                val UUID_Jugador = resultSet.getString("UUID_Jugador")
                val Nombre_Jugador = resultSet.getString("Nombre_Jugador")
                val Apellido_Jugador = resultSet.getString("Apellido_Jugador")
                val FNacimiento_Jugador = resultSet.getString("FNacimiento_Jugador")
                val Numero_Jugador = resultSet.getInt("Numero_Jugador")
                val Posicion_Jugador = resultSet.getString("Posicion_Jugador")
                val Estado_Jugador = resultSet.getString("Estado_Jugador")
                val Foto_Jugador = resultSet.getString("Foto_Jugador")

                val valoresJuntos = tbJugadores(UUID_Jugador, Nombre_Jugador, Apellido_Jugador, FNacimiento_Jugador, Numero_Jugador, Posicion_Jugador, Estado_Jugador, Foto_Jugador)
                listaJugadores.add(valoresJuntos)
            }
            return listaJugadores
        }

        CoroutineScope(Dispatchers.IO).launch {
            val jugadoresBD = obtenerJugadores()
            withContext(Dispatchers.Main){
                val adapter = Adapter(jugadoresBD)
                rcvMostrarJugador.adapter = adapter
            }
        }

    }
}