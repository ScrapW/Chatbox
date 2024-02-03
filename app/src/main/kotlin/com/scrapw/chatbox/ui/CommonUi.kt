package com.scrapw.chatbox.ui

import android.os.Build
import android.view.HapticFeedbackConstants

object HapticConstants {
    val optionToggleOn =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            HapticFeedbackConstants.TOGGLE_ON
        } else {
            HapticFeedbackConstants.KEYBOARD_TAP
        }

    val optionToggleOff =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            HapticFeedbackConstants.TOGGLE_OFF
        } else {
            HapticFeedbackConstants.KEYBOARD_TAP
        }

    val button =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            HapticFeedbackConstants.CONFIRM
        } else {
            HapticFeedbackConstants.KEYBOARD_TAP
        }

    val send = button
}
