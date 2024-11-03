package com.jahanshahi.magicmatrix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.jahanshahi.magicmatrix.ui.theme.MagicMatrixTheme

class MainActivity : ComponentActivity() {
    val colors = listOf(
        Color.White,
        Color.Blue,
        Color.Red,
        Color.Green,
        Color.Yellow
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp
            val screenWidth = configuration.screenWidthDp
            val scale = resources.displayMetrics.density
            val screenSize = Size(screenWidth * scale, screenHeight * scale)
            val squares = initializeSquares(screenSize)
            var touchLocation by remember {
                mutableStateOf(Offset(-1000f, -1000f))
            }
            var squaresColor by remember {
                mutableStateOf(Color.White)
            }
            MagicMatrixTheme {
                Scaffold {
                    Box(
                        modifier = Modifier
                            .padding(paddingValues = it)
                            .fillMaxSize()
                            .background(color = Color.Black)
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDrag = { change, _ ->
                                        change.consume()
                                        touchLocation = change.position
                                    },
                                    onDragStart = { position ->
                                        touchLocation = position
                                    },
                                    onDragEnd = {
                                        touchLocation = Offset(-1000f, -1000f)
                                        resetScreen(squares)
                                    },
                                )
                            },
                    ) {
                        squares.map { square ->
                            SquareShape(
                                square = square,
                                squareColor = squaresColor,
                                touchLocation = touchLocation,
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Card(
                                shape = RoundedCornerShape(32.dp),
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 64.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ){
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth().padding(8.dp)
                                        .background(color = Color.Transparent),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    colors.map { color ->
                                        Card(
                                            shape = CircleShape,
                                            border = BorderStroke(width = 1.dp, color = Color.Black),
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clickable {
                                                    squaresColor = color
                                                },
                                            colors = CardDefaults.cardColors(
                                                containerColor = color
                                            )
                                        ) {

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initializeSquares(
        screenSize: Size,
        squareSpacing: Double = 30.0,
    ): MutableList<Square> {
        val rows = (screenSize.height / squareSpacing).toInt()
        val columns = (screenSize.width / squareSpacing).toInt()
        return List(size = (rows * columns)) {
            val x = ((it + 1) % columns) * squareSpacing
            val y = ((it + 1) / columns) * squareSpacing
            return@List Square(initX = x, initY = y, x = x, y = y)
        }.toMutableList()
    }

    private fun resetSquare(square: Square) {
        square.x = square.initX
        square.y = square.initY
        square.vX = 0.0
        square.vY = 0.0
    }

    private fun resetScreen(squares: List<Square>) {
        squares.map { square ->
            resetSquare(square)
        }
    }
}