package com.example.teammanagement.dataclass

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class Task(
    @get:Exclude var id: String = "",
    var teamId: String = "",
    var title: String = "",
    var memberIds: List<String> = emptyList(),
    var status: String = "",
    var description: String = "",
    var date: Timestamp? = Timestamp.now(),
    var creationDate: Timestamp = Timestamp.now(),
    var repeat: String = "",
    var comment: List<Comment> = emptyList(),
    var attachmentsImage: List<String> = emptyList(),
    var attachmentsImageType: String = "bitmap",
    var attachmentsLink: List<String> = emptyList(),
    var history: List<History> = emptyList(),
    var category: List<Category> = emptyList(),
)

data class Comment(
    var comment: String = "",
    var author: String = "",
    var creationDate: Timestamp = Timestamp.now(),
)
data class History(
    var text: String = "",
    var creationDate: Timestamp = Timestamp.now(),
)

data class Category(
    var name: String = "",
    var color: String = "",
)