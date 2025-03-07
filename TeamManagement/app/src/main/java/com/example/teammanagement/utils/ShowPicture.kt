package com.example.teammanagement.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun ShowPicture(imageType: String, image: String, modifier: Modifier, monogram: String?, fontSize: Int){
    val context = LocalContext.current
    when (imageType) {
        "bitmap" -> {
            Image(
                bitmap = BitmapFactory.decodeByteArray(Base64.decode(image, Base64.DEFAULT), 0, Base64.decode(image, Base64.DEFAULT).size).asImageBitmap(),
                contentDescription = "Team image",
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }

        "uri" -> {
            Image(
                painter = rememberAsyncImagePainter(model = image),
                contentDescription = "Team image",
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }

        "resource" -> {
            val resId = context.resources.getIdentifier(image, "drawable", context.packageName)
            if (resId != 0) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = "Team image",
                    modifier = modifier,
                    contentScale = ContentScale.Crop
                )
            }
        }
        "monogram" -> {
            Box(
                modifier = modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF6495ED), Color(0xFFADD8E6)),
                            start = Offset(0f, 0f),
                            end = Offset(200f, 200f)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (monogram != null) {
                    Text(
                        text = monogram,
                        color = Color.White,
                        fontSize = fontSize.sp,
                        modifier = Modifier.padding(7.dp)
                    )
                }
            }
        }
    }
}