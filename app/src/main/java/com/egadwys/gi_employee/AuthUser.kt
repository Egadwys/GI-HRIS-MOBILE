package com.egadwys.gi_employee

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthUser : AppCompatActivity() {
    private lateinit var button: MaterialButton
    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var glide: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var loading_auth: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth_user)

        loading_auth = findViewById(R.id.loading_auth)
        loading_auth.visibility = View.GONE

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val cekuser = sharedPreferences.getString("user", "NoUser")
        val ceknama = sharedPreferences.getString("nama", "NoNama")
        if ( cekuser == "NoUser") {
            Toast.makeText(this, "Silahkan login terlebih dahulu",Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this@AuthUser, MainActivity::class.java).apply {
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
        val vibrator = ContextCompat.getSystemService(this,Vibrator::class.java) as Vibrator

        val fadeInAnimationin = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Vibrate for 100 milliseconds
                val vibrationEffect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            } else {
                // Deprecated in API 26
                vibrator.vibrate(50)
            }
            if (username.text.isNullOrBlank() || password.text.isNullOrBlank()) {
                Toast.makeText(this@AuthUser, "please fill all field", Toast.LENGTH_LONG).show()
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

    private fun getdata(user: String, pass: String) {
        val fadeInAnimationout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        LoginRetrofitClient.instance.cekuser(user,pass).enqueue(object : Callback<List<LoginDataClass>> {
            override fun onResponse(call: Call<List<LoginDataClass>>, response: Response<List<LoginDataClass>>) {
                if (response.isSuccessful) {
                    loading_auth.startAnimation(fadeInAnimationout)
                    loading_auth.visibility = View.GONE
                    response.body()?.let { loginDataList ->
                        if (loginDataList.isNotEmpty()) {
                            val loginData = loginDataList[0]
                            sharedPreferences.edit().putString("user", loginData.username).apply()
                            sharedPreferences.edit().putString("nama", loginData.name).apply()
                            val intent = Intent(this@AuthUser, MainActivity::class.java).apply {
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
                    Toast.makeText(this@AuthUser, "User tidak ditemukan, periksa username dan password", Toast.LENGTH_LONG).show()
//                    Toast.makeText(this@AuthUser, "Failed to get data: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<LoginDataClass>>, t: Throwable) {
                loading_auth.startAnimation(fadeInAnimationout)
                loading_auth.visibility = View.GONE
                Toast.makeText(this@AuthUser, "Autentikasi gagal!, tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
//                Toast.makeText(this@AuthUser, "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e("Request Failed E", t.message.toString())
                Log.d("Request Failed D", t.message.toString())
            }
        })
    }
}