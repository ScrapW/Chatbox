package com.scrapw.chatbox

import android.os.Build
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.scrapw.chatbox.data.SettingsStates
import com.scrapw.chatbox.ui.ChatboxViewModel
import com.scrapw.chatbox.ui.mainScreen.MainScreen
import com.scrapw.chatbox.ui.settingsScreen.SettingsScreen


enum class ChatboxScreen(@StringRes val title: Int) {
    Main(title = R.string.app_name),
    Settings(title = R.string.settings)
}

@Composable
fun ChatboxAppBar(
    currentScreen: ChatboxScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
//        colors = TopAppBarDefaults.mediumTopAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer
//        ),
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        actions = {
            if (currentScreen == ChatboxScreen.Main) {
                IconButton(onClick = navigateToSettings) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.settings_button),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun ChatboxApp(
    modifier: Modifier = Modifier,
    chatboxViewModel: ChatboxViewModel = viewModel(
        factory = ChatboxViewModel.Factory
    ),
    navController: NavHostController = rememberNavController()

) {

    val fullscreenState = SettingsStates.fullscreenState()

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = ChatboxScreen.valueOf(
        backStackEntry?.destination?.route ?: ChatboxScreen.Main.name
    )

    setFullscreen(fullscreenState.value && currentScreen == ChatboxScreen.Main)

    Scaffold(
        topBar = {
            ChatboxAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                navigateToSettings = { navController.navigate(ChatboxScreen.Settings.name) }
            )
        }
    ) { innerPadding ->
        val messengerUiState by chatboxViewModel.messengerUiState.collectAsState()


        NavHost(
            navController = navController,
            startDestination = ChatboxScreen.Main.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = ChatboxScreen.Main.name) {
                MainScreen(
                    modifier = modifier,
                    chatboxViewModel = chatboxViewModel,
                    uiState = messengerUiState
                )
            }
            composable(route = ChatboxScreen.Settings.name) {
                SettingsScreen(
                    chatboxViewModel = chatboxViewModel
                )
            }
        }
    }
}

@Composable
fun setFullscreen(value: Boolean) {
    val window = LocalContext.current.getActivity()?.window ?: return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val controller = window.insetsController ?: return

        if (value) {
            controller.hide(WindowInsets.Type.statusBars())
            controller.hide(WindowInsets.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            controller.show(WindowInsets.Type.statusBars())
            controller.show(WindowInsets.Type.navigationBars())
        }
    } else {
        @Suppress("DEPRECATION")
        if (value) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        } else {
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}