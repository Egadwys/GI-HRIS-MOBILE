package com.egadwys.gi_employee.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.video.VideoRecordEvent.Start
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.egadwys.gi_employee.R
import com.egadwys.gi_employee.dashboard.viewpager.ViewPagerAdapter
import com.egadwys.gi_employee.payroll.detail.Detail
import com.google.android.material.navigation.NavigationView
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class Dashboard : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var menu:ChipNavigationBar
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dashboard_header: TextView
    private lateinit var dashboard_logout: ImageView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        var cnb = 0

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dashboard_header = findViewById(R.id.dashboard_header)
        dashboard_logout = findViewById(R.id.dashboard_logout)

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val nama = sharedPreferences.getString("nama", "NoNama")
        val nik = sharedPreferences.getString("user", "NoUser")

        dashboard_header.text = nama

        viewPager = findViewById(R.id.viewPager)
        menu = findViewById(R.id.chip)
        adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = true

        menu.setItemSelected(R.id.menu_dashboard, true)

        menu.setOnItemSelectedListener { id ->
            when (id) {
                R.id.menu_dashboard -> {
                    viewPager.currentItem = 0
                    cnb = 0
                }
                R.id.menu_attendance -> {
                    viewPager.currentItem = 1
                    cnb = 1
                }
                R.id.menu_profile -> {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: android.view.View, slideOffset: Float) {
                // Respond when the drawer's position changes
            }

            override fun onDrawerOpened(drawerView: android.view.View) {
                // Respond when the drawer is opened
            }

            override fun onDrawerClosed(drawerView: android.view.View) {
                if (cnb == 0) {
                    menu.setItemSelected(R.id.menu_dashboard, true)
                } else {
                    menu.setItemSelected(R.id.menu_attendance, true)
                }
            }

            override fun onDrawerStateChanged(newState: Int) {
                // Respond when the drawer motion state changes
            }
        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Handle page selection
                when (position) {
                    0 -> {
                        cnb = 0
                        menu.setItemSelected(R.id.menu_dashboard, true)
                    }
                    1 -> {
                        cnb = 1
                        menu.setItemSelected(R.id.menu_attendance, true)
                    }
                }
            }
        })

        dashboard_logout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("LOG OUT")
            builder.setPositiveButton("Logout") { dialog, which ->
                sharedPreferences.edit().putString("user", "NoUser").apply()
                sharedPreferences.edit().putString("nama", "NoNama").apply()
                finish()
                Toast.makeText(this, "You're logging off", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                return@setNegativeButton
            }
            val dialog = builder.create()
            dialog.show()
        }

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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers()
        } else {
            if (viewPager.currentItem == 0) {
                finishAffinity()
            } else {
                viewPager.currentItem = 0
            }
        }
        return true
    }
}
