package marvin.coto.dillosports

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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

class activity_iniciar_sesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciar_sesion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recuperar = findViewById<TextView>(R.id.textViewRecuperar)
        val txtCorreoUsuario = findViewById<EditText>(R.id.txtCorreoUsuario)
        val txtContraseniaUsuario = findViewById<EditText>(R.id.txtContraseniaUsuario)
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)
        val textViewIrRegistro = findViewById<TextView>(R.id.textViewIrRegistro)
        val imgVerPassword = findViewById<ImageView>(R.id.imgVerContraseñaI)

        imgVerPassword.setOnClickListener {
            if (txtContraseniaUsuario.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                txtContraseniaUsuario.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            else {
                txtContraseniaUsuario.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        recuperar.setOnClickListener {
            val intent = Intent(this, Pantalla_recuUno::class.java)
            startActivity(intent)
        }

        fun hashSHA256(contraseniaEscrita: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(contraseniaEscrita.toByteArray())
            return bytes.joinToString("") {"%02x".format(it)}
        }

        btnIniciarSesion.setOnClickListener {
            val pantallaPrincipal = Intent(this, Home::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                val objConexion = ClaseConexion().cadenaConexion()

                val contraseniaEncriptada = hashSHA256(txtContraseniaUsuario.text.toString())

                val comprobarUsuario = objConexion?.prepareStatement("SELECT * FROM tbUsuarios WHERE Correo_Usuario  = ? AND Contrasena_Usuario = ?")!!
                comprobarUsuario.setString(1, txtCorreoUsuario.text.toString())
                comprobarUsuario.setString(2, contraseniaEncriptada)
                val resultado = comprobarUsuario.executeQuery()

                if(resultado.next()){
                    startActivity(pantallaPrincipal)
                    withContext(Dispatchers.Main){
                        val inflater = layoutInflater
                        val layout = inflater.inflate(R.layout.toast_normal, null)

                        val toast = Toast(applicationContext)
                        toast.duration = Toast.LENGTH_LONG
                        toast.view = layout

                        val text = layout.findViewById<TextView>(R.id.text3)
                        text.text = "Bienvenido"

                        toast.show()
                    }
                } else {
                    withContext(Dispatchers.Main){
                        val inflater = layoutInflater
                        val layout = inflater.inflate(R.layout.toast_bad, null)

                        val toast = Toast(applicationContext)
                        toast.duration = Toast.LENGTH_LONG
                        toast.view = layout

                        val text = layout.findViewById<TextView>(R.id.text2)
                        text.text = "Usuario o contraseña incorrectos"

                        toast.show()
                    }
                }
            }
        }

        textViewIrRegistro.setOnClickListener{
            val pantallaRegistro = Intent(this, activity_login::class.java)
            startActivity((pantallaRegistro))
        }

    }
}