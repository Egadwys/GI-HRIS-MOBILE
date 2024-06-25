package com.egadwys.gi_employee.apiContainer

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object okHttpClient {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()
}