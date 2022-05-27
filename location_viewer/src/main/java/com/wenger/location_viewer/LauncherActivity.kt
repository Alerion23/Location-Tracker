package com.wenger.location_viewer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.wenger.common.login.LoginView
import com.wenger.location_viewer.checker.MapsCheckerView
import org.koin.android.ext.android.inject


class LauncherActivity : AppCompatActivity() {

    private val firebaseAuth by inject<FirebaseAuth>()

    private val authStateListener = AuthStateListener { firebaseAuth: FirebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this, MapsCheckerView::class.java))
            overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
        } else {
            startActivity(Intent(this, LoginView::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}