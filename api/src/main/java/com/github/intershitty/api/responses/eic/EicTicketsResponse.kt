package com.github.intershitty.api.responses.eic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EicTicketsResponse(
    @SerialName("identyfikatory")
    val identyfikatory: List<EicTicket>
)

@Serializable
data class EicTicket(
    @SerialName("transakcjaNr")
    val transakcjaNr: Long,

    @SerialName("biletSeria")
    val biletSeria: String,

    @SerialName("biletNr")
    val biletNr: Long,

    @SerialName("biletPodstawowySeria")
    val biletPodstawowySeria: String,

    @SerialName("biletPodstawowyNr")
    val biletPodstawowyNr: Long,

    @SerialName("waznoscBiletuOd")
    val waznoscBiletuOd: String,

    @SerialName("waznoscBiletuDo")
    val waznoscBiletuDo: String,

    @SerialName("biletPodstawowyKod")
    val biletPodstawowyKod: String,

    @SerialName("typBiletu")
    val typBiletu: Int,

    @SerialName("typDokumentuKasa")
    val typDokumentuKasa: Int,

    @SerialName("zwrocony")
    val zwrocony: Int,

    @SerialName("anulowany")
    val anulowany: Int,

    @SerialName("czasNaOplacenie")
    val czasNaOplacenie: String,

    @SerialName("oplacony")
    val oplacony: Int,

    @SerialName("stacjaOd")
    val stacjaOd: List<String>,

    @SerialName("stacjaDo")
    val stacjaDo: List<String>,

    @SerialName("nrPociagu")
    val nrPociagu: List<Int>,

    @SerialName("cena")
    val cena: Int,

    @SerialName("klasa")
    val klasa: Int
)
