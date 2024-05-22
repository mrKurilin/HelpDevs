package ru.mrkurilin.helpDevs.features.mainScreen.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
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
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppsSort
import ru.mrkurilin.helpDevs.mainScreen.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsSortDialog(
    onAppsSortSelected: (AppsSort) -> Unit,
    onDismissRequest: () -> Unit,
    selectedAppsSort: AppsSort,
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
                    text = stringResource(id = R.string.apps_sort),
                    fontWeight = FontWeight.Bold
                )

                AppsSort.entries.forEach { appsSort ->
                    CheckedText(
                        text = stringResource(id = appsSort.titleId),
                        isChecked = appsSort == selectedAppsSort,
                        onItemSelected = { onAppsSortSelected(appsSort) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppsSortDialogPreview() {
    AppsSortDialog(
        {},
        {},
        AppsSort.NAME
    )
}
