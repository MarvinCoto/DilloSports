package marvin.coto.dillosports

import RecyclerView.AdapterPart
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

        val imgIrVerCpart = findViewById<ImageView>(R.id.imgIrVerCpart)
        imgIrVerCpart.setOnClickListener {
            val intent = Intent(this, Calendarizar_partido::class.java)
            startActivity(intent)
        }

        fun obtenerPartidos(): List<tbPartidos>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT p.UUID_Partido, e1.Nombre_Equipo AS Equipo1, e1.Logo_Equipo AS Logo_Equipo1, e2.Nombre_Equipo AS Equipo2, e2.Logo_Equipo AS Logo_Equipo2, p.Fecha_Partido, p.Lugar_Partido, p.Hora_Partido, p.Marcador_Equipo1, p.Marcador_Equipo2, p.Tipo_Partido, p.UUID_Arbitro FROM tbPartidos p INNER JOIN tbEquipos e1 ON p.UUID_Equipo1 = e1.UUID_Equipo INNER JOIN tbEquipos e2 ON p.UUID_Equipo2 = e2.UUID_Equipo")!!
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
                val Tipo_Partido = resultSet.getString("Tipo_Partido")
                val UUID_Arbitro = resultSet.getString("UUID_Arbitro")
                val imagen1 = resultSet.getString("imagen1")
                val imagen2 = resultSet.getString("imagen2")

                val valoresJuntos = tbPartidos(UUID_Partido, UUID_Equipo1, UUID_Equipo2, Fecha_Partido, Lugar_Partido, Hora_Partido, Marcador_Equipo1, Marcador_Equipo2, Tipo_Partido, UUID_Arbitro, imagen1, imagen2)
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