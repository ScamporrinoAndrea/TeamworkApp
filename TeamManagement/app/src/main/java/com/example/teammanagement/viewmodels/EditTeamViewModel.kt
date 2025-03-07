package com.example.teammanagement.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.teammanagement.Model
import com.example.teammanagement.dataclass.Category
import com.example.teammanagement.dataclass.Team
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.BlueTrasp

class EditTeamViewModel(private val model : Model) : ViewModel() {

    fun member() = model.getMember()

    val contactList = model.getListOfAllMembersWithYouHaveTeamInCommon()

    var titleValue by mutableStateOf("")
        private set
    var titleError by mutableStateOf("")
        private set
    var descriptionValue by mutableStateOf("")
        private set

    var isEditing by mutableStateOf(false)
        private set


    fun edit(){
        isEditing = true
    }

    fun unEdit() {
        isEditing = false
    }


    var membersValue:List<User> by mutableStateOf(emptyList())
        private set

    var isDialog by mutableStateOf(false)
        private set

    var isDialog2 by mutableStateOf(false)
        private set

    var contactsNotInTheTeam by mutableStateOf<List<User>>(emptyList())
        private set

    var userToRemove by mutableStateOf(
        User())
        private set

    fun setMemberToRemoveVal(user: User){
        userToRemove = user
    }
    fun dialog(){
        isDialog = !isDialog
    }

    fun dialog2(){
        isDialog2 = !isDialog2
    }



    fun setTeamMembers(list: List<User?>){
        membersValue = list as List<User>
    }

    fun setContactsToAdd(allContacts : List<User>){
        val ciao  = allContacts.filter{member->
            !membersValue.any { it.id == member.id }
        }
        contactsNotInTheTeam = ciao
    }

    fun addMember(user: User) {
        membersValue += user
    }

    fun removeMember(user: User){
        membersValue -= user
    }

    fun getTeamMembers(teamId:String) = model.getTeamMembers(teamId)





    private fun checkTitle(){
        titleError = if(titleValue.isBlank()){
            "Title cannot be blank"
        } else ""
    }

    fun setDescription(description: String) {
        descriptionValue = description
    }

    fun setTitle(title: String) {
        titleValue = title
    }

    fun validate(): Boolean {
        checkTitle()
        return titleError.isBlank()
    }

    fun validateCategory(): Boolean {
        checkCategoryTitle()
        if(categoryTitleError.isBlank())
            isEditing = false
        return categoryTitleError.isBlank()
    }

    fun updateTeam(teamId : String, oldTeam: Team){
        val team = Team(
        name = titleValue,
        memberIds = membersValue.map { it.id },
        favorite = oldTeam.favorite,
        description = descriptionValue,
        creationDate = oldTeam.creationDate,
        category = categoryList,
        admins = oldTeam.admins,
        image = oldTeam.image,
        imageType = oldTeam.imageType
        )
        model.updateTeam(teamId,team)
    }


    fun updateTeamMembers(teamId : String){
        model.updateTeamMembers(teamId,membersValue)
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

    fun setTeamCategoriesList(categories: List<Category>) {
        categoryList = categories
    }

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