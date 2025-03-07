package com.example.teammanagement.screencomponent.teamtaskcomponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teammanagement.Actions
import com.example.teammanagement.dataclass.Task
import com.example.teammanagement.ui.theme.Blue

@Composable
fun GroupOfTask(
    title: String,
    tasks: List<Task>,
    actions: Actions,
    navController : NavHostController
){
    var expanded by remember { mutableStateOf(true) }
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            onClick = { expanded = !expanded }
        ) {
            Text(
                text = title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                contentDescription = "Accordion Icon",
                modifier = Modifier.size(25.dp),
                tint = Color.Black
            )
        }
        if(tasks.isNotEmpty()){
            TextButton(

                onClick = {
                    if(currentRoute == "teams/{teamId}/tasks"){
                        actions.tasksPerStatus(tasks.first().teamId, title, false)
                    }
                    if(currentRoute == "myTask/{memberId}"){
                        actions.tasksPerStatus(tasks.first().teamId, title, true)

                    }

                }
            ) {
                Text(
                    text = "Show more",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W400,
                    color = Blue
                )
            }
        }
    }
    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 0.dp, top = 2.dp, bottom = 2.dp)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ){
            if(tasks.isEmpty())
                Text("No tasks")
            tasks.forEach { task -> TaskCard(task, actions) }
        }
    }
}

