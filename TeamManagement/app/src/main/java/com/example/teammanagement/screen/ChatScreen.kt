package com.example.teammanagement.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.teammanagement.Actions
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.GrayBackground
import com.example.teammanagement.ui.theme.White
import com.example.teammanagement.updateNavigationBar
import com.example.teammanagement.viewmodels.ChatViewModel
import androidx.compose.material.Surface
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.teammanagement.screencomponent.profilecomponent.monogram
import com.example.teammanagement.utils.ShowPicture
import com.google.firebase.Timestamp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.absoluteValue

var update by mutableStateOf(false)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String,
    vm: ChatViewModel = viewModel(factory = FactoryClass(LocalContext.current)),
    navController: NavHostController,
    actions: Actions
) {
    BackHandler {
        actions.navigateBack()
        updateNavigationBar(navController)
    }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val chats by vm.listOfChats().collectAsState(initial = null)
    val teams by vm.listOfTeams().collectAsState(initial = null)
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    var isGroup = false
    chats?.let { vm.setListOfChats(it) }
    teams?.let { vm.setListOfTeam(it) }

    val thisChat = vm.chats.find { x -> x.id == chatId }
    var title = ""
    if (thisChat?.memberID != null) {
        val contact by vm.getContact(memberID = thisChat.memberID!!).collectAsState(initial = null)
        title = contact?.name + " " + contact?.surname
    }
    if (thisChat?.groupID != null) {
        title = vm.teams.find { it.id == thisChat.groupID }?.name ?: String()
        isGroup = true
    }

    val keyboardOpenState = rememberUpdatedState(isKeyboardOpen())

    LaunchedEffect(keyboardOpenState.value, thisChat?.chat?.size) {
        coroutineScope.launch {
            delay(100)
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { actions.navigateBack() }) {
                            Image(
                                painter = painterResource(id = R.drawable.arrow_back),
                                contentDescription = "Back",
                                modifier = Modifier.size(28.dp),
                                colorFilter = ColorFilter.tint(Blue)
                            )
                        }
                        if (thisChat?.memberID != null) {
                            val contact by vm.getContact(memberID = thisChat.memberID!!).collectAsState(initial = null)
                            contact?.let {
                                ShowPicture(
                                    imageType = it.profilePictureType,
                                    image = it.profilePicture,
                                    modifier = Modifier
                                        .padding(top = 2.dp)
                                        .size(35.dp).clip(CircleShape),
                                    monogram = monogram(it.name,it.surname),
                                    fontSize = 12
                                )
                            }
                            /*vm.getContact(thisChat.memberID).profilePicture.let {
                                it(
                                    Modifier
                                        .padding(top = 2.dp)
                                        .size(35.dp),
                                    12
                                )
                            }*/
                        } else {
                            vm.teams.find { team -> team.id == thisChat?.groupID }?.let {
                                ShowPicture(
                                    imageType = it.imageType,
                                    image = it.image,
                                    modifier = Modifier
                                        .padding(top = 2.dp)
                                        .size(35.dp)
                                        .clip(CircleShape),
                                    monogram = monogram("",""),
                                    fontSize = 12
                                )
                                /*it(
                                    Modifier
                                        .padding(top = 2.dp)
                                        .size(35.dp)
                                        .clip(CircleShape)
                                )*/
                            }
                        }
                    }
                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
                title = {
                    Text(
                        text = title,
                        modifier = Modifier.padding(4.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White),
            )
        },
        bottomBar = {
            Surface(
                elevation = 8.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = vm.messages,
                        onValueChange = vm::setMessage,
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    coroutineScope.launch {
                                        delay(400)
                                        scrollState.animateScrollTo(scrollState.maxValue)
                                    }
                                }
                            }
                            .padding(end = 8.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFFF0F0F0))
                            .padding(horizontal = 16.dp),
                        placeholder = { Text("Type your message", color = Color.Gray) },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    IconButton(
                        onClick = {
                            if (vm.messages != "") {
                                thisChat?.let { vm.sendChatMessage(it.id) }
                                coroutineScope.launch {
                                    delay(400)
                                    scrollState.animateScrollTo(scrollState.maxValue)
                                }
                            }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF1E88E5))
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White
                        )
                    }
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GrayBackground)
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        keyboardController?.hide()
                    }
            ) {

                thisChat?.let { ChatScreenContent(it.id, vm, isGroup) }
            }
            if (update) {
                ModalBottomSheet(onDismissRequest = { update = false }) {}
            }
        }
    )
}

@Composable
fun ChatScreenContent(
    chatId: String,
    vm: ChatViewModel,
    isGroup: Boolean,
) {
    val memberProfile by vm.getContact().collectAsState(initial = null)
    val chat by vm.getChatById(chatId).collectAsState(initial = null)
    if (chat != null)
        for (message in chat!!.chat) {
            memberProfile?.let { ShowMessage(message.message, message.date, message.memberID, it.id, isGroup, vm) }
        }
}

@Composable
fun ShowMessage(
    message: String,
    date: Timestamp,
    memberID: String?,
    memberProfile: String,
    isGroup: Boolean,
    vm: ChatViewModel
) {
    val isCurrentUser = memberID == memberProfile
    val member by vm.member(memberID!!).collectAsState(initial = null)
    val messageShape = if (isCurrentUser) {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 0.dp,
            bottomEnd = 16.dp,
            bottomStart = 16.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp,
            bottomStart = 16.dp
        )
    }

    val backgroundColor = if (isCurrentUser) Color(0xFF1E88E5) else Color.White
    val textColor = if (isCurrentUser) Color.White else Color.DarkGray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 6.dp,
                bottom = 6.dp,
                start = if (isCurrentUser) 60.dp else 8.dp,
                end = if (isCurrentUser) 8.dp else 60.dp
            ),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = messageShape,
            color = backgroundColor,
            shadowElevation = 4.dp,
            modifier = Modifier.padding(horizontal = 8.dp)
        ){
            Column(
                modifier = Modifier
                    .clip(messageShape)
                    .background(backgroundColor)
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                if (isGroup && !isCurrentUser) {
                    Text(
                        text = "${member?.name} ${member?.surname }",
                        color = member?.let { getColorFromId(it.id) } ?: Blue
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = message,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
                ) {
                    Text(
                        text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date.toDate()),
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                }
            }

        }
    }
}

@Composable
fun isKeyboardOpen(): Boolean {
    val insets = WindowInsets.ime.getTop(LocalDensity.current).absoluteValue
    return insets > 0
}

fun getColorFromId(id: String): Color {
    val hash = abs((id.hashCode()*12345789).hashCode())
    val red = (hash and 0xFF0000) shr 16
    val green = (hash and 0x00FF00) shr 8
    val blue = hash and 0x0000FF

    return Color(red / 255f, green / 255f, blue / 255f)
}
