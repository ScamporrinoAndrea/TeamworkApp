package com.example.teammanagement.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.screencomponent.mytaskcomponent.TaskAdvancementBar
import com.example.teammanagement.screencomponent.teamtaskcomponent.GroupOfTask
import com.example.teammanagement.utils.BottomBar
import com.example.teammanagement.utils.BottomPlusButton
import com.example.teammanagement.utils.TopBar
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import com.example.teammanagement.viewmodels.ProfileViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun MyTasksScreen(
    vmProfile: ProfileViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    vmTeams: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    memberId: String,
    actions: Actions,
    navController: NavHostController
) {
    val member by vmTeams.getMemberById(memberId).collectAsState(initial = null)

    val tasks by vmTeams.getTaskByMember(memberId,
        vmTeams.filterCategory,
        vmTeams.filterCreationDate,
        vmTeams.filterDueDate,
        vmTeams.filterName).collectAsState(initial = null)


    tasks?.toMutableList()?.let { vmTeams.setMyTasks(it) }

    Scaffold(
        topBar = {
            TopBar(actions, "My tasks", navController, "", vmProfile, vmTeams)
        },
        bottomBar = {
            member?.let { BottomBar(actions = actions, user = it) }
        },
        floatingActionButton = {
            BottomPlusButton(actions = actions)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(it)
        ){
            tasks?.let { it1 -> TaskAdvancementBar(myTasks = it1) }


            if (tasks.isNullOrEmpty()) {
                Text(text = "No Tasks Found", modifier = Modifier.padding(start = 16.dp))
            } else {
                GroupOfTask(
                    title = "Pending",
                    tasks = tasks!!.filter { task ->  task.status == "Pending" },
                    actions = actions,
                    navController = navController)
                GroupOfTask(
                    title = "In Progress",
                    tasks = tasks!!.filter { task ->  task.status == "In Progress" },
                    actions = actions,
                    navController = navController)
                GroupOfTask(
                    title = "Completed",
                    tasks = tasks!!.filter { task ->  task.status == "Completed" },
                    actions = actions,
                    navController = navController)
            }

        }
    }


}