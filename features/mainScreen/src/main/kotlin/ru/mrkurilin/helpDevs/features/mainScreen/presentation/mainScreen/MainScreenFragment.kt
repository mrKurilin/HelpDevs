package ru.mrkurilin.helpDevs.features.mainScreen.presentation.mainScreen

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
                        toggleInfoDialog = mainScreenViewModel::toggleDialog,
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainScreenViewModel.updateData()
    }
}
