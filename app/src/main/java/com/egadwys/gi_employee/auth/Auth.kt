package com.egadwys.gi_employee.auth

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.egadwys.gi_employee.R
import com.egadwys.gi_employee.dashboard.Dashboard
import com.egadwys.gi_employee.scanner.Scanner
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Auth : AppCompatActivity() {
    private lateinit var button: MaterialButton
    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var glide: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var loading_auth: LinearLayout
    private lateinit var scan: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth2)

        scan = findViewById(R.id.testscanner)
        scan.setOnClickListener {
            vibrate()
            val intent = Intent(this@Auth, Scanner::class.java)
            startActivity(intent)
        }

        val receivedString = intent.getStringExtra("barcode")
        if (receivedString != null) {
            Toast.makeText(this, receivedString,Toast.LENGTH_SHORT).show()
        }

        loading_auth = findViewById(R.id.loading_auth)
        loading_auth.visibility = View.GONE

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val cekuser = sharedPreferences.getString("user", "NoUser")
        val ceknama = sharedPreferences.getString("nama", "NoNama")
        if ( cekuser == "NoUser") {
        } else {
            val intent = Intent(this@Auth, Dashboard::class.java).apply {
                putExtra("username", cekuser)
                putExtra("name", ceknama)
            }
            startActivity(intent)
        }
//        glide = findViewById(R.id.animationView)
//        Glide.with(this).load(R.drawable.anim_login).into(glide)
        button = findViewById(R.id.btn_login)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        val fadeInAnimationin = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        button.setOnClickListener {
            vibrate()
            if (username.text.isNullOrBlank() || password.text.isNullOrBlank()) {
                Toast.makeText(this@Auth, "please fill all field", Toast.LENGTH_LONG).show()
            } else {
                loading_auth.visibility = View.VISIBLE
                loading_auth.startAnimation(fadeInAnimationin)
                var user = username.text
                var pass = password.text
//                Toast.makeText(this@AuthUser, "ready get data", Toast.LENGTH_LONG).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    getdata(user.toString(), pass.toString())
                }, 1000)
            }
        }

    }

    private fun vibrate() {
        val vibrator = ContextCompat.getSystemService(this,Vibrator::class.java) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Vibrate for 100 milliseconds
            val vibrationEffect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            // Deprecated in API 26
            vibrator.vibrate(50)
        }
    }

    private fun getdata(user: String, pass: String) {
        val fadeInAnimationout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        RetrofitClient_auth.instance.cekuser(user,pass).enqueue(object : Callback<List<DataClass_auth>> {
            override fun onResponse(call: Call<List<DataClass_auth>>, response: Response<List<DataClass_auth>>) {
                vibratex()
                if (response.isSuccessful) {
                    response.body()?.let { loginDataList ->
                        if (loginDataList.isNotEmpty()) {
                            val loginData = loginDataList[0]
                            createNotificationChannel(this@Auth, loginData.name)
                            sendNotification(this@Auth, loginData.name)
                            sharedPreferences.edit().putString("user", loginData.username).apply()
                            sharedPreferences.edit().putString("nama", loginData.name).apply()
                            val intent = Intent(this@Auth, Dashboard::class.java).apply {
                                putExtra("username", loginData.username)
                                putExtra("name", loginData.name)
                            }
                            startActivity(intent)
                        }
                    }
                    Log.d("OK: ", "${response.body()}")
                } else {
                    loading_auth.startAnimation(fadeInAnimationout)
                    loading_auth.visibility = View.GONE
                    Toast.makeText(this@Auth, "User tidak ditemukan, periksa username dan password", Toast.LENGTH_LONG).show()
//                    Toast.makeText(this@AuthUser, "Failed to get data: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<DataClass_auth>>, t: Throwable) {
                vibratex()
                loading_auth.startAnimation(fadeInAnimationout)
                loading_auth.visibility = View.GONE
                Toast.makeText(this@Auth, "Autentikasi gagal!, tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
//                Toast.makeText(this@AuthUser, "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e("Request Failed E", t.message.toString())
                Log.d("Request Failed D", t.message.toString())
            }
        })
    }

    private fun vibratex() {
        val vibrator = ContextCompat.getSystemService(this, Vibrator::class.java) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Vibrate for 100 milliseconds
            val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            // Deprecated in API 26
            vibrator.vibrate(500)
        }
    }

    fun createNotificationChannel(context: Context, name: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Login success"
            val descriptionText = "Hello $name"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(context: Context, name: String) {
        val builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Login success")
            .setContentText("Hello $name")
            .setPriority(NotificationCompat.PRIORITY_MAX)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    this@Auth,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
    }
}