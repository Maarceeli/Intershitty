package com.github.intershitty.app.ui.screens.dashboard

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.intershitty.app.database.entities.TicketEntity
import com.github.intershitty.app.ui.common.MaterialListEntry
import com.github.intershitty.app.ui.theme.IntercityColor
import com.github.intershitty.app.utils.toOrdinal
import dev.vicart.compose.material.symbols.MaterialSymbol
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import org.koin.compose.viewmodel.koinViewModel

@OptIn(FormatStringsInDatetimeFormats::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = koinViewModel()
    val tickets = viewModel.tickets.collectAsStateWithLifecycle().value
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }

    var chosenTicket by remember { mutableStateOf<TicketEntity?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadTickets()
    }

    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
        ) {
            tickets.forEachIndexed { index, ticket ->
                TicketEntry(
                    ticket = ticket,
                    index = index,
                    lastIndex = tickets.lastIndex,
                    isOnly = tickets.count() == 1,
                    onClick = {
                        chosenTicket = ticket
                        showBottomSheet = true
                    }
                )
            }
        }

        if (showBottomSheet) {
            TicketBottomSheet(
                ticket = chosenTicket!!,
                onDissmiss = { showBottomSheet = false }
            )
        }

        if (isLoading) {
            @OptIn(ExperimentalMaterial3ExpressiveApi::class)
            ContainedLoadingIndicator(
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketBottomSheet(
    ticket: TicketEntity,
    onDissmiss: () -> Unit,
    sheetState: SheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.PartiallyExpanded, SheetValue.Expanded)
    )
) {
    ModalBottomSheet(
        onDismissRequest = onDissmiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Ticket data", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 6.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        ticket.ticketId,
                        style = MaterialTheme.typography.titleLarge

                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        ticket.validFrom.format(LocalDateTime.Format {
                            day()
                            char('.')
                            monthNumber()
                            char('.')
                            year()
                        }),
                        style = MaterialTheme.typography.titleLarge

                    )
                }

                // Ticket code
                Column(Modifier.fillMaxWidth()) {
                    var isExpanded by remember { mutableStateOf(false) }

                    if (!isExpanded) {
                        FilledTonalButton(
                            onClick = {
                                isExpanded = !isExpanded
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Show code", style = MaterialTheme.typography.titleMedium)
                        }
                    }

                    if (isExpanded) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            BoxWithConstraints {
                                val targetSize = minOf(maxWidth, 384.dp)

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    modifier = Modifier
                                        .size(targetSize)
                                        .aspectRatio(1f),
                                    onClick = {
                                        isExpanded = !isExpanded
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Base64Image(ticket.ticketCode)
                                    }
                                }
                            }

                            Text("Tap the code to close", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            HorizontalDivider()

            // Journey overview
            Column {
                Text("Joruney overview", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 6.dp))

                // Sections
                ticket.sections.forEach { section ->
                    val color = when (section.trainCategory) {
                        "IC" -> IntercityColor
                        else -> MaterialTheme.colorScheme.primaryContainer
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val tz = TimeZone.currentSystemDefault()
                        val duration =
                            section.arrivalTime.toInstant(tz) - section.departureTime.toInstant(tz)
                        val formattedTime = "${duration.inWholeHours}:${
                            (duration.inWholeMinutes % 60).toString().padStart(2, '0')
                        }h"
                        val context = LocalContext.current

                        // Legs
                        Row {
                            // Trip overview
                            LegDiagram(
                                departureStation = section.departureStation,
                                departureTime = section.departureTime.format(LocalDateTime.Format {
                                    hour(); char(
                                    ':'
                                ); minute()
                                }),
                                arrivalStation = section.arrivalStation,
                                arrivalTime = section.arrivalTime.format(LocalDateTime.Format {
                                    hour(); char(
                                    ':'
                                ); minute()
                                }),
                                legAccent = color.copy(alpha = 0.65f),
                                insideContent = {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        // Category and number
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            // Train type
                                            Card(
                                                modifier = Modifier
                                                    .width(35.dp)
                                                    .height(25.dp),
                                                shape = RoundedCornerShape(
                                                    topStart = 7.5.dp,
                                                    bottomStart = 7.5.dp
                                                ),
                                                colors = CardColors(
                                                    containerColor = color.copy(alpha = 0.65f),
                                                    contentColor = color.copy(alpha = 0.65f),
                                                    disabledContainerColor = color.copy(alpha = 0.65f),
                                                    disabledContentColor = color.copy(alpha = 0.65f)
                                                )
                                            ) {
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier.fillMaxSize()
                                                ) {
                                                    Text(
                                                        section.trainCategory,
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = Color.White
                                                    )
                                                }
                                            }

                                            // Train number
                                            Card(
                                                modifier = Modifier
                                                    .width(65.dp)
                                                    .height(25.dp),
                                                shape = RoundedCornerShape(
                                                    topEnd = 7.5.dp,
                                                    bottomEnd = 7.5.dp
                                                ),
                                                colors = CardColors(
                                                    containerColor = color.copy(alpha = 0.35f),
                                                    contentColor = color.copy(alpha = 0.35f),
                                                    disabledContainerColor = color.copy(alpha = 0.35f),
                                                    disabledContentColor = color.copy(alpha = 0.35f)
                                                )
                                            ) {
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier.fillMaxSize()
                                                ) {
                                                    Text(
                                                        section.trainNumber,
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = Color.White
                                                    )
                                                }
                                            }

                                            // Train name
                                            Text(
                                                text = section.trainName,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontStyle = FontStyle.Italic,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )

                                            Spacer(modifier = Modifier.weight(1f))

                                            // Train info
                                            Text(
                                                "${section.trainClass.toOrdinal()} Class | ${section.distance}km | $formattedTime",
                                                style = MaterialTheme.typography.titleSmall,
                                            )
                                        }

                                        // Reserved seats
                                        Column(verticalArrangement = Arrangement.spacedBy((-4).dp)) {
                                            section.seats.forEachIndexed { index, seat ->
                                                MaterialListEntry(
                                                    index = index,
                                                    lastIndex = section.seats.lastIndex,
                                                    isOnly = section.seats.size == 1,
                                                    contentPadding = 6.dp
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                    ) {
                                                        MaterialSymbol.Filled("airline_seat_recline_extra")

                                                        Text("${seat.seatNumber}")
                                                        Text(
                                                            seat.seatDescription,
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.outline
                                                        )

                                                        Text("\u2022")

                                                        MaterialSymbol.Filled("train")

                                                        Text("${seat.carriage}")
                                                        Text(
                                                            seat.seatType,
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.outline
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        if (section.trainCategory in listOf("IC", "EIC", "EIP", "TLK", "ICN", "MP", "EN", "EC")) {
                                            OutlinedButton(
                                                onClick = {
                                                    val uri =
                                                        "https://bocznica.eu/stats/${section.trainNumber}".toUri()
                                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                                    context.startActivity(intent)
                                                },
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) { MaterialSymbol.Outlined("train"); Text("Open in bocznica.eu") }
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val dateTimeFormat = remember {
                    LocalDateTime.Format {
                        day(); char('.'); monthNumber(); char('.'); year()
                        char(' ')
                        hour(); char(':'); minute()
                    }
                }
                val labelStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val valueStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )

                Text("Other details", style = MaterialTheme.typography.headlineMedium)

                val items = listOf(
                    "Ticket holder" to ticket.ownerName,
                    "Price" to "${"%.2f".format(ticket.price)} zł",
                    "Valid from" to ticket.validFrom.format(dateTimeFormat),
                    "Valid to" to ticket.validTo.format(dateTimeFormat),
                    "Control code" to ticket.controlCode
                )

                for ((label, value) in items) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(label, style = labelStyle)
                        Text(value, style = valueStyle)
                    }
                }
            }
        }
    }
}

@Composable
private fun TicketEntry(
    ticket: TicketEntity,
    index: Int,
    lastIndex: Int,
    isOnly: Boolean,
    onClick: () -> Unit
) {
    MaterialListEntry(index = index, lastIndex = lastIndex, isOnly = isOnly, onClick = onClick) {
        Column {
            // Header
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    ticket.ticketId,
                    style = MaterialTheme.typography.titleLarge

                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    ticket.validFrom.format( LocalDateTime.Format {
                        day()
                        char('.')
                        monthNumber()
                        char('.')
                        year()
                    }),
                    style = MaterialTheme.typography.titleLarge

                )
            }

            // Legs
            Column(
                modifier = Modifier
                    .padding(top = 6.dp)
            ) {
                // Trip overview
                LegDiagram(
                    departureStation = ticket.overallDepartureStation,
                    departureTime = "",
                    arrivalStation = ticket.overallArrivalStation,
                    arrivalTime = "",
                    legAccent = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
    }
}

// Szpont warning, im not responsible for stuff below
@Composable
private fun LegDiagram(
    departureStation: String,
    departureTime: String,
    arrivalStation: String,
    arrivalTime: String,
    legAccent: Color,
    insideContent: (@Composable () -> Unit)? = null,
) {
    val topHalfHeight = remember { mutableFloatStateOf(0f) }
    val bottomHalfHeight = remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .padding(top = 6.dp, start = 2.dp)
            .drawBehind {
                val stroke = 5.dp.toPx()
                val radius = 16.dp.toPx() / 2f
                val hole = 9.dp.toPx() / 2f

                val topCenterY = topHalfHeight.floatValue
                val bottomCenterY = size.height - bottomHalfHeight.floatValue

                val path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            left = radius - stroke / 2f,
                            top = topCenterY,
                            right = radius + stroke / 2f,
                            bottom = bottomCenterY,
                            cornerRadius = CornerRadius(stroke / 2f)
                        )
                    )
                    addOval(
                        Rect(
                            left = 0f,
                            top = topCenterY - radius,
                            right = radius * 2,
                            bottom = topCenterY + radius
                        )
                    )
                    addOval(
                        Rect(
                            left = 0f,
                            top = bottomCenterY - radius,
                            right = radius * 2,
                            bottom = bottomCenterY + radius
                        )
                    )
                }

                drawPath(path = path, color = legAccent)
                drawCircle(Color.White, radius = hole, center = Offset(radius, topCenterY))
                drawCircle(Color.White, radius = hole, center = Offset(radius, bottomCenterY))
            },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LegRow(
            station = departureStation,
            time = departureTime,
            modifier = Modifier.onSizeChanged { topHalfHeight.floatValue = it.height / 2f }
        )

        insideContent?.let { content ->
            Box(modifier = Modifier.padding(start = 24.dp)) {
                content()
            }
        }

        LegRow(
            station = arrivalStation,
            time = arrivalTime,
            modifier = Modifier.onSizeChanged { bottomHalfHeight.floatValue = it.height / 2f }
        )
    }
}

@Composable
private fun LegRow(
    station: String,
    time: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Reserves the dot's width so the labels line up with the diagram axis.
        Spacer(modifier = Modifier.size(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = station,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = time,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun Base64Image(base64String: String) {
    val imageBitmap = remember(base64String) {
        // Strip data URI prefix if present (e.g., "data:image/png;base64,")
        val cleanBase64 = base64String.substringAfter(",")
        val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)?.asImageBitmap()
    }

    imageBitmap?.let {
        Image(bitmap = it, contentDescription = null, modifier = Modifier.fillMaxSize(), filterQuality = FilterQuality.None)
    }
}