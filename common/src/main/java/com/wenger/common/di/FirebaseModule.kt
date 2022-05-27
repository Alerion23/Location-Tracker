package com.wenger.common.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val DB_URL =
    "https://location-tracker-kotlin-4398a-default-rtdb.europe-west1.firebasedatabase.app"
private const val DEFAULT_PATH_LOCATION = "Locations"
private const val DEFAULT_PATH_USERS = "Users"
private const val USERS = "Users"

val firebaseModule = module {

    single {
        FirebaseAuth.getInstance()
    }

    single(named(name = "FirebaseDBUser")) {
        FirebaseDatabase.getInstance(DB_URL).getReference(DEFAULT_PATH_USERS)
    }

    single(named(name = "FirebaseDBLocation")) {
        FirebaseDatabase.getInstance(DB_URL).getReference(DEFAULT_PATH_LOCATION)
            .child(USERS)
    }
}