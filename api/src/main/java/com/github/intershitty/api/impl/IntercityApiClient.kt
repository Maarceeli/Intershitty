package com.github.intershitty.api.impl

import com.github.intershitty.api.ApiInterface
import com.github.intershitty.api.client
import com.github.intershitty.api.responses.eic.EicTicketDetail
import com.github.intershitty.api.responses.eic.EicTicketsDetailsResponse
import com.github.intershitty.api.responses.eic.EicTicketsResponse
import com.github.intershitty.api.responses.eic.EicToken
import com.github.intershitty.api.types.Ticket
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.parameters
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.jsoup.Jsoup
import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random

class IntercityApiClient : ApiInterface {
    @OptIn(FormatStringsInDatetimeFormats::class)
    private val format = LocalDateTime.Format { byUnicodePattern("yyyy-MM-dd HH:mm:ss") }

    @OptIn(ExperimentalEncodingApi::class)
    private fun generateVerifier(): String {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        return Base64.UrlSafe.encode(bytes).trimEnd('=')
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun generateChallenge(verifier: String = generateVerifier()): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(verifier.toByteArray())
        return Base64.UrlSafe.encode(digest).trimEnd('=')
    }

    suspend fun refreshToken(refreshToken: String): EicToken {
        val response: EicToken = client.post("https://api-gateway.intercity.pl/public/sso/oauth_server/oauth/token") {
            setBody("""
                grant_type=refresh_token&refresh_token=$refreshToken
            """.trimIndent())

            headers {
                append(HttpHeaders.Authorization, "Basic cGtwbmF3aWdhdG9yOlVDMmZhMzQ2SFBxTg==")
                append(HttpHeaders.UserAgent, "Dart/3.12 (dart:io)")
                append(HttpHeaders.ContentType, "application/x-www-form-urlencoded")
            }
        }.body()

        return response
    }

    override suspend fun performLogin(username: String, password: String): EicToken {
        val codeVer = "Le-ts-ke!ep${Random.nextInt(100, 1000)}/it%sec_ret"
        val codeChallenge = generateChallenge(codeVer)

        val oauthLoginRequest = client.get("https://sso.intercity.pl/oauth_server/oauth/authorize") {
            url {
                parameters.append("client_id", "pkpnawigator")
                parameters.append("response_type", "code")
                parameters.append("state", "hello_ic")
                parameters.append("code_challenge", codeChallenge)
                parameters.append("t_m", "S256")
            }
            headers {
                append(HttpHeaders.UserAgent, "Mozilla/5.0 (Linux; Android 16; SzczurDevice; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/153.0.8010.36 Mobile Safari/537.36")
            }
        }.bodyAsText()

        val csrf = Jsoup.parse(oauthLoginRequest).selectFirst("input[name=_csrf]")?.`val`()

        val oauthLoginPost = client.submitForm(
            url = "https://sso.intercity.pl/oauth_server/login",
            formParameters = parameters {
                append("_csrf", csrf!!)
                append("client_id", "pkpnawigator")
                append("username", username)
                append("password", password)
            }
        ) {
            headers {
                append(HttpHeaders.UserAgent, "Mozilla/5.0 (Linux; Android 16; SzczurDevice; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/153.0.8010.36 Mobile Safari/537.36")
                append(HttpHeaders.Accept, "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                append("sec-ch-ua", "\"Android WebView\";v=\"153\", \"Not_A Brand\";v=\"8\", \"Chromium\";v=\"153\"")
            }
        }

        val code = oauthLoginPost.headers[HttpHeaders.Location]!!.substringAfter("#").split("&").firstOrNull { it.startsWith("code=") }?.substringAfter("code=")

        val oauthTokenRequest: EicToken = client.get("https://sso.intercity.pl/oauth_server/oauth/token/code/$code") {
            url {
                parameters.append("code_verifier", codeVer)
            }

            headers {
                append(HttpHeaders.UserAgent, "Dart/3.12 (dart:io)")
                remove(HttpHeaders.Cookie)
            }
        }.body()

        return oauthTokenRequest
    }

    override suspend fun getTickets(token: String): List<Ticket> {
        val tickets: MutableList<Ticket> = mutableListOf()

        val ticketsResponse = client.post("https://api-gateway.intercity.pl/server/private/endpoint/Sprzedaz") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "metoda": "pobierzIdentyfikatoryBiletow",
                  "biletTyp": 101,
                  "listaTyp": 101,
                  "historia": 0,
                  "sortowanie": 0,
                  "wynikowNaStronie": 500,
                  "numerStrony": 1,
                  "warunekWyszukiwania": "",
                  "urzadzenieNr": 956
                }
            """.trimIndent())

            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                append(HttpHeaders.UserAgent, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/151.0.0.0 Safari/537.36")
            }
        }

        if (ticketsResponse.status == HttpStatusCode.Unauthorized) {
            return emptyList()
        }

        val allTickets = ticketsResponse.body<EicTicketsResponse>()

        allTickets.identyfikatory.forEach { ticket ->
            val ticketDetail: EicTicketDetail = client.post("https://api-gateway.intercity.pl/server/private/endpoint/Sprzedaz") {
                contentType(ContentType.Application.Json)
                setBody("""
            {
              "metoda": "pobierzBilet",
              "transakcjaNr": "${ticket.transakcjaNr}",
              "biletSeria": "${ticket.biletSeria}",
              "biletNr": "${ticket.biletNr}",
              "biletKod": null,
              "urzadzenieNr": 956
            }
        """.trimIndent())

                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append(HttpHeaders.UserAgent, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/151.0.0.0 Safari/537.36")
                }
            }.body<EicTicketsDetailsResponse>().bilety.first()

            tickets.add(
                Ticket(
                    ticketId = "${ticket.biletSeria}${ticket.biletNr}",
                    sections = ticketDetail.odcinki.mapIndexed { index, section ->
                        Ticket.Section(
                            departureStation = ticket.stacjaOd[index],
                            arrivalStation = ticket.stacjaDo[index],
                            departureTime = format.parse(section.wyjazdData),
                            arrivalTime = format.parse(section.przyjazdData),
                            trainClass = section.klasa,
                            distance = section.kilometry,
                            trainNumber = ticket.nrPociagu.first().toString(),
                            trainCategory = ticketDetail.odcinki.first().pociagKategoriaNazwa,
                            trainName = ticketDetail.odcinki.first().pociagNazwa,
                            seats = section.miejsca.map { seat ->
                                Ticket.Seat(
                                    carriage = seat.wagonNr,
                                    seatNumber = seat.miejsceNr,
                                    seatType = seat.miejsceTypNazwa,
                                    seatDescription = seat.usytowanieNazwa
                                )
                            },
                        )
                    },
                    price = ticket.cena/100f,
                    overallArrivalStation = ticket.stacjaDo.last(),
                    overallDepartureStation = ticket.stacjaOd.first(),
                    validFrom = format.parse(ticketDetail.waznoscBiletuOd),
                    validTo = format.parse(ticketDetail.waznoscBiletuDo),
                    ownerName = ticketDetail.podroznyNazwa,
                    controlCode = ticketDetail.kodKontrolny,
                    ticketCode = ticketDetail.kod2D
                )
            )
        }

        return tickets
    }
}
