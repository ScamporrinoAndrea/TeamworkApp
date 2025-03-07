package com.example.teammanagement.screencomponent.taskdetailscomponents

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.core.content.ContextCompat
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

var linkPopUp by mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentPopUp(
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    cameraLauncher: ManagedActivityResultLauncher<Void?, Bitmap?>,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    vm : TaskViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    task: Task
){
    val context = LocalContext.current
    val member by vm.member().collectAsState(initial = null)

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Add attachment",
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Divider()
            }
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        FloatingActionButton(
                            onClick = {
                                val permissionCheckResult =
                                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch()
                                } else {
                                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                                }
                            },
                            containerColor = Color.White,
                            contentColor = Blue,
                            shape = CircleShape,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.camera),
                                contentDescription = "Camera button",
                                colorFilter = ColorFilter.tint(Blue),
                                modifier = Modifier.size(22.dp)
                            )                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Camera")
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        FloatingActionButton(
                            onClick = { galleryLauncher.launch("image/*") },
                            containerColor = White,
                            contentColor = Blue,
                            shape = CircleShape,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.galleryicon),
                                contentDescription = "gallery button",
                                colorFilter = ColorFilter.tint(Blue),
                                modifier = Modifier.size(22.dp)
                            )                           }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Gallery")
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        FloatingActionButton(
                            onClick = { linkPopUp = true },
                            containerColor = White,
                            contentColor = Blue,
                            shape = CircleShape,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.attachment_2_svgrepo_com),
                                contentDescription = "link button",
                                colorFilter = ColorFilter.tint(Blue),
                                modifier = Modifier.size(22.dp)
                            )                           }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Link")
                    }
                }

            }
        }
    }
    if(linkPopUp){
        member?.let { LinkPopUp(task = task, user = it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkPopUp(vm : TaskViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
              task: Task,
              user: User) {

    if (linkPopUp) {
        ModalBottomSheet(
            onDismissRequest = {
                linkPopUp = false
            },
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ){
                Text(
                    "Add Link",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(2f)
                    ) {
                        OutlinedTextField(
                            label = { Text("Link")},
                            value = vm.linkVal,
                            onValueChange = vm::setLinkValue,
                            isError = vm.linkErrorVal.isNotBlank()
                        )
                        if (vm.linkErrorVal.isNotBlank()) {
                            Text(vm.linkErrorVal, color = MaterialTheme.colorScheme.error)
                        }
                    }

                    Button(
                        onClick = {
                            if(vm.validateLink()){
                                vm.setLinkAttachment(task.id)
                                linkPopUp =false
                                vm.addToHistory(
                                    History("${user.name} upload a link",
                                        Timestamp.now()
                                    ),
                                    task.id
                                )
                                vm.setLinkValue("")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                        colors = ButtonDefaults.buttonColors(Blue)
                    ) {
                        Text("Insert")
                    }
                }
            }

        }
    }
}
