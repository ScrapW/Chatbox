package com.alorma.compose.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.ui.internal.SettingsTileScaffold

@Composable
fun SettingsUrl(
    url: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    title: String,
    subtitle: String? = null,
    useUrlAsSubtitle: Boolean = true
) {
    SettingsUrl(
        url = url,
        modifier = modifier,
        enabled = enabled,
        icon = icon?.let {
            { Icon(icon, null, Modifier.size(24.dp)) }
        },
        title = { Text(title) },
        subtitle = subtitle?.let {
            { Text(subtitle) }
        },
        useUrlAsSubtitle = useUrlAsSubtitle
    )
}

@Composable
fun SettingsUrl(
    url: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)? = null,
    useUrlAsSubtitle: Boolean = true
) {
    val uriHandler = LocalUriHandler.current

    Surface {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(
                    enabled = enabled,
                    onClick = {
                        uriHandler.openUri(url)
                    },
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SettingsTileScaffold(
                title = title,
                enabled = enabled,
                subtitle = if (subtitle == null && useUrlAsSubtitle) {
                    { Text(url) }
                } else {
                    subtitle
                },
                icon = icon,
                action = {
                    Icon(
                        Icons.AutoMirrored.Default.OpenInNew,
                        null,
                        Modifier.size(18.dp)
                    )
                },
                actionDivider = false,
            )
        }
    }
}

@Preview
@Composable
internal fun SettingsUrlPreview() {
    MaterialTheme {
        SettingsUrl(
            url = "example.com",
            icon = { Icon(imageVector = Icons.Default.Web, contentDescription = null) },
            title = { Text(text = "Settings Url Preview") },
//            subtitle = { Text(text = "This is a longer text") }
        )
    }
}
