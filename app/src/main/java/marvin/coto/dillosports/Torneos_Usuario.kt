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



    }
}