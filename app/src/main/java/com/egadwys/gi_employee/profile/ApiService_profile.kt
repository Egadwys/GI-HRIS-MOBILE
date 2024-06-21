package com.egadwys.gi_employee.profile

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService_profile {
    @FormUrlEncoded
    @POST("GetDetailUser")
    fun getData(@Field("nik") nik: String): Call<List<DataClass_profile>>
}
