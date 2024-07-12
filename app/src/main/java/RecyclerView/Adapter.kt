package RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import modelos.tbJugadores
import androidx.recyclerview.widget.RecyclerView
import marvin.coto.dillosports.R

class Adapter(var Datos:List<tbJugadores>): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       //Unir card con RecyclerView con la card
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card_jugadores, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = Datos[position]
        holder.cardJugadores
    }
}