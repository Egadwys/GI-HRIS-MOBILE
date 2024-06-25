package com.egadwys.gi_employee.apiContainer

import com.egadwys.gi_employee.attendance.DataClass_attendance
import com.egadwys.gi_employee.auth.DataClass_auth
import com.egadwys.gi_employee.cuti.DataClass_cuti
import com.egadwys.gi_employee.payroll.DataClass_payroll
import com.egadwys.gi_employee.payroll.detail.DataClass_detail
import com.egadwys.gi_employee.profile.DataClass_profile
import com.egadwys.gi_employee.scanner.DataClass_scanner
import com.egadwys.gi_employee.spl.DataClass_spl
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("GetAttendanceUser")
    fun GetAttendanceUser(@Field("nik") nik: String): Call<List<DataClass_attendance>>

    @FormUrlEncoded
    @POST("AuthUser")
    fun AuthUser(@Field("username") user: String,@Field("password") pass: String): Call<List<DataClass_auth>>

    @FormUrlEncoded
    @POST("GetDetailCuti")
    fun GetDetailCuti(@Field("nik") nik: String): Call<List<DataClass_cuti>>

    @FormUrlEncoded
    @POST("GetPayroll")
    fun GetPayroll(@Field("nik") nik: String): Call<List<DataClass_payroll>>

    @FormUrlEncoded
    @POST("GetDetailPayroll")
    fun GetDetailPayroll(@Field("id") id: String): Call<List<DataClass_detail>>

    @FormUrlEncoded
    @POST("GetDetailUser")
    fun GetDetailUser(@Field("nik") nik: String): Call<List<DataClass_profile>>

    @FormUrlEncoded
    @POST("AuthUserScanner")
    fun AuthUserScanner(@Field("username") user: String): Call<List<DataClass_scanner>>

    @FormUrlEncoded
    @POST("GetDetailSPL")
    fun GetDetailSPL(@Field("nik") nik: String): Call<List<DataClass_spl>>
}
