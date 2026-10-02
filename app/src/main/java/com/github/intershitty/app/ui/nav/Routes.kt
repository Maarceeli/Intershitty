package com.github.intershitty.app.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.github.intershitty.app.ui.common.BackButton
import kotlinx.serialization.Serializable

@Serializable
data object Welcome : NavKey

@Serializable
data object Dashboard : NavKey

@Serializable
data object LogIn : NavKey

enum class NavigationObject(
    val screen: NavKey,
    val screenName: String,
    val content: @Composable () -> Unit = {},
    val navContent: @Composable (backStack: NavBackStack<NavKey>) -> Unit = {}
) {
    LogIn(
        screen = com.github.intershitty.app.ui.nav.LogIn,
        screenName = "Log in",
        navContent = { backStack -> BackButton { backStack.removeLastOrNull() } }
    ),

    Dashboard(
        screen = com.github.intershitty.app.ui.nav.Dashboard,
        screenName = "Dashboard",
    )
}

