package com.wenger.common

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.wenger.common.models.UserCredentials
import com.wenger.common.util.Resource
import com.wenger.common.util.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LogSignUpRepository(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference
) : ILogSignUpRepository {

    override suspend fun signUpNewUser(
        email: String,
        password: String,
        userName: String
    ): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid!!
                val newUser = UserCredentials(email, password, userName)
                databaseReference.child(userId).setValue(newUser).await()
                Resource.Success(result)
            }
        }
    }

    override suspend fun signIn(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result)
            }
        }
    }
}