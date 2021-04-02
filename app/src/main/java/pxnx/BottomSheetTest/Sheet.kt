package pxnx.BottomSheetTest

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

enum class BottomSheetValue { SHOWING, HIDDEN }

@ExperimentalMaterialApi
@Composable
fun BottomSheet(
    parentHeight: Int,
    topOffset: Dp = 0.dp,
    fillMaxHeight: Boolean = false,
    sheetState: SwipeableState<BottomSheetValue>,
    shape: Shape = RoundedCornerShape(8.dp),
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    val topOffsetPx = with(LocalDensity.current) { topOffset.roundToPx() }
    var bottomSheetHeight = remember {  mutableStateOf(parentHeight.toFloat())}

    val scrollConnection = sheetState.PreUpPostDownNestedScrollConnection

    BottomSheetLayout(
        maxHeight = parentHeight - topOffsetPx,
        fillMaxHeight = fillMaxHeight
    ) {
        val swipeable = Modifier.swipeable(
            state = sheetState,
            anchors = mapOf(
                parentHeight.toFloat() to BottomSheetValue.HIDDEN,
                parentHeight - bottomSheetHeight.value to BottomSheetValue.SHOWING
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
            content()
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