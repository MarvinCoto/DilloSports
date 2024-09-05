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

        val imgAjustes43 = findViewById<ImageView>(R.id.imgAjustes43)
        imgAjustes43.setOnClickListener {
            val intent = Intent (this, Ajustes::class.java)
            startActivity(intent)
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

        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, activity_bienvenida::class.java)
            startActivity(intent)
        }

        val textViewNombresTitle = findViewById<TextView>(R.id.textViewNombresTitle)
        val textViewNombres = findViewById<TextView>(R.id.textViewNombres)
        val textViewApellidos = findViewById<TextView>(R.id.textViewApellidos)
        val textViewEdad = findViewById<TextView>(R.id.textViewEdad)
        val textViewTelefono = findViewById<TextView>(R.id.textViewTelefono)
        val textViewGenero = findViewById<TextView>(R.id.textViewGenero)
        val textViewGmail = findViewById<TextView>(R.id.textViewGmail)
    }
}