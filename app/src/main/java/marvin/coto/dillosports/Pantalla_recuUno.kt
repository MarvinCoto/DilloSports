package marvin.coto.dillosports

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Pantalla_recuUno : AppCompatActivity() {
    /*companion object variableGlobalCodigoRecu{
        lateinit var codigoRecu = (1000..9999).random()
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_recu_uno)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtRecuCorreo = findViewById<EditText>(R.id.txtRecuCorreo)
        val btnEnviarCodigo = findViewById<Button>(R.id.btnEnviarCodigo)

        /*btnEnviarCodigo.setOnClickListener {
            val recu = codigoRecu.toString()
            enviarCorreo(
                "${txtRecuCorreo.text}"
                "Recuperación de cuenta"
                "Codigo de Recuperación: $recu"
            )
        }*/
    }
}