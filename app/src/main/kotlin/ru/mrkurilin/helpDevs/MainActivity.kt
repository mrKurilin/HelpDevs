package ru.mrkurilin.helpDevs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.MainScreen
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.state.MainScreenEvent
import ru.mrkurilin.helpDevs.mainScreen.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                MainScreen(
                    handleEvent = ::handleEvent
                )
            }
        }
    }

    private fun handleEvent(mainScreenEvent: MainScreenEvent){
        when (mainScreenEvent) {
            is MainScreenEvent.AddedAppToInstall -> {
                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(mainScreenEvent.appLink))
                )
            }

            MainScreenEvent.InternetConnectionError -> {
                Toast.makeText(
                    this,
                    R.string.internet_connection_error,
                    Toast.LENGTH_LONG
                ).show()
            }

            MainScreenEvent.UnknownError -> {
                Toast.makeText(
                    this,
                    R.string.unknown_error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
