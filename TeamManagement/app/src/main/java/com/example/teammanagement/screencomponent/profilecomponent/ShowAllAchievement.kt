package com.example.teammanagement.screencomponent.profilecomponent

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.updateNavigationBar
import com.example.teammanagement.utils.BottomBar
import com.example.teammanagement.utils.BottomPlusButton
import com.example.teammanagement.utils.TopBar
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import com.example.teammanagement.viewmodels.ProfileViewModel

@Composable
fun ShowAllAchievements(
    actions: Actions,
    navController: NavHostController,
    vm: ProfileViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    vm2: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    teamId: String,
    memberId: String
    ) {
    BackHandler {
        actions.navigateBack()
        updateNavigationBar(navController)
    }
    val member by vm.user.collectAsState()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    Scaffold(
        topBar = {
            TopBar(actions, "Achievements", navController, "")
        },
        bottomBar = {
            member?.let { BottomBar(actions = actions, user = it) }
        },
        floatingActionButton = {
            BottomPlusButton(actions)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 80.dp)
                .padding(paddingValues)
        ) {
            GeneralAchievements(
                actions = actions , profileView = false,
                navController = navController,
                teamId = "", memberId = "")
            Spacer(modifier = Modifier.height(20.dp))
            if(currentRoute ==  "team/{teamId}/members/{memberId}/profile"){
                val teamMembers by vm2.getTeamMembers(teamId).collectAsState(initial = null)

                if (teamMembers != null) {
                    if (teamMembers!!.isNotEmpty()) {
                        teamMembers!!.forEach {member ->
                            if (member!=null){
                                val teamAchievement by vm.teamAchievement(member.id, memberId).collectAsState(initial = null)
                                Achievements(teamAchievement, member.name, false, actions)
                                Spacer(modifier = Modifier.height(20.dp))
                                }
                        }
                    }
                }
            }else{
                if (member != null) {
                    val memberTeams by vm.getMemberTeams(member!!.id).collectAsState(initial = null)
                    if (memberTeams != null) {
                        if (memberTeams!!.isNotEmpty()) {
                            memberTeams!!.forEach {
                                val teamAchievement by vm.teamAchievement(it.id, member!!.id).collectAsState(initial = null)
                                Achievements(teamAchievement, it.name, false, actions)
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }
                }
            }

        }
    }
}