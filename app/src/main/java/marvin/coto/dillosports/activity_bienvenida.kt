package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_bienvenida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bienvenida)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnIrAlRegistroBienvenida = findViewById<Button>(R.id.btnIrAlRegistroBienvenida)
        val btnIrAIniciarSesionBienvenida = findViewById<Button>(R.id.btnIrAIniciarSesionBienvenida)

        btnIrAlRegistroBienvenida.setOnClickListener{
            val pantallaRegistro = Intent(this, activity_login::class.java)
            startActivity(pantallaRegistro)
        }

        btnIrAIniciarSesionBienvenida.setOnClickListener{
            val pantallaLogin = Intent(this, activity_iniciar_sesion::class.java)
            startActivity(pantallaLogin)
        }
    }
}