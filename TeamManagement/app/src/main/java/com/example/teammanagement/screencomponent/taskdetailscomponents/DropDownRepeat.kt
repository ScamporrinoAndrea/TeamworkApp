package com.example.teammanagement.screencomponent.taskdetailscomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.viewmodels.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownRepeat(vm: TaskViewModel = viewModel(factory = FactoryClass(LocalContext.current))) {
    val repeat = arrayOf("no repeat","each day", "each week", "each month")
    var expanded by remember { mutableStateOf(false) }
    var selectedRepeat by remember { mutableStateOf(repeat[0]) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                label = { Text(text = "Repeat")},
                value = vm.repeatValue,
                onValueChange = {vm.setRepeat(selectedRepeat)},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                repeat.forEachIndexed { _, item ->
                    DropdownMenuItem(
                        text = { Text(text = item )},
                        onClick = {
                            selectedRepeat = item
                            expanded = false
                            vm.setRepeat(selectedRepeat)
                            if(item != "no repeat")vm.setDueDate(null)
                        })
                }
            }
        }
    }
}