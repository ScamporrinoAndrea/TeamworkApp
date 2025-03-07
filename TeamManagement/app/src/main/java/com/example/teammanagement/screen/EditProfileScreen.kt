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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.screencomponent.profilecomponent.EditBio
import com.example.teammanagement.screencomponent.profilecomponent.EditMainInfo
import com.example.teammanagement.screencomponent.profilecomponent.Picture
import com.example.teammanagement.ui.theme.GrayBackground
import com.example.teammanagement.updateNavigationBar
import com.example.teammanagement.utils.Dialog
import com.example.teammanagement.utils.TopBar
import com.example.teammanagement.utils.openDialog
import com.example.teammanagement.viewmodels.ProfileViewModel

@Composable
fun EditProfileColumnLayout(
    navController: NavHostController,
    teamId : String,
    memberId : String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(GrayBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Picture(navController = navController, teamId = teamId, memberId = memberId)
        EditMainInfo()
        Spacer(modifier = Modifier.height(30.dp))
        Column{
            EditBio()
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun EditProfileRowLayout(
    navController: NavHostController,
    teamId : String,
    memberId : String
) {
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
                EditMainInfo()
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Column(modifier = Modifier.padding(16.dp)) {
            EditBio()
        }
    }
}






@Composable
fun EditProfile(
    actions: Actions,
    navController: NavHostController,
    teamId : String,
    memberId : String,
    vm : ProfileViewModel = viewModel(factory = FactoryClass(LocalContext.current))
){
    val member by vm.getMember().collectAsState(initial = null)
    LaunchedEffect(key1 = member, key2 = vm){
        member?.let {
            vm.setName(it.name)
            vm.setBio(it.bio)
            vm.setEmail(it.email)
            vm.setSurname(it.surname)
            vm.setNickname(it.nickname)
            vm.setPosition(it.location)
        }
    }
    BackHandler {
        openDialog = true
    }
    Scaffold(
        topBar = {
            TopBar(actions, "Profile", navController, "")
        },
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (this.maxHeight > this.maxWidth) {  //if I'm in vertical position
                EditProfileColumnLayout(navController = navController, teamId = teamId, memberId = memberId)
            } else {
                EditProfileRowLayout(navController = navController, teamId = teamId, memberId = memberId)
            }
        }
        when {
            openDialog -> {
                Dialog(
                    title = "Go back",
                    body = "Are you sure you want to go back? \n The actual change will not be saved",
                    dismiss = { openDialog = false },
                    confirm = {
                        openDialog = false
                        actions.navigateBack()
                        updateNavigationBar(navController)
                    }
                )
            }
        }
    }
}