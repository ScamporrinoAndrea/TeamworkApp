package com.example.teammanagement.screencomponent.qrcodecomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.Red
import com.example.teammanagement.utils.ShowPicture
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel

var showQrCodePopUp by mutableStateOf(false)
var newTeamIdQrCode by mutableStateOf("")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUpQrCode(
    vm: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    )
{
    val team by vm.getTeamById(newTeamIdQrCode).collectAsState(initial = null)
    val member by vm.getMember().collectAsState(initial = null)
    if (showQrCodePopUp) {
        ModalBottomSheet(
            onDismissRequest = {
                showQrCodePopUp = false
            },
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Join new Team",
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                team?.let {
                    ShowPicture(
                        imageType = it.imageType,
                        image = it.image,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(shape = CircleShape),
                        monogram = "",
                        fontSize = 0
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        it.name,
                        modifier = Modifier.padding(top = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (team?.memberIds?.contains(member?.id) != true) {
                        OutlinedButton(
                            onClick = { showQrCodePopUp = false },
                        ) {
                            Text("Cancel", color = Red)
                        }
                        Button(
                            onClick = {
                                vm.addTeamMember(newTeamIdQrCode)
                                showQrCodePopUp = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                Blue
                            )
                        ) {
                            Text("Join")
                        }
                    }
                    else{
                        Text("You are already a member of the team")
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }

        }
    }
}