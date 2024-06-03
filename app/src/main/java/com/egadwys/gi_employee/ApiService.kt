package com.egadwys.gi_employee

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("GetAttendanceUser")
    fun getData(@Field("nik") nik: String): Call<List<YourDataClass>>
}
