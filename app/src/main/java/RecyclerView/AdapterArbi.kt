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
import marvin.coto.dillosports.VerArbitro
import modelos.ClaseConexion
import modelos.tbArbitros


class AdapterArbi(var Datos: List<tbArbitros>): RecyclerView.Adapter<ViewHolderArbi>() {

    //Eliminar
    fun eliminarDatos(Nombre_Arbitro: String, posicion: Int){
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val deleteArbitro = objConexion?.prepareStatement("delete tbArbitros1 where Nombre_Arbitro = ?")!!
            deleteArbitro.setString(1, Nombre_Arbitro)
            deleteArbitro.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit?.executeUpdate()
        }
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    //Editar
    fun editarDatos(nuevoNombre: String, nuevoApellido: String, nuevoEdad: Int, nuevoTelefono: String, UUID_Arbitro: String){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val updateArbitro = objConexion?.prepareStatement("update tbArbitros1 set Nombre_Arbitro =?, Apellido_Arbitro =?, Edad_Arbitro =?, Telefono_Arbitro =? where UUID_Arbitro =?")!!
            updateArbitro.setString(1, nuevoNombre)
            updateArbitro.setString(2, nuevoApellido)
            updateArbitro.setInt(3, nuevoEdad)
            updateArbitro.setString(4, nuevoTelefono)
            updateArbitro.setString(5, UUID_Arbitro)
            updateArbitro.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit?.executeUpdate()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderArbi {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card_arbitro, parent, false)
        return ViewHolderArbi(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderArbi, position: Int) {
        val item = Datos[position]
        holder.txtNombreArbitro.text = item.Nombre_Arbitro

        holder.imgEliminarArbitro.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Eliminar Arbitro")
            builder.setMessage("¿Desea eliminar el Arbitro?")

            builder.setPositiveButton("Si"){
                dialog, wich -> eliminarDatos(item.Nombre_Arbitro, position)
            }
            builder.setNegativeButton("No"){
                dialog, wich -> dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        holder.imgEditarArbitro.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Editar Arbitro")

            val txtNuevoNombre = EditText(context).apply {
                setHint(item.Nombre_Arbitro)
            }
            val txtNuevoApellido = EditText(context).apply {
                setHint(item.Apellido_Arbitro)
            }
            val txtNuevaEdad = EditText(context).apply {
                setHint(item.Edad_Arbitro.toString())
            }
            val txtNuevoTelefono = EditText(context).apply {
                setHint(item.Telefono_Arbitro)
            }

            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(txtNuevoNombre)
                addView(txtNuevoApellido)
                addView(txtNuevaEdad)
                addView(txtNuevoTelefono)
            }
            builder.setView(layout)

            builder.setPositiveButton("Guardar"){
                dialog, wich -> editarDatos(txtNuevoNombre.text.toString(), txtNuevoApellido.text.toString(), txtNuevaEdad.text.toString().toInt(), txtNuevoTelefono.text.toString(), item.UUID_Arbitro)
            }
            builder.setNegativeButton("Cancelar"){
                dialog, wich -> dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val pantallaVer = Intent(context, VerArbitro::class.java)
            pantallaVer.putExtra("UUID_Arbitro", item.UUID_Arbitro)
            pantallaVer.putExtra("NombreArbitro", item.Nombre_Arbitro)
            pantallaVer.putExtra("ApellidoArbitro", item.Apellido_Arbitro)
            pantallaVer.putExtra("EdadArbitro", item.Edad_Arbitro)
            pantallaVer.putExtra("TelefonoArbitro", item.Telefono_Arbitro)
            context.startActivity(pantallaVer)
        }
    }
}