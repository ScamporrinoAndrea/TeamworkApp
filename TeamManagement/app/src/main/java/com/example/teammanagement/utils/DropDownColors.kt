package com.example.teammanagement.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.Green
import com.example.teammanagement.ui.theme.Orange
import com.example.teammanagement.ui.theme.Red
import com.example.teammanagement.ui.theme.Yellow
import com.example.teammanagement.viewmodels.CreateTeamViewModel
import com.example.teammanagement.viewmodels.EditTeamViewModel
import com.example.teammanagement.viewmodels.TaskViewModel
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownColors(
    chooseColor: KFunction1<String, Unit>,
    task: String = "create",
    vmCreate : CreateTeamViewModel = viewModel (factory = FactoryClass(LocalContext.current)),
    vm : TaskViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    vm2 : EditTeamViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    ) {
    val colorNames = arrayOf("Blue", "Red", "Orange", "Yellow", "Green")
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                label = { Text("Color")},
                value = if(task == "task") vm.categoryColorValue else if (task == "edit") vm2.categoryColorValue else vmCreate.categoryColorValue,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                colorNames.forEach{
                    DropdownMenuItem(
                        text = {Text(text = it)},
                        onClick = {
                            expanded = false
                            chooseColor(it)
                        })
                }
            }
        }
    }
}
