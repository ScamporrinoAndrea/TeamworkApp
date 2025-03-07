package com.example.teammanagement.viewmodels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammanagement.Model
import com.example.teammanagement.dataclass.Chat
import com.example.teammanagement.dataclass.Team
import com.example.teammanagement.dataclass.User
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(private val model :Model): ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        viewModelScope.launch {
            model.getMember().collect { member ->
                _user.value = member
            }
        }
    }

    fun getMember()= model.getMember()

    fun generalAchievement(memberId: String)= model.getMemberAchievement(memberId)
    fun teamAchievement(teamId: String, memberId: String)= model.getMemberAchievementsOfTeam(teamId,memberId)

    fun getMemberTeams(memberId: String)= model.getMemberTeams(memberId)

    fun setPicture(image: String, type: String, memberId: String){
        model.setPictureMember(image, type, memberId)
    }

    fun isValid(): Boolean{
        checkName()
        checkSurname()
        checkEmail()
        checkNickname()
        checkBio()
        checkPosition()
        return (nameError.isBlank()
                && surnameError.isBlank()
                && nicknameError.isBlank()
                && emailError.isBlank()
                && positionError.isBlank())
    }

    fun setMemberProfile(){
        viewModelScope.launch {
            val user = model.getMember().first()
            val userId = user?.id ?: return@launch
            val newUser = User(
                name = nameValue,
                surname = surnameValue,
                email = emailValue,
                nickname = nicknameValue,
                bio = bioValue,
                location = positionValue,
                profilePicture = user.profilePicture,
                profilePictureType = user.profilePictureType
            )
            model.setMemberProfile(newUser, userId)
        }
    }

    var nameValue by mutableStateOf("")
        private set
    var nameError by mutableStateOf("")
        private set
    fun setName(n: String){
        nameValue = n
    }
    private fun checkName(){
        nameError = if(nameValue.isBlank()){
            "Name cannot be blank"
        } else ""
    }
    var surnameValue by mutableStateOf("")
        private set
    var surnameError by mutableStateOf("")
        private set
    fun setSurname(n: String){
        surnameValue = n
    }
    private fun checkSurname(){
        surnameError = if(surnameValue.isBlank()){
            "Surname cannot be blank"
        } else ""
    }
    var emailValue by mutableStateOf("")
        private set
    var emailError by mutableStateOf("")
        private set
    fun setEmail(n: String){
        emailValue = n.trim()
    }
    private fun checkEmail(){
        emailError = if (emailValue.isBlank()){
            "Email cannot be blank"
        } else if(!emailValue.contains('@')){
            "Invalid email address"
        } else ""
    }
    var nicknameValue by mutableStateOf("")
        private set
    var nicknameError by mutableStateOf("")
        private set
    fun setNickname(n: String){
        nicknameValue = n.trim()
    }

    private fun checkNickname(){
        nicknameError = if(nicknameValue.isBlank()){
            "Nickname cannot be blank"
        } else ""
    }

    var bioValue by mutableStateOf("")
        private set

    fun setBio(n: String){
        bioValue = n
    }

    private fun checkBio(){
        if(bioValue.isBlank()){
            bioValue = "Hey there I'm using TeamTasker"
        }
    }

    var positionValue by mutableStateOf("")
        private set
    var positionError by mutableStateOf("")
        private set
    fun setPosition(n: String){
        positionValue = n
    }

    private fun checkPosition(){
        positionError = if(positionValue.isBlank()){
            "Position cannot be blank"
        } else ""
    }
}
