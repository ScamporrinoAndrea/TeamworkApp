package com.example.teammanagement.screen

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavHostController
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.screencomponent.profilecomponent.ChangePicturePopUp
import com.example.teammanagement.screencomponent.profilecomponent.monogram
import com.example.teammanagement.screencomponent.profilecomponent.showPicturePopUp
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.BlueVeryTrasp
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.updateNavigationBar
import com.example.teammanagement.utils.CategoryTag
import com.example.teammanagement.utils.DropDownColors
import com.example.teammanagement.utils.QRCodeGenerator
import com.example.teammanagement.utils.ShowPicture
import com.example.teammanagement.viewmodels.CreateTeamViewModel
import com.example.teammanagement.viewmodels.EditTeamViewModel
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTeamScreen(
    vm: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    vm2: EditTeamViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    teamId: String,
    actions: Actions,
    navController: NavHostController
){
    BackHandler {
        actions.navigateBack()
        updateNavigationBar(navController)
    }
    val context = LocalContext.current
    val team by vm.getTeamById(teamId).collectAsState(initial = null)
    val member by vm.member().collectAsState(initial = null)
    val teamMember by vm2.getTeamMembers(teamId).collectAsState(initial = null)
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = team, key2 = teamMember){
        team?.let {
            vm2.setTitle(it.name)
            if(it.description == "No description"){
                vm2.setDescription("")
            }else{
                vm2.setDescription(it.description)
            }
            teamMember?.let { it1 -> vm2.setTeamMembers(it1) }
            it.category.let { it1 -> vm2.setTeamCategoriesList(it1) }
        }
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                val bao = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao)
                val imageData: ByteArray = bao.toByteArray()
                val encodedString = Base64.encodeToString(imageData, Base64.DEFAULT)
                vm.setPictureTeam(encodedString,"bitmap", teamId)
                /*vm.setPictureTeam(teamId){ modifier ->
                    AsyncImage(
                        model = uri,
                        contentDescription = "Team picture",
                        contentScale = ContentScale.Crop,
                        modifier = modifier,
                        alignment = Alignment.Center
                    )
                }*/
            }
        }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview(),
    ) { newImage ->
        if (newImage != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            newImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)
            vm.setPictureTeam(encodedString,"bitmap", teamId)
            /*vm.setPictureTeam(teamId) { modifier ->
                Image(
                    bitmap = newImage.asImageBitmap(),
                    contentDescription = "Team picture",
                    contentScale = ContentScale.Crop,
                    modifier = modifier,
                    alignment = Alignment.Center
                )
            }*/
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        }
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
                actions = {
                    TextButton(
                        onClick = {
                            if(vm2.validate()){
                                team?.let { vm2.updateTeam(teamId, it) }
                                actions.navigateBack()
                            }
                        }) {
                        Text(text = "SAVE", color = White)
                    }
                },
                title = {
                    Text("Edit ${team?.name}'s team")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Box{
                    team?.let { it1 ->
                        ShowPicture(
                            image = it1.image,
                            imageType = it1.imageType,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            monogram = "",
                            fontSize = 10
                        ) }
                    /*team?.image?.let { it1 -> it1(
                        Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))) }
                     */
                    FilledIconButton(
                        onClick = { showPicturePopUp = true },
                        modifier = Modifier.align(Alignment.Center).size(40.dp),
                        shape = CircleShape,
                        colors =  IconButtonDefaults.filledIconButtonColors(BlueVeryTrasp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Icon"
                        )
                    }
                }
            }

            // Title
            OutlinedTextField(
                value = vm2.titleValue,
                onValueChange = vm2::setTitle,
                label = { Text(text = "Team's Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                isError = vm2.titleError.isNotBlank()
            )
            if (vm2.titleError.isNotBlank()) {
                Text(vm2.titleError, color = MaterialTheme.colorScheme.error)
            }


            //Description
            OutlinedTextField(
                value = vm2.descriptionValue,
                onValueChange = vm2::setDescription,
                label = { Text(text = "Team's Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Categories
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                    vm2.categoryList.forEach{
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
                                            vm2.removeCategoryList(it)
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
                        onClick = { vm2.edit() },
                        modifier = Modifier.padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(Blue)
                    ) {
                        Text(text = "Add Category")
                    }
                }
            }


            //Members
            member?.let { it1 -> DropdownMembers(vm = vm2, user = it1, teamId = teamId) }



            //DatePicker(pickedDate = vm.dueDateValue, setDueDate = vm::setDueDate)
            //DropDownRepeat(vm.repeatDateValue, vm::setRepeatDate)

            //DropdownMembers(vm.membersValue, vm::addMember, vm.teamMember, vm::removeMember, member)
            if(vm2.isEditing){
                ModalBottomSheet(
                    onDismissRequest = {
                        vm2.unEdit()
                    },
                    sheetState = sheetState,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                    ){
                        OutlinedTextField(
                            value = vm2.categoryTitleValue,
                            onValueChange =  vm2::setCategoryTitle,
                            label = {Text(text = "Category Name")},
                            isError = vm2.categoryTitleError.isNotBlank(),
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth()
                        )
                        if(vm2.categoryTitleError.isNotBlank()){
                            Text(vm2.categoryTitleError, color = MaterialTheme.colorScheme.error)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                    ){
                        DropDownColors(vm2::setCategoryColor,"edit")
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth().padding(top = 16.dp)
                    ){
                        Button(onClick = {
                            vm2.validateCategory()
                            if(vm2.validateCategory()){
                                vm2.addCategoryList()
                                vm2.unEdit()
                            }
                        }, colors = ButtonDefaults.buttonColors(Blue)) {
                            Text("Done")

                        }
                    }
                }
            }
        }

    }
    if (showPicturePopUp) {
        ChangePicturePopUp(
            galleryLauncher,
            cameraLauncher,
            permissionLauncher,
            null,
            team
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMembers(
    vm : EditTeamViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    user: User,
    teamId: String
) {

    val allContacts by vm.contactList.collectAsState(initial = emptyList())
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
                            vm.dialog()
                            expanded = false
                        },
                        text = { Text("Invite via QR") },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                    DropdownMenuItem(
                        onClick = {
                            vm.dialog2()
                            vm.setContactsToAdd(allContacts)
                            expanded = false
                        },
                        text = { Text("Add from other contacts") },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )

                }
            }
            if(vm.isDialog){
                ModalBottomSheet(
                    onDismissRequest = vm::dialog
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
                                                vm.setContactsToAdd(allContacts)
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



