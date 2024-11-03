package com.jahanshahi.magicmatrix

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun SquareShape(
    square: Square,
    squareSize: Double = 6.0,
    squareColor: Color,
    touchLocation: Offset,
) {
    updateSquare(
        square = square,
        touchLocation = touchLocation,
    )
    Canvas(
        modifier = Modifier.size((squareSize).dp),
        onDraw = {
            drawRect(
                color = squareColor,
                size = Size(width = squareSize.toFloat(), height = squareSize.toFloat()),
                topLeft = Offset(square.x.toFloat(), square.y.toFloat())
            )
        }
    )
}

private fun updateSquare(
    square: Square,
    touchLocation: Offset,
    touchBoundingSize: Double = 100.0,
    squareInertia: Double = 0.4
) {
    val dx = touchLocation.x - square.x
    val dy = touchLocation.y - square.y
    val distanceSquared = dx * dx + dy * dy
    val touchBoundingSizeSquared = touchBoundingSize * touchBoundingSize
    if (distanceSquared < (touchBoundingSizeSquared)) {
        val distance = sqrt(distanceSquared)
        val force = (touchBoundingSize - distance) / touchBoundingSize
        val angle = atan2(dy, dx)
        val targetX = square.x - cos(angle) * force * 50
        val targetY = square.y - sin(angle) * force * 50

        square.vX += (targetX - square.x) * squareInertia
        square.vY += (targetY - square.y) * squareInertia
    }
    // Applying inertia to the squares
    square.vX *= 0.9
    square.vY *= 0.9
    // Update square positions based on calculated velocities
    square.x += square.vX
    square.y += square.vY
    // Slowly move squares back to origin
    val dx2 = square.initX - square.x
    val dy2 = square.initY - square.y
    if (dx2 * dx2 + dy2 * dy2 > 1) {
        square.x += dx2 * 0.2
        square.y += dy2 * 0.2
    }
}