package id.bangkumis.tradewise.ui.components.chartinterval
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChartIntervalButtonGroup(
    modifier: Modifier,
    onIntervalChanged: (TimeIntervals) -> Unit,
){
    val selectedColor = remember { mutableIntStateOf(0) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            14.dp,
            Alignment.CenterHorizontally
        )
    ){
        TimeIntervals.entries.forEachIndexed { index, intervals ->
            val backgroundColor by animateColorAsState(
                targetValue = if (selectedColor.intValue == index) MaterialTheme.colorScheme.primary else Color.Transparent
            )
            val textColor by animateColorAsState(
                targetValue = if (selectedColor.intValue == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )

            ChartInternalButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(100))
                    .requiredWidth(45.dp)
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        if (selectedColor.intValue != index){
                            selectedColor.intValue = index
                            onIntervalChanged.invoke(intervals)
                        }
                    },
                textColor = textColor,
                intervals = intervals
            )
        }
    }
}

@Composable
fun ChartInternalButton(
    modifier: Modifier,
    textColor: Color,
    intervals: TimeIntervals,
){
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        text = intervals.value,
        color = textColor
    )
}

@Preview
@Composable
fun ChartIntervalButtonGroupPreview(){
    ChartIntervalButtonGroup(
        modifier = Modifier,
        onIntervalChanged = {}
    )
}