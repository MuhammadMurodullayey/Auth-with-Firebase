package uz.gita.onlineshop.presentation.viewmodel.impl

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.onlineshop.data.UserModel
import uz.gita.onlineshop.domain.AppRepository
import uz.gita.onlineshop.presentation.viewmodel.VerifyViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class VerifyViewModelImpl @Inject constructor(
    private val repository: AppRepository
) : ViewModel(), VerifyViewModel {
    private var credential : AuthCredential? = null
    private var storedVerificationId = ""

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential_: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            errorLiveData.value = "verify failed: ${e.message}"

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }


        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {

           storedVerificationId = verificationId

        }
    }
    override val errorLiveData = MutableLiveData<String>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val timeLiveData = MutableLiveData<String>()
    override val goToNextScreenLiveData = MutableLiveData<Unit>()
    private var time = 120
    private val myAuth = Firebase.auth


    override fun addUser(user: UserModel) {
        repository.addUser(user).onEach {
            it.onSuccess {
                progressLiveData.value = false
                goToNextScreenLiveData.value = Unit
            }
            it.onFailure {
                errorLiveData.value = it.message
            }
        }.launchIn(viewModelScope)
    }


    override fun checkUser(user: UserModel, activity: Activity, code: String) {
       progressLiveData.value = true
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        myAuth.signInWithCredential(credential).addOnSuccessListener {
            addUser(user)
        }
        .addOnFailureListener {
            errorLiveData.value = it.message
        }


    }
        override fun takeTime() {
            viewModelScope.launch(Dispatchers.IO) {
                while (true){
                if (time != -1) {
                    val ti = time.toString()
                    delay(1000)
                    if (time > 10){
                        timeLiveData.postValue(time.toString())
                    }else{
                        timeLiveData.postValue("0$time")
                    }
                    time--
                }
                }

            }
        }

        override fun verifyUser(user: UserModel, activity: Activity) {


//        myAuth.firebaseAuthSettings.forceRecaptchaFlowForTesting(true)
            val option = PhoneAuthOptions.newBuilder(myAuth)
                .setPhoneNumber(user.phoneNumber!!)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(option)
            takeTime()
        }

    }