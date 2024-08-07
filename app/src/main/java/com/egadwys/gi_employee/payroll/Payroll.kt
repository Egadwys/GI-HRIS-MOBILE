package com.egadwys.gi_employee.payroll

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
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.egadwys.gi_employee.R
import com.egadwys.gi_employee.apiContainer.RetrofitClient
import com.egadwys.gi_employee.payroll.detail.Detail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Payroll : AppCompatActivity(), DataAdapter_payroll.OnItemClickListener {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var loading: LinearLayout
    private lateinit var loadtext: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payroll)

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        loading = findViewById(R.id.loading)
        loadtext = findViewById(R.id.loadtext)
        mRecyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        val nik = sharedPreferences.getString("user", "NoUser")
        Log.d("Filter", "NIK: ${nik}")
        swipeRefreshLayout.setDistanceToTriggerSync(800)
        swipeRefreshLayout.setOnRefreshListener {
            loaddata(nik.toString())
        }
        loaddata(nik.toString())
    }

    private fun vibrate() {
        val vibrator = ContextCompat.getSystemService(this, Vibrator::class.java) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(50)
        }
    }

    private fun loaddata(nik: String) {
        loading.visibility = View.VISIBLE
        mRecyclerView.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = true

        RetrofitClient.instance.GetPayroll(nik).enqueue(object : Callback<List<DataClass_payroll>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<DataClass_payroll>>, response: Response<List<DataClass_payroll>>) {
                Log.d("Error: ", response.message())
                if (response.isSuccessful) {
                    Log.d("Sukses: ", "${response.body()}")
                    val data = response.body()
                    if (!data.isNullOrEmpty()) {
                        runOnUiThread {
                            mRecyclerView.layoutManager = GridLayoutManager(this@Payroll, 3)
                            val adapter = DataAdapter_payroll(data, this@Payroll)
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
                        Log.d("Null", "onResponse: ${response.body()}")
                    }
                    swipeRefreshLayout.isRefreshing = false
                } else {
                    mRecyclerView.visibility = View.GONE
                    loading.visibility = View.VISIBLE
                    if (response.message().toString() == "Not Found"){
                        loadtext.text = "Tidak ada data Payroll"
                    } else {
                        loadtext.text = "Failed to get data: ${response.message()}"
                    }
                    swipeRefreshLayout.isRefreshing = false
                    Log.d("Gagal: ", response.message())
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<List<DataClass_payroll>>, t: Throwable) {
                Log.e("Request Failed", t.message.toString())
                loadtext.text = "Failed to get data: ${t.message.toString()}"
                Toast.makeText(this@Payroll, "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    override fun onItemClick(data: DataClass_payroll) {
        vibrate()
        val intent = Intent(this, Detail::class.java).apply {
            putExtra("id", data.id)
        }
        startActivity(intent)
    }
}
