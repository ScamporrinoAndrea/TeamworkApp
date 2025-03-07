package com.example.teammanagement.screencomponent.teamtaskcomponent

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(task: Task, actions: Actions,
             vm : ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current))
) {
    val taskMembers by vm.getTaskMembers(task.id).collectAsState(initial = emptyList())
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(200.dp)
            .padding(end = 16.dp)
            .clickable(onClick = {})
        ,
        colors = CardDefaults.cardColors(
            containerColor = GrayBackground,
        ),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp
        ),
        onClick = {
            actions.taskDetails(task.id, task.teamId)}
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = task.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .weight(2f)
                    .padding(end = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Box {
                taskMembers.take(3).forEachIndexed { index, member ->
                    if (member != null) {
                        ShowPicture(
                            imageType = member.profilePictureType,
                            image = member.profilePicture,
                            modifier =  Modifier
                                .padding(start = (index * 16).dp)
                                .size(30.dp)
                                .clip(CircleShape),
                            monogram = monogram(member.name,member.surname),
                            fontSize = 10
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            task.category.forEach {
                CategoryTag(
                    borderColor = it.color,
                    text = it.name
                )
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

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
        ){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painterResource(id = R.drawable.clockicon),
                    contentDescription = "clock",
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier.padding(end = 4.dp)
                )
                task.date?.let { Text(text= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it.toDate()),
                ) }
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