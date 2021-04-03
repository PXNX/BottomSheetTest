package pxnx.BottomSheetTest

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@ExperimentalMaterialApi
enum class ModalBottomSheetValue { Hidden, Shown }

@ExperimentalMaterialApi
class ModalBottomSheetState(
    initialValue: ModalBottomSheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (ModalBottomSheetValue) -> Boolean = { true }
) : SwipeableState<ModalBottomSheetValue>(
    initialValue = initialValue,
    animationSpec = animationSpec,
    confirmStateChange = confirmStateChange
) {
    val isVisible: Boolean
        get() = currentValue != ModalBottomSheetValue.Hidden

    suspend fun show() = animateTo(ModalBottomSheetValue.Shown)

    suspend fun hide() = animateTo(ModalBottomSheetValue.Hidden)

    internal val nestedScrollConnection = this.PreUpPostDownNestedScrollConnection

    companion object {
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (ModalBottomSheetValue) -> Boolean
        ): Saver<ModalBottomSheetState, *> = Saver(
            save = { it.currentValue },
            restore = {
                ModalBottomSheetState(
                    initialValue = it,
                    animationSpec = animationSpec,
                    confirmStateChange = confirmStateChange
                )
            }
        )
    }
}

@Composable
@ExperimentalMaterialApi
fun rememberModalBottomSheetState(
    initialValue: ModalBottomSheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (ModalBottomSheetValue) -> Boolean = { true }
): ModalBottomSheetState {
    return rememberSaveable(
        saver = ModalBottomSheetState.Saver(
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange
        )
    ) {
        ModalBottomSheetState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun BottomSheet(
    parentHeight: Int,
    topOffset: Dp = 0.dp,
    fillMaxHeight: Boolean = false,
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    shape: Shape = RoundedCornerShape(8.dp),
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = 0.dp,
    scrimColor: Color = ModalBottomSheetDefaults.scrimColor,
    content: @Composable () -> Unit
) {
    val topOffsetPx = with(LocalDensity.current) { topOffset.roundToPx() }

    var bottomSheetHeight = remember {  mutableStateOf(parentHeight.toFloat())}

    val scrollConnection = sheetState.PreUpPostDownNestedScrollConnection

    val scope = rememberCoroutineScope()

    BottomSheetLayout(
        maxHeight = parentHeight - topOffsetPx,
        fillMaxHeight = fillMaxHeight
    ) {
        val swipeable = Modifier.swipeable(
            state = sheetState,
            anchors = mapOf(
                parentHeight.toFloat() to ModalBottomSheetValue.Hidden,
                parentHeight - bottomSheetHeight.value to ModalBottomSheetValue.Shown
            ),
            orientation = Orientation.Vertical,
            resistance = null
        )

        Surface(
            shape = shape,
            color = backgroundColor,
            contentColor = contentColor,
            elevation = elevation,
            modifier = Modifier
                .nestedScroll(scrollConnection)
                .offset { IntOffset(0, sheetState.offset.value.roundToInt()) }
                .then(swipeable)
                .onGloballyPositioned {
                    bottomSheetHeight.value = it.size.height.toFloat()
                },
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .then(swipeable)) {
                content()
                Scrim(
                    color = scrimColor,
                    onDismiss = { scope.launch { sheetState.hide() } },
                    visible = sheetState.targetValue != ModalBottomSheetValue.Hidden
                )
            }
        }
    }
}


@Composable
private fun BottomSheetLayout(
    maxHeight: Int,
    fillMaxHeight: Boolean,
    content: @Composable () -> Unit
) {
    Layout(content = content) { measurables, constraints ->
        val sheetConstraints =
            if (fillMaxHeight) {
                constraints.copy(minHeight = maxHeight, maxHeight = maxHeight)
            } else {
                constraints.copy(maxHeight = maxHeight)
            }

        val placeable = measurables.first().measure(sheetConstraints)

        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}







@Composable
private fun Scrim(
    color: Color,
    onDismiss: () -> Unit,
    visible: Boolean
) {
    if (color != Color.Transparent) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = TweenSpec()
        )
        val dismissModifier = if (visible) {
            Modifier.pointerInput(onDismiss) { detectTapGestures { onDismiss() } }
        } else {
            Modifier
        }

        Canvas(
            Modifier
                .fillMaxSize()
                .then(dismissModifier)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}


































//////stolen

internal var minBound = Float.NEGATIVE_INFINITY

@ExperimentalMaterialApi
internal val <T> SwipeableState<T>.PreUpPostDownNestedScrollConnection: NestedScrollConnection
    get() = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.toFloat()
            return if (delta < 0 && source == NestedScrollSource.Drag) {
                performDrag(delta).toOffset()
            } else {
                Offset.Zero
            }
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            return if (source == NestedScrollSource.Drag) {
                performDrag(available.toFloat()).toOffset()
            } else {
                Offset.Zero
            }
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            val toFling = Offset(available.x, available.y).toFloat()
            return if (toFling < 0 && offset.value > minBound) {
                performFling(velocity = toFling)
                // since we go to the anchor with tween settling, consume all for the best UX
                available
            } else {
                Velocity.Zero
            }
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            performFling(velocity = Offset(available.x, available.y).toFloat())
            return available
        }

        private fun Float.toOffset(): Offset = Offset(0f, this)

        private fun Offset.toFloat(): Float = this.y
    }