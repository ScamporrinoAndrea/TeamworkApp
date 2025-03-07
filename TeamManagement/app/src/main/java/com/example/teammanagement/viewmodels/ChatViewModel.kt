package com.example.teammanagement.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammanagement.Actions
import com.example.teammanagement.Model
import com.example.teammanagement.dataclass.Chat
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.dataclass.Team
import com.example.teammanagement.dataclass.TypeMessage
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChatViewModel(private val model: Model) : ViewModel() {

    fun member() = model.getMember()

    fun getUserId() = model.getUserId()
    fun member(memberID: String) = model.getMember(memberID)

    fun listOfChats() = model.getAllChats()
    fun listOfTeams() = model.getAllTeams()

    var messages by mutableStateOf("")
        private set

    fun setMessage(text: String) {
        messages = text
    }

    var teams: List<Team> by mutableStateOf(emptyList())
        private set

    fun setListOfTeam(list: List<Team>) {
        teams = list
    }

    var chats: List<Chat> by mutableStateOf(emptyList())
        private set

    fun setListOfChats(list: List<Chat>) {
        chats = list
    }

    fun getChatById(chatId: String) = model.getChatById(chatId)

    fun getContact(): Flow<User?> {
        return model.getMember()
    }
    fun getContact(memberID: String): Flow<User?> {
        return model.getMember(memberID)
    }

    fun getListOfAllMembersWithYouHaveTeamInCommon(): Flow<List<User>> {
        return model.getListOfAllMembersWithYouHaveTeamInCommon()
    }

    fun sendChatMessage(chatId: String) {
        viewModelScope.launch {
            val user = model.getMember().first()
            val userId = user?.id ?: return@launch
            val newMessage = TypeMessage(
                memberID = userId,
                message = messages,
                date = Timestamp.now()
            )
            setMessage("")
            model.sendChatMessage(chatId, newMessage)
        }
    }

    fun addChat(memberID: String, actions: Actions) {
        viewModelScope.launch {
            val user = model.getMember().first()
            val userId = user?.id ?: return@launch
            val chat = Chat(
                groupID = null,
                memberID = memberID,
                userID = userId,
                chat = mutableListOf()
            )
            model.addChat(chat) { chatId ->
                actions.chatShow(chatId)
            }
        }
    }
}