package net.azarquiel.gafas.viewmodel

import android.content.Context
import android.widget.Toast
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.azarquiel.gafas.model.Comentario
import net.azarquiel.gafas.model.Gafa
import net.azarquiel.gafas.model.Marca
import net.azarquiel.gafas.model.Usuario
import net.azarquiel.gafas.view.MainActivity

class MainViewModel(mainActivity: MainActivity): ViewModel()  {

    private val mainActivity by lazy { mainActivity }

    private var dataviewModel: DataViewModel=ViewModelProvider(mainActivity)[DataViewModel::class.java]

    private val _usuario: MutableLiveData<Usuario?> = MutableLiveData()
    val usuario: LiveData<Usuario?> = _usuario

    private val _openDialogLogin = MutableLiveData(false)
    val openDialogLogin: LiveData<Boolean> = _openDialogLogin

    private val _openDialogComentario = MutableLiveData(false)
    val openDialogComentario: LiveData<Boolean> = _openDialogComentario

    private val _marcas: MutableLiveData<List<Marca>> = MutableLiveData()
    val marcas: LiveData<List<Marca>> = _marcas

    private val _gafas: MutableLiveData<List<Gafa>> = MutableLiveData()
    val gafas: LiveData<List<Gafa>> = _gafas

    private val _comentarios: MutableLiveData<List<Comentario>> = MutableLiveData()
    val comentarios: LiveData<List<Comentario>> = _comentarios

    private val sharedPreferences: SharedPreferences =
        mainActivity.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    init {
        loadUserFromPreferences()
        dataviewModel.getMarcas().observe(mainActivity) {
            it?.let {
                _marcas.value = it
            }
        }
    }

    fun setDialogLogin(open: Boolean) {
        _openDialogLogin.value = open
    }
    fun setDialogComentario(open: Boolean) {
        _openDialogComentario.value = open
    }

    private fun saveUserToPreferences(user: Usuario) {
        with(sharedPreferences.edit()) {
            putInt("user_id", user.id)
            putString("user_nick", user.nick)
            putString("user_pass", user.pass)
            apply()
        }
    }

    fun login(nick: String, pass: String) {
        dataviewModel.getDataUsuarioPorNickPass(nick, pass).observe(mainActivity) { userlogin ->
            if (userlogin != null) {
                _usuario.value = userlogin
                saveUserToPreferences(userlogin)
                Toast.makeText(mainActivity, "$nick Login ok...", Toast.LENGTH_SHORT).show()
            }
            else {
                dataviewModel.saveUsuario(Usuario(-1, nick, pass)).observe(mainActivity) { userregister ->
                    _usuario.value = userregister
                    saveUserToPreferences(userregister)
                    Toast.makeText(mainActivity, "$nick Registed...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun loadUserFromPreferences() {
        val id = sharedPreferences.getInt("user_id", -1)
        val nick = sharedPreferences.getString("user_nick", null)
        val pass = sharedPreferences.getString("user_pass", null)

        if (id != -1 && nick != null && pass != null) {
            _usuario.value = Usuario(id, nick, pass)
        }
    }

    fun logout() {
        _usuario.value = null
        sharedPreferences.edit().clear().apply()
        Toast.makeText(mainActivity, "Logout exitoso", Toast.LENGTH_SHORT).show()
    }

    fun getGafasPorMarca(idmarca: Int){
        dataviewModel.getGafas(idmarca).observe(mainActivity) {
            it?.let {
                _gafas.value = it
            }
        }
    }
    fun getComentarios(idmarca: Int): LiveData<List<Comentario>> {
        dataviewModel.getComentarios(idmarca).observe(mainActivity) {
            it?.let {
                _comentarios.value = it
            }
        }
        return comentarios
    }
    fun saveComentario(idmarca: Int, comentario: Comentario) {
        dataviewModel.saveComentario(idmarca, comentario).observe(mainActivity) {
            it?.let {
                getComentarios(idmarca)
                Toast.makeText(mainActivity, "Comentario insertado con éxito...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
