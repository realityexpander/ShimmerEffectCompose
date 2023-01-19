package com.realityexpander.shimmereffectcompose

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun ShimmerListItem(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    if(isLoading) {
        Row(modifier = modifier) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .shimmerEffect()
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .warningEffect(2000, startColor = Color.Black, endColor = Color.Yellow)
        ) {
            Text(
                "hello",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White)
                ,
                color = Color.Black
            )
        }
        contentAfterLoading()
    }
}

// Custom modifier to add shimmer effect to any composable
// Shimmer needs to keep state for each element applied, so we use the 'composed' function
// to create a stateful modifier with an internal state (infinite animation and size).
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
//                Color(0xFFB8B5B5),
                Color(0xFFFF0000),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ).reversed(), // because of negative offset, the order of the colors is reversed
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned { layoutCoords ->
            // save the size of the element (after layout)
            size = layoutCoords.size
        }
}

// Custom modifier to add warning effect to any composable
// More advanced example: https://gist.github.com/DavidIbrahim/236dadbccd99c4fd328e53587df35a21
fun Modifier.warningEffect(
    delay: Int = 1000,
    spacing: Float = 50f,
    startColor: Color = Color.Black,
    endColor: Color = Color.Yellow
): Modifier = composed {

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val animatedFloat by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                delay,
                easing = LinearEasing
            )
        )
    )

    background(
            brush = Brush.linearGradient(
                0f to startColor,
                0.25f to startColor,

                0.25f to endColor,
                0.75f to endColor,

                0.75f to startColor,
                1f to startColor,

                start = Offset(0.0f + animatedFloat*100, 0.0f + animatedFloat*100),
                end = Offset(spacing + animatedFloat*100, spacing + animatedFloat*100),
                tileMode = TileMode.Repeated
            ),
        )
        .onGloballyPositioned { layoutCoords ->
            // save the size of the element (after layout)
            size = layoutCoords.size
        }
}