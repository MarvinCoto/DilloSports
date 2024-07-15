package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Ver_Torneo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_torneo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgAtras = findViewById<ImageView>(R.id.imgAtras1)
        imgAtras.setOnClickListener {
            val intent = Intent(this, Torneos::class.java)
            startActivity(intent)
        }

        val imgEquipo = findViewById<ImageView>(R.id.imgEquiposA)
        imgEquipo.setOnClickListener {
            val intent = Intent(this, Equipos::class.java)
            startActivity(intent)
        }

        val imgArbitro = findViewById<ImageView>(R.id.imgArbitrosA)
        imgArbitro.setOnClickListener {
            val intent = Intent(this, Arbitros::class.java)
            startActivity(intent)
        }


        val UUIDRecibido = intent.getStringExtra("UUID_Torneo")
        val nombreRecibido = intent.getStringExtra("NombreTorneo")
        val ubicacionRecibido = intent.getStringExtra("UbicacionTorneo")
        val descripcionRecibido = intent.getStringExtra("DescripcionTorneo")
        val deporteRecibido = intent.getStringExtra("DeporteTorneo")
        val estadoRecibido = intent.getStringExtra("EstadoToneo")
        val textViewNombreTorneo = findViewById<TextView>(R.id.textViewNombreTorneo)
        val textViewUbicacionTorneo = findViewById<TextView>(R.id.textViewUbicacionTorneo)
        val textViewDescripcionTorneo = findViewById<TextView>(R.id.textViewDescripcionTorneo)
        val textViewDeporteTorneo = findViewById<TextView>(R.id.textViewDeporteTorneo)
        val textViewEstadoTorneo = findViewById<TextView>(R.id.textViewEstadoTorneo)
        textViewNombreTorneo.text = nombreRecibido
        textViewUbicacionTorneo.text = ubicacionRecibido
        textViewDescripcionTorneo.text = descripcionRecibido
        textViewDeporteTorneo.text = deporteRecibido
        textViewEstadoTorneo.text = estadoRecibido
    }
}