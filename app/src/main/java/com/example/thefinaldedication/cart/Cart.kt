// Cart.kt
package com.example.thefinaldedication.Laundry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thefinaldedication.cart.CartItem
import com.example.thefinaldedication.navigation.ROUT_OTP

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(navController: NavController, cartItems: List<CartItem>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Cart",
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                actions = {}
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: ${cartItems.sumOf { it.price * it.quantity }}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
                Button(
                    onClick = { navController.navigate(ROUT_OTP) },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Complete Your Order")
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
        ) {
            items(cartItems) { item ->
                CartItemRow(item = item, onQuantityChange = { newQuantity ->
                    item.quantity = newQuantity
                })
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onQuantityChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(28.dp))
            Row {
                Button(
                    onClick = { onQuantityChange(item.quantity - 1) },
                    enabled = item.quantity > 1,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .size(height = 32.dp, width = 62.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${item.quantity}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .width(40.dp)
                        .padding(start = 10.dp, end = 8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onQuantityChange(item.quantity + 1) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .size(height = 32.dp, width = 62.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text("+")
                }
            }
        }
        Text(
            text = "Price: ${item.price * item.quantity}",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CartPreview() {
    val cartItems = listOf(
        CartItem("Shirt", "Laundry Service 2", 100.0, 2),
        CartItem("Trousers", "Laundry Service 2", 150.0, 1)
    )
    Cart(navController = rememberNavController(), cartItems = cartItems)
}