package com.wenger.location_viewer

import android.content.Intent
import android.os.Bundle
import com.wenger.common.base.BaseLauncherActivity
import com.wenger.location_viewer.checker.MapsCheckerActivity
import com.wenger.location_viewer.login.LoginActivity


class LauncherActivity : BaseLauncherActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }

    override fun startMapActivity() {
        Intent(this, MapsCheckerActivity::class.java).apply {
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