package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Ajustes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ajustes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgRedes1 = findViewById<ImageView>(R.id.imgRedes1)
        imgRedes1.setOnClickListener {
            val intent = Intent(this, Redes::class.java)
            startActivity(intent)
        }
        val imgAyuda1 = findViewById<ImageView>(R.id.imgAyuda1)
        imgAyuda1.setOnClickListener {
            val intent = Intent(this, Help::class.java)
            startActivity(intent)
        }
        val btnCerrarSesionReal = findViewById<Button>(R.id.btnCerrarSesionReal)
        btnCerrarSesionReal.setOnClickListener {
            val intent = Intent(this, activity_bienvenida::class.java)
            startActivity(intent)
            finish()
        }
        val imgAtrasdeAjustes = findViewById<ImageView>(R.id.imgAtrasdeAjustes)
        imgAtrasdeAjustes.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
            finish()
        }
    }
}