package com.example.teammanagement.dataclass

import com.google.firebase.firestore.Exclude

data class Chat(
    @get:Exclude var id: String = "",
    var groupID: String? = null,
    var memberID: String? = null,
    var userID: String? = null,
    var chat: List<TypeMessage> = emptyList(),
)