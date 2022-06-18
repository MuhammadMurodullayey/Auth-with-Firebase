package uz.gita.onlineshop.presentation.viewmodel

import androidx.lifecycle.LiveData
import uz.gita.onlineshop.data.UserModel

interface RegisterViewModel  {
    val errorLiveData : LiveData<String>
    val progressLiveData : LiveData<Boolean>
    val goToNextScreenLiveData : LiveData<Unit>
    val goToLoginScreenLiveData : LiveData<Unit>
    val registerWithPhoneNumberLiveData :LiveData<UserModel>

    fun registerUser(name : String, emailOrPhoneNumber : String, password :String)
    fun goToNextScreen()
    fun goToLoginScreen()
    fun getUser()
}