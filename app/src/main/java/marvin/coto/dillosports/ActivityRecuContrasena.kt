package marvin.coto.dillosports

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityRecuContrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recu_contrasena)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnEnviar = findViewById<Button>(R.id.btnEnviar)

        btnEnviar.setOnClickListener{

            CoroutineScope(Dispatchers.Main).launch {

                val Codigorecuperacion = (100000..999999).random()

                enviarCorreo(
                    "20200438@ricaldone.edu.sv",
                    "Recuperacion de Contraseña",
                    "Este es tu codigo de Recuperacion $Codigorecuperacion"
                )
            }
        }
    }
}