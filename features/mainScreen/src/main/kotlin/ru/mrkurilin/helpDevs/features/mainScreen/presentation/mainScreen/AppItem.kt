package ru.mrkurilin.helpDevs.features.mainScreen.presentation.mainScreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppItem(
    appModel: AppModel,
    changeCanBeDeleted: (appId: String) -> Unit,
) {
    val context = LocalContext.current
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(appModel.appLink))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(2.dp, Color.Blue), RoundedCornerShape(8.dp))
            .padding(4.dp)
            .clickable {
                context.startActivity(webIntent)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = appModel.appName,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.basicMarquee(),
            )
            Text(
                text = appModel.appId,
                modifier = Modifier.basicMarquee(),
            )
        }

        if (appModel.isInstalled) {
            Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
        }

        IconButton(
            onClick = { changeCanBeDeleted(appModel.appId) },
        ) {
            Icon(
                imageVector = if (appModel.canBeDeleted) {
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
        appModel = AppModel(
            appName = "ru.mrKurilin.helpDevs.data.local.AppModel",
            appId = "com.example.com",
            appLink = "",
            canBeDeleted = false,
            appearanceDate = 0,
        ),
        changeCanBeDeleted = { }
    )
}
