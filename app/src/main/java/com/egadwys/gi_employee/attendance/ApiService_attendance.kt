package com.egadwys.gi_employee.attendance

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService_attendance {
    @FormUrlEncoded
    @POST("GetAttendanceUser")
    fun getData(@Field("nik") nik: String): Call<List<DataClass_attendance>>
}
