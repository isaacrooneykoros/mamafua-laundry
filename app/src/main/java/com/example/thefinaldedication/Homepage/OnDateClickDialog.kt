package com.example.thefinaldedication.Homepage

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.thefinaldedication.models.LaundryRecord
import java.time.LocalDate

@Composable
fun OnDateClickDialog(
    date: LocalDate,
    record: LaundryRecord?,
    onDismiss: () -> Unit,
    onSchedule: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Laundry Details") },
        text = {
            if (record != null) {
                Text("✔️ Laundry Completed\nTime: ${record.time}\nType: ${record.type}")
            } else {
                Text("📅 Schedule Laundry?")
            }
        },
        confirmButton = {
            if (record == null) {
                Button(onClick = onSchedule) {
                    Text("Schedule")
                }
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewOnDateClickDialog() {
    val date = LocalDate.now()
    val record = LaundryRecord(date, "10:30 AM", "Wash & Iron", "Completed")
    OnDateClickDialog(
        date = date,
        record = record,
        onDismiss = {},
        onSchedule = {}
    )
}