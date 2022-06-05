package com.wenger.common.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.wenger.common.R
import com.wenger.common.databinding.ActivitySignUpBinding
import com.wenger.common.util.ITextChangeWatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpView : AppCompatActivity() {

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
                signUpProgressBar.visibility = View.VISIBLE
                viewModel.createNewUser()
            }
            typeNewEmail.addTextChangedListener(object : ITextChangeWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        viewModel.onEmailEntered(s.toString().trim())
                    }
                }
            })
            typeNewPassword.addTextChangedListener(object : ITextChangeWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        viewModel.onPasswordEntered(s.toString().trim())
                    }
                }
            })
            typeNewUsername.addTextChangedListener(object : ITextChangeWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        viewModel.onUserNameEntered(s.toString().trim())
                    }
                }
            })
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun subscribeToObservers() {
        binding?.apply {
            lifecycleScope.launchWhenStarted {
                launch {
                    viewModel.validState.collectLatest {
                        signUpButton.isEnabled = it
                    }
                }
                launch {
                    viewModel.userRegistrationStatus.collectLatest {
                        it
                            .onSuccess {
                                signUpProgressBar.visibility = View.GONE
                                Toast.makeText(this@SignUpView, R.string.success_registration,
                                    Toast.LENGTH_SHORT)
                                    .show()
                                finish()
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                            }
                            .onFailure {
                                signUpProgressBar.visibility = View.GONE
                                Toast.makeText(this@SignUpView, it.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }
            }
        }
    }
}