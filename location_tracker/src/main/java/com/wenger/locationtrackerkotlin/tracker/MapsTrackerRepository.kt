package com.wenger.locationtrackerkotlin.tracker

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.wenger.common.data.UserLocation
import com.wenger.common.util.BaseResult
import com.wenger.common.util.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MapsTrackerRepository(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference
) : IMapsTrackerRepository {


    override suspend fun addLocation(userLocation: UserLocation) {
        suspendCoroutine<Unit> { cont ->
            val map: Map<String, Any> = userLocation.toMap()
            val userId = firebaseAuth.currentUser?.uid
            databaseReference
                .child(userId!!)
                .push()
                .setValue(map)
                .addOnSuccessListener {
                    cont.resume(Unit)
                }
                .addOnFailureListener {
                    cont.resumeWithException(it)
                    Timber.e(it.message)
                }
        }
    }

    override suspend fun logOut(): BaseResult<Unit> {
        return withContext(Dispatchers.IO) {
            safeCall {
               val result = firebaseAuth.signOut()
                BaseResult.Success(result)
            }
        }
    }
}