package com.metinozcura.tmdb.movielist.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.metinozcura.tmdb.design.R
import com.metinozcura.tmdb.design.theme.AMROTheme

private val CardShape = RoundedCornerShape(20.dp)
private const val PosterAspectRatio = 2f / 3f

@Composable
internal fun MovieGridItem(
    modifier: Modifier = Modifier,
    posterUrl: String?,
    movieName: String,
    genre: String,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val posterGradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, colorScheme.surface)
    )
    Card(
        modifier = modifier.fillMaxWidth().padding(6.dp),
        onClick = onClick,
        shape = CardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(PosterAspectRatio)
        ) {
            AsyncImage(
                model = posterUrl,
                contentDescription = movieName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.img_placeholder_poster)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(posterGradient)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = movieName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = genre,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = colorScheme.onSurface.copy(alpha = 0.9f)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieGridItemPreview() {
    AMROTheme {
        MovieGridItem(
            modifier = Modifier.fillMaxWidth(),
            posterUrl = "https://image.tmdb.org/t/p/w500/f4aul3FyD3jv3v4bul1IrkWZvzq.jpg",
            movieName = "Onward",
            genre = "Animation | Family | Adventure",
            onClick = {}
        )
    }
}