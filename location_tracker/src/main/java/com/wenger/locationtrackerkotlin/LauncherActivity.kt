package com.wenger.locationtrackerkotlin


import android.content.Intent
import com.wenger.common.base.BaseLauncherActivity
import com.wenger.locationtrackerkotlin.login.LoginActivity

import com.wenger.locationtrackerkotlin.tracker.MapsTrackerActivity


class LauncherActivity : BaseLauncherActivity() {

    override fun startMapActivity() {
        Intent(this, MapsTrackerActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
    }

    override fun startLoginActivity() {
        Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
    }

}