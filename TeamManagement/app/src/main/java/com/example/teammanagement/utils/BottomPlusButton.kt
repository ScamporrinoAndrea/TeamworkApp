package com.example.teammanagement.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.screen.CreateTeamPopUp
import com.example.teammanagement.screen.showCreateTeam
import com.example.teammanagement.screencomponent.chatcomponent.CreateChatPopUp
import com.example.teammanagement.screencomponent.chatcomponent.showCreateChat
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel

var areButtonsVisible by mutableStateOf(false)

@Composable
fun BottomPlusButton(
    actions: Actions,
    vm: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current))

) {
    val member by vm.member().collectAsState(initial = null)


    Column(
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = areButtonsVisible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        actions.newTeam()
                        //showCreateTeam = true
                        areButtonsVisible = false},
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Extended floating action button.") },
                    text = { Text(text = "New Team") },
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                ExtendedFloatingActionButton(
                    onClick = {
                        member?.let { actions.newTask(it.id)}
                        areButtonsVisible = false},
                    icon = { Icon(Icons.Filled.List, contentDescription = "Extended floating action button.") },
                    text = { Text(text = "New Task") },
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                ExtendedFloatingActionButton(
                    onClick = {
                        showCreateChat = true
                        areButtonsVisible = false},
                    icon = { Icon(Icons.Filled.Email, contentDescription = "Extended floating action button.") },
                    text = { Text(text = "New Chat") },
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
        }
        FloatingActionButton(
            onClick = { areButtonsVisible = !areButtonsVisible },
        ) {
            if (!areButtonsVisible)
                Icon(Icons.Filled.Add, contentDescription = "Main button")
            else
                Icon(Icons.Filled.Close, contentDescription = "Main button")
        }
    }

    if (showCreateTeam){
        CreateTeamPopUp(actions)
    }

    if (showCreateChat){
        CreateChatPopUp(actions)
    }
}