package com.example.teammanagement.screencomponent.profilecomponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import com.example.teammanagement.viewmodels.ProfileViewModel

@Composable
fun Bio(
    vm: ProfileViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    vm2: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    navController: NavHostController,
    memberId: String
){
    val member by vm.user.collectAsState()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route


    Row(
        modifier = Modifier.padding(horizontal = 16.dp)
    ){
        if(currentRoute == "team/{teamId}/members/{memberId}/profile" || currentRoute == "profile") {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "Bio Icon" ,
                modifier = Modifier.size(25.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                "Biography",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        if(currentRoute == "team/{teamId}/members/{memberId}/profile"){
            val contact by vm2.getContact(memberId).collectAsState(initial = null)

            contact?.bio?.let {
                Text(
                    text= it,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }else if(currentRoute == "profile"){
            member?.let {
                Text(
                    text= it.bio,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

    }
}