package com.egadwys.gi_employee.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.egadwys.gi_employee.R
import com.egadwys.gi_employee.dashboard.viewpager.ViewPagerAdapter
import com.egadwys.gi_employee.profile.Profile
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class Dashboard : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var menu:ChipNavigationBar
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dashboard_header: TextView
    private lateinit var dashboard_subheader: TextView
    private lateinit var dashboard_logout: ImageView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var profil_picture: ImageView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        val versionName = PackageInfoCompat.getLongVersionCode(packageManager.getPackageInfo(packageName, 0))

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content_frame, Profile())
        fragmentTransaction.commit()

        var cnb = 0

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dashboard_header = findViewById(R.id.dashboard_header)
        dashboard_subheader = findViewById(R.id.dashboard_subheader)
        dashboard_logout = findViewById(R.id.dashboard_logout)
        profil_picture= findViewById(R.id.profile_picture)

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val nama = sharedPreferences.getString("nama", "NoNama")
        val nik = sharedPreferences.getString("user", "NoUser")

        val imageUrl = "http://192.168.10.242:8078/GI-HRIS-API/photo/${nik}.jpg"
        Glide.with(this)
            .load(imageUrl)
            .into(profil_picture)

        profil_picture.setOnClickListener {
            menu.setItemSelected(R.id.menu_profile, true)
        }

        dashboard_header.text = nama
        dashboard_subheader.text = nik

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
                    vibrate()
                }
                R.id.menu_attendance -> {
                    viewPager.currentItem = 1
                    cnb = 1
                    vibrate()
                }
                R.id.menu_profile -> {
                    drawerLayout.openDrawer(GravityCompat.START)
                    vibrate()
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
                        vibrate()
                    }
                    1 -> {
                        cnb = 1
                        menu.setItemSelected(R.id.menu_attendance, true)
                        vibrate()
                    }
                }
            }
        })

        dashboard_logout.setOnClickListener {
            vibrate()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("LOG OUT")
            builder.setPositiveButton("Logout") { dialog, which ->
                vibrate()
                sharedPreferences.edit().putString("user", "NoUser").apply()
                sharedPreferences.edit().putString("nama", "NoNama").apply()
                finish()
                Toast.makeText(this, "You're logging off", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                vibrate()
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

    private fun vibrate() {
        val vibrator = ContextCompat.getSystemService(this@Dashboard, Vibrator::class.java) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Vibrate for 100 milliseconds
            val vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            // Deprecated in API 26
            vibrator.vibrate(100)
        }
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
