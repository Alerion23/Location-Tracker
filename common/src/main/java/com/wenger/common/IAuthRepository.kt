package com.wenger.common

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface IAuthRepository {

    suspend fun signUpNewUser(email: String, password: String, userName: String) : Result<Void>

    suspend fun signIn(email: String, password: String): Result<AuthResult>

    suspend fun checkAuth() : Result<FirebaseUser?>
}