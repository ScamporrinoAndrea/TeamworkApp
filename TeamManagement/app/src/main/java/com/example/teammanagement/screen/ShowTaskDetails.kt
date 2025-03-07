package com.example.teammanagement.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.teammanagement.screencomponent.taskdetailscomponents.Attachment
import com.example.teammanagement.screencomponent.taskdetailscomponents.DescriptionCard
import com.example.teammanagement.screencomponent.taskdetailscomponents.HistoryText
import com.example.teammanagement.screencomponent.taskdetailscomponents.SectionRow
import com.example.teammanagement.screencomponent.taskdetailscomponents.TitleCard
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.GrayBackground
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.updateNavigationBar
import com.example.teammanagement.utils.Dialog
import com.example.teammanagement.viewmodels.TaskViewModel
import com.example.teammanagement.screencomponent.taskdetailscomponents.CategoryCard

var openDialog by mutableStateOf(false)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTaskDetails(
    taskId: String,
    actions: Actions,
    vm: TaskViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    navController: NavHostController
){
    BackHandler {
        actions.navigateBack()
        updateNavigationBar(navController)
    }
    val task by vm.getTaskById(taskId).collectAsState(initial = null)

    val sheetState = rememberModalBottomSheetState()
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.LightGray, Color.Transparent),
                            startY = -20f,
                            endY = 170f
                        )
                    )
            ) {
                TopAppBar(
                    title = { /*TODO*/ },
                    navigationIcon = {
                        IconButton(onClick = { actions.navigateBack() }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close Task",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                    actions = {
                        IconButton(onClick = {
                            openDialog =true
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "delete Task",
                                tint = Color.Black
                            )
                        }
                    },
                )
            }
        }
    ) {padding ->
        val scrollState = rememberScrollState()
        Box(modifier = Modifier.verticalScroll(scrollState)) {
            Column(
                modifier = Modifier
                    .padding()
                    .fillMaxSize()
                    .background(GrayBackground)
            ){
                task?.let { TitleCard(task = it) }

                Spacer(modifier = Modifier.height(15.dp))

                //--------------------------------------------------
                //CATEGORY
                //--------------------------------------------------
                SectionRow("Category", painterResource(id = R.drawable.tag_svgrepo_com),"tag",
                    "Modify", vm::editCategory)


                task?.let { CategoryCard(it) }

                Spacer(modifier = Modifier.height(15.dp))
                //--------------------------------------------------
                //DESCRIPTION
                //--------------------------------------------------

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 2.dp, top = 0.dp, bottom = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.text_description_svgrepo_com),
                            contentDescription = "description",

                            )
                        Text(
                            text = "Description",
                            modifier = Modifier.padding(start = 4.dp),
                            fontSize = 18.sp
                        )
                    }
                    TextButton(
                        onClick = {
                            vm.editDescription()
                            task?.description.let {
                                if (it != null) {
                                    vm.setDescription(it)
                                }
                            }
                        }
                    ) {
                        Text(text = "Modify", color = Blue)
                    }


                }
                task?.let { DescriptionCard(task = it) }

                Spacer(modifier = Modifier.height(16.dp))
                //--------------------------------------------------
                //ATTACHMENT
                //--------------------------------------------------
                task?.let { Attachment(it) }
                Spacer(modifier = Modifier.height(16.dp))
                //--------------------------------------------------
                //HISTORY
                //--------------------------------------------------

                SectionRow("History", painterResource(id = R.drawable.history_3_svgrepo_com),"history",
                    "Show More", vm::showHistory)


                task?.history

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
                    )){
                    task?.history?.take(3)?.forEach{
                        HistoryText(it.text, it.creationDate)
                    }
                }


                if(vm.isShowingHistory){
                    ModalBottomSheet(
                        onDismissRequest = {
                            vm.hideHistory()

                        },
                        sheetState = sheetState,
                        modifier = Modifier
                            .fillMaxHeight()


                    ) {
                        val ss2 = rememberScrollState()
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "History",
                                modifier = Modifier.padding(bottom = 8.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Divider()
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxHeight().padding(bottom = 50.dp)
                                .verticalScroll(ss2),
                        ){
                            task?.history?.forEach{ element ->
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 16.dp,
                                            bottom = 0.dp
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    HistoryText(title = element.text, date = element.creationDate)
                                }
                            }
                        }



                    }
                }



            }
        }

    }
    when {
        openDialog -> {
            Dialog(
                title = "Delete Task",
                body = "Are you sure you want to delete the task? \n The action is irreversible",
                dismiss = { openDialog = false},
                confirm = {
                    task?.let { vm.deleteTask(it.id) }
                    openDialog = false
                    actions.navigateBack()
                }
            )
        }
    }

}