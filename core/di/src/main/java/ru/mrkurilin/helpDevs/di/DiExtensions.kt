package ru.mrkurilin.helpDevs.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import ru.mrkurilin.helpDevs.di.api.SubComponentsProvider
import ru.mrkurilin.helpDevs.di.api.SubComponentsProviderHolder

fun <T : SubComponentsProvider> Fragment.requireSubComponentsProvider(): T {
    val subComponentsProviderHolder = requireActivity().application as? SubComponentsProviderHolder
        ?: error("application should implement SubComponentsProviderHolder")

    return subComponentsProviderHolder.subComponentsProvider as? T
        ?: error("subComponentsProvider should implement required interface")
}

inline fun <reified T : ViewModel> Fragment.lazyViewModel(
    noinline create: () -> T,
): Lazy<T> {
    return viewModels {
        ViewModelFactory(this, create)

    }
}
