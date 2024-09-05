package RecyclerView

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import modelos.tbJugadores
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import marvin.coto.dillosports.R
import marvin.coto.dillosports.VerJugadores
import modelos.ClaseConexion

class Adapter(var Datos: List<tbJugadores>): RecyclerView.Adapter<ViewHolder>() {

    //Eliminar
    fun eliminarDatos(Nombre_Jugador: String, Posicion_Jugador: String, Numero_Jugador: Int, posicion: Int){
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val deleteJugador = objConexion?.prepareStatement("delete tbJugadores where Nombre_Jugador = ? and Posicion_Jugador = ? and Numero_Jugador = ?")!!
            deleteJugador.setString(1, Nombre_Jugador)
            deleteJugador.setString(2, Posicion_Jugador)
            deleteJugador.setInt(3, Numero_Jugador)
            deleteJugador.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit?.executeUpdate()
        }
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    //Editar
    fun editarDatos(nuevoNombre: String, nuevoApellido: String, nuevoNumeroCamiseta: Int, nuevaPosicion: String, nuevoUUID_Estado_Jugador: String, UUID_Jugador: String){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val updateJugador = objConexion?.prepareStatement("update tbJugadores set Nombre_Jugador =?, Apellido_Jugador =?, Numero_Jugador =?, Posicion_Jugador =?, UUID_Estado_Jugador =? where UUID_Jugador =?")!!
            updateJugador.setString(1, nuevoNombre)
            updateJugador.setString(2, nuevoApellido)
            updateJugador.setInt(3, nuevoNumeroCamiseta)
            updateJugador.setString(4, nuevaPosicion)
            updateJugador.setString(5, nuevoUUID_Estado_Jugador)
            updateJugador.setString(6, UUID_Jugador)
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
        Glide.with(holder.imgCardJugadores.context)
            .load(item.Foto_Jugador)
            .into(holder.imgCardJugadores)

        holder.imgEliminarJugador.setOnClickListener {
            val context = holder.itemView.context
            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogLayout = inflater.inflate(R.layout.alertdialog_elim_jugadores, null)

            builder.setView(dialogLayout)
            val alertDialog = builder.create()

            dialogLayout.findViewById<Button>(R.id.btnCancelarEliminarJug).setOnClickListener {
                alertDialog.dismiss()
            }

            dialogLayout.findViewById<Button>(R.id.btnEliminarJug).setOnClickListener {
                eliminarDatos(item.Nombre_Jugador, item.Posicion_Jugador, item.Numero_Jugador, position)
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        holder.imgEditarJugador.setOnClickListener {
            val context = holder.itemView.context
            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogLayout = inflater.inflate(R.layout.alertdialog_edit_jugadores, null)

            val txtEditar_Nombre_Jugad = dialogLayout.findViewById<EditText>(R.id.txtEditar_Nombre_Jugad)
            val txtApellido_Jugad = dialogLayout.findViewById<EditText>(R.id.txtApellido_Jugad)
            val txtNumDorsal_Jugad = dialogLayout.findViewById<EditText>(R.id.txtNumDorsal_Jugad)
            val txtPosicion_Jugad = dialogLayout.findViewById<EditText>(R.id.txtPosicion_Jugad)
            val spEstado_Jugad = dialogLayout.findViewById<Spinner>(R.id.spEstado_Jugad)

            spEstado_Jugad.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val listaEstadoJugador = arrayOf("Seleccionar Estado", "Activo", "Inactivo", "Expulsado", "Lesionado")

                    withContext(Dispatchers.Main){
                        val miAdaptador = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, listaEstadoJugador)
                        spEstado_Jugad.adapter = miAdaptador
                    }
                }
            }
            builder.setView(dialogLayout)
            val alertDialog = builder.create()

            dialogLayout.findViewById<Button>(R.id.btnActualizarEditarJugad).setOnClickListener {
                alertDialog.dismiss()
            }
            dialogLayout.findViewById<Button>(R.id.btnCancelarEditarJugad).setOnClickListener {
                val nombre = txtEditar_Nombre_Jugad.text.toString()
                val apellido = txtApellido_Jugad.text.toString()
                val dorsal = txtNumDorsal_Jugad.text.toString().toInt()
                val posicion = txtPosicion_Jugad.text.toString()
                val estado = spEstado_Jugad.selectedItemPosition.toString()
                editarDatos(nombre, apellido, dorsal, posicion, estado, item.UUID_Jugador)

                alertDialog.dismiss()
            }
            alertDialog.show()
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val pantallaVer = Intent(context, VerJugadores::class.java)
            pantallaVer.putExtra("UUID_Jugador", item.UUID_Jugador)
            pantallaVer.putExtra("NombreJugador", item.Nombre_Jugador)
            pantallaVer.putExtra("ApellidoJugador", item.Apellido_Jugador)
            pantallaVer.putExtra("FNacimientoJugador", item.FNacimiento_Jugador)
            pantallaVer.putExtra("NumeroJugador", item.Numero_Jugador)
            pantallaVer.putExtra("PosicionJugador", item.Posicion_Jugador)
            pantallaVer.putExtra("EstadoJugador", item.UUID_Estado_Jugador)
            pantallaVer.putExtra("FotoJugador", item.Foto_Jugador)
            context.startActivity(pantallaVer)
        }

    }
}
