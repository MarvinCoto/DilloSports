package marvin.coto.dillosports

import RecyclerView.Adapter
import RecyclerView.AdapterEqui
import RecyclerView.AdapterTorn
import android.content.Intent
import android.os.Bundle
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

class VerJugadores : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_jugadores)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgAtras = findViewById<ImageView>(R.id.imgAtras3)
        imgAtras.setOnClickListener {
            val intent = Intent(this, mostrarJugadores::class.java)
            startActivity(intent)
        }
        val nombreRecibidoxd = AdapterEqui.variablesGlobalEqui.Nombre_Equipo
        val textViewEquipxd = findViewById<TextView>(R.id.textViewEquipxd)
        textViewEquipxd.text = nombreRecibidoxd

        val UUIDRecibido = Adapter.variablesGlobalJug.UUID_Jugador
        val nombreRecibido = Adapter.variablesGlobalJug.Nombre_Jugador
        val apellidoRecibido = Adapter.variablesGlobalJug.Apellido_Jugador
        val fNacimientoRecibido = Adapter.variablesGlobalJug.FNacimiento_Jugador
        val numeroRecibido = Adapter.variablesGlobalJug.Numero_Jugador
        val fotoRecibido = Adapter.variablesGlobalJug.Foto_Jugador
        val posicionRecibido = Adapter.variablesGlobalJug.Posicion_Jugador
        val estadoRecibido = Adapter.variablesGlobalJug.UUID_Estado_Jugador
        val img_Jugadores = findViewById<ImageView>(R.id.img_Jugadores)
        val textViewFecha_Jugador = findViewById<TextView>(R.id.textViewFecha_Jugador)
        val textViewNombreJugador = findViewById<TextView>(R.id.textViewNombreJugador)
        val textViewApellidoJugador = findViewById<TextView>(R.id.textViewApellidoJugador)
        val textViewNumeroJugador = findViewById<TextView>(R.id.textViewNumeroJugador)
        val textViewPosicionJugador = findViewById<TextView>(R.id.textViewPosicionJugador)
        val textViewEstadoJugador = findViewById<TextView>(R.id.textViewEstadoJugador)
        textViewNombreJugador.text = nombreRecibido
        textViewApellidoJugador.text = apellidoRecibido
        textViewNumeroJugador.text = numeroRecibido.toString()
        textViewPosicionJugador.text = posicionRecibido
        textViewFecha_Jugador.text = fNacimientoRecibido
        Glide.with(img_Jugadores.context)
            .load(fotoRecibido)
            .into(img_Jugadores)

        fun obtenerDatoUUID(UUID_Estado_Jugador: String): String? {
            val objConexion = ClaseConexion().cadenaConexion()

            val query = "select Estado_Jugador from tbEstadoJugador where UUID_Estado_Jugador = ?"
            val preparedStatement = objConexion?.prepareStatement(query)
            preparedStatement?.setString(1, UUID_Estado_Jugador)
            val resultSet = preparedStatement?.executeQuery()

            var estadoJugador: String? = null

            if (resultSet?.next() == true) {
                estadoJugador = resultSet.getString("Estado_Jugador")
            }

            return estadoJugador
        }

        CoroutineScope(Dispatchers.IO).launch {
            val resultado = obtenerDatoUUID(estadoRecibido)

            withContext(Dispatchers.Main) {
                textViewEstadoJugador.text = resultado ?: "Sin datos"
            }
        }

    }
}