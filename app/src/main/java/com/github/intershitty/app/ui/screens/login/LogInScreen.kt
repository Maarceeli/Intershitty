package com.github.intershitty.app.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LogInScreen(
    onAuthenticated: () -> Unit,
) {
    val viewModel: LogInViewModel = koinViewModel()

    val loginFieldState = rememberTextFieldState()
    val passwordFieldState = rememberTextFieldState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
        ) {
            // Login
            OutlinedTextField(
                state = loginFieldState,
                label = { Text("Login") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, // or KeyboardType.Text
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedSecureTextField(
                state = passwordFieldState,
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 15.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            FilledTonalButton(
                onClick = { viewModel.authenticate(loginFieldState.text.toString(), passwordFieldState.text.toString(), onAuthenticated) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Log In",
                    fontSize = 16.sp
                )
            }
        }
    }
}
