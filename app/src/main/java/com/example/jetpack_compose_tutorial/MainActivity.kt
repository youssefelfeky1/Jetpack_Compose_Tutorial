package com.example.jetpack_compose_tutorial

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.Dimension.Companion.DP
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF101010)),
                contentAlignment = Alignment.Center){

                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .border(1.dp, Color.Green, RoundedCornerShape(10.dp))
                        .padding(30.dp)) {
                    var volume by remember {
                        mutableStateOf(0f)
                    }
                    val barCount = 20
                    MusicKnob(modifier = Modifier.size(100.dp)){
                        volume = it
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    VolumeBar(modifier = Modifier.fillMaxWidth()
                        .height(30.dp),
                        activeBars = (barCount*volume).roundToInt(),
                        barsCount = barCount
                    )

                }
            }

        }


    }
}


val fontFamily = FontFamily(
    Font(R.font.lexend_bold, FontWeight.Bold),
    Font(R.font.lexend_light,FontWeight.Light),
    Font(R.font.lexend_medium,FontWeight.Medium),
    Font(R.font.lexend_regular,FontWeight.Normal),
    Font(R.font.lexend_extrabold,FontWeight.ExtraBold),
    Font(R.font.lexend_extralight,FontWeight.ExtraLight),
    Font(R.font.lexend_black,FontWeight.Black),
    Font(R.font.lexend_semibold,FontWeight.SemiBold),
    Font(R.font.lexend_thin,FontWeight.Thin)
)

@Composable
fun ColorBox(modifier: Modifier =Modifier,updateColor:(Color)->Unit){

    Box(modifier = modifier
        .fillMaxSize()
        .background(Color.Yellow)
        .clickable {
            updateColor(
                Color(
                    Random.nextFloat(),
                    Random.nextFloat(),
                    Random.nextFloat(),
                    1f
                )
            )

        })


}

@Composable
fun CircularProgressBar(
    percentage:Float,
    number:Int,
    fontSize:TextUnit = 28.sp,
    radius: Dp = 50.dp,
    color:Color = Color.Green,
    strokeWidth:Dp = 8.dp,
    animationDuration:Int = 2000,
    animDelay:Int = 100
){
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercantage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        label = "",
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animDelay
        )

    )
    LaunchedEffect(key1 = true){
        animationPlayed = true
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius*2f)
    ){
        Canvas(modifier = Modifier.size(radius*2f)){
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * curPercantage.value,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)

            )
        }
        Text(text = (curPercantage.value * number).toInt().toString(),
            color = Color.Black,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MusicKnob(
    modifier: Modifier = Modifier,
    limitingAngle:Float = 25f,
    onValueChange:(Float)->Unit
){
    var rotation by remember {
    mutableStateOf(limitingAngle)
    }

    var touchX by remember {
        mutableStateOf(0f)
    }
    var touchY by remember {
        mutableStateOf(0f)
    }
    
    var centerX by remember {
        mutableStateOf(0f)
    }
    var centerY by remember {
        mutableStateOf(0f)
    }
    
    Image(
        painter = painterResource(id = R.drawable.music_knob),
        contentDescription = "music knob",
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                val windowBounds = it.boundsInWindow()
                centerX = windowBounds.width / 2f
                centerY = windowBounds.height / 2f
            }
            .pointerInteropFilter { event ->
                touchX = event.x
                touchY = event.y
                val angle = -atan2((centerX - touchX), (centerY - touchY)) * (180f / PI).toFloat()

                when (event.action) {
                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_MOVE -> {
                        if (angle !in -limitingAngle..limitingAngle) {

                            val fixedAngle = if (angle in -180f..-limitingAngle) {
                                360 + angle
                            } else {
                                angle
                            }
                            rotation = fixedAngle

                            val percent = (fixedAngle - limitingAngle) / (360f - 2 * limitingAngle)
                            onValueChange(percent)
                            true
                        } else false

                    }

                    else -> false

                }

            }
            .rotate(rotation)
    )
}

@Composable
fun VolumeBar(
    modifier: Modifier = Modifier,
    activeBars:Int =0,
    barsCount:Int =10
){
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        val barWidth = remember {
            constraints.maxWidth / (2f*barsCount)
        }
        Canvas(modifier = modifier){
            for (i in 0 until barsCount){
                drawRoundRect(
                    color = if (i in 0..activeBars ) Color.Green else Color.DarkGray,
                    topLeft = Offset(i * barWidth *2f + barWidth/2f,0f),
                    size = Size(barWidth,constraints.maxHeight.toFloat()),
                    cornerRadius = CornerRadius(0f)
                )
            }
        }
//
    }

}





