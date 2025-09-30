package id.bangkumis.tradewise.domain.usecase

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetDominantColorUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    suspend operator fun invoke(imageUrl: String): Color{
        return try {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()
            val result = context.imageLoader.execute(request)
            val bitmap = (result.drawable as? BitmapDrawable)?.bitmap ?: return Color.Gray

            val palette = Palette.from(bitmap).generate()
            val color = palette.getDominantColor(Color.Gray.toArgb())?.let { Color(it) } ?: Color.Gray
            color
        } catch (e: Exception){
            Color.Gray
        }
    }
}