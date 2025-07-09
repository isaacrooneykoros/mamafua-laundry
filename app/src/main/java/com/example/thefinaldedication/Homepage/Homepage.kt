@file:Suppress("DEPRECATION")

package com.example.thefinaldedication.Homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thefinaldedication.R
import com.example.thefinaldedication.navigation.ROUT_LAUNDRY

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homepage(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        var searchQuery = remember { mutableStateOf("") }
        val navController = rememberNavController()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
                .padding(innerPadding), // Apply padding provided by Scaffold
        ) {
            // Location Card
            Box(
                modifier = Modifier
                    .size(width = 390.dp, height = 230.dp)
                    .padding(start = 10.dp, end = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .paint(
                        painterResource(id = R.drawable.backgroundhomepage), // Ensure this matches your drawable resource
                        contentScale = ContentScale.Crop // You might want to use Crop instead of Fit depending on your design needs
                    )
            ) {
                // Location icon
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Location Icon",
                    modifier = Modifier
                        .size(90.dp) // Adjust size as needed
                        .clip(CircleShape)
                        .padding(16.dp),
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.size(15.dp))

            // Service Filter
            ServiceFilter(
                services = listOf(
                    ServiceFilterItem("1", "All"),
                    ServiceFilterItem("2", "Dry Cleaning"),
                    ServiceFilterItem("3", "Wash,Dry and Fold Services"),
                    ServiceFilterItem("4", "Business Shirt Laundry and pressing"),
                    ServiceFilterItem("5", "Bedding and Linen Cleaning"),
                    ServiceFilterItem("6", "Household furnishings "),
                    ServiceFilterItem("7", "Silk and Evening wear Specialists"),
                    ServiceFilterItem("8", "Ceremonial Rob and Graduation Gown Cleaning"),
                ),
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            )

            Spacer(modifier = Modifier.size(8.dp))
            // Search Bar
            TextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                placeholder = { Text("Search services...", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = Color.Black) },
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

            // Inside your Homepage() function, after the search TextField:

            Text(
                text = "Popular Services",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color(0xFF222B45),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 4.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            //Row 1 of Icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                // Define a data class for icon items with optional route
                data class IconItem(val icon: Int, val name: String, val route: String? = null)
                val iconItems = listOf(
                    IconItem(R.drawable.washing, "Laundry", ROUT_LAUNDRY),
                    IconItem(R.drawable.washandfold, "Wash and Fold"),
                    IconItem(R.drawable.suit, "Dry Cleaning"),
                    // Add more icons and names as needed
                )
                iconItems.forEach { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { item.route?.let { navController.navigate(it) } }
                            .padding(12.dp)
                            .width(100.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Color.White, Color(0xFFB2FEFA))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.name,
                                modifier = Modifier.size(36.dp),
                                tint = Color.Unspecified,
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF222B45),
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(18.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                val iconItems = listOf(
                    Pair(R.drawable.hand, "Custom Laundry"),
                    Pair(R.drawable.sewing, "Tailoring"),
                    Pair(R.drawable.premium, "Premium Services"),
                    // Add more icons and names as needed
                )
                iconItems.forEach { (icon, name) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { /* Handle click for $name */ }
                            .padding(12.dp)
                            .width(100.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Color.White, Color(0xFFB2FEFA))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = name,
                                modifier = Modifier.size(36.dp),
                                tint = Color.Unspecified,
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF222B45),
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(top = 4.dp, start = 20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(18.dp))


        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    Homepage(rememberNavController())
}