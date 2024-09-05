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

class VerJugadores : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_jugadores)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgAtras = findViewById<ImageView>(R.id.imgAtras3)
        imgAtras.setOnClickListener {
            val intent = Intent(this, mostrarJugadores::class.java)
            startActivity(intent)
        }

        val UUIDRecibido = intent.getStringExtra("UUID_Jugador")
        val nombreRecibido = intent.getStringExtra("NombreJugador")
        val apellidoRecibido = intent.getStringExtra("ApellidoJugador")
        val fNacimientoRecibido = intent.getStringExtra("FNacimientoJugador")
        val numeroRecibido = intent.getIntExtra("NumeroJugador", 0)
        val fotoRecibido = intent.getStringExtra("FotoJugador")
        val posicionRecibido = intent.getStringExtra("PosicionJugador")
        val estadoRecibido = intent.getStringExtra("EstadoJugador")
        val img_Jugadores = findViewById<ImageView>(R.id.img_Jugadores)
        val textViewFecha_Jugador = findViewById<TextView>(R.id.textViewFecha_Jugador)
        val textViewNombreJugador = findViewById<TextView>(R.id.textViewNombreJugador)
        val textViewApellidoJugador = findViewById<TextView>(R.id.textViewApellidoJugador)
        val textViewNumeroJugador = findViewById<TextView>(R.id.textViewNumeroJugador)
        val textViewPosicionJugador = findViewById<TextView>(R.id.textViewPosicionJugador)
        val textViewEstadoJugador = findViewById<TextView>(R.id.textViewEstadoJugador)
        textViewNombreJugador.text = nombreRecibido
        textViewApellidoJugador.text = apellidoRecibido
        textViewNumeroJugador.text = numeroRecibido.toString()
        textViewPosicionJugador.text = posicionRecibido
        textViewEstadoJugador.text = estadoRecibido
        textViewFecha_Jugador.text = fNacimientoRecibido
        Glide.with(img_Jugadores.context)
            .load(fotoRecibido)
            .into(img_Jugadores)

    }
}