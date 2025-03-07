package com.example.teammanagement.screencomponent.chatcomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.screencomponent.profilecomponent.monogram
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.utils.ShowPicture
import com.example.teammanagement.viewmodels.ChatViewModel

var showCreateChat by mutableStateOf(false)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateChatPopUp(
    actions: Actions,
    vmChat: ChatViewModel = viewModel (factory = FactoryClass(LocalContext.current)),
){
    val member by vmChat.member().collectAsState(initial = null)
    val members by vmChat.getListOfAllMembersWithYouHaveTeamInCommon().collectAsState(initial = null)
    ModalBottomSheet(
        onDismissRequest = {
            showCreateChat = false
        },
        modifier = Modifier.fillMaxHeight()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Start New Chat With",
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Divider()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
        ) {

            Text(
                "Select the member you want to start a conversation with",
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp)
        ) {
            if(members.isNullOrEmpty()){
                Text(
                    "No members found",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            members?.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShowPicture(
                        imageType = it.profilePictureType,
                        image = it.profilePicture,
                        modifier = Modifier.size(40.dp).clip(CircleShape),
                        monogram = monogram(it.name,it.surname),
                        fontSize = 12
                    )
                    //it.profilePicture(Modifier.size(40.dp), 12)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = if (it.id == member?.id) "${it.name} ${it.surname} (Me)" else "${it.name} ${it.surname}",
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            vmChat.addChat(it.id, actions)
                        },
                        modifier = Modifier.padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(Blue)
                    ) {
                        Text(text = "Add")
                    }

                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }


}
