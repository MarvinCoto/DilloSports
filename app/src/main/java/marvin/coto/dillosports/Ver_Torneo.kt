package marvin.coto.dillosports

import RecyclerView.AdapterTorn
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.ClaseConexion
import modelos.tbDeportes
import modelos.tbEstadoTorneo
import modelos.tbTorneos

class Ver_Torneo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_torneo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgAtras = findViewById<ImageView>(R.id.imgAtras1)
        imgAtras.setOnClickListener {
            val intent = Intent(this, Torneos::class.java)
            startActivity(intent)
        }

        val imgPartidosA = findViewById<ImageView>(R.id.imgPartidosA)
        imgPartidosA.setOnClickListener {
            val intent = Intent(this, Partidos::class.java)
            startActivity(intent)
        }

        val imgEquipo = findViewById<ImageView>(R.id.imgEquiposA)
        imgEquipo.setOnClickListener {
            val intent = Intent(this, Equipos::class.java)
            startActivity(intent)
        }

        val imgArbitro = findViewById<ImageView>(R.id.imgArbitrosA)
        imgArbitro.setOnClickListener {
            val intent = Intent(this, Arbitros::class.java)
            startActivity(intent)
        }


        val UUIDRecibido = AdapterTorn.variablesGlobalTorn.UUID_Torneo
        val nombreRecibido = AdapterTorn.variablesGlobalTorn.Nombre_Torneo
        val ubicacionRecibido = AdapterTorn.variablesGlobalTorn.Ubicacion_Torneo
        val descripcionRecibido = AdapterTorn.variablesGlobalTorn.Descripcion_Torneo
        val deporteRecibido = AdapterTorn.variablesGlobalTorn.UUID_Tipo_Deporte
        val estadoRecibido = AdapterTorn.variablesGlobalTorn.UUID_Estado_Torneo
        val logoRecibido = AdapterTorn.variablesGlobalTorn.Logo_Torneo
        val imgTorn = findViewById<ImageView>(R.id.imgTorn)
        val textViewNombreTorneo2 = findViewById<TextView>(R.id.textViewNombreTorneo2)
        val textViewNombreTorneo = findViewById<TextView>(R.id.textViewNombreTorneo)
        val textViewUbicacionTorneo = findViewById<TextView>(R.id.textViewUbicacionTorneo)
        val textViewDescripcionTorneo = findViewById<TextView>(R.id.textViewDescripcionTorneo)
        val textViewDeporteTorneo = findViewById<TextView>(R.id.textViewDeporteTorneo)
        val textViewEstadoTorneo = findViewById<TextView>(R.id.textViewEstadoTorneo)
        Glide.with(imgTorn.context)
            .load(logoRecibido)
            .into(imgTorn)
        textViewNombreTorneo.text = nombreRecibido
        textViewNombreTorneo2.text = nombreRecibido
        textViewUbicacionTorneo.text = ubicacionRecibido
        textViewDescripcionTorneo.text = descripcionRecibido

        fun DatoUUIDEstado(UUID_Estado_Torneo: String): String? {
            val objConexion = ClaseConexion().cadenaConexion()

            val query = "select Nombre_Estado from tbEstadoTorneo where UUID_Estado_Torneo = ?"
            val preparedStatement = objConexion?.prepareStatement(query)
            preparedStatement?.setString(1, UUID_Estado_Torneo)
            val resultSet = preparedStatement?.executeQuery()

            var nombreEstado: String? = null

            if (resultSet?.next() == true) {
                nombreEstado = resultSet.getString("Nombre_Estado")
            }

            return nombreEstado
        }

        CoroutineScope(Dispatchers.IO).launch {
            val resultado = DatoUUIDEstado(estadoRecibido)

            withContext(Dispatchers.Main) {
                textViewEstadoTorneo.text = resultado ?: "Sin datos"
            }
        }


        fun DatoUUIDDeporte(UUID_Tipo_Deporte: String): String? {
            val objConexion = ClaseConexion().cadenaConexion()

            val query = "select Nombre_Tipo_Deporte from tbTipoDeporte where UUID_Tipo_Deporte = ?"
            val preparedStatement = objConexion?.prepareStatement(query)
            preparedStatement?.setString(1, UUID_Tipo_Deporte)
            val resultSet = preparedStatement?.executeQuery()

            var nombreDeporte: String? = null

            if (resultSet?.next() == true) {
                nombreDeporte = resultSet.getString("Nombre_Tipo_Deporte")
            }

            return nombreDeporte
        }

        CoroutineScope(Dispatchers.IO).launch {
            val resultado = DatoUUIDDeporte(deporteRecibido)

            withContext(Dispatchers.Main) {
                textViewDeporteTorneo.text = resultado ?: "Sin datos"
            }
        }

        suspend fun obtenerDeporte(): List<tbDeportes>{
            return withContext(Dispatchers.IO) {
                val objConexion = ClaseConexion().cadenaConexion()

                val statement = objConexion?.createStatement()
                val resultSet = statement?.executeQuery("SELECT * FROM tbTipoDeporte")!!
                val listaDeportes = mutableListOf<tbDeportes>()

                while (resultSet.next() == true) {
                    val uuidDeporte = resultSet.getString("UUID_Tipo_Deporte")
                    val nombreDeporte = resultSet.getString("Nombre_Tipo_Deporte")
                    val unDeporte = tbDeportes(uuidDeporte, nombreDeporte)
                    listaDeportes.add(unDeporte)
                }
                resultSet?.close()
                statement?.close()
                objConexion?.close()

                listaDeportes
            }
        }

        suspend fun obtenerEstado(): List<tbEstadoTorneo>{
            return withContext(Dispatchers.IO) {
                val objConexion = ClaseConexion().cadenaConexion()

                val statement = objConexion?.createStatement()
                val resultSet = statement?.executeQuery("SELECT * FROM tbEstadoTorneo")!!
                val listaEstados = mutableListOf<tbEstadoTorneo>()

                while (resultSet.next() == true) {
                    val uuidEstado = resultSet.getString("UUID_Estado_Torneo")
                    val nombreEstado = resultSet.getString("Nombre_Estado")
                    val unEstado = tbEstadoTorneo(uuidEstado, nombreEstado)
                    listaEstados.add(unEstado)
                }
                resultSet?.close()
                statement?.close()
                objConexion?.close()

                listaEstados
            }
        }


        val btnEliminarTorneo = findViewById<TextView>(R.id.btnEliminarTorneo)
        val btnEditarTorneo = findViewById<TextView>(R.id.btnEditarTorneo)

        fun eliminarDatos(Nombre_Torneo: String, UUID_Torneo: String){

            GlobalScope.launch(Dispatchers.IO){
                val objConexion = ClaseConexion().cadenaConexion()

                val deleteTorneo = objConexion?.prepareStatement("delete tbTorneos where Nombre_Torneo = ? and UUID_Torneo = ?")!!
                deleteTorneo.setString(1, Nombre_Torneo)
                deleteTorneo.setString(2, UUID_Torneo)
                deleteTorneo.executeUpdate()

                val commit = objConexion.prepareStatement("commit")
                commit?.executeUpdate()
            }
        }

        fun editarDatos(nuevoNombre: String, nuevoUbicacion: String, nuevoDescripcion: String, nuevoEstadoTorneo: String, nuevoTipoDeporte: String, UUID_Torneo: String){
            GlobalScope.launch(Dispatchers.IO){
                val objConexion = ClaseConexion().cadenaConexion()

                val updateTorneo = objConexion?.prepareStatement("update tbTorneos set Nombre_Torneo =?, Ubicacion_Torneo =?, Descripcion_Torneo =?, UUID_Estado_Torneo =?, UUID_Tipo_Deporte =? where UUID_Torneo =?")!!
                updateTorneo.setString(1, nuevoNombre)
                updateTorneo.setString(2, nuevoUbicacion)
                updateTorneo.setString(3, nuevoDescripcion)
                updateTorneo.setString(4, nuevoTipoDeporte)
                updateTorneo.setString(5, nuevoEstadoTorneo)
                updateTorneo.setString(6, UUID_Torneo)
                updateTorneo.executeUpdate()

                val commit = objConexion.prepareStatement("commit")
                commit?.executeUpdate()
            }
        }


        fun showEditTorneoDialog() {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.alertdialog_edit_torneos, null)

            val txtEditar_Nombre_Torn = dialogLayout.findViewById<EditText>(R.id.txtEditar_Nombre_Torn)
            val txtDescripcion_Torn = dialogLayout.findViewById<EditText>(R.id.txtDescripcion_Torn)
            val txtUbicacion_Torn = dialogLayout.findViewById<EditText>(R.id.txtUbicacion_Torn)
            val spTipoDeporte_Torn = dialogLayout.findViewById<Spinner>(R.id.spTipoDeporte_Torn)
            val spEstado_Torn = dialogLayout.findViewById<Spinner>(R.id.spEstado_Torn)

            CoroutineScope(Dispatchers.IO).launch {
                val listaDeportes = obtenerDeporte()
                val nombreDeporte = listaDeportes.map { it.Nombre_Tipo_Deporte }

                withContext(Dispatchers.Main){
                    val miAdaptador = ArrayAdapter(this@Ver_Torneo, android.R.layout.simple_spinner_dropdown_item, nombreDeporte)
                    spTipoDeporte_Torn.adapter = miAdaptador
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                val listaEstados = obtenerEstado()
                val nombreEstado = listaEstados.map { it.Nombre_Estado }

                withContext(Dispatchers.Main){
                    val miAdaptador = ArrayAdapter(this@Ver_Torneo, android.R.layout.simple_spinner_dropdown_item, nombreEstado)
                    spEstado_Torn.adapter = miAdaptador
                }
            }

            builder.setView(dialogLayout)
            val alertDialog = builder.create()

            dialogLayout.findViewById<Button>(R.id.btnCancelarEditarTorn).setOnClickListener {
                alertDialog.dismiss()
            }

            dialogLayout.findViewById<Button>(R.id.btnActualizarEditarTorn).setOnClickListener {
                GlobalScope.launch(Dispatchers.Main){
                    val deporte = obtenerDeporte()
                    val estado = obtenerEstado()

                    val nombre = txtEditar_Nombre_Torn.text.toString()
                    val ubicacion = txtUbicacion_Torn.text.toString()
                    val descripcion = txtDescripcion_Torn.text.toString()
                    val tipoDeporte = deporte[spTipoDeporte_Torn.selectedItemPosition].UUID_Tipo_Deporte
                    val estadoTorneo = estado[spTipoDeporte_Torn.selectedItemPosition].UUID_Estado_Torneo
                editarDatos(nombre, ubicacion, descripcion, estadoTorneo, tipoDeporte, intent.getStringExtra("UUID_Torneo").toString())

                alertDialog.dismiss()
                }
            }
            alertDialog.show()
        }

        fun showDeleteTorneoDialog() {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.alertdialog_elim_torneos, null)

            builder.setView(dialogLayout)
            val alertDialog = builder.create()

            dialogLayout.findViewById<Button>(R.id.btnCancelarEliminarTorn).setOnClickListener {
                alertDialog.dismiss()
            }

            dialogLayout.findViewById<Button>(R.id.btnEliminarTorn).setOnClickListener {
                val nombrequequieroeliminar = intent.getStringExtra("Nombre_Torneo").toString()
                val torneliminar = intent.getStringExtra("UUID_Torneo").toString()
                eliminarDatos(nombrequequieroeliminar, torneliminar)
                alertDialog.dismiss()
                finish()
            }

            alertDialog.show()
        }

        btnEditarTorneo.setOnClickListener {
            showEditTorneoDialog()
        }

        btnEliminarTorneo.setOnClickListener {
            showDeleteTorneoDialog()
        }


    }




}