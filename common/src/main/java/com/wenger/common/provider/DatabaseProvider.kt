package com.wenger.common.provider

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private const val DB_URL =
    "https://location-tracker-kotlin-4398a-default-rtdb.europe-west1.firebasedatabase.app"
private const val DEFAULT_PATH_LOCATION = "Locations"
private const val DEFAULT_PATH_USERS = "Users"
private const val USERS = "Users"

class DatabaseProvider() : IDatabaseProvider {

    override fun provideUsersPath(): DatabaseReference {
        return FirebaseDatabase.getInstance(DB_URL).getReference(DEFAULT_PATH_USERS)
    }

    override fun provideLocationsPath(): DatabaseReference {
        return FirebaseDatabase.getInstance(DB_URL).getReference(DEFAULT_PATH_LOCATION).child(USERS)
    }

}