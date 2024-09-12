package RecyclerView

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import marvin.coto.dillosports.R
import marvin.coto.dillosports.Ver_Torneo
import marvin.coto.dillosports.Ver_Torneo_User
import modelos.tbTorneos


class AdapterTornUser(var Datos: List<tbTorneos>): RecyclerView.Adapter<ViewHolderTornUser>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTornUser {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card_torneos_user, parent, false)
        return ViewHolderTornUser(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderTornUser, position: Int) {
        val item = Datos[position]
        holder.txtNombresTorneosUser.text = item.Nombre_Torneo
        holder.txtUbicacionesTorneosUser.text = item.Ubicacion_Torneo
        holder.txtDescripcionTorneoUser.text = item.Descripcion_Torneo
        Glide.with(holder.imgCardTorneosUser.context)
            .load(item.Logo_Torneo)
            .into(holder.imgCardTorneosUser)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val pantallaVer = Intent(context, Ver_Torneo_User::class.java)
            pantallaVer.putExtra("UUID_Torneo", item.UUID_Torneo)
            pantallaVer.putExtra("NombreTorneo", item.Nombre_Torneo)
            pantallaVer.putExtra("UbicacionTorneo", item.Ubicacion_Torneo)
            pantallaVer.putExtra("DescripcionTorneo", item.Descripcion_Torneo)
            pantallaVer.putExtra("DeporteTorneo", item.UUID_Tipo_Deporte)
            pantallaVer.putExtra("LogoTorneo", item.Logo_Torneo)
            context.startActivity(pantallaVer)
        }
    }

}