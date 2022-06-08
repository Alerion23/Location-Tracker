package com.wenger.locationtrackerkotlin.login

import android.content.Intent
import com.wenger.common.base.BaseLoginActivity
import com.wenger.locationtrackerkotlin.tracker.MapsTrackerActivity

class LoginActivity: BaseLoginActivity() {

    override fun nextNavigation() {
        val intent = Intent(this, MapsTrackerActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}