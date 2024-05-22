package ru.mrkurilin.helpDevs.features.mainScreen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.mrkurilin.helpDevs.di.qualifiers.IODispatcher
import ru.mrkurilin.helpDevs.features.mainScreen.data.AppsRepository
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.IsAppLinkValid
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.dialogs.MainScreenDialog
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppUiModel
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppUiModelMapper
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppsFilter
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppsSort
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.state.MainScreenEvent
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.state.MainScreenState
import java.lang.CharSequence.compare
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val appsRepository: AppsRepository,
    private val appUiModelMapper: AppUiModelMapper,
    private val isAppLinkValid: IsAppLinkValid,
    @IODispatcher
    private val iODispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    val events = Channel<MainScreenEvent>()

    init {
        viewModelScope.launch(iODispatcher) {
            appsRepository.getApps().combine(state) { appModels, _ ->
                appModels.map { appUiModelMapper.mapFromAppModel(it) }
            }.collect { appUiModels ->
                _state.update { currentState ->
                    currentState.copy(
                        allApps = getSortedAndFilteredList(appUiModels),
                        appsToInstall = appUiModels.filter { !it.canBeDeleted && !it.isInstalled },
                        appsToDelete = appUiModels.filter { it.canBeDeleted && it.isInstalled },
                    )
                }
            }
        }
    }

    fun updateData() {
        viewModelScope.launch(iODispatcher) {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }

            appsRepository.updateData()

            _state.update { currentState ->
                currentState.copy(
                    isLoading = false,
                )
            }
        }
    }

    fun changeCanBeDeleted(appId: String) {
        viewModelScope.launch {
            appsRepository.changeCanBeDeleted(appId)
        }
    }

    fun toggleDialogVisibility(mainScreenDialog: MainScreenDialog) {
        when (mainScreenDialog) {
            MainScreenDialog.ADD_APP -> {
                _state.update { it.copy(showAddAppDialog = !it.showAddAppDialog) }
            }

            MainScreenDialog.APP_INFO -> {
                _state.update { it.copy(showInfoDialog = !it.showInfoDialog) }
            }

            MainScreenDialog.APPS_SORT -> {
                _state.update { it.copy(showAppsSortDialog = !it.showAppsSortDialog) }
            }

            MainScreenDialog.APPS_FILTER -> {
                _state.update { it.copy(showAppsFilterDialog = !it.showAppsFilterDialog) }
            }
        }
    }

    fun onAddAppClicked(appLink: String) {
        _state.update { it.copy(showAddAppDialog = false) }

        if (!isAppLinkValid(appLink)) {
            return
        }

        viewModelScope.launch(iODispatcher) {
            appsRepository.addApp(appLink)
            events.send(MainScreenEvent.AddedAppToInstall(appLink))
        }
    }

    fun onTabSelected(tabIndex: Int) {
        _state.update {
            it.copy(
                selectedTabIndex = tabIndex,
                selectedTab = MainScreenTabs.entries[tabIndex],
            )
        }
    }

    fun onAppsFilterSelected(appsFilter: AppsFilter) {
        _state.update { mainScreenState ->
            val currentFilters = mainScreenState.selectedAppsFilters

            val updatedFilters = if (currentFilters.contains(appsFilter)) {
                currentFilters - appsFilter
            } else {
                currentFilters + appsFilter
            }

            mainScreenState.copy(selectedAppsFilters = updatedFilters)
        }
    }

    fun onAppsSortSelected(appsSort: AppsSort) {
        _state.update { it.copy(
            selectedAppsSort = appsSort,
            showAppsSortDialog = false,
        ) }
    }

    fun changeSortDirection() {
        _state.update { it.copy(isDescendingSort = !it.isDescendingSort) }
    }

    fun clearFilters() {
        _state.update { it.copy(selectedAppsFilters = emptyList()) }
    }

    private fun getSortedAndFilteredList(appUiModels: List<AppUiModel>): List<AppUiModel> {
        val currentState = state.value

        val sortedAndFilteredList = appUiModels.sortedWith { o1, o2 ->
            when (currentState.selectedAppsSort) {
                AppsSort.NAME -> compare(o1.appName, o2.appName)
                AppsSort.INSTALL_DATE -> o1.installDate.compareTo(o2.installDate)
                AppsSort.APP_ID -> compare(o1.appId, o2.appId)
                AppsSort.INSTALL_DURATION -> {
                    o1.appInstalledDurationDays.compareTo(o2.appInstalledDurationDays)
                }
            }
        }.toMutableList()

        if (currentState.isDescendingSort) {
            sortedAndFilteredList.reverse()
        }

        currentState.selectedAppsFilters.forEach { appsFilter ->
            when (appsFilter) {
                AppsFilter.INSTALLED -> sortedAndFilteredList.removeAll { !it.isInstalled }
                AppsFilter.UNINSTALLED -> sortedAndFilteredList.removeAll { it.isInstalled }
            }
        }

        return sortedAndFilteredList
    }

    private fun Long?.compareTo(o2: Long?): Int {
        if (this === o2) return 0
        if (this == null) return -1
        if (o2 == null) return 1
        return this.compareTo(o2)
    }
}
