package com.example.thefinaldedication.Homepage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class LaundryService(
    val name: String,
    val isAvailable: Boolean
)

enum class LaundryCardVariant {
    DEFAULT,
    PRICING
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaundryCard(
    name: String,
    services: List<LaundryService>,
    location: String,
    estimationTime: String,
    estimationDate: String,
    price: String,
    tags: List<String>,
    category: String,
    variant: LaundryCardVariant = LaundryCardVariant.DEFAULT,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(300.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "• $estimationDate",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Text(
                    text = "Ksh $price",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Services
            services.forEach { service ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "• ${service.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (service.isAvailable) Color.Black else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                tags.forEach { tag ->
                    AssistChip(
                        onClick = { },
                        label = { Text(tag, fontSize = 12.sp) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LaundryCardPreview() {
    LaundryCard(
            name = "Regular Laundry",
            services = listOf(
                LaundryService("Wash", true),
                LaundryService("Dry", true),
                LaundryService("Fold", true)
            ),
            location = "South B, Nairobi",
            estimationTime = "2 hours",
            estimationDate = "Today",
            price = "200",
            tags = listOf("Fast Service", "Quality Care"),
            category = "Regular Laundry"
        )
    }
