package RecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import marvin.coto.dillosports.R

class ViewHolderTornUser(view: View): RecyclerView.ViewHolder(view) {
    val imgCardTorneosUser = view.findViewById<ImageView>(R.id.imgCardTorneosUser)
    val txtNombresTorneosUser = view.findViewById<TextView>(R.id.txtNombresTorneosUser)
    val txtUbicacionesTorneosUser = view.findViewById<TextView>(R.id.txtUbicacionesTorneosUser)
    val txtDescripcionTorneoUser = view.findViewById<TextView>(R.id.txtDescripcionTorneoUser)
}