package com.example.teammanagement

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.teammanagement.screen.ChatMenu
import com.example.teammanagement.screen.ChatScreen
import com.example.teammanagement.screen.CreateTaskScreen
import com.example.teammanagement.screen.CreateTeamPopUp
import com.example.teammanagement.screen.EditProfile
import com.example.teammanagement.screen.EditTeamScreen
import com.example.teammanagement.screen.MyTasksScreen
import com.example.teammanagement.screen.Profile
import com.example.teammanagement.screen.RegistrationScreen
import com.example.teammanagement.screen.ShowMorePage
import com.example.teammanagement.screen.ShowTaskDetails
import com.example.teammanagement.screen.ShowTeamDetails
import com.example.teammanagement.screen.TeamMembersScreen
import com.example.teammanagement.screen.TeamTasksScreen
import com.example.teammanagement.screencomponent.profilecomponent.ShowAllAchievements
import com.example.teammanagement.utils.selectedItem
import com.example.teammanagement.screen.SignInScreen
import com.example.teammanagement.screencomponent.qrcodecomponent.CameraScreen
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun Navigation(
    auth: FirebaseAuth,
    vm: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current))
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }
    val credentialManager = CredentialManager.create(context)
    val coroutineScope = rememberCoroutineScope()
    val startDestination = if (auth.currentUser == null){
        "signIn"
    } else{
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(auth.currentUser!!.uid)
        var destination = "teams"
        userRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                destination = "registration"
            } else {
                auth.currentUser?.uid?.let { userId ->
                    vm.setUserId(userId)
                }
                destination = "teams"
            }
        }.addOnFailureListener { e ->
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
        auth.currentUser?.uid?.let { userId ->
            vm.setUserId(userId)
        }
        destination
    }
    NavHost(navController = navController, startDestination = startDestination) {
        composable("signIn") {
            SignInScreen(
                onSignInClick = {
                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(WEB_CLIENT_ID)
                        .build()

                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    coroutineScope.launch {
                        try {
                            val result = credentialManager.getCredential(
                                context = context,
                                request = request
                            )
                            val credential = result.credential
                            val googleIdTokenCredential = GoogleIdTokenCredential
                                .createFrom(credential.data)
                            val googleIdToken = googleIdTokenCredential.idToken

                            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val user = auth.currentUser
                                        user?.let {
                                            val db = FirebaseFirestore.getInstance()
                                            val userRef = db.collection("users").document(user.uid)
                                            userRef.get().addOnSuccessListener { document ->
                                                if (!document.exists()) {
                                                    navController.navigate("registration")
                                                } else {
                                                    auth.currentUser?.uid?.let { userId ->
                                                        vm.setUserId(userId)
                                                    }
                                                    navController.popBackStack()
                                                    navController.navigate("teams")
                                                }
                                            }.addOnFailureListener { e ->
                                                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                                                e.printStackTrace()
                                            }
                                        }
                                    } else {
                                        Toast.makeText(context, task.exception?.message, Toast.LENGTH_LONG).show()
                                    }
                                }
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }
                }
            )
        }
        composable("registration") {
            RegistrationScreen(navController = navController, user = auth.currentUser!!)
        }
        composable("teams") {
            ShowTeamDetails(navController = navController,actions = actions)
        }
        composable("teams/{teamId}/tasks",
        arguments = listOf( navArgument("teamId"){type= NavType.StringType})
        ) {entry ->
            entry.arguments?.getString("teamId")?.let { TeamTasksScreen( teamId = it, navController = navController, actions = actions) }
        }
        composable("teams/{teamId}/edit",
        arguments = listOf( navArgument("teamId"){type= NavType.StringType})
        ){entry ->
            entry.arguments?.getString("teamId")?.let {EditTeamScreen(
                navController = navController,
                actions = actions, teamId = it) }
        }
        composable("profile") {
            Profile(
                teamId = "",
                memberId = "",
                actions = actions,
                navController = navController,
                onSignOut = {
                    auth.signOut()
                    coroutineScope.launch {
                        credentialManager.clearCredentialState(ClearCredentialStateRequest())
                    }
                    navController.popBackStack()
                    navController.navigate("signIn")
                }
            )
        }
        composable("profile/edit") {
            EditProfile(actions = actions, navController = navController, teamId = "", memberId = "")
        }
        composable("profile/achievements")  {
            ShowAllAchievements(actions = actions, navController = navController, teamId = "", memberId = "")
        }
        composable("chat")  {
            ChatMenu(actions = actions, navController = navController)
        }
        composable("teams/new")  {
            CreateTeamPopUp(actions = actions)
        }
        composable("team/{teamId}/members" ,
                arguments = listOf( navArgument("teamId"){type= NavType.StringType})
        ){entry ->
            entry.arguments?.getString("teamId")?.let { TeamMembersScreen( teamId = it, navController = navController, actions = actions) }
        }
        composable("team/{teamId}/members/{memberId}/profile" ,
            arguments = listOf(
                navArgument("teamId") { type = NavType.StringType },
                navArgument("memberId") { type = NavType.StringType }
            )
        ) { entry ->
            val teamId = entry.arguments?.getString("teamId")
            val memberId = entry.arguments?.getString("memberId")
            if (teamId != null && memberId != null) {
                Profile(
                    teamId = teamId,
                    memberId = memberId,
                    actions = actions,
                    navController = navController,
                    onSignOut = {
                        auth.signOut()
                        coroutineScope.launch {
                            credentialManager.clearCredentialState(ClearCredentialStateRequest())
                        }
                        navController.popBackStack()
                        navController.navigate("signIn")
                    }
                )
            }
        }
        composable("teams/{teamId}/tasks/taskId/{taskId}",
            arguments = listOf( navArgument("teamId"){type= NavType.StringType},
                navArgument("taskId"){type= NavType.StringType})
        ){
                entry ->
            val taskId = entry.arguments?.getString("taskId")!!
            ShowTaskDetails(navController = navController, taskId = taskId, actions = actions)
        }


        composable("teams/{teamId}/tasks/{taskStatus}/{filterByMember}",
            arguments = listOf( navArgument("teamId"){type= NavType.StringType},
                navArgument("taskStatus"){type= NavType.StringType})
        ) {entry ->
            val teamId = entry.arguments?.getString("teamId")!!
            val taskStatus = entry.arguments?.getString("taskStatus")!!
            val filterByMember = entry.arguments?.getString("filterByMember")!!.toBoolean()
            ShowMorePage( teamId = teamId, actions = actions, taskStatus = taskStatus, navController = navController, filterByMember = filterByMember)
        }

        composable(
            "chat/{chatId}",
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { entry ->
            entry.arguments?.getString("chatId")?.let { chatId ->
                ChatScreen(chatId = chatId, actions = actions, navController = navController)
            }
        }
        composable(
            "myTask/{memberId}",
            arguments = listOf(navArgument("memberId") { type = NavType.StringType })
        ) { entry ->
            entry.arguments?.getString("memberId")?.let { memberId ->
                MyTasksScreen(memberId = memberId, actions = actions, navController = navController)
            }
        }
        composable("task/new/{memberId}",
            arguments = listOf(navArgument("memberId") { type = NavType.StringType })
        ){entry ->
            entry.arguments?.getString("memberId")?.let { memberId ->
                CreateTaskScreen(memberId = memberId, actions = actions, navController = navController)
            }
        }
        composable("qrcode") {
            CameraScreen(actions)
        }
    }
}

