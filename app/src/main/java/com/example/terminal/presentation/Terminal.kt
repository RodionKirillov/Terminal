package com.example.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.terminal.data.Bar

@Composable
fun Terminal(
    barList: List<Bar>
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val max = barList.maxOf { it.high }
        val min = barList.minOf { it.low }
        val barWidth = size.width / barList.size
        val pxPerPoint = size.height / (max - min)
        barList.forEachIndexed { index, bar ->
            val offsetX = index * barWidth
            drawLine(
                color = Color.White,
                start = Offset(offsetX, size.height - ((bar.low- min)) * pxPerPoint),
                end = Offset(offsetX, size.height - ((bar.high- min)) * pxPerPoint),
                strokeWidth = 1f
            )
        }
    }
}