package com.scrapw.chatbox.overlay

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlin.math.roundToInt

// From comments of https://gist.github.com/handstandsam/6ecff2f39da72c0b38c07aa80bbb5a2f
// https://www.jetpackcompose.app/snippets/OverlayService

// TODO: https://gist.github.com/handstandsam/6ecff2f39da72c0b38c07aa80bbb5a2f?permalink_comment_id=4216617#gistcomment-4216617

class OverlayService : Service() {

    lateinit var composeView: ComposeView
    lateinit var msgComposeView: ComposeView

    private val lifecycleOwner = MyLifecycleOwner()

    enum class Window {
        NONE,
        BUTTON,
        MESSENGER
    }

    var currentWindow = Window.NONE

    private val windowManager get() = getSystemService(WINDOW_SERVICE) as WindowManager

    private var windowParams = WindowManager.LayoutParams().apply {
        width = WindowManager.LayoutParams.WRAP_CONTENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
        format = PixelFormat.TRANSLUCENT
        type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        windowAnimations = android.R.style.Animation_Dialog
        gravity = Gravity.START or Gravity.TOP
        flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    private var msgWindowParams = WindowManager.LayoutParams().apply {
        width = WindowManager.LayoutParams.WRAP_CONTENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
        format = PixelFormat.TRANSLUCENT
        type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        windowAnimations = android.R.style.Animation_Dialog
        gravity = Gravity.START or Gravity.TOP
        flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)
    }

    override fun onCreate() {
        super.onCreate()
        composeView = ComposeView(this)
        msgComposeView = ComposeView(this)

        val f = Rect().also { composeView.getWindowVisibleDisplayFrame(it) }
        val w = f.width()
        val h = f.height()

        setInitPos(w, h)

        initOverlay()

        switchOverlay(Window.BUTTON)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (currentWindow == Window.BUTTON) {
            windowManager.removeViewImmediate(composeView)
        } else if (currentWindow == Window.MESSENGER) {
            windowManager.removeViewImmediate(msgComposeView)
        }

        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    fun setInitPos(w: Int, h: Int) {
        windowParams = windowParams.apply {
            x = w
            y = (h * 0.7).toInt()
        }
        overlayOffset = Offset(x = windowParams.x.toFloat(), y = windowParams.y.toFloat())
    }


//    val expanded = mutableStateOf(false)

    @SuppressLint("ClickableViewAccessibility")
    private fun initOverlay() {
        composeView.setContent {
            OverlayDraggableContainer {
                ButtonOverlay {
                    switchOverlay(Window.MESSENGER)
                }
            }
        }

        msgComposeView.setContent {
            MessengerOverlay {
                switchOverlay(Window.BUTTON)
            }
        }

        msgComposeView.setOnTouchListener { _, event ->
            Log.d("action", event.toString())
            if (event.action == MotionEvent.ACTION_OUTSIDE) {

                switchOverlay(Window.BUTTON)

                Log.i("Touch Listener", "outside")
            } else {
                Log.i("Touch Listener", "inside")
            }
            true
        }

        // Trick The ComposeView into thinking we are tracking lifecycle


        val viewModelStoreOwner = object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore
                get() = ViewModelStore()
        }

        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        composeView.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)
        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)

        msgComposeView.setViewTreeLifecycleOwner(lifecycleOwner)
        msgComposeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)
        msgComposeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)

        // This is required or otherwise the UI will not recompose
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    private fun switchOverlay(destinationWindow: Window) {
        when (currentWindow) {
            Window.BUTTON ->
                windowManager.removeViewImmediate(composeView)

            Window.MESSENGER ->
                windowManager.removeViewImmediate(msgComposeView)

            Window.NONE -> {}
        }

        // TODO: Why have to removeAllViews() for composeView ???
        // TODO: Why composeView.childCount won't clear after removeViewImmediate ?
        when (destinationWindow) {
            Window.BUTTON -> {
                composeView.removeAllViews()
                windowManager.addView(composeView, windowParams)
            }

            Window.MESSENGER -> {
                msgComposeView.removeAllViews()
                windowManager.addView(msgComposeView, msgWindowParams)
            }

            Window.NONE -> {}
        }

        currentWindow = destinationWindow
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private var overlayOffset: Offset by mutableStateOf(Offset.Zero)

    @Composable
    fun OverlayDraggableContainer(
        modifier: Modifier = Modifier,
        content: @Composable BoxScope.() -> Unit
    ) = Box(
        modifier = modifier.pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()

                // Update our current offset
                val newOffset = Offset(
                    if (windowParams.gravity and Gravity.END == Gravity.END) {
                        overlayOffset.x - dragAmount.x
                    } else {
                        overlayOffset.x + dragAmount.x
                    },
                    if (windowParams.gravity and Gravity.BOTTOM == Gravity.BOTTOM) {
                        overlayOffset.y - dragAmount.y
                    } else {
                        overlayOffset.y + dragAmount.y
                    }
                )

                overlayOffset = newOffset

                // Update the layout params, and then the view
                windowParams.apply {
                    x = overlayOffset.x.roundToInt()
                    y = overlayOffset.y.roundToInt()
                }
                windowManager.updateViewLayout(composeView, windowParams)
            }
        },
        content = content
    )
}