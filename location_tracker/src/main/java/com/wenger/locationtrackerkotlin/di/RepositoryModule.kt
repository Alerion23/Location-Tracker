package com.wenger.locationtrackerkotlin.di

import com.wenger.common.ILogSignUpRepository
import com.wenger.common.LogSignUpRepository
import com.wenger.locationtrackerkotlin.tracker.IMapsTrackerRepository
import com.wenger.locationtrackerkotlin.tracker.MapsTrackerRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val USERS = "FirebaseDBUser"
private const val LOCATION = "FirebaseDBLocation"

val repositoryModule = module {

    single<ILogSignUpRepository> {
        LogSignUpRepository(firebaseAuth = get(), databaseReference = get(named(USERS)))
    }

    single<IMapsTrackerRepository> {
        MapsTrackerRepository(firebaseAuth = get(), databaseReference = get(named(LOCATION)))
    }

}