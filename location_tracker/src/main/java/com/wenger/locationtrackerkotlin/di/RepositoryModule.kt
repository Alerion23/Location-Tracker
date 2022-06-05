package com.wenger.locationtrackerkotlin.di

import com.wenger.common.IAuthRepository
import com.wenger.common.AuthRepository
import com.wenger.locationtrackerkotlin.tracker.IMapsTrackerRepository
import com.wenger.locationtrackerkotlin.tracker.MapsTrackerRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val USERS = "FirebaseDBUser"
private const val LOCATION = "FirebaseDBLocation"

val repositoryModule = module {

    factory<IAuthRepository> {
        AuthRepository(firebaseAuth = get(), databaseReference = get(named(USERS)))
    }

    factory<IMapsTrackerRepository> {
        MapsTrackerRepository(firebaseAuth = get(), databaseReference = get(named(LOCATION)))
    }

}