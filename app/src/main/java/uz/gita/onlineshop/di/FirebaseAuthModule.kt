package uz.gita.onlineshop.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseAuthModule {

    @[Singleton Provides]
    fun getFireBaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()
    @[Singleton Provides]
    fun getFireBaseFireBaseStore() : FirebaseFirestore = FirebaseFirestore.getInstance()
}