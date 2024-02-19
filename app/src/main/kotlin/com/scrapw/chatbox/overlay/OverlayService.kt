package com.scrapw.chatbox.overlay

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.scrapw.chatbox.overlay.ui.ButtonOverlay
import com.scrapw.chatbox.overlay.ui.MessengerOverlay
import com.scrapw.chatbox.ui.ChatboxViewModel
import com.scrapw.chatbox.ui.theme.OverlayTheme
import kotlin.math.roundToInt

// From comments of https://gist.github.com/handstandsam/6ecff2f39da72c0b38c07aa80bbb5a2f
// https://www.jetpackcompose.app/snippets/OverlayService

// TODO: https://gist.github.com/handstandsam/6ecff2f39da72c0b38c07aa80bbb5a2f?permalink_comment_id=4216617#gistcomment-4216617

class OverlayService : Service() {

    private lateinit var buttonComposeView: ComposeView
    private lateinit var msgComposeView: ComposeView

    private val lifecycleOwner = MyLifecycleOwner()

    private val buttonDefaultPos = Offset(1f, 0.7f)
    private val msgDefaultPos = Offset(0f, 0.1f)

    enum class Window {
        NONE,
        BUTTON,
        MESSENGER
    }

    private var currentWindow = Window.NONE

    private val windowManager get() = getSystemService(WINDOW_SERVICE) as WindowManager

    private val defaultOverlayParams
        get() = WindowManager.LayoutParams().apply {
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

    private var buttonWindowParams = defaultOverlayParams
    private var msgWindowParams = defaultOverlayParams.apply {
        gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
        flags = flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.inv()
    }


    override fun onCreate() {
        super.onCreate()
        Log.d("Service", "onCreate()")

        buttonComposeView = ComposeView(this)
        msgComposeView = ComposeView(this)

        orientation.value = resources.configuration.orientation

        onOrientationChange()
        initOverlay()

        switchOverlay(Window.BUTTON)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Service", "onDestroy()")

        if (currentWindow == Window.BUTTON) {
            windowManager.removeViewImmediate(buttonComposeView)
        } else if (currentWindow == Window.MESSENGER) {
            windowManager.removeViewImmediate(msgComposeView)
        }

        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initOverlay() {
        buttonComposeView.setContent {
            OverlayTheme {
                OverlayDraggableContainer {
                    ButtonOverlay {
                        switchOverlay(Window.MESSENGER)
                    }
                }

                val configuration = LocalConfiguration.current

                // https://stackoverflow.com/a/67612872
                LaunchedEffect(configuration) {
                    orientation.value = configuration.orientation
                    onOrientationChange()
                }
            }
        }

        msgComposeView.setContent {

            val chatboxViewModel: ChatboxViewModel =
                if (!ChatboxViewModel.isInstanceInitialized()) {
                    viewModel(
                        factory = ChatboxViewModel.Factory
                    )
                } else {
                    ChatboxViewModel.getInstance()
                }


            OverlayTheme {
                MessengerOverlay(chatboxViewModel) {
                    switchOverlay(Window.BUTTON)
                }

                val configuration = LocalConfiguration.current

                LaunchedEffect(configuration) {
                    orientation.value = configuration.orientation
                    onOrientationChange()
                }
            }
        }

        msgComposeView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                switchOverlay(Window.BUTTON)
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

        buttonComposeView.setViewTreeLifecycleOwner(lifecycleOwner)
        buttonComposeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)
        buttonComposeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)

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
                windowManager.removeViewImmediate(buttonComposeView)

            Window.MESSENGER ->
                windowManager.removeViewImmediate(msgComposeView)

            Window.NONE -> {}
        }

        // TODO: Why have to removeAllViews() for composeView ???
        // TODO: Why composeView.childCount won't clear after removeViewImmediate ?
        when (destinationWindow) {
            Window.BUTTON -> {
                buttonComposeView.removeAllViews()
                windowManager.addView(buttonComposeView, buttonWindowParams)
            }

            Window.MESSENGER -> {
                msgComposeView.removeAllViews()
                windowManager.addView(msgComposeView, msgWindowParams)
            }

            Window.NONE -> {}
        }

