package com.wenger.location_viewer.checker

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.wenger.common.data.UserLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class MapsCheckerRepository(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference
) : IMapsCheckerRepository {

    override suspend fun logOutUser(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                firebaseAuth.signOut()
            }
        }
    }

    override suspend fun getLocationList(): ArrayList<UserLocation> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { cont ->
                val userId = firebaseAuth.currentUser?.uid
                val valueEventListener: ValueEventListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = ArrayList<UserLocation>()
                        for (dataSnapshot in snapshot.children) {
                            val userLocation: UserLocation? =
                                dataSnapshot.getValue(UserLocation::class.java)
                            if (userLocation != null) {
                                list.add(userLocation)
                            }
                        }
                        cont.resume(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        cont.resumeWithException(error.toException())
                        Timber.e(error.toException())
                    }
                }
                databaseReference.child(userId!!).addValueEventListener(valueEventListener)
            }
        }
    }
}