package com.wenger.common.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.wenger.common.R
import com.wenger.common.databinding.ActivityLoginBinding
import com.wenger.common.signup.SignUpActivity
import com.wenger.common.util.ViewState
import com.wenger.common.util.collectWhenStarted
import com.wenger.common.util.listenTextChange
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseLoginActivity : AppCompatActivity() {

    private val viewModel by viewModel<BaseLoginViewModel>()
    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        supportActionBar?.hide()
        observeViewModel()
        setUpView()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setUpView() {
        binding?.apply {
            signInButton.setOnClickListener {
                viewModel.loginUser()
            }
            signUp.setOnClickListener {
                startActivity(Intent(this@BaseLoginActivity, SignUpActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            typeEmail.listenTextChange().collectWhenStarted(lifecycleScope) {
                viewModel.onEmailEntered(it)
            }

            typePassword.listenTextChange().collectWhenStarted(lifecycleScope) {
                viewModel.onPasswordEntered(it)
            }
        }
    }

    private fun observeViewModel() {
        binding?.apply {
            viewModel.validState.collectWhenStarted(lifecycleScope) {
                signInButton.isEnabled = it
            }
            viewModel.loginStatus.collectWhenStarted(lifecycleScope) {
                when (it) {
                    is ViewState.Success -> {
                        nextNavigation()
                    }
                    is ViewState.Error -> {
                        signInProgressBar.visibility = View.GONE
                        Toast.makeText(this@BaseLoginActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is ViewState.Loading -> {
                        signInProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }


    abstract fun nextNavigation()

}