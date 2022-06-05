package com.wenger.common.di

import com.wenger.common.base.BaseLauncherViewModel
import com.wenger.common.login.BaseLoginViewModel
import com.wenger.common.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {

    viewModel {
        BaseLoginViewModel(repository = get())
    }

    viewModel {
        SignUpViewModel(repository = get())
    }

    viewModel {
        BaseLauncherViewModel(repository = get())
    }

}