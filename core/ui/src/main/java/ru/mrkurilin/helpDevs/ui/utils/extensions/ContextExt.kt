package ru.mrkurilin.helpDevs.ui.utils.extensions

import android.content.ClipboardManager
import android.content.Context

fun Context.getTextFromClipboard(): String {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    return try {
        clipboardManager.primaryClip?.getItemAt(0)?.text.toStringOrEmpty()
    } catch (e: Exception) {
        ""
    }
}
