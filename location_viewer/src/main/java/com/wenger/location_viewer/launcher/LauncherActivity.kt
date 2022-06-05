package com.wenger.location_viewer.launcher

import android.content.Intent
import android.os.Bundle
import com.wenger.common.base.BaseLauncher
import com.wenger.common.login.BaseLoginView
import com.wenger.location_viewer.R
import com.wenger.location_viewer.checker.MapsCheckerView
import com.wenger.location_viewer.login.LoginView


class LauncherActivity : BaseLauncher() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }

    override fun startMapActivity() {
        val intent = Intent(this, MapsCheckerView::class.java)
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