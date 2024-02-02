package com.scrapw.chatbox

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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

@Composable
fun ChatboxApp(
    modifier: Modifier = Modifier,
    chatboxViewModel: ChatboxViewModel = viewModel(
        factory = ChatboxViewModel.Factory
    ),
    navController: NavHostController = rememberNavController()

) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = ChatboxScreen.valueOf(
        backStackEntry?.destination?.route ?: ChatboxScreen.Main.name
    )


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
//                val context = LocalContext.current
                SettingsScreen(
                    chatboxViewModel = chatboxViewModel
                )
            }
        }
    }
}
