package RecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import marvin.coto.dillosports.R

class ViewHolderNoticias(view: View): RecyclerView.ViewHolder(view) {
    val imgNoticia = view.findViewById<ImageView>(R.id.imgNoticia)
    val txtTitleNoticia = view.findViewById<TextView>(R.id.txtTitleNoticia)
    val txtDescripcionNoticia = view.findViewById<TextView>(R.id.txtDescripcionNoticia)
    val txtHoradeNoticia = view.findViewById<TextView>(R.id.txtHoradeNoticia)
}