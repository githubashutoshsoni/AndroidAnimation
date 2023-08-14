package com.example.renderproject

import PERLIN_NOISE
import android.graphics.RuntimeShader
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.renderproject.JellyfishPaths.headOfTentacle
import com.example.renderproject.JellyfishPaths.leftEye
import com.example.renderproject.JellyfishPaths.outerHead
import com.example.renderproject.JellyfishPaths.rightEye
import com.example.renderproject.JellyfishPaths.smile
import com.example.renderproject.JellyfishPaths.tenaclesPath
import com.example.renderproject.JellyfishPaths.tentacle8
import com.example.renderproject.JellyfishPaths.tentacleFifth
import com.example.renderproject.JellyfishPaths.tentacleForth
import com.example.renderproject.JellyfishPaths.tentacleSecond
import com.example.renderproject.JellyfishPaths.tentacleSeven
import com.example.renderproject.JellyfishPaths.tentacleSixth
import com.example.renderproject.JellyfishPaths.tentacleThree
import com.example.renderproject.ui.theme.RenderProjectTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RenderProjectTheme {
                // A surface container using the 'background' color from the theme


                ShowAwesomeFeatures()/* Surface(
                     modifier = Modifier.fillMaxSize(),
                     color = MaterialTheme.colorScheme.background
                 ) {

                     *//*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        JellyfishAnimation()
                    }*//*
                }*/
            }
        }
    }
}

private enum class ImageState {
    Small, Large
}

@Preview
@Composable
fun ShowAwesomeFeatures() {




    var playTime = remember { 0L }

    val animationScope = rememberCoroutineScope()


    val targetBasedAnimation = remember {
        TargetBasedAnimation(
            animationSpec = tween(2000),
            typeConverter = Float.VectorConverter,
            initialValue = 0f,
            targetValue = 1000f
        )
    }


    animationScope.launch {
        // Need to extract the already played time
        // If user pauses and resumes the animation, shifting start time by
        // play time will make sure that the animation will continue from the
        // last played time
        val startTime = withFrameNanos { it } - playTime

        // Only continue animating if the state is running
        while (true) {
            playTime = withFrameNanos { it } - startTime
//            animationValue = targetBasedAnimation.getValueFromNanos(playTime)
        }
    }


    val infiniteTransition = rememberInfiniteTransition()
    val infinitelyAnimatedFloat = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(),
            // The value will infinitely repeat from 0 to 1 and 1 to 0
            repeatMode = RepeatMode.Reverse
        )
    )

    val infinitelyAnimatedRadiusFloat = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 100f, animationSpec = infiniteRepeatable(
            animation = tween(),
            // The value will infinitely repeat from 0 to 1 and 1 to 0
            repeatMode = RepeatMode.Reverse
        )
    )


    var imageState by remember {
        mutableStateOf(ImageState.Small)
    }


    val transition = updateTransition(targetState = imageState, label = "BoxState Transition")

    val borderColor by transition.animateColor(
        label = "BoxState Color Transition",
        transitionSpec = { tween(durationMillis = 5000) }) {
        when (it) {
            ImageState.Small -> Color.Green
            ImageState.Large -> Color.Magenta
        }

    }

    val size by transition.animateFloat(label = "BoxState Size Transition",
        transitionSpec = { tween(durationMillis = 5000) }) {
        when (it) {
            ImageState.Small -> 90f
            ImageState.Large -> 130f
        }
    }



    LaunchedEffect(key1 = Unit, block = {
        imageState = ImageState.Large
    })


    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black)
    ) {
        drawCircle(
            color = Color.White,
            radius = infinitelyAnimatedRadiusFloat.value,
            // The circle will blink infinitely with the animation specs above
            alpha = infinitelyAnimatedFloat.value
        )
    }


}


val largeRadialGradient = object : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        val biggerDimension = maxOf(size.height, size.width)
        return RadialGradientShader(
            colors = listOf(Color(0xFF2be4dc), Color(0xFF243484)),
            center = size.center,
            radius = biggerDimension / 2f,
            colorStops = listOf(0f, 0.95f)
        )
    }
}


