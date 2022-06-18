package uz.gita.onlineshop.data

import java.io.Serializable

data class UserModel(
    var name :String? = "",
    var phoneNumber : String? ="",
    var password : String?=""
    ) : Serializable