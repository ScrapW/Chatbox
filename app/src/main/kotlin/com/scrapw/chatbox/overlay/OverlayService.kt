package com.scrapw.chatbox.overlay

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

// From comments of https://gist.github.com/handstandsam/6ecff2f39da72c0b38c07aa80bbb5a2f
// https://www.jetpackcompose.app/snippets/OverlayService

class OverlayService : Service() {


    private val windowManager get() = getSystemService(WINDOW_SERVICE) as WindowManager


    val layoutFlag: Int = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY


    private val windowParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        layoutFlag,
        // https://developer.android.com/reference/android/view/WindowManager.LayoutParams
        // alt: WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
        // WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
//                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    )

    override fun onCreate() {
        super.onCreate()
        showOverlay()
    }

    override fun onDestroy() {
        super.onDestroy()

        windowManager.removeViewImmediate(composeView)

        val lifecycleOwner = MyLifecycleOwner()
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    lateinit var composeView: ComposeView

    @SuppressLint("ClickableViewAccessibility")
    private fun showOverlay() {


        composeView = ComposeView(this)


        composeView.setContent {
//            ChatboxTheme {
            Overlay(::enableKeyboard)
//            }
        }

        // Trick The ComposeView into thinking we are tracking lifecycle
        val viewModelStoreOwner = object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore
                get() = ViewModelStore()
        }
        val lifecycleOwner = MyLifecycleOwner()

        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        composeView.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)
        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)

        // This is required or otherwise the UI will not recompose
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        windowManager.addView(composeView, windowParams)



        composeView.setOnTouchListener { _, event ->
            Log.d("action", event.toString())
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                disableKeyboard()
                Log.i("Touch Listener", "outside");
            } else {
                Log.i("Touch Listener", "inside");
            }
            true
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    private fun enableKeyboard() {
        if (windowParams.flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE != 0) {
            windowParams.flags =
                windowParams.flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.inv()
            update()
        }
    }

    private fun disableKeyboard() {
        if (windowParams.flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE == 0) {
            windowParams.flags =
                windowParams.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            update()
        }
    }

    private fun update() {
        try {
            windowManager.updateViewLayout(composeView, windowParams)
        } catch (e: Exception) {
            // Ignore exception for now, but in production, you should have some
            // warning for the user here.
        }
    }
}

@Composable
fun Overlay(enableKeyboard: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    Log.d("Overlay gesture", "onPress()")
                    enableKeyboard()
                },
                onDoubleTap = { Log.d("Overlay gesture", "onDoubleTap()") },
                onLongPress = { Log.d("Overlay gesture", "onLongPress()") },
                onTap = { Log.d("Overlay gesture", "onTap()") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
        ) {
            Text(text = "Hello from Compose")

            var text by remember { mutableStateOf("Hello") }

            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Label") }
            )
        }
    }
}