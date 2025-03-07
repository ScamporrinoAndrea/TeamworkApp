package com.example.teammanagement.dataclass

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import java.time.LocalDate
data class Team(
    @get:Exclude var id: String = "",
    var name: String = "",
    var memberIds: List<String> = emptyList(),
    var favorite: List<String> = emptyList(),
    var description: String = "",
    var creationDate: Timestamp = Timestamp.now(),
    var category: List<Category> = emptyList(),
    val admins: List<String> = emptyList(),
    var image: String = "",
    val imageType: String = "resource"
)