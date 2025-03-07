package com.example.teammanagement.screencomponent.showteamcomponent

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.dataclass.Team
import com.example.teammanagement.screencomponent.profilecomponent.monogram
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.utils.ShowPicture
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.reflect.KFunction2

@Composable
fun TeamCard(
    team: Team, toggleFavorite: KFunction2<String, Boolean, Unit>, numberOfTasks: Int?, actions: Actions,
    vm: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current))
) {
    val teamMembers by vm.getTeamMembers(team.id).collectAsState(initial = emptyList())
    val member by vm.getMember().collectAsState(initial = null)
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
            .clickable(onClick = { actions.teamDetails(team.id) }),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row {
            ShowPicture(imageType = team.imageType, image = team.image, modifier = Modifier
                .width(132.dp)
                .height(129.dp),
                null,
                fontSize = 12)
            Column{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Text(
                        text = team.name,
                        fontWeight = FontWeight.Medium,
                    )
                    if(team.favorite.contains(member?.id)) {
                        IconButton(onClick = { toggleFavorite(team.id, false) }) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Favorite Icon",
                            )
                        }
                    }
                    else {
                        IconButton(onClick = { toggleFavorite(team.id, true) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_star_outline_24),
                                contentDescription = "Favorite Icon",
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                    ) {
                        teamMembers.take(3).forEachIndexed { index, member ->
                            if (member != null) {
                                ShowPicture(
                                    imageType = member.profilePictureType,
                                    image = member.profilePicture,
                                    modifier = Modifier
                                        .padding(start = (index * 16).dp)
                                        .size(30.dp)
                                        .clip(CircleShape),
                                    monogram = monogram(member.name,member.surname),
                                    fontSize = 10
                                )
                            }
                        }
                        if (teamMembers.size > 3) {
                            3.times(16)
                                .let { Modifier.padding(start = it.dp) }.let {
                                    FloatingActionButton(
                                        onClick = { },
                                        containerColor = Color.Black,
                                        modifier = it.size(31.dp),
                                        shape = CircleShape
                                    ) {
                                        Text("+" + (teamMembers.size - 3).toString(), color = White)
                                    }
                                }
                        }
                    }
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 0.7.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = numberOfTasks.toString() + " Tasks",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light
                    )
                }

            }
        }
    }
}