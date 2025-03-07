package com.example.teammanagement.viewmodels

import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammanagement.Model
import com.example.teammanagement.R
import com.example.teammanagement.dataclass.Category
import com.example.teammanagement.dataclass.Chat
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.dataclass.Team
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.BlueTrasp
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

class CreateTeamViewModel(val model: Model) : ViewModel() {

    private var isAdding by mutableStateOf(false)

    fun member() = model.getMember()
    fun contactList() = model.getListOfAllMembersWithYouHaveTeamInCommon()

    fun add () {
        isAdding = !isAdding
    }


    var contactsNotInTheTeam by mutableStateOf<List<User>>(emptyList())
        private set

    var isEditing by mutableStateOf(false)
        private set

    var membersValue:List<User> by mutableStateOf(emptyList())
        private set

    var descriptionValue by mutableStateOf("")
        private set

    fun setDescription(description: String) {
        descriptionValue = description
    }

    var teamCategoriesValue : List<Category> by mutableStateOf(emptyList())


    var teamNameValue by mutableStateOf("")
        private set

    var teamNameError by mutableStateOf("")
        private set

    var isDialog by mutableStateOf(false)
        private set

    var isDialog2 by mutableStateOf(false)
        private set



    fun setTeamNameVal(s : String){
        teamNameValue = s
    }

    fun addTeam(){
        viewModelScope.launch {
            val user = model.getMember().first()
            val userId = user?.id ?: return@launch
            if (descriptionValue.isBlank()) setDescription("No description")
            val list = membersValue.map { it.id }.toMutableList()
            val team = Team(
                name = teamNameValue,
                memberIds = list + userId,
                favorite = emptyList(),
                admins = listOf(userId),
                description = descriptionValue,
                creationDate = Timestamp.now(),
                category = categoryList,
                image = "default_team_image",
                imageType = "resource"
            )
            model.addTeam(team){teamId ->
                val chat = Chat(
                    groupID = teamId,
                    memberID = null,
                    userID = null,
                    chat = emptyList()
                )
                model.addChat(chat){}
            }
            setTeamNameVal("")
            setDescription("")
        }
    }

    fun validate(): Boolean {
        checkTaskTitle()
        return teamNameError.isBlank()
    }

    private fun checkTaskTitle(){
        teamNameError = if(teamNameValue.isBlank()){
            "Team name cannot be blank"
        } else ""
    }

    fun clear(){
        setNameErrorVal("")
        setDescription("")
        teamCategoriesValue = emptyList()
        membersValue = emptyList()
    }


    fun setNameErrorVal(s : String){
        teamNameError = s
    }

    fun unEdit(){
        isEditing = false
    }

    fun edit(){
        isEditing = true
    }

    fun validateCategory(): Boolean {
        checkCategoryTitle()
        if(categoryTitleError.isBlank())
            isEditing = false
        return categoryTitleError.isBlank()
    }

    fun setContactsToAdd(allContacts : List<User>){
        val ciao  = allContacts.filter{member->
            !membersValue.any { it.id == member.id }
        }
        contactsNotInTheTeam = ciao
    }



    fun dialog(){
        isDialog = !isDialog
    }

    fun dialog2(){
        isDialog2 = !isDialog2
    }

    fun addMember(user: User) {
        membersValue += user
    }

    fun removeMember(user: User){
        membersValue -= user
    }



    var categoryTitleValue by mutableStateOf("")
        private set

    var categoryTitleError by mutableStateOf("")
        private set

    private fun checkCategoryTitle(){
        categoryTitleError = if(categoryTitleValue.isBlank()){
            "Title cannot be blank"
        } else ""
    }

    fun setCategoryTitle(title: String) {
        categoryTitleValue = title
    }


    var categoryColorValue by mutableStateOf("Blue")
        private set

    fun setCategoryColor(color: String) {
        categoryColorValue = color
    }

    var categoryList by mutableStateOf<List<Category>>(emptyList())


    fun addCategoryList() {
        val category = Category(
            name = categoryTitleValue,
            color = categoryColorValue,
        )
        categoryList += category
        categoryTitleValue = ""
        categoryColorValue = "Blue"
    }

    fun removeCategoryList(category: Category) {
        categoryList -= category
    }

}