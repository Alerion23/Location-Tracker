package com.wenger.common.di

import com.wenger.common.login.LoginViewModel
import com.wenger.common.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val logSignUpModule = module {

    viewModel {
        LoginViewModel(repository = get())
    }

    viewModel {
        SignUpViewModel(repository = get())
    }

}