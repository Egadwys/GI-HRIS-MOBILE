package com.egadwys.gi_employee.attendance

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.egadwys.gi_employee.R
import com.egadwys.gi_employee.payroll.Payroll
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Attendance : AppCompatActivity(), DataAdapter_attendance.OnItemClickListener {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var loading: LinearLayout
    private lateinit var loadtext: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var title: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabOption1: FloatingActionButton
    private lateinit var fabOption2: FloatingActionButton
    private lateinit var fabOption3: FloatingActionButton
    private var isFabOpen = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.attendance)

        fabMain = findViewById(R.id.fab_main)
        fabOption1 = findViewById(R.id.fab_option1)
        fabOption2 = findViewById(R.id.fab_option2)
        fabOption3 = findViewById(R.id.fab_option3)
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        loading = findViewById(R.id.loading)
        loadtext = findViewById(R.id.loadtext)
        mRecyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        title = findViewById(R.id.main_title_header)

        title.text = sharedPreferences.getString("nama", "NoNama")
        val nik = sharedPreferences.getString("user", "NoUser")
        Log.d("Filter", "NIK: ${nik}")
        swipeRefreshLayout.setOnRefreshListener {
            loaddata(nik.toString())
        }
        loaddata(nik.toString())

        fabMain.setOnClickListener {
            vibrate()
            if (isFabOpen) {
                closeFABMenu()
            } else {
                showFABMenu()
            }
        }

        fabOption1.setOnClickListener {
            vibrate()
            closeFABMenu()
            sharedPreferences.edit().putString("user", "NoUser").apply()
            sharedPreferences.edit().putString("nama", "NoNama").apply()
            finish()
            Toast.makeText(this, "You're logging off", Toast.LENGTH_SHORT).show()
        }

        fabOption2.setOnClickListener {
            vibrate()
            closeFABMenu()
            finishAffinity()
        }

        fabOption3.setOnClickListener {
            vibrate()
            closeFABMenu()
            val intent = Intent(this@Attendance, Payroll::class.java).apply {
                putExtra("username", nik)
                putExtra("name", title.text)
            }
            startActivity(intent)
        }

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

    private fun showFABMenu() {
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeInAnimation_delay = AnimationUtils.loadAnimation(this, R.anim.fade_in_delay)
        val fadeInAnimation_delay2 = AnimationUtils.loadAnimation(this, R.anim.fade_in_delay2)
        isFabOpen = true
        fabOption1.visibility = View.VISIBLE
        fabOption2.visibility = View.VISIBLE
        fabOption3.visibility = View.VISIBLE
        fabOption1.startAnimation(fadeInAnimation)
        fabOption2.startAnimation(fadeInAnimation_delay)
        fabOption3.startAnimation(fadeInAnimation_delay2)
        rotateFab(fabMain, 0f, 1350f) // Change icon to close
    }

    private fun closeFABMenu() {        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        isFabOpen = false
        fabOption1.startAnimation(fadeInAnimation)
        fabOption2.startAnimation(fadeInAnimation)
        fabOption3.startAnimation(fadeInAnimation)
        fabOption1.visibility = View.GONE
        fabOption2.visibility = View.GONE
        fabOption3.visibility = View.GONE
        rotateFab(fabMain, 1350f, 0f) // Change icon back to add
    }

    private fun rotateFab(fab: FloatingActionButton, from: Float, to: Float) {
        val animator = ObjectAnimator.ofFloat(fab, "rotation", from, to)
        animator.duration = 500
        animator.start()
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("DUMMY")
        builder.setPositiveButton("Logout") { dialog, which ->
            sharedPreferences.edit().putString("user", "NoUser").apply()
            sharedPreferences.edit().putString("nama", "NoNama").apply()
            Toast.makeText(this, "You're logging off", Toast.LENGTH_SHORT).show()
            super.onBackPressed()
        }
        builder.setNegativeButton("Exit app") { dialog, which ->
            finishAffinity()
            super.onBackPressed()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun loaddata(nik: String) {
        loading.visibility = View.VISIBLE
        mRecyclerView.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = true

        RetrofitClient_attendance.instance.getData(nik).enqueue(object : Callback<List<DataClass_attendance>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<DataClass_attendance>>, response: Response<List<DataClass_attendance>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.isNotEmpty()) {
                        runOnUiThread {
                            mRecyclerView.layoutManager = GridLayoutManager(this@Attendance, 2)
                            val adapter = DataAdapter_attendance(data, this@Attendance) // Pass 'this' as itemClickListener
                            mRecyclerView.adapter = adapter

                            val searchView: SearchView = findViewById(R.id.searchView)
                            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    return false
                                }

                                override fun onQueryTextChange(newText: String?): Boolean {
                                    adapter.filter.filter(newText)
                                    return false
                                }
                            })
                        }
                        mRecyclerView.visibility = View.VISIBLE
                        loading.visibility = View.GONE
                    } else {
                        Toast.makeText(this@Attendance, "Response body is null or empty", Toast.LENGTH_LONG).show()
                    }
                    swipeRefreshLayout.isRefreshing = false
                } else {
                    mRecyclerView.visibility = View.GONE
                    loading.visibility = View.VISIBLE
                    loadtext.text = "Failed to get data: ${response.message()}"
                    Toast.makeText(this@Attendance, "Failed to get data: ${response.message()}", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout.isRefreshing = false
                    Log.d("Filter", "Publishing results for constraint: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<DataClass_attendance>>, t: Throwable) {
                Log.e("Request Failed", t.message.toString())
                loadtext.text = "Failed to get data: ${t.message.toString()}"
                Toast.makeText(this@Attendance, "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    override fun onItemClick(data: DataClass_attendance) {
        vibrate()
//        val dialogFragment = DialogFragment()
//        val bundle = Bundle()
//        bundle.putString("name", data.name)
//        bundle.putString("position", data.position)
//        bundle.putString("nik", data.NIK.toString())
//        bundle.putString("dept", data.dept)
//        bundle.putString("div", data.division)
//        bundle.putString("wg", data.Workgroup)
//        bundle.putString("birth", data.dateBirth)
//        bundle.putString("kec", data.kecamatan)
//        bundle.putString("kel", data.kelurahan)
//        bundle.putString("kota", data.kota)
//        bundle.putString("prov", data.provinsi)
//        bundle.putString("negara", data.negara)
//        bundle.putString("tin", data.masuk)
//        bundle.putString("tout", data.pulang)
//        dialogFragment.arguments = bundle
//        dialogFragment.show(supportFragmentManager, "CustomDialogFragmentTag")
    }
}