package com.example.teammanagement.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.dataclass.Team
import com.example.teammanagement.screencomponent.profilecomponent.monogram
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.utils.ShowPicture
import com.example.teammanagement.viewmodels.CreateTaskViewModel
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import com.google.firebase.Timestamp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    vmTeams: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    vmTask: CreateTaskViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    actions: Actions,
    memberId: String,
    navController: NavHostController
){
    val listOfTeams by vmTeams.getAllTeams().collectAsState(initial = null)
    val member by vmTask.getMemberById(memberId).collectAsState(initial = null)
    val teamMembers by vmTeams.getTeamMembers(vmTask.teamValue.id).collectAsState(initial = null)
    teamMembers?.let { vmTask.setTeamMember(it) }
    listOfTeams?.let { vmTask.setAllTeams(it) }


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
                actions = {
                    TextButton(
                        onClick = {
                            if(vmTask.validate()){
                                member.let {
                                    if (it != null) {
                                        vmTask.insertTask(it)
                                    }
                                }
                                println(vmTask.teamValue.id)
                                vmTask.teamValue.let {
                                    navController.popBackStack()
                                    actions.teamDetails(it.id)
                                    navController.popBackStack()

                                }

                            }
                        }) {
                        Text(text = "SAVE", color = White)
                    }
                },
                title = {
                    Text("New Task")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(it)
        ) {

            listOfTeams?.let { it1 -> DropdownTeams(it1) }


            OutlinedTextField(
                label = { Text("Title") },
                value = vmTask.titleValue,
                onValueChange = vmTask::setTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                isError = vmTask.titleError.isNotBlank(),
            )
            if (vmTask.titleError.isNotBlank()) {
                Text(vmTask.titleError, color = MaterialTheme.colorScheme.error)
            }
            OutlinedTextField(
                label = { Text("Description") },
                placeholder = { Text("Optional") },
                value = vmTask.descriptionValue,
                onValueChange = vmTask::setDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            DatePicker(pickedDate = vmTask.dueDateValue, setDueDate = vmTask::setDueDate)
            DropDownRepeat(vmTask.repeatDateValue, vmTask::setRepeatDate)
            member?.let { it1 ->
                DropdownMembers(vmTask.membersValue, vmTask::addMember, vmTask.teamMembersVal, vmTask::removeMember,
                    it1
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownTeams(listOfTeams: List<Team>,
                  vm: CreateTaskViewModel= viewModel(factory = FactoryClass(LocalContext.current))) {
    var expanded by remember { mutableStateOf(false) }

    var actualTeam by remember { mutableStateOf(vm.teamValue)}

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded })
        {
            OutlinedTextField(
                value = actualTeam.name,
                onValueChange = { },
                label = { Text("Team") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                listOfTeams.forEach {
                    DropdownMenuItem(
                        onClick = {
                            vm.setTeam(it)
                            expanded = false
                            actualTeam = it
                        },
                        text = { Text(it.name) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMembers(
    membersValue: List<User>,
    addMember: (User) -> Unit,
    teamUsers: List<User>?,
    removeMember: (User) -> Unit,
    user: User
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 32.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.memberscreatetask),
            contentDescription = "members logo",
            colorFilter = ColorFilter.tint(Color.Black),
            modifier = Modifier
                .padding(end = 8.dp, top = 12.dp)
                .size(35.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded })
            {
                FloatingActionButton(
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
                    if (teamUsers != null) {
                        teamUsers.sortedBy { it.name }.forEach {
                            DropdownMenuItem(
                                onClick = {
                                    addMember(it)
                                    expanded = false },
                                text = { Text(if (it.id==user.id) it.name+ " " + it.surname +" (Me)" else it.name+ " " + it.surname) },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,)
                        }
                        if (teamUsers.isEmpty()){
                            DropdownMenuItem(
                                onClick = {},
                                text = { Text("No members")},
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,)
                        }
                    }else {
                        DropdownMenuItem(
                            onClick = {},
                            text = { Text("No members")},
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,)
                    }

                }
            }
        }
    }
    Column {
        membersValue.forEach{
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShowPicture(
                    imageType = it.profilePictureType,
                    image = it.profilePicture,
                    modifier = Modifier.padding(start = (16).dp).size(30.dp).clip(CircleShape),
                    monogram = monogram(it.name,it.surname),
                    fontSize = 10)
                /*it.profilePicture(
                    Modifier
                        .padding(start = (16).dp)
                        .size(30.dp), 12
                )*/
                Text(it.name + " " + it.surname, modifier=Modifier.padding(start = 10.dp))
                IconButton(onClick = { removeMember(it) }) {
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

@Composable
fun DatePicker(pickedDate: Timestamp?, setDueDate: KFunction1<Timestamp?, Unit>){
    val dateDialogState = rememberMaterialDialogState()
    OutlinedTextField(
        value = if (pickedDate==null)
            "Select a date"
        else
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(pickedDate.toDate()),
        onValueChange = {  },
        readOnly = true,
        label = { Text("Due Date") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        trailingIcon = {
            if(pickedDate!=null){
                IconButton(onClick = { setDueDate(null) }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Close Icon",
                    )
                }
            }
        },
        leadingIcon = {
            IconButton(onClick = { dateDialogState.show() }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "date Icon"
                )
            }

        }
    )

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok")
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick a date",
            allowedDateValidator = {
                it >= LocalDate.now()
            }
        ) {
            val selDate = it.plusDays(1)
            setDueDate(localDateToTimestamp(selDate,"Europe/Rome"))
        }
    }
}

fun localDateToTimestamp(localDate: LocalDate, timeZone: String): Timestamp {
    val zonedDateTime = localDate.atStartOfDay(ZoneId.of(timeZone))
    val instant = zonedDateTime.toInstant()
    return Timestamp(instant.epochSecond, instant.nano)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownRepeat(repeatDateValue: String, setRepeatDate: (String) -> Unit) {
    val repeat = arrayOf("no repeat","each day", "each week", "each month")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded })
        {
            OutlinedTextField(
                value = repeatDateValue,
                onValueChange = {  },
                label = { Text("Repeat") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                repeat.forEach {
                    DropdownMenuItem(
                        onClick = {
                            setRepeatDate(it)
                            expanded = false
                        },
                        text = { Text(it) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}