package RecyclerView

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import marvin.coto.dillosports.R
import marvin.coto.dillosports.VerEquipo
import marvin.coto.dillosports.Ver_partido
import modelos.ClaseConexion
import modelos.tbArbitros
import modelos.tbEditPartidos
import modelos.tbEquipos
import modelos.tbPartidos

class AdapterPart(var Datos: List<tbPartidos>): RecyclerView.Adapter<ViewHolderPart>() {
    companion object variablesGlobalPart{
        lateinit var UUID_Partido: String
        lateinit var UUID_Equipo1: String
        lateinit var UUID_Equipo2: String
        lateinit var Fecha_Partido: String
        lateinit var Lugar_Partido: String
        lateinit var Hora_Partido: String
        var Marcador_Equipo1: Int? = null
        var Marcador_Equipo2: Int? = null
        lateinit var Tipo_Partido: String
        lateinit var UUID_Arbitro: String
        lateinit var imagen1: String
        lateinit var imagen2: String
    }

    fun obtenerDatoUUID(UUID_Equipo: String): String? {
        val objConexion = ClaseConexion().cadenaConexion()

        val query = "select Nombre_Equipo from tbEquipos where UUID_Equipo = ?"
        val preparedStatement = objConexion?.prepareStatement(query)
        preparedStatement?.setString(1, UUID_Equipo)
        val resultSet = preparedStatement?.executeQuery()

        var nombreEquipo: String? = null

        if (resultSet?.next() == true) {
            nombreEquipo = resultSet.getString("Nombre_Equipo")
        }

        return nombreEquipo
    }

    suspend fun obtenerArbitro(): List<tbArbitros> {
        return withContext(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbArbitros")!!
            val listaArbitros = mutableListOf<tbArbitros>()

            while (resultSet.next() == true) {
                val UUID_Arbitro = resultSet.getString("UUID_Arbitro")
                val Nombre_Arbitro = resultSet.getString("Nombre_Arbitro")
                val Apellido_Arbitro = resultSet.getString("Apellido_Arbitro")
                val Edad_Arbitro = resultSet.getInt("Edad_Arbitro")
                val Telefono_Arbitro = resultSet.getString("Telefono_Arbitro")
                val Foto_Arbitro = resultSet.getString("Foto_Arbitro")
                val unArbitro = tbArbitros(UUID_Arbitro, Nombre_Arbitro, Apellido_Arbitro, Edad_Arbitro, Telefono_Arbitro, Foto_Arbitro)
                listaArbitros.add(unArbitro)
            }
            resultSet?.close()
            statement?.close()
            objConexion?.close()

            listaArbitros
        }
    }

    suspend fun obtenerEquipo(): List<tbEquipos> {
        return withContext(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbEquipos")!!
            val listaEquipos = mutableListOf<tbEquipos>()

            while (resultSet.next() == true) {
                val UUID_Equipo = resultSet.getString("UUID_Equipo")
                val Nombre_Equipo = resultSet.getString("Nombre_Equipo")
                val Descripcion_Equipo = resultSet.getString("Descripcion_Equipo")
                val Ubicacion_Equipo = resultSet.getString("Ubicacion_Equipo")
                val Logo_Equipo = resultSet.getString("Logo_Equipo")
                val UUID_Estado_Equipo = resultSet.getString("UUID_Estado_Equipo")
                val unEquipo = tbEquipos(UUID_Equipo, Nombre_Equipo, Descripcion_Equipo, Ubicacion_Equipo, Logo_Equipo, UUID_Estado_Equipo)
                listaEquipos.add(unEquipo)
            }
            resultSet?.close()
            statement?.close()
            objConexion?.close()

            listaEquipos
        }
    }

