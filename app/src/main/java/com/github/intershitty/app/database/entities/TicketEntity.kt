package com.github.intershitty.app.database.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Entity(tableName = "ticket")
data class TicketEntity (
    @PrimaryKey val ticketId: String,
    val sections: List<Section>,
    val price: Float,
    val overallDepartureStation: String,
    val overallArrivalStation: String,
    val validFrom: LocalDateTime,
    val validTo: LocalDateTime,
    val controlCode: String,
    val ownerName: String,
    val ticketCode: String,
) {
    @Serializable
    data class Seat (
        val carriage: Int,
        val seatNumber: Int,
        val seatType: String,
        val seatDescription: String,
    )

    @Serializable
    data class Section (
        val departureTime: LocalDateTime,
        val arrivalTime: LocalDateTime,
        val departureStation: String,
        val arrivalStation: String,
        val trainNumber: String,
        val trainCategory: String,
        val trainName: String,
        val trainClass: Int,
        val distance: Int,
        val seats: List<Seat>,
    )
}