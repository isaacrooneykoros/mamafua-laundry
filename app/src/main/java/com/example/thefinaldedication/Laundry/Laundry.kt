
@file:Suppress("DEPRECATION")

package com.example.thefinaldedication.Homepage

import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.thefinaldedication.Laundry.CheckoutButton
import com.example.thefinaldedication.Laundry.LaundryFilter
import com.example.thefinaldedication.Laundry.LaundryFilterItem
import com.example.thefinaldedication.Laundry.LaundryItem
import com.example.thefinaldedication.Laundry.LaundryListItem
import com.example.thefinaldedication.navigation.ROUT_HOME
import com.example.thefinaldedication.navigation.ROUT_PAYMENT
import kotlin.collections.listOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Laundry( navController: NavController) {
    val itemCountState = remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Laundry Services",
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ROUT_HOME) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                actions = {}
            )
        },
       bottomBar = {
           CheckoutButton(itemCount = itemCountState.intValue)
       },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(ROUT_HOME) }) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
            }
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        var searchQuery = remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
                .padding(innerPadding), // Apply padding provided by Scaffold
        ) {
            Spacer(modifier = Modifier.size(8.dp))
            // Laundry Filter

            val filterOptions = listOf(
                LaundryFilterItem("1", "Men's Laundry"),
                LaundryFilterItem("2", "Women's Laundry"),
                LaundryFilterItem("3", "Kids' Laundry"),
                LaundryFilterItem("4", "Bedding and Linen Cleaning"),
            )
            val filterCategories = listOf("Men", "Women", "Kids", "Bedding")
            val (selectedFilter, setSelectedFilter) = remember { mutableIntStateOf(0) }


            Spacer(modifier = Modifier.size(18.dp))
            // Search Bar
            TextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                placeholder = { Text("Search type of laundry...", color = Color.Black) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Black
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.size(18.dp))
            // Move LaundryFilter and filtered list rendering inside the Column, after search bar and before the full list
            LaundryFilter(
                laundry = filterOptions,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                selectedIndex = selectedFilter,
                onFilterSelected = { index -> setSelectedFilter(index) }
            )
            //Laundry Items
            // Laundry Items List
            val laundryItems = listOf(
                LaundryItem(
                    "Shirt",
                    100.0,
                    com.example.thefinaldedication.R.drawable.shirt,
                    listOf("Men")
                ),
                LaundryItem(
                    "Trousers",
                    150.0,
                    com.example.thefinaldedication.R.drawable.trousers,
                    listOf("Men", "Women", "Kids")
                ),
                LaundryItem(
                    "Sweaters",
                    180.0,
                    com.example.thefinaldedication.R.drawable.sweater,
                    listOf("Men", "Women", "Kids")
                ),
                LaundryItem(
                    "Hoods",
                    180.0,
                    com.example.thefinaldedication.R.drawable.hoodie,
                    listOf("Men", "Women", "Kids")
                ),
                LaundryItem(
                    "T-Shirt",
                    100.0,
                    com.example.thefinaldedication.R.drawable.tshirt,
                    listOf("Men", "Women", "Kids")
                ),
                LaundryItem(
                    "Shorts",
                    120.0,
                    com.example.thefinaldedication.R.drawable.shorts,
                    listOf("Men", "Women", "Kids")
                ),
                LaundryItem(
                    "Jacket",
                    200.0,
                    com.example.thefinaldedication.R.drawable.jacket,
                    listOf("Men", "Women","Kids")
                ),
                LaundryItem(
                    "Undergarments",
                    130.0,
                    com.example.thefinaldedication.R.drawable.underpants,
                    listOf("Men", "Women", "Kids")
                ),
                LaundryItem(
                    "Blouse",
                    250.0,
                    com.example.thefinaldedication.R.drawable.blouse,
                    listOf("Women")
                ),
                )
            val filteredItems = laundryItems.filter { item ->
                filterCategories[selectedFilter] in item.categories &&
                        item.name.contains(searchQuery.value, ignoreCase = true)
            }
            filteredItems.forEach { item: LaundryItem ->
                LaundryListItem(
                    item = item,
                    onAdd = { itemCountState.intValue = itemCountState.intValue + 1 },
                    onRemove = {
                        if (itemCountState.intValue > 0) {
                            itemCountState.intValue = itemCountState.intValue - 1
                        }
                    }
                )
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LaundryPreview() {
    Laundry(navController = NavController(LocalContext.current))
}
