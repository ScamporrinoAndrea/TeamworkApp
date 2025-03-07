package com.example.teammanagement

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teammanagement.viewmodels.ChatViewModel
import com.example.teammanagement.viewmodels.CreateTaskViewModel
import com.example.teammanagement.viewmodels.CreateTeamViewModel
import com.example.teammanagement.viewmodels.EditTeamViewModel
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import com.example.teammanagement.viewmodels.ProfileViewModel
import com.example.teammanagement.viewmodels.RegistrationViewModel
import com.example.teammanagement.viewmodels.TaskViewModel

class FactoryClass(context: Context): ViewModelProvider.Factory{
    private val model: Model = (context.applicationContext as? MyApplication)?.model ?: throw IllegalArgumentException("Bad application class")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ListOfTeamsViewModel::class.java))
            ListOfTeamsViewModel(model) as T
        else if (modelClass.isAssignableFrom(EditTeamViewModel::class.java))
            EditTeamViewModel(model) as T
        else if (modelClass.isAssignableFrom(CreateTeamViewModel::class.java))
            CreateTeamViewModel(model) as T
        else if (modelClass.isAssignableFrom(ProfileViewModel::class.java))
            ProfileViewModel(model) as T
        else if (modelClass.isAssignableFrom(TaskViewModel::class.java))
            TaskViewModel(model) as T
        else if (modelClass.isAssignableFrom(ChatViewModel::class.java))
            ChatViewModel(model) as T
        else if (modelClass.isAssignableFrom(CreateTaskViewModel::class.java))
            CreateTaskViewModel(model) as T
        else if (modelClass.isAssignableFrom(RegistrationViewModel::class.java))
            RegistrationViewModel(model) as T
        else throw java.lang.IllegalArgumentException("Unknown view Model Class")
    }
}