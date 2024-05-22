package ru.mrkurilin.helpDevs.features.mainScreen.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.components.IconDescription
import ru.mrkurilin.helpDevs.mainScreen.R

@Composable
fun InfoDialog(
    toggleInfoDialog: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { toggleInfoDialog() },
        title = {
            Text(text = stringResource(R.string.info))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                IconDescription(
                    icon = Icons.Default.Check,
                    description = stringResource(R.string.installed_icon_description)
                )
                IconDescription(
                    icon = Icons.Default.Delete,
                    description = stringResource(R.string.can_be_deleted_icon_description)
                )
                IconDescription(
                    icon = Icons.Default.Refresh,
                    description = stringResource(R.string.can_be_installed_icon_description)
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { toggleInfoDialog() }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        }
    )
}
