package com.wenger.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.wenger.common.util.collectWhenStarted
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseLauncherActivity : AppCompatActivity() {

    protected val viewModel by viewModel<BaseLauncherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        viewModel.checkAuthState()
        subscribeOnObservers()
    }

    private fun subscribeOnObservers() {
        viewModel.userExist.collectWhenStarted(lifecycleScope) {
            when (it) {
                true -> {
                    startMapActivity()
                }
                false -> {
                    startLoginActivity()
                }
            }
        }
    }

    abstract fun startMapActivity()

    abstract fun startLoginActivity()

}