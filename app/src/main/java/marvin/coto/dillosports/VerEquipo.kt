package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class VerEquipo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_equipo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgAtras = findViewById<ImageView>(R.id.imgAtras4)
        imgAtras.setOnClickListener {
            val intent = Intent(this, Equipos::class.java)
            startActivity(intent)
        }

        val btnVerJugadores = findViewById<Button>(R.id.btnVerJugadoress)
        btnVerJugadores.setOnClickListener {
            val intent = Intent(this, mostrarJugadores::class.java)
            startActivity(intent)
        }

        val UUIDRecibido = intent.getStringExtra("UUID_Equipo")
        val nombreRecibido = intent.getStringExtra("NombreEquipo")
        val ubicacionRecibido = intent.getStringExtra("UbicacionEquipo")
        val descripcionRecibido = intent.getStringExtra("DescripcionEquipo")
        val estadoRecibido = intent.getStringExtra("EstadoEquipo")
        val logoRecibido = intent.getStringExtra("LogoEquipo")
        val imgEquipo = findViewById<ImageView>(R.id.imgEquip)
        val txtNombreEquipo = findViewById<TextView>(R.id.textViewNombreEquipo)
        val txtUbicacionEquipo = findViewById<TextView>(R.id.textViewUbicacionEquipo)
        val txtDescripcionEquipo = findViewById<TextView>(R.id.textViewDescripcionEquipo)
        val txtEstadoEquipo = findViewById<TextView>(R.id.textViewEstadoEquipo)
        Glide.with(imgEquipo.context)
            .load(logoRecibido)
            .into(imgEquipo)
        txtNombreEquipo.text = nombreRecibido
        txtUbicacionEquipo.text = ubicacionRecibido
        txtDescripcionEquipo.text = descripcionRecibido
        txtEstadoEquipo.text = estadoRecibido
    }
}