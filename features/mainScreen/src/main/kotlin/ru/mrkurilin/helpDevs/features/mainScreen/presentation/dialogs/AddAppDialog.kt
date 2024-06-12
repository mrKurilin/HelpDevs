package ru.mrkurilin.helpDevs.features.mainScreen.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mrkurilin.helpDevs.mainScreen.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAppDialog(
    onDismissRequest: () -> Unit,
    onAddAppClicked: (String) -> Unit,
    isAppLinkValid: (String) -> Boolean,
) {
    val textFromClipboard = LocalContext.current.getTextFromClipboard()

    var appLink by remember {
        mutableStateOf(
            if (isAppLinkValid(textFromClipboard)) {
                textFromClipboard
            } else {
                ""
            }
        )
    }

    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.add_app),
                )
                Spacer(modifier = Modifier.height(24.dp))

                TextField(
                    value = appLink,
                    onValueChange = {
                        appLink = it
                    },
                    label = { Text(stringResource(id = R.string.app_link)) }
                )

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                    ) {
                        Text(stringResource(id = R.string.dismiss))
                    }

                    TextButton(
                        onClick = { onAddAppClicked(appLink.trim()) },
                    ) {
                        Text(stringResource(id = R.string.ok))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddAppDialogPreview() {
    AddAppDialog(
        onDismissRequest = {},
        onAddAppClicked = {},
        isAppLinkValid = { false },
    )
}
