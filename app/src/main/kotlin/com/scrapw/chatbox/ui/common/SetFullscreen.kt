package com.scrapw.chatbox.ui.common

import android.os.Build
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.scrapw.chatbox.getActivity

@Composable
fun SetFullscreen(value: Boolean) {
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