    // Eliminar
    fun eliminarDatos(Fecha_Partido: String, Tipo_Partido: String, posicion: Int){
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val deletePartido = objConexion?.prepareStatement("delete tbPartidos where Fecha_Partido = ? and Tipo_Partido = ?")!!
            deletePartido.setString(1, Fecha_Partido)
            deletePartido.setString(2, Tipo_Partido)
            deletePartido.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit?.executeUpdate()
        }
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    //Editar
    fun editarDatos(nuevoMarcadorEquipo1: Int, nuevoMarcadorEquipo2: Int, nuevoTipoPartido: String, nuevoLugarPartido: String, nuevoEquipo1: String, nuevoEquipo2: String, nuevoArbitro: String,  UUID_Partido: String){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val updatePartido = objConexion?.prepareStatement("update tbPartidos set Marcador_Equipo1 =?, Marcador_Equipo2 =?, Tipo_Partido =?, Lugar_Partido =?, UUID_Arbitro =?, UUID_Equipo1 =?, UUID_Equipo2 =? where UUID_Partido =?")!!
            updatePartido.setInt(1, nuevoMarcadorEquipo1)
            updatePartido.setInt(2, nuevoMarcadorEquipo2)
            updatePartido.setString(3, nuevoTipoPartido)
            updatePartido.setString(4, nuevoLugarPartido)
            updatePartido.setString(5, nuevoEquipo1)
            updatePartido.setString(6, nuevoEquipo2)
            updatePartido.setString(7, nuevoArbitro)
            updatePartido.setString(8, UUID_Partido)
            updatePartido.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit?.executeUpdate()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPart {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card_partidos, parent, false)
        return ViewHolderPart(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderPart, position: Int) {
        val item = Datos[position]
        val resultado1 = obtenerDatoUUID(item.UUID_Equipo1)
        val resultado2 = obtenerDatoUUID(item.UUID_Equipo2)
        holder.txtResultado1.text = item.Marcador_Equipo1.toString()
        holder.txtResultado2.text = item.Marcador_Equipo2.toString()
        holder.txtFechaPart.text = item.Fecha_Partido
        holder.txtEspPartido.text = item.Tipo_Partido
        holder.txtNombreEquipo1.text = resultado1
        holder.txtNombreEquipo2.text = resultado2
        Glide.with(holder.imgEquipo1.context)
            .load(item.imagen1)
            .into(holder.imgEquipo1)

        Glide.with(holder.imgEquipo2.context)
            .load(item.imagen2)
            .into(holder.imgEquipo2)

        holder.imgEliminarPartido.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogLayout = inflater.inflate(R.layout.alertdialog_elim_partido, null)

            builder.setView(dialogLayout)
            val alertDialog = builder.create()

            dialogLayout.findViewById<Button>(R.id.btnCancelarEliminarPart).setOnClickListener {
                alertDialog.dismiss()
            }
            dialogLayout.findViewById<Button>(R.id.btnEliminarPart).setOnClickListener {
                eliminarDatos(item.Fecha_Partido, item.Tipo_Partido, position)
                alertDialog.dismiss()
            }
            alertDialog.show()
        }

        holder.imgEditarPartido.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogLayout = inflater.inflate(R.layout.alertdialog_edit_partidos, null)

            val txtEditar_Lugar_Part = dialogLayout.findViewById<EditText>(R.id.txtEditar_Lugar_Part)
            val txtTipo_Part = dialogLayout.findViewById<EditText>(R.id.txtTipo_Part)
            val txtMarcador1 = dialogLayout.findViewById<EditText>(R.id.txtMarcador1)
            val txtMarcador2 = dialogLayout.findViewById<EditText>(R.id.txtMarcador2)
            val spEdit_Equipo1 = dialogLayout.findViewById<Spinner>(R.id.spEdit_Equipo1)
            val spEdit_Equipo2 = dialogLayout.findViewById<Spinner>(R.id.spEdit_Equipo2)
            val spArbitros_Editar = dialogLayout.findViewById<Spinner>(R.id.spArbitros_Editar)



            CoroutineScope(Dispatchers.IO).launch {
                val listaEquipos = obtenerEquipo()
                val nombreEquipo = listaEquipos.map { it.Nombre_Equipo }

                withContext(Dispatchers.Main){
                    val miAdaptador = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, nombreEquipo)
                    spEdit_Equipo1.adapter = miAdaptador
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                val listaEquipos = obtenerEquipo()
                val nombreEquipo = listaEquipos.map { it.Nombre_Equipo }

                withContext(Dispatchers.Main){
                    val miAdaptador = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, nombreEquipo)
                    spEdit_Equipo2.adapter = miAdaptador
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                val listaArbitros = obtenerArbitro()
                val nombreArbitro = listaArbitros.map { it.Nombre_Arbitro }

                withContext(Dispatchers.Main){
                    val miAdaptador = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, nombreArbitro)
                    spArbitros_Editar.adapter = miAdaptador
                }
            }
            builder.setView(dialogLayout)
            val alertDialog = builder.create()

            dialogLayout.findViewById<Button>(R.id.btnCancelarEditarPart).setOnClickListener {
                alertDialog.dismiss()
            }
            dialogLayout.findViewById<Button>(R.id.btnActualizarEditarPart).setOnClickListener {
                GlobalScope.launch(Dispatchers.Main){
                    val equipo = obtenerEquipo()
                    val arbitros = obtenerArbitro()

                    val marcador1 = txtMarcador1.text.toString().toInt()
                    val marcador2 = txtMarcador2.text.toString().toInt()
                    val tipo = txtTipo_Part.text.toString()
                    val lugar = txtEditar_Lugar_Part.text.toString()
                    val arbitro = arbitros[spArbitros_Editar.selectedItemPosition].UUID_Arbitro
                    val equipo1 = equipo[spEdit_Equipo1.selectedItemPosition].UUID_Equipo
                    val equipo2 = equipo[spEdit_Equipo2.selectedItemPosition].UUID_Equipo
                    editarDatos(marcador1, marcador2, tipo, lugar, arbitro, equipo1, equipo2, item.UUID_Partido)

                    alertDialog.dismiss()
                }
            }
            alertDialog.show()
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val pantallaVer = Intent(context, Ver_partido::class.java)
            UUID_Partido = item.UUID_Partido
            UUID_Equipo1 = item.UUID_Equipo1
            UUID_Equipo2 = item.UUID_Equipo2
            Fecha_Partido = item.Fecha_Partido
            Lugar_Partido = item.Lugar_Partido
            Hora_Partido = item.Hora_Partido
            Marcador_Equipo1 = item.Marcador_Equipo1
            Marcador_Equipo2 = item.Marcador_Equipo2
            Tipo_Partido = item.Tipo_Partido
            UUID_Arbitro = item.UUID_Arbitro
            imagen1 = item.imagen1
            imagen2 = item.imagen2
            context.startActivity(pantallaVer)
        }
    }

}