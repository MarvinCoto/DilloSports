package modelos

data class tbUsuarios(
    val UUID_Usuario: String,
    val Nombre_Usuario: String,
    val Apellido_Usuario: String,
    val User_name: String,
    val Contrasena_Usuario: String,
    val Correo_Usuario: String,
    val FNacimiento_Usuario: String,
    val Foto_Usuario: String,
    val UUID_Tipo_Usuario: Int,
    val Genero: String
)
{ companion object {
        var currentUser: tbUsuarios? = null
    }
}