@Composable
fun JellyfishAnimation() {

    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f
            }
        }
    }

    val shader = RuntimeShader(PERLIN_NOISE)

    val coroutineScope = rememberCoroutineScope()

    val blinkAlphaAnimation = remember {
        Animatable(1f)
    }
    val blinkScaleAnimation = remember {
        Animatable(1f)
    }

    suspend fun instantBlinkAnimation() {
        val tweenSpec = tween<Float>(150, easing = LinearEasing)
        coroutineScope {
            launch {
                blinkAlphaAnimation.animateTo(0f, animationSpec = tweenSpec)
                blinkAlphaAnimation.animateTo(1f, animationSpec = tweenSpec)
            }
            launch {
                blinkScaleAnimation.animateTo(0.3f, animationSpec = tweenSpec)
                blinkScaleAnimation.animateTo(1f, animationSpec = tweenSpec)
            }
        }
    }

    val duration = 3000
    val transition = rememberInfiniteTransition()
    val translationY by transition.animateFloat(
        initialValue = 0f, targetValue = -30f, animationSpec = infiniteRepeatable(
            tween(duration, easing = EaseInOut), repeatMode = RepeatMode.Reverse
        )
    )

    val vectorPainter = rememberVectorPainter(
        defaultWidth = 530.46f.dp,
        defaultHeight = 563.1f.dp,
        viewportWidth = 530.46f,
        viewportHeight = 563.1f,
        autoMirror = true,
    ) { viewPortWidth, viewPortHeight ->


        Group(
            name = "jellyfish", translationY = translationY
        ) {
            Group("tentacles") {
                Path(
                    pathData = tenaclesPath, fill = SolidColor(Color.White), fillAlpha = 0.49f
                )
                Path(
                    pathData = tentacleSecond, fill = SolidColor(Color.White), fillAlpha = 0.49f
                )
                Path(
                    pathData = tentacleThree, fill = SolidColor(Color.White), fillAlpha = 0.49f
                )
                Path(
                    pathData = tentacleForth, fill = SolidColor(Color.White), fillAlpha = 0.49f
                )
                Path(
                    pathData = tentacleFifth, fill = SolidColor(Color.White), fillAlpha = 0.49f
                )
                Path(
                    pathData = tentacleSixth, fill = SolidColor(Color.White), fillAlpha = 0.49f
                )
                Path(
                    pathData = tentacleSeven, fill = SolidColor(Color.White), fillAlpha = 0.49f
                )
                Path(
                    pathData = tentacle8, fill = SolidColor(Color.White), fillAlpha = 0.49f
                )

            }

            Group("Smile") {
                Path(
                    pathData = smile, fill = SolidColor(Color.White), fillAlpha = 0.49f
                )
            }

            Group("Head") {
                Path(
                    pathData = headOfTentacle, fill = SolidColor(Color.White), fillAlpha = 0.49f
                )

                Path(
                    pathData = outerHead, fill = SolidColor(Color.White), fillAlpha = 0.49f
                )
            }

            Group(
                "eye-left",
                scaleY = blinkScaleAnimation.value,
                pivotY = 233f // vertical center of eye path


            ) {
                Path(
                    pathData = leftEye, fill = SolidColor(Color.White), fillAlpha = 0.49f
                )


            }
            Group(
                "eye-right",
                scaleY = blinkScaleAnimation.value,
                pivotY = 233f // vertical center of eye path
            ) {
                Path(
                    pathData = rightEye,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f,

                    )
            }

        }
    }



    Image(vectorPainter, contentDescription = "Jellyfish", modifier = Modifier
        .clickable {

            coroutineScope.launch {
                instantBlinkAnimation()
            }
        }
        .fillMaxSize()
        .background(largeRadialGradient)
        .onSizeChanged { size ->
            shader.setFloatUniform(
                "resolution", size.width.toFloat(), size.height.toFloat()
            )
        }
        .graphicsLayer {
            shader.setFloatUniform("time", time)
            renderEffect = android.graphics.RenderEffect
                .createRuntimeShaderEffect(
                    shader, "contents"
                )
                .asComposeRenderEffect()
        }


    )


}







