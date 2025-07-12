package com.example.thefinaldedication.Payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thefinaldedication.cart.CartItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun PaymentScreen(
    navController: NavController,
    cartItems: List<CartItem>
) {
    val totalAmount = cartItems.sumOf { it.price * it.quantity }
    var phoneNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var paymentStatus by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Payment",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
                text = "Total Amount: $totalAmount",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number (2547XXXXXXXX)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Button(
                onClick = {
                    if (!phoneNumber.matches(Regex("^2547\\d{8}$"))) {
                        errorMessage = "Please enter a valid phone number in the format 2547XXXXXXXX."
                    } else if (totalAmount <= 0) {
                        errorMessage = "Amount must be greater than 0."
                    } else {
                        errorMessage = ""

                        val request = StkPushRequest(
                            phone = phoneNumber,
                            amount = totalAmount.toInt() // ✅ fixed!
                        )

                        RetrofitInstance.api.initiateStkPush(request)
                            .enqueue(object : Callback<StkPushResponse> {
                                override fun onResponse(
                                    call: Call<StkPushResponse>,
                                    response: Response<StkPushResponse>
                                ) {
                                    val errorBody = response.errorBody()?.string()
                                    println("❌ M-Pesa Error Response: $errorBody")

                                    paymentStatus = if (response.isSuccessful) {
                                        "✅ ${response.body()?.CustomerMessage ?: "Payment initiated."}"
                                    } else {
                                        "❌ Payment failed: ${errorBody ?: "Unknown error from server"}"
                                    }
                                }

                                override fun onFailure(call: Call<StkPushResponse>, t: Throwable) {
                                    paymentStatus = "❌ Network error: ${t.message}"
                                }
                            })
                    }
                }
                ,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pay with M-Pesa")
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            paymentStatus?.let {
                Text(
                    text = it,
                    color = if (it.startsWith("✅")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }

@Preview
@Composable
fun PaymentScreenPreview() {
    PaymentScreen(
        navController = rememberNavController(),
        cartItems = listOf(
            CartItem("1", "Laundry Service 1", 100, 2),
            CartItem("2", "Laundry Service 2", 150, 1)
        )
    )
}
