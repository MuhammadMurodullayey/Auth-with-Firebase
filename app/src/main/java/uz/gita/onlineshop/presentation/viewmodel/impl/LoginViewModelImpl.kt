package uz.gita.onlineshop.presentation.viewmodel.impl

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.onlineshop.R
import uz.gita.onlineshop.domain.AppRepository
import uz.gita.onlineshop.presentation.viewmodel.LoginViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: AppRepository
) : ViewModel(), LoginViewModel {
    override val errorLiveData = MutableLiveData<String>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val goToNextScreenLiveData = MutableLiveData<Int>()

    override fun goToRegisterScreen() {
        goToNextScreenLiveData.value = 0
    }

    override fun goToHomeScreen() {
        goToNextScreenLiveData.value = 1
    }

    override fun loginUser(number: String, password: String) {
        progressLiveData.value = true
        repository.isUserHave(number).onEach { result ->
            progressLiveData.value = false
            result.onSuccess {
                if (!it.name.isNullOrEmpty()) {
                    if (password == it.password) {
                        goToNextScreenLiveData.value = 1
                    } else {
                        errorLiveData.value = context.resources.getString(R.string.wrong_password)
                    }
                } else {
                    errorLiveData.value = context.resources.getString(R.string.this_user_has_not_yet_registered)
                }
            }
            result.onFailure {
                errorLiveData.value = it.message
            }
        }.launchIn(viewModelScope)

    }
}