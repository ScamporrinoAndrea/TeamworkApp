package com.example.teammanagement.screencomponent.taskdetailscomponents


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
import androidx.compose.ui.graphics.Color
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.Green
import com.example.teammanagement.ui.theme.Orange
import com.example.teammanagement.ui.theme.Red
import com.example.teammanagement.ui.theme.Yellow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownColors(chooseColor : (Color)-> Unit) {
    val colorNames = arrayOf("Blue", "Red", "Orange", "Yellow", "Green")
    val colors = arrayOf(Blue, Red, Orange, Yellow, Green)
    var expanded by remember { mutableStateOf(false) }
    var selectedColorIndex by remember { mutableIntStateOf(0) }

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
                value = colorNames[selectedColorIndex],
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                colors.forEachIndexed { index, _ ->
                    DropdownMenuItem(
                        text = {Text(text = colorNames[index])},
                        onClick = {
                            selectedColorIndex = index
                            expanded = false
                            chooseColor(colors[selectedColorIndex])
                        })
                }
            }
        }
    }
}
