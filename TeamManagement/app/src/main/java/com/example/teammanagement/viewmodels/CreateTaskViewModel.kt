package com.example.teammanagement.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammanagement.Model
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.dataclass.Task
import com.example.teammanagement.dataclass.Team
import com.google.firebase.Timestamp
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CreateTaskViewModel(val model: Model) : ViewModel() {

    var teamValue: Team by mutableStateOf(Team())
        private set

    var allTeamsVal: List<Team> by mutableStateOf(emptyList())
        private set

    init {
        getAllTeams()
    }

    private fun getAllTeams() {
        viewModelScope.launch {
            allTeamsVal = model.getAllTeams().first()
            if(allTeamsVal.isNotEmpty()){
                teamValue = allTeamsVal[0]
            }
        }
    }

    fun setAllTeams(teams: List<Team>){
        allTeamsVal = teams
    }

    var teamMembersVal: List<User> by mutableStateOf(emptyList())
        private set

    fun setTeam(team: Team) {
        teamValue = team
    }

    fun setTeamMember(users: List<User?>){
        teamMembersVal = users as List<User>
    }

    // Title
    var titleValue by mutableStateOf("")
        private set

    var titleError by mutableStateOf("")
        private set

    private fun checkTitle(){
        titleError = if(titleValue.isBlank()){
            "Title cannot be blank"
        } else ""
    }

    fun setTitle(title: String) {
        titleValue = title
    }

    // Description
    var descriptionValue by mutableStateOf("")
        private set

    fun setDescription(description: String) {
        descriptionValue = description
    }


    // Due Date
    var dueDateValue: Timestamp? by mutableStateOf(null)
        private set

    fun setDueDate(dueDate: Timestamp?) {
        dueDateValue = dueDate
        if(dueDateValue!=null)
            repeatDateValue = "no repeat"
    }

    var repeatDateValue by mutableStateOf("no repeat")
        private set

    fun setRepeatDate(s: String) {
        repeatDateValue = s
        if(repeatDateValue!="no repeat")
            dueDateValue = null
    }

    var membersValue:List<User> by mutableStateOf(emptyList())
        private set


    fun addMember(user: User) {
        membersValue += user
        teamMembersVal = teamMembersVal - user
        setTeamMember(teamMembersVal)
    }


    fun removeMember(user: User){
        membersValue -= user
        teamMembersVal = teamMembersVal.plus(user)
    }

    fun getTeamMembers(teamId:String): Flow<List<User?>>{
        return model.getTeamMembers(teamId)
    }

    fun validate(): Boolean {
        checkTitle()
        return titleError.isBlank()
    }

    fun insertTask(user: User){
        val t = Task(
            teamId = teamValue.id,
            title = titleValue,
            memberIds = membersValue.map{m ->m.id},
            status = "Pending",
            description = if (descriptionValue=="") "No Description" else descriptionValue,
            date = dueDateValue,
            creationDate = Timestamp.now(),
            repeat = repeatDateValue,
            comment = emptyList(),
            attachmentsImage = emptyList(),
            attachmentsLink = emptyList(),
            history = emptyList(),
            category = emptyList()
        )
        model.insertTask(t)
    }

    fun getMemberById(id:String): Flow<User?>{
        return model.getMember(id)
    }

}