class Actions(private val navCont: NavHostController){
    val teams: ()->Unit = {
        navCont.navigate("teams")
    }
    val chatScreen: ()->Unit = {
        navCont.navigate("chat")
    }
    val membersScreen: (String)->Unit = { teamId ->
        navCont.navigate("team/${teamId}/members")
    }
    val profile: ()->Unit = {
        navCont.navigate("profile")
    }
    val otherMemberProfile: (String, String) -> Unit = { teamId, memberId ->
        navCont.navigate("team/$teamId/members/$memberId/profile")
    }
    val editProfile: ()->Unit = {
        navCont.navigate("profile/edit")
    }

    val profileAchievement: ()->Unit = {
        navCont.navigate("profile/achievements")
    }

    val teamDetails:(String)->Unit = { teamId ->
        navCont.navigate("teams/${teamId}/tasks")
    }
    val editTeam:(String)->Unit = { teamId ->
        navCont.navigate("teams/${teamId}/edit")
    }
    val navigateBack: ()->Unit = {
        navCont.popBackStack()
    }
    val newTask: (String)->Unit = {memberId ->
        navCont.navigate("task/new/${memberId}")
    }
    val tasksPerStatus: (String, String, Boolean) -> Unit = { teamId, taskStatus, filterByMember ->
        navCont.navigate("teams/${teamId}/tasks/${taskStatus}/${filterByMember}")
    }
    val taskDetails: (String, String) -> Unit = {taskId, teamId ->
        navCont.navigate("teams/${teamId}/tasks/taskId/${taskId}")
    }
    val chatShow: (String) -> Unit = { id ->
        navCont.navigate("chat/${id}")
    }
    val myTasks: (String)->Unit = { memberId ->
        navCont.navigate("myTask/${memberId}")
    }
    val newTeam: ()->Unit = {
        navCont.navigate("teams/new")
    }
    val qrcode: ()->Unit = {
        navCont.navigate("qrcode")
    }

}

fun updateNavigationBar(navController: NavHostController) {

    val currentDestination = navController.currentDestination?.route
    selectedItem = if (currentDestination != null && currentDestination.startsWith("teams"))
        "Teams"
    else if (currentDestination != null && currentDestination.startsWith("profile"))
        "Profile"
    else if (currentDestination != null && currentDestination.startsWith("chat"))
        "Chats"
    else if (currentDestination != null && currentDestination.startsWith("task"))
        "Task"
    else
        "Teams"

}