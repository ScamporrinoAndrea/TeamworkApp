package com.example.teammanagement.screencomponent.profilecomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import com.example.teammanagement.viewmodels.ProfileViewModel

@Composable
fun MainInfo(
    navController: NavHostController,
    vm: ProfileViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    vm2: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    memberId: String,
){
    val member by vm.user.collectAsState()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route


    if(currentRoute == "team/{teamId}/members/{memberId}/profile"){
        val contact by vm2.getContact(memberId).collectAsState(initial = null)
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Text( //Name + Surname
                text = "${contact?.name} ${contact?.surname}",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }else if(currentRoute == "profile"){
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Text( //Name + Surname
                text = "${member?.name} ${member?.surname}",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }


    Spacer(modifier = Modifier.height(8.dp))
    if(currentRoute == "team/{teamId}/members/{memberId}/profile"){
        val contact by vm2.getContact(memberId).collectAsState(initial = null)

        contact?.nickname?.let {
            Text(  //Nickname
                text = it,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
            )
        }
    }else if(currentRoute == "profile"){
        member?.let {
            Text(  //Nickname
                text = it.nickname,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
            )
        }
    }


    Spacer(modifier = Modifier.height(8.dp))
    if(currentRoute == "team/{teamId}/members/{memberId}/profile"){
        val contact by vm2.getContact(memberId).collectAsState(initial = null)

        contact?.email?.let {
            Text(  //Email
                text = it,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp
            )
        }
    }else if(currentRoute == "profile"){
        Text(
            text = member?.email.toString(),
            fontWeight = FontWeight.Light,
            fontSize = 18.sp
        )
    }


    Spacer(modifier = Modifier.height(8.dp))
    if(currentRoute == "team/{teamId}/members/{memberId}/profile"){
        val contact by vm2.getContact(memberId).collectAsState(initial = null)

        Row{
            Icon(imageVector = Icons.Filled.LocationOn,
                contentDescription ="location",
            )
            Spacer(modifier = Modifier.width(2.dp))
            contact?.location?.let {
                Text(
                    text = it
                )
            }
        }
    }else if(currentRoute == "profile"){
        Row{
            Icon(imageVector = Icons.Filled.LocationOn,
                contentDescription ="location",
            )
            Spacer(modifier = Modifier.width(2.dp))
            member?.let {
                Text(
                    text = it.location
                )
            }
        }
    }

}
