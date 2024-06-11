package com.egadwys.gi_employee.scanner

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService_scanner {
    @FormUrlEncoded
    @POST("AuthUserScanner")
    fun cekuser(@Field("username") user: String): Call<List<DataClass_scanner>>
}
