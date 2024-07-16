package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        val txtTipoDeporte = findViewById<TextView>(R.id.txtTipoDeporte)
        val txtEstadoTorneo = findViewById<TextView>(R.id.txtEstadoTorneo)
        val btnCrearTorneo = findViewById<Button>(R.id.btnCrearTorneo)

        btnCrearTorneo.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val objConexion = ClaseConexion().cadenaConexion()

                    val addTorneo = objConexion?.prepareStatement("insert into tbTorneos (UUID_Torneo, Nombre_Torneo, Ubicacion_Torneo, Descripcion_Torneo, Deporte, Estado_Toneo) values (?,?,?,?,?,?)")!!
                    addTorneo.setString(1, UUID.randomUUID().toString())
                    addTorneo.setString(2, txtNombreTorneo.text.toString())
                    addTorneo.setString(3, txtUbicacionTorneo.text.toString())
                    addTorneo.setString(4, txtDescripcionTorneo.text.toString())
                    addTorneo.setString(5, txtEstadoTorneo.text.toString())
                    addTorneo.setString(6, txtTipoDeporte.text.toString())
                    addTorneo.executeUpdate()

                    withContext(Dispatchers.Main){
                        Toast.makeText(this@Crear_Torneo, "Torneo Creado", Toast.LENGTH_SHORT).show()
                        txtNombreTorneo.setText("")
                        txtDescripcionTorneo.setText("")
                        txtUbicacionTorneo.setText("")
                        txtTipoDeporte.setText("")
                        txtEstadoTorneo.setText("")
                    }
                }
                val intent: Intent = Intent(this, Torneos::class.java)
                startActivity(intent)

        }
    }
}