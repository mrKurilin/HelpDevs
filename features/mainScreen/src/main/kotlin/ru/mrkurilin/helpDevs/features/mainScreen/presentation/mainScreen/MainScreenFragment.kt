package ru.mrkurilin.helpDevs.features.mainScreen.presentation.mainScreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                        toggleInfoDialogVisibility = mainScreenViewModel::toggleInfoDialogVisibility,
                        toggleAddAppDialogVisibility = mainScreenViewModel::toggleAddAppDialogVisibility,
                        onAddAppClicked = mainScreenViewModel::onAddAppClicked,
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
