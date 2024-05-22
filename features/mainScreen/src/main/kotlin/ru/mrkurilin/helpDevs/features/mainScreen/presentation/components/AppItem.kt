package ru.mrkurilin.helpDevs.features.mainScreen.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppUiModel
import ru.mrkurilin.helpDevs.mainScreen.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppItem(
    appUiModel: AppUiModel,
    changeCanBeDeleted: (appId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(appUiModel.appLink))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(BorderStroke(2.dp, Color.Blue), RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable {
                context.startActivity(webIntent)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = appUiModel.appName,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.basicMarquee(),
            )
            Text(
                text = appUiModel.appId,
                modifier = Modifier.basicMarquee(),
            )
            if (appUiModel.isInstalled) {
                Text(
                    text = pluralStringResource(
                        R.plurals.installed_days,
                        appUiModel.appInstalledDurationDays,
                        appUiModel.appInstalledDurationDays
                    ),
                    modifier = Modifier.basicMarquee(),
                )
            }
        }

        if (appUiModel.isInstalled) {
            Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
        }

        IconButton(
            onClick = { changeCanBeDeleted(appUiModel.appId) },
        ) {
            Icon(
                imageVector = if (appUiModel.canBeDeleted) {
                    Icons.Rounded.Refresh
                } else {
                    Icons.Rounded.Delete
                },
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun AppItemPreview() {
    AppItem(
        appUiModel = AppUiModel(
            appName = "appName",
            appId = "com.example.com",
            appLink = "",
            canBeDeleted = false,
        ),
        changeCanBeDeleted = { }
    )
}

@Preview
@Composable
fun AppItemInstalledPreview() {
    AppItem(
        appUiModel = AppUiModel(
            appName = "appName",
            appId = "com.example.com",
            appLink = "",
            isInstalled = true,
            canBeDeleted = false,
            appInstalledDurationDays = 4,
        ),
        changeCanBeDeleted = { }
    )
}
