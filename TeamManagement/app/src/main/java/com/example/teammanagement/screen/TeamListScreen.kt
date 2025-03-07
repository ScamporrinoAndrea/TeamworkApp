package com.example.teammanagement.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
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
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.screencomponent.qrcodecomponent.PopUpQrCode
import com.example.teammanagement.screencomponent.qrcodecomponent.showQrCodePopUp
import com.example.teammanagement.screencomponent.showteamcomponent.SearchBar
import com.example.teammanagement.utils.BottomBar
import com.example.teammanagement.screencomponent.showteamcomponent.TeamCard
import com.example.teammanagement.ui.theme.GrayBackground
import com.example.teammanagement.updateNavigationBar
import com.example.teammanagement.utils.BottomPlusButton
import com.example.teammanagement.utils.TopBar
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel

@Composable
fun ShowTeamDetails(
    vm: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    navController : NavHostController,
    actions: Actions
) {
    BackHandler {
        actions.navigateBack()
        updateNavigationBar(navController)
    }
    val favoriteTeams by vm.getFavoriteTeams(vm.searchText).collectAsState(initial = null)
    val notFavoriteTeams by vm.getNotFavoriteTeams(vm.searchText).collectAsState(initial = null)

    val member by vm.member().collectAsState(initial = null)

    member?.let { m ->
        CreateScaffold(
            actions = actions,
            navController = navController,
            user = m,
        ) { _ ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GrayBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 70.dp, start = 10.dp, end = 10.dp, bottom = 150.dp)
            ) {
                SearchBar()
                Text(
                    modifier = Modifier.padding(start = 10.dp, top = 4.dp, bottom = 4.dp),
                    text = "Favorite",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                if (favoriteTeams.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(start = 10.dp, top = 4.dp, bottom = 4.dp),
                        text = "No favorite teams"
                    )
                }
                else{
                    favoriteTeams?.forEach {
                        val numberOfTasks by vm.getNumberOfTeamTask(it.id).collectAsState(initial = null)
                        TeamCard(it, vm::toggleFavorite, numberOfTasks, actions)
                    }
                }
                Text(
                    modifier = Modifier.padding(start = 10.dp, top = 4.dp, bottom = 4.dp),
                    text = "All Teams",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                if (notFavoriteTeams.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(start = 10.dp, top = 4.dp, bottom = 4.dp),
                        text = "No teams"
                    )
                } else{
                    notFavoriteTeams?.forEach {
                        val numberOfTasks by vm.getNumberOfTeamTask(it.id).collectAsState(initial = null)
                        TeamCard(it, vm::toggleFavorite, numberOfTasks , actions)
                    }
                }

            }
        }
    }
    if (showQrCodePopUp){
        PopUpQrCode()
    }
}

@Composable
private fun CreateScaffold(
    navController: NavHostController,
    actions: Actions,
    user: User,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(actions,"Teams", navController,"")
        },
        bottomBar = {
            BottomBar(actions, user)
        },
        floatingActionButton = {
            BottomPlusButton(actions)
        },
        content = content
    )
}