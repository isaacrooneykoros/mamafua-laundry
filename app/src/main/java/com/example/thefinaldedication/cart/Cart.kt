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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thefinaldedication.navigation.ROUT_PAYMENT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    val cartItems by cartViewModel.cartItems.collectAsState()

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
                    text = "Total: ${cartViewModel.getTotalPrice()}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
                Button(
                    onClick = {
                        if (cartItems.isNotEmpty()) {
                            navController.navigate(ROUT_PAYMENT)
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    enabled = cartItems.isNotEmpty()
                ) {
                    Text("Complete Your Order")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
        ) {
            if (cartItems.isEmpty()) {
                Text(
                    text = "Your cart is empty",
                    color = Color.Gray,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(cartItems) { cartItem ->
                        CartItemRow(cartItem = cartItem, cartViewModel = cartViewModel)
                        Spacer(modifier = Modifier.size(12.dp))
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${cartViewModel.getTotalPrice()}",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemRow(cartItem: CartItem, cartViewModel: CartViewModel) {
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
                text = cartItem.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(28.dp))
            Row {
                Button(
                    onClick = { cartViewModel.removeItem(cartItem) },
                    enabled = cartItem.quantity > 1,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .size(height = 32.dp, width = 62.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${cartItem.quantity}",
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
                    onClick = { cartViewModel.addItem(cartItem.copy(quantity = 1)) },
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
            text = "Price: ${cartItem.price * cartItem.quantity}",
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
    val navController = rememberNavController()
    val cartViewModel = CartViewModel()
    cartViewModel.addItem(CartItem("Shirt", "Laundry Service", 100.0, 2))
    cartViewModel.addItem(CartItem("Trousers", "Laundry Service", 150.0, 1))
    Cart(navController = navController, cartViewModel = cartViewModel)
}
