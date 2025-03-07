package com.example.teammanagement.screencomponent.taskdetailscomponents


import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryTag(borderColor : Color, backgroundColor : Color, text: String){
    SuggestionChip(
        onClick = {  },
        label = { Text(text, color = Color.Black,
            style = TextStyle(fontSize = 14.sp), maxLines = 1,
            modifier = Modifier.padding(horizontal = 1.dp, vertical = 1.dp),
            overflow = TextOverflow.Ellipsis)},
        enabled = false,
        modifier = Modifier
            .padding(end = 8.dp)
            .height(30.dp),
        colors = SuggestionChipDefaults.suggestionChipColors(
            disabledContainerColor = backgroundColor
        ),
        shape = RoundedCornerShape(30.dp),
        border = SuggestionChipDefaults.suggestionChipBorder(
            borderWidth = 1.dp,
            disabledBorderColor = borderColor
        )
        )
}


