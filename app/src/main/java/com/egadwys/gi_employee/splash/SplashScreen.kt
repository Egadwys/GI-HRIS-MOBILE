package com.egadwys.gi_employee.splash

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.core.view.GravityCompat
import com.egadwys.gi_employee.databinding.SplashScreenBinding
import com.egadwys.gi_employee.R
import com.egadwys.gi_employee.dashboard.Dashboard

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        enableEdgeToEdge()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (shouldHandleBackPress()) {
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val cekuser = sharedPreferences.getString("user", "NoUser")
        val ceknama = sharedPreferences.getString("nama", "NoNama")
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreen, Dashboard::class.java).apply {
                putExtra("username", cekuser)
                putExtra("name", ceknama)
            }
            finish()
            startActivity(intent)
        }, 3000)
    }

    private fun shouldHandleBackPress(): Boolean {
        return true
    }
}