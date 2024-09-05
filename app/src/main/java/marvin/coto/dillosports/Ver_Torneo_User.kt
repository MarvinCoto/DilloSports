package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class Ver_Torneo_User : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_torneo_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgAtrasUser = findViewById<ImageView>(R.id.imgAtrasUser)
        imgAtrasUser.setOnClickListener {
            val intent = Intent(this, Torneos_Usuario::class.java)
            startActivity(intent)
        }
        val UUIDRecibido = intent.getStringExtra("UUID_Torneo")
        val nombreRecibido = intent.getStringExtra("NombreTorneo")
        val ubicacionRecibido = intent.getStringExtra("UbicacionTorneo")
        val descripcionRecibido = intent.getStringExtra("DescripcionTorneo")
        val deporteRecibido = intent.getStringExtra("DeporteTorneo")
        val estadoRecibido = intent.getStringExtra("EstadoToneo")
        val logoRecibido = intent.getStringExtra("LogoTorneo")
        val textViewNombreTorneoUser = findViewById<TextView>(R.id.textViewNombreTorneoUser)
        val textViewUbicacionTorneoUser = findViewById<TextView>(R.id.textViewUbicacionTorneoUser)
        val textViewDescripcionTorneoUser = findViewById<TextView>(R.id.textViewDescripcionTorneoUser)
        val textViewDeporteTorneoUser = findViewById<TextView>(R.id.textViewDeporteTorneoUser)
        val textViewEstadoTorneoUser = findViewById<TextView>(R.id.textViewEstadoTorneoUser)
        val imgTornUser = findViewById<ImageView>(R.id.imgTornUser)
        textViewNombreTorneoUser.text = nombreRecibido
        textViewUbicacionTorneoUser.text = ubicacionRecibido
        textViewDescripcionTorneoUser.text = descripcionRecibido
        textViewDeporteTorneoUser.text = deporteRecibido
        textViewEstadoTorneoUser.text = estadoRecibido
        Glide.with(imgTornUser.context)
            .load(logoRecibido)
            .into(imgTornUser)

    }
}