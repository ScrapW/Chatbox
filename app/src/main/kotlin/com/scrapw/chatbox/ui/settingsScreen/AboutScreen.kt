package com.scrapw.chatbox.ui.settingsScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.PestControl
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSubGroup
import com.alorma.compose.settings.ui.SettingsUrl
import com.scrapw.chatbox.BuildConfig
import com.scrapw.chatbox.ChatboxScreen
import com.scrapw.chatbox.R
import com.scrapw.chatbox.UpdateStatus
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

        AboutHeader()
        AboutUpdate(chatboxViewModel)


        SettingsSubGroup(stringResource(R.string.project)) {
            SettingsUrl(
                icon = ImageVector.vectorResource(R.drawable.github_mark),
                title = stringResource(R.string.source_code),
                url = "https://github.com/ScrapW/Chatbox"
            )

            SettingsUrl(
                icon = Icons.Default.PestControl,
                title = stringResource(R.string.send_a_feedback),
                url = "https://github.com/ScrapW/Chatbox/issues",
                useUrlAsSubtitle = true
            )

            SettingsUrl(
                icon = Icons.Default.Receipt,
                title = stringResource(R.string.release_page),
                url = "https://github.com/ScrapW/Chatbox/releases",
                useUrlAsSubtitle = false
            )
        }

        SettingsSubGroup(stringResource(R.string.release_page)) {
            SettingsMenuLink(
                icon = Icons.AutoMirrored.Default.ReceiptLong,
                title = stringResource(R.string.dependencies)
            ) {
                navController.navigate(ChatboxScreen.Dependencies.name)
            }
        }
        SettingsSubGroup(stringResource(R.string.license)) {
            AboutLicense()
        }
    }
}

@Composable
fun OldAboutHeader() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(10.dp)
            .clip(RoundedCornerShape(12.dp)),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {

            Image(
                painter = painterResource(R.drawable.icon),
                contentDescription = null,
                Modifier
                    .size(72.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(Modifier.width(8.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = BuildConfig.VERSION_NAME,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

@Composable
fun AboutHeader() {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.icon),
            contentDescription = null,
            modifier = Modifier
                .size(72.dp)
                .clip(
                    MaterialTheme.shapes.large
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = BuildConfig.VERSION_NAME,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
fun AboutUpdate(chatboxViewModel: ChatboxViewModel) {
    val updateInfo = chatboxViewModel.updateInfo

    if (chatboxViewModel.updateInfo.status == UpdateStatus.AVAILABLE && updateInfo.downloadUrl != null) {
        SettingsUrl(
            icon = Icons.Default.FileDownload,
            title = stringResource(R.string.download_latest_version),
            url = updateInfo.downloadUrl,
            subtitle = "${updateInfo.version}"
        )
    }
}

@Preview
@Composable
fun AboutLicense() {
    val copyright = """
            Chatbox: An android app to help send chatboxes to VRChat.
            Copyright (C) 2023-2024  ScrapW
    """.trimIndent()

    val gpl3LicenseText = """
    This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>.
    """.trimIndent()

    val extraNotice =
        "Notice: This license applies to the entire repo except for subfolders that have their own license file. In such cases, the license file in the subfolder takes precedence."

    val licenseLink = "https://www.gnu.org/licenses/"


    val annotatedString = buildAnnotatedString {
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.outline
            )
        ) {
            append(copyright)

            append("\n\n")

            append(gpl3LicenseText.substringBefore(licenseLink))
            pushStringAnnotation(tag = "licenses_link", annotation = licenseLink)
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(licenseLink)
            }
            pop()
            append(gpl3LicenseText.substringAfter(licenseLink))

            append("\n\n")

            append(extraNotice)
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 16.dp),
    ) {
        val uriHandler = LocalUriHandler.current

        ClickableText(
            modifier = Modifier.fillMaxWidth(),
            text = annotatedString,
            style = MaterialTheme.typography.bodySmall
        ) { offset ->
            annotatedString.getStringAnnotations(
                tag = "licenses_link", start = offset, end = offset
            ).firstOrNull()?.let {
                uriHandler.openUri(it.item)
            }
        }
    }
    Spacer(Modifier.height(32.dp))
}