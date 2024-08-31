package RecyclerView

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import marvin.coto.dillosports.R
import marvin.coto.dillosports.VerJugadores
import marvin.coto.dillosports.Ver_Torneo
import modelos.ClaseConexion
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
        //holder.imgCardTorneos = item.Logo_Torneo

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val pantallaVer = Intent(context, Ver_Torneo::class.java)
            pantallaVer.putExtra("UUID_Torneo", item.UUID_Torneo)
            pantallaVer.putExtra("NombreTorneo", item.Nombre_Torneo)
            pantallaVer.putExtra("UbicacionTorneo", item.Ubicacion_Torneo)
            pantallaVer.putExtra("DescripcionTorneo", item.Descripcion_Torneo)
            pantallaVer.putExtra("DeporteTorneo", item.UUID_Tipo_Deporte)
            pantallaVer.putExtra("EstadoToneo", item.Estado_Toneo)
            pantallaVer.putExtra("LogoTorneo", item.Logo_Torneo)
            context.startActivity(pantallaVer)
        }
    }

}
