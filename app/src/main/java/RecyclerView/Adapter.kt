package RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import modelos.tbJugadores
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import marvin.coto.dillosports.R
import modelos.ClaseConexion

class Adapter(var Datos:List<tbJugadores>): RecyclerView.Adapter<ViewHolder>() {

    ///eliminar datos
    fun eliminarDatos(UUID_Jug: String, Nombre_Jugador: String, position: Int){
        val ListaDatos = Datos.toMutableList()
        ListaDatos.removeAt(position)

        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()

            val deleteJugadores = objConexion?.prepareStatement(" delete tbJugadores where Nombre_Jugador = ?")!!
            deleteJugadores.setString(1, Nombre_Jugador)
            deleteJugadores.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()

        }
        Datos = ListaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()




    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       //Unir card con RecyclerView con la card
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card_jugadores, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = Datos[position]
        holder.cardJugadores



        //DARLE CLICK EN BOTON ELIMINAR
        holder.cardJugadores.text = item.Nombre_Jugador


    }







}
