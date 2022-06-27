package com.wenger.common

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.wenger.common.util.BaseResult

interface IAuthRepository {

    suspend fun signUpNewUser(email: String, password: String, userName: String) : BaseResult<AuthResult>

    suspend fun signIn(email: String, password: String): BaseResult<AuthResult>

    suspend fun checkAuth() : BaseResult<FirebaseUser?>
}