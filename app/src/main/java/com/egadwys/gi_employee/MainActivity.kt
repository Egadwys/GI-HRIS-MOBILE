package com.egadwys.gi_employee

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), YourDataAdapter.OnItemClickListener{
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var loading: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var loadtext: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var title: TextView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loading = findViewById(R.id.loading)
        progressBar = findViewById(R.id.progressBar)
        loadtext = findViewById(R.id.loadtext)
        mRecyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        title = findViewById(R.id.main_title_header)

        title.text = intent.getStringExtra("name")



        swipeRefreshLayout.setOnRefreshListener {
            loaddata()
        }

        loaddata()

    }

    private fun loaddata() {
        progressBar.visibility = View.GONE
        loading.visibility = View.VISIBLE
        mRecyclerView.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = true

        RetrofitClient.instance.getData().enqueue(object : Callback<List<YourDataClass>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<YourDataClass>>, response: Response<List<YourDataClass>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.isNotEmpty()) {
                        runOnUiThread {
                            mRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                            val adapter = YourDataAdapter(data, this@MainActivity) // Pass 'this' as itemClickListener
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
                        Toast.makeText(this@MainActivity, "Response body is null or empty", Toast.LENGTH_LONG).show()
                    }
                    swipeRefreshLayout.isRefreshing = false
                } else {
                    progressBar.visibility = View.GONE
                    mRecyclerView.visibility = View.GONE
                    loading.visibility = View.VISIBLE
                    loadtext.text = "Failed to get data: ${response.message()}"
                    Toast.makeText(this@MainActivity, "Failed to get data: ${response.message()}", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout.isRefreshing = false
                    Log.d("Filter", "Publishing results for constraint: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<YourDataClass>>, t: Throwable) {
                Log.e("Request Failed", t.message.toString())
                loadtext.text = "Failed to get data: ${t.message.toString()}"
                Toast.makeText(this@MainActivity, "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    override fun onItemClick(data: YourDataClass) {
        val dialogFragment = DialogFragment()
        val bundle = Bundle()
        bundle.putString("name", data.name)
        bundle.putString("position", data.position)
        bundle.putString("nik", data.NIK.toString())
        bundle.putString("dept", data.dept)
        bundle.putString("div", data.division)
        bundle.putString("wg", data.Workgroup)
        bundle.putString("birth", data.dateBirth)
        bundle.putString("kec", data.kecamatan)
        bundle.putString("kel", data.kelurahan)
        bundle.putString("kota", data.kota)
        bundle.putString("prov", data.provinsi)
        bundle.putString("negara", data.negara)
        bundle.putString("tin", data.masuk)
        bundle.putString("tout", data.pulang)
        dialogFragment.arguments = bundle
        dialogFragment.show(supportFragmentManager, "CustomDialogFragmentTag")
    }
}