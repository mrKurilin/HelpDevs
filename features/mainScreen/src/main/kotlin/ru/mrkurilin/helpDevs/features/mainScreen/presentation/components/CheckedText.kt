package ru.mrkurilin.helpDevs.features.mainScreen.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CheckedText(
    text: String,
    isChecked: Boolean,
    onItemSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable { onItemSelected() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onItemSelected() }
        )
        Text(
            text = text
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun CheckedTextPreview() {
    CheckedText(
        "text",
        true,
        {},
    )
}
