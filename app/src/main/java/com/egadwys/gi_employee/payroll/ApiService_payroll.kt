package com.egadwys.gi_employee.payroll

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService_payroll {
    @FormUrlEncoded
    @POST("GetPayroll")
    fun getData(@Field("nik") nik: String): Call<List<DataClass_payroll>>
}
