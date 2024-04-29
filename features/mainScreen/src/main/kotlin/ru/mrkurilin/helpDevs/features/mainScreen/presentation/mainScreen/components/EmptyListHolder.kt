package ru.mrkurilin.helpDevs.features.mainScreen.presentation.mainScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.mrkurilin.helpDevs.mainScreen.R

@Composable
fun EmptyListHolder() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painterResource(R.drawable.android_phone),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.list_is_empty),
            textAlign = TextAlign.Center,
        )
    }
}
