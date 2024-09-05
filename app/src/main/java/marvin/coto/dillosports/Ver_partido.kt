package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Ver_partido : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_partido)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgAtrasola = findViewById<ImageView>(R.id.imgAtrasola)
        val textViewEquipo1 = findViewById<TextView>(R.id.textViewEquipo1)
        val textViewEquipo2 = findViewById<TextView>(R.id.textViewEquipo2)
        val textViewMarcador1 = findViewById<TextView>(R.id.textViewMarcador1)
        val textViewMarcador2 = findViewById<TextView>(R.id.textViewMarcador2)
        val textViewLugarPart = findViewById<TextView>(R.id.textViewLugarPart)
        val textViewTipoPart = findViewById<TextView>(R.id.textViewTipoPart)
        val textViewArbitro = findViewById<TextView>(R.id.textViewArbitro)
        val textViewFechaPart = findViewById<TextView>(R.id.textViewFechaPart)
        val textHoraPart =  findViewById<TextView>(R.id.textHoraPart)
        val UUIDrecibido = intent.getStringExtra("UUID_Partido")
        val UUID_Equipo1 = intent.getStringExtra("UUID_Equipo1")
        val UUID_Equipo2 = intent.getStringExtra("UUID_Equipo2")
        val UUID_Arbitro = intent.getStringExtra("UUID_Arbitro")
        val Fecha_Partido = intent.getStringExtra("Fecha_Partido")
        val Lugar_Partido = intent.getStringExtra("Lugar_Partido")
        val Hora_Partido = intent.getStringExtra("Hora_Partido")
        val Marcador_Equipo1 = intent.getIntExtra("Marcador_Equipo1", 0)
        val Marcador_Equipo2 = intent.getIntExtra("Marcador_Equipo2", 0)
        val Tipo_Partido = intent.getStringExtra("Tipo_Partido")
        textViewEquipo1.text = UUID_Equipo1
        textViewEquipo2.text = UUID_Equipo2
        textViewMarcador1.text = Marcador_Equipo1.toString()
        textViewMarcador2.text = Marcador_Equipo2.toString()
        textViewLugarPart.text = Lugar_Partido
        textViewTipoPart.text = Tipo_Partido
        textViewArbitro.text = UUID_Arbitro
        textViewFechaPart.text = Fecha_Partido
        textHoraPart.text = Hora_Partido

        imgAtrasola.setOnClickListener {
            val intent: Intent = Intent(this, Partidos::class.java)
            startActivity(intent)
        }
    }
}