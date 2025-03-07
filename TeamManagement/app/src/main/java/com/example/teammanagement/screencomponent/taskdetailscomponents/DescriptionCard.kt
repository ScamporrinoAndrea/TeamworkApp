package com.example.teammanagement.screencomponent.taskdetailscomponents

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.teammanagement.FactoryClass
import com.example.teammanagement.dataclass.History
import com.example.teammanagement.dataclass.Task
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.viewmodels.TaskViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionCard(
    vm : TaskViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    task: Task
){
    val member by vm.member.collectAsState(initial = null)
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

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
        ){
            Text(
                text = task.description,
                modifier = Modifier.padding(16.dp)
            )

        }

    if(vm.isEditingDescription){
        ModalBottomSheet(
            onDismissRequest = {
                vm.unEditDescription()
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Edit description",
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Divider()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){
                OutlinedTextField(
                    value = vm.taskDescriptionVal,
                    onValueChange = vm::setDescription,
                    label = {Text(text = "Task's Description")},
                    //isError = taskDescriptionError.isNotBlank(),
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                )
                //if(taskDescriptionError.isNotBlank()){
                //    Text(taskDescriptionError, color = MaterialTheme.colorScheme.error)
                //}
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(onClick = {
                    vm.validateDescription()
                    vm.updateTaskDescription(task.id)
                    vm.addToHistory(
                        History("${member?.name} modified the description",
                            Timestamp.now()
                        ),
                        task.id
                    )
                }, colors = ButtonDefaults.buttonColors(Blue)) {
                    Text("Done")

                }
            }



        }


    }


}