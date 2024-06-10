package com.egadwys.gi_employee.auth

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService_auth {
    @FormUrlEncoded
    @POST("AuthUser")
    fun cekuser(@Field("username") user: String,@Field("password") pass: String): Call<List<DataClass_auth>>
}
