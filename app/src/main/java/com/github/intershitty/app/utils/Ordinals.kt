package com.github.intershitty.app.utils

fun Int.toOrdinal(): String {
    val rem100 = this % 100
    if (rem100 in 11..13) return "${this}th"
    return when (this % 10) {
        1 -> "${this}st"
        2 -> "${this}nd"
        3 -> "${this}rd"
        else -> "${this}th"
    }
}