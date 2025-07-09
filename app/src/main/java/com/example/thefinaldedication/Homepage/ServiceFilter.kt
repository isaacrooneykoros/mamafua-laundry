package com.example.thefinaldedication.Homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ServiceFilterItem(
    val id: String,
    val name: String
)

@Composable
fun ServiceFilter(
    services: List<ServiceFilterItem>,
    modifier: Modifier = Modifier,
    onSelect: (ServiceFilterItem) -> Unit = {}
) {
    var selectedServiceId by remember { mutableStateOf(services.firstOrNull()?.id ?: "") }

    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        services.forEach { service ->
            val isSelected = selectedServiceId == service.id

            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelected) Color.Black else Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable {
                        selectedServiceId = service.id
                        onSelect(service)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = service.name,
                    color = if (isSelected) Color.White else Color(0xFF757575),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview
@Composable
fun ServiceFilterPreview() {
    val services = listOf(
        ServiceFilterItem("1", "All"),
        ServiceFilterItem("2", "Dry Cleaning"),
        ServiceFilterItem("3", "Wash,Dry and Fold Services"),
        ServiceFilterItem("4", "Business Shirt Laundry and pressing"),
        ServiceFilterItem("5", "Bedding and Linen Cleaning"),
        ServiceFilterItem("6", "Household furnishings "),
        ServiceFilterItem("7", "Silk and Evening wear Specialists"),
        ServiceFilterItem("8", "Ceremonial Rob Graduation Gown Cleaning"),
        ServiceFilterItem("9", "Graduation Gown Cleaning"),
    )

    ServiceFilter(services = services)
}
