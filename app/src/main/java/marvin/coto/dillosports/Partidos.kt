package marvin.coto.dillosports

import RecyclerView.AdapterPart
import RecyclerView.AdapterTorn
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
import modelos.tbPartidos

class Partidos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_partidos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgTorneoC = findViewById<ImageView>(R.id.imgTorneoC)
        imgTorneoC.setOnClickListener {
            val intent = Intent(this, Ver_Torneo::class.java)
            startActivity(intent)
        }

        val imgEquipoC = findViewById<ImageView>(R.id.imgEquipoC)
        imgEquipoC.setOnClickListener {
            val intent = Intent(this, Equipos::class.java)
            startActivity(intent)
        }

        val imgArbitrosC = findViewById<ImageView>(R.id.imgArbitrosC)
        imgArbitrosC.setOnClickListener {
            val intent = Intent(this, Arbitros::class.java)
            startActivity(intent)
        }

        val btnVerCrearPartido = findViewById<Button>(R.id.btnVerCrearPartido)
        btnVerCrearPartido.setOnClickListener {
            val intent = Intent(this, Calendarizar_partido::class.java)
            startActivity(intent)
        }

        fun obtenerPartidos(): List<tbPartidos>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbPartidos")!!
            val listaPartidos = mutableListOf<tbPartidos>()

            while(resultSet.next()){
                val UUID_Partido = resultSet.getString("UUID_Partido")
                val UUID_Equipo1 = resultSet.getString("UUID_Equipo1")
                val UUID_Equipo2 = resultSet.getString("UUID_Equipo2")
                val Fecha_Partido = resultSet.getString("Fecha_Partido")
                val Lugar_Partido = resultSet.getString("Lugar_Partido")
                val Hora_Partido = resultSet.getString("Hora_Partido")
                val Marcador_Equipo1 = resultSet.getInt("Marcador_Equipo1")
                val Marcador_Equipo2 = resultSet.getInt("Marcador_Equipo2")
                val UUID_Tipo_Partido = resultSet.getString("UUID_Tipo_Partido")
                val UUID_Arbitro = resultSet.getString("UUID_Arbitro")

                val valoresJuntos = tbPartidos(UUID_Partido, UUID_Equipo1, UUID_Equipo2, Fecha_Partido, Lugar_Partido, Hora_Partido, Marcador_Equipo1, Marcador_Equipo2, UUID_Tipo_Partido, UUID_Arbitro)
                listaPartidos.add(valoresJuntos)
            }
            return listaPartidos
        }

        val rcvPartidos = findViewById<RecyclerView>(R.id.rcvPartidos)
        rcvPartidos.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            val partidosBD = obtenerPartidos()
            withContext(Dispatchers.Main){
                val adapter = AdapterPart(partidosBD)
                rcvPartidos.adapter = adapter
            }
        }
    }
}