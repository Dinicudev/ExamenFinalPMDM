package net.azarquiel.gafas.api

import net.azarquiel.gafas.model.Comentario
import net.azarquiel.gafas.model.Gafa
import net.azarquiel.gafas.model.Marca
import net.azarquiel.gafas.model.Usuario

class MainRepository {
    private val service = WebAccess.gafasService

    suspend fun getMarcas(): List<Marca> {
        val webResponse = service.getMarcas().await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.marcas
        }
        return emptyList()
    }

    suspend fun getGafasPorMarca(idmarca: Int): List<Gafa> {
        val webResponse = service.getGafasPorMarca(idmarca).await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.gafas
        }
        return emptyList()
    }

    suspend fun getUsuarios(): List<Usuario> {
        val webResponse = service.getUsuarios().await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.usuarios
        }
        return emptyList()
    }

    suspend fun getDataUsuarioPorNickPass(nick: String, pass: String): Usuario? {
        val webResponse = service.getDataUsuarioPorNickPass(nick, pass).await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.usuario
        }
        return null
    }

    suspend fun saveUsuario(usuario: Usuario): Usuario? {
        val webResponse = service.saveUsuario(usuario).await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.usuario
        }
        return null
    }

    suspend fun getComentariosPorGafa(idgafa: Int): List<Comentario> {
        val webResponse = service.getComentariosPorGafa(idgafa).await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.comentarios
        }
        return emptyList()
    }

    suspend fun saveComentario(idgafa: Int, comentario: Comentario): Comentario? {
        val webResponse = service.saveComentario(idgafa, comentario)
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.comentario
        }
        return null
    }

}