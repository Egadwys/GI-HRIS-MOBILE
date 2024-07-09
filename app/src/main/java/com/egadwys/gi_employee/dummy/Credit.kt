package com.egadwys.gi_employee.dummy

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.egadwys.gi_employee.R
import com.google.android.gms.common.util.concurrent.HandlerExecutor

class Credit : AppCompatActivity() {
    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.credit)

        linearLayout = findViewById(R.id.linearcredit)
        linearLayout.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed({
            linearLayout.visibility = View.VISIBLE
            linearLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
        }, 800)

    }
}