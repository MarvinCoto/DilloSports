package marvin.coto.dillosports

import RecyclerView.AdapterTorn
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class VerArbitro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_arbitro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgAtras = findViewById<ImageView>(R.id.imgAtras2)
        imgAtras.setOnClickListener {
            val intent = Intent(this, Arbitros::class.java)
            startActivity(intent)
        }
        val eeeRibido = AdapterTorn.variablesGlobalTorn.Nombre_Torneo
        val textViewNombredelTornya = findViewById<TextView>(R.id.textViewNombredelTornya)
        textViewNombredelTornya.text = eeeRibido

        val UUIDRecibido = intent.getStringExtra("UUID_Arbitro")
        val nombreRecibido = intent.getStringExtra("NombreArbitro")
        val apellidoRecibido = intent.getStringExtra("ApellidoArbitro")
        val edadRecibido = intent.getIntExtra("EdadArbitro", 0)
        val telefonoRecibido = intent.getStringExtra("TelefonoArbitro")
        val fotoRecibida = intent.getStringExtra("FotoArbitro")
        val imgArbi = findViewById<ImageView>(R.id.imgArbi)
        val textViewNombreArbitro = findViewById<TextView>(R.id.textViewNombreArbitro)
        val textViewApellidoArbitro = findViewById<TextView>(R.id.textViewApellidoArbitro)
        val textViewEdadArbitro = findViewById<TextView>(R.id.textViewEdadArbitro)
        val textViewTelefonoArbitro = findViewById<TextView>(R.id.textViewTelefonoArbitro)
        textViewNombreArbitro.text = nombreRecibido
        textViewApellidoArbitro.text = apellidoRecibido
        textViewEdadArbitro.text = edadRecibido.toString()
        textViewTelefonoArbitro.text = telefonoRecibido
        Glide.with(imgArbi.context)
            .load(fotoRecibida)
            .into(imgArbi)

    }
}