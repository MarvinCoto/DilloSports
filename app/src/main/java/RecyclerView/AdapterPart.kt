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
import modelos.tbEquipos
import modelos.tbPartidos

class AdapterPart(var Datos: List<tbPartidos>): RecyclerView.Adapter<ViewHolderPart>() {

    fun obtenerArbitro(): List<tbArbitros> {
        val objConexion = ClaseConexion().cadenaConexion()

        val statement = objConexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM tbArbitros")!!
        val listaArbitros = mutableListOf<tbArbitros>()

        while (resultSet.next()) {
            val uuidArbitro = resultSet.getString("UUID_Arbitro")
            val nombreArbitro = resultSet.getString("Nombre_Arbitro")
            val apellidoArbitro = resultSet.getString("Apellido_Arbitro")
            val edadArbitro = resultSet.getInt("Edad_Arbitro")
            val telefonoArbitro = resultSet.getString("Telefono_Arbitro")
            val fotoArbitro = resultSet.getString("Foto_Arbitro")
            val unArbitro = tbArbitros(uuidArbitro, nombreArbitro, apellidoArbitro, edadArbitro, telefonoArbitro, fotoArbitro)
            listaArbitros.add(unArbitro)
        }
        return listaArbitros
    }

    fun obtenerEquipo(): List<tbEquipos> {
        val objConexion = ClaseConexion().cadenaConexion()

        val statement = objConexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM tbEquipos")!!
        val listaEquipos = mutableListOf<tbEquipos>()

        while (resultSet.next()) {
            val uuidEquipo = resultSet.getString("UUID_Equipo")
            val nombreEquipo = resultSet.getString("Nombre_Equipo")
            val descripcionEquipo = resultSet.getString("Descripcion_Equipo")
            val ubicacionEquipo = resultSet.getString("Ubicacion_Equipo")
            val estadoEquipo = resultSet.getString("Estado_Equipo")
            val logoEquipo = resultSet.getString("Logo_Equipo")
            val unEquipo = tbEquipos(uuidEquipo, nombreEquipo, descripcionEquipo, ubicacionEquipo, estadoEquipo, logoEquipo)
            listaEquipos.add(unEquipo)
        }
        return listaEquipos
    }

    // Eliminar
    fun eliminarDatos(Marcador_Equipo1: Int, Marcador_Equipo2: Int, Fecha_Partido: String, Tipo_Partido: String, posicion: Int){
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val deletePartido = objConexion?.prepareStatement("delete tbPartidos where Marcador_Equipo1 = ? and Marcador_Equipo2 = ? and Fecha_Partido = ? and Tipo_Partido = ?")!!
            deletePartido.setInt(1, Marcador_Equipo1)
            deletePartido.setInt(2, Marcador_Equipo2)
            deletePartido.setString(3, Fecha_Partido)
            deletePartido.setString(4, Tipo_Partido)
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
            updatePartido.setString(6, nuevoEquipo1)
            updatePartido.setString(7, nuevoEquipo2)
            updatePartido.setString(8, nuevoArbitro)
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
        holder.txtResultado1.text = item.Marcador_Equipo1.toString()
        holder.txtResultado2.text = item.Marcador_Equipo2.toString()
        holder.txtFechaPart.text = item.Fecha_Partido
        holder.txtEspPartido.text = item.Tipo_Partido
        holder.txtNombreEquipo1.text = item.UUID_Equipo1
        holder.txtNombreEquipo2.text = item.UUID_Equipo2

        holder.imgEditarPartido.setOnClickListener {
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
                eliminarDatos(item.Marcador_Equipo1, item.Marcador_Equipo2, item.Fecha_Partido, item.Tipo_Partido, position)
                alertDialog.dismiss()
            }
            alertDialog.show()
        }

        holder.imgEliminarPartido.setOnClickListener {
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
                val lugar = txtEditar_Lugar_Part.text.toString()
                val tipo = txtTipo_Part.text.toString()
                val marcador1 = txtMarcador1.text.toString().toInt()
                val marcador2 = txtMarcador2.text.toString().toInt()
                val equipo1 = spEdit_Equipo1.selectedItemPosition.toString()
                val equipo2 = spEdit_Equipo2.selectedItemPosition.toString()
                val arbitro = spArbitros_Editar.selectedItemPosition.toString()
                editarDatos(marcador1, marcador2, tipo, lugar, equipo1, equipo2, arbitro, item.UUID_Partido)

                alertDialog.dismiss()
            }
            alertDialog.show()
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val pantallaVer = Intent(context, Ver_partido::class.java)
            pantallaVer.putExtra("UUID_Partido", item.UUID_Partido)
            pantallaVer.putExtra("UUID_Equipo1", item.UUID_Equipo1)
            pantallaVer.putExtra("UUID_Equipo2", item.UUID_Equipo2)
            pantallaVer.putExtra("UUID_Arbitro", item.UUID_Arbitro)
            pantallaVer.putExtra("Fecha_Partido", item.Fecha_Partido)
            pantallaVer.putExtra("Lugar_Partido", item.Lugar_Partido)
            pantallaVer.putExtra("Hora_Partido", item.Hora_Partido)
            pantallaVer.putExtra("Tipo_Partido", item.Tipo_Partido)
            pantallaVer.putExtra("Marcador_Equipo1", item.Marcador_Equipo1)
            pantallaVer.putExtra("Marcador_Equipo2", item.Marcador_Equipo2)
            context.startActivity(pantallaVer)
        }
    }

}