package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Redes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_redes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgAjustes2 = findViewById<ImageView>(R.id.imgAjustes2)
        imgAjustes2.setOnClickListener {
            val intent = Intent(this, Ajustes::class.java)
            startActivity(intent)
        }
        val imgAyuda2 = findViewById<ImageView>(R.id.imgAyuda2)
        imgAyuda2.setOnClickListener {
            val intent = Intent(this, Help::class.java)
            startActivity(intent)
        }
        val imgAtrasdeRedes = findViewById<ImageView>(R.id.imgAtrasdeRedes)
        imgAtrasdeRedes.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }

    }
}