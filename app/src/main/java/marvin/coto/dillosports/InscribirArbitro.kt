package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

class InscribirArbitro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscribir_arbitro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtNombreArbitro = findViewById<TextView>(R.id.txtNombreArbitro)
        val txtApellidoArbitro = findViewById<TextView>(R.id.txtApellidoArbitro)
        val txtEdadArbitro = findViewById<TextView>(R.id.txtEdadArbitro)
        val txtTelefonoArbitro = findViewById<TextView>(R.id.txtTelefonoArbitro)
        val btnInscribirArbitro = findViewById<Button>(R.id.btnInscribirArbitro)

        btnInscribirArbitro.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = ClaseConexion().cadenaConexion()

                val addArbitro = objConexion?.prepareStatement("insert into tbArbitros1 (UUID_Arbitro, Nombre_Arbitro, Apellido_Arbitro, Edad_Arbitro, Telefono_Arbitro) values (?,?,?,?,?)")!!
                addArbitro.setString(1, UUID.randomUUID().toString())
                addArbitro.setString(2, txtNombreArbitro.text.toString())
                addArbitro.setString(3, txtApellidoArbitro.text.toString())
                addArbitro.setInt(4, txtEdadArbitro.text.toString().toInt())
                addArbitro.setString(5, txtTelefonoArbitro.text.toString())
                addArbitro.executeUpdate()

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@InscribirArbitro, "Arbitro Inscrito", Toast.LENGTH_SHORT).show()
                    txtNombreArbitro.setText("")
                    txtApellidoArbitro.setText("")
                    txtEdadArbitro.setText("")
                    txtTelefonoArbitro.setText("")
                }
            }
            val intent: Intent = Intent(this, Arbitros::class.java)
            startActivity(intent)
        }
    }
}