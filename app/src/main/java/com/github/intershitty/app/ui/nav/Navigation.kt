package com.github.intershitty.app.ui.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.github.intershitty.app.ui.screens.dashboard.DashboardScreen
import com.github.intershitty.app.ui.screens.login.LogInScreen
import com.github.intershitty.app.ui.screens.welcome.WelcomeScreen

@Composable
fun AppNavigation(
    backStack: NavBackStack<NavKey>
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val currentKey = backStack.lastOrNull()
            val navObject = NavigationObject.entries.firstOrNull { it.screen == currentKey }

            if (navObject != null) {
                TopAppBar(
                    title = { Text(navObject.screenName) },
                    actions = { navObject.content() },
                    navigationIcon = { navObject.navContent(backStack) }
                )
            } }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Welcome> {
                    WelcomeScreen(
                        onGoToLogIn = {
                            backStack.add(LogIn)
                        }
                    )
                }
                entry<LogIn> {
                    LogInScreen(
                        onAuthenticated = {
                            backStack.clear()
                            backStack.add(Dashboard)
                        }
                    )
                }
                entry<Dashboard> {
                    DashboardScreen()
                }
            }
        )
    }
}