package com.example.teammanagement.screencomponent.profilecomponent

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.teammanagement.dataclass.Team
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.utils.Dialog
import com.example.teammanagement.utils.openDialog
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import com.example.teammanagement.viewmodels.ProfileViewModel


fun monogram(s1: String, s2: String): String {
    val firstLetter = if (s1.isNotEmpty()) s1.substring(0, 1) else "X"
    val secondLetter = if (s2.isNotEmpty()) s2.substring(0, 1) else "X"
    return firstLetter + secondLetter
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePicturePopUp(
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    cameraLauncher: ManagedActivityResultLauncher<Void?, Bitmap?>,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    user: User?,
    team: Team? = null,
    vm : ProfileViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    vm2: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    ){
    val context = LocalContext.current
    if (showPicturePopUp) {
        ModalBottomSheet(
            onDismissRequest = {
                showPicturePopUp = false
            },
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Change Picture",
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
                                showPicturePopUp = false
                            },
                            containerColor = White,
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
                            onClick = {
                                galleryLauncher.launch("image/*")
                                showPicturePopUp = false },
                            containerColor = White,
                            contentColor = Blue,
                            shape = CircleShape,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.galleryicon),
                                contentDescription = "Camera button",
                                colorFilter = ColorFilter.tint(Blue),
                                modifier = Modifier.size(22.dp)
                            )                           }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Gallery")
                    }

                    if ((user?.profilePicture != user?.googlePicture) || (user == null && team?.imageType != "resource" )) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    openDialog = true
                                },
                                containerColor = White,
                                contentColor = Color.Red,
                                shape = CircleShape,
                            ) {
                                Icon(Icons.Filled.Close, "Floating action button.")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Remove photo")
                        }
                    }

                }

            }
        }
        when {
            openDialog -> {
                Dialog(
                    title = "Remove picture",
                    body = "Are you sure you want to remove your actual profile picture?",
                    dismiss = { openDialog = false},
                    confirm = {
                        if (user?.profilePicture != user?.googlePicture && user!=null) {
                            vm.setPicture( user.googlePicture, "uri", user.id  )
                        }else{
                            team?.id?.let { vm2.setPictureTeam( "default_team_image", "resource", it) }
                        }
                        openDialog = false
                        showPicturePopUp = false
                    }
                )
            }
        }
    }
}