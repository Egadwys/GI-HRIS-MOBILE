package com.egadwys.gi_employee

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("GetAttendanceAll")
    fun getData(): Call<List<YourDataClass>>

    @POST("GetInfoEmp")
    fun insertData(@Body data: YourDataClass): Call<YourResponseClass>
}
