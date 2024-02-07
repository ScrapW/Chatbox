package com.scrapw.chatbox.ui.settingsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsSubGroup
import com.alorma.compose.settings.ui.SettingsUrl
import com.scrapw.chatbox.R
import com.scrapw.chatbox.ui.ChatboxViewModel

@Composable
fun AboutScreen(
    chatboxViewModel: ChatboxViewModel,
    navController: NavController
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SettingsSubGroup(stringResource(R.string.about)) {
            SettingsUrl(
                icon = ImageVector.vectorResource(R.drawable.github_mark),
                title = stringResource(R.string.source_code),
                url = "https://github.com/ScrapW/Chatbox"
            )

            SettingsUrl(
                icon = Icons.Default.Update,
                title = stringResource(R.string.check_updates),
                url = "https://github.com/ScrapW/Chatbox/releases",
                useUrlAsSubtitle = false
            )
        }
    }
}