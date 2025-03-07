package com.example.teammanagement.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.dataclass.Team
import com.example.teammanagement.screencomponent.teamtaskcomponent.GroupOfTask
import com.example.teammanagement.screencomponent.teamtaskcomponent.InfoTeam
import com.example.teammanagement.screencomponent.teamtaskcomponent.SearchBarTask
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.updateNavigationBar
import com.example.teammanagement.utils.BottomBar
import com.example.teammanagement.utils.BottomPlusButton
import com.example.teammanagement.utils.TopBar
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import kotlinx.coroutines.flow.first

@Composable
fun TeamTasksScreen(
    listOfTeamsViewModel: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    teamId: String,
    navController: NavHostController,
    actions: Actions
){
    BackHandler {
        actions.navigateBack()
        updateNavigationBar(navController)
    }
    val member by listOfTeamsViewModel.member().collectAsState(initial = null)
    val tasksOfTheTeam by listOfTeamsViewModel.getTasksOfTeam(teamId,listOfTeamsViewModel.filterCategory,
        listOfTeamsViewModel.filterCreationDate,
        listOfTeamsViewModel.filterDueDate,
        listOfTeamsViewModel.filterName,
        listOfTeamsViewModel.searchTextTask).collectAsState(initial = null)
    //val team by listOfTeamsViewModel.getTeamById(teamId).collectAsState(initial = null)

    var team by remember { mutableStateOf<Team?>(null) }
    //var tasksOfTheTeam by remember { mutableStateOf<List<Task>>(mutableListOf()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Launch coroutine to fetch task
    LaunchedEffect(teamId) {
        isLoading = true
        errorMessage = null
        try {
            val t = listOfTeamsViewModel.getTeamById(teamId)
            val toff = listOfTeamsViewModel.getTasksOfTeam(
                teamId, listOfTeamsViewModel.filterCategory,
                listOfTeamsViewModel.filterCreationDate,
                listOfTeamsViewModel.filterDueDate,
                listOfTeamsViewModel.filterName,
                listOfTeamsViewModel.searchTextTask
            )
            team = t.first()
            //tasksOfTheTeam = toff.first()
        } catch (e: Exception) {
            errorMessage = "Failed to fetch task"
            Log.e("ShowTaskDetails", "Error fetching task", e)
        } finally {
            isLoading = false
        }
    }


    Scaffold(
        topBar = {
            team?.let { TopBar(actions, it.name, navController,teamId) }
        },
        bottomBar = {
            member?.let { BottomBar(actions = actions, user = it) }
        },
        floatingActionButton = {
            BottomPlusButton(actions)
        }
    ) { paddingValues ->
        if(isLoading){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Blue )
            }
        }else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(bottom = 80.dp)
            ) {
                team?.let { InfoTeam(it) }
                Text(
                    "Tasks",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 0.dp, start = 16.dp, end = 16.dp))
                SearchBarTask()
                if (tasksOfTheTeam.isNullOrEmpty()) {
                    Text(text = "No Tasks Found", modifier = Modifier.padding(start = 16.dp))
                } else {
                    GroupOfTask(
                        title = "Pending",
                        tasks = tasksOfTheTeam!!.filter { task ->  task.status == "Pending" },
                        actions = actions,
                        navController = navController)
                    GroupOfTask(
                        title = "In Progress",
                        tasks = tasksOfTheTeam!!.filter { task ->  task.status == "In Progress" },
                        actions = actions,
                        navController = navController)
                    GroupOfTask(
                        title = "Completed",
                        tasks = tasksOfTheTeam!!.filter { task ->  task.status == "Completed" },
                        actions = actions,
                        navController = navController)
                }
            }
        }

    }
}