package com.example.teammanagement.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.dataclass.Chat
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.dataclass.TypeMessage
import com.example.teammanagement.screencomponent.profilecomponent.monogram
import com.example.teammanagement.ui.theme.GrayBackground
import com.example.teammanagement.updateNavigationBar
import com.example.teammanagement.utils.BottomBar
import com.example.teammanagement.utils.BottomPlusButton
import com.example.teammanagement.utils.ShowPicture
import com.example.teammanagement.utils.TopBar
import com.example.teammanagement.viewmodels.ChatViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ChatMenu(
    vm: ChatViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    navController: NavHostController,
    actions: Actions
) {
    BackHandler {
        actions.navigateBack()
        updateNavigationBar(navController)
    }
    val member by vm.member().collectAsState(initial = null)
    val chats by vm.listOfChats().collectAsState(initial = null)
    val teams by vm.listOfTeams().collectAsState(initial = null)
    chats?.let { vm. setListOfChats(it)}
    teams?.let { vm.setListOfTeam(it)}

    member?.let {
        CreateChatScaffold(
            actions = actions,
            navController = navController,
            user = it,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GrayBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 50.dp, bottom = 150.dp)
            ) {
                if (chats?.isEmpty() == true){
                    Text(
                        "No chats found",
                        modifier = Modifier.padding(top = 20.dp,start = 16.dp)
                    )
                }
                else if(chats == null) {
                    Text(
                        "No chats found",
                        modifier = Modifier.padding(top = 20.dp, start = 16.dp)
                    )
                }
                else{
                    ChatMenuContent(actions, vm)
                }

            }
        }
    }
}

@Composable
fun CreateChatScaffold(
    navController: NavHostController,
    actions: Actions,
    user: User,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(actions, "Chats", navController, "")
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

@Composable
fun ChatMenuContent(actions: Actions, vm: ChatViewModel) {
    Column {
        Spacer(modifier = Modifier.height(10.dp))
        ChatList(actions, vm)
    }
}

@Composable
fun ChatList(actions: Actions, vm: ChatViewModel) {
    Column {
        for (chat in vm.chats) {
            ChatItem(chat = chat, actions = actions, vm)
        }
    }
}

@Composable
fun ChatItem(chat: Chat, actions: Actions, vm: ChatViewModel) {
    var title = ""
    val mess : TypeMessage = if (chat.chat.isEmpty()){
        TypeMessage(
            memberID = null,
            message = "New Chat",
            date = Timestamp.now(),
        )
    }
    else{
        vm.chats.find { it.id == chat.id }?.chat?.last()!!
    }
    if (chat.memberID != null){
        val contact by vm.getContact(memberID = chat.memberID!!).collectAsState(initial = null)
        title = "${contact?.name} ${contact?.surname}"
    }
    if (chat.groupID !=null){
        title = vm.teams.find { it.id == chat.groupID }?.name ?: String()
    }
    Row(
        modifier = Modifier
            .clickable {
                actions.chatShow(chat.id)
            }
            .fillMaxWidth()
            .padding(1.dp)
            .background(Color.White)
            .padding(16.dp)
    ) {
        if (chat.memberID != null) {
            val contact by vm.getContact(chat.memberID!!).collectAsState(initial = null)
            contact?.let {
                ShowPicture(
                    imageType = it.profilePictureType,
                    image = it.profilePicture,
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(35.dp)
                        .clip(CircleShape),
                    monogram = monogram(it.name,it.surname),
                    fontSize = 12
                )
            }
            /*vm.getContact(chat.memberID).profilePicture.let {
                it(
                    Modifier.padding(top=2.dp).size(35.dp), 12
                )
            }*/
        }else{
            chat.groupID?.let { groupID ->
                vm.teams.filter { it.id == groupID }.forEach { team ->
                    ShowPicture(
                        imageType = team.imageType,
                        image = team.image,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(35.dp)
                            .clip(CircleShape),
                        monogram = monogram("",""),
                        fontSize = 12
                    )/*
                    team.image(
                        Modifier
                            .padding(top = 2.dp)
                            .size(35.dp)
                            .clip(CircleShape)
                    )*/
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                text = mess.message,
                color = Color.Gray,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(mess.date.toDate()), color = Color.Gray, fontSize = 12.sp)
    }
}