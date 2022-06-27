package com.wenger.location_viewer.di

import com.wenger.location_viewer.checker.MapsCheckerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        MapsCheckerViewModel(repository = get())
    }

}