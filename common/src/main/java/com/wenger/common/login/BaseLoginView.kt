package com.wenger.common.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.wenger.common.R
import com.wenger.common.databinding.ActivityLoginBinding
import com.wenger.common.signup.SignUpView
import com.wenger.common.util.ITextChangeWatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseLoginView : AppCompatActivity() {

    private val viewModel by viewModel<BaseLoginViewModel>()
    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater).also {
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
            signInButton.setOnClickListener {
                signInProgressBar.visibility = View.VISIBLE
                viewModel.loginUser()
            }
            signUp.setOnClickListener {
                startActivity(Intent(this@BaseLoginView, SignUpView::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            typeEmail.addTextChangedListener(object : ITextChangeWatcher {

                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        viewModel.onEmailEntered(s.toString().trim())
                    }
                }
            })
            typePassword.addTextChangedListener(object : ITextChangeWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        viewModel.onPasswordEntered(s.toString().trim())
                    }
                }
            })
        }
    }

    private fun subscribeToObservers() {
        binding?.apply {
            lifecycleScope.launchWhenStarted {
                launch {
                    viewModel.validState.collectLatest {
                        signInButton.isEnabled = it
                    }
                }
                launch {
                    viewModel.loginStatus.collectLatest {
                        it
                            .onSuccess {
                                nextNavigation()
                            }
                            .onFailure { error ->
                                signInProgressBar.visibility = View.GONE
                                Toast.makeText(this@BaseLoginView, error.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }
            }
        }
    }

    abstract fun nextNavigation()

}