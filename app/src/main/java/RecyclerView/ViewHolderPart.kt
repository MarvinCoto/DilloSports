package RecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import marvin.coto.dillosports.R

class ViewHolderPart(view: View): RecyclerView.ViewHolder(view) {
    //val imgEquipo1 = view.findViewById<ImageView>(R.id.imgEquipo1)
    //val imgEquipo2 = view.findViewById<ImageView>(R.id.imgEquipo2)
   // val txtNombreEquipo1 = view.findViewById<TextView>(R.id.txtNombreEquipo1) sppiners
    //val txtNombreEquipo2 = view.findViewById<TextView>(R.id.txtNombreEquipo2)
    val txtResultado1 = view.findViewById<TextView>(R.id.txtResultado1)
    val txtResultado2 = view.findViewById<TextView>(R.id.txtResultado2)
    val txtEspPartido = view.findViewById<TextView>(R.id.txtEspPartido)
    val txtFechaPart = view.findViewById<TextView>(R.id.txtFechaPart)
    val imgEditarPartido = view.findViewById<ImageView>(R.id.imgEditarPartido)
    val imgEliminarPartido = view.findViewById<ImageView>(R.id.imgEliminarPartido)
}