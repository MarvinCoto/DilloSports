package modelos

data class tbTorneos(
    val UUID_Torneo: String,
    val Nombre_Torneo: String,
    val Ubicacion_Torneo: String,
    val Descripcion_Torneo: String,
    val Logo_Torneo: String,
    val UUID_Estado_Torneo: String,
    val UUID_Tipo_Deporte: String,
    val UUID_Usuario: String
)
