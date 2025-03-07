package com.example.teammanagement.dataclass

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.firestore.Exclude

data class User(
    @get:Exclude var id: String = "",
    var name: String = "",
    var surname: String = "",
    var googlePicture: String = "",
    var profilePicture: String = "",
    var profilePictureType: String = "",
    var nickname: String = "",
    var email: String = "",
    var location: String = "",
    var bio: String = "",
)