package marvin.coto.dillosports

import RecyclerView.AdapterEqui
import RecyclerView.AdapterTorn
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.ClaseConexion

class VerEquipo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_equipo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgAtras = findViewById<ImageView>(R.id.imgAtras4)
        imgAtras.setOnClickListener {
            val intent = Intent(this, Equipos::class.java)
            startActivity(intent)
        }

        val btnVerJugadores = findViewById<Button>(R.id.btnVerJugadoress)
        btnVerJugadores.setOnClickListener {
            val intent = Intent(this, mostrarJugadores::class.java)
            startActivity(intent)
        }


        val UUIDRecibido = AdapterEqui.variablesGlobalEqui.UUID_Equipo
        val nombreRecibido = AdapterEqui.variablesGlobalEqui.Nombre_Equipo
        val ubicacionRecibido = AdapterEqui.variablesGlobalEqui.Ubicacion_Equipo
        val descripcionRecibido = AdapterEqui.variablesGlobalEqui.Descripcion_Equipo
        val estadoRecibido = AdapterEqui.variablesGlobalEqui.UUID_Estado_Equipo
        val logoRecibido = AdapterEqui.variablesGlobalEqui.Logo_Equipo
        val imgEquipo = findViewById<ImageView>(R.id.imgEquip)
        val txtNombreEquipo = findViewById<TextView>(R.id.textViewNombreEquipo)
        val txtUbicacionEquipo = findViewById<TextView>(R.id.textViewUbicacionEquipo)
        val txtDescripcionEquipo = findViewById<TextView>(R.id.textViewDescripcionEquipo)
        val txtEstadoEquipo = findViewById<TextView>(R.id.textViewEstadoEquipo)
        val textViewEquienEqui = findViewById<TextView>(R.id.textViewEquienEqui)
        textViewEquienEqui.text = nombreRecibido
        Glide.with(imgEquipo.context)
            .load(logoRecibido)
            .into(imgEquipo)
        txtNombreEquipo.text = nombreRecibido
        txtUbicacionEquipo.text = ubicacionRecibido
        txtDescripcionEquipo.text = descripcionRecibido

        fun obtenerDatoUUID(UUID_Estado_Equipo: String): String? {
            val objConexion = ClaseConexion().cadenaConexion()

            val query = "select Estado_Equipo from tbEstadoEquipo where UUID_Estado_Equipo = ?"
            val preparedStatement = objConexion?.prepareStatement(query)
            preparedStatement?.setString(1, UUID_Estado_Equipo)
            val resultSet = preparedStatement?.executeQuery()

            var estadoEquipo: String? = null

            if (resultSet?.next() == true) {
                estadoEquipo = resultSet.getString("Estado_Equipo")
            }

            return estadoEquipo
        }

        CoroutineScope(Dispatchers.IO).launch {
            val resultado = obtenerDatoUUID(estadoRecibido)

            withContext(Dispatchers.Main) {
                txtEstadoEquipo.text = resultado ?: "Sin datos"
            }
        }
    }
}