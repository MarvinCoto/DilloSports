package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        val btnVerCrearPartido = findViewById<ImageView>(R.id.btnVerCrearPartido)
        btnVerCrearPartido.setOnClickListener {
            val intent = Intent(this, Calendarizar_partido::class.java)
            startActivity(intent)
        }
    }
}