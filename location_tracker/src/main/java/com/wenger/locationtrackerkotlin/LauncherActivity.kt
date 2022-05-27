package com.wenger.locationtrackerkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import com.wenger.common.login.LoginView

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

import com.wenger.locationtrackerkotlin.tracker.MapsTrackerView


class LauncherActivity : AppCompatActivity() {

    private val firebaseAuth: FirebaseAuth by inject()

    private val authStateListener = AuthStateListener { firebaseAuth: FirebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this@LauncherActivity, MapsTrackerView::class.java))
            overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
        } else {
            startActivity(Intent(this, LoginView::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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