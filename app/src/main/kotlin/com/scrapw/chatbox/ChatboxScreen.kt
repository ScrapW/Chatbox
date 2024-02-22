package com.scrapw.chatbox

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.scrapw.chatbox.data.SettingsStates
import com.scrapw.chatbox.ui.ChatboxViewModel
import com.scrapw.chatbox.ui.common.ChatboxAppBar
import com.scrapw.chatbox.ui.common.SetFullscreen
import com.scrapw.chatbox.ui.mainScreen.MainScreen
import com.scrapw.chatbox.ui.settingsScreen.AboutScreen
import com.scrapw.chatbox.ui.settingsScreen.DependenciesScreen
import com.scrapw.chatbox.ui.settingsScreen.SettingsScreen


enum class ChatboxScreen(@StringRes val title: Int) {
    Main(title = R.string.app_name),
    Settings(title = R.string.settings),
    About(title = R.string.about),
    Dependencies(title = R.string.dependencies)
}

@Composable
fun ChatboxApp(
    modifier: Modifier = Modifier,
    chatboxViewModel: ChatboxViewModel =
        if (!ChatboxViewModel.isInstanceInitialized()) {
            viewModel(
                factory = ChatboxViewModel.Factory
            )
        } else {
            ChatboxViewModel.getInstance()
        },
    navController: NavHostController = rememberNavController()

) {
    chatboxViewModel.checkUpdate()

    val fullscreenState = SettingsStates.fullscreenState()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ChatboxScreen.valueOf(
        backStackEntry?.destination?.route ?: ChatboxScreen.Main.name
    )

    SetFullscreen(fullscreenState.value && currentScreen == ChatboxScreen.Main)

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            //TODO: Animation Fix
            ChatboxAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                scrollBehavior = scrollBehavior,
                navigateToSettings = { navController.navigate(ChatboxScreen.Settings.name) }
            )
        },
        // When LazyColumn have animateItemPlacement() item, nestedScroll will conflict with it.
        modifier = if (currentScreen != ChatboxScreen.Main) {
            Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        } else {
            Modifier
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
                    chatboxViewModel = chatboxViewModel,
                    navController = navController
                )
            }
            composable(route = ChatboxScreen.About.name) {
                AboutScreen(
                    chatboxViewModel = chatboxViewModel,
                    navController = navController
                )
            }
            composable(route = ChatboxScreen.Dependencies.name) {
                DependenciesScreen()
            }
        }
    }
}

