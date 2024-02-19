package com.scrapw.chatbox.overlay

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.scrapw.chatbox.R
import com.scrapw.chatbox.data.SettingsStates

@Composable
fun OverlayDaemon(context: Context) {
    val state = SettingsStates.overlayState()

    if (state.value) {
        StartOverlay(context)
    } else {
        StopOverlay(context)
    }
}

@Composable
private fun StartOverlay(context: Context) {
    Log.d("Service", "startOverlay")

    val state = SettingsStates.overlayState()

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
//            if (result.resultCode == Activity.RESULT_OK) {
            if (Settings.canDrawOverlays(context)) {
                Log.d("Overlay Permission", "Permission granted")
                context.startService(Intent(context, OverlayService::class.java))
            } else {
                Log.d("Overlay Permission", "Permission denied")
                state.value = false
            }
        }

    if (Settings.canDrawOverlays(context)) {
        context.startService(Intent(context, OverlayService::class.java))
    } else {
        Log.d("Service", "Can't draw Overlays!")
        CheckOverlayPermission(context, startForResult)
    }
}

@Composable
private fun StopOverlay(context: Context) {
    Log.d("Service", "stopOverlay")

    context.stopService(Intent(context, OverlayService::class.java))
}

@Composable
private fun CheckOverlayPermission(
    context: Context,
    result: ManagedActivityResultLauncher<Intent, ActivityResult>
) {

    val CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084


    if (!Settings.canDrawOverlays(context)) {
        //If the draw over permission is not available open the settings screen
        //to grant the permission.
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$context.packageName")
        )

        val toast = Toast.makeText(
            context,
            stringResource(R.string.overlay_permission_request),
            Toast.LENGTH_LONG
        )

        LaunchedEffect(Unit) {
            toast.show()
            result.launch(intent)
        }


    }
}
