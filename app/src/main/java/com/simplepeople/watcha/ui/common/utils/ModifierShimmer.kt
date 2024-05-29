package com.simplepeople.watcha.ui.common.utils

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

fun Modifier.shimmerEffect(): Modifier =
    composed {
        var size by remember {
            mutableStateOf(IntSize.Zero)
        }
        val transition = rememberInfiniteTransition()
        val startOffsetX by transition.animateFloat(
            initialValue = -3 * size.width.toFloat(),
            targetValue = 3 * size.width.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    750,
                    delayMillis = 500
                )
            )
        )

        background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xA1AAA3A3),
                    Color(0xD8EBE1E1),
                    Color(0xA1AAA3A3)
                ),
                start = Offset(startOffsetX, 0f),
                end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
            )
        )
            .onGloballyPositioned {
                size = it.size
            }
    }