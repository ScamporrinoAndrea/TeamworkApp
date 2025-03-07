package com.example.teammanagement.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.teammanagement.Model
import com.example.teammanagement.dataclass.Task
import com.example.teammanagement.dataclass.Team
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.utils.isVisible
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListOfTeamsViewModel(private val model : Model) : ViewModel() {

    fun setUserId(userId: String) = model.setUserId(userId)
    fun getMember() = model.getMember()
    fun getFavoriteTeams(searchText: String): Flow<List<Team>>{
        return model.getFavoriteTeams().map { teams ->
            teams.filter { team ->
                team.name.contains(searchText, ignoreCase = true) || team.description.contains(searchText, ignoreCase = true)
            }
        }
    }
    fun getNotFavoriteTeams(searchText: String): Flow<List<Team>>{
        return model.getNotFavoriteTeams().map { teams ->
            teams.filter { team ->
                team.name.contains(searchText, ignoreCase = true) || team.description.contains(searchText, ignoreCase = true)
            }
        }
    }

    fun toggleFavorite(idTeam : String, newFavorite : Boolean)  {
        model.toggleFavorite(idTeam, newFavorite)
    }





    var filterByVal by mutableStateOf("")
        private set
    fun setFilterBy(s : String){
        filterByVal = s
    }
    var isFilter by mutableStateOf(false)
        private set
    var filterCategory by mutableStateOf("")
        private set

    var filterName by mutableStateOf("")
        private set
    var filterCreationDate : Timestamp? by mutableStateOf(null)
        private set

    fun setFilterCreationDateVal(date : Timestamp?){
        filterCreationDate = date
    }

    var filterDueDate : Timestamp? by mutableStateOf(null)
        private set

    fun setFilterDueDateVal(date : Timestamp?){
        filterDueDate = date
    }

    fun setFilterCategoryVal(filter : String){
        filterCategory = filter
    }

    fun setFilterNameVal(filter : String){
        filterName = filter
    }
    fun filter(){
        isFilter = !isFilter
        if(!isFilter){
            setFilterBy("")
            setFilterCategoryVal("")
            setFilterCreationDateVal(null)
            setFilterDueDateVal(null)
            setFilterNameVal("")
        }
    }
    fun getContact(contactId: String) = model.getMember(contactId)

    fun setPictureTeam(image: String, type: String, teamId: String){
        model.setPictureTeam(image, type, teamId)
    }


    fun member() = model.getMember()

    fun addTeamMember(teamId: String) = model.addTeamMember(teamId)

    fun getNumberOfTeamTask(idTeam : String) = model.getNumberOfTeamTask(idTeam)

    fun getTeamMembers(idTeam : String) = model.getTeamMembers(idTeam)

    fun getTaskMembers(idTask : String) = model.getTaskMembers(idTask)

    fun getTasksOfTeam(
        teamId: String,
        category: String,
        creationDate: Timestamp?,
        dueDate: Timestamp?,
        name: String,
        searchTextTask: String
    ): Flow<List<Task>> {
        return model.getTasksOfTeam(teamId,category.trimEnd(),creationDate,dueDate,name.trimEnd()).map { tasks ->
            tasks.filter { task ->
                task.title.contains(searchTextTask, ignoreCase = true)
            }
        }
    }

    fun getTeamById(teamId : String) = model.getTeamById(teamId)

    fun getTeamAchievement(teamId : String) = model.getTeamAchievement(teamId)

    fun deleteTeam(teamId : String) = model.deleteTeam(teamId)
    fun leaveTeam(teamId : String) = model.leaveTeam(teamId)


    var searchText by mutableStateOf("")
        private set

    fun setSearchTexts(text: String) {
        searchText = text
    }

    var searchTextTask by mutableStateOf("")
        private set

    fun setSearchTextsTask(text: String) {
        searchTextTask = text
    }

    fun getTasksOfTeamByStatus(teamId: String, taskStatus: String)
            = model.getTaskOfTeamByStatus(teamId, taskStatus)


    var myTasksVal by mutableStateOf(mutableListOf<Task>())
        private set


    fun setMyTasks(list: MutableList<Task>){
        myTasksVal = list
    }

    fun getTaskByMember(id: String, category : String, creationDate : Timestamp?, dueDate : Timestamp?, name : String): Flow<List<Task>> {
        return model.getTaskByMember(id,category.trimEnd(),creationDate,dueDate,name.trimEnd()).map { tasks ->
            tasks.filter { task ->
                task.title.contains(searchTextTask, ignoreCase = true)
            }
        }
    }
    fun getMemberById(id: String): Flow<User?>{
        return model.getMember(id)
    }

    fun makeAdmin(teamId: String, memberId: String){
        model.makeAdmin(teamId,memberId)
    }
    fun removeAdmin(teamId: String, memberId: String){
        model.removeAdmin(teamId,memberId)
    }

    fun getAllTeams(): Flow<List<Team>>{
        return model.getAllTeams()
    }
}