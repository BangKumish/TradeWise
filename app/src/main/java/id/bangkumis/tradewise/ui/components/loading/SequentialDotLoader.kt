package id.bangkumis.tradewise.ui.components.loading

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SequentialDotLoader(
    modifier: Modifier = Modifier,
    dotSize: Dp = 12.dp,
    expandedDotWidth: Dp = 30.dp,
    delayBetweenDots: Long = 300L
){
    var activeDot by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(delayBetweenDots)
            activeDot = (activeDot + 1) % 4
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        repeat(4) { index ->
            val width by animateDpAsState(
                targetValue = if (index == activeDot) expandedDotWidth else dotSize,
                animationSpec = tween(durationMillis = 200),
                label = "Dot Width Animation"
            )
            Box(
               modifier = Modifier
                   .size(width = width, height = dotSize)
                   .clip(CircleShape)
                   .background(MaterialTheme.colorScheme.primary)
            )
            if (index < 3){
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}