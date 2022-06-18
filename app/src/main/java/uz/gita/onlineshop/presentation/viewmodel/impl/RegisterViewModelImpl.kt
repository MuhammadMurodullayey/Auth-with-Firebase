package uz.gita.onlineshop.presentation.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.onlineshop.data.UserModel
import uz.gita.onlineshop.domain.AppRepository
import uz.gita.onlineshop.presentation.viewmodel.RegisterViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImpl @Inject constructor(
   private val repository: AppRepository
) : ViewModel(),RegisterViewModel {
    override val errorLiveData = MutableLiveData<String>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val goToNextScreenLiveData = MutableLiveData<Unit>()
    override val registerWithPhoneNumberLiveData =  MutableLiveData<UserModel> ()
    override val goToLoginScreenLiveData =  MutableLiveData<Unit>()

    override fun goToNextScreen() {
       goToNextScreenLiveData.value = Unit
    }

    override fun goToLoginScreen() {
        goToLoginScreenLiveData.value = Unit
    }

    override fun getUser() {
        repository.getUsers().onEach {
            it.onSuccess {

            }
            it.onFailure {

            }
        }.launchIn(viewModelScope)
    }

    override fun registerUser(name: String, emailOrPhoneNumber: String, password: String) {
        progressLiveData.value =true
        var isCorrect = true
        var list = ArrayList<UserModel>()
        repository.getUsers().onEach { result ->
            result.onSuccess {
                list = it as ArrayList<UserModel>
            }
            result.onFailure {
                errorLiveData.value = "Fail"
            }
        }.launchIn(viewModelScope)
        for (i in 0 until list.size){
            if (list[i].password == password){
                isCorrect = false
            }
        }
        if("\\+998[0-9]*".toRegex().matches(emailOrPhoneNumber) &&
            emailOrPhoneNumber.length == 13 && name.length > 2 && password.length >= 6){
            if (isCorrect){
                registerWithPhoneNumberLiveData.value = UserModel(name,emailOrPhoneNumber,password)
                progressLiveData.value =false
            }else{
                errorLiveData.value = "This user already have"
            }

        }else{
               errorLiveData.value = "Dates is not correct"
        }

    }
}