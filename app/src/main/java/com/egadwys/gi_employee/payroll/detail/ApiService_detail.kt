package com.egadwys.gi_employee.payroll.detail

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService_detail {
    @FormUrlEncoded
    @POST("GetDetailPayroll")
    fun cekpayroll(@Field("id") id: String): Call<List<DataClass_detail>>
}
