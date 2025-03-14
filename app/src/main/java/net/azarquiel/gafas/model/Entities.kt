package net.azarquiel.gafas.model
import java.io.Serializable

data class Marca(
    var id: Int,
    var nombre: String,
    var imagen: String
) : Serializable


data class Gafa(
    var id: Int,
    var nombre: String,
    var imagen: String,
    var precio: Double,
    var marca: Int
) : Serializable


data class Usuario(
    var id: Int,
    var nick: String,
    var pass: String
) : Serializable


data class Comentario(
    var id: Int = 0,
    var usuario: Int,
    var nick: String="",
    var gafa: Int,
    var fecha: String,
    var comentario: String
) : Serializable


data class Result(
    val marcas: List<Marca>,
    val gafas: List<Gafa>,
    val comentarios: List<Comentario>,
    val usuarios: List<Usuario>,
    val usuario: Usuario,
    val comentario: Comentario
)