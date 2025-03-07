package com.example.teammanagement.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.teammanagement.Model
import com.example.teammanagement.dataclass.User
import com.google.firebase.auth.FirebaseUser

class RegistrationViewModel(private val model : Model) : ViewModel() {
    var location by mutableStateOf("")
        private set

    var bio by mutableStateOf("")
        private set

    var name by mutableStateOf("")
        private set

    var surname by mutableStateOf("")
        private set

    var nickname by mutableStateOf("")
        private set

    fun setLocations(location: String) {
        this.location = location
    }

    fun setBios(bio: String) {
        this.bio = bio
    }

    fun setNames(name: String) {
        this.name = name
    }

    fun setSurnames(surname: String) {
        this.surname = surname
    }

    fun setNicknames(nickname: String) {
        this.nickname = nickname
    }

    var nameError by mutableStateOf("")
        private set

    var surnameError by mutableStateOf("")
        private set

    var nicknameError by mutableStateOf("")
        private set

    private fun checkName() {
        nameError = if (name.isBlank()) {
            "Name is required"
        } else ""
    }

    private fun checkSurname() {
        surnameError = if (surname.isBlank()) {
            "Surname is required"
        } else ""
    }

    private fun checkNickname() {
        nicknameError = if (nickname.isBlank()) {
            "Nickname is required"
        } else ""
    }

    fun validate():Boolean {
        checkName()
        checkSurname()
        checkNickname()
        return (nameError.isBlank()
                && surnameError.isBlank()
                && nicknameError.isBlank()
                )
    }

    fun register(navController: NavController, user: FirebaseUser) {
        val newUser = User(
            id = user.uid,
            name = name,
            surname = surname,
            nickname = nickname,
            googlePicture = user.photoUrl.toString(),
            profilePicture = user.photoUrl.toString(),
            profilePictureType = "uri",
            email = user.email ?: "",
            location = location.ifBlank { "No location" },
            bio = bio.ifBlank { "No bio yet" }
        )
        model.registerUser(newUser) {
            model.setUserId(user.uid)
            navController.popBackStack()
            navController.navigate("teams")
        }
    }
}