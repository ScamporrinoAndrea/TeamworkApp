package com.example.teammanagement.viewmodels

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammanagement.Model
import com.example.teammanagement.dataclass.Category
import com.example.teammanagement.dataclass.Comment
import com.example.teammanagement.dataclass.History
import com.example.teammanagement.dataclass.User
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@SuppressLint("MutableCollectionMutableState")
class TaskViewModel(private val model : Model) : ViewModel() {

    val member = model.getMember()
    fun getTaskById(id: String) = model.getTaskById(id)

    /******* TITLE SECTION *******/

    var taskTitleVal by mutableStateOf("")
        private set
    var isEditingTitle by mutableStateOf(false)
        private set
    var taskTitleErrorVal by mutableStateOf("")
        private set

    fun validateTitle() : Boolean{
        checkTaskTitle()
        return taskTitleErrorVal.isBlank()
    }

    private fun checkTaskTitle(){
        taskTitleErrorVal = if(taskTitleVal.isBlank()){
            "Task title cannot be blank"
        } else ""
    }

    fun editTitle(){
        isEditingTitle = true
    }

    fun unEditTitle(){
        isEditingTitle = false
    }

    fun setTitle(title: String){
        taskTitleVal = title
    }

    fun updateTaskTitle(taskId: String){
        model.updateTaskTitle(taskId, taskTitleVal)
    }

    /******* HISTORY SECTION *******/
    var isShowingHistory by mutableStateOf(false)
        private set

    fun showHistory(){
        isShowingHistory = true
    }
    fun hideHistory(){
        isShowingHistory = false
    }

    fun addToHistory(history: History, taskId : String){
        model.addHistory(taskId, history)
    }

    /******* DATE SECTION *******/

    var dueDateValue: Timestamp? by mutableStateOf(null)
        private set

    var repeatValue by mutableStateOf("")
        private set

    var isEditingDueDate by mutableStateOf(false)
        private set

    fun editDueDate(){
        isEditingDueDate = true
    }

    fun unEditDueDate(){
        isEditingDueDate = false
    }
    fun setDueDate(dueDate: Timestamp?) {
        dueDateValue = dueDate
    }

    fun setRepeat(repeatEach : String){
        repeatValue = repeatEach
    }

    fun validateDueDate(taskId : String){
        if(repeatValue != "no repeat"){
            model.updateDueDate(taskId, null)
        }
        else{
            model.updateDueDate(taskId,dueDateValue)
        }
        if(dueDateValue == null && repeatValue == "no repeat")
            model.updateRepeat(taskId, "no due date")
        else model.updateRepeat(taskId, repeatValue)
    }

    private val _dateOrRepeat = MutableStateFlow<String?>(null)
    val dateOrRepeat: StateFlow<String?> get() = _dateOrRepeat

    fun getDateOrRepeat(taskId: String) {
        viewModelScope.launch {
            val repeat = model.getRepeat(taskId).first()
            val result = if (repeat == "no repeat") {
                val dueDate = model.getDueDate(taskId).first()
                if (dueDate == null){
                    "No due date"
                }
                else {
                    dueDate.toDate().toInstant()?.atZone(java.time.ZoneId.systemDefault())
                        ?.toLocalDate()?.format(
                        DateTimeFormatter.ofPattern("dd MMM")
                    )
                }
            } else {
                repeat
            }
            _dateOrRepeat.value = result
        }
    }




    /******* STATUS SECTION *******/

    fun setStatus(taskId: String, newStatus: String){
        model.setStatus(taskId,newStatus)
    }

    /******* MEMBERS SECTION *******/
    var actualTeamMembersVal:MutableList<User?> by mutableStateOf(mutableListOf())
        private set

    var actualTaskMembersVal:MutableList<User?> by mutableStateOf(mutableListOf())
        private set

    fun setActualTeamMembers(users: List<User?>){
        actualTeamMembersVal = users.toMutableList()
    }

    fun setActualTaskMembers(users: List<User?>){
        actualTaskMembersVal = users.toMutableList()
    }

    fun updateTaskMembers(id: String){
        model.updateTaskMembers(id, actualTaskMembersVal)
    }

    fun addTaskMember(user: User){
        actualTaskMembersVal.add(user)
        actualTeamMembersVal.remove(user)
    }

    fun removeTaskMember(user: User){
        actualTaskMembersVal.remove(user)
        actualTeamMembersVal.add(user)
    }


    fun getTaskMembers(idTask : String) = model.getTaskMembers(idTask)
    fun getTeamMembers(idTeam: String) = model.getTeamMembers(idTeam)

    fun getMember(id: String) = model.getMember(id)

    fun member() = model.getMember()


