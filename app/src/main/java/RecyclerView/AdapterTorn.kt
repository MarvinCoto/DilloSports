package RecyclerView

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import marvin.coto.dillosports.R
import marvin.coto.dillosports.Ver_Torneo
import modelos.tbTorneos

class AdapterTorn(var Datos: List<tbTorneos>): RecyclerView.Adapter<ViewHolderTorn>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTorn {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card_torneos, parent, false)
        return ViewHolderTorn(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderTorn, position: Int) {
        val item = Datos[position]
        holder.txtNombreTorneo.text = item.Nombre_Torneo
        holder.txtUbicacionTorneo.text = item.Ubicacion_Torneo
        holder.txtDescripcionTorneo.text = item.Descripcion_Torneo
        Glide.with(holder.imgCardTorneos.context)
            .load(item.Logo_Torneo)
            .into(holder.imgCardTorneos)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val pantallaVer = Intent(context, Ver_Torneo::class.java)
            pantallaVer.putExtra("UUID_Torneo", item.UUID_Torneo)
            pantallaVer.putExtra("NombreTorneo", item.Nombre_Torneo)
            pantallaVer.putExtra("UbicacionTorneo", item.Ubicacion_Torneo)
            pantallaVer.putExtra("DescripcionTorneo", item.Descripcion_Torneo)
            pantallaVer.putExtra("LogoTorneo", item.Logo_Torneo)
            pantallaVer.putExtra("DeporteTorneo", item.UUID_Tipo_Deporte)
            context.startActivity(pantallaVer)
        }
    }

}
