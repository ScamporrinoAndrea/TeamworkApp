package com.example.teammanagement.screencomponent.taskdetailscomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teammanagement.ui.theme.Blue


@Composable
fun SectionRow(title : String, icon : Painter, content : String, whatToClick: String, action : () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 2.dp, top = 0.dp, bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = icon,
                contentDescription = content,

                )
            Text(
                text = title,
                modifier = Modifier.padding(start = 4.dp),
                fontSize = 18.sp
            )
        }
        TextButton(
            onClick = action
        ) {
            Text(text = whatToClick, color = Blue)
        }


    }
}