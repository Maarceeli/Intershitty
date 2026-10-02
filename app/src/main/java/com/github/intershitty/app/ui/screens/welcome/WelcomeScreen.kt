package com.github.intershitty.app.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.intershitty.app.ui.common.appIcon

@Composable
fun WelcomeScreen(
    onGoToLogIn: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
        ) {
            Image(
                painter = appIcon(),
                contentDescription = null,
                modifier = Modifier
                    .size(160.dp)
            )

            Text(
                text = "Welcome to InterShitty!",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(bottom = 15.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Please log in as you would normally in the PKP Intercity website", // TODO: Support more types of ticketing platforms
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )

            FilledTonalButton(
                onClick = onGoToLogIn,
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