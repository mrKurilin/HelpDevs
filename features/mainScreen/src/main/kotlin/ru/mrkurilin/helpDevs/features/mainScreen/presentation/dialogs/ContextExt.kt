package ru.mrkurilin.helpDevs.features.mainScreen.presentation.dialogs

import android.content.ClipboardManager
import android.content.Context

fun Context.getTextFromClipboard(): String {
    return try {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.primaryClip?.getItemAt(0)?.text?.toString() ?: ""
    } catch (e: Exception) {
        ""
    }
}
