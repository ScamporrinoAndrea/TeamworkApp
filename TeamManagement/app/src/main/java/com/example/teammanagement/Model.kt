package com.example.teammanagement

import android.content.Context
import android.util.Log
import com.example.teammanagement.dataclass.Category
import com.example.teammanagement.dataclass.Chat
import com.example.teammanagement.dataclass.Comment
import com.example.teammanagement.dataclass.History
import com.example.teammanagement.dataclass.User
import com.example.teammanagement.dataclass.MemberAchievement
import com.example.teammanagement.dataclass.Task
import com.example.teammanagement.dataclass.Team
import com.example.teammanagement.dataclass.TypeMessage
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.time.ZoneId

class Model(val context: Context) {

    private val db = Firebase.firestore
    private lateinit var userId: String

    fun setUserId(id: String) {
        userId = id
    }
    fun getUserId() : String {
        return userId
    }

    fun getMember(memberId: String = userId): Flow<User?> {
        return if (memberId.isNotEmpty()) {
            callbackFlow {
                val listener = db.collection("users")
                    .document(memberId)
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            Log.e("getMember", "Error fetching member", exception)
                            trySend(null)
                        } else if (snapshot != null) {
                            val user = snapshot.toObject(User::class.java)?.apply { id = snapshot.id }
                            trySend(user)
                        }
                    }
                awaitClose { listener.remove() }
            }
        } else {
            callbackFlow {
                trySend(null).isSuccess
                awaitClose()
            }
        }
    }

    fun registerUser(user: User, onSuccess: () -> Unit) {
        db.collection("users")
            .document(user.id)
            .set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                Log.e("registerUser", "Error registering user", e)
            }
    }

    fun getFavoriteTeams(): Flow<List<Team>> = callbackFlow {
        val listener = db.collection("teams")
            .whereArrayContains("favorite", userId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("getFavoriteTeams", "Error fetching favorite teams", exception)
                    trySend(emptyList())
                } else if (snapshot != null) {
                    val favoriteTeams = snapshot.documents.mapNotNull { document ->
                        document.toObject(Team::class.java)?.apply { id = document.id }
                    }
                    trySend(favoriteTeams)
                }
            }
        awaitClose { listener.remove() }
    }

    fun getNotFavoriteTeams(): Flow<List<Team>> = callbackFlow {
        val listener = db.collection("teams")
            .whereArrayContains("memberIds", userId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("getNotFavoriteTeams", "Error fetching not favorite teams", exception)
                    trySend(emptyList())
                } else if (snapshot != null) {
                    val memberTeams = snapshot.documents.mapNotNull { document ->
                        document.toObject(Team::class.java)?.apply { id = document.id }
                    }
                    val notFavoriteTeams = memberTeams.filter { team ->
                        !team.favorite.contains(userId)
                    }
                    trySend(notFavoriteTeams)
                }
            }
        awaitClose { listener.remove() }
    }

    fun toggleFavorite(idTeam: String, newFavorite: Boolean) {
        val updateTask = if (newFavorite) {
            db.collection("teams")
                .document(idTeam)
                .update("favorite", FieldValue.arrayUnion(userId))
        } else {
            db.collection("teams")
                .document(idTeam)
                .update("favorite", FieldValue.arrayRemove(userId))
        }

        updateTask.addOnFailureListener { e ->
            Log.e("toggleFavorite", "Error updating favorite status", e)
        }
    }

    fun setMemberProfile(user: User, userId: String) {
        db.collection("users")
            .document(userId)
            .set(user)
            .addOnFailureListener { e ->
                Log.e("setMemberProfile", "Error setting member profile", e)
            }
    }

    fun getTaskMembers(taskId: String): Flow<List<User?>> = callbackFlow {
        val listener = db.collection("tasks")
            .document(taskId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("getTaskMembers", "Error fetching task members", exception)
                    trySend(emptyList()).isSuccess
                } else if (snapshot != null) {
                    val memberIds = snapshot.toObject(Task::class.java)?.memberIds ?: emptyList()
                    val memberFlows = memberIds.map { memberId -> getMember(memberId) }
                    combine(memberFlows) { members -> members.toList() }
                        .onEach { members -> trySend(members).isSuccess }
                        .launchIn(this)
                }
            }
        awaitClose { listener.remove() }
    }

    fun getNumberOfTeamTask(idTeam: String): Flow<Int> = callbackFlow {
        val listener = db.collection("tasks")
            .whereEqualTo("teamId", idTeam)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("getNumberOfTeamTask", "Error fetching number of team tasks", exception)
                    trySend(0)
                } else if (snapshot != null) {
                    val numberOfTasks = snapshot.documents.size
                    trySend(numberOfTasks)
                }
            }
        awaitClose { listener.remove() }
    }


    fun getTasksOfTeam(teamId: String, category: String? = null, creationDate: Timestamp? = null, dueDate: Timestamp? = null, name: String? = null): Flow<List<Task>> = callbackFlow {
        val listener = db.collection("tasks")
            .whereEqualTo("teamId", teamId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("getTasksOfTeam", "Error fetching tasks of team", exception)
                    trySend(emptyList())
                } else if (snapshot != null) {
                    val tasks = snapshot.documents.mapNotNull { document ->
                        document.toObject(Task::class.java)?.apply { id = document.id }
                    }
                    when {
                        category?.isNotBlank() == true && creationDate == null && dueDate == null && name?.isBlank() == true -> {
                            val filteredTasks = if (category == "filter by repeat") {
                                tasks.filter { it.date == null && it.repeat != "no repeat" }
                            } else {
                                tasks.filter { task -> task.category.any { it.name.contains(category) } }
                            }
                            trySend(filteredTasks)
                        }
                        category?.isBlank() == true && creationDate == null && dueDate == null && name?.isNotBlank() == true -> {
                            val filteredTasks = tasks.filter { it.title.contains(name, ignoreCase = true) }
                            trySend(filteredTasks)
                        }
                        category?.isBlank() == true && creationDate != null && dueDate == null -> {
                            val filteredTasks = tasks.filter { task ->
                                task.creationDate.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() ==
                                        creationDate.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                            }
                            trySend(filteredTasks)
                        }
                        category?.isBlank() == true && creationDate == null && dueDate != null -> {
                            val filteredTasks = tasks.filter { it.date == dueDate && it.repeat == "no repeat" }
                            trySend(filteredTasks)
                        }
                        else -> trySend(tasks)
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    fun getTeamMembers(teamId: String): Flow<List<User?>> = callbackFlow {
        if (teamId.isNotEmpty()) {
            val listener = db.collection("teams")
                .document(teamId)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        Log.e("getTeamMembers", "Error fetching team members", exception)
                        trySend(emptyList())
                    } else if (snapshot != null) {
                        val memberIds = snapshot.toObject(Team::class.java)?.memberIds ?: emptyList()
                        val memberFlows = memberIds.map { memberId -> getMember(memberId) }
                        combine(memberFlows) { members -> members.toList() }
                            .onEach { members -> trySend(members) }
                            .launchIn(this)
                    }
                }
            awaitClose { listener.remove() }
        } else {
            trySend(emptyList())
            awaitClose()
        }
    }

    fun getMemberTeams(memberId: String): Flow<List<Team>> = callbackFlow {
        val listener = db.collection("teams")
            .whereArrayContains("memberIds", memberId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("getMemberTeams", "Error fetching member teams", exception)
                    trySend(emptyList())
                } else if (snapshot != null) {
                    val teams = snapshot.documents.mapNotNull { document ->
                        document.toObject(Team::class.java)?.apply { id = document.id }
                    }
                    trySend(teams).isSuccess
                }
            }
        awaitClose { listener.remove() }
    }

    fun getTaskByMember(memberId: String, category: String? = null, creationDate: Timestamp? = null, dueDate: Timestamp? = null, name: String? = null): Flow<List<Task>> = callbackFlow {
        val listener = db.collection("tasks")
            .whereArrayContains("memberIds", memberId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("getTaskByMember", "Error fetching tasks by member", exception)
                    trySend(emptyList())
                } else if (snapshot != null) {
                    val tasks = snapshot.documents.mapNotNull { document ->
                        document.toObject(Task::class.java)?.apply { id = document.id }
                    }
                    when {
                        category?.isNotBlank() == true && creationDate == null && dueDate == null && name?.isBlank() == true -> {
                            val filteredTasks = if (category == "filter by repeat") {
                                tasks.filter { it.memberIds.contains(memberId) && it.date == null && it.repeat != "no repeat" }
                            } else {
                                tasks.filter { task -> task.category.any { it.name.contains(category) } && task.memberIds.contains(memberId) }
                            }
                            trySend(filteredTasks)
                        }
                        category?.isBlank() == true && creationDate == null && dueDate == null && name?.isNotBlank() == true -> {
                            val filteredTasks = tasks.filter { it.memberIds.contains(memberId) && it.title.contains(name, ignoreCase = true) }
                            trySend(filteredTasks)
                        }
                        category?.isBlank() == true && creationDate != null && dueDate == null -> {
                            val filteredTasks = tasks.filter { task ->
                                task.creationDate.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() ==
                                        creationDate.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() && task.memberIds.contains(memberId)
                            }
                            trySend(filteredTasks)
                        }
                        category?.isBlank() == true && creationDate == null && dueDate != null -> {
                            val filteredTasks = tasks.filter { it.date == dueDate && it.repeat == "no repeat" && it.memberIds.contains(memberId) }
                            trySend(filteredTasks)
                        }
                        else -> trySend(tasks)
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    fun getMemberAchievement(memberId: String): Flow<MemberAchievement> {
        val memberTeamsFlow = getMemberTeams(memberId)
        val memberTasksFlow = getTaskByMember(memberId)
        return combine(memberTeamsFlow, memberTasksFlow) { memberTeams, memberTasks ->
            val totalTasksAssigned = memberTasks.size
            val totalTasksCompleted = memberTasks.count { it.status == "Completed" }
            val totalTeams = memberTeams.size
            MemberAchievement(
                totalTasksAssigned = totalTasksAssigned,
                totalTasksCompleted = totalTasksCompleted,
                totalTeams = totalTeams
            )
        }
    }

    fun getTeamAchievement(teamId: String): Flow<MemberAchievement> {
        val tasksFlow = getTasksOfTeam(teamId)
        return tasksFlow.map { tasks ->
            val totalTasksAssigned = tasks.size
            val totalTasksCompleted = tasks.count { it.status == "Completed" }
            MemberAchievement(
                totalTasksAssigned = totalTasksAssigned,
                totalTasksCompleted = totalTasksCompleted,
                totalTeams = 0
            )
        }
    }

    private fun getTaskByMemberAndTeam(memberId: String, teamId: String): Flow<List<Task>> = callbackFlow {
        val listener = db.collection("tasks")
            .whereEqualTo("teamId", teamId)
            .whereArrayContains("memberIds", memberId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("getTaskByMemberAndTeam", "Error fetching tasks by member and team", exception)
                    trySend(emptyList())
                } else if (snapshot != null) {
                    val tasks = snapshot.documents.mapNotNull { document ->
                        document.toObject(Task::class.java)?.apply { id = document.id }
                    }
                    trySend(tasks)
                }
            }
        awaitClose { listener.remove() }
    }

    fun getMemberAchievementsOfTeam(teamId: String, memberId: String): Flow<MemberAchievement> {
        val memberTasksFlow = getTaskByMemberAndTeam(memberId, teamId)
        return memberTasksFlow.map { memberTasks ->
            val totalTasksAssigned = memberTasks.size
            val totalTasksCompleted = memberTasks.count { it.status == "Completed" }
            MemberAchievement(
                totalTasksAssigned = totalTasksAssigned,
                totalTasksCompleted = totalTasksCompleted,
                totalTeams = 0
            )
        }
    }

    fun addTeam(team: Team, onSuccess: (String) -> Unit) {
        db.collection("teams")
            .add(team)
            .addOnSuccessListener { documentReference ->
                val teamId = documentReference.id
                onSuccess(teamId)
            }
            .addOnFailureListener { e ->
                Log.e("addTeam", "Error adding team", e)
            }
    }

    fun updateTeamMembers(teamId: String, users: List<User>) {
        db.collection("teams")
            .document(teamId)
            .update("memberIds", users.map { it.id })
            .addOnFailureListener { e ->
                Log.e("updateTeamMembers", "Error updating team members", e)
            }
    }

    fun leaveTeam(teamId: String, memberId: String = userId) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val teamMembers = getTeamMembers(teamId).first()
                val newMemberIds = teamMembers.mapNotNull { it?.id }.filter { it != memberId }
                db.collection("teams")
                    .document(teamId)
                    .update("memberIds", newMemberIds)
                    .await()
            } catch (e: Exception) {
                Log.e("leaveTeam", "Error leaving team", e)
            }
        }
    }

    fun addChat(chat: Chat, onSuccess: (String) -> Unit) {
        db.collection("chats")
            .add(chat)
            .addOnSuccessListener { documentReference ->
                val chatId = documentReference.id
                onSuccess(chatId)
            }
            .addOnFailureListener { e ->
                Log.e("addChat", "Error adding chat", e)
            }
    }

    fun deleteTeam(teamId: String) {
        db.collection("teams")
            .document(teamId)
            .delete()
            .addOnFailureListener { e ->
                Log.e("deleteTeam", "Error deleting team", e)
            }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val chatId = getChatByTeamId(teamId).first().id
                db.collection("chats")
                    .document(chatId)
                    .delete()
                    .addOnFailureListener { e ->
                        Log.e("deleteTeam - deleteChat", "Error deleting chat", e)
                    }
                getTasksByTeamId(teamId).first()?.forEach {task->
                    val taskId = task.id
                    db.collection("tasks")
                        .document(taskId)
                        .delete()
                        .addOnFailureListener { e ->
                            Log.e("deleteTeam - deleteChat", "Error deleting chat", e)
                        }
                }
            } catch (e: Exception) {
                Log.e("deleteTeam - CoroutineScope", "Error in CoroutineScope", e)
            }
        }
    }

    fun deleteTask(taskId: String) {
        db.collection("tasks")
            .document(taskId)
            .delete()
            .addOnFailureListener { e ->
                Log.e("deleteTask", "Error deleting task", e)
            }
    }

    fun getTaskOfTeamByStatus(teamId: String, taskStatus: String): Flow<List<Task>> = callbackFlow {
        val listener = db.collection("tasks")
            .whereEqualTo("teamId", teamId)
            .whereEqualTo("status", taskStatus)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    val tasks = snapshot.documents.mapNotNull { document ->
                        document.toObject(Task::class.java)?.apply { id = document.id }
                    }
                    trySend(tasks).isSuccess
                } else {
                    Log.e("getTaskOfTeamByStatus", exception.toString())
                    trySend(emptyList()).isSuccess
                }
            }
        awaitClose { listener.remove() }
    }

    fun getTaskById(taskId: String): Flow<Task?> = callbackFlow {
        if (taskId.isNotEmpty()) {
            val listener = db.collection("tasks")
                .document(taskId)
                .addSnapshotListener { snapshot, exception ->
                    if (snapshot != null) {
                        val task = snapshot.toObject(Task::class.java)?.apply { id = snapshot.id }
                        trySend(task).isSuccess
                    } else {
                        Log.e("getTaskById", exception.toString())
                    }
                }
            awaitClose { listener.remove() }
        } else {
            trySend(null).isSuccess
            awaitClose()
        }
    }

    private fun getTasksByTeamId(teamId: String): Flow<List<Task>?> = callbackFlow {
        val listener = db.collection("tasks")
            .whereEqualTo("teamId", teamId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("getTasksByTeamId", "Error getting tasks", exception)
                    trySend(null).isSuccess
                    return@addSnapshotListener
                }

                val tasks = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Task::class.java)?.apply { id = document.id }
                }
                trySend(tasks).isSuccess
            }

        awaitClose { listener.remove() }
    }


    fun updateTaskTitle(taskId: String, title: String) {
        db.collection("tasks")
            .document(taskId)
            .update("title", title)
            .addOnFailureListener { e -> Log.e("updateTaskTitle", e.toString()) }
    }

    fun addHistory(taskId: String, history: History) {
        db.collection("tasks")
            .document(taskId)
            .update("history", FieldValue.arrayUnion(history))
            .addOnFailureListener { e -> Log.e("addHistory", e.toString()) }
    }

    fun updateDueDate(taskId: String, dueDate: Timestamp?) {
        db.collection("tasks")
            .document(taskId)
            .update("date", dueDate)
            .addOnFailureListener { e -> Log.e("updateDueDate", e.toString()) }
    }

    fun updateRepeat(taskId: String, repeat: String) {
        db.collection("tasks")
            .document(taskId)
            .update("repeat", repeat)
            .addOnFailureListener { e -> Log.e("updateRepeat", e.toString()) }
    }

    fun getRepeat(taskId: String): Flow<String?> = callbackFlow {
        val listener = db.collection("tasks")
            .document(taskId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    val task = snapshot.toObject(Task::class.java)
                    trySend(task?.repeat).isSuccess
                } else {
                    Log.e("getRepeat", exception.toString())
                }
            }
        awaitClose { listener.remove() }
    }

    fun getDueDate(taskId: String): Flow<Timestamp?> = callbackFlow {
        val listener = db.collection("tasks")
            .document(taskId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    val task = snapshot.toObject(Task::class.java)
                    trySend(task?.date).isSuccess
                } else {
                    Log.e("getDueDate", exception.toString())
                }
            }
        awaitClose { listener.remove() }
    }

    fun setStatus(taskId: String, status: String) {
        db.collection("tasks")
            .document(taskId)
            .update("status", status)
            .addOnFailureListener { e -> Log.e("setStatus", e.toString()) }
    }

    fun updateTaskMembers(taskId: String, users: MutableList<User?>) {
        db.collection("tasks")
            .document(taskId)
            .update("memberIds", users.map { it?.id ?: "" })
            .addOnFailureListener { e -> Log.e("updateTaskMembers", e.toString()) }
    }

    fun updateTaskDescription(taskId: String, description: String) {
        db.collection("tasks")
            .document(taskId)
            .update("description", description)
            .addOnFailureListener { e -> Log.e("updateTaskDescription", e.toString()) }
    }

    fun removeAttachmentsImage(taskId: String, image: String) {
        db.collection("tasks")
            .document(taskId)
            .update("attachmentsImage", FieldValue.arrayRemove(image))
            .addOnFailureListener { e -> Log.e("removeAttachmentsImage", e.toString()) }
    }

    fun setAttachmentsImage(taskId: String, image: String) {
        db.collection("tasks")
            .document(taskId)
            .update("attachmentsImage", FieldValue.arrayUnion(image))
            .addOnFailureListener { e -> Log.e("setAttachmentsImage", e.toString()) }
    }

    fun removeAttachmentsLink(taskId: String, link: String) {
        db.collection("tasks")
            .document(taskId)
            .update("attachmentsLink", FieldValue.arrayRemove(link))
            .addOnFailureListener { e -> Log.e("removeAttachmentsLink", e.toString()) }
    }

    fun setLinkAttachment(taskId: String, link: String) {
        db.collection("tasks")
            .document(taskId)
            .update("attachmentsLink", FieldValue.arrayUnion(link))
            .addOnFailureListener { e -> Log.e("setLinkAttachment", e.toString()) }
    }

    fun setComments(taskId: String, comment: Comment) {
        db.collection("tasks")
            .document(taskId)
            .update("comment", FieldValue.arrayUnion(comment))
            .addOnFailureListener { e -> Log.e("setComments", e.toString()) }
    }

    fun addCategory(taskId: String, category: Category) {
        db.collection("tasks")
            .document(taskId)
            .update("category", FieldValue.arrayUnion(category))
            .addOnFailureListener { e -> Log.e("addCategory", e.toString()) }
    }

    fun deleteCategory(taskId: String, category: Category) {
        db.collection("tasks")
            .document(taskId)
            .update("category", FieldValue.arrayRemove(category))
            .addOnFailureListener { e -> Log.e("deleteCategory", e.toString()) }
    }

    fun setPictureTeam(image: String, type: String, teamId: String) {
        db.collection("teams")
            .document(teamId)
            .update(mapOf("imageType" to type, "image" to image))
            .addOnFailureListener { e -> Log.e("setPictureTeam", e.toString()) }
    }

    fun sendChatMessage(chatId: String, message: TypeMessage) {
        db.collection("chats")
            .document(chatId)
            .update("chat", FieldValue.arrayUnion(message))
            .addOnFailureListener { e -> Log.e("sendChatMessage", e.toString()) }
    }

    private fun getChatByTeamId(teamId: String): Flow<Chat> = callbackFlow {
        val listener = db.collection("chats")
            .whereEqualTo("groupID", teamId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    val chat = snapshot.documents.mapNotNull { document ->
                        document.toObject(Chat::class.java)?.apply { id = document.id }
                    }
                    chat.firstOrNull()?.let { trySend(it).isSuccess }
                } else {
                    Log.e("getChatByTeamId", exception.toString())
                }
            }
        awaitClose { listener.remove() }
    }

    fun getChatById(chatId: String): Flow<Chat?> = callbackFlow {
        if (chatId.isNotEmpty()) {
            val listener = db.collection("chats")
                .document(chatId)
                .addSnapshotListener { snapshot, exception ->
                    if (snapshot != null) {
                        val chat = snapshot.toObject(Chat::class.java)?.apply { id = snapshot.id }
                        trySend(chat).isSuccess
                    }
                    else {
                        Log.e("getChatById", exception.toString())
                    }
                }
            awaitClose { listener.remove() }
        } else {
            trySend(null).isSuccess
            awaitClose()
        }
    }

    fun getListOfAllMembersWithYouHaveTeamInCommon(memberId: String = userId): Flow<List<User>> = callbackFlow {
        val teamsListener = db.collection("teams")
            .whereArrayContains("memberIds", memberId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    val teamIds = snapshot.documents.mapNotNull { it.id }
                    val memberFlows = teamIds.map { teamId -> getTeamMembers(teamId) }
                    combine(memberFlows) { membersList ->
                        val allMembers = mutableSetOf<User>()
                        runBlocking {
                            membersList.forEach { memberList ->
                                memberList.filterNotNull().forEach { member ->
                                    if (member.id != memberId && !hasChatWithMember(member.id)) {
                                        allMembers.add(member)
                                    }
                                }
                            }
                        }
                        allMembers.toList()
                    }.onEach { allMembers ->
                        trySend(allMembers).isSuccess
                    }.launchIn(this)
                } else {
                    Log.e("getListOfAllMembersWithYouHaveTeamInCommon", exception.toString())
                    trySend(emptyList()).isSuccess
                }
            }
        awaitClose { teamsListener.remove() }
    }

    private suspend fun hasChatWithMember(memberId: String): Boolean {
        return try {
            val querySnapshot = db.collection("chats")
                .whereEqualTo("userID", userId)
                .whereEqualTo("memberID", memberId)
                .get()
                .await()
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.e("hasChatWithMember", e.toString())
            false
        }
    }

    fun makeAdmin(teamId: String, memberId: String) {
        db.collection("teams")
            .document(teamId)
            .update("admins", FieldValue.arrayUnion(memberId))
            .addOnFailureListener { e -> Log.e("makeAdmin", e.toString()) }
    }

    fun removeAdmin(teamId: String, memberId: String) {
        db.collection("teams")
            .document(teamId)
            .update("admins", FieldValue.arrayRemove(memberId))
            .addOnFailureListener { e -> Log.e("removeAdmin", e.toString()) }
    }

    fun getAllTeams(): Flow<List<Team>> = callbackFlow {
        val listener = db.collection("teams")
            .whereArrayContains("memberIds", userId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    val teams = snapshot.documents.mapNotNull { document ->
                        document.toObject(Team::class.java)?.apply { id = document.id }
                    }
                    trySend(teams).isSuccess
                } else {
                    Log.e("getAllTeams", exception.toString())
                    trySend(emptyList()).isSuccess
                }
            }
        awaitClose { listener.remove() }
    }

    fun getAllChats(): Flow<List<Chat>?> = callbackFlow {
        val teamIds = mutableListOf<String>()

        val teamsListener = db.collection("teams")
            .whereArrayContains("memberIds", userId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("getAllChats", exception.toString())
                    trySend(null).isSuccess
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    teamIds.clear()
                    teamIds.addAll(snapshot.documents.map { it.id })

                    if (teamIds.isEmpty()) {
                        trySend(null).isSuccess
                        return@addSnapshotListener
                    }

                    db.collection("chats")
                        .whereIn("groupID", teamIds)
                        .addSnapshotListener { groupSnapshot, groupException ->
                            if (groupException != null) {
                                Log.e("getAllChats", groupException.toString())
                                trySend(null).isSuccess
                                return@addSnapshotListener
                            }

                            val groupChats = groupSnapshot?.documents?.mapNotNull { document ->
                                document.toObject(Chat::class.java)?.apply { id = document.id }
                            }?.toMutableList() ?: mutableListOf()

                            db.collection("chats")
                                .whereEqualTo("memberID", userId)
                                .addSnapshotListener { memberSnapshot, memberException ->
                                    if (memberException != null) {
                                        Log.e("getAllChats", memberException.toString())
                                        trySend(null).isSuccess
                                        return@addSnapshotListener
                                    }

                                    val memberChats = memberSnapshot?.documents?.mapNotNull { document ->
                                        document.toObject(Chat::class.java)?.apply { id = document.id }
                                    } ?: emptyList()

                                    groupChats.addAll(memberChats)

                                    db.collection("chats")
                                        .whereEqualTo("userID", userId)
                                        .addSnapshotListener { userSnapshot, userException ->
                                            if (userException != null) {
                                                Log.e("getAllChats", userException.toString())
                                                trySend(null).isSuccess
                                                return@addSnapshotListener
                                            }

                                            val userChats = userSnapshot?.documents?.mapNotNull { document ->
                                                document.toObject(Chat::class.java)?.apply { id = document.id }
                                            } ?: emptyList()

                                            groupChats.addAll(userChats)

                                            groupChats.forEach {
                                                if (it.memberID != null) {
                                                    if (it.memberID == userId) {
                                                        val tmp = it.memberID
                                                        it.memberID = it.userID
                                                        it.userID = tmp
                                                    }
                                                }
                                            }
                                            trySend(groupChats).isSuccess
                                        }
                                }
                        }
                } else {
                    trySend(null).isSuccess
                }
            }
        awaitClose { teamsListener.remove() }
    }



    fun insertTask(task: Task) {
        db.collection("tasks")
            .add(task)
            .addOnFailureListener { e -> Log.e("insertTask", e.toString()) }
    }

    fun getTeamById(teamId: String): Flow<Team?> = callbackFlow {
        if (teamId.isNotEmpty()) {
            val listener = db.collection("teams")
                .document(teamId)
                .addSnapshotListener { snapshot, exception ->
                    if (snapshot != null) {
                        val team = snapshot.toObject(Team::class.java)?.apply { id = snapshot.id }
                        trySend(team).isSuccess
                    } else {
                        Log.e("getTeamById", exception.toString())
                    }
                }
            awaitClose { listener.remove() }
        } else {
            trySend(null).isSuccess
            awaitClose()
        }
    }

    fun updateTeam(teamId: String, team: Team) {
        db.collection("teams")
            .document(teamId)
            .set(team)
            .addOnFailureListener { e -> Log.e("updateTeam", e.toString()) }
    }

    fun setPictureMember(image: String, type: String, memberId: String) {
        db.collection("users")
            .document(memberId)
            .update(mapOf("profilePictureType" to type, "profilePicture" to image))
            .addOnFailureListener { e -> Log.e("setPictureMember", e.toString()) }
    }

    fun addTeamMember(teamId: String) {
        db.collection("teams")
            .document(teamId)
            .update("memberIds", FieldValue.arrayUnion(userId))
            .addOnFailureListener { e -> Log.e("addTeamMember", e.toString()) }
    }

}