package com.github.intershitty.api.responses.eic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EicTicketsDetailsResponse(
    @SerialName("bilety")
    val bilety: List<EicTicketDetail> = emptyList(),
)

@Serializable
data class EicTicketDetail(
    @SerialName("biletRodzaj")
    val biletRodzaj: Int,

    @SerialName("transakcjaNr")
    val transakcjaNr: Long,

    @SerialName("biletSeria")
    val biletSeria: String,

    @SerialName("biletNr")
    val biletNr: Long,

    @SerialName("biletZrodloKod")
    val biletZrodloKod: Int,

    @SerialName("sprzedazData")
    val sprzedazData: String,

    @SerialName("waznoscBiletuOd")
    val waznoscBiletuOd: String,

    @SerialName("waznoscBiletuDo")
    val waznoscBiletuDo: String,

    @SerialName("biletPodstawowySeria")
    val biletPodstawowySeria: String,

    @SerialName("biletPodstawowyNr")
    val biletPodstawowyNr: Long,

    @SerialName("biletPodstawowyKod")
    val biletPodstawowyKod: String,

    @SerialName("odcinki")
    val odcinki: List<EicOdcinek> = emptyList(),

    @SerialName("podrozni")
    val podrozni: List<EicPodrozny> = emptyList(),

    @SerialName("ofertaKod")
    val ofertaKod: Int,

    @SerialName("ofertaNazwa")
    val ofertaNazwa: String,

    @SerialName("cenaBilet")
    val cenaBilet: Int,

    @SerialName("ptuBiletKwota")
    val ptuBiletKwota: Int,

    @SerialName("ptuBiletProcent")
    val ptuBiletProcent: Int,

    @SerialName("cenaRezerwacja")
    val cenaRezerwacja: Int,

    @SerialName("ptuRezerwacjaKwota")
    val ptuRezerwacjaKwota: Int,

    @SerialName("ptuRezerwacjaProcent")
    val ptuRezerwacjaProcent: Int,

    @SerialName("podroznyNazwa")
    val podroznyNazwa: String,

    @SerialName("mozliwaZmianaDanychPodroznego")
    val mozliwaZmianaDanychPodroznego: Boolean,

    @SerialName("kod2D")
    val kod2D: String,

    @SerialName("kodKontrolny")
    val kodKontrolny: String,

    @SerialName("sladWeglowy")
    val sladWeglowy: List<EicSladWeglowy> = emptyList(),

    @SerialName("fakturaSeria")
    val fakturaSeria: String,

    @SerialName("fakturaNr")
    val fakturaNr: String,

    @SerialName("fakturaKorektaSeria")
    val fakturaKorektaSeria: String,

    @SerialName("fakturaKorektaNr")
    val fakturaKorektaNr: String,

    @SerialName("notaKorygujacaSeria")
    val notaKorygujacaSeria: String,

    @SerialName("notaKorygujacaNr")
    val notaKorygujacaNr: String,

    @SerialName("poziomCenowy")
    val poziomCenowy: String,

    @SerialName("poziomCenowyHaslo")
    val poziomCenowyHaslo: List<EicPoziomCenowyHaslo> = emptyList(),

    @SerialName("stacjaKoncowa")
    val stacjaKoncowa: String,

    @SerialName("platnoscSposobKod")
    val platnoscSposobKod: Int,

    @SerialName("platnoscNr")
    val platnoscNr: Long,

    @SerialName("zwrotOpis")
    val zwrotOpis: String,

    @SerialName("informacjeDodatkowe")
    val informacjeDodatkowe: List<String> = emptyList(),

    @SerialName("oplataDodatkowa")
    val oplataDodatkowa: List<String> = emptyList(),

    @SerialName("czyMozliwyZwrot")
    val czyMozliwyZwrot: Int,

    @SerialName("mozliwyZwrotDoData")
    val mozliwyZwrotDoData: String,

    @SerialName("czyMozliwaWymiana")
    val czyMozliwaWymiana: Int,

    @SerialName("mozliwaWymianaDoData")
    val mozliwaWymianaDoData: String,

    @SerialName("liczbaRowerow")
    val liczbaRowerow: Int,

    @SerialName("cenaRowery")
    val cenaRowery: Int,

    @SerialName("liczbaBagazy")
    val liczbaBagazy: Int,

    @SerialName("cenaBagaze")
    val cenaBagaze: Int,

    @SerialName("liczbaPsow")
    val liczbaPsow: Int,

    @SerialName("cenaPsy")
    val cenaPsy: Int,

    @SerialName("cenaCalkowita")
    val cenaCalkowita: Int,

    @SerialName("zwrotRozpoczety")
    val zwrotRozpoczety: Int
)

@Serializable
data class EicOdcinek(
    @SerialName("wyjazdData")
    val wyjazdData: String,

    @SerialName("przyjazdData")
    val przyjazdData: String,

    @SerialName("stacjaOdKod")
    val stacjaOdKod: Int,

    @SerialName("stacjaDoKod")
    val stacjaDoKod: Int,

    @SerialName("pociagNr")
    val pociagNr: Int,

    @SerialName("pociagNazwa")
    val pociagNazwa: String,

    @SerialName("pociagKategoriaNazwa")
    val pociagKategoriaNazwa: String,

    @SerialName("klasa")
    val klasa: Int,

    @SerialName("kilometry")
    val kilometry: Int,

    @SerialName("transfer")
    val transfer: Int,

    @SerialName("transferKomMiejska")
    val transferKomMiejska: Int,

    @SerialName("transferKomunikaty")
    val transferKomunikaty: List<String> = emptyList(),

    @SerialName("miejsca")
    val miejsca: List<EicMiejsce> = emptyList(),

    @SerialName("nrReferencyjnyRezerwacji")
    val nrReferencyjnyRezerwacji: String
)

@Serializable
data class EicMiejsce(
    @SerialName("miejsceTypKod")
    val miejsceTypKod: Int,

    @SerialName("miejsceTypNazwa")
    val miejsceTypNazwa: String,

    @SerialName("usytowanieNazwa")
    val usytowanieNazwa: String,

    @SerialName("usytowanieKod")
    val usytowanieKod: Int,

    @SerialName("wagonNr")
    val wagonNr: Int,

    @SerialName("miejsceNr")
    val miejsceNr: Int
)

@Serializable
data class EicPodrozny(
    @SerialName("znizkaKod")
    val znizkaKod: Int,

    @SerialName("spolkaKolejowaKod")
    val spolkaKolejowaKod: Int,

    @SerialName("dataUrodzenia")
    val dataUrodzenia: String,

    @SerialName("wspolneMiejsce")
    val wspolneMiejsce: Int,

    @SerialName("typPodroznego")
    val typPodroznego: String
)

@Serializable
data class EicSladWeglowy(
    @SerialName("jezyk")
    val jezyk: String,

    @SerialName("tresc")
    val tresc: String
)

@Serializable
data class EicPoziomCenowyHaslo(
    @SerialName("jezyk")
    val jezyk: String,

    @SerialName("opis")
    val opis: String
)
