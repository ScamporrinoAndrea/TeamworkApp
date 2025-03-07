package com.example.teammanagement.screencomponent.taskdetailscomponents

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.dataclass.History
import com.example.teammanagement.dataclass.Task
import com.example.teammanagement.utils.Dialog
import com.example.teammanagement.utils.ShowPicture
import com.example.teammanagement.viewmodels.TaskViewModel
import com.google.accompanist.pager.*
import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

var openDialog2 by mutableStateOf(false)
var function2 by mutableStateOf({})
@ExperimentalPagerApi
@Composable
fun ViewPagerSlider(task: Task, vm : TaskViewModel = viewModel(factory = FactoryClass(LocalContext.current))) {
    val pagerState  = rememberPagerState(
        initialPage =  0
    )
    val member by vm.member().collectAsState(initial = null)

    val attachmentsImage = task.attachmentsImage
    if (attachmentsImage.isNotEmpty()){
        Column(modifier = Modifier .background(Color.White)) {
            HorizontalPager(state = pagerState,
                modifier = Modifier,
                count = attachmentsImage.size) { page ->
                Card(modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale

                        }
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                    }
                    .fillMaxWidth(),
                ) {

                    val newKids = attachmentsImage[page]
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                    ) {
                        ShowPicture(
                            imageType = task.attachmentsImageType,
                            image = newKids,
                            modifier = Modifier
                                .size(350.dp)
                                .padding(16.dp)
                                .clip(shape = RoundedCornerShape(8.dp)),
                            monogram = "",
                            fontSize = 0,
                        )
                        /*when (newKids) {
                            is Bitmap -> {
                                Image(
                                    bitmap = newKids.asImageBitmap(),
                                    contentDescription = "attachment image",
                                    modifier = Modifier
                                        .size(350.dp)
                                        .padding(16.dp)
                                        .clip(shape = RoundedCornerShape(8.dp))
                                )
                            }

                            is Uri -> {
                                AsyncImage(
                                    model = newKids,
                                    contentDescription = "attachment image",
                                    modifier = Modifier
                                        .size(350.dp)
                                        .padding(16.dp)
                                        .clip(shape = RoundedCornerShape(8.dp))
                                )
                            }

                            else -> {}
                        }*/
                        IconButton(onClick = {
                            openDialog2 = true
                            function2 ={
                                vm.removeAttachmentsImage(task.id,newKids)
                                vm.addToHistory(
                                    History("${member?.name} update the status",
                                        Timestamp.now()), task.id)
                                image = if(task.attachmentsImage.isNotEmpty()) task.attachmentsImage[0] else null
                            }
                        }, modifier = Modifier.align(Alignment.TopEnd)) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "remove image button",
                                tint = Color.Black
                            )
                        }
                    }


                }

            }

            //Horizontal dot indicator
            HorizontalPagerIndicator(
                pagerState = pagerState,modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )

        }
    }
    else{
        Text(text = "No attachments", modifier = Modifier.padding(start = 16.dp, bottom = 16.dp), fontWeight = FontWeight.Light)
    }
    when {
        openDialog2 -> {
            Dialog(
                title = "Delete image",
                body = "Are you sure you want to delete the image? \n The action is irreversible",
                dismiss = { openDialog2 = false },
                confirm = {
                    function2()
                    openDialog2 = false
                }
            )
        }
    }
}