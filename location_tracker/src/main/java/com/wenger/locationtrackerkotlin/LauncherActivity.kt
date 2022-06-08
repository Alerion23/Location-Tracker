package com.wenger.locationtrackerkotlin


import android.content.Intent
import com.wenger.common.base.BaseLauncherActivity
import com.wenger.locationtrackerkotlin.login.LoginActivity

import com.wenger.locationtrackerkotlin.tracker.MapsTrackerActivity


class LauncherActivity : BaseLauncherActivity() {

    override fun startMapActivity() {
        val intent = Intent(this, MapsTrackerActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    override fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

}