package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Perfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgHome1 = findViewById<ImageView>(R.id.imgHome1)
        imgHome1.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        val imgTorneo1 = findViewById<ImageView>(R.id.imgTorneo1)
        imgTorneo1.setOnClickListener {
            val intent = Intent(this, Torneos::class.java)
            startActivity(intent)
        }
    }
}