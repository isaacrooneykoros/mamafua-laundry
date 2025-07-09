package com.example.thefinaldedication.Payment

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MpesaApi {
    @POST("mpesa/stkpush/v1/processrequest")
    fun initiateStkPush(@Body stkPushRequest: StkPushRequest): Call<StkPushResponse>
}
