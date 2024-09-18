package RecyclerView

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
import modelos.ClaseConexion
import modelos.tbEquipos
import modelos.tbEstadoEquipos


class AdapterEqui(var Datos: List<tbEquipos>): RecyclerView.Adapter<ViewHolderEquip>() {

    fun obtenerEstado(): List<tbEstadoEquipos>{
        val objConexion = ClaseConexion().cadenaConexion()

        val statement = objConexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM tbEstadoEquipo")!!
        val listaEstadoEquipo = mutableListOf<tbEstadoEquipos>()

        while (resultSet.next()){
            val uuidEstadoEquipo = resultSet.getString("UUID_Estado_Equipo")
            val estadoEquipo = resultSet.getString("Estado_Equipo")
            val unEstadoEquipo = tbEstadoEquipos(uuidEstadoEquipo, estadoEquipo)
            listaEstadoEquipo.add(unEstadoEquipo)
        }
        return listaEstadoEquipo
    }

    //Eliminar
    fun eliminarDatos(Nombre_Equipo: String, posicion: Int){
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val deleteEquipo = objConexion?.prepareStatement("delete tbEquipos where Nombre_Equipo = ?")!!
            deleteEquipo.setString(1, Nombre_Equipo)
            deleteEquipo.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit?.executeUpdate()
        }
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    //Editar
    fun editarDatos(nuevoNombre: String, nuevoDescripcion: String, nuevoUbicacion: String, nuevoEstado: String, UUID_Equipo: String){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val updateEquipo = objConexion?.prepareStatement("update tbEquipos set Nombre_Equipo =?, Descripcion_Equipo =?, Ubicacion_Equipo =?, Estado_Equipo =? where UUID_Equipo =?")!!
            updateEquipo.setString(1, nuevoNombre)
            updateEquipo.setString(2, nuevoDescripcion)
            updateEquipo.setString(3, nuevoUbicacion)
            updateEquipo.setString(4, nuevoEstado)
            updateEquipo.setString(5, UUID_Equipo)
            updateEquipo.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit?.executeUpdate()
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEquip {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card_equipos, parent, false)
        return ViewHolderEquip(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderEquip, position: Int) {
        val item = Datos[position]
        holder.txtNombreEquipo.text = item.Nombre_Equipo
        Glide.with(holder.imgCardEquipo.context)
            .load(item.Logo_Equipo)
            .into(holder.imgCardEquipo)

        holder.imgEliminarEquipo.setOnClickListener {
            val context = holder.itemView.context
            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogLayout = inflater.inflate(R.layout.alertdialog_elim_equipo, null)

            builder.setView(dialogLayout)
            val alertDialog = builder.create()

            dialogLayout.findViewById<Button>(R.id.btnCancelarEliminarEqui).setOnClickListener {
                alertDialog.dismiss()
            }

            dialogLayout.findViewById<Button>(R.id.btnEliminarEqui).setOnClickListener {
                eliminarDatos(item.Nombre_Equipo, position)
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        holder.imgEditarEquipo.setOnClickListener {
            val context = holder.itemView.context
            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogLayout = inflater.inflate(R.layout.alertdialog_edit_equipos, null)

            val txtEditar_Nombre_Equip = dialogLayout.findViewById<EditText>(R.id.txtEditar_Nombre_Equip)
            val txtDescripcion_Equip = dialogLayout.findViewById<EditText>(R.id.txtDescripcion_Equip)
            val txtUbicacion_Equip = dialogLayout.findViewById<EditText>(R.id.txtUbicacion_Equip)
            val spEstado_Equip = dialogLayout.findViewById<Spinner>(R.id.spEstado_Equip)

            CoroutineScope(Dispatchers.IO).launch {
                val listaEstadoEquipo = obtenerEstado()
                val nombreEstadoEquipo = listaEstadoEquipo.map { it.Estado_Equipo }

                withContext(Dispatchers.Main){
                    val miAdaptador = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, nombreEstadoEquipo)
                    spEstado_Equip.adapter = miAdaptador
                }
            }

            builder.setView(dialogLayout)
            val alertDialog = builder.create()

            dialogLayout.findViewById<Button>(R.id.btnCancelarEditarEquip).setOnClickListener {
                alertDialog.dismiss()
            }
            dialogLayout.findViewById<Button>(R.id.btnActualizarEditarEquip).setOnClickListener {
                val estado = obtenerEstado()

                val nombre = txtEditar_Nombre_Equip.text.toString()
                val descripcion = txtDescripcion_Equip.text.toString()
                val ubicacion = txtUbicacion_Equip.text.toString()
                val estados = estado[spEstado_Equip.selectedItemPosition].UUID_Estado_Equipo
                editarDatos(nombre, descripcion, ubicacion, estados, item.UUID_Equipo)

                alertDialog.dismiss()
            }
            alertDialog.show()
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val pantallaVer = Intent(context, VerEquipo::class.java)
            pantallaVer.putExtra("UUID_Equipo", item.UUID_Equipo)
            pantallaVer.putExtra("NombreEquipo", item.Nombre_Equipo)
            pantallaVer.putExtra("DescripcionEquipo", item.Descripcion_Equipo)
            pantallaVer.putExtra("UbicacionEquipo", item.Ubicacion_Equipo)
            pantallaVer.putExtra("EstadoEquipo", item.UUID_Estado_Equipo)
            pantallaVer.putExtra("LogoEquipo", item.Logo_Equipo)
            context.startActivity(pantallaVer)
        }
    }
}