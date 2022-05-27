package com.wenger.common.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.wenger.common.R
import com.wenger.common.databinding.ActivityLoginBinding
import com.wenger.common.signup.SignUpView
import com.wenger.common.util.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginView : AppCompatActivity() {

    private val viewModel by viewModel<LoginViewModel>()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setUpView()
    }

    private fun setUpView() {
        onSignUpClickListener()
        onSignInClickListener()
        onLoginResult()
        setEmailError()
        setPasswordError()
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    private fun onSignInClickListener() {
        binding.signInButton.setOnClickListener {
            val email = binding.typeEmail.text.toString().trim()
            val password = binding.typePassword.text.toString().trim()
            viewModel.loginUser(email, password)
        }

    }

    private fun onSignUpClickListener() {
        binding.signUp.setOnClickListener {
            startActivity(Intent(this, SignUpView::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun onLoginResult() {
        viewModel.loginStatus.observe(this) {
            when(it) {
                is Resource.Loading -> {
                    binding.signInProgressBar.isVisible = true
                }
                is Resource.Success -> {
                    finish()
                }
                is Resource.Error -> {
                    binding.signInProgressBar.isVisible = false
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun setEmailError() {
        viewModel.errorEmail.observe(this) {
            binding.typeEmail.error = getString(it)
            binding.typeEmail.requestFocus()
        }
    }

    private fun setPasswordError() {
        viewModel.errorPassword.observe(this) {
            binding.typePassword.error = getString(it)
            binding.typePassword.requestFocus()
        }
    }
}