package com.example.teammanagement.screencomponent.profilecomponent

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.utils.ShowPicture
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import com.example.teammanagement.viewmodels.ProfileViewModel
import java.io.ByteArrayOutputStream

var showPicturePopUp by mutableStateOf(false)
@Composable
fun Picture(
    vm: ProfileViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    vm2: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    navController: NavHostController,
    teamId: String,
    memberId: String
) {
    val context = LocalContext.current
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    val member by vm.user.collectAsState()
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
                member?.let { vm.setPicture(encodedString,"bitmap", it.id) }
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
            member?.let { vm.setPicture(encodedString,"bitmap", it.id) }
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(130.dp)
    ) {
        if (currentRoute == "team/{teamId}/members/{memberId}/profile") {
            val contact by vm2.getContact(memberId).collectAsState(initial = null)
            contact?.let {
                ShowPicture(
                    imageType = it.profilePictureType,
                    image = it.profilePicture,
                    modifier = Modifier.size(180.dp).clip(CircleShape),
                    monogram = monogram(it.name,it.surname),
                    fontSize = 55
                )
            }
            //contact?.profilePicture?.let { it(Modifier.size(180.dp), 55) }

        } else if (currentRoute == "profile" || currentRoute == "profile/edit") {
            member?.let {
                ShowPicture(
                    imageType = it.profilePictureType,
                    image = it.profilePicture,
                    modifier = Modifier.size(180.dp).clip(CircleShape),
                    monogram = monogram(it.name,it.surname),
                    fontSize = 55
                )
            }
            //member!!.profilePicture(Modifier.size(180.dp), 55)
            IconButton(
                modifier = Modifier
                    .background(Blue, CircleShape)
                    .size(40.dp)
                    .align(Alignment.BottomEnd),
                onClick = {
                    showPicturePopUp = true
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Camera button",
                    colorFilter = ColorFilter.tint(White),
                    modifier = Modifier.size(19.dp)
                )
            }
        }

    }
    Spacer(modifier = Modifier.height(20.dp))
    if (currentRoute == "team/{teamId}/members/{memberId}/profile") {
        val teamMembers by vm2.getTeamMembers(teamId).collectAsState(initial = null)
        if (showPicturePopUp) {
            ChangePicturePopUp(
                galleryLauncher,
                cameraLauncher,
                permissionLauncher,
                teamMembers?.find { it?.id==memberId }
            )
        }
    } else if (currentRoute == "profile" || currentRoute == "profile/edit" ) {
        if (showPicturePopUp) {
            ChangePicturePopUp(
                galleryLauncher,
                cameraLauncher,
                permissionLauncher,
                member
            )
        }
    }

}

