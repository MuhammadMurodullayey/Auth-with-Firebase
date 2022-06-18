package uz.gita.onlineshop.domain

import kotlinx.coroutines.flow.Flow
import uz.gita.onlineshop.data.UserModel

interface AppRepository {

    fun registerUser(email : String,password : String) : Flow<Result<Boolean>>
    fun loginUser(email : String,password : String): Flow<Result<Boolean>>
    fun getUsers() : Flow<Result<List<UserModel>>>
    fun addUser(user : UserModel)  : Flow<Result<Unit>>
    fun isUserHave(number: String)  : Flow<Result<UserModel>>

}