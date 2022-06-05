package com.wenger.location_viewer.di

import com.wenger.common.IAuthRepository
import com.wenger.common.AuthRepository
import com.wenger.location_viewer.checker.IMapsCheckerRepository
import com.wenger.location_viewer.checker.MapsCheckerRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val USERS = "FirebaseDBUser"
private const val LOCATION = "FirebaseDBLocation"

val repositoryModule = module {

    factory<IMapsCheckerRepository> {
      MapsCheckerRepository(firebaseAuth = get(), databaseReference = get(named(LOCATION)))
    }

    factory<IAuthRepository> {
        AuthRepository(firebaseAuth = get(), databaseReference = get(named(USERS)))
    }

}