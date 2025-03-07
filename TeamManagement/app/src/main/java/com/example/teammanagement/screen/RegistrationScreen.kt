package com.example.teammanagement.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.R
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.viewmodels.RegistrationViewModel
import com.google.firebase.auth.FirebaseUser

@Composable
fun RegistrationScreen(
    navController: NavHostController,
    user: FirebaseUser,
    vm: RegistrationViewModel = viewModel(factory = FactoryClass(LocalContext.current))
) {
    LaunchedEffect(Unit) {
        user.displayName?.let {displayName ->
            val nameAndSurname = displayName.split(" ")
            vm.setNames(nameAndSurname[0])
            vm.setSurnames(nameAndSurname[1])
            vm.setNicknames(displayName.replace(" ", ""))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.networking_collaboration_svgrepo_com),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 32.dp)
                .size(50.dp),
            colorFilter = ColorFilter.tint(Blue)
        )
        Text(
            "Registration",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )
        OutlinedTextField(
            label = { Text("* Name") },
            value = vm.name,
            onValueChange = vm::setNames,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            isError = vm.nameError.isNotBlank(),
        )
        if (vm.nameError.isNotBlank()) {
            Text(vm.nameError, color = MaterialTheme.colorScheme.error)
        }
        OutlinedTextField(
            label = { Text("* Surname") },
            value = vm.surname,
            onValueChange = vm::setSurnames,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            isError = vm.surnameError.isNotBlank(),
        )
        if (vm.surnameError.isNotBlank()) {
            Text(vm.surnameError, color = MaterialTheme.colorScheme.error)
        }
        OutlinedTextField(
            label = { Text("* Nickname") },
            value = vm.nickname,
            onValueChange = vm::setNicknames,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            isError = vm.nicknameError.isNotBlank(),
        )
        if (vm.nicknameError.isNotBlank()) {
            Text(vm.nicknameError, color = MaterialTheme.colorScheme.error)
        }
        OutlinedTextField(
            label = { Text("Bio") },
            value = vm.bio,
            onValueChange = vm::setBios,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )
        OutlinedTextField(
            label = { Text("Location") },
            value = vm.location,
            onValueChange = vm::setLocations,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )

        Button(
            onClick = { if (vm.validate())
                vm.register(navController,user) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp),
            colors = ButtonDefaults.buttonColors(Blue)
            ) {
            Text(
                "Register",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}