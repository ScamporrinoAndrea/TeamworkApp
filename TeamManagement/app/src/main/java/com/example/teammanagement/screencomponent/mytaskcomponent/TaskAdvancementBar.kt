package com.example.teammanagement.screencomponent.mytaskcomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.dataclass.Task
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel

@Composable
fun TaskAdvancementBar(vm: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
                       myTasks: List<Task>){


    val totalTasks = myTasks.count()
    val totalTasksCompleted = myTasks.count { it.status == "Completed" }
    val progress: Float = totalTasksCompleted.toFloat() / totalTasks.toFloat()

    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Completed ${totalTasksCompleted}/${totalTasks}",
            fontWeight = FontWeight.Light,
            fontSize = 14.sp
        )
        LinearProgressIndicator(
            progress = progress,
            color = Blue,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
    }
}