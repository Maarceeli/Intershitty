package com.github.intershitty.app.ui.screens.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.intershitty.api.impl.IntercityApiClient
import com.github.intershitty.app.database.dao.AccountDao
import com.github.intershitty.app.database.dao.TicketDao
import com.github.intershitty.app.database.dao.toEntity
import com.github.intershitty.app.database.entities.AccountEntity
import com.github.intershitty.app.database.entities.TicketEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

class DashboardViewModel(
    private val accountDao: AccountDao,
    private val ticketDao: TicketDao
) : ViewModel() {
    private val _accounts = MutableStateFlow<List<AccountEntity>>(emptyList())
    private val accounts: StateFlow<List<AccountEntity>> = _accounts.asStateFlow()

    private val _tickets = MutableStateFlow<List<TicketEntity>>(emptyList())
    val tickets = _tickets.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadTickets() {
        viewModelScope.launch {
            _tickets.value = ticketDao.getAll()
            _accounts.value = accountDao.getAll()

            _isLoading.value = true
            try {
                if (Clock.System.now() > _accounts.value.first().generatedOn.toInstant(TimeZone.currentSystemDefault()) + _accounts.value.first().expiresIn.seconds) {
                    Log.d("refresh", _accounts.value.toString())
                    // Refresh token
                    val token = IntercityApiClient().refreshToken( _accounts.value.first().refreshToken)

                    accountDao.update(AccountEntity(
                        id = 0,
                        accessToken = token.accessToken,
                        refreshToken = token.refreshToken,
                        expiresIn = token.expiresIn,
                        generatedOn = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    ))

                    _accounts.value = accountDao.getAll()
                    Log.d("refresh", _accounts.value.toString())
                }

                val networkTickets = IntercityApiClient().getTickets(accounts.value.first().accessToken) // TODO: Make the app independent of the IC api itself

                ticketDao.deleteAll()
                ticketDao.insertAll(*networkTickets.map { it.toEntity() }.toTypedArray())
                _tickets.value = ticketDao.getAll()
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }
}