package RecyclerView

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        holder.txtEdadArbitro.text = item.Edad_Arbitro.toString()
        Glide.with(holder.imgCardArbi.context)
            .load(item.Foto_Arbitro)
            .into(holder.imgCardArbi)

        holder.imgEliminarArbitro.setOnClickListener {
            val context = holder.itemView.context
            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogLayout = inflater.inflate(R.layout.alertdialog_elim_arbitros, null)

            builder.setView(dialogLayout)
            val alertDialog = builder.create()

            dialogLayout.findViewById<Button>(R.id.btnCancelarEliminarArbi).setOnClickListener {
                alertDialog.dismiss()
            }
            dialogLayout.findViewById<Button>(R.id.btnEliminarArbi).setOnClickListener {
                eliminarDatos(item.Nombre_Arbitro, position)
                alertDialog.dismiss()
            }
            alertDialog.show()
        }

        holder.imgEditarArbitro.setOnClickListener {
            val context = holder.itemView.context
            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogLayout = inflater.inflate(R.layout.alertdialog_edit_arbitros, null)

            val txtNombresArbi = dialogLayout.findViewById<EditText>(R.id.txtNombresArbi)
            val txtApellidosArbi = dialogLayout.findViewById<EditText>(R.id.txtApellidosArbi)
            val txtEdadArbitro = dialogLayout.findViewById<EditText>(R.id.txtEdadArbitro)
            val txtNumeroArbi = dialogLayout.findViewById<EditText>(R.id.txtNumeroArbi)

            builder.setView(dialogLayout)
            val alertDialog = builder.create()

            dialogLayout.findViewById<Button>(R.id.btnCancelarEditarArbi).setOnClickListener {
                alertDialog.dismiss()
            }
            dialogLayout.findViewById<Button>(R.id.btnActualizarEditarArbi).setOnClickListener {
                val nombre = txtNombresArbi.text.toString()
                val apellido = txtApellidosArbi.text.toString()
                val edad = txtEdadArbitro.text.toString().toInt()
                val numero = txtNumeroArbi.text.toString()
                editarDatos(nombre, apellido, edad, numero, item.UUID_Arbitro)

                alertDialog.dismiss()
            }
            alertDialog.show()
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val pantallaVer = Intent(context, VerArbitro::class.java)
            pantallaVer.putExtra("UUID_Arbitro", item.UUID_Arbitro)
            pantallaVer.putExtra("NombreArbitro", item.Nombre_Arbitro)
            pantallaVer.putExtra("ApellidoArbitro", item.Apellido_Arbitro)
            pantallaVer.putExtra("EdadArbitro", item.Edad_Arbitro)
            pantallaVer.putExtra("TelefonoArbitro", item.Telefono_Arbitro)
            pantallaVer.putExtra("FotoArbitro", item.Foto_Arbitro)
            context.startActivity(pantallaVer)
        }
    }
}