    /******* COMMENT SECTION *******/
    var commentValueVal by mutableStateOf("")
        private set

    fun setCommentsValue(s:String){
        commentValueVal=s
    }

    fun setComments(taskId: String, comment: Comment){
        if (commentValueVal.isNotBlank()){
            commentValueVal=""
            model.setComments(taskId,comment)
        }
    }

    /******* CATEGORY SECTION *******/
    var isEditingCategory by mutableStateOf(false)
        private set

    var isEditingNewCategory by mutableStateOf(false)
        private set

    var taskCategoriesVal: List<Category> by mutableStateOf(emptyList())
        private set

    var taskCategoriesErrorVal by mutableStateOf("")
        private set

    var catNameVal by mutableStateOf("")
        private set

    fun setCatName(s: String){
        catNameVal = s
    }

    var catNameErrorVal by mutableStateOf("")
        private set

    fun setCatNameError(err: String){
        catNameErrorVal = err
    }

    var catColorValue by mutableStateOf("")
        private set



    var catColorTrasp by mutableStateOf("")

    fun editCategory(){
        isEditingCategory = true
    }

    fun unEditCategory(){
        isEditingCategory = false
    }

    fun editNewCategory(){
        isEditingNewCategory = true
    }

    fun unEditNewCategory(){
        isEditingNewCategory = false
    }




    private fun checkCatNameErrorVal(){
        catNameErrorVal = if(catNameVal.isBlank()){
            "Category name cannot be blank"
        } else ""


    }

    fun setColorVal(c : String){
        catColorValue = c
        catColorTrasp = c
    }



    fun validateNewCat(taskId: String, category: Category){
        checkCatNameErrorVal()
        if(catNameErrorVal.isBlank()){
            isEditingNewCategory = false
            model.addCategory(taskId,category)
        }


    }



    /*fun deleteCategory(taskId: String, categoryId: String){
        if(taskCategoriesErrorVal.isBlank()){
            model.deleteCategoryById(taskId,categoryId)
            viewModelScope.launch {
                model.getCategoriesById(taskId).collect { categories ->
                    if (categories != null) {
                        setTaskCategories(categories)
                    } else {
                        setTaskCategories(emptyList())
                    }
                }
            }
        }
    }*/

    fun setTaskCategories(categories: List<Category>){
        taskCategoriesVal = categories
    }




    /******* DESCRIPTION SECTION *******/
    var taskDescriptionVal by mutableStateOf("")
        private set
    var isEditingDescription by mutableStateOf(false)
        private set

    var taskDescriptionErrorVal by mutableStateOf("")
        private set

    fun editDescription(){
        isEditingDescription = true
    }

    fun unEditDescription(){
        isEditingDescription = false
    }
    fun validateDescription(){
        if(
            taskDescriptionErrorVal.isBlank()
        ){
            isEditingDescription = false
            if(taskDescriptionVal.isBlank())setDescription("No description")
        }
    }

    fun setDescription(title: String){
        taskDescriptionVal = title
    }

    fun updateTaskDescription(taskId: String){
        model.updateTaskDescription(taskId, taskDescriptionVal)
    }


    /******* ATTACHMENT SECTION *******/
    var linkVal by mutableStateOf("")
        private set

    var linkErrorVal by mutableStateOf("")
        private set

    fun setLinkValue(link: String){
        linkVal = link
    }

    fun validateLink():Boolean{
        if (linkVal.isBlank()){
            linkErrorVal="Link cannot be blank"
            return false
        }
        return true
    }

    fun setLinkAttachment(id: String){
        model.setLinkAttachment(id, linkVal)
    }


    fun setAttachmentsImage(taskId: String, image: String) {
        model.setAttachmentsImage(taskId, image)

    }

    fun removeAttachmentsImage(taskId: String, image: String){
        model.removeAttachmentsImage(taskId,image)
    }

    fun removeAttachmentsLink(taskId: String, link: String){
        model.removeAttachmentsLink(taskId, link)
    }



    /******* DELETE SECTION *******/
    fun deleteTask(taskId: String){
        model.deleteTask(taskId)
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

    fun addCategoryList(taskId: String) {
        val category = Category(
            name = categoryTitleValue,
            color = categoryColorValue,
        )
        model.addCategory(taskId, category)
        categoryTitleValue = ""
        categoryColorValue = "Blue"
    }

    fun removeCategoryList(taskId: String, category: Category) {
        model.deleteCategory(taskId, category)
    }

    fun validateCategory(): Boolean {
        checkCategoryTitle()
        if(categoryTitleError.isBlank())
            isEditingNewCategory = false
        return categoryTitleError.isBlank()
    }


}
