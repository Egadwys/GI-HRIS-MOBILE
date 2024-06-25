package com.egadwys.gi_employee.profile

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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.egadwys.gi_employee.R
import com.egadwys.gi_employee.attendance.DataAdapter_attendance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var profile_name: TextView
    private lateinit var profile_subname: TextView
    private lateinit var profil_picture: ImageView
    private lateinit var dept: TextView
    private lateinit var div: TextView
    private lateinit var wg: TextView
    private lateinit var ws: TextView
    private lateinit var ss: TextView
    private lateinit var db: TextView
    private lateinit var jk: TextView
    private lateinit var s: TextView
    private lateinit var a: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile, container, false)

        profile_name = view.findViewById(R.id.profile_name)
        profile_subname = view.findViewById(R.id.profile_subname)
        profil_picture = view.findViewById(R.id.profile_picture)
        dept = view.findViewById(R.id.profile_dept)
        div = view.findViewById(R.id.profile_div)
        wg = view.findViewById(R.id.profile_workgroup)
        ws = view.findViewById(R.id.profile_workStatus)
        ss = view.findViewById(R.id.profile_staffStatus)
        db = view.findViewById(R.id.profile_dateBirth)
        jk = view.findViewById(R.id.profile_jenis)
        s = view.findViewById(R.id.profile_menikah)
        a = view.findViewById(R.id.profile_address)

        sharedPreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)

        val nik = sharedPreferences.getString("user", "NoUser")
        Log.d("Filter", "NIK: ${nik}")

        loaddata(nik.toString())

        val imageUrl = "http://192.168.10.242:8078/GI-HRIS-API/photo/${nik}.jpg"
        Glide.with(this)
            .load(imageUrl)
            .into(profil_picture)

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
        RetrofitClient_profile.instance.getData(nik).enqueue(object : Callback<List<DataClass_profile>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<DataClass_profile>>, response: Response<List<DataClass_profile>>) {
                if (response.isSuccessful) {
                    response.body()?.let { loginDataList ->
                        if (loginDataList.isNotEmpty()) {
                            val loginData = loginDataList[0]
                            profile_name.text = loginData.name
                            profile_subname.text = "(${loginData.position})"
                            dept.text = loginData.dept
                            div.text = loginData.division
                            wg.text = loginData.workgroup
                            ws.text = loginData.workStatus
                            ss.text = loginData.EmpStatus
                            db.text = loginData.dateBirth
                            jk.text = loginData.jenis
                            s.text = loginData.status
                            a.text = loginData.address
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to get data: ${response.message()}", Toast.LENGTH_LONG).show()
                    Log.d("Filter", "Publishing results for constraint: ${response.message()}")
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<List<DataClass_profile>>, t: Throwable) {
                Log.e("Request Failed", t.message.toString())
                Toast.makeText(requireContext(), "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}