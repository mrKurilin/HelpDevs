package ru.mrkurilin.helpDevs.features.mainScreen.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.mrkurilin.helpDevs.di.lazyViewModel
import ru.mrkurilin.helpDevs.di.requireSubComponentsProvider
import ru.mrkurilin.helpDevs.features.mainScreen.di.MainScreenComponentProvider
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.state.MainScreenEvent
import ru.mrkurilin.helpDevs.mainScreen.R

class MainScreenFragment : Fragment() {

    private val mainScreenViewModel: MainScreenViewModel by lazyViewModel {
        requireSubComponentsProvider<MainScreenComponentProvider>()
            .provideMainScreenComponent()
            .mainScreenViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainScreen(
                        stateFlow = mainScreenViewModel.state,
                        updateData = mainScreenViewModel::updateData,
                        changeCanBeDeleted = mainScreenViewModel::changeCanBeDeleted,
                        onAddAppClicked = mainScreenViewModel::onAddAppClicked,
                        onTabSelected = mainScreenViewModel::onTabSelected,
                        onAppsFilterSelected = mainScreenViewModel::onAppsFilterSelected,
                        onAppsSortSelected = mainScreenViewModel::onAppsSortSelected,
                        toggleDialogVisibility = mainScreenViewModel::toggleDialogVisibility,
                        changeSortDirection = mainScreenViewModel::changeSortDirection,
                        clearFilters = mainScreenViewModel::clearFilters,
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainScreenViewModel.events.receiveAsFlow().collect { mainScreenEvent ->
                    when (mainScreenEvent) {
                        is MainScreenEvent.AddedAppToInstall -> {
                            requireContext().startActivity(
                                Intent(Intent.ACTION_VIEW, Uri.parse(mainScreenEvent.appLink))
                            )
                        }

                        MainScreenEvent.InternetConnectionError -> {
                            Toast.makeText(
                                requireContext(),
                                R.string.internet_connection_error,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        MainScreenEvent.UnknownError -> {
                            Toast.makeText(
                                requireContext(),
                                R.string.unknown_error,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainScreenViewModel.updateData()
    }
}
