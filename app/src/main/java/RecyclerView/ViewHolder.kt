package RecyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import marvin.coto.dillosports.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val cardJugadores = view.findViewById<TextView>(R.id.cardViewJugadores)
}