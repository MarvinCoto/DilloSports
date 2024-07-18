package marvin.coto.dillosports

import RecyclerView.AdapterArbi
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
import modelos.tbArbitros

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
        val imgEquiposC = findViewById<ImageView>(R.id.imgEquiposC)
        imgEquiposC.setOnClickListener {
            val intent = Intent(this, Equipos::class.java)
            startActivity(intent)
        }
        val imgTorneosC = findViewById<ImageView>(R.id.imgTorneosC)
        imgTorneosC.setOnClickListener {
            val intent = Intent(this, Ver_Torneo::class.java)
            startActivity(intent)
        }

        val btnVerCrearArbitros = findViewById<Button>(R.id.btnVerCrearArbitros)
        btnVerCrearArbitros.setOnClickListener {
            val intent: Intent = Intent(this, InscribirArbitro::class.java)
            startActivity(intent)
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

                val valoresJuntos = tbArbitros(UUID_Arbitro, Nombre_Arbitro, Apellido_Arbitro, Edad_Arbitro, Telefono_Arbitro)
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