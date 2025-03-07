package com.example.teammanagement.screencomponent.taskdetailscomponents


import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.teammanagement.utils.CategoryTag
import com.example.teammanagement.utils.DropDownColors
import com.example.teammanagement.viewmodels.TaskViewModel
import com.google.firebase.Timestamp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    task : Task,
    vm : TaskViewModel = viewModel(factory = FactoryClass(LocalContext.current))
){
    val member by vm.member().collectAsState(initial = null)
    val sheetState1 = rememberModalBottomSheetState()
    val sheetState2 = rememberModalBottomSheetState()
    val scope2 = rememberCoroutineScope()

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
                .horizontalScroll(rememberScrollState())
            ,
            verticalAlignment = Alignment.CenterVertically
        ){
            if(task.category.isEmpty()){
                Text("No categories")
            }
           task.category.forEach {
                   CategoryTag(
                       borderColor = it.color,
                       text = it.name
                   )
           }

            if(vm.isEditingCategory){
                ModalBottomSheet(
                    onDismissRequest = {
                        vm.unEditCategory()
                    },
                    sheetState = sheetState1,
                    modifier = Modifier.fillMaxHeight()
                ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Edit category",
                                modifier = Modifier.padding(bottom = 8.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Divider(modifier = Modifier.padding(bottom = 16.dp))
                        }
                        if(task.category.isEmpty()){
                            Text("No categories", modifier = Modifier.padding(16.dp))
                        }
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    ) {
                        task.category.forEach{
                                    Box(
                                        modifier = Modifier.padding(start = 16.dp, end = 0.dp, bottom = 16.dp)
                                    ) {
                                        CategoryTag(
                                            borderColor = it.color,
                                            text = it.name
                                        )
                                        FilledIconButton(
                                            onClick = {
                                                vm.removeCategoryList(task.id,it)
                                                vm.addToHistory(
                                                    History("${member?.name} delete a category",
                                                        Timestamp.now()
                                                    ),
                                                    task.id
                                                )
                                            },
                                            modifier = Modifier
                                                .size(24.dp)
                                                .align(Alignment.BottomEnd)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "delete category",
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Button(
                                onClick = { vm.editNewCategory() },
                                colors = ButtonDefaults.buttonColors(Blue)
                            ) {
                                Text("Add category")
                            }
                        }
                    }
                }
                if(vm.isEditingNewCategory){
                    ModalBottomSheet(
                        onDismissRequest = {
                            vm.unEditNewCategory()
                        },
                        sheetState = sheetState2,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                        ){
                            OutlinedTextField(
                                value = vm.categoryTitleValue,
                                onValueChange =  vm::setCategoryTitle,
                                label = {Text(text = "Category Name")},
                                isError = vm.categoryTitleError.isNotBlank(),
                                modifier = Modifier
                                    .height(60.dp)
                                    .fillMaxWidth()
                            )
                            if(vm.categoryTitleError.isNotBlank()){
                                Text(vm.categoryTitleError, color = MaterialTheme.colorScheme.error)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                        ){
                            DropDownColors(vm::setCategoryColor, "task")
                        }

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Button(onClick = {
                                if(vm.validateCategory()){
                                    vm.addCategoryList(task.id)
                                }
                                vm.addToHistory(
                                    History("${member?.name} added a category",
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



        }
    }





