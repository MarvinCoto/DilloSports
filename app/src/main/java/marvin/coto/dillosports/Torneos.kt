package marvin.coto.dillosports

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
import modelos.tbTorneos

class Torneos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_torneos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgHomeT = findViewById<ImageView>(R.id.img)
        imgHomeT.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        val imgPerfilT = findViewById<ImageView>(R.id.imgPerfilT)
        imgPerfilT.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }

        val btnVerCrearTorneo = findViewById<Button>(R.id.btnVerCrearTorneo)
        btnVerCrearTorneo.setOnClickListener {
            val intent: Intent = Intent(this, Crear_Torneo::class.java)
            startActivity(intent)
        }

        val rcvTorneos = findViewById<RecyclerView>(R.id.rcvTorneos)
        rcvTorneos.layoutManager = LinearLayoutManager(this)

        fun obtenerTorneos(): List<tbTorneos>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbTorneos")!!
            val listaTorneos = mutableListOf<tbTorneos>()

            while(resultSet.next()){
                val UUID_Torneo = resultSet.getString("UUID_Torneo")
                val Nombre_Torneo = resultSet.getString("Nombre_Torneo")
                val Ubicacion_Torneo = resultSet.getString("Ubicacion_Torneo")
                val Descripcion_Torneo = resultSet.getString("Descripcion_Torneo")
                val Deporte = resultSet.getString("Deporte")
                val Estado_Toneo = resultSet.getString("Estado_Toneo")

                val valoresJuntos = tbTorneos(UUID_Torneo, Nombre_Torneo, Ubicacion_Torneo, Descripcion_Torneo, Deporte, Estado_Toneo)
                listaTorneos.add(valoresJuntos)
            }
            return listaTorneos
        }

        CoroutineScope(Dispatchers.IO).launch {
            val torneosBD = obtenerTorneos()
            withContext(Dispatchers.Main){
                val adapter = AdapterTorn(torneosBD)
                rcvTorneos.adapter = adapter
            }
        }
    }
}