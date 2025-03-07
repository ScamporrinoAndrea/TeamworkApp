package com.example.teammanagement.screencomponent.profilecomponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.dataclass.MemberAchievement
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import com.example.teammanagement.viewmodels.ProfileViewModel

@Composable
fun Achievements(generalAchievement: MemberAchievement?, title: String, profileView: Boolean, actions: Actions) {
    if(generalAchievement != null){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row{
                Image(
                    painter = painterResource(id = R.drawable.logoachievement),
                    contentDescription = "Logo achievements",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,)
            }
            if(profileView){
                TextButton(onClick = {
                    actions.profileAchievement()
                },
                    modifier = Modifier.height(31.dp)
                ) {
                    Text("Show more", color = Blue)
                }
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = White,
            ),
            border = BorderStroke(1.dp, Color.LightGray),
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                Text("Task assigned completed")
                Text("${generalAchievement.totalTasksCompleted}/${generalAchievement.totalTasksAssigned}", color = Color.Gray)
            }
                LinearProgressIndicator(
                    progress = generalAchievement.totalTasksCompleted.toFloat() / generalAchievement.totalTasksAssigned.toFloat(),
                    color = Blue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                )

            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.medal3),
                        contentDescription = "Medal1",
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                            if(generalAchievement.totalTasksCompleted<10){
                                setToSaturation(0f)
                            }
                        })
                    )
                    Text("Complete 10 tasks", textAlign = TextAlign.Center)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.medal2),
                        contentDescription = "Medal2",
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                            if(generalAchievement.totalTasksCompleted<50){
                                setToSaturation(0f)
                            }
                        })

                    )
                    Text("Complete 50 tasks", textAlign = TextAlign.Center)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.medal1),
                        contentDescription = "Medal3",
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                            if(generalAchievement.totalTasksCompleted < 100){
                                setToSaturation(0f)
                            }
                        })
                    )
                    Text("Complete 100 Tasks", textAlign = TextAlign.Center)
                }
            }
            if(title == "General achievements"){
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                ){
                    Text("Number of teams you belong to: ")
                    Text(generalAchievement.totalTeams.toString(), color = Color.Gray)
                }
            }

        }
    }
}

@Composable
fun GeneralAchievements(
    actions: Actions,
    vm: ProfileViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    profileView: Boolean,
    vm2: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    navController: NavHostController,
    teamId: String,
    memberId: String
){
    val member by vm.user.collectAsState()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route


    if(currentRoute == "team/{teamId}/members/{memberId}/profile"){
        val teamAchievement by vm.teamAchievement(teamId, memberId).collectAsState(initial = null)
        val team by vm2.getTeamById(teamId).collectAsState(initial = null)
        Achievements(teamAchievement,"${team?.name}'s Achievements" , false, actions)
    }else if(currentRoute == "profile"){
        if(member != null) {
            val generalAchievement by vm.generalAchievement(member!!.id).collectAsState(null)
            Achievements(
                title = "General achievements",
                generalAchievement = generalAchievement,
                profileView = profileView,
                actions = actions,
            )
        }
    }


}