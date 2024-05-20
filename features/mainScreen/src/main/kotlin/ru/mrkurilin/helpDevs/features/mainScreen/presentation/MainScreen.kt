package ru.mrkurilin.helpDevs.features.mainScreen.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.components.AddAppDialog
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.components.AppItem
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.components.EmptyListHolder
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.components.InfoDialog
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.components.MainScreenTopBar
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.state.MainScreenState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    stateFlow: StateFlow<MainScreenState>,
    updateData: () -> Unit,
    changeCanBeDeleted: (appId: String) -> Unit,
    toggleInfoDialogVisibility: () -> Unit,
    toggleAddAppDialogVisibility: () -> Unit,
    onAddAppClicked: (appLink: String) -> Unit,
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
            MainScreenTopBar(
                title = stringResource(id = tabs[selectedTabIndex].titleId),
                toggleInfoDialogVisibility = toggleInfoDialogVisibility,
            )
        },
        bottomBar = {
            MainScreenTopBar(
                selectedTabIndex = selectedTabIndex,
                tabs = tabs,
                onTabSelected = { tabPosition ->
                    selectedTabIndex = tabPosition
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = selectedTabIndex == 0,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                FloatingActionButton(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    onClick = toggleAddAppDialogVisibility,
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "icon")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
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
                        EmptyListHolder()
                    }
                }

                items(selectedTabList.size) { index ->
                    AppItem(
                        appUiModel = selectedTabList[index],
                        changeCanBeDeleted = changeCanBeDeleted
                    )
                }
            }

            if (state.showInfoDialog) {
                InfoDialog(
                    toggleInfoDialog = toggleInfoDialogVisibility,
                )
            }

            if (state.showAddAppDialog) {
                AddAppDialog(
                    onDismissRequest = toggleAddAppDialogVisibility,
                    onAddAppClicked = onAddAppClicked,
                )
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

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        stateFlow = MutableStateFlow(MainScreenState()),
        updateData = {},
        changeCanBeDeleted = {},
        toggleAddAppDialogVisibility = {},
        toggleInfoDialogVisibility = {},
        onAddAppClicked = {},
    )
}
