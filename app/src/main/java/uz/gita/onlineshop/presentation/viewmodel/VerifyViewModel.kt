package uz.gita.onlineshop.presentation.viewmodel

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.google.firebase.auth.AuthCredential
import uz.gita.onlineshop.data.UserModel

interface VerifyViewModel {
   val errorLiveData : LiveData<String>
   val progressLiveData : LiveData<Boolean>
   val timeLiveData : LiveData<String>
   val goToNextScreenLiveData : LiveData<Unit>

    fun verifyUser(user : UserModel, activity : Activity)
    fun takeTime()
    fun checkUser(user: UserModel, activity : Activity,code : String)
    fun addUser(user : UserModel)
}