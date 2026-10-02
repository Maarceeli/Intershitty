package com.github.intershitty.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.produceState
import androidx.navigation3.runtime.rememberNavBackStack
import com.github.intershitty.app.database.dao.AccountDao
import com.github.intershitty.app.database.entities.AccountEntity
import com.github.intershitty.app.ui.nav.AppNavigation
import com.github.intershitty.app.ui.nav.Dashboard
import com.github.intershitty.app.ui.nav.Welcome
import com.github.intershitty.app.ui.theme.IntershittyTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val accountDao: AccountDao by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            IntershittyTheme {
                val accountsState = produceState<List<AccountEntity>?>(initialValue = null) {
                    value = accountDao.getAll()
                }

                val accounts = accountsState.value
                if (accounts != null) {
                    val startRoute = if (accounts.isEmpty()) Welcome else Dashboard
                    val backStack = rememberNavBackStack(startRoute)
                    AppNavigation(backStack)
                }
            }
        }
    }
}