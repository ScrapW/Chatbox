package com.scrapw.chatbox.overlay

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.Composable
import com.scrapw.chatbox.data.SettingsStates

@Composable
fun OverlayDaemon(context: Context) {
    val state = SettingsStates.floatingWindowState()

    if (state.value) {
        startOverlay(context)
    } else {
        stopOverlay(context)
    }

}


private fun startOverlay(context: Context) {
    Log.d("Service", "Start")

    context.startService(Intent(context, OverlayService::class.java))

//    if (Settings.canDrawOverlays(context)) {
//    } else {
//        checkOverlayPermission(context)
//    }
}

private fun stopOverlay(context: Context) {
    Log.d("Service", "STOP")
    
    context.stopService(Intent(context, OverlayService::class.java))
}

private fun checkOverlayPermission(context: Context) {

    val CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084


    if (!Settings.canDrawOverlays(context)) {
        //If the draw over permission is not available open the settings screen
        //to grant the permission.
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$context.packageName")
        )
//        startActivityForResult(
//            intent,
//            CODE_DRAW_OVER_OTHER_APP_PERMISSION
//        )
    }
}
