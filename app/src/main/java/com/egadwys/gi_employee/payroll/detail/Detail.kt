package com.egadwys.gi_employee.payroll.detail


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.egadwys.gi_employee.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Detail : AppCompatActivity() {
    private lateinit var title: TextView
    private lateinit var main_subtitle_header: TextView
    private lateinit var loadtext_detail_payroll: TextView
    private lateinit var scrollview_detail_payroll: ScrollView
    private lateinit var detail1: TextView
    private lateinit var detail2: TextView
    private lateinit var detail3: TextView
    private lateinit var detail4: TextView
    private lateinit var detail5: TextView
    private lateinit var detail6: TextView
    private lateinit var detail7: TextView
    private lateinit var detail8: TextView
    private lateinit var detail9: TextView
    private lateinit var detail10: TextView
    private lateinit var detail11: TextView
    private lateinit var detail_potongan1: TextView
    private lateinit var detail_potongan2: TextView
    private lateinit var detail_potongan3: TextView
    private lateinit var detail_potongan4: TextView
    private lateinit var detail_potongan5: TextView
    private lateinit var detail_terima1: TextView
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payroll_detail)

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        title = findViewById(R.id.main_title_header)
        title.text = sharedPreferences.getString("nama", "NoNama")

        main_subtitle_header = findViewById(R.id.main_subtitle_header)
        loadtext_detail_payroll = findViewById(R.id.loadtext_detail_payroll)
        scrollview_detail_payroll = findViewById(R.id.scrollview_detail_payroll)
        detail1 = findViewById(R.id.detail1)
        detail2 = findViewById(R.id.detail2)
        detail3 = findViewById(R.id.detail3)
        detail4 = findViewById(R.id.detail4)
        detail5 = findViewById(R.id.detail5)
        detail6 = findViewById(R.id.detail6)
        detail7 = findViewById(R.id.detail7)
        detail8 = findViewById(R.id.detail8)
        detail9 = findViewById(R.id.detail9)
        detail10 = findViewById(R.id.detail10)
        detail11 = findViewById(R.id.detail11)

        detail_potongan1 = findViewById(R.id.detail_potongan1)
        detail_potongan2 = findViewById(R.id.detail_potongan2)
        detail_potongan3 = findViewById(R.id.detail_potongan3)
        detail_potongan4 = findViewById(R.id.detail_potongan4)
        detail_potongan5 = findViewById(R.id.detail_potongan5)

        detail_terima1 = findViewById(R.id.detail_terima1)

        loadtext_detail_payroll.visibility = View.VISIBLE
        scrollview_detail_payroll.visibility = View.GONE

        val id = intent.getStringExtra("id")
        getdata(id.toString())

    }

    private fun getdata(id: String) {
        RetrofitClient_detail.instance.cekpayroll(id).enqueue(object :
            Callback<List<DataClass_detail>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<DataClass_detail>>, response: Response<List<DataClass_detail>>) {
                if (response.isSuccessful) {
                    response.body()?.let { loginDataList ->
                        if (loginDataList.isNotEmpty()) {
                            val loginData = loginDataList[0]
                            loadtext_detail_payroll.visibility = View.GONE
                            scrollview_detail_payroll.visibility = View.VISIBLE

                            main_subtitle_header.text = loginData.periode

                            detail1.text = "Rp. ${loginData.jumlahup}"
                            detail2.text = "Rp. ${loginData.tunjjab}"
                            detail3.text = "Rp. ${loginData.tunjket}"
                            detail4.text = "Rp. ${loginData.tunjutility}"
                            detail5.text = "Rp. ${loginData.amtshift2}"
                            detail6.text = "Rp. ${loginData.amtshift3}"
                            detail7.text = "Rp. ${loginData.amtot1}"
                            detail8.text = "Rp. ${loginData.amtot2}"
                            detail9.text = "Rp. ${loginData.amtot3}"
                            detail10.text = "Rp. ${loginData.amtot4}"
                            detail11.text = "Rp. ${loginData.pendapatan}"

                            detail_potongan1.text = "Rp. ${loginData.bpjsks}"
                            detail_potongan2.text = "Rp. ${loginData.bpjstk}"
                            detail_potongan3.text = "Rp. ${loginData.koperasi}"
                            detail_potongan4.text = "Rp. ${loginData.kasbongj}"
                            detail_potongan5.text = "Rp. ${loginData.potongan}"

                            detail_terima1.text = "Rp. ${loginData.transfer}"
                        }
                    }
                    Log.d("OK: ", "${response.body()}")
                } else {
                    loadtext_detail_payroll.text = response.message().toString()
                    Toast.makeText(this@Detail, "Response: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<DataClass_detail>>, t: Throwable) {
                loadtext_detail_payroll.text = t.message.toString()
                Toast.makeText(this@Detail, "Failure: ${t.message.toString()}", Toast.LENGTH_SHORT).show()
                Log.e("Request Failed E", t.message.toString())
            }
        })
    }
}