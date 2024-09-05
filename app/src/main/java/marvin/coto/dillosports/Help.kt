package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Help : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_help)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgAjustes3 = findViewById<ImageView>(R.id.imgAjustes3)
        imgAjustes3.setOnClickListener {
            val intent = Intent(this, Ajustes::class.java)
            startActivity(intent)
        }
        val imgRedes3 = findViewById<ImageView>(R.id.imgRedes3)
        imgRedes3.setOnClickListener {
            val intent = Intent(this, Redes::class.java)
            startActivity(intent)
        }

    }
}