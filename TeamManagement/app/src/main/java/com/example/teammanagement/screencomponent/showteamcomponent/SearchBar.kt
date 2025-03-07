package com.example.teammanagement.screencomponent.showteamcomponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammanagement.FactoryClass
import com.example.teammanagement.viewmodels.ListOfTeamsViewModel
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.teammanagement.screencomponent.teamtaskcomponent.rememberKeyboardState
import com.example.teammanagement.utils.isVisible

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    vm: ListOfTeamsViewModel = viewModel(factory = FactoryClass(LocalContext.current))
    ) {
    val isKeyboardOpen by rememberKeyboardState()
    isVisible = !isKeyboardOpen
    Box(modifier = Modifier.padding(vertical = 16.dp)){
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp),
            color = Color.Black,
            shape = RoundedCornerShape(40.dp)
        ) {
            TextField(
                value = vm.searchText,
                onValueChange = vm::setSearchTexts,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Search...") },
                trailingIcon = {
                    if (vm.searchText.isEmpty()) {
                        IconButton(onClick = { /* Handle search icon click */ }) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.ic_menu_search),
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    }else{
                        IconButton(onClick = { vm.setSearchTexts("") }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    }

                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }

}