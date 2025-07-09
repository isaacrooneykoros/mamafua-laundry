// PaymentScreen.kt
package com.example.thefinaldedication.Payment

import android.util.Base64
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thefinaldedication.cart.CartItem
import kotlinx.coroutines.launch
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

class PaymentViewModel : ViewModel() {
    var paymentStatus by mutableStateOf<String?>(null)
        private set

    fun initiateStkPush(phoneNumber: String, amount: Double) {
        // TODO: Move sensitive data to secure backend
        val businessShortCode = "174379"
        val passkey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
        val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        val password = Base64.encodeToString(
            "$businessShortCode$passkey$timestamp".toByteArray(),
            Base64.NO_WRAP
        )
        val stkPushRequest = StkPushRequest(
            BusinessShortCode = businessShortCode,
            Password = password,
            Timestamp = timestamp,
            TransactionType = "CustomerPayBillOnline",
            Amount = amount.toInt().toString(),
            PartyA = phoneNumber,
            PartyB = businessShortCode,
            PhoneNumber = phoneNumber,
            // TODO: Set your production callback URL here
            CallBackURL = "https://your-backend.com/mpesa/callback",
            AccountReference = "ORDER12345",
            TransactionDesc = "Payment for laundry services"
        )

        // TODO: Use dependency injection for RetrofitInstance
        viewModelScope.launch {
            RetrofitInstance.api.initiateStkPush(stkPushRequest).enqueue(object : retrofit2.Callback<StkPushResponse> {
                override fun onResponse(call: Call<StkPushResponse>, response: retrofit2.Response<StkPushResponse>) {
                    paymentStatus = if (response.isSuccessful) {
                        "Payment initiated successfully. Please complete the payment on your phone."
                    } else {
                        "Error initiating payment: ${response.errorBody()?.string() ?: "Unknown error"}"
                    }
                }
                override fun onFailure(call: Call<StkPushResponse>, t: Throwable) {
                    paymentStatus = "Network error: ${t.message}"
                }
            })
        }
    }
}

@Composable
fun PaymentScreen(
    navController: NavController,
    cartItems: List<CartItem>,
    viewModel: PaymentViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val totalAmount = cartItems.sumOf { it.price * it.quantity }
    var phoneNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val paymentStatus = viewModel.paymentStatus

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                // Mpesa expects 2547XXXXXXXX format
                if (!phoneNumber.matches(Regex("^2547\\d{8}\$"))) {
                    errorMessage = "Please enter a valid phone number in the format 2547XXXXXXXX."
                } else {
                    errorMessage = ""
                    viewModel.initiateStkPush(phoneNumber, totalAmount)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pay with Mpesa")
        }
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        if (paymentStatus != null) {
            Text(
                text = paymentStatus,
                color = if (paymentStatus.startsWith("Payment initiated")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
    // TODO: Check and request required permissions if needed
}

@Preview
@Composable
fun PaymentScreenPreview() {
    PaymentScreen(
        navController = rememberNavController(),
        cartItems = listOf(
            CartItem("1", "Laundry Service 1", 100.0, 2),
            CartItem("2", "Laundry Service 2", 150.0, 1)
        )
    )
}