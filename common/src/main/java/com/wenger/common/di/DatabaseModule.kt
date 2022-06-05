package com.wenger.common.di

import com.google.firebase.auth.FirebaseAuth
import com.wenger.common.provider.DatabaseProvider
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val DB_USERS = "FirebaseDBUser"
private const val DB_LOCATION = "FirebaseDBLocation"

val databaseModule = module {

    single {
        FirebaseAuth.getInstance()
    }

    single(named(name = DB_USERS)) {
        DatabaseProvider().provideUsersPath()
    }

    single(named(name = DB_LOCATION)) {
        DatabaseProvider().provideLocationsPath()
    }
}