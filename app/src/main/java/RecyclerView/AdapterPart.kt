package RecyclerView

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import marvin.coto.dillosports.R
import modelos.ClaseConexion
import modelos.tbPartidos

class AdapterPart(var Datos: List<tbPartidos>): RecyclerView.Adapter<ViewHolderPart>() {

    // Eliminar
    fun eliminarDatos(Marcador_Equipo1: Int, Marcador_Equipo2: Int, Fecha_Partido: String, Tipo_Partido: String, posicion: Int){
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val deletePartido = objConexion?.prepareStatement("delete tbPartidos where Marcador_Equipo1 = ? and Marcador_Equipo2 = ? and Fecha_Partido = ? and Tipo_Partido = ?")!!
            deletePartido.setInt(1, Marcador_Equipo1)
            deletePartido.setInt(2, Marcador_Equipo2)
            deletePartido.setString(3, Fecha_Partido)
            deletePartido.setString(4, Tipo_Partido)
            deletePartido.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit?.executeUpdate()
        }
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    //Editar
    fun editarDatos(nuevoMarcadorEquipo1: Int, nuevoMarcadorEquipo2: Int, nuevoFechaPartido: String, nuevoTipoPartido: String, nuevoLugarPartido: String, nuevoHoraPartido: String, UUID_Partido: String){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()

            val updatePartido = objConexion?.prepareStatement("update tbPartidos set Marcador_Equipo1 =?, Marcador_Equipo2 =?, Fecha_Partido =?, Tipo_Partido =?, Lugar_Partido =?, Hora_Partido =? where UUID_Partido =?")!!
            updatePartido.setInt(1, nuevoMarcadorEquipo1)
            updatePartido.setInt(2, nuevoMarcadorEquipo2)
            updatePartido.setString(3, nuevoFechaPartido)
            updatePartido.setString(4, nuevoTipoPartido)
            updatePartido.setString(5, nuevoLugarPartido)
            updatePartido.setString(6, nuevoHoraPartido)
            updatePartido.setString(7, UUID_Partido)
            updatePartido.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit?.executeUpdate()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPart {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card_partidos, parent, false)
        return ViewHolderPart(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderPart, position: Int) {
        val item = Datos[position]
        holder.txtResultado1.text = item.Marcador_Equipo1.toString()
        holder.txtResultado2.text = item.Marcador_Equipo2.toString()
        holder.txtFechaPart.text = item.Fecha_Partido
        holder.txtEspPartido.text = item.Tipo_Partido

    }

    /*
    private fun showEditTorneoDialog() {
    // Crea el AlertDialog
    val builder = AlertDialog.Builder(this)
    val inflater = layoutInflater
    val dialogLayout = inflater.inflate(R.layout.dialog_edit_torneo, null)

    val etNombre = dialogLayout.findViewById<EditText>(R.id.etNombre)
    val etDescripcion = dialogLayout.findViewById<EditText>(R.id.etDescripcion)
    val etUbicacion = dialogLayout.findViewById<EditText>(R.id.etUbicacion)
    val spTipoDeporte = dialogLayout.findViewById<Spinner>(R.id.spTipoDeporte)
    val spEstadoTorneo = dialogLayout.findViewById<Spinner>(R.id.spEstadoTorneo)

    // Configura el AlertDialog
    builder.setView(dialogLayout)
    val alertDialog = builder.create()

    // Botones
    dialogLayout.findViewById<Button>(R.id.btnCancelar).setOnClickListener {
        alertDialog.dismiss()
    }

    dialogLayout.findViewById<Button>(R.id.btnActualizar).setOnClickListener {
        // Lógica para actualizar los datos
        val nombre = etNombre.text.toString()
        val descripcion = etDescripcion.text.toString()
        val ubicacion = etUbicacion.text.toString()
        val tipoDeporte = spTipoDeporte.selectedItem.toString()
        val estadoTorneo = spEstadoTorneo.selectedItem.toString()

        // Aquí puede añadir todo lo demás que utilizará

        alertDialog.dismiss()
    }

    alertDialog.show()
}
*/
}