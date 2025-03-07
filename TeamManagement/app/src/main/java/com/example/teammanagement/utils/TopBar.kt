package com.example.teammanagement.utils

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.screen.localDateToTimestamp
import com.example.teammanagement.screencomponent.profilecomponent.monogram
import com.example.teammanagement.screencomponent.qrcodecomponent.CameraScreen
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import com.example.teammanagement.viewmodels.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.firebase.Timestamp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.reflect.KFunction1

var showModal by mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TopBar(
    actions: Actions,
    title: String,
    navController: NavHostController,
    teamId: String,
    profileViewModel: ProfileViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    listOfTeamsViewModel: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current))
    ) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    val team by listOfTeamsViewModel.getTeamById(teamId).collectAsState(initial = null)
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var cameraPermission by remember { mutableStateOf("") }

    Column {
        TopAppBar(
            navigationIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if(currentRoute == "teams/{teamId}/tasks"
                        || currentRoute == "profile/edit"
                        || currentRoute == "profile/achievements"
                        || currentRoute == "team/{teamId}/members/{memberId}/profile"
                        || currentRoute == "team/{teamId}/members/{memberId}/achievements"
                        || currentRoute == "teams/{teamId}/tasks/{taskStatus}/{filterByMember}"
                        ){
                        IconButton(
                            onClick = {
                                if(currentRoute == "profile/edit"){
                                    openDialog = true
                                }
                                else{
                                    actions.navigateBack()
                                }
                            }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.arrow_back),
                                contentDescription = "Back",
                                modifier = Modifier.size(28.dp),
                                colorFilter = ColorFilter.tint(Blue)
                            )
                        }
                    }
                    IconButton(
                        onClick = { /* Go to home page */ }
                    ) {
                        if (currentRoute == "teams/{teamId}/tasks"){
                            team?.let {
                                ShowPicture(
                                    imageType = it.imageType,
                                    image = it.image,
                                    modifier = Modifier,
                                    monogram = monogram("",""),
                                    fontSize = 12
                                )
                            }
                            //team.value?.image?.let { it(Modifier) }
                        }
                        else{
                            Image(
                                painter = painterResource(id = R.drawable.networking_collaboration_svgrepo_com),
                                contentDescription = "Logo",
                                modifier = Modifier.size(28.dp),
                                colorFilter = ColorFilter.tint(Blue)
                            )
                        }
                    }
                }


            },
            scrollBehavior = scrollBehavior,
            title = { Text(text= title, modifier = Modifier.padding(4.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = White),
            actions = {
                if(currentRoute == "teams/{teamId}/tasks"){
                    Row{
                        if(listOfTeamsViewModel.isFilter){
                            IconButton(onClick = { listOfTeamsViewModel.filter() }) {
                                Icon(painter = painterResource(id = R.drawable.filter_xmark_svgrepo_com), contentDescription = "filter icon")
                            }
                        }else{
                            IconButton(onClick = {
                                listOfTeamsViewModel.filter()
                                showModal = true
                            }) {
                                Icon(painter = painterResource(id = R.drawable.filter_svgrepo_com), contentDescription = "filter icon")
                            }
                        }

                        Settings(actions,teamId)
                    }
                }
                if( currentRoute == "profile"){
                    IconButton(onClick = {
                        actions.editProfile()
                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
                if(currentRoute == "profile/edit"){
                    Button(
                        onClick = {
                            if (profileViewModel.isValid()){
                                profileViewModel.setMemberProfile()
                                actions.navigateBack()
                            } },
                        colors = ButtonDefaults.buttonColors(Blue)
                    ) {
                        Text("Done")
                    }
                }
                if(currentRoute == "myTask/{memberId}"){
                    Row{
                        if(listOfTeamsViewModel.isFilter){
                            IconButton(onClick = { listOfTeamsViewModel.filter() }) {
                                Icon(painter = painterResource(id = R.drawable.filter_xmark_svgrepo_com), contentDescription = "filter icon")
                            }
                        }else{
                            IconButton(onClick = {
                                listOfTeamsViewModel.filter()
                                showModal = true
                            }) {
                                Icon(painter = painterResource(id = R.drawable.filter_svgrepo_com), contentDescription = "filter icon")
                            }
                        }

                    }
                }
                if(currentRoute == "teams"){
                    IconButton(onClick = {
                        if (cameraPermissionState.status.isGranted) {
                            actions.qrcode()
                        } else if (cameraPermissionState.status.shouldShowRationale) {
                            cameraPermission = "Denied"
                        } else {
                            cameraPermission = "To allow"
                        }
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.qr_code_scan_icon),
                            contentDescription = "Group image",
                            modifier = Modifier.size(28.dp),
                        )
                    }
                    if(cameraPermission == "To allow" || cameraPermission == "Denied"){
                        SideEffect {
                            cameraPermissionState.run { launchPermissionRequest() }
                        }
                        if (cameraPermissionState.status.isGranted) {
                            actions.qrcode()
                        }
                    }
                }
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )


        if(showModal){
            ModalBottomSheet(
                onDismissRequest = {
                    showModal = false
                    listOfTeamsViewModel.filter()
                },
                sheetState = sheetState,
                modifier = Modifier.fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Filter",
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Divider()
                }




                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("Filter By", modifier = Modifier.padding(end = 16.dp))
                    DropDownFilters(setFilter = listOfTeamsViewModel::setFilterBy, filterVal = listOfTeamsViewModel.filterByVal)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                ){
                    if(listOfTeamsViewModel.filterByVal == "category"){
                        OutlinedTextField(
                            value = listOfTeamsViewModel.filterCategory,
                            onValueChange =  { listOfTeamsViewModel.setFilterCategoryVal(it) },
                            label = {Text(text = listOfTeamsViewModel.filterByVal)},
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth()
                        )
                    }
                    if(listOfTeamsViewModel.filterByVal == "due date"){
                        DatePicker2(pickedDate = listOfTeamsViewModel.filterDueDate, setDueDate = listOfTeamsViewModel::setFilterDueDateVal)
                    }
                    if(listOfTeamsViewModel.filterByVal == "creation date"){
                        DatePicker2(pickedDate = listOfTeamsViewModel.filterCreationDate, setDueDate = listOfTeamsViewModel::setFilterCreationDateVal)
                    }
                    if(listOfTeamsViewModel.filterByVal == "repeat"){
                        listOfTeamsViewModel.setFilterCategoryVal("filter by repeat")
                    }
                    if(listOfTeamsViewModel.filterByVal == "name"){
                        OutlinedTextField(
                            value = listOfTeamsViewModel.filterName,
                            onValueChange =  { listOfTeamsViewModel.setFilterNameVal(it) },
                            label = {Text(text = listOfTeamsViewModel.filterByVal)},
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth()
                        )
                    }

                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Button(onClick = {
                        showModal = false
                    }, colors = ButtonDefaults.buttonColors(Blue)) {
                        Text("Add filter")
                    }

                }


            }
        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownFilters(setFilter : (String)->Unit, filterVal : String,     listOfTeamsViewModel: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current))
) {
    val repeat = arrayOf("category", "creation date", "due date","repeat", "name")
    var expanded by remember { mutableStateOf(false) }
    var selectedRepeat by remember { mutableStateOf(repeat[0]) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = filterVal,
                onValueChange = {setFilter(selectedRepeat)},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                repeat.forEachIndexed { _, item ->
                    DropdownMenuItem(
                        text = { Text(text = item )},
                        onClick = {
                            selectedRepeat = item
                            expanded = false
                            setFilter(selectedRepeat)
                            listOfTeamsViewModel.setFilterCategoryVal("")
                        })
                }
            }
        }
    }
}

