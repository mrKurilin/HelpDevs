package ru.mrkurilin.helpDevs.features.mainScreen.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopBar(
    title: String,
    onInfoButtonClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title
            )
        },
        actions = {
            IconButton(
                onClick = { onInfoButtonClick() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                )
            }
        },
    )
}
