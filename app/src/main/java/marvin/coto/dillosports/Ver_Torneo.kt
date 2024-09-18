package marvin.coto.dillosports

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


        val UUIDRecibido = intent.getStringExtra("UUID_Torneo")
        val nombreRecibido = intent.getStringExtra("NombreTorneo")
        val ubicacionRecibido = intent.getStringExtra("UbicacionTorneo")
        val descripcionRecibido = intent.getStringExtra("DescripcionTorneo")
        val deporteRecibido = intent.getStringExtra("DeporteTorneo")
        val estadoRecibido = intent.getStringExtra("EstadoToneo")
        val logoRecibido = intent.getStringExtra("LogoTorneo")
        val imgTorn = findViewById<ImageView>(R.id.imgTorn)
        val textViewNombreTorneo = findViewById<TextView>(R.id.textViewNombreTorneo)
        val textViewUbicacionTorneo = findViewById<TextView>(R.id.textViewUbicacionTorneo)
        val textViewDescripcionTorneo = findViewById<TextView>(R.id.textViewDescripcionTorneo)
        val textViewDeporteTorneo = findViewById<TextView>(R.id.textViewDeporteTorneo)
        val textViewEstadoTorneo = findViewById<TextView>(R.id.textViewEstadoTorneo)
        Glide.with(imgTorn.context)
            .load(logoRecibido)
            .into(imgTorn)
        textViewNombreTorneo.text = nombreRecibido
        textViewUbicacionTorneo.text = ubicacionRecibido
        textViewDescripcionTorneo.text = descripcionRecibido
        textViewDeporteTorneo.text = deporteRecibido
        textViewEstadoTorneo.text = estadoRecibido

        val btnEliminarTorneo = findViewById<TextView>(R.id.btnEliminarTorneo)
        val btnEditarTorneo = findViewById<TextView>(R.id.btnEditarTorneo)

        fun eliminarDatos(Nombre_Torneo: String, posicion: Int): List<tbTorneos>{
            val listaDatos = mutableListOf<tbTorneos>()
            listaDatos.removeAt(posicion)

            GlobalScope.launch(Dispatchers.IO){
                val objConexion = ClaseConexion().cadenaConexion()

                val deleteTorneo = objConexion?.prepareStatement("delete tbTorneos where Nombre_Torneo = ?")!!
                deleteTorneo.setString(1, Nombre_Torneo)
                deleteTorneo.executeUpdate()

                val commit = objConexion.prepareStatement("commit")
                commit?.executeUpdate()
            }
            return listaDatos.toList()
        }

        fun editarDatos(nuevoNombre: String, nuevoUbicacion: String, nuevoDescripcion: String, nuevoTipoDeporte: String, UUID_Torneo: String){
            GlobalScope.launch(Dispatchers.IO){
                val objConexion = ClaseConexion().cadenaConexion()

                val updateTorneo = objConexion?.prepareStatement("update tbTorneos set Nombre_Torneo =?, Ubicacion_Torneo =?, Descripcion_Torneo =?, UUID_Tipo_Deporte =? where UUID_Torneo =?")!!
                updateTorneo.setString(1, nuevoNombre)
                updateTorneo.setString(2, nuevoUbicacion)
                updateTorneo.setString(3, nuevoDescripcion)
                updateTorneo.setString(4, nuevoTipoDeporte)
                updateTorneo.setString(6, UUID_Torneo)
                updateTorneo.executeUpdate()

                val commit = objConexion.prepareStatement("commit")
                commit?.executeUpdate()
            }
        }

        fun obtenerDeporte(): List<tbDeportes>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbTipoDeporte")!!
            val listaDeportes = mutableListOf<tbDeportes>()

            while (resultSet.next()) {
                val uuidDeporte = resultSet.getString("UUID_Tipo_Deporte")
                val nombreDeporte = resultSet.getString("Nombre_Tipo_Deporte")
                val unDeporte = tbDeportes(uuidDeporte, nombreDeporte)
                listaDeportes.add(unDeporte)
            }
            return listaDeportes
        }



        fun showEditTorneoDialog() {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.alertdialog_edit_torneos, null)

            val txtEditar_Nombre_Torn = dialogLayout.findViewById<EditText>(R.id.txtEditar_Nombre_Torn)
            val txtDescripcion_Torn = dialogLayout.findViewById<EditText>(R.id.txtDescripcion_Torn)
            val txtUbicacion_Torn = dialogLayout.findViewById<EditText>(R.id.txtUbicacion_Torn)
            val spTipoDeporte_Torn = dialogLayout.findViewById<Spinner>(R.id.spTipoDeporte_Torn)

            CoroutineScope(Dispatchers.IO).launch {
                val listaDeportes = obtenerDeporte()
                val nombreDeporte = listaDeportes.map { it.Nombre_Tipo_Deporte }

                withContext(Dispatchers.Main){
                    val miAdaptador = ArrayAdapter(this@Ver_Torneo, android.R.layout.simple_spinner_dropdown_item, nombreDeporte)
                    spTipoDeporte_Torn.adapter = miAdaptador
                }
            }

            builder.setView(dialogLayout)
            val alertDialog = builder.create()

            dialogLayout.findViewById<Button>(R.id.btnCancelarEditarTorn).setOnClickListener {
                alertDialog.dismiss()
            }

            dialogLayout.findViewById<Button>(R.id.btnActualizarEditarTorn).setOnClickListener {
                val deporte = obtenerDeporte()

                val nombre = txtEditar_Nombre_Torn.text.toString()
                val descripcion = txtDescripcion_Torn.text.toString()
                val ubicacion = txtUbicacion_Torn.text.toString()
                val tipoDeporte = deporte[spTipoDeporte_Torn.selectedItemPosition].UUID_Tipo_Deporte
                editarDatos(nombre, ubicacion, descripcion, tipoDeporte, intent.getStringExtra("UUID_Torneo").toString())

                alertDialog.dismiss()
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
                eliminarDatos(intent.getStringExtra("Nombre_Torneo").toString(), 0)
                alertDialog.dismiss()
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