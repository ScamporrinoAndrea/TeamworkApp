package com.example.teammanagement.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.teammanagement.Actions
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.screencomponent.profilecomponent.monogram
import com.example.teammanagement.ui.theme.White

var selectedItem by mutableStateOf("Teams")
var isVisible by mutableStateOf(true)
@Composable
fun BottomBar(actions: Actions, user: User) {
    if (isVisible){
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            NavigationBar(
                containerColor = White
            ){
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Teams") },
                    label = { Text("Teams") },
                    selected = selectedItem == "Teams",
                    onClick = {
                        selectedItem = "Teams"
                        actions.teams()
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "Tasks") },
                    label = { Text("My Tasks") },
                    selected = selectedItem == "Tasks",
                    onClick = { selectedItem = "Tasks"
                        actions.myTasks(user.id) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Email, contentDescription = "Chats") },
                    //old icon: Icons.Filled.Email
                    label = { Text("Chats") },
                    selected = selectedItem == "Chats",
                    onClick = {
                        selectedItem = "Chats"
                        actions.chatScreen()
                    }
                )
                NavigationBarItem(
                    icon = {
                        ShowPicture(
                            imageType = user.profilePictureType,
                            image = user.profilePicture,
                            modifier = Modifier.size(30.dp).clip(CircleShape),
                            monogram = monogram(user.name,user.surname),
                            fontSize = 10
                        )
                           /*user.profilePicture(Modifier.size(30.dp), 12)*/
                           },
                    label = { Text("Profile") },
                    selected = selectedItem == "Profile",
                    onClick = {
                        selectedItem = "Profile"
                        actions.profile()
                    }
                )
            }
        }
    }
}






