package RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import marvin.coto.dillosports.R
import modelos.tbNoticias

class AdapterNoti(var Datos: List<tbNoticias>): RecyclerView.Adapter<ViewHolderNoticias>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNoticias {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card_noticias, parent, false)
        return ViewHolderNoticias(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderNoticias, position: Int) {
        val item = Datos[position]
        holder.txtTitleNoticia.text = item.Nombre_Noticia
        holder.txtDescripcionNoticia.text = item.Descripcion_Noticia
        holder.txtHoradeNoticia.text = item.Hora_Noticia
        Glide.with(holder.imgNoticia.context)
            .load(item.Imagen_Noticia)
            .into(holder.imgNoticia)
    }

}