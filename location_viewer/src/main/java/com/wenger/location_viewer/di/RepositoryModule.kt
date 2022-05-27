package com.wenger.location_viewer.di

import com.wenger.common.ILogSignUpRepository
import com.wenger.common.LogSignUpRepository
import com.wenger.location_viewer.checker.IMapsCheckerRepository
import com.wenger.location_viewer.checker.MapsCheckerRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val USERS = "FirebaseDBUser"
private const val LOCATION = "FirebaseDBLocation"

val repositoryModule = module {

    single<IMapsCheckerRepository> {
      MapsCheckerRepository(firebaseAuth = get(), databaseReference = get(named(LOCATION)))
    }

    single<ILogSignUpRepository> {
        LogSignUpRepository(firebaseAuth = get(), databaseReference = get(named(USERS)))
    }

}