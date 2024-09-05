package marvin.coto.dillosports

import RecyclerView.AdapterTornUser
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

class Torneos_Usuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_torneos_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgCasaUser = findViewById<ImageView>(R.id.imgCasaUser)
        imgCasaUser.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
        val imgPerfilUser = findViewById<ImageView>(R.id.imgPerfilUser)

        imgPerfilUser.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }

        val btnVerCrearTorneoss = findViewById<Button>(R.id.btnVerCrearTorneoss)
        btnVerCrearTorneoss.setOnClickListener {
            val intent: Intent = Intent(this, Crear_Torneo::class.java)
            startActivity(intent)
        }

        val rcvTorneosUser = findViewById<RecyclerView>(R.id.rcvTorneosUser)
        rcvTorneosUser.layoutManager = LinearLayoutManager(this)

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
                val Logo_Torneo = resultSet.getString("Logo_Torneo")

                val valoresJuntos = tbTorneos(UUID_Torneo, Nombre_Torneo, Ubicacion_Torneo, Descripcion_Torneo, Deporte, Estado_Toneo, Logo_Torneo)
                listaTorneos.add(valoresJuntos)
            }
            return listaTorneos
        }

        CoroutineScope(Dispatchers.IO).launch {
            val torneosBDD = obtenerTorneos()
            withContext(Dispatchers.Main){
                val adapter = AdapterTornUser(torneosBDD)
                rcvTorneosUser.adapter = adapter
            }
        }

    }
}