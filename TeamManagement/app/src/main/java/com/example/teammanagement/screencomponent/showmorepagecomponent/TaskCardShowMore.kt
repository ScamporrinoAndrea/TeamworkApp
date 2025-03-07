package com.example.teammanagement.screencomponent.showmorepagecomponent

import android.app.Notification
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.dataclass.Task
import com.example.teammanagement.screencomponent.profilecomponent.monogram
import com.example.teammanagement.ui.theme.GrayBackground
import com.example.teammanagement.utils.CategoryTag
import com.example.teammanagement.utils.ShowPicture
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

var image:Any? by mutableStateOf(null)

@Composable
fun TaskCardShowMore(
    listOfTeamsViewModel : ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    task: Task,
    actions : Actions,
){
    val taskMembers by listOfTeamsViewModel.getTaskMembers(task.id).collectAsState(initial = emptyList())



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .clickable(onClick = { actions.taskDetails(task.id,task.teamId) })
        ,
        colors = CardDefaults.cardColors(
            containerColor = GrayBackground,
        ),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp
        )
    ){
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
                    .align(Alignment.CenterHorizontally)
                    .height(130.dp)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                monogram = "",
                fontSize = 0,
            )
            /*when (image) {
                is Bitmap -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            bitmap = (image as Bitmap).asImageBitmap(),
                            contentDescription = "attachment image",
                            modifier = Modifier
                                .height(120.dp)
                                .padding(16.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                        )
                    }

                }
                is Uri -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = image,
                            contentDescription = "attachment image",
                            modifier = Modifier
                                .height(120.dp)
                                .padding(16.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                        )
                    }
                }
                else -> {}
            }*/
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = task.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(2f)
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            task.category.forEach {
                    CategoryTag(borderColor = it.color, text = it.name)
                }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                task.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Box {
            taskMembers.take(3).forEachIndexed { index, member ->
                if (member != null) {
                    ShowPicture(
                        imageType = member.profilePictureType,
                        image = member.profilePicture,
                        modifier = Modifier.padding(start = ((15 + index * 16).dp)).size(30.dp).clip(
                            CircleShape),
                        monogram = monogram(member.name, member.surname),
                        fontSize = 10
                    )
                }
                /*member.profilePicture(
                    Modifier.padding(start=((15+index*16).dp)).size(30.dp), 12
                )*/
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
        ){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painterResource(id = R.drawable.clockicon),
                    contentDescription = "clock",
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier.padding(end = 4.dp)
                )
                task.date?.let { Text(text= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it.toDate())) }
                    ?: if (task.repeat != "no repeat") Text(text= task.repeat) else Text(text= "No due date")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text= task.comment.size.toString())
                Image(
                    painterResource(id = R.drawable.commenticon),
                    contentDescription = "comments",
                    modifier = Modifier.padding(top = 10.dp),
                    colorFilter = ColorFilter.tint(Color.Black)
                )

            }
        }

    }

}