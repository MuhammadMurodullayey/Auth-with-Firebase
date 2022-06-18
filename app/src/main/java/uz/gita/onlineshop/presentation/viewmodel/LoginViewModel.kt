package uz.gita.onlineshop.presentation.viewmodel

import androidx.lifecycle.LiveData

interface LoginViewModel {
    val errorLiveData : LiveData<String>
    val progressLiveData : LiveData<Boolean>
    val goToNextScreenLiveData : LiveData<Int>

    fun goToRegisterScreen()
    fun goToHomeScreen()
    fun loginUser(number : String,password :String)
}