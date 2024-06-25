package com.egadwys.gi_employee.dashboard.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.egadwys.gi_employee.R
import com.egadwys.gi_employee.apiContainer.RetrofitClient
import com.egadwys.gi_employee.payroll.DataAdapter_payroll
import com.egadwys.gi_employee.payroll.DataClass_payroll
import com.egadwys.gi_employee.payroll.detail.Detail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PayrollFragment : Fragment(), DataAdapter_payroll.OnItemClickListener {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var loading: LinearLayout
    private lateinit var loadtext: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.payroll, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        loading = view.findViewById(R.id.loading)
        loadtext = view.findViewById(R.id.loadtext)
        mRecyclerView = view.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        val nik = sharedPreferences.getString("user", "NoUser")
        Log.d("Filter", nik.toString())
        swipeRefreshLayout.setOnRefreshListener {
            loaddata(nik.toString())
        }
        loaddata(nik.toString())

        return view
    }

    private fun vibrate() {
        val vibrator = ContextCompat.getSystemService(requireContext(), Vibrator::class.java) as Vibrator
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
        Log.d("NIK: ", nik)
        RetrofitClient.instance.GetPayroll(nik).enqueue(object : Callback<List<DataClass_payroll>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<DataClass_payroll>>, response: Response<List<DataClass_payroll>>) {
                Log.d("Error: ", response.message())
                if (response.isSuccessful) {
                    Log.d("Sukses: ", "${response.body()}")
                    val data = response.body()
                    if (!data.isNullOrEmpty()) {
                        requireActivity().runOnUiThread {
                            mRecyclerView.layoutManager = GridLayoutManager(context, 3)
                            val adapter = DataAdapter_payroll(data, this@PayrollFragment)
                            mRecyclerView.adapter = adapter

                            val searchView: SearchView = view!!.findViewById(R.id.searchView)
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
                        Toast.makeText(context, "Response body is null or empty", Toast.LENGTH_LONG).show()
                    }
                    swipeRefreshLayout.isRefreshing = false
                } else {
                    mRecyclerView.visibility = View.GONE
                    loading.visibility = View.VISIBLE
                    loadtext.text = "Failed to get data: ${response.message()}"
                    Toast.makeText(context, "Failed to get data: ${response.message()}", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout.isRefreshing = false
                    Log.d("Gagal: ", response.message())
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<List<DataClass_payroll>>, t: Throwable) {
                Log.e("Request Failed", t.message.toString())
                loadtext.text = "Failed to get data: ${t.message.toString()}"
                Toast.makeText(context, "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    override fun onItemClick(data: DataClass_payroll) {
        vibrate()
        val intent = Intent(activity, Detail::class.java).apply {
            putExtra("id", data.id)
        }
        startActivity(intent)
    }
}
