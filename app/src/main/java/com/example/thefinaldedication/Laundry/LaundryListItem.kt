package com.example.thefinaldedication.Laundry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.compareTo
import kotlin.dec

data class LaundryItem(
    val name: String,
    val price: Double,
    val imageRes: Int,
    val categories: List<String>  // e.g., "Men", "Women", "Kids", "Bedding"
)

@Composable
fun LaundryListItem(item: LaundryItem, onAdd: () -> Unit, onRemove: () -> Unit) {
    val count = remember { mutableIntStateOf(0) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(height = 100.dp, width = 350.dp)
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .background(Color.Gray, RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(80.dp).fillMaxHeight(),
        ) {
            Icon(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier.fillMaxSize(),
                tint = Color.Unspecified
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Price: Ksh${item.price}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    if (count.intValue > 0) {
                        count.intValue--
                        onRemove() // Call onRemove
                    }
                },
                enabled = count.value > 0
            ) {
                Text("-", style = MaterialTheme.typography.headlineSmall)
            }
            Text(
                text = count.intValue.toString(),
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(
                onClick = {
                    count.intValue++
                    onAdd()
                }
            ) {
                Text("+", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}

@Preview
@Composable
fun PreviewLaundryListItem() {
    val sampleItem = LaundryItem(
        name = "Shirt",
        price = 2.0,
        imageRes = com.example.thefinaldedication.R.drawable.shirt,
        categories = listOf("Men", "Women")
    )
    LaundryListItem(item = sampleItem, onAdd = {}, onRemove = {})
}