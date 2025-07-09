package com.example.thefinaldedication.Homepage

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thefinaldedication.models.LaundryRecord
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LaundryCalendar(
    records: List<LaundryRecord>,
    onDateClick: (LocalDate) -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.padding(16.dp)
    ) {
        // Add empty cells for alignment
        items(firstDayOfWeek - 1) {
            Spacer(modifier = Modifier.size(40.dp))
        }

        // Add days with markers
        items(daysInMonth) { day ->
            val date = currentMonth.atDay(day + 1 - firstDayOfWeek)
            val record = records.find { it.date == date }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onDateClick(date) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "${day + 1 - firstDayOfWeek}")
                when (record?.status) {
                    "done" -> Icon(
                        painter = painterResource(id = com.example.thefinaldedication.R.drawable.ic_location),
                        contentDescription = "Done",
                        tint = Color.Green,
                        modifier = Modifier.size(12.dp)
                    )
                    "scheduled" -> Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Blue, CircleShape)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewLaundryCalendar() {
    val records = listOf(
        LaundryRecord(LocalDate.now(), "10:00 AM", "Wash", "done"),
        LaundryRecord(LocalDate.now().plusDays(1), "2:00 PM", "Dry", "scheduled")
    )

    LaundryCalendar(records) { date ->
        // Handle date click
    }
}