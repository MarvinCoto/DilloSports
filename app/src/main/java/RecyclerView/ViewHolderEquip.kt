package RecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import marvin.coto.dillosports.R

class ViewHolderEquip(view: View): RecyclerView.ViewHolder(view) {
    val txtNombreEquipo = view.findViewById<TextView>(R.id.txtNombreEquipo)
    val imgEditarEquipo = view.findViewById<ImageView>(R.id.imgEditarEquipo)
    val imgEliminarEquipo = view.findViewById<ImageView>(R.id.imgEliminarEquipo)
    val imgCardEquipo = view.findViewById<ImageView>(R.id.imgCardEquipo)
}