package marvin.coto.dillosports

import RecyclerView.AdapterArbi
import RecyclerView.AdapterTorn
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import modelos.tbArbitros
import modelos.tbUsuarios

class Arbitros : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_arbitros)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgEquiposD = findViewById<ImageView>(R.id.imgEquiposD)
        imgEquiposD.setOnClickListener {
            val intent = Intent(this, Equipos::class.java)
            startActivity(intent)
        }
        val imgTorneosD = findViewById<ImageView>(R.id.imgTorneosD)
        imgTorneosD.setOnClickListener {
            val intent = Intent(this, Ver_Torneo::class.java)
            startActivity(intent)
        }
        val imgPartidosD = findViewById<ImageView>(R.id.imgPartidosD)
        imgPartidosD.setOnClickListener {
            val intent = Intent(this, Partidos::class.java)
            startActivity(intent)
        }

        val btnVerCrearArbitros = findViewById<Button>(R.id.btnVerCrearArbitros)
        btnVerCrearArbitros.setOnClickListener {
            val intent = Intent(this, InscribirArbitro::class.java)
            startActivity(intent)
        }

        val imgVerInsArbi = findViewById<ImageView>(R.id.imgVerInsArbi)
        imgVerInsArbi.setOnClickListener {
            val intent = Intent(this, InscribirArbitro::class.java)
            startActivity(intent)
        }

        val uuidReference = AdapterTorn.variablesGlobalTorn.UUID_Usuario
        val usuario = tbUsuarios.currentUser

        if (usuario!!.UUID_Usuario == uuidReference || usuario!!.UUID_Tipo_Usuario == 3) {
            btnVerCrearArbitros.visibility = View.VISIBLE
            imgVerInsArbi.visibility = View.VISIBLE
        } else {
            btnVerCrearArbitros.visibility = View.GONE
            imgVerInsArbi.visibility = View.GONE
        }

        val rcvArbitros = findViewById<RecyclerView>(R.id.rcvArbitros)
        rcvArbitros.layoutManager = LinearLayoutManager(this)

        fun obtenerArbitros(): List<tbArbitros>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbArbitros")!!
            val listaArbitros = mutableListOf<tbArbitros>()

            while(resultSet.next()){
                val UUID_Arbitro = resultSet.getString("UUID_Arbitro")
                val Nombre_Arbitro = resultSet.getString("Nombre_Arbitro")
                val Apellido_Arbitro = resultSet.getString("Apellido_Arbitro")
                val Edad_Arbitro = resultSet.getInt("Edad_Arbitro")
                val Telefono_Arbitro = resultSet.getString("Telefono_Arbitro")
                val Foto_Arbitro = resultSet.getString("Foto_Arbitro")

                val valoresJuntos = tbArbitros(UUID_Arbitro, Nombre_Arbitro, Apellido_Arbitro, Edad_Arbitro, Telefono_Arbitro, Foto_Arbitro)
                listaArbitros.add(valoresJuntos)
            }
            return listaArbitros
        }

        CoroutineScope(Dispatchers.IO).launch {
            val arbitrosBD = obtenerArbitros()
            withContext(Dispatchers.Main){
                val adapter = AdapterArbi(arbitrosBD)
                rcvArbitros.adapter = adapter
            }
        }
    }
}