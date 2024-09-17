package com.example.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import com.example.terminal.data.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun Terminal(
    barList: List<Bar>
) {
    var visibleBarsCount by remember {
        mutableIntStateOf(100)
    }

    var terminalWidth by remember {
        mutableFloatStateOf(0f)
    }

    val barWidth by remember {
        derivedStateOf {
            terminalWidth / visibleBarsCount
        }
    }

    var scrolledBy by remember {
        mutableFloatStateOf(0f)
    }

    val visibleBars by remember {
        derivedStateOf {
            val startIndex = (scrolledBy / barWidth).roundToInt().coerceAtLeast(minimumValue = 0)
            val endIndex = (startIndex + visibleBarsCount).coerceAtMost(maximumValue = barList.size)
            barList.subList(startIndex,endIndex)
        }
    }

    val transformableState = TransformableState { zoomChange, panChange, _ ->
        visibleBarsCount = (visibleBarsCount / zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_BARS_COUNT, barList.size)

        scrolledBy = (scrolledBy + panChange.x)
            .coerceAtLeast(0f)
            .coerceAtMost(barList.size * barWidth - terminalWidth)
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .transformable(transformableState)
    ) {
        terminalWidth = size.width
        val max = visibleBars.maxOf { it.high }
        val min = visibleBars.minOf { it.low }
        val pxPerPoint = size.height / (max - min)
        translate(left = scrolledBy) {
            barList.forEachIndexed { index, bar ->
                val offsetX = size.width - (index * barWidth)
                drawLine(
                    color = Color.White,
                    start = Offset(offsetX, size.height - ((bar.low - min)) * pxPerPoint),
                    end = Offset(offsetX, size.height - ((bar.high - min)) * pxPerPoint),
                    strokeWidth = 1f
                )
                drawLine(
                    color = if (bar.open < bar.close) Color.Green else Color.Red,
                    start = Offset(offsetX, size.height - ((bar.open - min)) * pxPerPoint),
                    end = Offset(offsetX, size.height - ((bar.close - min)) * pxPerPoint),
                    strokeWidth = barWidth / 2
                )
            }
        }
    }
}