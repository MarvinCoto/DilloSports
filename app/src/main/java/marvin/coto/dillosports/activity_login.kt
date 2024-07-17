package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.ClaseConexion
import java.security.MessageDigest
import java.util.Calendar
import java.util.UUID

class activity_login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1- Mando a llamar a los elementos de la vista
        val txtNombreRegistro = findViewById<EditText>(R.id.txtNombreRegistro)
        val txtApellidoRegistro = findViewById<EditText>(R.id.txtApellidoRegistro)
        val txtUsernameRegistro = findViewById<EditText>(R.id.txtUsernameRegistro)
        val txtContraseniaRegistro = findViewById<EditText>(R.id.txtContraseniaRegistro)
        val txtCorreoRegistro = findViewById<EditText>(R.id.txtCorreoRegistro)
        val txtTelefonoRegistro = findViewById<EditText>(R.id.txtTelefonoRegistro)
        val txtGeneroRegistro = findViewById<EditText>(R.id.txtGeneroRegistro)
        val txtNacimientoRegistro = findViewById<EditText>(R.id.txtNacimientoRegistro)
        val txtEdadRegistro = findViewById<EditText>(R.id.txtEdadRegistro)
        val txtAlturaRegistro = findViewById<EditText>(R.id.txtAlturaRegistro)
        val txtPesoRegistro = findViewById<EditText>(R.id.txtPesoRegistro)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val btnIrALogin = findViewById<Button>(R.id.btnIrALogin)

        txtNacimientoRegistro.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)
        }

        //Creo la función para encriptar la contraseña
        fun hashSHA256(contraseniaEscrita: String): String {
            val bytes =
                MessageDigest.getInstance("SHA-256").digest(contraseniaEscrita.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

        btnCrearCuenta.setOnClickListener {

            //Guardo en una variable los valores que escribió el usuario

            val nombre = txtNombreRegistro.text.toString()
            val apellido = txtApellidoRegistro.text.toString()
            val username = txtUsernameRegistro.text.toString()
            val contrasenia = txtContraseniaRegistro.text.toString()
            val correo = txtCorreoRegistro.text.toString()
            val telefono = txtTelefonoRegistro.text.toString()
            val genero = txtGeneroRegistro.text.toString()
            val fecha = txtNacimientoRegistro.text.toString()
            val edad = txtEdadRegistro.text.toString()
            val altura = txtAlturaRegistro.text.toString()
            val peso = txtPesoRegistro.text.toString()

            //Creo la variable para verificar que no hay errores
            //Se inicializa en false
            var hayErrores = false

            //1-
            //Validar que los campos no estén vacios

            if (nombre.isEmpty()) {
                txtNombreRegistro.error = "El nombre es obligatorio"
                hayErrores = true
            } else {
                txtNombreRegistro.error = null
            }

            if (apellido.isEmpty()) {
                txtApellidoRegistro.error = "El apellido es obligatorio"
                hayErrores = true
            } else {
                txtApellidoRegistro.error = null
            }

            if (username.isEmpty()) {
                txtUsernameRegistro.error = "El username es obligatorio"
                hayErrores = true
            } else {
                txtUsernameRegistro.error = null
            }

            if (contrasenia.isEmpty()) {
                txtContraseniaRegistro.error = "El contraseña es obligatorio"
                hayErrores = true
            } else {
                txtContraseniaRegistro.error = null
            }

            if (correo.isEmpty()) {
                txtCorreoRegistro.error = "El correo electrónico es obligatorio"
                hayErrores = true
            } else {
                txtCorreoRegistro.error = null
            }

            if (telefono.isEmpty()) {
                txtTelefonoRegistro.error = "El teléfono es obligatorio"
                hayErrores = true
            } else {
                txtTelefonoRegistro.error = null
            }

            if (genero.isEmpty()) {
                txtGeneroRegistro.error = "El género es obligatorio"
                hayErrores = true
            } else {
                txtGeneroRegistro.error = null
            }

            if (fecha.isEmpty()) {
                txtNacimientoRegistro.error = "La fecha de nacimiento es obligatorio"
                hayErrores = true
            } else {
                txtNacimientoRegistro.error = null
            }

            if (edad.isEmpty()) {
                txtEdadRegistro.error = "La edad es obligatorio"
                hayErrores = true
            } else {
                txtEdadRegistro.error = null
            }

            if (altura.isEmpty()) {
                txtAlturaRegistro.error = "La altura es obligatorio"
                hayErrores = true
            } else {
                txtEdadRegistro.error = null
            }

            if (peso.isEmpty()) {
                txtPesoRegistro.error = "El peso es obligatorio"
                hayErrores = true
            } else {
                txtPesoRegistro.error = null
            }

            //Validar que el correo sea correo

            if (!correo.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+[.][a-z]+"))) {
                txtCorreoRegistro.error = "El correo no tiene un formato válido"
                hayErrores = true
            } else {
                txtCorreoRegistro.error = null
            }

            //Validar los caracteres de la contraseña

            if (contrasenia.length <= 7) {
                txtContraseniaRegistro.error = "La contraseña debe tener más de 7 caracteres"
                hayErrores = true
            } else {
                txtContraseniaRegistro.error = null
            }

            //Funcion para limpiar campos

            fun limpiarCampos() {

                txtNombreRegistro.text.clear()
                txtApellidoRegistro.text.clear()
                txtUsernameRegistro.text.clear()
                txtContraseniaRegistro.text.clear()
                txtCorreoRegistro.text.clear()
                txtTelefonoRegistro.text.clear()
                txtGeneroRegistro.text.clear()
                txtNacimientoRegistro.text.clear()
                txtEdadRegistro.text.clear()
                txtAlturaRegistro.text.clear()
                txtPesoRegistro.text.clear()

            }

            //Funcion para guardar los datos

            fun guardarUsuario(
                nombre: String,
                apellido: String,
                username: String,
                contrasenia: String,
                correo: String,
                telefono: String,
                genero: String,
                fecha: String,
                edad: Int,
                altura: Int,
                peso: Int
            ) {

                GlobalScope.launch(Dispatchers.IO) {
                    //Creo un objeto de la clase conexion
                    val objConexion = ClaseConexion().cadenaConexion()

                    //Encripto la contraseña
                    val contraseniaEncriptada = hashSHA256(txtContraseniaRegistro.text.toString())

                    //Creo una variable que contenga el PrepareStatement
                    val crearUsuario =
                        objConexion?.prepareStatement("INSERT INTO tbUsuarios(UUID_Usuario, Nombre_Usuario, Apellido_Usuario, User_name, Contrasena_Usuario, Correo_Usuario, Telefono_Usuario, Genero_Usuario, FNacimiento_Usuario, Edad_Usuario, Altura_Usuario, Peso_Usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")!!
                    crearUsuario.setString(1, UUID.randomUUID().toString())
                    crearUsuario.setString(2, txtNombreRegistro.text.toString())
                    crearUsuario.setString(3, txtApellidoRegistro.text.toString())
                    crearUsuario.setString(4, txtUsernameRegistro.text.toString())
                    crearUsuario.setString(5, contraseniaEncriptada)
                    crearUsuario.setString(6, txtCorreoRegistro.text.toString())
                    crearUsuario.setString(7, txtTelefonoRegistro.text.toString())
                    crearUsuario.setString(8, txtGeneroRegistro.text.toString())
                    crearUsuario.setString(9, txtNacimientoRegistro.text.toString())
                    crearUsuario.setInt(10, txtEdadRegistro.text.toString().toInt())
                    crearUsuario.setInt(11, txtAlturaRegistro.text.toString().toInt())
                    crearUsuario.setInt(12, txtPesoRegistro.text.toString().toInt())
                    crearUsuario.executeUpdate()
                    withContext(Dispatchers.Main) {
                        //Abro otra corrutiana para mostrar el mensaje y limpiar campos
                        Toast.makeText(
                            this@activity_login,
                            "Usuario creado con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                        txtNombreRegistro.setText("")
                        txtApellidoRegistro.setText("")
                        txtUsernameRegistro.setText("")
                        txtContraseniaRegistro.setText("")
                        txtCorreoRegistro.setText("")
                        txtTelefonoRegistro.setText("")
                        txtGeneroRegistro.setText("")
                        txtNacimientoRegistro.setText("")
                        txtEdadRegistro.setText("")
                        txtAlturaRegistro.setText("")
                        txtPesoRegistro.setText("")
                    }
                }
                val intent = Intent(this, Home::class.java)
                startActivity(intent)

                if (hayErrores) {
                    Toast.makeText(this@activity_login, "Datos ingresados incorrectamente", Toast.LENGTH_SHORT).show()
                } else {
                    // Si todas las validaciones son correctas, procede a guardar los datos
                    guardarUsuario(nombre, apellido, username, contrasenia, correo, telefono, genero, fecha, edad.toInt(), altura.toInt(), peso.toInt())
                    limpiarCampos()
                }
            }


        }

        btnIrALogin.setOnClickListener {
            val pantallaLogin = Intent(this, activity_iniciar_sesion::class.java)
            startActivity(pantallaLogin)
        }



    }
}