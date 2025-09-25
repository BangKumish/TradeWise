package id.bangkumis.tradewise.ui.components.linechart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope

interface ILabelDrawer {
    fun requiredXAboveHeight(drawScope: DrawScope): Float = 0f
    fun requiredAboveBarHeight(drawScope: DrawScope): Float = 0f

    fun drawLabel(
        drawScope: DrawScope,
        canvas: Canvas,
        label: Any?,
        pointLocation: Offset,
        xAxisArea: Rect,
        isHighestPrice: Boolean
    )
}