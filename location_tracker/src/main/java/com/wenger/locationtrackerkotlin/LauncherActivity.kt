package com.wenger.locationtrackerkotlin


import android.content.Intent
import com.wenger.common.base.BaseLauncher
import com.wenger.locationtrackerkotlin.login.LoginView

import com.wenger.locationtrackerkotlin.tracker.MapsTrackerView


class LauncherActivity : BaseLauncher() {

    override fun startMapActivity() {
        val intent = Intent(this, MapsTrackerView::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun startLoginActivity() {
        val intent = Intent(this, LoginView::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}