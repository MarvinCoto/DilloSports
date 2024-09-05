package RecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import marvin.coto.dillosports.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val imgCardJugadores = view.findViewById<ImageView>(R.id.imgCardJugadores)
    val txtNombreJugador = view.findViewById<TextView>(R.id.txtNombreJugador)
    val txtPosicionJugador = view.findViewById<TextView>(R.id.txtPosicionJugador)
    val txtNumeroCamiseta = view.findViewById<TextView>(R.id.txtNumeroCamisa)
    val imgEliminarJugador = view.findViewById<ImageView>(R.id.imgEliminarJugador)
    val imgEditarJugador = view.findViewById<ImageView>(R.id.imgEditarJugador)
}