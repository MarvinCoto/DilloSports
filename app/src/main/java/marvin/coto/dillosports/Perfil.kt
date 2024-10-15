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
import modelos.tbUsuarios

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
            finish()
        }

        //2-Prueba del companion
        val usuario = tbUsuarios.currentUser
        if (usuario != null) {
            val imageViewPerfil = findViewById<ImageView>(R.id.imageViewPerfil)
            Glide.with(imageViewPerfil.context)
                .load(usuario.Foto_Usuario)
                .into(imageViewPerfil)
            findViewById<TextView>(R.id.textViewNombresTitle).text = usuario.User_name
            findViewById<TextView>(R.id.textViewNombres).text = usuario.Nombre_Usuario
            findViewById<TextView>(R.id.textViewApellidos).text = usuario.Apellido_Usuario
            findViewById<TextView>(R.id.textViewEdad).text = usuario.FNacimiento_Usuario
            findViewById<TextView>(R.id.textViewTelefono).text = usuario.User_name
            findViewById<TextView>(R.id.textViewGenero).text = usuario.Genero
            findViewById<TextView>(R.id.textViewGmail).text = usuario.Correo_Usuario
        } else {
            println("Usuario no encontrado")
        }
    }
}