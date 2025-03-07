package com.example.teammanagement.dataclass

import com.google.firebase.Timestamp


data class TypeMessage(
    var memberID: String? = "",
    var message: String = "",
    var date: Timestamp = Timestamp.now()
)