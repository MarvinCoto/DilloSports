package marvin.coto.dillosports

import RecyclerView.AdapterPart
import RecyclerView.AdapterTorn
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.ClaseConexion

class Ver_partido : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_partido)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgAtrasola = findViewById<ImageView>(R.id.imgAtrasola)
        imgAtrasola.setOnClickListener {
            val intent = Intent(this, Partidos::class.java)
            startActivity(intent)
        }

        val textViewEquipo1 = findViewById<TextView>(R.id.textViewEquipo1)
        val textViewEquipo2 = findViewById<TextView>(R.id.textViewEquipo2)
        val textViewMarcador1 = findViewById<TextView>(R.id.textViewMarcador1)
        val textViewMarcador2 = findViewById<TextView>(R.id.textViewMarcador2)
        val textViewLugarPart = findViewById<TextView>(R.id.textViewLugarPart)
        val textViewTipoPart = findViewById<TextView>(R.id.textViewTipoPart)
        val textViewArbitro = findViewById<TextView>(R.id.textViewArbitro)
        val textViewFechaPart = findViewById<TextView>(R.id.textViewFechaPart)
        val textHoraPart =  findViewById<TextView>(R.id.textHoraPart)
        val nombreRecibido = AdapterTorn.variablesGlobalTorn.Nombre_Torneo
        val UUIDrecibido = AdapterPart.variablesGlobalPart.UUID_Partido
        val UUID_Equipo1 = AdapterPart.variablesGlobalPart.UUID_Equipo1
        val UUID_Equipo2 = AdapterPart.variablesGlobalPart.UUID_Equipo2
        val UUID_Arbitro = AdapterPart.variablesGlobalPart.UUID_Arbitro
        val Fecha_Partido = AdapterPart.variablesGlobalPart.Fecha_Partido
        val Lugar_Partido = AdapterPart.variablesGlobalPart.Lugar_Partido
        val Hora_Partido = AdapterPart.variablesGlobalPart.Hora_Partido
        val textViewNombreTornxd = findViewById<TextView>(R.id.textViewNombreTornxd)
        val Marcador_Equipo1 = AdapterPart.variablesGlobalPart.Marcador_Equipo1
        val Marcador_Equipo2 = AdapterPart.variablesGlobalPart.Marcador_Equipo2
        val Tipo_Partido = AdapterPart.variablesGlobalPart.Tipo_Partido
        textViewMarcador1.text = Marcador_Equipo1.toString()
        textViewMarcador2.text = Marcador_Equipo2.toString()
        textViewLugarPart.text = Lugar_Partido
        textViewTipoPart.text = Tipo_Partido
        textViewFechaPart.text = Fecha_Partido
        textHoraPart.text = Hora_Partido
        textViewNombreTornxd.text = nombreRecibido

        imgAtrasola.setOnClickListener {
            val intent = Intent(this, Partidos::class.java)
            startActivity(intent)
        }

        fun obtenerDatoUUIDArbi(UUID_Arbitro: String): String? {
            val objConexion = ClaseConexion().cadenaConexion()

            val query = "select Nombre_Arbitro from tbArbitros where UUID_Arbitro = ?"
            val preparedStatement = objConexion?.prepareStatement(query)
            preparedStatement?.setString(1, UUID_Arbitro)
            val resultSet = preparedStatement?.executeQuery()

            var nombreArbi: String? = null

            if (resultSet?.next() == true) {
                nombreArbi = resultSet.getString("Nombre_Arbitro")
            }

            return nombreArbi
        }

        CoroutineScope(Dispatchers.IO).launch {
            val resultado = obtenerDatoUUIDArbi(UUID_Arbitro)

            withContext(Dispatchers.Main) {
                textViewArbitro.text = resultado ?: "Sin datos"
            }
        }

        fun obtenerDatoUUIDEquipUn(UUID_Equipo: String): String? {
            val objConexion = ClaseConexion().cadenaConexion()

            val query = "select Nombre_Equipo from tbEquipos where UUID_Equipo = ?"
            val preparedStatement = objConexion?.prepareStatement(query)
            preparedStatement?.setString(1, UUID_Equipo)
            val resultSet = preparedStatement?.executeQuery()

            var nombreEquipUn: String? = null

            if (resultSet?.next() == true) {
                nombreEquipUn = resultSet.getString("Nombre_Equipo")
            }

            return nombreEquipUn
        }

        CoroutineScope(Dispatchers.IO).launch {
            val resultado = obtenerDatoUUIDEquipUn(UUID_Equipo1)

            withContext(Dispatchers.Main) {
                textViewEquipo1.text = resultado ?: "Sin datos"
            }
        }

        fun obtenerDatoUUIDEquipDo(UUID_Equipo: String): String? {
            val objConexion = ClaseConexion().cadenaConexion()

            val query = "select Nombre_Equipo from tbEquipos where UUID_Equipo = ?"
            val preparedStatement = objConexion?.prepareStatement(query)
            preparedStatement?.setString(1, UUID_Equipo)
            val resultSet = preparedStatement?.executeQuery()

            var nombreEquipDo: String? = null

            if (resultSet?.next() == true) {
                nombreEquipDo = resultSet.getString("Nombre_Equipo")
            }

            return nombreEquipDo
        }

        CoroutineScope(Dispatchers.IO).launch {
            val resultado = obtenerDatoUUIDEquipDo(UUID_Equipo2)

            withContext(Dispatchers.Main) {
                textViewEquipo2.text = resultado ?: "Sin datos"
            }
        }
    }
}