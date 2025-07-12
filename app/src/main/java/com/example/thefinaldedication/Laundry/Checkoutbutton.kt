// CheckoutButton.kt
package com.example.thefinaldedication.Laundry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thefinaldedication.navigation.ROUT_PAYMENT

@Composable
fun CheckoutButton(itemCount: Int, navController: NavController, cartViewModel: CartViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(itemCount) {
        if (itemCount == 0) {
            snackbarHostState.showSnackbar("No items in cart")
        }
    }

    Column {
        SnackbarHost(hostState = snackbarHostState)
        Button(
            onClick = {
                if (itemCount > 0) {
                    navController.navigate(ROUT_PAYMENT)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
            enabled = itemCount > 0
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$itemCount",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Items",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCheckoutButton() {
    val navController = rememberNavController()
    val cartViewModel = CartViewModel()
    cartViewModel.addItem(CartItem("Shirt", "Laundry Service", 100.0, 2))
    val itemCount by cartViewModel.cartItems.collectAsState().map { it.sumOf { cartItem -> cartItem.quantity } }
    CheckoutButton(itemCount = itemCount, navController = navController, cartViewModel = cartViewModel)
}
