package com.example.teammanagement.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.screencomponent.profilecomponent.monogram
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.utils.CategoryTag
import com.example.teammanagement.utils.DropDownColors
import com.example.teammanagement.utils.QRCodeGenerator
import com.example.teammanagement.utils.ShowPicture
import com.example.teammanagement.utils.areButtonsVisible
import com.example.teammanagement.viewmodels.CreateTeamViewModel
import kotlinx.coroutines.launch

var showCreateTeam by mutableStateOf(false)

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamPopUp(
    actions: Actions,
    vmCreate : CreateTeamViewModel = viewModel (factory = FactoryClass(LocalContext.current)),
    ){


    val member by vmCreate.member().collectAsState(initial = null)
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    titleContentColor = White,
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        vmCreate.add()
                        vmCreate.clear()
                        actions.navigateBack()
                        areButtonsVisible = false
                    }) {
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
                            if (vmCreate.validate()) {
                                vmCreate.addTeam()
                                areButtonsVisible = false
                                actions.navigateBack()

                            }
                        }) {
                        Text(text = "SAVE", color = White)
                    }
                },
                title = {
                    Text("New Team")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = vmCreate.teamNameValue,
                onValueChange = vmCreate::setTeamNameVal,
                label = { Text(text = "Team Name") },
                isError = vmCreate.teamNameError.isNotBlank(),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .height(60.dp)
                    .fillMaxWidth()
            )
            if (vmCreate.teamNameError.isNotBlank()) {
                Text(
                    vmCreate.teamNameError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            //Description
            OutlinedTextField(
                value = vmCreate.descriptionValue,
                onValueChange = vmCreate::setDescription,
                label = { Text(text = "Team's Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))


            //Categories
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = White,
                ),
                border = BorderStroke(1.dp, Color.LightGray),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                )
            ) {
                Text(
                    text = "Categories",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    vmCreate.categoryList.forEach {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                ) {

                                    CategoryTag(
                                        borderColor = it.color,
                                        text = it.name
                                    )
                                    FilledIconButton(
                                        onClick = {
                                            vmCreate.removeCategoryList(it)
                                        },
                                        modifier = Modifier
                                            .size(24.dp)
                                            .align(Alignment.CenterEnd)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "delete category",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                    }


                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { vmCreate.edit() },
                        modifier = Modifier.padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(Blue)
                    ) {
                        Text(text = "Add Category")
                    }
                }
            }


            Spacer(modifier = Modifier.height(32.dp))

            //Favorite


            //Members
            member?.let { it1 -> DropdownMembers(vm = vmCreate, user = it1) }


            //DatePicker(pickedDate = vm.dueDateValue, setDueDate = vm::setDueDate)
            //DropDownRepeat(vm.repeatDateValue, vm::setRepeatDate)

            //DropdownMembers(vm.membersValue, vm::addMember, vm.teamMember, vm::removeMember, member)
            if (vmCreate.isEditing) {
                ModalBottomSheet(
                    onDismissRequest = {
                        vmCreate.unEdit()
                        // vm3.validate()
                    },
                    sheetState = sheetState,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                    ) {
                        OutlinedTextField(
                            value = vmCreate.categoryTitleValue,
                            onValueChange = vmCreate::setCategoryTitle,
                            label = { Text(text = "Category Name") },
                            isError = vmCreate.categoryTitleError.isNotBlank(),
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth()
                        )

                        if (vmCreate.categoryTitleError.isNotBlank()) {
                            Text(vmCreate.categoryTitleError, color = MaterialTheme.colorScheme.error)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                    ) {
                        DropDownColors(vmCreate::setCategoryColor)
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    ) {

                        Button(
                            onClick = {
                                if (vmCreate.validateCategory()) {
                                    vmCreate.addCategoryList()
                                    vmCreate.unEdit()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(Blue)) {
                            Text("Done")

                        }
                    }
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMembers(
    vm : CreateTeamViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    user: User
) {

    val allContacts by vm.contactList().collectAsState(initial = null)
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val shareIntent = remember {
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/*"
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.memberscreatetask),
            contentDescription = "members logo",
            colorFilter = ColorFilter.tint(Color.Black),
            modifier = Modifier
                .padding(end = 8.dp, top = 0.dp)
                .size(40.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded })
            {
                FloatingActionButton(
                    onClick = {  },
                    shape = CircleShape,
                    containerColor = Blue,
                    modifier = Modifier
                        .menuAnchor()
                        .size(30.dp),
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
                    DropdownMenuItem(
                        onClick = {
                            vm.dialog2()
                            allContacts?.let { vm.setContactsToAdd(it) }
                            expanded = false
                        },
                        text = { Text("Add from other contacts") },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )

                }
            }
            if(vm.isDialog2){
                ModalBottomSheet(
                    onDismissRequest = vm::dialog2
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
                        if (vm.contactsNotInTheTeam.isNotEmpty()) {
                            vm.contactsNotInTheTeam.sortedBy { it.name }.forEach {
                                if(it != user){
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
                                            text = if (it.id == user.id) "${it.name} ${it.surname} (Me)" else "${it.name} ${it.surname}",
                                            modifier = Modifier.weight(1f)
                                        )
                                        Button(
                                            onClick = {
                                                vm.addMember(it)
                                                allContacts?.let { it1 -> vm.setContactsToAdd(it1) }
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
                            if (vm.contactsNotInTheTeam.isEmpty()){
                                    Text("No members")
                            }
                        }else {
                            Text("No other contacts")
                        }
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Column {
        vm.membersValue.sortedBy { it.name }.forEach{
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(it != user){
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        ShowPicture(
                            imageType = it.profilePictureType,
                            image = it.profilePicture,
                            modifier = Modifier.size(40.dp).clip(CircleShape),
                            monogram = monogram(it.name,it.surname),
                            fontSize = 12
                        )
                        //it.profilePicture(Modifier.size(40.dp), 12)
                        Text(it.name + " " + it.surname, modifier= Modifier.padding(start = 10.dp))
                    }
                    IconButton(onClick = { vm.removeMember(it) }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Profile Picture",
                        )
                    }
                }
            }
            if(it != user) {
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    }
}