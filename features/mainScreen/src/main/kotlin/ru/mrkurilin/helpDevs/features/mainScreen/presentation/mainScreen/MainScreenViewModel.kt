package ru.mrkurilin.helpDevs.features.mainScreen.presentation.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.mrkurilin.helpDevs.features.mainScreen.data.AppsRepository
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.IsAppLinkValid
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppUiModelMapper
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val appsRepository: AppsRepository,
    private val appUiModelMapper: AppUiModelMapper,
    private val isAppLinkValid: IsAppLinkValid,
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    val events = Channel<MainScreenEvent>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            appsRepository.getApps().collect { appModels ->
                val appUiModels = appModels.map { appUiModelMapper.mapFromAppModel(it) }
                _state.update { currentState ->
                    currentState.copy(
                        allApps = appUiModels,
                        appsToInstall = appUiModels.filter { !it.canBeDeleted && !it.isInstalled },
                        appsToDelete = appUiModels.filter { it.canBeDeleted && it.isInstalled },
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun toggleInfoDialogVisibility() {
        _state.update {
            it.copy(showInfoDialog = !it.showInfoDialog)
        }
    }

    fun toggleAddAppDialogVisibility() {
        _state.update {
            it.copy(showAddAppDialog = !it.showAddAppDialog)
        }
    }

    fun onAddAppClicked(appLink: String) {
        _state.update {
            it.copy(showAddAppDialog = false)
        }

        if (!isAppLinkValid(appLink)) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            appsRepository.addApp(appLink)
            events.send(MainScreenEvent.AddedAppToInstall(appLink))
        }
    }
}
