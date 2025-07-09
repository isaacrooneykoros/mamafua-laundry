package com.example.thefinaldedication.models

import java.time.LocalDate

data class LaundryRecord(
    val date: LocalDate,
    val time: String,
    val type: String,
    val status: String // "done" or "scheduled"
)