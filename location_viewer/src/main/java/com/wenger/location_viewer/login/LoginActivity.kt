package com.wenger.location_viewer.login

import android.content.Intent
import com.wenger.common.base.BaseLoginActivity
import com.wenger.location_viewer.checker.MapsCheckerActivity

class LoginActivity: BaseLoginActivity() {

    override fun nextNavigation() {
        val intent = Intent(this, MapsCheckerActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
    }
}