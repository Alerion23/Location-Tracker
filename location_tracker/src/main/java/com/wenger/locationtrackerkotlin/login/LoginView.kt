package com.wenger.locationtrackerkotlin.login

import android.content.Intent
import com.wenger.common.login.BaseLoginView
import com.wenger.locationtrackerkotlin.tracker.MapsTrackerView

class LoginView: BaseLoginView() {

    override fun nextNavigation() {
        val intent = Intent(this, MapsTrackerView::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}