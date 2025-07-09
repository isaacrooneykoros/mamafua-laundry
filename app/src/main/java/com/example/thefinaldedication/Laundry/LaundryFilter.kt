// LaundryFilter.kt
package com.example.thefinaldedication.Laundry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LaundryFilter(
    laundry: List<LaundryFilterItem>,
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onFilterSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(laundry.size) { index ->
            val item = laundry[index]
            Card(
                modifier = Modifier
                    .width(130.dp)
                    .height(30.dp),
                shape = RoundedCornerShape(14.dp),
                onClick = { onFilterSelected(index) }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                        color = if ( selectedIndex == index ) Color.Black else Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(24.dp)
                    ),
                ) {
                    Text(
                        text = item.name,
                        color = if (selectedIndex == index) Color.White else Color(0xFF757575),
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Center),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

data class LaundryFilterItem(val id: String, val name: String)

@Preview
@Composable
fun PreviewLaundryFilter() {
    val sampleFilters = listOf(
        LaundryFilterItem(id = "1", name = "All"),
        LaundryFilterItem(id = "2", name = "Washed"),
        LaundryFilterItem(id = "3", name = "Unwashed")
    )
    LaundryFilter(
        laundry = sampleFilters,
        selectedIndex = 0,
        onFilterSelected = {}
    )
}