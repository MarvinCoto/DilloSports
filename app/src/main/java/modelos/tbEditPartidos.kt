package modelos

data class tbEditPartidos(
    val UUID_Partido: String,
    val UUID_Equipo1: String,
    val UUID_Equipo2: String,
    val Fecha_Partido: String,
    val Lugar_Partido: String,
    val Hora_Partido: String,
    val Marcador_Equipo1: Int,
    val Marcador_Equipo2: Int,
    val UUID_Tipo_Partido: String,
    val UUID_Arbitro: String
)
