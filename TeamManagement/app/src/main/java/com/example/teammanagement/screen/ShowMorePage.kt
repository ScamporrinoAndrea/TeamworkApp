package com.example.teammanagement.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.screencomponent.showmorepagecomponent.TaskCardShowMore
import com.example.teammanagement.updateNavigationBar
import com.example.teammanagement.utils.BottomBar
import com.example.teammanagement.utils.BottomPlusButton
import com.example.teammanagement.utils.TopBar
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel



@Composable
fun ShowMorePage(
    listOfTeamsViewModel : ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    taskStatus : String,
    actions : Actions,
    navController : NavHostController,
    teamId: String,
    filterByMember : Boolean
){
    BackHandler {
        actions.navigateBack()
        updateNavigationBar(navController)
    }
    val tasksByStatus by listOfTeamsViewModel
        .getTasksOfTeamByStatus(teamId, taskStatus).collectAsState(initial = null)
    val team by listOfTeamsViewModel.getTeamById(teamId).collectAsState(initial = null)
    val member by listOfTeamsViewModel.member().collectAsState(initial = null)



    Scaffold(
        topBar = {
            team?.let { TopBar(actions, taskStatus, navController, teamId) }
        },
        bottomBar = {
            member?.let { BottomBar(actions = actions, user = it) }
        },
        floatingActionButton = {
            BottomPlusButton(actions)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(tasksByStatus?.isEmpty() == true)
                Text("No tasks")
            if(!filterByMember){
                tasksByStatus?.forEach { task -> TaskCardShowMore(task = task, actions = actions) }

            }
            else{
                tasksByStatus?.forEach { task ->
                    if(task.memberIds.contains(member?.id)){
                        TaskCardShowMore(task = task, actions = actions)
                    }

                }

            }

        }
    }
}


