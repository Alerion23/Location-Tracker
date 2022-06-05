package com.wenger.location_viewer.login

import android.content.Intent
import com.wenger.common.login.BaseLoginView
import com.wenger.location_viewer.checker.MapsCheckerView

class LoginView: BaseLoginView() {

    override fun nextNavigation() {
        val intent = Intent(this, MapsCheckerView::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}