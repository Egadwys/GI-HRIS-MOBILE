package com.egadwys.gi_employee.apiContainer

import com.egadwys.gi_employee.apiContainer.ApiService
import com.egadwys.gi_employee.apiContainer.base_url
import com.egadwys.gi_employee.apiContainer.okHttpClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(base_url.BASE_URL)
            .client(okHttpClient.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}