package com.github.intershitty.app.database.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.github.intershitty.api.types.Ticket
import com.github.intershitty.app.database.entities.TicketEntity

@Dao
interface TicketDao {
    @Query("SELECT * FROM ticket")
    suspend fun getAll(): List<TicketEntity>

    @Insert
    suspend fun insertAll(vararg tickets: TicketEntity)

    @Delete
    suspend fun delete(ticket: TicketEntity)

    @Query("DELETE FROM ticket")
    suspend fun deleteAll()

    @Update
    suspend fun update(vararg tickets: TicketEntity)
}

fun Ticket.toEntity() = TicketEntity(
    ticketId = ticketId,
    sections = sections.map { it.toEntity() },
    price = price,
    overallDepartureStation = overallDepartureStation,
    overallArrivalStation = overallArrivalStation,
    validFrom = validFrom,
    validTo = validTo,
    ownerName = ownerName,
    controlCode = controlCode,
    ticketCode = ticketCode,
)

fun Ticket.Section.toEntity() = TicketEntity.Section(
    departureTime = departureTime,
    arrivalTime = arrivalTime,
    departureStation = departureStation,
    arrivalStation = arrivalStation,
    trainNumber = trainNumber,
    trainCategory = trainCategory,
    trainName = trainName,
    trainClass = trainClass,
    distance = distance,
    seats = seats.map { it.toEntity() },
)

fun Ticket.Seat.toEntity() = TicketEntity.Seat(
    carriage = carriage,
    seatNumber = seatNumber,
    seatType = seatType,
    seatDescription = seatDescription,
)