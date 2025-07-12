package com.example.thefinaldedication.Payment

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://345b8d1eb0fd.ngrok-free.app/"  // My current backend URL

    val api: MpesaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MpesaApi::class.java)
    }
}
// Note: Ensure that your backend is running and accessible at the specified BASE_URL.
