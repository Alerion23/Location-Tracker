package com.wenger.common.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.wenger.common.R
import com.wenger.common.databinding.ActivitySignUpBinding
import com.wenger.common.util.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpView : AppCompatActivity() {

    private val viewModel by viewModel<SignUpViewModel>()
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setUpView()
    }

    private fun setUpView() {
        onSignUpClockListener()
        onRegisterResult()
        setEmailError()
        setPasswordError()
        setUserNameError()
    }

    private fun onSignUpClockListener() {
        binding.signUpButton.setOnClickListener {
            val email = binding.typeNewEmail.text.toString().trim()
            val password = binding.typeNewPassword.text.toString().trim()
            val userName = binding.typeNewUsername.text.toString().trim()
            viewModel.createNewUser(email, password, userName)
        }
    }

    private fun onRegisterResult() {
        viewModel.userRegistrationStatus.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.signUpProgressBar.isVisible = true
                }
                is Resource.Success -> {
                    Toast.makeText(this, R.string.success_registration, Toast.LENGTH_SHORT)
                        .show()
                    finish()
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }
                is Resource.Error -> {
                    binding.signUpProgressBar.isVisible = false
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun setEmailError() {
        viewModel.errorEmail.observe(this) {
            binding.typeNewEmail.error = getString(it)
            binding.typeNewEmail.requestFocus()
        }
    }

    private fun setPasswordError() {
        viewModel.errorPassword.observe(this) {
            binding.typeNewPassword.error = getString(it)
            binding.typeNewPassword.requestFocus()
        }
    }

    private fun setUserNameError() {
        viewModel.errorUserName.observe(this) {
            binding.typeNewUsername.error = getString(it)
            binding.typeNewUsername.requestFocus()
        }
    }
}