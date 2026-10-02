package com.github.intershitty.api

import com.github.intershitty.api.responses.eic.EicToken
import com.github.intershitty.api.types.Ticket

interface ApiInterface {
    suspend fun performLogin(username: String, password: String): EicToken
    suspend fun getTickets(token: String): List<Ticket>
}
