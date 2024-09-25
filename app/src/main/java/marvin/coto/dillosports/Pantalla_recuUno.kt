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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.ClaseConexion

class Pantalla_recuUno : AppCompatActivity() {
    companion object variableGlobalCodigoRecu{
         var codigoRecu = (1000..9999).random()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_recu_uno)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtRecuCorreo = findViewById<EditText>(R.id.txtRecuCorreo)
        val btnEnviarCodigo = findViewById<Button>(R.id.btnEnviarCodigo)

        btnEnviarCodigo.setOnClickListener {
            val pantallaRecudos = Intent(this, Pantalla_recuDos::class.java)

            val recu = codigoRecu.toString()
            val codigoHTML = """<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Correo</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 600px;
            margin: auto;
            background: #ffffff;
            border: 1px solid #dddddd;
            border-radius: 5px;
        }
        .header {
            background-color: #1C1C1C;
            color: #00a532;
            padding: 10px;
            text-align: center;
        }
        .content {
            padding: 20px;
        }
        .footer {
            text-align: center;
            padding: 10px;
            font-size: 12px;
            color: #777777;
        }
        a {
            color: #1C1C1C;
            text-decoration: none;
            text-decoration: underline;
            transition: .2s;
        }
        a:hover{
            color: #00a532;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>DilloSports</h1>
        </div>
        <div class="content">
            <p>Para recuperar su cuenta debede ingresar el siguiente codigo:</p>
            <h2 style="text-align: center;">Código de recuperción:</h2>
            <h1 style="color: #00a532; text-align: center; font-size: 40px;">$recu</h1>
            <p style="color: #777777;">Si presenta alguna pregunta o necesita ayuda, no dude en <a href="oficial.dillo.sports@gmail.com">contactarnos</a>.</p>
        </div>
        <div class="footer">
            <p>&copy; 2024 DilloSports | PTC </p>
        </div>
    </div>
</body>
</html>"""
            GlobalScope.launch(Dispatchers.IO) {
                val objConexion = ClaseConexion().cadenaConexion()

                val comprobarUsuario = objConexion?.prepareStatement("SELECT * FROM tbUsuarios WHERE Correo_Usuario  = ?")!!
                comprobarUsuario.setString(1, txtRecuCorreo.text.toString())
                val resultado = comprobarUsuario.executeQuery()

                if(resultado.next()){
                    CoroutineScope(Dispatchers.Main).launch {
                        enviarCorreo(
                            "${txtRecuCorreo.text}",
                            "Recuperación de cuenta",
                            codigoHTML
                        )
                    }
                    startActivity(pantallaRecudos)
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@Pantalla_recuUno, "Correo enviado correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@Pantalla_recuUno, "Correo no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
}