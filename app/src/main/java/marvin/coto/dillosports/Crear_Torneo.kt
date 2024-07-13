package marvin.coto.dillosports

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import modelos.ClaseConexion
import java.util.UUID

class Crear_Torneo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_torneo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtNombreTorneo = findViewById<TextView>(R.id.txtNombreTorneo)
        val txtDescripcionTorneo = findViewById<TextView>(R.id.txtDescripcionTorneo)
        val txtUbicacionTorneo = findViewById<TextView>(R.id.txtUbicacionTorneo)
        val btnCrearTorneo = findViewById<Button>(R.id.btnCrearTorneo)

    }
}