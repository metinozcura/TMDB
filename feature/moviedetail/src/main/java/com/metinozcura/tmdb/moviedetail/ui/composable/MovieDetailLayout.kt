package com.metinozcura.tmdb.moviedetail.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.metinozcura.tmdb.common.util.formatReleaseDate
import com.metinozcura.tmdb.moviedetail.R
import com.metinozcura.tmdb.common.util.formatRuntime
import com.metinozcura.tmdb.design.R as DesignR
import com.metinozcura.tmdb.moviedetail.model.MovieDetailUiData

private const val POSTER_HEIGHT_DP = 420

@Composable
internal fun MovieDetailLayout(
    movie: MovieDetailUiData,
    onBackClick: () -> Unit,
    onOpenImdb: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val scrollState = rememberScrollState()
    val formattedDate = movie.releaseDate.formatReleaseDate()
    val runtimeText = movie.runtime?.formatRuntime()
    val posterGradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, colorScheme.surface)
    )

    Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(POSTER_HEIGHT_DP.dp)
        ) {
            AsyncImage(
                model = movie.backdropUrl ?: movie.posterUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(posterGradient)
            )
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(DesignR.drawable.ic_back),
                    contentDescription = stringResource(DesignR.string.back),
                    tint = colorScheme.inverseOnSurface
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (formattedDate.isNotBlank()) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = colorScheme.primaryContainer,
                                contentColor = colorScheme.onPrimaryContainer
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = formattedDate,
                                style = MaterialTheme.typography.labelMedium,
                                color = colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                if (!movie.tagline.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = movie.tagline,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurface.copy(alpha = 0.9f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = colorScheme.surfaceContainerHigh,
                            contentColor = colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "%.1f".format(movie.voteAverage),
                            style = MaterialTheme.typography.labelMedium,
                            color = colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "•",
                        color = colorScheme.onSurface.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )
                    Text(
                        text = movie.genre.joinToString(" | ") { it.name },
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurface.copy(alpha = 0.95f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (runtimeText != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = runtimeText,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurface.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text(
                text = stringResource(DesignR.string.synopsis),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = movie.overview.ifBlank { stringResource(R.string.no_overview_available) },
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.onBackground.copy(alpha = 0.9f),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
            DetailTable(
                movie = movie,
                formattedDate = formattedDate,
                runtimeText = runtimeText,
                onOpenImdb = onOpenImdb
            )
        }
    }
}