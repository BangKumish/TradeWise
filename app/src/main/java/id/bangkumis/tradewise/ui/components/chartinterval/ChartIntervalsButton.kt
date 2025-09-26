package id.bangkumis.tradewise.ui.components.chartinterval

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChartIntervalButtonGroup(
    modifier: Modifier,
    onIntervalChanged: (TimeIntervals) -> Unit,
){
    val selectedColor = remember { mutableStateOf(0) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            14.dp,
            Alignment.CenterHorizontally
        )
    ){
        TimeIntervals.entries.forEachIndexed { index, intervals ->
            val backgroundColor = if (selectedColor.value == index) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.background
            }
            val textColor = if (selectedColor.value == index) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            }

            ChartInternalButton(
                modifier = Modifier
                    .requiredWidth(45.dp)
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(12.dp)
                    ),
                textColor = textColor,
                intervals = intervals,
                onClick = {
                    if (selectedColor.value != index){
                        selectedColor.value = index
                        onIntervalChanged.invoke(intervals)
                    }
                }
            )
        }
    }
}

@Composable
fun ChartInternalButton(
    modifier: Modifier,
    textColor: Color,
    intervals: TimeIntervals,
    onClick: () -> Unit
){
    Text(
        modifier = modifier
            .clickable{ onClick() }
            .padding(vertical = 4.dp, horizontal = 8.dp),
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