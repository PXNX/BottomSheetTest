package pxnx.BottomSheetTest

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt



@Composable
@ExperimentalMaterialApi
fun ModalBottomSheetLayout(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = ModalBottomSheetDefaults.Elevation,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    sheetContentColor: Color = contentColorFor(sheetBackgroundColor),
    scrimColor: Color = ModalBottomSheetDefaults.scrimColor,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    BottomSheetStack(
        modifier = modifier,
        sheetContent = {
            Surface(
                Modifier
                    .fillMaxWidth()
                    .nestedScroll(sheetState.nestedScrollConnection)
                    .offset { IntOffset(0, sheetState.offset.value.roundToInt()) }
                   /* .semantics {
                        if (sheetState.isVisible) {
                            dismiss {
                                scope.launch { sheetState.hide() }
                                true
                            }
                        }else{
                            expand {
                                scope.launch { sheetState.show() }
                                true
                            }

                        }
                    }  */,
                shape = sheetShape,
                elevation = sheetElevation,
                color = sheetBackgroundColor,
                contentColor = sheetContentColor
            ) {
                Column(content = sheetContent)
            }
        },
        content = { constraints, sheetHeight ->
            val fullHeight = constraints.maxHeight.toFloat()
            val anchors = mapOf(
                fullHeight to ModalBottomSheetValue.Hidden,
                fullHeight - sheetHeight to ModalBottomSheetValue.Shown
            )

            val swipeable = Modifier.swipeable(
                state = sheetState,
                anchors = anchors,
                orientation = Orientation.Vertical,
                enabled = sheetState.currentValue != ModalBottomSheetValue.Hidden,
                resistance = null
            )

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
    )
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

@Composable
private fun BottomSheetStack(
    modifier: Modifier,
    sheetContent: @Composable () -> Unit,
    content: @Composable (constraints: Constraints, sheetHeight: Float) -> Unit
) {
    SubcomposeLayout(modifier) { constraints ->
        val sheetPlaceable =
            subcompose(BottomSheetStackSlot.SheetContent, sheetContent)
                .first().measure(constraints.copy(minWidth = 0, minHeight = 0))

        val sheetHeight = sheetPlaceable.height.toFloat()

        val placeable =
            subcompose(BottomSheetStackSlot.Content) { content(constraints, sheetHeight) }
                .first().measure(constraints)

        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
            sheetPlaceable.placeRelative(0, 0)
        }
    }
}

private enum class BottomSheetStackSlot { SheetContent, Content }

object ModalBottomSheetDefaults {
    val Elevation = 16.dp
    val scrimColor: Color
        @Composable
        get() = MaterialTheme.colors.onSurface.copy(alpha = 0.32f)
}
