package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Pantalla_recuDos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_recu_dos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtRecu = findViewById<EditText>(R.id.txtRecu)
        val btnIniciarSesionRecu = findViewById<Button>(R.id.btnIniciarSesionRecu)

        val recuGlobal = Pantalla_recuUno.codigoRecu

        btnIniciarSesionRecu.setOnClickListener {
            if (txtRecu.text.toString() == recuGlobal.toString()){
                val intent = Intent(this, Home::class.java)
                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.toast_normal, null)

                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_LONG
                toast.view = layout

                val text = layout.findViewById<TextView>(R.id.text3)
                text.text = "Código correcto, Bienvenido"

                toast.show()
                startActivity(intent)
            }
            else{
                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.toast_bad, null)

                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_LONG
                toast.view = layout

                val text = layout.findViewById<TextView>(R.id.text2)
                text.text = "Código incorrecto"

                toast.show()
            }
        }
    }
}