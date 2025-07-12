package com.example.thefinaldedication.Payment

data class StkPushRequest(
    val phone: String,
    val amount: Int
)
// Note: Ensure that the phone number is in the correct format (e.g., "254712345678" for Kenyan numbers).