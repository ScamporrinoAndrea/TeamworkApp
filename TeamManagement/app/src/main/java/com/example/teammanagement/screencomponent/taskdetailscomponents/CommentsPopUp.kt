package com.example.teammanagement.screencomponent.taskdetailscomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.dataclass.Comment

import com.example.teammanagement.dataclass.User
import com.example.teammanagement.dataclass.Task
import com.example.teammanagement.screencomponent.profilecomponent.monogram
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.utils.ShowPicture
import com.example.teammanagement.viewmodels.TaskViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsPopUp(task: Task, user: User,
                  vm: TaskViewModel = viewModel(factory = FactoryClass(LocalContext.current)
)
) {

    if (showComments) {
        ModalBottomSheet(
            modifier = Modifier.height(1000.dp),
            onDismissRequest = {
                showComments = false
            },
        )
        {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Comments",
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Divider()
            }
            Row(
                modifier = Modifier.padding(top = 24.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /*user.profilePicture(
                    Modifier
                        .padding(start = (16).dp)
                        .size(30.dp), 12
                )*/
                OutlinedTextField(
                    value = vm.commentValueVal,
                    onValueChange = vm::setCommentsValue,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    textStyle = TextStyle(fontSize = 15.sp)
                )
                SmallFloatingActionButton(
                    onClick = {
                        vm.setComments(task.id, Comment(vm.commentValueVal, user.id, Timestamp.now()))
                        showComments =false
                        showComments =true },
                    shape = CircleShape,
                    containerColor = Color.Blue
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Send Icon",
                        tint = White
                    )
                }
            }
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                task.comment.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val mem by vm.getMember(it.author).collectAsState(initial = null)
                        /*mem?.profilePicture?.let { it1 ->
                            it1(
                                Modifier
                                    .padding(start = (16).dp)
                                    .size(30.dp), 12
                            )
                        }*/
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row {
                                    mem?.let { it1 ->
                                        ShowPicture(
                                            imageType = it1.profilePictureType,
                                            image = it1.profilePicture,
                                            modifier = Modifier
                                                .size(30.dp)
                                                .clip(CircleShape),
                                            monogram = monogram(it1.name,it1.surname),
                                            fontSize = 10)
                                    }
                                    Text(
                                        mem?.name + " " + mem?.surname,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                                Text(
                                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it.creationDate.toDate()),
                                    fontWeight = FontWeight.Light
                                )
                            }
                            Text(
                                it.comment,
                                modifier = Modifier.padding(start = 46.dp)
                            )
                        }
                    }

                }
            }
        }
    }
}