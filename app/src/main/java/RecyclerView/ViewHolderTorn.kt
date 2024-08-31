package RecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import marvin.coto.dillosports.R

class ViewHolderTorn(view: View): RecyclerView.ViewHolder(view) {
    val txtNombreTorneo = view.findViewById<TextView>(R.id.txtNombresTorneos)
    val txtUbicacionTorneo = view.findViewById<TextView>(R.id.txtUbicacionesTorneos)
    val txtDescripcionTorneo = view.findViewById<TextView>(R.id.txtDescripcionTorneo)
    //val imgCardTorneos = view.findViewById<ImageView>(R.id.imgCardTorneos)
}