package com.github.intershitty.app.database

import androidx.room3.ColumnTypeConverter
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.github.intershitty.app.database.dao.AccountDao
import com.github.intershitty.app.database.dao.TicketDao
import com.github.intershitty.app.database.entities.AccountEntity
import com.github.intershitty.app.database.entities.TicketEntity
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json


object Converters {
    @ColumnTypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? = value?.toString()

    @ColumnTypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let(LocalDateTime::parse)
}

class TicketConverters {
    @ColumnTypeConverter
    fun fromSectionList(value: List<TicketEntity.Section>): String =
        Json.encodeToString(value)

    @ColumnTypeConverter
    fun toSectionList(value: String): List<TicketEntity.Section> =
        Json.decodeFromString(value)

    @ColumnTypeConverter
    fun fromLocalDateTime(value: LocalDateTime): String =
        value.toString()

    @ColumnTypeConverter
    fun toLocalDateTime(value: String): LocalDateTime =
        LocalDateTime.parse(value)
}
@Database(entities = [AccountEntity::class, TicketEntity::class], version = 1, exportSchema = true, autoMigrations =[])
@ColumnTypeConverters(Converters::class, TicketConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun ticketDao(): TicketDao
}
