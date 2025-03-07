package com.example.teammanagement.screencomponent.profilecomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.viewmodels.ProfileViewModel

@Composable
fun EditMainInfo(
    vm : ProfileViewModel = viewModel(factory = FactoryClass(LocalContext.current))
){
    Row(
        horizontalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, end = 8.dp, top = 8.dp)
        ) {
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                value = vm.nameValue,
                onValueChange = vm::setName,
                label = { Text(text = "Name") },
                isError = vm.nameError.isNotBlank(),
            )
            if (vm.nameError.isNotBlank()) {
                Text(vm.nameError, color = MaterialTheme.colorScheme.error)
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 16.dp, top = 8.dp)
        ) {
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                value = vm.surnameValue,
                onValueChange = vm::setSurname,
                label = { Text(text = "Surname") },
                isError = vm.surnameError.isNotBlank()
            )
            if (vm.surnameError.isNotBlank()) {
                Text(vm.surnameError, color = MaterialTheme.colorScheme.error)
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        value = vm.nicknameValue,
        onValueChange = vm::setNickname,
        label = { Text(text = "Nickname") },
        isError = vm.nicknameError.isNotBlank(),
    )
    if (vm.nicknameError.isNotBlank()) {
        Text(vm.nicknameError, color = MaterialTheme.colorScheme.error)
    }
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        value = vm.emailValue,
        onValueChange = vm::setEmail,
        label = { Text(text = "Email") },
        isError = vm.emailError.isNotBlank()
    )
    if (vm.emailError.isNotBlank()) {
        Text(vm.emailError, color = MaterialTheme.colorScheme.error)
    }
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        value = vm.positionValue,
        onValueChange = vm::setPosition,
        label = { Text(text = "Location") },
        isError = vm.positionError.isNotBlank()
    )
    if (vm.positionError.isNotBlank()) {
        Text(vm.positionError, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun EditBio(
    vm : ProfileViewModel = viewModel(factory = FactoryClass(LocalContext.current))
){
    Row(
        modifier = Modifier.padding(horizontal = 16.dp)
    ){
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "Bio Icon" ,
            modifier = Modifier.size(25.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            "Biography",
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        value = vm.bioValue,
        onValueChange = vm::setBio,
        label = { Text(text = "") },
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
    )
}