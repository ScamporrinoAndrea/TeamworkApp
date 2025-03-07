package com.example.teammanagement.screen

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavHostController
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.screencomponent.profilecomponent.monogram
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.updateNavigationBar
import com.example.teammanagement.utils.QRCodeGenerator
import com.example.teammanagement.utils.ShowPicture
import com.example.teammanagement.utils.openDialog
import com.example.teammanagement.utils.openDialog2
import com.example.teammanagement.utils.selectedItem
import com.example.teammanagement.viewmodels.EditTeamViewModel
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel

var openDialog3 by mutableStateOf(false)
var openDialog4 by mutableStateOf(false)
var userAdmin: User? by mutableStateOf(null)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamMembersScreen(
    vm: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    vm2: EditTeamViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    teamId: String,
    navController: NavHostController,
    actions: Actions
) {
    BackHandler {
        actions.navigateBack()
        updateNavigationBar(navController)
    }
    val teamMembers by vm.getTeamMembers(teamId).collectAsState(initial = null)
    val team by vm.getTeamById(teamId).collectAsState(initial = null)
    var expanded by remember { mutableStateOf(false) }
    val allContacts by vm2.contactList.collectAsState(initial = emptyList())
    val member by vm.member().collectAsState(initial = null)
    val context = LocalContext.current
    val shareIntent = remember {
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/*"
        }
    }

    LaunchedEffect(teamMembers) {
        teamMembers?.let { vm2.setTeamMembers(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    titleContentColor = White,
                ),
                navigationIcon = {
                    IconButton(onClick = { actions.navigateBack() }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close Icon",
                            tint = White
                        )
                    }
                },
                title = {
                    Text("${team?.name}'s Members")
                }
            )
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(it)
                .verticalScroll(rememberScrollState()),
        ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = White,
                    )
                ){
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded })
                        {
                            FloatingActionButton(
                                onClick = { },
                                shape = CircleShape,
                                containerColor = Blue,
                                modifier = Modifier
                                    .menuAnchor()
                                    .size(40.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add Icon",
                                    tint = White,
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(White)
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        vm2.dialog()
                                    },
                                    text = { Text("Invite via QR") },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        vm2.dialog2()
                                        vm2.setContactsToAdd(allContacts)
                                    },
                                    text = { Text("Add from other contacts") },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )

                            }

                        }
                        if(vm2.isDialog){
                            ModalBottomSheet(
                                onDismissRequest = vm2::dialog
                            ){
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Invite via QR Code",
                                        modifier = Modifier.padding(bottom = 8.dp),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                    Divider()
                                }
                                Spacer(modifier = Modifier.height(32.dp))
                                Column(
                                        modifier = Modifier.padding(bottom = 80.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ){
                                        QRCodeGenerator(text = teamId)
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,
                                        ) {
                                            Button(
                                                onClick = {
                                                    context.startActivity(Intent.createChooser(shareIntent, "Share via..."))
                                                },
                                                modifier = Modifier.padding(16.dp),
                                                colors = ButtonDefaults.buttonColors(Blue)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Share,
                                                    contentDescription = "share button",
                                                    tint = White,
                                                )
                                                Text(text = "Share", modifier = Modifier.padding(start = 16.dp), color = White)
                                            }

                                        }
                                    }
                            }
                        }
                        if(vm2.isDialog2){
                            ModalBottomSheet(
                                onDismissRequest = vm2::dialog2
                            ){
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Add from other contacts",
                                        modifier = Modifier.padding(bottom = 8.dp),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                    Divider()
                                }
                                Spacer(modifier = Modifier.height(32.dp))
                                Column(
                                    modifier = Modifier.padding(bottom = 80.dp, start = 16.dp, end = 16.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ){
                                        if (vm2.contactsNotInTheTeam.isNotEmpty()) {
                                            vm2.contactsNotInTheTeam.sortedBy { it.name }.forEach {
                                                if(it != member){
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        ShowPicture(
                                                            imageType = it.profilePictureType,
                                                            image = it.profilePicture,
                                                            modifier = Modifier.size(40.dp).clip(
                                                                CircleShape),
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
                                                                vm2.addMember(it)
                                                                vm2.updateTeamMembers(teamId)
                                                                vm2.setContactsToAdd(allContacts)
                                                                expanded = false
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
                                            if (vm2.contactsNotInTheTeam.isEmpty()){
                                                DropdownMenuItem(
                                                    onClick = {},
                                                    text = { Text("No members")},
                                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,)
                                            }
                                        }else {
                                            DropdownMenuItem(
                                                onClick = {},
                                                text = { Text("No other contacts")},
                                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,)
                                        }
                                    }

                                }
                        }


                        Text(text = "Add members", modifier = Modifier.padding(horizontal = 20.dp))

                    }
                    Divider(color = Color.LightGray, thickness = 1.dp)

                    vm2.membersValue.sortedBy { member ->  member.name }.forEach{
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            var expanded2 by remember { mutableStateOf(false) }
                            ShowPicture(
                                imageType = it.profilePictureType,
                                image = it.profilePicture,
                                modifier = Modifier.size(40.dp).clip(CircleShape),
                                monogram = monogram(it.name,it.surname),
                                fontSize = 10
                            )
                            //it.profilePicture(Modifier.size(40.dp), 12)
                            Spacer(modifier = Modifier.width(8.dp))
                            if(member?.name == it.name){
                                Column{
                                    Text(it.name+  " " + it.surname+" (me)", modifier=Modifier.padding(start = 10.dp))
                                    if (team?.admins?.contains(it.id)==true){
                                        Text(
                                            "Admin",
                                            modifier=Modifier.padding(start = 10.dp),
                                            fontWeight = FontWeight.Light,
                                            fontSize = 13.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }else{
                                Column{
                                    Text(it.name + " " + it.surname, modifier=Modifier.padding(start = 10.dp))
                                    if (team?.admins?.contains(it.id)==true) {
                                        Text(
                                            "Admin",
                                            modifier = Modifier.padding(start = 10.dp),
                                            fontWeight = FontWeight.Light,
                                            fontSize = 13.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            ExposedDropdownMenuBox(
                                expanded = expanded2,
                                onExpandedChange = { expanded2 = !expanded2 })
                            {
                                IconButton(onClick = {  }) {
                                    Icon(
                                        Icons.Filled.MoreVert,
                                        contentDescription = "More setting",
                                        modifier = Modifier.menuAnchor()
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded2,
                                    onDismissRequest = { expanded2 = false },
                                    modifier = Modifier.background(White)
                                ) {
                                    if(it.name == member?.name){
                                        DropdownMenuItem(
                                            onClick = {
                                                expanded2 = false
                                                actions.profile()
                                                selectedItem = "Profile"
                                            },
                                            text = { Text("Show profile") },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                    }
                                    else {
                                        DropdownMenuItem(
                                            onClick = {
                                                expanded2 = false
                                                actions.otherMemberProfile(teamId,it.id)
                                            },
                                            text = { Text("Show profile") },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                    }

                                    if(it.name == member?.name){
                                        DropdownMenuItem(
                                            onClick = {
                                                expanded2 = false
                                                openDialog = true
                                            },
                                            text = { Text("Leave team") },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                    }else if(team?.admins?.contains(member?.id)==true){
                                        DropdownMenuItem(
                                            onClick = {
                                                expanded2 = false
                                                vm2.setMemberToRemoveVal(it)
                                                openDialog2 = true
                                            },
                                            text = { Text("Remove member") },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                        if (team?.admins?.contains(it.id)!=true) {
                                            DropdownMenuItem(
                                                onClick = {
                                                    expanded2 = false
                                                    userAdmin = it
                                                    openDialog3 = true
                                                },
                                                text = { Text("Make admin") },
                                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                                modifier = Modifier.padding(horizontal = 16.dp)
                                            )
                                        } else{
                                            DropdownMenuItem(
                                                onClick = {
                                                    expanded2 = false
                                                    userAdmin = it
                                                    openDialog4 = true },
                                                text = { Text("Remove admin") },
                                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                                modifier = Modifier.padding(horizontal = 16.dp)
                                            )
                                        }
                                    }



                                }


                            }

                        }

                        Divider(color = Color.LightGray, thickness = 1.dp)
                    }
                    when {
                        openDialog -> {

                                com.example.teammanagement.utils.Dialog(
                                    title = "Leave team",
                                    body = "Are you sure you want to leave this team?",
                                    dismiss = { openDialog = false },
                                    confirm = {
                                        openDialog = false
                                        vm.leaveTeam(teamId)
                                        actions.teams()
                                    }
                                )


                        }
                        openDialog2 -> {
                            com.example.teammanagement.utils.Dialog(
                                title = "Remove team Member",
                                body = "Are you sure you want to remove this member?",
                                dismiss = { openDialog2 = false },
                                confirm = {
                                    openDialog2 = false
                                    vm2.removeMember(vm2.userToRemove)
                                    vm2.updateTeamMembers(teamId)
                                }
                            )
                        }
                        openDialog3 -> {
                            com.example.teammanagement.utils.Dialog(
                                title = "Make Admin Team Member",
                                body = "Are you sure you want to make admin ${userAdmin?.name + " " + userAdmin?.surname}?",
                                dismiss = { openDialog3 = false },
                                confirm = {
                                    openDialog3 = false
                                    userAdmin?.id?.let { it1 -> vm.makeAdmin(teamId, it1) }
                                }
                            )
                        }
                        openDialog4 -> {
                            com.example.teammanagement.utils.Dialog(
                                title = "Remove Team Member from Admin role ",
                                body = "Are you sure you want to remove ${userAdmin?.name + " " + userAdmin?.surname} from admin role ?",
                                dismiss = { openDialog4 = false },
                                confirm = {
                                    openDialog4 = false
                                    userAdmin?.id?.let { it1 -> vm.removeAdmin(teamId, it1) }
                                }
                            )
                        }
                    }

            }
        }
    }
}



