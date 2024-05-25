package ru.mrkurilin.helpDevs.features.mainScreen.presentation.dialogs

import androidx.compose.runtime.Composable
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppsFilter
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppsSort
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.state.MainScreenState

@Composable
fun MainScreenDialogs(
    state: MainScreenState,
    toggleDialogVisibility: (MainScreenDialog) -> Unit,
    onAddAppClicked: (String) -> Unit,
    onAppsSortSelected: (AppsSort) -> Unit,
    onAppsFilterSelected: (AppsFilter) -> Unit,
    isAppLinkValid: (String) -> Boolean,
) {
    if (state.showInfoDialog) {
        InfoDialog(
            toggleInfoDialog = { toggleDialogVisibility(MainScreenDialog.APP_INFO) },
        )
    }

    if (state.showAddAppDialog) {
        AddAppDialog(
            onDismissRequest = { toggleDialogVisibility(MainScreenDialog.ADD_APP) },
            onAddAppClicked = onAddAppClicked,
            isAppLinkValid = isAppLinkValid,
        )
    }

    if (state.showAppsSortDialog) {
        AppsSortDialog(
            onDismissRequest = { toggleDialogVisibility(MainScreenDialog.APPS_SORT) },
            onAppsSortSelected = onAppsSortSelected,
            selectedAppsSort = state.selectedAppsSort,
        )
    }

    if (state.showAppsFilterDialog) {
        AppsFilterDialog(
            onDismissRequest = { toggleDialogVisibility(MainScreenDialog.APPS_FILTER) },
            onAppsFilterSelected = onAppsFilterSelected,
            selectedAppsFilters = state.selectedAppsFilters,
        )
    }
}
