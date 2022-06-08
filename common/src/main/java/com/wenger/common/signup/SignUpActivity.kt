package com.wenger.common.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.wenger.common.R
import com.wenger.common.databinding.ActivitySignUpBinding
import com.wenger.common.util.ViewState
import com.wenger.common.util.collectWhenStarted
import com.wenger.common.util.listenTextChange
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : AppCompatActivity() {

    private val viewModel by viewModel<SignUpViewModel>()
    private var binding: ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        supportActionBar?.hide()
        setUpView()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setUpView() {
        subscribeToListeners()
        subscribeToObservers()
    }

    private fun subscribeToListeners() {
        binding?.apply {
            signUpButton.setOnClickListener {
                viewModel.createNewUser()
            }
            typeNewEmail.listenTextChange().collectWhenStarted(lifecycleScope) {
                viewModel.onEmailEntered(it)
            }
            typeNewPassword.listenTextChange().collectWhenStarted(lifecycleScope) {
                viewModel.onPasswordEntered(it)
            }
            typeNewUsername.listenTextChange().collectWhenStarted(lifecycleScope) {
                viewModel.onUserNameEntered(it)
            }
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun subscribeToObservers() {
        binding?.apply {
            viewModel.validState.collectWhenStarted(lifecycleScope) {
                signUpButton.isEnabled = it
            }
            viewModel.userRegistrationStatus.collectWhenStarted(lifecycleScope) {
                when (it) {
                    is ViewState.Success -> {
                        signUpProgressBar.visibility = View.GONE
                        Toast.makeText(this@SignUpActivity, R.string.success_registration,
                            Toast.LENGTH_SHORT)
                            .show()
                        finish()
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    }
                    is ViewState.Error -> {
                        signUpProgressBar.visibility = View.GONE
                        Toast.makeText(this@SignUpActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is ViewState.Loading -> {
                        signUpProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}