        currentWindow = destinationWindow
    }

    private fun update() {
        try {
            if (currentWindow == Window.BUTTON) {
                windowManager.updateViewLayout(buttonComposeView, buttonWindowParams)
            } else if (currentWindow == Window.MESSENGER) {
                windowManager.updateViewLayout(msgComposeView, msgWindowParams)
            }
        } catch (e: Exception) {
            // Ignore exception for now, but in production, you should have some
            // warning for the user here.
        }
    }

    var orientation = mutableStateOf(Configuration.ORIENTATION_PORTRAIT)

    private fun isPortrait(): Boolean {
        return orientation.value != Configuration.ORIENTATION_LANDSCAPE
    }

    var buttonPortraitPos: Offset? = null
    var buttonLandscapePos: Offset? = null

    var msgPortraitPos: Offset? = null
    var msgLandscapePos: Offset? = null

    private fun onOrientationChange() {
        //TODO: Shorter

        val f = Rect().also { buttonComposeView.getWindowVisibleDisplayFrame(it) }

        Log.d("isPortrait()", isPortrait().toString())
        Log.d("width()", f.width().toString())
        Log.d("height()", f.height().toString())


        if (isPortrait()) {
            if (buttonPortraitPos == null) {
                val f = Rect().also { buttonComposeView.getWindowVisibleDisplayFrame(it) }
                buttonPortraitPos = Offset(
                    x = f.width() * buttonDefaultPos.x,
                    y = f.height() * buttonDefaultPos.y,
                )
            }

            buttonWindowParams.apply {
                x = buttonPortraitPos!!.x.toInt()
                y = buttonPortraitPos!!.y.toInt()
            }

            overlayOffset = buttonPortraitPos as Offset

            if (msgPortraitPos == null) {
                val f = Rect().also { msgComposeView.getWindowVisibleDisplayFrame(it) }
                msgPortraitPos = Offset(
                    x = f.width() * msgDefaultPos.x,
                    y = f.height() * msgDefaultPos.y,
                )
            }

            msgWindowParams.apply {
                x = msgPortraitPos!!.x.toInt()
                y = msgPortraitPos!!.y.toInt()
            }

        } else {
            if (buttonLandscapePos == null) {
                val f = Rect().also { buttonComposeView.getWindowVisibleDisplayFrame(it) }
                buttonLandscapePos = Offset(
                    x = f.width() * buttonDefaultPos.x,
                    y = f.height() * buttonDefaultPos.y,
                )
            }

            buttonWindowParams.apply {
                x = buttonLandscapePos!!.x.toInt()
                y = buttonLandscapePos!!.y.toInt()
            }

            overlayOffset = buttonLandscapePos as Offset


            if (msgLandscapePos == null) {
                val f = Rect().also { msgComposeView.getWindowVisibleDisplayFrame(it) }
                msgLandscapePos = Offset(
                    x = f.width() * msgDefaultPos.x,
                    y = f.height() * msgDefaultPos.y,
                )
            }

            msgWindowParams.apply {
                x = msgLandscapePos!!.x.toInt()
                y = msgLandscapePos!!.y.toInt()
            }
        }
        update()
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
                    if (buttonWindowParams.gravity and Gravity.END == Gravity.END) {
                        overlayOffset.x - dragAmount.x
                    } else {
                        overlayOffset.x + dragAmount.x
                    },
                    if (buttonWindowParams.gravity and Gravity.BOTTOM == Gravity.BOTTOM) {
                        overlayOffset.y - dragAmount.y
                    } else {
                        overlayOffset.y + dragAmount.y
                    }
                )

                overlayOffset = newOffset

                // Update the layout params, and then the view
                buttonWindowParams.apply {
                    x = overlayOffset.x.roundToInt()
                    y = overlayOffset.y.roundToInt()
                }

                if (isPortrait()) {
                    buttonPortraitPos = overlayOffset
                } else {
                    buttonLandscapePos = overlayOffset
                }

                windowManager.updateViewLayout(buttonComposeView, buttonWindowParams)
            }
        },
        content = content
    )

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}