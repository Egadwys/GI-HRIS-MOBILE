package com.egadwys.gi_employee.spl

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService_spl {
    @FormUrlEncoded
    @POST("GetDetailSPL")
    fun getData(@Field("nik") nik: String): Call<List<DataClass_spl>>
}
