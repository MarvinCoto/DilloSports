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
import marvin.coto.dillosports.VerJugadores
import marvin.coto.dillosports.Ver_Torneo
import modelos.ClaseConexion
import modelos.tbTorneos

class AdapterTorn(var Datos: List<tbTorneos>): RecyclerView.Adapter<ViewHolderTorn>() {

    //Eliminar
    fun eliminarDatos(Nombre_Torneo: String, Ubicacion_Torneo: String, posicion: Int){
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val deleteTorneo = objConexion?.prepareStatement("delete tbTorneos where Nombre_Torneo = ? and Ubicacion_Torneo = ?")!!
            deleteTorneo.setString(1, Nombre_Torneo)
            deleteTorneo.setString(2, Ubicacion_Torneo)
            deleteTorneo.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit?.executeUpdate()
        }
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    //Editar
    fun editarDatos(nuevoNombre: String, nuevoUbicacion: String, nuevoDescripcion: String, nuevoDeporte: String, nuevoEstado: String, UUID_Torneo: String){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val updateTorneo = objConexion?.prepareStatement("update tbTorneos set Nombre_Torneo =?, Ubicacion_Torneo =?, Descripcion_Torneo =?, Deporte_Torneo =?, Estado_Toneo =? where UUID_Torneo =?")!!
            updateTorneo.setString(1, nuevoNombre)
            updateTorneo.setString(2, nuevoUbicacion)
            updateTorneo.setString(3, nuevoDescripcion)
            updateTorneo.setString(4, nuevoDeporte)
            updateTorneo.setString(5, nuevoEstado)
            updateTorneo.setString(6, UUID_Torneo)
            updateTorneo.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit?.executeUpdate()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTorn {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card_torneos, parent, false)
        return ViewHolderTorn(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderTorn, position: Int) {
        val item = Datos[position]
        holder.txtNombreTorneo.text = item.Nombre_Torneo
        holder.txtUbicacionTorneo.text = item.Ubicacion_Torneo

        /*holder.imgEliminarTorneo.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Eliminar Torneo")
            builder.setMessage("¿Desea eliminar el Torneo?")

            builder.setPositiveButton("Si"){
                    dialog, wich -> eliminarDatos(item.Nombre_Torneo, item.Ubicacion_Torneo, position)
            }
            builder.setNegativeButton("No"){
                    dialog, wich -> dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        holder.imgEditarTorneo.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Editar Torneo")

            val txtNuevoNombre = EditText(context).apply {
                setHint(item.Nombre_Torneo)
            }
            val txtNuevoUbicacion = EditText(context).apply {
                setHint(item.Ubicacion_Torneo)
            }
            val txtNuevaDescripcion = EditText(context).apply {
                setHint(item.Descripcion_Torneo)
            }
            val txtNuevoDeporte = EditText(context).apply {
                setHint(item.Deporte)
            }
            val txtNuevoEstado = EditText(context).apply {
                setHint(item.Estado_Toneo)
            }

            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(txtNuevoNombre)
                addView(txtNuevoUbicacion)
                addView(txtNuevaDescripcion)
                addView(txtNuevoDeporte)
                addView(txtNuevoEstado)
            }
            builder.setView(layout)

            builder.setPositiveButton("Guardar"){
                dialog, wich -> editarDatos(txtNuevoNombre.text.toString(), txtNuevoUbicacion.text.toString(), txtNuevaDescripcion.text.toString(), txtNuevoDeporte.text.toString(), txtNuevoEstado.text.toString(), item.UUID_Torneo)
            }
            builder.setNegativeButton("Cancelar"){
                dialog, wich -> dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }*/

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val pantallaVer = Intent(context, Ver_Torneo::class.java)
            pantallaVer.putExtra("UUID_Torneo", item.UUID_Torneo)
            pantallaVer.putExtra("NombreTorneo", item.Nombre_Torneo)
            pantallaVer.putExtra("UbicacionTorneo", item.Ubicacion_Torneo)
            pantallaVer.putExtra("DescripcionTorneo", item.Descripcion_Torneo)
            pantallaVer.putExtra("DeporteTorneo", item.Deporte)
            pantallaVer.putExtra("EstadoToneo", item.Estado_Toneo)
            context.startActivity(pantallaVer)
        }
    }

}
