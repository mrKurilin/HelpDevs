package ru.mrkurilin.helpDevs.features.mainScreen.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.components.CheckedText
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppsFilter
import ru.mrkurilin.helpDevs.mainScreen.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsFilterDialog(
    onAppsFilterSelected: (AppsFilter) -> Unit,
    onDismissRequest: () -> Unit,
    selectedAppsFilters: List<AppsFilter>,
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.apps_filter),
                    fontWeight = FontWeight.Bold
                )

                AppsFilter.entries.forEach { appsFilter ->
                    CheckedText(
                        text = stringResource(id = appsFilter.titleId),
                        isChecked = selectedAppsFilters.contains(appsFilter),
                        onItemSelected = { onAppsFilterSelected(appsFilter) }
                    )
                }

                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.ok)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppsFilterDialogPreview() {
    AppsFilterDialog(
        {},
        {},
        listOf(),
    )
}
