package com.example.thefinaldedication.Registrationpages

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun WashingMachineIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color.White
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val strokeWidth = width * 0.08f

        // Outer rectangle
        drawRect(
            color = tint,
            topLeft = Offset(0f, 0f),
            size = Size(width, height),
            style = Stroke(width = strokeWidth)
        )

        // Outer circle (drum)
        drawCircle(
            color = tint,
            radius = width * 0.25f,
            center = Offset(width * 0.5f, height * 0.5f),
            style = Stroke(width = strokeWidth)
        )

        // Inner circle (window)
        drawCircle(
            color = tint,
            radius = width * 0.08f,
            center = Offset(width * 0.5f, height * 0.5f),
            style = Stroke(width = strokeWidth)
        )

        // Control panel line
        drawLine(
            color = tint,
            start = Offset(0f, height * 0.3f),
            end = Offset(width, height * 0.3f),
            strokeWidth = strokeWidth
        )

        // Control button 1
        drawLine(
            color = tint,
            start = Offset(width * 0.75f, height * 0.15f),
            end = Offset(width * 0.75f, height * 0.2f),
            strokeWidth = strokeWidth
        )

        // Control button 2
        drawLine(
            color = tint,
            start = Offset(width * 0.625f, height * 0.15f),
            end = Offset(width * 0.625f, height * 0.2f),
            strokeWidth = strokeWidth
        )
    }
}
