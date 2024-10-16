package marvin.coto.dillosports

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.ClaseConexion
import modelos.tbArbitros
import modelos.tbEquipos
import java.util.Calendar
import java.util.UUID

class Calendarizar_partido : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calendarizar_partido)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtFechaPartido = findViewById<EditText>(R.id.txtFechaPartido)
        val txtLugarPartido = findViewById<EditText>(R.id.txtLugarPartido)
        val txtHoraPartido = findViewById<EditText>(R.id.txtHoraPartido)
        val txtTipoPartido = findViewById<EditText>(R.id.txtTipoPartido)
        val spEquipoUno = findViewById<Spinner>(R.id.spEquipoUno)
        val spEquipoDos = findViewById<Spinner>(R.id.spEquipoDos)
        val spArbitro = findViewById<Spinner>(R.id.spArbitro)
        val btnCalendarizarPartido = findViewById<Button>(R.id.btnCalendarizarPartido)
        val imgAtrasdeCalPart = findViewById<ImageView>(R.id.imgAtrasdeCalPart)

        imgAtrasdeCalPart.setOnClickListener {
            val intent = Intent(this, Partidos::class.java)
            startActivity(intent)
        }

        txtFechaPartido.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                R.style.CustomDatePickerDialog,
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada =
                        "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtFechaPartido.setText(fechaSeleccionada)
                },
                anio, mes, dia
            )
            datePickerDialog.show()
        }

         fun showTimePickerDialog() {
            val calendario = Calendar.getInstance()
            val hour = calendario.get(Calendar.HOUR_OF_DAY)
            val minute = calendario.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                R.style.CustomTimePickerDialog,
                { _, selectedHour, selectedMinute ->
                    val amPm = if (selectedHour < 12) "AM" else "PM"
                    val hourIn12Format = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                    val horaSeleccionada = String.format("%02d:%02d %s", hourIn12Format, selectedMinute, amPm)
                    txtHoraPartido.setText(horaSeleccionada)
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }

        txtHoraPartido.setOnClickListener {
            showTimePickerDialog()
        }



        fun obtenerEquipo(): List<tbEquipos> {
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbEquipos")!!
            val listaEquipos = mutableListOf<tbEquipos>()

            while (resultSet.next()) {
                val uuidEquipo = resultSet.getString("UUID_Equipo")
                val nombreEquipo = resultSet.getString("Nombre_Equipo")
                val descripcionEquipo = resultSet.getString("Descripcion_Equipo")
                val ubicacionEquipo = resultSet.getString("Ubicacion_Equipo")
                val logoEquipo = resultSet.getString("Logo_Equipo")
                val estadoEquipo = resultSet.getString("UUID_Estado_Equipo")
                val unEquipo = tbEquipos(uuidEquipo, nombreEquipo, descripcionEquipo, ubicacionEquipo, logoEquipo, estadoEquipo)
                listaEquipos.add(unEquipo)
            }
            return listaEquipos
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listaEquipos = obtenerEquipo()
            val nombreEquipo = listaEquipos.map { it.Nombre_Equipo }

            withContext(Dispatchers.Main){
                val miAdaptador = ArrayAdapter(this@Calendarizar_partido, android.R.layout.simple_spinner_dropdown_item, nombreEquipo)
                spEquipoUno.adapter = miAdaptador
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listaEquiposs = obtenerEquipo()
            val nombreEquipos = listaEquiposs.map { it.Nombre_Equipo }

            withContext(Dispatchers.Main){
                val miAdaptador = ArrayAdapter(this@Calendarizar_partido, android.R.layout.simple_spinner_dropdown_item, nombreEquipos)
                spEquipoDos.adapter = miAdaptador
            }
        }

        fun obtenerArbitro(): List<tbArbitros> {
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM tbArbitros")!!
            val listaArbitros = mutableListOf<tbArbitros>()

            while (resultSet.next()) {
                val uuidArbitro = resultSet.getString("UUID_Arbitro")
                val nombreArbitro = resultSet.getString("Nombre_Arbitro")
                val apellidoArbitro = resultSet.getString("Apellido_Arbitro")
                val edadArbitro = resultSet.getInt("Edad_Arbitro")
                val telefonoArbitro = resultSet.getString("Telefono_Arbitro")
                val fotoArbitro = resultSet.getString("Foto_Arbitro")
                val unArbitro = tbArbitros(uuidArbitro, nombreArbitro, apellidoArbitro, edadArbitro, telefonoArbitro, fotoArbitro)
                listaArbitros.add(unArbitro)
            }
            return listaArbitros
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listaArbitros = obtenerArbitro()
            val nombreArbitro = listaArbitros.map { it.Nombre_Arbitro }

            withContext(Dispatchers.Main){
                val miAdaptador = ArrayAdapter(this@Calendarizar_partido, android.R.layout.simple_spinner_dropdown_item, nombreArbitro)
                spArbitro.adapter = miAdaptador
            }
        }

        btnCalendarizarPartido.setOnClickListener {
            val lugar = txtLugarPartido.text.toString()
            val tipo = txtTipoPartido.text.toString()
            val fecha = txtFechaPartido.text.toString()
            val hora = txtHoraPartido.text.toString()

            var hayErrores = false

            if (lugar.isEmpty()) {
                txtLugarPartido.error = "Este campo es obligatorio"
                hayErrores = true
            }
            else {
                txtLugarPartido.error = null
            }

            if (tipo.isEmpty()) {
                txtTipoPartido.error = "Este campo es obligatorio"
                hayErrores = true
            }
            else {
                txtTipoPartido.error = null
            }

            if (fecha.isEmpty()) {
                txtFechaPartido.error = "Este campo es obligatorio"
                hayErrores = true
            }
            else {
                txtFechaPartido.error = null
            }

            if (hora.isEmpty()) {
                txtHoraPartido.error = "Este campo es obligatorio"
                hayErrores = true
            }
            else {
                txtHoraPartido.error = null
            }

            if (hayErrores) {
                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.toast_bad, null)

                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_LONG
                toast.view = layout

                val text = layout.findViewById<TextView>(R.id.text2)
                text.text = "Datos ingresados incorrectamente"

                toast.show()
            }

            else {
                val intent = Intent(this, Partidos::class.java)
                CoroutineScope(Dispatchers.IO).launch {
                    val objConexion = ClaseConexion().cadenaConexion()
                    val equipo1 = obtenerEquipo()
                    val equipo2 = obtenerEquipo()
                    val arbitro = obtenerArbitro()

                    val addPartido = objConexion?.prepareStatement("insert into tbPartidos (UUID_Partido, UUID_Equipo1, UUID_Equipo2, Fecha_Partido, Lugar_Partido, Hora_Partido, Marcador_Equipo1, Marcador_Equipo2, Tipo_Partido, UUID_Arbitro) values (?,?,?,?,?,?,?,?,?,?)")!!
                    addPartido.setString(1, UUID.randomUUID().toString())
                    addPartido.setString(2, equipo1[spEquipoUno.selectedItemPosition].UUID_Equipo)
                    addPartido.setString(3, equipo2[spEquipoDos.selectedItemPosition].UUID_Equipo)
                    addPartido.setString(4, txtFechaPartido.text.toString())
                    addPartido.setString(5, txtLugarPartido.text.toString())
                    addPartido.setString(6, txtHoraPartido.text.toString())
                    addPartido.setInt(7, 0)
                    addPartido.setInt(8, 0)
                    addPartido.setString(9, txtTipoPartido.text.toString())
                    addPartido.setString(10, arbitro[spArbitro.selectedItemPosition].UUID_Arbitro)
                    addPartido.executeUpdate()

                    withContext(Dispatchers.Main){
                        val inflater = layoutInflater
                        val layout = inflater.inflate(R.layout.toast_good, null)

                        val toast = Toast(applicationContext)
                        toast.duration = Toast.LENGTH_LONG
                        toast.view = layout

                        val text = layout.findViewById<TextView>(R.id.text)
                        text.text = "Partido programado"

                        toast.show()
                        txtFechaPartido.setText("")
                        txtLugarPartido.setText("")
                        txtHoraPartido.setText("")
                        txtTipoPartido.setText("")
                    }
                }
                startActivity(intent)
            }
        }

    }
}