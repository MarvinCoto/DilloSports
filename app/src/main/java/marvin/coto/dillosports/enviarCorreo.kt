package marvin.coto.dillosports

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

suspend fun enviarCorreo(receptor: String, asunto: String, mensaje: String) = withContext(Dispatchers.IO){
    val props = Properties().apply {
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.socketFactory.port", "465")
        put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        put("mail.smtp.auth", "true")
        put("mail.smtp.port", "465")
    }

    val session = Session.getInstance(props, object : javax.mail.Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication("oficial.dillo.sports@gmail.com", "l b v d y n r x x n g b w m g u")
        }
    })

    try {
        val message = MimeMessage(session).apply {
            setFrom(InternetAddress("oficial.dillo.sports@gmail.com"))
            addRecipient(Message.RecipientType.TO, InternetAddress(receptor))
            subject = asunto
            setContent(mensaje, "text/html; charset=utf-8")
        }
        Transport.send(message)
        println("Correo enviado satisfactoriamente")
    } catch (e: MessagingException) {
        e.printStackTrace()
        println("Correo no enviado, ERROR: ${e.message}")
    }
}