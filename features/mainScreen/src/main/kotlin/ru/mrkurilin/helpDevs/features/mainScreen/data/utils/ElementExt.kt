package ru.mrkurilin.helpDevs.features.mainScreen.data.utils

import org.jsoup.nodes.Element

fun Element.getTextValue(): String {
    val childNodes: List<org.jsoup.nodes.Node> = childNodes()

    if (childNodes.isEmpty()) {
        return ""
    }

    val firstChildNote = childNodes.first()

    if (firstChildNote is org.jsoup.nodes.TextNode) {
        return firstChildNote.toString().trim()
    }

    if (firstChildNote is Element) {
        return firstChildNote.getTextValue()
    }

    return ""
}
