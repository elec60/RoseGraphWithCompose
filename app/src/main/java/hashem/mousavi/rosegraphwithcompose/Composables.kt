package hashem.mousavi.rosegraphwithcompose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MainScreen() {
    val b = 4 // we have 2𝑏 petals
    val widthPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val a = widthPx / 2 // The length of each petal is 𝑎

    val animatable = remember {
        Animatable(initialValue = 0f)
    }

    var shouldFill by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        animatable.animateTo(
            1f,
            animationSpec = tween(durationMillis = 10000, easing = LinearEasing)
        )
        delay(400)
        shouldFill = true
        animatable.snapTo(0f)
        animatable.animateTo(
            1f,
            animationSpec = tween(durationMillis = 5000)
        )
    }
    val path = remember {
        Path().apply {
            moveTo(
                x = a,
                y = 0f
            )
        }
    }
    Canvas(modifier = Modifier.fillMaxSize()) {
        val theta = (b * animatable.value * 90f).toRadian()
        // r = a * cos(theta) -> sqrt(x2 + y2) = a * cos(tan-1(y/x))
        translate(left = this.size.width / 2f, top = this.size.height / 2f) {
            if (!shouldFill) {
                val x = a * (cos(b * theta) * cos(theta))
                val y = a * (cos(b * theta) * sin(theta))
                path.lineTo(x = x, y = y)
                drawPath(path = path, color = Color.Red, style = Stroke(width = 10f))
            } else {
                drawPath(path = path, color = Color.Red, style = Stroke(width = 10f))
                clipPath(path = path, clipOp = ClipOp.Intersect) {
                    drawCircle(
                        color = Color.Red,
                        center = Offset.Zero,
                        radius = widthPx * animatable.value
                    )
                }
            }
        }
    }
}

private fun Float.toRadian(): Float {
    return (this * PI / 180f).toFloat()
}