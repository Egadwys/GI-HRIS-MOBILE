package com.egadwys.gi_employee.spl

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AnimationUtils
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
import com.egadwys.gi_employee.custom.CustomSwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Spl : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var loading: LinearLayout
    private lateinit var loadtext: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var scroll_up: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.spl)

        scroll_up = findViewById(R.id.scroll_up)
        scroll_up.visibility = View.GONE

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        loading = findViewById(R.id.loading)
        loadtext = findViewById(R.id.loadtext)
        mRecyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        scroll_up.setOnClickListener {
            mRecyclerView.scrollToPosition(0)
        }

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        // The RecyclerView is not scrolling
                        val fadeInAnimation = AnimationUtils.loadAnimation(this@Spl, R.anim.fade_in)
                        scroll_up.startAnimation(fadeInAnimation)
                        scroll_up.visibility = View.VISIBLE
                    }

                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        // The RecyclerView is currently being dragged by outside input such as user touch input
                        val fadeInAnimation = AnimationUtils.loadAnimation(this@Spl, R.anim.fade_out)
                        scroll_up.startAnimation(fadeInAnimation)
                        scroll_up.visibility = View.GONE
                    }

                    RecyclerView.SCROLL_STATE_SETTLING -> {
                        // The RecyclerView is currently animating to a final position while not under outside control
                    }
                }
            }
        })

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

        RetrofitClient_spl.instance.getData(nik).enqueue(object : Callback<List<DataClass_spl>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<DataClass_spl>>, response: Response<List<DataClass_spl>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (!data.isNullOrEmpty()) {
                        runOnUiThread {
                            mRecyclerView.layoutManager = GridLayoutManager(this@Spl, 1)
                            val adapter = DataAdapter_spl(data, this@Spl)
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
                        Toast.makeText(this@Spl, "Response body is null or empty", Toast.LENGTH_LONG).show()
                    }
                    swipeRefreshLayout.isRefreshing = false
                } else {
                    mRecyclerView.visibility = View.GONE
                    loading.visibility = View.VISIBLE
                    loadtext.text = "Failed to get data: ${response.message()}"
                    Toast.makeText(this@Spl, "Failed to get data: ${response.message()}", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout.isRefreshing = false
                    Log.d("Filter", "Publishing results for constraint: ${response.message()}")
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<List<DataClass_spl>>, t: Throwable) {
                Log.e("Request Failed", t.message.toString())
                loadtext.text = "Failed to get data: ${t.message.toString()}"
                Toast.makeText(this@Spl, "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    fun onItemClick(data: DataClass_spl) {
        vibrate()
    }
}