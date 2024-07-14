package RecyclerView

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import modelos.tbJugadores
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import marvin.coto.dillosports.R
import marvin.coto.dillosports.VerJugadores
import modelos.ClaseConexion

class Adapter(var Datos: List<tbJugadores>): RecyclerView.Adapter<ViewHolder>() {

    //Eliminar
    fun eliminarDatos(Nombre_Jugador: String, Apellido_Jugador: String, Numero_Jugador: Int, posicion: Int){
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val deleteJugador = objConexion?.prepareStatement("delete tbJugadores where Nombre_Jugador = ? and Apellido_Jugador = ? and Numero_Jugador = ?")!!
            deleteJugador.setString(1, Nombre_Jugador)
            deleteJugador.setString(2, Apellido_Jugador)
            deleteJugador.setInt(3, Numero_Jugador)
            deleteJugador.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit?.executeUpdate()
        }
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    fun actualizarPantalla(nuevalista: List<tbJugadores>){
        Datos = nuevalista
        notifyDataSetChanged()
    }

    //Editar
    fun editarDatos(nuevoNombre: String, nuevoApellido: String, nuevaFechaNacimiento: String, nuevaEdad: Int, nuevoTelefono: String, nuevoNumeroCamiseta: Int, nuevaPosicion: String, nuevoEstado: String, UUID_Jugador: String){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val updateJugador = objConexion?.prepareStatement("update tbJugadores set Nombre_Jugador =?, Apellido_Jugador =?, FNacimiento_Jugador =?, Edad_Jugador =?, Telefono_Jugador =?, Numero_Jugador =?, Posicion_Jugador =?, Estado_Jugador =? where UUID_Jugador =?")!!
            updateJugador.setString(1, nuevoNombre)
            updateJugador.setString(2, nuevoApellido)
            updateJugador.setString(3, nuevaFechaNacimiento)
            updateJugador.setInt(4, nuevaEdad)
            updateJugador.setString(5, nuevoTelefono)
            updateJugador.setInt(6, nuevoNumeroCamiseta)
            updateJugador.setString(7, nuevaPosicion)
            updateJugador.setString(8, nuevoEstado)
            updateJugador.setString(9, UUID_Jugador)
            updateJugador.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit?.executeUpdate()
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card_jugadores, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = Datos[position]
        holder.txtNombreJugador.text = item.Nombre_Jugador
        holder.txtPosicionJugador.text = item.Posicion_Jugador
        holder.txtNumeroCamiseta.text = item.Numero_Jugador.toString()

        holder.imgEliminarJugador.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Eliminar Jugador")
            builder.setMessage("¿Desea eliminar el Jugador?")

            builder.setPositiveButton("Si"){
                dialog, wich -> eliminarDatos(item.Nombre_Jugador, item.Apellido_Jugador, item.Numero_Jugador, position)
            }
            builder.setNegativeButton("No"){
                dialog, wich -> dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        //Todavia no funciona
        holder.imgEditarJugador.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Editar Jugador")

            val txtNuevoNombre = EditText(context).apply {
                setHint(item.Nombre_Jugador)
            }
            val txtNuevoApellido = EditText(context).apply {
                setHint(item.Apellido_Jugador)
            }
            val txtNuevaFechaNacimiento = EditText(context).apply {
                setHint(item.FNacimiento_Jugador)
            }
            val txtNuevaEdad = EditText(context).apply {
                setHint(item.Edad_Jugador.toString())
            }
            val txtNuevoTelefono = EditText(context).apply {
                setHint(item.Telefono_Jugador)
            }
            val txtNuevoNumeroCamiseta = EditText(context).apply {
                setHint(item.Numero_Jugador.toString())
            }
            val txtNuevaPosicion = EditText(context).apply {
                setHint(item.Posicion_Jugador)
            }
            val txtNuevoEstado = EditText(context).apply {
                setHint(item.Estado_Jugador)
            }

            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(txtNuevoNombre)
                addView(txtNuevoApellido)
                addView(txtNuevaFechaNacimiento)
                addView(txtNuevaEdad)
                addView(txtNuevoTelefono)
                addView(txtNuevoNumeroCamiseta)
                addView(txtNuevaPosicion)
                addView(txtNuevoEstado)
            }
            builder.setView(layout)

            builder.setPositiveButton("Guardar"){
                dialog, which -> editarDatos(item.Nombre_Jugador, item.Apellido_Jugador, item.FNacimiento_Jugador, item.Edad_Jugador, item.Telefono_Jugador, item.Numero_Jugador, item.Posicion_Jugador, item.Estado_Jugador, item.UUID_Jugador)
            }
            builder.setNegativeButton("Cancelar"){
                dialog, wich -> dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

    }
}
