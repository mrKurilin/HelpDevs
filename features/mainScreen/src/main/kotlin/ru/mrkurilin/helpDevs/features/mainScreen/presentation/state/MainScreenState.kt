package ru.mrkurilin.helpDevs.features.mainScreen.presentation.state

import ru.mrkurilin.helpDevs.features.mainScreen.presentation.MainScreenTabs
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppUiModel
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppsFilter
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppsSort

data class MainScreenState(
    val isLoading: Boolean = true,
    val appsToInstall: List<AppUiModel> = listOf(),
    val appsToDelete: List<AppUiModel> = listOf(),
    val allApps: List<AppUiModel> = listOf(),
    val showInfoDialog: Boolean = false,
    val showAddAppDialog: Boolean = false,
    val showAppsSortDialog: Boolean = false,
    val showAppsFilterDialog: Boolean = false,
    val selectedTabIndex: Int = 0,
    val selectedTab: MainScreenTabs = MainScreenTabs.CAN_BE_INSTALLED,
    val selectedAppsSort: AppsSort = AppsSort.APP_ID,
    val selectedAppsFilters: List<AppsFilter> = listOf(),
    val isDescendingSort: Boolean = false,
)
