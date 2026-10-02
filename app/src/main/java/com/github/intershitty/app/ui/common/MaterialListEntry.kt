package com.github.intershitty.app.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MaterialListEntry(
    modifier: Modifier = Modifier,
    contentPadding: Dp = 16.dp,
    index: Int,
    lastIndex: Int,
    isOnly: Boolean,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val shape = when {
        isOnly -> ExpressiveListItemShapes.singleListItemShape
        index == 0 -> ExpressiveListItemShapes.topListItemShape
        index == lastIndex -> ExpressiveListItemShapes.bottomListItemShape
        else -> ExpressiveListItemShapes.middleListItemShape
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}