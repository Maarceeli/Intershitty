package com.github.intershitty.app.database.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey val id: Int,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int,
    val generatedOn: LocalDateTime
)