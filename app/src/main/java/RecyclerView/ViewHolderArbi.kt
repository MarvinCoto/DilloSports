package RecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import marvin.coto.dillosports.R


class ViewHolderArbi(view: View): RecyclerView.ViewHolder(view) {
    val txtNombreArbitro = view.findViewById<TextView>(R.id.txtNombreArbitro)
    val imgEditarArbitro = view.findViewById<ImageView>(R.id.imgEditarArbitro)
    val imgEliminarArbitro = view.findViewById<ImageView>(R.id.imgEliminarArbitro)
}