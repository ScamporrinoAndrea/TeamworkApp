package com.example.teammanagement.screencomponent.taskdetailscomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.dataclass.History
import com.example.teammanagement.dataclass.Task
import com.example.teammanagement.screencomponent.profilecomponent.monogram
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.GrayBackground
import com.example.teammanagement.ui.theme.Success
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.ui.theme.Yellow
import com.example.teammanagement.utils.ShowPicture
import com.example.teammanagement.viewmodels.TaskViewModel
import com.google.firebase.Timestamp

var showComments by mutableStateOf(false)
var showAddMember by mutableStateOf(false)
var image:Any? by mutableStateOf(null)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleCard(
    vm: TaskViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    task: Task,

    ){

    vm.setTitle(task.title)

    val member by vm.member().collectAsState(initial = null)
    val taskMembers by vm.getTaskMembers(task.id).collectAsState(initial = null)
    val sheetState = rememberModalBottomSheetState()
    val sheetState2 = rememberModalBottomSheetState()

    image = try{
        if(task.attachmentsImage.isNotEmpty())
            task.attachmentsImage[0]
        else
            null
    } catch(e: Exception) {
        null
    }
    if (image != null) {
        ShowPicture(
            imageType = task.attachmentsImageType,
            image = image as String,
            modifier = Modifier
                .size(350.dp)
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(8.dp)),
            monogram = "",
            fontSize = 0,
        )
    } else{
        Image(
            painter = painterResource(id = R.drawable.flat_mountains),
            contentDescription = "Task image",
        )
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = White,
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp
        )

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 4.dp, bottom = 10.dp)
                    .weight(4f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 23.sp,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f, fill = false)
                )
                IconButton(
                    onClick = {
                        vm.editTitle()
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            }

            TextButton(
                onClick = {
                    vm.editDueDate()
                    task.repeat.let { vm.setRepeat(it) }
                    vm.setDueDate(task.date)
                },
                modifier = Modifier.weight(2f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.clockicon),
                    contentDescription = "Clock"
                )
                val dateOrRepeat by vm.dateOrRepeat.collectAsState()
                LaunchedEffect(task.id, task.date, task.repeat) {
                    vm.getDateOrRepeat(task.id)
                }
                dateOrRepeat?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(start = 5.dp),
                        color = Blue
                    )
                }
            }
        }


        if (vm.isEditingTitle) {
            ModalBottomSheet(
                onDismissRequest = {
                    vm.unEditTitle()
                },
                sheetState = sheetState,
                modifier = Modifier.fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Edit task name",
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Divider()
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                    //verticalAlignment = Alignment.CenterVertically,
                ) {

                    OutlinedTextField(
                        value = vm.taskTitleVal,
                        onValueChange = vm::setTitle,
                        label = { Text(text = "Task Name") },
                        isError = vm.taskTitleErrorVal.isNotBlank(),
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth()
                    )
                    if (vm.taskTitleErrorVal.isNotBlank()) {
                        Text(vm.taskTitleErrorVal, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(start = 8.dp, top = 20.dp))
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        if(vm.validateTitle()){

                            vm.updateTaskTitle(task.id)
                            vm.addToHistory(
                                History("${member?.name} changed the title",
                                    Timestamp.now()
                                ),
                                task.id)
                            vm.unEditTitle()
                        }


                    }, colors = ButtonDefaults.buttonColors(Blue)) {
                        Text("Done")
                    }
                }
            }
        }

        if(vm.isEditingDueDate){
            ModalBottomSheet(
                onDismissRequest = {
                    vm.unEditDueDate()
                },
                sheetState = sheetState2,
                modifier = Modifier.fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Edit due date",
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Divider()
                }
                Text(
                    "You can select only one due date or one repeat",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 15.sp
                )
                DropDownRepeat()

                DatePicker2(pickedDate = task.date, setDueDate = vm::setDueDate, vm::setRepeat, dueDate = vm.dueDateValue)

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        vm.validateDueDate(task.id)
                        vm.unEditDueDate()
                        vm.addToHistory(History("${member?.name} changed the due date",
                            Timestamp.now()
                        ),
                            task.id)
                    }, colors = ButtonDefaults.buttonColors(Blue)) {
                        Text("Done")
                    }
                }

            }
        }
        var selectedStatus by remember { mutableStateOf(task.status) }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            FilledTonalButton(
                onClick = {
                    selectedStatus= "Pending"
                    vm.setStatus(task.id,"Pending")
                    vm.addToHistory(
                        History("${member?.name} update the status",
                        Timestamp.now()
                    ),
                        task.id)},
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .padding(end = 8.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
                border = BorderStroke(width= 1.dp,
                    color = if (selectedStatus=="Pending") Yellow else Color.Gray),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedStatus=="Pending") Yellow.copy(0.2f) else GrayBackground)
            ) {
                Text(text = "Pending", fontSize = 14.sp, fontWeight = FontWeight.Bold,
                    color = if (selectedStatus=="Pending") Yellow else Color.Gray)
            }
            FilledTonalButton(
                onClick = {
                    selectedStatus= "In Progress"
                    vm.setStatus(task.id,"In Progress")
                    vm.addToHistory(History("${member?.name} update the status",
                        Timestamp.now()
                    ),
                        task.id)},
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .padding(start = 8.dp, end = 8.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
                border = BorderStroke(width= 1.dp,
                    color = if (selectedStatus=="In Progress") Blue else Color.Gray),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedStatus=="In Progress") Blue.copy(0.2f) else GrayBackground)
            ) {
                Text(text = "In Progress", fontSize = 14.sp, fontWeight = FontWeight.Bold,
                    color = if (selectedStatus=="In Progress") Blue else Color.Gray)
            }
            FilledTonalButton(
                onClick = {
                    selectedStatus= "Completed"
                    vm.setStatus(task.id,"Completed")
                    vm.addToHistory(History("${member?.name} update the status",
                        Timestamp.now()
                    ),
                        task.id)},
                modifier = Modifier
                    .height(34.dp)
                    .weight(1f)
                    .padding(start = 8.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
                border = BorderStroke(width= 1.dp,
                    color = if (selectedStatus=="Completed") Success else Color.Gray),                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedStatus=="Completed") Success.copy(0.2f) else GrayBackground)
            ) {
                Text(text = "Completed", fontSize = 14.sp, fontWeight = FontWeight.Bold,
                    color = if (selectedStatus=="Completed") Success else Color.Gray)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Box {
                taskMembers?.forEachIndexed { index, member ->
                    if (member != null) {
                        ShowPicture(
                            imageType = member.profilePictureType,
                            image = member.profilePicture,
                            modifier = Modifier.padding(start=(index*16).dp).size(30.dp).clip(CircleShape),
                            monogram = monogram(member.name,member.surname),
                            fontSize = 12)
                    }
                    /*member.profilePicture(
                        Modifier.padding(start=(index*16).dp).size(30.dp), 12
                    )*/
                }
                taskMembers?.size?.times(16)
                    ?.let { Modifier.padding(start= it.dp) }?.let {
                        FloatingActionButton(
                            onClick = { showAddMember = true},
                            //vm.setMembersValue(task.id)},
                            containerColor = Blue,
                            modifier = it.size(31.dp),
                            shape = CircleShape
                        ) {
                            Icon(imageVector = Icons.Filled.Add,
                                contentDescription = "add member button",
                                tint = White)
                        }
                    }
                if(taskMembers.isNullOrEmpty()){
                    FloatingActionButton(
                        onClick = {
                            showAddMember = true
                                  },
                        //vm.setMembersValue(task.id)},
                        containerColor = Blue,
                        modifier = Modifier.size(31.dp),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "add member button",
                            tint = White
                        )
                    }
                }
            }

            TextButton(
                onClick = { showComments = true},
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.comments_3_svgrepo_com),
                    contentDescription = "comments"
                )
                Text(
                    text = task.comment.size.toString(),
                    modifier = Modifier.padding(start = 5.dp),
                    color = Blue
                )
            }
        }


    }
    if(showComments){
        member?.let { CommentsPopUp(task, it) }
    }
    if(showAddMember){
        AddMemberPopUp(task = task)
    }
}