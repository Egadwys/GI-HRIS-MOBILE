package com.egadwys.gi_employee.cuti

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService_cuti {
    @FormUrlEncoded
    @POST("GetDetailCuti")
    fun getData(@Field("nik") nik: String): Call<List<DataClass_cuti>>
}
