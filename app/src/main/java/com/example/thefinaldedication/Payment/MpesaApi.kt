package com.example.thefinaldedication.Payment

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MpesaApi {
    @POST("stkpush") // ✅ Correct endpoint for your Node.js backend
    fun initiateStkPush(@Body stkPushRequest: StkPushRequest): Call<StkPushResponse>
}
// Note: Ensure that your Node.js backend is set up to handle this endpoint and process the STK push request.
