package com.wenger.common

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.wenger.common.models.UserCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference
) : IAuthRepository {

    override suspend fun signUpNewUser(
        email: String,
        password: String,
        userName: String
    ): Result<Void> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid!!
                val newUser = UserCredentials(email, password, userName)
                databaseReference.child(userId).setValue(newUser).await()
            }
        }
    }

    override suspend fun signIn(email: String, password: String): Result<AuthResult> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
            }
        }
    }

    override suspend fun checkAuth(): Result<FirebaseUser?> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                firebaseAuth.currentUser
                }
            }
        }
}
