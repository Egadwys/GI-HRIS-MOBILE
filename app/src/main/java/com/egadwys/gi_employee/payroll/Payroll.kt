package com.egadwys.gi_employee.payroll

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
import com.egadwys.gi_employee.attendance.Attendance
import com.egadwys.gi_employee.payroll.detail.Detail
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Payroll : AppCompatActivity(), DataAdapter_payroll.OnItemClickListener {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var loading: LinearLayout
    private lateinit var loadtext: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var title: TextView
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payroll)

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        loading = findViewById(R.id.loading)
        loadtext = findViewById(R.id.loadtext)
        mRecyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        title = findViewById(R.id.main_title_header)

        title.text = sharedPreferences.getString("nama", "NoNama")
        val nik = sharedPreferences.getString("user", "NoUser")
        Log.d("Filter", nik.toString())
//        Toast.makeText(this, nik, Toast.LENGTH_LONG).show()
        swipeRefreshLayout.setOnRefreshListener {
            loaddata(nik.toString())
        }
        loaddata(nik.toString())
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

    private fun loaddata(nik: String) {
        loading.visibility = View.VISIBLE
        mRecyclerView.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = true
        Log.d("NIK: ", nik)
        RetrofitClient_payroll.instance.getData(nik).enqueue(object : Callback<List<DataClass_payroll>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<DataClass_payroll>>, response: Response<List<DataClass_payroll>>) {
                Log.d("Error: ", response.message())
                if (response.isSuccessful) {
                    Log.d("Sukses: ", "${response.body()}")
                    val data = response.body()
                    if (data != null && data.isNotEmpty()) {
                        runOnUiThread {
                            mRecyclerView.layoutManager = GridLayoutManager(this@Payroll, 3)
                            val adapter = DataAdapter_payroll(data, this@Payroll) // Pass 'this' as itemClickListener
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
//                        Toast.makeText(this@Payroll, "Response body is null or empty", Toast.LENGTH_LONG).show()
                    }
                    swipeRefreshLayout.isRefreshing = false
                } else {
                    mRecyclerView.visibility = View.GONE
                    loading.visibility = View.VISIBLE
                    loadtext.text = "Failed to get data: ${response.message()}"
//                    Toast.makeText(this@Payroll, "Failed to get data: ${response.message()}", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout.isRefreshing = false
                    Log.d("Gagal: ", response.message())
                }
            }

            override fun onFailure(call: Call<List<DataClass_payroll>>, t: Throwable) {
                Log.e("Request Failed", t.message.toString())
                loadtext.text = "Failed to get data: ${t.message.toString()}"
//                Toast.makeText(this@Payroll, "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    override fun onItemClick(data: DataClass_payroll) {
        vibrate()
        val intent = Intent(this@Payroll, Detail::class.java).apply {
            putExtra("id", data.id)
        }
        startActivity(intent)
//        Toast.makeText(this, "ID: ${data.id}\nPeriode: ${data.periode}", Toast.LENGTH_SHORT).show()
    }
}