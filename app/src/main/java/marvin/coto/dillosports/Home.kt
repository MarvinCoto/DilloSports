package marvin.coto.dillosports

import RecyclerView.AdapterNoti
import RecyclerView.AdapterPart
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.ClaseConexion
import modelos.tbNoticias
import modelos.tbPartidos
import modelos.tbUsuarios

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgTorneo = findViewById<ImageView>(R.id.imgTorneoHome)
        imgTorneo.setOnClickListener {
            val intent = Intent(this, Torneos::class.java)
            startActivity(intent)
        }

        val imgPerfil = findViewById<ImageView>(R.id.imgPerfilH)
        imgPerfil.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }
        val usuario = tbUsuarios.currentUser
        val textViewNombreInicio = findViewById<TextView>(R.id.textViewNombreInicio)
        val nombreRecibido = usuario!!.Nombre_Usuario
        textViewNombreInicio.text = nombreRecibido

        val rcvNoticias = findViewById<RecyclerView>(R.id.rcvNoticias)
        rcvNoticias.layoutManager = LinearLayoutManager(this)

        fun obtenerNoticias(): List<tbNoticias>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbNoticias")!!
            val listaNoticias = mutableListOf<tbNoticias>()

            while(resultSet.next()){
                val UUID_Noticia = resultSet.getString("UUID_Noticia")
                val Nombre_Noticia = resultSet.getString("Nombre_Noticia")
                val Descripcion_Noticia = resultSet.getString("Descripcion_Noticia")
                val Hora_Noticia = resultSet.getString("Hora_Noticia")
                val Imagen_Noticia = resultSet.getString("Imagen_Noticia")

                val valoresJuntos = tbNoticias(UUID_Noticia, Nombre_Noticia, Descripcion_Noticia, Hora_Noticia, Imagen_Noticia)
                listaNoticias.add(valoresJuntos)
            }
            return listaNoticias
        }

        CoroutineScope(Dispatchers.IO).launch {
            val noticiasBD = obtenerNoticias()
            withContext(Dispatchers.Main){
                val adapter = AdapterNoti(noticiasBD)
                rcvNoticias.adapter = adapter
            }
        }

        val rcvPartidos = findViewById<RecyclerView>(R.id.rcvProximosPartidos)
        rcvNoticias.layoutManager = LinearLayoutManager(this)

        fun obtenerPartidos(): List<tbPartidos>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT p.UUID_Partido, p.UUID_Equipo1, p.UUID_Equipo2, p.Fecha_Partido, p.Lugar_Partido, p.Hora_Partido, p.Marcador_Equipo1, p.Marcador_Equipo2, p.Tipo_Partido, p.UUID_Arbitro, e1.Logo_Equipo AS imagen1, e2.Logo_Equipo AS imagen2 FROM tbPartidos p INNER JOIN tbEquipos e1 ON p.UUID_Equipo1 = e1.UUID_Equipo INNER JOIN tbEquipos e2 ON p.UUID_Equipo2 = e2.UUID_Equipo")!!
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

        CoroutineScope(Dispatchers.IO).launch {
            val partidosBD = obtenerPartidos()
            withContext(Dispatchers.Main){
                val adapter = AdapterPart(partidosBD)
                rcvPartidos.adapter = adapter
            }
        }

    }
}