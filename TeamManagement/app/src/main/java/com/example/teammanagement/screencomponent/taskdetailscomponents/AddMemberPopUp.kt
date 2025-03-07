package com.example.teammanagement.screencomponent.taskdetailscomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.dataclass.History
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.dataclass.Task
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.viewmodels.TaskViewModel
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberPopUp(vm: TaskViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
                   task: Task,
) {

    val taskMembers by vm.getTaskMembers(task.id).collectAsState(initial = emptyList())
    val teamMembers by vm.getTeamMembers(task.teamId).collectAsState(initial = emptyList())

    taskMembers.let { vm.setActualTaskMembers(it) }
    teamMembers.let { vm.setActualTeamMembers(it.filter { t -> taskMembers?.contains(t) == false }) }

    if (showAddMember) {
        ModalBottomSheet(
            modifier = Modifier.height(1000.dp),
            onDismissRequest = {
                showAddMember = false
            },
        )
        {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Members assigned to the task",
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Divider()
            }
            /*Column(
                modifier = Modifier.padding(24.dp)
            ) {
                vm.getTaskMembers(taskId)?.forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.padding(bottom = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            it.profilePicture(Modifier.padding(end = 16.dp))
                            Text(
                                text = it.name + " " + it.surname,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "remove member button",
                                tint = Color.Red
                            )
                        }
                    }
                    Divider(color = Color.LightGray)
                }
            }*/


            DropdownMembers(task, vm.actualTeamMembersVal, vm.actualTaskMembersVal,
                vm::removeTaskMember)

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMembers(

    task: Task,
    actualTeamUsers: MutableList<User?>,
    actualTaskUsers: MutableList<User?>,
    removeTaskMember: (User) -> Unit,
    vm: TaskViewModel = viewModel(factory = FactoryClass(LocalContext.current))

) {


    val member by vm.member().collectAsState(initial = null)
    var expanded by remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.memberscreatetask),
                contentDescription = "members logo",
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier
                    .padding(end = 8.dp, top = 6.dp)
                    .size(35.dp)
            )
            Column(
                modifier = Modifier
                    .padding()
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded })
                {
                    SmallFloatingActionButton(
                        onClick = { /*TODO*/ },
                        shape = CircleShape,
                        containerColor = Blue,
                        modifier = Modifier.menuAnchor(),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Close Icon",
                            tint = White,
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(White)
                    ) {
                        actualTeamUsers.sortedBy { it?.name }.forEach {
                            DropdownMenuItem(
                                onClick = {
                                    if (it != null) {
                                        vm.addTaskMember(it)
                                    }
                                    expanded = false },
                                text = {
                                    if (it != null) {
                                        Text(if (it.id==member?.id) it.name+ " " + it.surname +" (Me)" else it.name+ " " + it.surname)
                                    }
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,)
                        }
                        if (actualTeamUsers.isEmpty()){
                            DropdownMenuItem(
                                onClick = {},
                                text = { Text("No members")},
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,)
                        }

                    }
                }
            }
        }
        Button(
            onClick = {
                vm.addToHistory(
                    History("${member?.name} modified member list", Timestamp.now()),
                    task.id
                )
                vm.updateTaskMembers(task.id)
                showAddMember=false },
            colors = ButtonDefaults.buttonColors(Blue)) {
            Text("Save")
        }
    }
    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        actualTaskUsers.forEach{
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                /*it.profilePicture(
                    Modifier.padding(start=(16).dp).size(30.dp), 12
                )*/
                if (it != null) {
                    Text(it.name + " " + it.surname, modifier=Modifier.padding(start = 10.dp))
                }
                IconButton(onClick = {
                    if (it != null) {
                        removeTaskMember(it)
                    }
                    expanded=true
                    expanded=false
                }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Profile Picture",
                        tint = Color.Red,
                    )
                }
            }
        }
    }
}