package com.example.teammanagement.screencomponent.teamtaskcomponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.sharp.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.dataclass.Team
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.GrayBackground
import com.example.teammanagement.utils.CategoryTag
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoTeam(team: Team, vm : ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current))){
    val achievement by vm.getTeamAchievement(team.id).collectAsState(initial = null)
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.padding(16.dp),
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
            expanded = !expanded
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    "Description",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Sharp.DateRange,
                        contentDescription = "date Icon",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(25.dp)
                            .padding(end = 5.dp)
                    )
                    Text(
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(team.creationDate.toDate()),
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                    )
                }
            }
            Text(team.description)

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        team.category.forEach {
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
                            .padding(bottom = 16.dp)
                    ) {
                        Text("Tasks completed", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Text(
                            "${achievement?.totalTasksCompleted}/${achievement?.totalTasksAssigned}",
                            color = Color.Gray
                        )
                    }
                    LinearProgressIndicator(
                        progress = (achievement?.totalTasksCompleted?.toFloat()
                            ?: 1f) / (achievement?.totalTasksAssigned?.toFloat() ?: 1f),
                        color = Blue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.medal3),
                                contentDescription = "Medal1",
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                    if ((achievement?.totalTasksCompleted ?: 0) < 5) {
                                        setToSaturation(0f)
                                    }
                                })
                            )
                            Text("Complete 5 tasks", textAlign = TextAlign.Center)
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.medal2),
                                contentDescription = "Medal2",
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                    if ((achievement?.totalTasksCompleted ?: 0) < 25) {
                                        setToSaturation(0f)
                                    }
                                })

                            )
                            Text("Complete 25 tasks", textAlign = TextAlign.Center)
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.medal1),
                                contentDescription = "Medal3",
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                    if ((achievement?.totalTasksCompleted ?: 0) < 50) {
                                        setToSaturation(0f)
                                    }
                                })
                            )
                            Text("Complete 50 Tasks", textAlign = TextAlign.Center)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Accordion Icon",
                    modifier = Modifier.size(25.dp),
                    tint = Color.Black
                )
            }
        }

    }
}