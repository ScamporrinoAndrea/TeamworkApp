package com.example.teammanagement.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.screencomponent.profilecomponent.Bio
import com.example.teammanagement.screencomponent.profilecomponent.GeneralAchievements
import com.example.teammanagement.screencomponent.profilecomponent.MainInfo
import com.example.teammanagement.screencomponent.profilecomponent.Picture
import com.example.teammanagement.screencomponent.profilecomponent.Settings
import com.example.teammanagement.ui.theme.GrayBackground
import com.example.teammanagement.updateNavigationBar
import com.example.teammanagement.utils.BottomBar
import com.example.teammanagement.utils.BottomPlusButton
import com.example.teammanagement.utils.TopBar
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import com.example.teammanagement.viewmodels.ProfileViewModel

@Composable
fun ProfileColumnLayout(
    actions: Actions,
    navController: NavHostController,
    teamId: String,
    memberId: String,
    onSignOut: () -> Unit
) {

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(GrayBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Picture(navController = navController, teamId = teamId, memberId = memberId)
        MainInfo(navController = navController, memberId = memberId)
        Spacer(modifier = Modifier.height(30.dp))
        Column{
            Bio(navController = navController, memberId = memberId)
            Spacer(modifier = Modifier.height(30.dp))
            GeneralAchievements(actions, profileView = true, navController = navController,
                teamId = teamId, memberId = memberId)
            Spacer(modifier = Modifier.height(30.dp))
            if(currentRoute == "profile"){
                Settings(onSignOut)
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ProfileRowLayout(
    actions: Actions,
    navController: NavHostController,
    teamId: String,
    memberId: String,
    onSignOut: () -> Unit

) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment =  Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Picture(navController = navController, teamId = teamId, memberId = memberId)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize(),
            ) {
                MainInfo(navController = navController, memberId = memberId)
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Column(modifier = Modifier.padding(16.dp)) {
            Bio(navController = navController, memberId = memberId)
            Spacer(modifier = Modifier.height(30.dp))
            GeneralAchievements(actions, profileView = true, navController = navController,
                teamId = teamId, memberId = memberId)
            Spacer(modifier = Modifier.height(30.dp))
            if(currentRoute == "profile"){
                Settings(onSignOut)
            }
        }
    }
}






@Composable
fun Profile(
    teamId: String,
    memberId: String,
    vm: ProfileViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    vm2: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    actions: Actions,
    navController: NavHostController,
    onSignOut: () -> Unit
){
    BackHandler {
        actions.navigateBack()
        updateNavigationBar(navController)
    }
    val member by vm.user.collectAsState()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    Scaffold(
        topBar = {
            if(currentRoute == "team/{teamId}/members/{memberId}/profile"){
                val contact by vm2.getContact(memberId).collectAsState(initial = null)
                TopBar(actions, "${contact?.name}'s Profile", navController, "")

            }else if(currentRoute == "profile"){
                TopBar(actions, "Profile", navController, "")

            }
        },
        bottomBar = {
            member?.let { BottomBar(actions = actions, user = it) }
        },
        floatingActionButton = {
            BottomPlusButton(actions)
        }
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (this.maxHeight > this.maxWidth) {  //if I'm in vertical position
                ProfileColumnLayout(actions,navController,teamId,memberId,onSignOut)
            } else {
                ProfileRowLayout(actions,navController,teamId,memberId,onSignOut)
            }
        }
    }
}