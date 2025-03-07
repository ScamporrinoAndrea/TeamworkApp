package com.example.teammanagement

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LoginPage(onSignInClick: () -> Unit) {

    /*
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId("737168430174-piqa18odd7hugrpvur216m3hc4rl6mkh.apps.googleusercontent.com")
        .setAutoSelectEnabled(true)
        //.setNonce(<nonce string to use when generating a Google ID token>)
        .build()

    val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption(
        serverClientId = "737168430174-piqa18odd7hugrpvur216m3hc4rl6mkh.apps.googleusercontent.com"
    )


    val request: GetCredentialRequest = GetCredentialRequest(
        credentialOptions = listOf(signInWithGoogleOption)
    )
    val activityContext = LocalContext.current

    val credentialManager: CredentialManager = CredentialManager.create(LocalContext.current)

    fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {

            // Passkey credential
            is PublicKeyCredential -> {
                // Share responseJson such as a GetCredentialResponse on your server to
                // validate and authenticate
                val responseJson = credential.authenticationResponseJson
            }

            // Password credential
            is PasswordCredential -> {
                // Send ID and password to your server to validate and authenticate.
                val username = credential.id
                val password = credential.password
            }

            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("ERROR", "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e("ERROR", "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e("ERROR", "Unexpected type of credential")
            }
        }
    }

    fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        val credential = result.credential

        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("ERR", "Received an invalid google id token response", e)
                    }
                }
                else  {
                    // Catch any unrecognized credential type here.
                    Log.e("ERR", "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e("ERR", "Unexpected type of credential")
            }
        }
    }

    CoroutineScope(Dispatchers.IO).launch{
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = activityContext,
            )
            handleSignIn(result)
            println(result)
        } catch (e: GetCredentialException) {
            Log.e("ERROR", "Error")
        }
    }



    Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){

        OutlinedTextField(value = "", onValueChange = {})

        CredentialManager.create(context = LocalContext.current)

        Button( onClick = {

            CoroutineScope(Dispatchers.IO).launch{
                try {
                    val result = credentialManager.getCredential(
                        request = request,
                        context = activityContext,
                    )
                    handleSignIn(result)
                    println(result)
                } catch (e: GetCredentialException) {
                    Log.e("ERROR", "Error")
                }
            }
        }){}

    }

*/
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Login")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onSignInClick) {
                Text(text = "Sign in with Google")
            }
        }
    }


}

private fun handleSignIn(result: GetCredentialResponse) {
    //val googleIdToken = resul
    // Utilizza googleIdToken come necessario per autenticare con il tuo backend
    Log.d("SignIn", "Google ID Token: ")
}