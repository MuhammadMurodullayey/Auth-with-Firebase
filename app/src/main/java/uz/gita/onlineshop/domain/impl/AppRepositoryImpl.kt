package uz.gita.onlineshop.domain.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import uz.gita.onlineshop.data.UserModel
import uz.gita.onlineshop.domain.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val firebase: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : AppRepository {
    private val db = fireStore.collection("users")

    override fun isUserHave(number: String): Flow<Result<UserModel>> =  callbackFlow <Result<UserModel>> {
        db.whereEqualTo("phoneNumber", number).get()
            .addOnSuccessListener {
                if(it.size() > 0) {
                    trySend(Result.success(it.first().toObject(UserModel::class.java)))
                }else{
                    trySend(Result.success(UserModel()))
                }
            }
            .addOnFailureListener {
                Log.d("TTT",it.message.toString())
                trySend(Result.failure(it))
            }
        awaitClose {  }
    }.flowOn(Dispatchers.IO)

    override fun getUsers() = callbackFlow<Result<List<UserModel>>> {
        db.get()
            .addOnSuccessListener {
                val list = it.toObjects(UserModel::class.java)

                trySend(Result.success(list))
            }
            .addOnFailureListener {
                trySend(Result.failure(Throwable("Fail get user")))
            }
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun addUser(user: UserModel) = callbackFlow<Result<Unit>> {
        db.document(user.phoneNumber!!)
            .set(
                hashMapOf(
                    "name" to user.name,
                    "phoneNumber" to user.phoneNumber,
                    "password" to user.password
                )
            )
            .addOnSuccessListener {
                trySend(Result.success(Unit))
            }
            .addOnFailureListener {
                trySend(Result.failure(it))
            }

        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun registerUser(email: String, password: String) = callbackFlow<Result<Boolean>> {

        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun loginUser(email: String, password: String) = callbackFlow<Result<Boolean>> {

        awaitClose { }
    }.flowOn(Dispatchers.IO)


}