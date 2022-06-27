package com.wenger.locationtrackerkotlin.di

import com.wenger.locationtrackerkotlin.tracker.MapsTrackerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        MapsTrackerViewModel(repository = get())
    }

}