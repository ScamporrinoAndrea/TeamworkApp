package com.example.teammanagement.screencomponent.taskdetailscomponents

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.dataclass.History
import com.example.teammanagement.dataclass.Task
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.utils.Dialog
import com.example.teammanagement.viewmodels.TaskViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.Timestamp
import java.io.ByteArrayOutputStream
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


var showBottomSheet by mutableStateOf(false)
var openDialog by mutableStateOf(false)
var function by mutableStateOf({})

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Attachment(task: Task,
               vm : TaskViewModel = viewModel(factory = FactoryClass(LocalContext.current))) {

    val member by vm.member().collectAsState(initial = null)
    val context = LocalContext.current
    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) {uri: Uri? ->
            if (uri!=null){
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                val bao = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao)
                val imageData: ByteArray = bao.toByteArray()
                val encodedString = Base64.encodeToString(imageData, Base64.DEFAULT)
                vm.setAttachmentsImage(task.id,encodedString)
                showBottomSheet = false
                image = if(task.attachmentsImage.isNotEmpty()) task.attachmentsImage[0] else null
                vm.addToHistory(History("${member?.name} add an image", Timestamp.now(),), task.id)
            }
        }
    val cameraLauncher
            = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview(),
    ){ bitmap ->
        if(bitmap!=null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)
            vm.setAttachmentsImage(task.id,encodedString)
            showBottomSheet = false
            image = if(task.attachmentsImage.isNotEmpty()) task.attachmentsImage[0] else null
            vm.addToHistory(History("${member?.name} add an image", Timestamp.now(),), task.id)
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        }
    }
    SectionRow(
        "Attachment",
        painterResource(id = R.drawable.attachment_2_svgrepo_com),
        "attachment",
        "+"
    ) { showBottomSheet = true }

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
    ){
        SectionRow("Image", painterResource(id = R.drawable.image_svgrepo_com),"image",
            ""
        ) {}

        ViewPagerSlider(task)

        Divider(
            color = Color.Black,
            thickness = 0.5.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        SectionRow("Link", painterResource(id = R.drawable.link_svgrepo_com),"link",
            ""
        ) {}
        val localUriHandler = LocalUriHandler.current
        if (task.attachmentsLink.isEmpty()) {
            Text(
                text = "No Link",
                modifier = Modifier.padding(start = 18.dp, bottom = 16.dp),
                fontWeight = FontWeight.Light
            )
        }
        task.attachmentsLink.forEach{
            Divider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = {
                    if (it.isValidUrl()) {
                        localUriHandler.openUri(it)
                    } else {
                        val googleSearchUrl = "https://www.google.com/search?q=${Uri.encode(it)}"
                        localUriHandler.openUri(googleSearchUrl)
                    }
                },
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                ) {
                    Text(text = it)
                }
                IconButton(onClick = {

                    function = { vm.removeAttachmentsLink(task.id,it)
                        vm.addToHistory(
                            History("${member?.name} remove a link",
                            Timestamp.now(),
                            ),
                            task.id)}
                    openDialog = true
                }) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "remove image button",
                        tint = Color.Black
                    )
                }
            }

        }
    }
    if (showBottomSheet) {
        AttachmentPopUp(
            galleryLauncher,
            cameraLauncher,
            permissionLauncher,
            vm,
            task)
    }
    when {
        openDialog -> {
            Dialog(
                title = "Delete link",
                body = "Are you sure you want to delete the link? \n The action is irreversible",
                dismiss = { openDialog = false },
                confirm = {
                    function()
                    openDialog = false
                }
            )
        }
    }
}

fun String.isValidUrl(): Boolean {
    return try {
        URL(this).toURI()
        true
    } catch (e: Exception) {
        false
    }
}