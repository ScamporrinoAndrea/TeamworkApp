package com.example.teammanagement.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

var openDialog by mutableStateOf(false)
var openDialog2 by mutableStateOf(false)
@Composable
fun Dialog(
    title: String,
    body: String,
    dismiss: () -> Unit,
    confirm: () -> Unit
) {
    AlertDialog(
        title = {
            Text(title)
        },
        text = {
            Text(body)
        },
        onDismissRequest = {
            dismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    confirm()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    dismiss()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}
