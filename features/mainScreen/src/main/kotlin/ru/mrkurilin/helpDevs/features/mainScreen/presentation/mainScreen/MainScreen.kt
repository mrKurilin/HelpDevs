package ru.mrkurilin.helpDevs.features.mainScreen.presentation.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.StateFlow
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.infoDialog.InfoDialog
import ru.mrkurilin.helpDevs.mainScreen.R

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    stateFlow: StateFlow<MainScreenState>,
    updateData: () -> Unit,
    changeCanBeDeleted: (appId: String) -> Unit,
    toggleInfoDialog: () -> Unit,
) {
    val tabs = MainScreenTabs.entries

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val state by stateFlow.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = updateData
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(id = tabs[selectedTabIndex].titleId))
                },
                actions = {
                    IconButton(onClick = { toggleInfoDialog() }) {
                        Icon(imageVector = Icons.Filled.Info, contentDescription = null)
                    }
                },
            )
        },
        bottomBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        modifier = Modifier.height(48.dp),
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
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
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            if (state.showInfo) {
                InfoDialog(
                    toggleInfoDialog = toggleInfoDialog
                )
            }

            LazyColumn(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val selectedTabList = when (tabs[selectedTabIndex]) {
                    MainScreenTabs.CAN_BE_INSTALLED -> state.appsToInstall
                    MainScreenTabs.CAN_BE_DELETED -> state.appsToDelete
                    MainScreenTabs.ALL_APPS -> state.allApps
                }

                if (selectedTabList.isEmpty()) {
                    item {
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
                }

                items(selectedTabList.size) { index ->
                    AppItem(
                        appUiModel = selectedTabList[index],
                        changeCanBeDeleted = changeCanBeDeleted
                    )
                }
            }

            PullRefreshIndicator(
                refreshing = state.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = if (state.isLoading) Color.Red else Color.Green,
            )
        }
    }
}
