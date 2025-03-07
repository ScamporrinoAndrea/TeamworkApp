package com.example.teammanagement.dataclass
/*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teammanagement.R
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.BlueTrasp
import com.example.teammanagement.ui.theme.Orange
import com.example.teammanagement.ui.theme.OrangeTrasp
import com.example.teammanagement.ui.theme.Purple40
import com.example.teammanagement.ui.theme.Purple40Trasp
import java.time.LocalDate
import java.time.LocalDateTime


fun getListOfTasks(): List<Task> {
    return listOf(
        Task(
            id = 1,
            teamId = 1,
            title = "Script Breakdown",
            memberIds = listOf(4, 2),
            status = "Completed",
            description = "Identify key scenes to be filmed. Review shot lists and storyboard.",
            date = LocalDate.now(),
            creationDate = LocalDate.of(2024, 3, 15),
            repeat = "each week",
            comments = mutableListOf(
                Pair(0, Pair("I will arrive 5 min later", LocalDateTime.of(2024, 4, 20, 14, 15))),
                Pair(1, Pair("No problem!", LocalDateTime.of(2024, 4, 20, 14, 15)))
            ),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Giovanna", "12/03/2024 15:09")),
            category = mutableMapOf(
                1 to mutableMapOf("Meeting" to Pair(Orange, OrangeTrasp)),
                2 to mutableMapOf("Script" to Pair(Orange, OrangeTrasp)),
            )
        ),
        Task(
            id = 2,
            teamId = 1,
            title = "Morning meeting: 9PM",
            memberIds = listOf(1),
            status = "Completed",
            description = "Review the weekly's schedule",
            date = LocalDate.now(),
            creationDate = LocalDate.of(2024, 3, 12),
            repeat = "each week",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Edoardo", "12/03/2024 17:09")),
            category = mutableMapOf(
                1 to mutableMapOf("Meeting" to Pair(Orange, OrangeTrasp))
            )
        ),
        Task(
            id = 3,
            teamId = 1,
            title = "Filming Scene 1",
            memberIds = listOf(4),
            status = "Completed",
            description = "Execute the first scene. Capture multiple takes as needed.",
            date = LocalDate.of(2024, 11, 12),
            creationDate = LocalDate.of(2024, 4, 12),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Luca", "12/04/2024 15:09")),
            category = mutableMapOf(
                1 to mutableMapOf("Design" to Pair(Orange, OrangeTrasp))
            )
        ),
        Task(
            id = 4,
            teamId = 1,
            title = "Filming Scene 2",
            memberIds = listOf(0,4),
            status = "Deleted",
            description = "Transition to the next location or set. Continue filming according to the schedule.",
            date = LocalDate.of(2023, 12, 14),
            creationDate = LocalDate.of(2024, 3, 12),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Matteo", "12/03/2024 15:30")),
            category = mutableMapOf(
                1 to mutableMapOf("Testing" to Pair(Orange, OrangeTrasp))
            )
        ),
        Task(
            id = 5,
            teamId = 0,
            title = "Daily Wrap-Up",
            memberIds = listOf(4),
            status = "Pending",
            description = "Review footage captured throughout the day.",
            date = null,
            creationDate = LocalDate.of(2024, 3, 15),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Giovanni", "15/03/2024 14:09")),
            category = mutableMapOf(
                1 to mutableMapOf("Deployment" to Pair(Orange, OrangeTrasp))
            )
        ),
        Task(
            id = 6,
            teamId = 1,
            title = "Post-Production Meeting",
            memberIds = emptyList(),
            status = "In Progress",
            description = "Discuss the day's progress with editors.",
            date = null,
            creationDate = LocalDate.of(2024, 3, 17),
            repeat = "each month",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Marco", "17/03/2024 15:09")),
            category = mutableMapOf(
                1 to mutableMapOf("Post-Production" to Pair(Orange, OrangeTrasp))
            )
        ),
        Task(
            id = 7,
            teamId = 1,
            title = "Equipment Check",
            memberIds = listOf(4),
            status = "In Progress",
            description = "Inspect and maintain equipment for the next day's use.",
            date = null,
            creationDate = LocalDate.of(2024, 2, 12),
            repeat = "each month",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Andrea", "12/02/2024 15:15")),
            category = mutableMapOf(
                1 to mutableMapOf("Check" to Pair(Orange, OrangeTrasp))
            )
        ),
        Task(
            id = 8,
            teamId = 1,
            title = "Planning and Scheduling",
            memberIds = listOf(4),
            status = "Completed",
            description = "Confirm call times and any special requirements.",
            date = LocalDate.now(),
            creationDate = LocalDate.of(2024, 3, 12),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Giovanna", "12/03/2024 15:19")),
            category = mutableMapOf(
            )
        ),
        Task(
            id = 9,
            teamId = 1,
            title = "End-of-Day Review",
            memberIds = listOf(4),
            status = "Pending",
            description = "Update production logs and reports.",
            date = LocalDate.now(),
            creationDate = LocalDate.of(2024, 3, 12),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Giovanna", "12/03/2024 15:29")),
            category = mutableMapOf(
                1 to mutableMapOf("Meeting" to Pair(Orange, OrangeTrasp))
            )
        ),
        Task(
            id = 10,
            teamId = 1,
            title = "Wrap-Up and Clean-Up",
            memberIds = listOf(1, 4, 2),
            status = "Completed",
            description = "Clean the set/location. Ensure all equipment is stored properly.",
            date = LocalDate.now(),
            creationDate = LocalDate.of(2024, 3, 12),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Giovanna", "12/03/2024 15:29")),
            category = mutableMapOf(
                1 to mutableMapOf("Check" to Pair(Orange, OrangeTrasp))
            )
        ),
        Task(
            id = 11,
            teamId = 1,
            title = "Casting Call",
            memberIds = listOf(3, 4),
            status = "Pending",
            description = "Organize and conduct auditions for remaining roles.",
            date = LocalDate.of(2024, 4, 18),
            creationDate = LocalDate.of(2024, 3, 25),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Roberto", "25/03/2024 10:00")),
            category = mutableMapOf(
                1 to mutableMapOf("Casting" to Pair(Orange, OrangeTrasp))
            )
        ),
        Task(
            id = 12,
            teamId = 1,
            title = "Location Scouting",
            memberIds = listOf(1,4),
            status = "In Progress",
            description = "Visit potential filming locations and evaluate suitability.",
            date = LocalDate.of(2024, 5, 5),
            creationDate = LocalDate.of(2024, 4, 5),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Laura", "05/04/2024 11:30")),
            category = mutableMapOf(
                1 to mutableMapOf("Location" to Pair(Orange, OrangeTrasp))
            )
        ),
        Task(
            id = 13,
            teamId = 2,
            title = "Market Research",
            memberIds = listOf(4),
            status = "Pending",
            description = "Analyze market trends and identify target demographics.",
            date = LocalDate.of(2024, 6, 1),
            creationDate = LocalDate.of(2024, 5, 10),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Alice", "10/05/2024 09:00")),
            category = mutableMapOf(
                1 to mutableMapOf("Research" to Pair(Blue, BlueTrasp))
            )
        ),
        Task(
            id = 14,
            teamId = 2,
            title = "Pitch Deck Preparation",
            memberIds = listOf(0),
            status = "In Progress",
            description = "Prepare slides and data for investor pitch.",
            date = LocalDate.of(2024, 6, 15),
            creationDate = LocalDate.of(2024, 5, 20),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Bob", "20/05/2024 10:00")),
            category = mutableMapOf(
                1 to mutableMapOf("Presentation" to Pair(Blue, BlueTrasp))
            )
        ),
        Task(
            id = 15,
            teamId = 2,
            title = "Prototype Development",
            memberIds = listOf(0, 4),
            status = "Completed",
            description = "Develop the first prototype of the product.",
            date = LocalDate.of(2024, 7, 1),
            creationDate = LocalDate.of(2024, 5, 30),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Federica", "30/05/2024 12:00")),
            category = mutableMapOf(
                1 to mutableMapOf("Development" to Pair(Blue, BlueTrasp))
            )
        ),
        Task(
            id = 16,
            teamId = 2,
            title = "User Testing",
            memberIds = listOf(0,4),
            status = "Pending",
            description = "Conduct user testing sessions and gather feedback.",
            date = LocalDate.of(2024, 8, 1),
            creationDate = LocalDate.of(2024, 6, 15),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Dana", "15/06/2024 11:00")),
            category = mutableMapOf(
                1 to mutableMapOf("Testing" to Pair(Blue, BlueTrasp))
            )
        ),
        Task(
            id = 17,
            teamId = 2,
            title = "Marketing Campaign Launch",
            memberIds = listOf(0, 4),
            status = "In Progress",
            description = "Launch the initial marketing campaign.",
            date = LocalDate.of(2024, 9, 1),
            creationDate = LocalDate.of(2024, 7, 1),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf(),
            history = mutableListOf(Pair("Task created by Evan", "01/07/2024 14:00")),
            category = mutableMapOf(
                1 to mutableMapOf("Marketing" to Pair(Blue, BlueTrasp))
            )
        ),
        Task(
            id = 18,
            teamId = 3,
            title = "Initial Library Setup",
            memberIds = listOf(2),
            status = "In Progress",
            description = "Set up the initial project structure and repository on GitHub.",
            date = LocalDate.of(2023, 10, 1),
            creationDate = LocalDate.of(2023, 9, 20),
            repeat = "no repeat",
            comments = mutableListOf(
                Pair(0, Pair("Repository created", LocalDateTime.of(2023, 9, 20, 10, 0)))
            ),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf("https://github.com/new-python-library"),
            history = mutableListOf(Pair("Task created by Federica", "20/09/2023 10:00")),
            category = mutableMapOf(
                2 to mutableMapOf("Development" to Pair(Blue, BlueTrasp))
            )
        ),
        Task(
            id = 19,
            teamId = 3,
            title = "Write Initial Documentation",
            memberIds = listOf(3, 4),
            status = "Pending",
            description = "Draft initial documentation for the library, including installation instructions and usage examples.",
            date = LocalDate.of(2023, 11, 1),
            creationDate = LocalDate.of(2023, 10, 10),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf("https://github.com/new-python-library/docs"),
            history = mutableListOf(Pair("Task created by Federica", "10/10/2023 11:00")),
            category = mutableMapOf(
                2 to mutableMapOf("Documentation" to Pair(Blue, BlueTrasp))
            )
        ),
        Task(
            id = 20,
            teamId = 3,
            title = "Implement Core Features",
            memberIds = listOf(2, 4),
            status = "Pending",
            description = "Develop the core functionalities of the library, including essential modules and functions.",
            date = LocalDate.of(2023, 12, 1),
            creationDate = LocalDate.of(2023, 10, 15),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf("https://github.com/new-python-library/core-features"),
            history = mutableListOf(Pair("Task created by Federica", "15/10/2023 12:00")),
            category = mutableMapOf(
                2 to mutableMapOf("Development" to Pair(Blue, BlueTrasp))
            )
        ),
        Task(
            id = 21,
            teamId = 3,
            title = "Community Outreach",
            memberIds = listOf(3),
            status = "Pending",
            description = "Engage with the open-source community to gather feedback and attract contributors.",
            date = LocalDate.of(2023, 12, 15),
            creationDate = LocalDate.of(2023, 10, 20),
            repeat = "monthly",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf("https://github.com/new-python-library/community"),
            history = mutableListOf(Pair("Task created by Federica", "20/10/2023 13:00")),
            category = mutableMapOf(
                2 to mutableMapOf("Community" to Pair(Blue, BlueTrasp))
            )
        ),
        Task(
            id = 22,
            teamId = 3,
            title = "Code Review",
            memberIds = listOf(2, 3, 4),
            status = "Pending",
            description = "Review code submissions and ensure they meet the project's standards and guidelines.",
            date = LocalDate.of(2023, 11, 1),
            creationDate = LocalDate.of(2023, 10, 25),
            repeat = "weekly",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf("https://github.com/new-python-library/code-reviews"),
            history = mutableListOf(Pair("Task created by Federica", "25/10/2023 14:00")),
            category = mutableMapOf(
                2 to mutableMapOf("Review" to Pair(Blue, BlueTrasp))
            )
        ),
        Task(
            id = 23,
            teamId = 3,
            title = "Release v0.1.0",
            memberIds = listOf(4),
            status = "Pending",
            description = "Prepare and publish the first release of the library, version 0.1.0.",
            date = LocalDate.of(2024, 1, 15),
            creationDate = LocalDate.of(2023, 12, 20),
            repeat = "no repeat",
            comments = mutableListOf(),
            attachmentsImage = mutableListOf(),
            attachmentsLink = mutableListOf("https://github.com/new-python-library/releases"),
            history = mutableListOf(Pair("Task created by Federica", "20/12/2023 15:00")),
            category = mutableMapOf(
                2 to mutableMapOf("Release" to Pair(Blue, BlueTrasp))
            )
        )
    )
}
fun getListOfTeams(): List<Team> {
    return listOf(
        Team(
            id = 1,
            name = "Cinema srl",
            admins = mutableListOf(4),
            memberIds = listOf(0, 1, 2, 4),
            favorite = true,
            description = "We are the best company in cinema industry!",
            creationDate = LocalDate.of(2020, 1, 1),
            category = mutableMapOf(
                1 to mutableMapOf(
                    "Important" to Pair(Purple40, Purple40Trasp)
                ),
                2 to mutableMapOf(
                    "Meeting" to Pair(Blue, BlueTrasp)
                )
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.flat_mountains),
                contentDescription = "Group image",
                modifier = it,
                contentScale = ContentScale.Crop

            )
        },
        Team(
            id = 2,
            name = "Start up",
            memberIds = listOf(0, 3, 4),
            favorite = false,
            admins = mutableListOf(0),
            description = "We have had a very good idea!",
            creationDate = LocalDate.of(2021, 9, 15),
            category = mutableMapOf(
                2 to mutableMapOf(
                    "Review" to Pair(Blue, BlueTrasp)
                )
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.default_team_image),
                contentDescription = "Group image",
                modifier = it,
                contentScale = ContentScale.Crop
            )
        },
        Team(
            id = 3,
            name = "Open Source Project",
            memberIds = listOf(2, 3, 4),
            favorite = false,
            admins = mutableListOf(0),
            description = "A new Python library",
            creationDate = LocalDate.of(2021, 9, 15),
            category = mutableMapOf(
                2 to mutableMapOf(
                    "Review" to Pair(Blue, BlueTrasp)
                )
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.default_team_image),
                contentDescription = "Group image",
                modifier = it,
                contentScale = ContentScale.Crop
            )
        }
    )
}

fun getMemberProfile(): Int {
    return 4
}

fun getAllContacts() : List<User>{
    return listOf(
        User(
            id = 0,
            name = "Andrea",
            surname = "Scamporrino",
            nickname = "Andre",
            email = "andrea.scamporrino@example.com",
            location = "Milan, Italy",
            bio = "Project manager with over 10 years of experience.",
            isProfilePictureChanged = true,
            profilePicture = { modifier, _ ->
                Image(
                    painter = painterResource(id = R.drawable.profilouomo),
                    contentDescription = "group image",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .size(30.dp)
                        .clip(CircleShape)
                )
            }
        ),
        User(
            id = 1,
            name = "Giovanna",
            surname = "Rossi",
            nickname = "Giova",
            email = "giovanna.rossi@example.com",
            location = "Naples, Italy",
            bio = "Graphic designer with a keen eye for detail.",
            isProfilePictureChanged = true,
            profilePicture = { modifier, _ ->
                Image(
                    painter = painterResource(id = R.drawable.profilodonna),
                    contentDescription = "group image",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .size(30.dp)
                        .clip(CircleShape)
                )
            }
        ),
        User(
            id = 2,
            name = "Luca",
            surname = "Garino",
            nickname = "Luck",
            email = "luca.garino@example.com",
            location = "Turin, Italy",
            bio = "Marketing specialist with a creative mindset.",
            isProfilePictureChanged = false,
            profilePicture = {modifier, fontSize ->
                Box(
                    modifier = modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF6495ED), Color(0xFFADD8E6)),
                                start = Offset(0f, 0f),
                                end = Offset(200f, 200f)
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "LG",
                        color = Color.White,
                        fontSize = fontSize.sp,
                        modifier = Modifier.padding(7.dp)
                    )
                }
            }
        ),
        User(
            id = 3,
            name = "Gabriele",
            surname = "Lucardi",
            nickname = "Gab",
            email = "gabriele.lucardi@example.com",
            location = "Florence, Italy",
            bio = "Student of computer science with a focus on AI.",
            isProfilePictureChanged = false,
            profilePicture = { modifier, fontSize ->
                Box(
                    modifier = modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF6495ED), Color(0xFFADD8E6)),
                                start = Offset(0f, 0f),
                                end = Offset(200f, 200f)
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "GL",
                        color = Color.White,
                        fontSize = fontSize.sp,
                        modifier = Modifier.padding(7.dp)
                    )
                }
            }
        ),
        User(
            id = 4,
            name = "Federica",
            surname = "Intini",
            nickname = "Fede",
            email = "federica.intini@example.com",
            location = "Rome, Italy",
            bio = "Software developer with a passion for mobile applications.",
            isProfilePictureChanged = true,
            profilePicture = { modifier, _ ->
                Image(
                    painter = painterResource(id = R.drawable.userimage1),
                    contentDescription = "group image",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .size(30.dp)
                        .clip(CircleShape)
                )
            }
        )

    )
}

fun getListOfChats(): List<Chat> {
    return listOf(
        Chat(
            id = 1,
            groupID = null,
            memberID = 1,
            userID = getMemberProfile(),
            chat = mutableListOf(
                TypeMessage(
                    memberID = 1,
                    message = "Hey Federica, did you catch that email from the client about the new feature request?",
                    date = LocalDateTime.of(2024, 3, 12, 12, 0),
                ),
                TypeMessage(
                    memberID = 4,
                    message = "Yeah, just saw it. Sounds like they're asking for a lot, huh?",
                    date = LocalDateTime.of(2024, 3, 12, 13, 0),
                ),
                TypeMessage(
                    memberID = 1,
                    message = "Tell me about it! But hey, that's what they pay us for, right?",
                    date = LocalDateTime.of(2024, 3, 12, 13, 11),
                ),
                TypeMessage(
                    memberID = 4,
                    message = "Yeah, just saw it. Sounds like they're asking for a lot, huh?",
                    date = LocalDateTime.of(2024, 3, 12, 13, 0),
                ),
                TypeMessage(
                    memberID = 1,
                    message = "I like your attitude, Federica. Let's get to it and impress them!",
                    date = LocalDateTime.of(2024, 3, 12, 13, 0),
                ),
                TypeMessage(
                    memberID = 4,
                    message = "You got it! Let's show them what we're capable of.",
                    date = LocalDateTime.of(2024, 3, 12, 13, 0),
                ),

                )
        ),
        Chat(
            id = 2,
            groupID = 1,
            memberID = null,
            userID = getMemberProfile(),
            chat = mutableListOf(
                TypeMessage(
                    memberID = 1,
                    message = "Hey team, just wanted to update everyone on the progress. I've finished the first draft of the presentation slides. Going to send it out for feedback now.",
                    date = LocalDateTime.of(2024, 4, 12, 12, 1),
                ),
                TypeMessage(
                    memberID = 4,
                    message = "Awesome, John! Can't wait to see it. I'll make sure to give it a thorough review.",
                    date = LocalDateTime.of(2024, 4, 12, 13, 0),
                ),
                TypeMessage(
                    memberID = 0,
                    message = "Sounds good! If you need any help with the content, just let me know.",
                    date = LocalDateTime.of(2024, 4, 1, 13, 15),
                ),
                TypeMessage(
                    memberID = 1,
                    message = "Thanks, guys! Will do.",
                    date = LocalDateTime.of(2024, 4, 1, 13, 15),
                )
            )
        ),
        Chat(
            id = 3,
            groupID = 2,
            memberID = null,
            userID = getMemberProfile(),
            chat = mutableListOf(
                TypeMessage(
                    memberID = 0,
                    message = "Hey team, quick reminder that we have our weekly sync-up meeting tomorrow at 10 AM. Agenda items: project updates, blockers, and action items.",
                    date = LocalDateTime.of(2024, 5, 12, 12, 0),
                ),
                TypeMessage(
                    memberID = 3,
                    message = "Got it! I'll make sure to have my updates ready.",
                    date = LocalDateTime.of(2024, 5, 12, 12, 0),
                ),
                TypeMessage(
                    memberID = 4,
                    message = " I might be a few minutes late, got another call scheduled right before. Is that okay?",
                    date = LocalDateTime.of(2024, 5, 12, 12, 0),
                ),
                TypeMessage(
                    memberID = 0,
                    message = "No worries, Lucas! We'll catch you up when you join.",
                    date = LocalDateTime.of(2024, 5, 13, 12, 10),
                )
            )

        ),
        Chat(
            id = 4,
            groupID = 3,
            memberID = null,
            userID = getMemberProfile(),
            chat = mutableListOf(
                TypeMessage(
                    memberID = 2,
                    message = "Hi everyone, just a heads up that our product demo is scheduled for next Tuesday at 3 PM. Please make sure your features are ready for presentation.",
                    date = LocalDateTime.of(2024, 6, 1, 9, 0),
                ),
                TypeMessage(
                    memberID = 3,
                    message = "Sounds good! I'll finalize the UI updates by then.",
                    date = LocalDateTime.of(2024, 6, 1, 9, 5),
                ),
                TypeMessage(
                    memberID = 4,
                    message = "I'm currently working on the backend integration. Should be ready by Monday.",
                    date = LocalDateTime.of(2024, 6, 1, 9, 10),
                ),
                TypeMessage(
                    memberID = 3,
                    message = "I'll prepare the slides for the demo. Let's do a quick run-through on Monday afternoon?",
                    date = LocalDateTime.of(2024, 6, 1, 9, 15),
                ),
                TypeMessage(
                    memberID = 2,
                    message = "Great idea! Let's schedule the run-through for 2 PM on Monday. See you all then!",
                    date = LocalDateTime.of(2024, 6, 1, 9, 20),
                )
            )
        )
    )


}

 */