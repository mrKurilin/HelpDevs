package ru.mrkurilin.helpDevs.features.mainScreen.presentation.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.mrkurilin.helpDevs.features.mainScreen.data.AppsRepository
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppUiModelMapper
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val appsRepository: AppsRepository,
    private val appUiModelMapper: AppUiModelMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

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

    fun toggleDialog() {
        _state.update {
            it.copy(showInfo = !it.showInfo)
        }
    }
}
