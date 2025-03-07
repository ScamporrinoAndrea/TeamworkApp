package com.example.teammanagement.utils

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
import androidx.core.graphics.ColorUtils
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.BlueTrasp
import com.example.teammanagement.ui.theme.Green
import com.example.teammanagement.ui.theme.GreenTrasp
import com.example.teammanagement.ui.theme.Orange
import com.example.teammanagement.ui.theme.OrangeTrasp
import com.example.teammanagement.ui.theme.Purple40
import com.example.teammanagement.ui.theme.Purple40Trasp
import com.example.teammanagement.ui.theme.Purple80
import com.example.teammanagement.ui.theme.Red
import com.example.teammanagement.ui.theme.RedTrasp
import com.example.teammanagement.ui.theme.Yellow
import com.example.teammanagement.ui.theme.YellowTrasp

@Composable
fun CategoryTag(borderColor : String, text: String){
    SuggestionChip(
        onClick = {  },
        label = { Text(text, color = Color.Black,
            style = TextStyle(fontSize = 14.sp), maxLines = 1,
            modifier = Modifier.padding(horizontal = 1.dp, vertical = 1.dp),
            overflow = TextOverflow.Ellipsis)
        },
        enabled = false,
        modifier = Modifier
            .padding(end = 12.dp)
            .height(30.dp),
        colors = SuggestionChipDefaults.suggestionChipColors(
            disabledContainerColor = ThemeColors.getColorFromString(borderColor+"Trasp"),
        ),
        shape = RoundedCornerShape(30.dp),
        border = SuggestionChipDefaults.suggestionChipBorder(
            borderWidth = 1.dp,
            disabledBorderColor = ThemeColors.getColorFromString(borderColor),
        )
    )
}

object ThemeColors {
    val colorMap = mapOf(
        "Purple40Trasp" to Purple40Trasp,
        "Purple40" to Purple40,
        "Red" to Red,
        "RedTrasp" to RedTrasp,
        "Orange" to Orange,
        "OrangeTrasp" to OrangeTrasp,
        "Yellow" to Yellow,
        "YellowTrasp" to YellowTrasp,
        "Green" to Green,
        "GreenTrasp" to GreenTrasp,
        "Blue" to Blue,
        "BlueTrasp" to BlueTrasp,
    )
    fun getColorFromString(colorString: String): Color {
        return colorMap[colorString] ?: throw IllegalArgumentException("Color string not found: $colorString")
    }
}