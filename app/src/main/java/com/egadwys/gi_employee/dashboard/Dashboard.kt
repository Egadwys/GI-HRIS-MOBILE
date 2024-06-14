package com.egadwys.gi_employee.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.egadwys.gi_employee.R
import com.egadwys.gi_employee.dashboard.viewpager.ViewPagerAdapter
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class Dashboard : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var menu:ChipNavigationBar
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dashboard_header: TextView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        dashboard_header = findViewById(R.id.dashboard_header)

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val nama = sharedPreferences.getString("nama", "NoNama")
        val nik = sharedPreferences.getString("user", "NoUser")

        dashboard_header.text = nama

        Toast.makeText(this, nama, Toast.LENGTH_SHORT).show()
        Toast.makeText(this, nik, Toast.LENGTH_SHORT).show()

        viewPager = findViewById(R.id.viewPager)
        menu = findViewById(R.id.chip)
        adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        menu.setItemSelected(R.id.menu_dashboard, true)

        menu.setOnItemSelectedListener { id ->
            when (id) {
                R.id.menu_dashboard -> {
                    vibrate()
                    viewPager.currentItem = 0
                }
                R.id.menu_attendance -> {
                    vibrate()
                    viewPager.currentItem = 1
                }
                R.id.menu_payroll -> {
                    vibrate()
                    viewPager.currentItem = 2
                }
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Handle page selection
                when (position) {
                    0 -> {
                        menu.setItemSelected(R.id.menu_dashboard, true)
                    }
                    1 -> {
                        menu.setItemSelected(R.id.menu_attendance, true)
                    }
                    2 -> {
                        menu.setItemSelected(R.id.menu_payroll, true)
                    }
                }
            }
        })

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                // For example, show a dialog or finish the activity
                if (shouldHandleBackPress()) {
                    // Custom behavior
                } else {
                    // Default behavior
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun shouldHandleBackPress(): Boolean {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("DUMMY")
        builder.setPositiveButton("Logout") { dialog, which ->
            sharedPreferences.edit().putString("user", "NoUser").apply()
            sharedPreferences.edit().putString("nama", "NoNama").apply()
            finish()
            Toast.makeText(this, "You're logging off", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Exit app") { dialog, which ->
            finishAffinity()
        }
        val dialog = builder.create()
        dialog.show()
        return true
    }

    private fun vibrate() {
        val vibrator = ContextCompat.getSystemService(this, Vibrator::class.java) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Vibrate for 100 milliseconds
            val vibrationEffect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            // Deprecated in API 26
            vibrator.vibrate(50)
        }
    }
}