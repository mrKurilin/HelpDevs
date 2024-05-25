package ru.mrkurilin.helpDevs.ui.utils.extensions

fun Any?.toStringOrEmpty(): String {
    return this?.toString() ?: return ""
}