@Composable
fun DatePicker2(pickedDate: Timestamp?, setDueDate: KFunction1<Timestamp?, Unit>){
    val dateDialogState = rememberMaterialDialogState()
    OutlinedTextField(
        value = if (pickedDate==null)
            "Select a date"
        else
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(pickedDate.toDate()),
        onValueChange = {  },
        readOnly = true,
        label = { Text("") },
        modifier = Modifier
            .fillMaxWidth(),
        trailingIcon = {
            if(pickedDate!=null){
                IconButton(onClick = {
                    setDueDate(null)
                    //setRepeat("no repeat")
                }) {
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
            //allowedDateValidator = {
            //    it >= LocalDate.now()
            //}
        ) {
            setDueDate(localDateToTimestamp(it,"Europe/Rome"))
            //setRepeat("no repeat")
        }
    }
}

var openDialog5 by mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    actions: Actions, teamId: String,
    listOfTeamsViewModel: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current))
){
    var expanded by remember { mutableStateOf(false) }
    val team by listOfTeamsViewModel.getTeamById(teamId).collectAsState(initial = null)
    val member by listOfTeamsViewModel.member().collectAsState(initial = null)
    Column(
        horizontalAlignment = Alignment.End
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded })
        {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "More setting",
                    modifier = Modifier.menuAnchor()
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
                        actions.membersScreen(teamId)
                              },
                    text = { Text("Team members") },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        actions.editTeam(teamId)
                              },
                    text = { Text("Edit team") },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                DropdownMenuItem(
                    onClick = { expanded = false
                              openDialog = true},
                    text = { Text("Leave team", color = Color.Red) },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                if(team?.admins?.contains(member?.id) == true) {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            openDialog5 = true
                        },
                        text = { Text("Delete team", color = Color.Red) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
            when {
                openDialog -> {
                    Dialog(
                        title = "Leave Team",
                        body = "Are you sure you want to leave this team?",
                        dismiss = { openDialog = false },
                        confirm = {
                            openDialog = false
                            listOfTeamsViewModel.leaveTeam(teamId)
                            actions.navigateBack()
                        }
                    )
                }
                openDialog5 -> {
                    Dialog(
                        title = "Delete Team",
                        body = "Are you sure you want to delete this team?",
                        dismiss = { openDialog5 = false },
                        confirm = {
                            openDialog5 = false

                            listOfTeamsViewModel.deleteTeam(teamId)
                            actions.navigateBack()
                        }
                    )
                }
            }
        }
    }
}