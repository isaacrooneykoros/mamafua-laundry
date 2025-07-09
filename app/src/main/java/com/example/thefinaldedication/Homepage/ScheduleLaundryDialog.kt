package com.example.thefinaldedication.Homepage

import android.R.attr.type
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScheduleLaundryDialog(
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedTime = remember { mutableStateOf("") }
    val selectedType = remember { mutableStateOf("") }
    var timeExpanded = remember { mutableStateOf(false) }
    var typeExpanded = remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Schedule Laundry") },
        text = {
            Column {
                Row {
                    Text("Select Time")
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedTextField(
                        value = selectedTime.value,
                        onValueChange = { /* No-op */ },
                        modifier = Modifier.padding(end = 8.dp),
                        readOnly = true,
                        label = { Text("Time") },
                        trailingIcon = {
                            Button(onClick = { }) {
                                Text(if (timeExpanded.value) "Close" else "Open")
                            }
                        }
                    )
                }
                DropdownMenu(
                    expanded = timeExpanded.value,
                    onDismissRequest = { timeExpanded.value = false }
                ) {
                    listOf("10:30 AM", "11:00 AM", "11:30 AM").forEach { time ->
                    DropdownMenuItem(
                       text = { Text(time) },
                        onClick = {
                            selectedType.value = type.toString()
                            typeExpanded.value = false
                        }
                    )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Text("Select Service")
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedTextField(
                        value = selectedType.value,
                        onValueChange = { /* No-op */ },
                        modifier = Modifier.padding(end = 8.dp),
                        readOnly = true,
                        label = { Text("Service") },
                        trailingIcon = {
                            Button(onClick = { }) {
                                Text(if (typeExpanded.value) "Close" else "Open")
                            }
                        }
                    )
                }
                DropdownMenu(
                    expanded = typeExpanded.value,
                    onDismissRequest = { typeExpanded.value = false }
                ) {
                    listOf("Wash Only", "Wash & Iron").forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedType.value = type.toString()
                            typeExpanded.value = false
                        }
                    )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selectedTime.value, selectedType.value) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Preview
@Composable
fun PreviewScheduleLaundryDialog() {
    ScheduleLaundryDialog(
        onConfirm = { time, type -> /* Handle confirmation */ },
        onDismiss = {}
    )
}
