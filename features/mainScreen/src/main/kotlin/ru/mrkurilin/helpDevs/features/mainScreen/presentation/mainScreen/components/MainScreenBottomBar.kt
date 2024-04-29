package ru.mrkurilin.helpDevs.features.mainScreen.presentation.mainScreen.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.mainScreen.MainScreenTabs
import kotlin.enums.EnumEntries

@Composable
fun MainScreenTopBar(
    selectedTabIndex: Int,
    tabs: EnumEntries<MainScreenTabs>,
    onTabSelected: (Int) -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                modifier = Modifier.height(48.dp),
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                }
            ) {
                Text(
                    text = stringResource(id = tab.titleId),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
