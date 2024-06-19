package com.egadwys.gi_employee.dashboard.fragment

import android.annotation.SuppressLint
import android.content.Context
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
import com.egadwys.gi_employee.spl.DataAdapter_spl
import com.egadwys.gi_employee.spl.DataClass_spl
import com.egadwys.gi_employee.spl.RetrofitClient_spl
import com.egadwys.gi_employee.spl.Spl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplFragment : Fragment(), DataAdapter_spl.OnItemClickListener {
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
        return inflater.inflate(R.layout.spl, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        loading = view.findViewById(R.id.loading)
        loadtext = view.findViewById(R.id.loadtext)
        mRecyclerView = view.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        val nik = sharedPreferences.getString("user", "NoUser")
        Log.d("Filter", "NIK: ${nik}")
        swipeRefreshLayout.setOnRefreshListener {
            loaddata(nik.toString())
        }
        loaddata(nik.toString())
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

        RetrofitClient_spl.instance.getData(nik).enqueue(object : Callback<List<DataClass_spl>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<DataClass_spl>>, response: Response<List<DataClass_spl>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (!data.isNullOrEmpty()) {
                        requireActivity().runOnUiThread {
                            mRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
                            val adapter = DataAdapter_spl(data, Spl())
                            mRecyclerView.adapter = adapter

                            val searchView: SearchView = view?.findViewById(R.id.searchView)!!
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
                        Toast.makeText(requireContext(), "Response body is null or empty", Toast.LENGTH_LONG).show()
                    }
                    swipeRefreshLayout.isRefreshing = false
                } else {
                    mRecyclerView.visibility = View.GONE
                    loading.visibility = View.VISIBLE
                    loadtext.text = "Failed to get data: ${response.message()}"
                    Toast.makeText(requireContext(), "Failed to get data: ${response.message()}", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout.isRefreshing = false
                    Log.d("Filter", "Publishing results for constraint: ${response.message()}")
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<List<DataClass_spl>>, t: Throwable) {
                Log.e("Request Failed", t.message.toString())
                loadtext.text = "Failed to get data: ${t.message.toString()}"
                Toast.makeText(requireContext(), "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    override fun onItemClick(data: DataClass_spl) {
        vibrate()
    }
}