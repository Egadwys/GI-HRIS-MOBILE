package com.egadwys.gi_employee

import android.text.Editable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginApiService {
    @FormUrlEncoded
    @POST("AuthUser")
    fun cekuser(@Field("username") user: String,@Field("password") pass: String): Call<List<LoginDataClass>>
}
