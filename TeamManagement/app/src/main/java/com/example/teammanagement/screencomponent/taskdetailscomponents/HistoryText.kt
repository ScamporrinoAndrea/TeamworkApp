package com.example.teammanagement.screencomponent.taskdetailscomponents

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HistoryText(title: String, date: Timestamp){
    Text(text= title, modifier = Modifier.padding(start = 16.dp, end =16.dp, bottom= 0.dp, top=10.dp).width((LocalConfiguration.current.screenWidthDp*0.5).dp), fontWeight = FontWeight.Bold)
    Text(text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date.toDate()), modifier = Modifier.padding(start = 16.dp, end =16.dp, bottom= 10.dp, top=0.dp))
}