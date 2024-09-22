package marvin.coto.dillosports

import RecyclerView.AdapterEqui
import RecyclerView.AdapterTorn
import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.ClaseConexion
import modelos.tbEquipos
import modelos.tbTorneos

class Equipos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_equipos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgPartidosB = findViewById<ImageView>(R.id.imgPartidosB)
        imgPartidosB.setOnClickListener {
            val intent = Intent(this, Partidos::class.java)
            startActivity(intent)
        }

        val imgTorneoB = findViewById<ImageView>(R.id.imgTorneoB)
        imgTorneoB.setOnClickListener {
            val intent = Intent(this, Ver_Torneo::class.java)
            startActivity(intent)
        }

        val imgArbitroB = findViewById<ImageView>(R.id.imgArbitrosB)
        imgArbitroB.setOnClickListener {
            val intent = Intent(this, Arbitros::class.java)
            startActivity(intent)
        }

        val btnVerCrearEquipo = findViewById<Button>(R.id.btnVerCrearEquipo)
        btnVerCrearEquipo.setOnClickListener {
            val intent = Intent(this, inscribir_equipo::class.java)
            startActivity(intent)
        }

        val imgIrEquipo2 = findViewById<ImageView>(R.id.imgirCrearTorn2)
        imgIrEquipo2.setOnClickListener {
            val intent = Intent(this, inscribir_equipo::class.java)
            startActivity(intent)
        }
        val recibirNameTorn = AdapterTorn.variablesGlobalTorn.Nombre_Torneo
        val textViewNameTornEquip = findViewById<TextView>(R.id.textViewNameTornEquip)
        textViewNameTornEquip.text = recibirNameTorn

        fun obtenerEquipos(): List<tbEquipos> {
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbEquipos")!!
            val listaEquipos = mutableListOf<tbEquipos>()

            while(resultSet.next()){
                val UUID_Equipo = resultSet.getString("UUID_Equipo")
                val Nombre_Equipo = resultSet.getString("Nombre_Equipo")
                val Descripcion_Equipo = resultSet.getString("Descripcion_Equipo")
                val Ubicacion_Equipo = resultSet.getString("Ubicacion_Equipo")
                val Logo_Equipo = resultSet.getString("Logo_Equipo")
                val UUID_Estado_Equipo = resultSet.getString("UUID_Estado_Equipo")

                val valoresJuntos = tbEquipos(UUID_Equipo, Nombre_Equipo, Descripcion_Equipo, Ubicacion_Equipo, Logo_Equipo, UUID_Estado_Equipo)
                listaEquipos.add(valoresJuntos)
            }
            return listaEquipos
        }

        val rcvEquipos = findViewById<RecyclerView>(R.id.rcvEquipos)
        rcvEquipos.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            val equiposBD = obtenerEquipos()
            withContext(Dispatchers.Main){
                val adapter = AdapterEqui(equiposBD)
                rcvEquipos.adapter = adapter
            }
        }

    }
}