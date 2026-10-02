package com.github.intershitty.app.ui.common

import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import dev.vicart.compose.material.symbols.MaterialSymbol

@Composable
fun BackButton(onclick: () -> Unit) {
    FilledTonalIconButton(
        shapes = IconButtonDefaults.shapes(),
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        onClick = onclick
    ) {
        MaterialSymbol.Filled(icon = "arrow_back")
    }
}