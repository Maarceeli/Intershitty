package com.github.intershitty.app.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.drawablepainter.DrawablePainter

@Composable
fun appIcon(): Painter {
    val context = LocalContext.current
    return DrawablePainter(context.packageManager.getApplicationIcon(context.packageName))
}