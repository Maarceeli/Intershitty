package com.github.intershitty.app.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.intershitty.api.impl.IntercityApiClient
import com.github.intershitty.app.database.dao.AccountDao
import com.github.intershitty.app.database.entities.AccountEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class LogInViewModel(
    private val accountDao: AccountDao
) : ViewModel() {

    fun authenticate(login: String, password: String, callback: () -> Unit) {
        viewModelScope.launch {
            try {
                val token = IntercityApiClient().performLogin(login, password)
                accountDao.insertAll(AccountEntity(
                    id = 0,
                    accessToken = token.accessToken,
                    refreshToken = token.refreshToken,
                    expiresIn = token.expiresIn,
                    generatedOn = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                ))

                withContext(Dispatchers.Main) {
                    callback()
                }
            }
            catch (e: Exception) {
                return@launch // Tomasz kurwa problem lmao, but fine, TODO: Make this a proper exception handling mechanism and not this thing...
            }
        }
    }
}
