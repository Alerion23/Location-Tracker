package com.wenger.locationtrackerkotlin.tracker

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.wenger.common.data.UserLocation
import com.wenger.common.util.Resource
import com.wenger.common.util.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
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
                    cont.resumeWith(Result.success(Unit))
                }
                .addOnFailureListener {
                    cont.resumeWithException(it)
                }
        }
    }

    override suspend fun logOut() {
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth.signOut()
            } catch (e: Exception) {
                Timber.e(e.localizedMessage)
            }
        }
    }
}