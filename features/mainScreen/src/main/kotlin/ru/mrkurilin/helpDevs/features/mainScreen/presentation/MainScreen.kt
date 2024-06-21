package ru.mrkurilin.helpDevs.features.mainScreen.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.compose.koinViewModel
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.components.AppItem
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.components.EmptyListHolder
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.components.FilterSortRow
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.components.MainScreenTopBar
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.dialogs.MainScreenDialog
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.dialogs.MainScreenDialogs
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.state.MainScreenEvent

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    handleEvent: (MainScreenEvent) -> Unit,
) {
    val mainScreenViewModel = koinViewModel<MainScreenViewModel>()

    val tabs = MainScreenTabs.entries

    val state by mainScreenViewModel.state.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = mainScreenViewModel::updateData
    )

    LaunchedEffect(LocalLifecycleOwner.current) {
        mainScreenViewModel.events.receiveAsFlow().collect { mainScreenEvent ->
            handleEvent(mainScreenEvent)
        }
    }

    Scaffold(
        topBar = {
            MainScreenTopBar(
                title = stringResource(id = state.selectedTab.titleId),
                onInfoButtonClick = {
                    mainScreenViewModel.toggleDialogVisibility(MainScreenDialog.APP_INFO)
                },
            )
        },
        bottomBar = {
            MainScreenTopBar(
                selectedTabIndex = state.selectedTabIndex,
                tabs = tabs,
                onTabSelected = mainScreenViewModel::onTabSelected,
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = state.selectedTab == MainScreenTabs.CAN_BE_INSTALLED,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                FloatingActionButton(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    onClick = { mainScreenViewModel.toggleDialogVisibility(MainScreenDialog.ADD_APP) },
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "icon"
                    )
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
                val selectedTabList = when (state.selectedTab) {
                    MainScreenTabs.CAN_BE_INSTALLED -> state.appsToInstall
                    MainScreenTabs.CAN_BE_DELETED -> state.appsToDelete
                    MainScreenTabs.ALL_APPS -> state.allApps
                }

                item {
                    AnimatedVisibility(
                        visible = state.selectedTab == MainScreenTabs.ALL_APPS,
                        enter = expandVertically(),
                        exit = shrinkVertically(),
                    ) {
                        FilterSortRow(
                            onChangeSortDirectionClicked = mainScreenViewModel::changeSortDirection,
                            onSortClicked = {
                                mainScreenViewModel.toggleDialogVisibility(
                                    MainScreenDialog.APPS_SORT
                                )
                            },
                            onFilterClicked = {
                                mainScreenViewModel.toggleDialogVisibility(
                                    MainScreenDialog.APPS_FILTER
                                )
                            },
                            onClearFilterClicked = mainScreenViewModel::clearFilters,
                            isDescendingSort = state.isDescendingSort,
                        )
                    }
                }

                if (selectedTabList.isEmpty()) {
                    item {
                        EmptyListHolder()
                    }
                }

                itemsIndexed(
                    items = selectedTabList,
                    key = { _, appUiModel -> appUiModel.appId }
                ) { index, appUiModel ->
                    AppItem(
                        position = index + 1,
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(durationMillis = 600)
                        ),
                        appUiModel = appUiModel,
                        changeCanBeDeleted = mainScreenViewModel::changeCanBeDeleted,
                    )
                }
            }

            MainScreenDialogs(
                state = state,
                toggleDialogVisibility = mainScreenViewModel::toggleDialogVisibility,
                onAddAppClicked = mainScreenViewModel::onAddAppClicked,
                onAppsSortSelected = mainScreenViewModel::onAppsSortSelected,
                onAppsFilterSelected = mainScreenViewModel::onAppsFilterSelected,
                isAppLinkValid = { appLink -> mainScreenViewModel.isAppLinkValid(appLink) },
            )

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
        handleEvent = {},
    )
}
