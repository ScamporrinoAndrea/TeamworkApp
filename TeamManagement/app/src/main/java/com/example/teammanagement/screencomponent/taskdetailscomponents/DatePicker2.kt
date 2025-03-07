package com.example.teammanagement.screencomponent.taskdetailscomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teammanagement.screen.localDateToTimestamp
import com.google.firebase.Timestamp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.reflect.KFunction1

@Composable
fun DatePicker2(
    pickedDate: Timestamp?,
    setDueDate: KFunction1<Timestamp?, Unit>,
    setRepeat: (String)->Unit,
    dueDate: Timestamp?
){
    val dateDialogState = rememberMaterialDialogState()
    OutlinedTextField(
        value = if (dueDate==null){
            "Select a date"
        }
        else{
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(dueDate.toDate())
        },
        onValueChange = { setDueDate(pickedDate) },
        readOnly = true,
        label = { Text("Due Date") },
        modifier = Modifier
            .fillMaxWidth().padding(horizontal = 16.dp),
        trailingIcon = {
            if(pickedDate!=null){
                IconButton(onClick = {
                    setDueDate(null)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Close Icon",
                    )
                }
            }
        },
        leadingIcon = {
            IconButton(onClick = { dateDialogState.show() }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "date Icon"
                )
            }

        }
    )

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok")
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick a date",
            allowedDateValidator = {
                it >= LocalDate.now()
            }
        ) {
            val selectedDate = it.plusDays(1)
            setDueDate(localDateToTimestamp(selectedDate,"Europe/Rome"))
            setRepeat("no repeat")
        }
    }
}

