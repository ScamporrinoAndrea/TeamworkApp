package com.example.teammanagement.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teammanagement.R
import com.example.teammanagement.ui.theme.Blue

@Composable
fun SignInScreen(
    onSignInClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.networking_collaboration_svgrepo_com),
                contentDescription = "Logo",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(80.dp),
                colorFilter = ColorFilter.tint(Blue)
            )
            Text(
                "TeamManagement",
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 64.dp, top = 16.dp),
                fontSize = 30.sp
            )
        }
        Box(
            contentAlignment = Alignment.Center
        ) {
            OutlinedButton(
                onClick = { onSignInClick() },
                shape = RoundedCornerShape(50.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8_google),
                        contentDescription = "Google Logo",
                        tint = Color.Unspecified,
                    )
                    Text(
                        text = "Sign in with Google",
                        color = Color.Black,
                        modifier = Modifier.padding(start = 12.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}