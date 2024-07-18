package RecyclerView

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import marvin.coto.dillosports.R
import marvin.coto.dillosports.VerEquipo
import marvin.coto.dillosports.Ver_Torneo
import modelos.ClaseConexion
import modelos.tbEquipos


class AdapterEqui(var Datos: List<tbEquipos>): RecyclerView.Adapter<ViewHolderEquip>() {

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

        holder.imgEliminarEquipo.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Eliminar Equipo")
            builder.setMessage("¿Desea eliminar el Equipo?")

            builder.setPositiveButton("Si"){
                    dialog, wich -> eliminarDatos(item.Nombre_Equipo, position)
            }
            builder.setNegativeButton("No"){
                    dialog, wich -> dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        holder.imgEditarEquipo.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Editar Equipo")

            val txtNuevoNombre = EditText(context).apply {
                setHint(item.Nombre_Equipo)
            }
            val txtNuevaDescripcion = EditText(context).apply {
                setHint(item.Descripcion_Equipo)
            }
            val txtNuevaUbicacion = EditText(context).apply {
                setHint(item.Ubicacion_Equipo)
            }
            val txtNuevoEstado = EditText(context).apply {
                setHint(item.Estado_Equipo)
            }

            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(txtNuevoNombre)
                addView(txtNuevaDescripcion)
                addView(txtNuevaUbicacion)
                addView(txtNuevoEstado)
            }
            builder.setView(layout)

            builder.setPositiveButton("Guardar"){
                dialog, wich -> editarDatos(txtNuevoNombre.text.toString(), txtNuevaDescripcion.text.toString(), txtNuevaUbicacion.text.toString(), txtNuevoEstado.text.toString(), item.UUID_Equipo)
            }
            builder.setNegativeButton("Cancelar"){
                dialog, wich -> dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val pantallaVer = Intent(context, VerEquipo::class.java)
            pantallaVer.putExtra("UUID_Equipo", item.UUID_Equipo)
            pantallaVer.putExtra("NombreEquipo", item.Nombre_Equipo)
            pantallaVer.putExtra("DescripcionEquipo", item.Descripcion_Equipo)
            pantallaVer.putExtra("UbicacionEquipo", item.Ubicacion_Equipo)
            pantallaVer.putExtra("EstadoEquipo", item.Estado_Equipo)
            context.startActivity(pantallaVer)
        }
    }
}