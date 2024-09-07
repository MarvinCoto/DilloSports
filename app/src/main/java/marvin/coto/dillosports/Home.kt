package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import modelos.ClaseConexion

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgTorneo = findViewById<ImageView>(R.id.imgTorneoHome)
        imgTorneo.setOnClickListener {
            val intent = Intent(this, Torneos::class.java)
            startActivity(intent)
        }

        val imgPerfil = findViewById<ImageView>(R.id.imgPerfilH)
        imgPerfil.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }

        /*fun obtenerRol(): List<tbUsuario>{
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbUsuarios")!!
            val listaUsuarios = mutableListOf<tbUsuario>()

            while(resultSet.next()){
                val UUID_Usuario = resultSet.getString("UUID_Usuario")
                val UUID_Tipo_Usuario = resultSet.getString("UUID_Tipo_Usuario")
                val valoresJuntos = tbUsuario(UUID_Usuario, UUID_Tipo_Usuario)
                listaUsuarios.add(valoresJuntos)
            }
            return listaUsuarios
        }*/
    }
}