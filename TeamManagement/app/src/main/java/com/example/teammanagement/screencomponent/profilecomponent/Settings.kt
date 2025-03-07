package com.example.teammanagement.screencomponent.profilecomponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teammanagement.ui.theme.White

@Composable
fun Settings(onSignOut: () -> Unit) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Bio Icon" ,
                modifier = Modifier.size(25.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                "Settings",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = White,
            ),
            border = BorderStroke(1.dp, Color.LightGray),
        ) {
            TextButton(onClick = {}) {
                Text(
                    "Choose theme",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black
                )
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            TextButton(onClick = {}) {
                Text(
                    "Help",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black
                )
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            TextButton(onClick = { onSignOut() }) {
                Text(
                    "Log Out",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
        }
}