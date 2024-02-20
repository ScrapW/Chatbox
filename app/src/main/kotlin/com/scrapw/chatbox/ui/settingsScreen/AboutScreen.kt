package com.scrapw.chatbox.ui.settingsScreen

import UpdateStatus
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSubGroup
import com.alorma.compose.settings.ui.SettingsUrl
import com.scrapw.chatbox.BuildConfig
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

            val updateInfo = chatboxViewModel.updateInfo

            SettingsMenuLink(
                title = "Current version",
                subtitle = BuildConfig.VERSION_NAME,
                onClick = {},
                enabled = false
            )

            SettingsMenuLink(
                title = "Latest version",
                subtitle = when (updateInfo.status) {
                    UpdateStatus.AVAILABLE, UpdateStatus.UP_TO_DATE ->
                        updateInfo.version

                    UpdateStatus.FAILED ->
                        "Failed to retrieve"

                    UpdateStatus.NOT_CHECKED ->
                        "Retrieving..."
                },
                onClick = {},
                enabled = false
            )

            if (chatboxViewModel.updateInfo.status == UpdateStatus.AVAILABLE && updateInfo.downloadUrl != null) {
                SettingsUrl(
                    icon = Icons.Default.FileDownload,
                    title = "Download latest version",
                    url = updateInfo.downloadUrl
                )
            }
        }
    }
}