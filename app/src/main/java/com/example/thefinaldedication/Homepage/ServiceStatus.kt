package com.example.thefinaldedication.Homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Service(
    val name: String,
    val isActive: Boolean
)

@Composable
fun ServiceStatus(
    services: List<Service>, // Ensure you are using the correct type
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        services.forEachIndexed { index, service ->
            ServiceItem(service = service)

            if (index < services.size - 1) {
                Icon(
                    painter = painterResource(id = com.example.thefinaldedication.R.drawable.ic_arrow_up_right), // Ensure this resource exists
                    contentDescription = "Arrow",
                    tint = Color.Black,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ServiceItem(service: Service) {
    Box(
        modifier = Modifier
            .background(
                color = if (service.isActive) Color.Black else Color(0xFFE5E5E5),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = service.name,
            color = if (service.isActive) Color.White else Color(0xFF757575),
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ServiceStatusPreview() {
    val services = listOf(
        Service("Service 1", true),
        Service("Service 2", false),
        Service("Service 3", true)
    )

    ServiceStatus(services = services)
}
