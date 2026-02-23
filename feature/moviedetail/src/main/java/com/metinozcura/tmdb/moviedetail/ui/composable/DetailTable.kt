package com.metinozcura.tmdb.moviedetail.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metinozcura.tmdb.common.util.formatMoney
import com.metinozcura.tmdb.design.R as DesignR
import com.metinozcura.tmdb.moviedetail.R
import com.metinozcura.tmdb.moviedetail.model.MovieDetailUiData

@Composable
internal fun DetailTable(
    movie: MovieDetailUiData,
    formattedDate: String,
    runtimeText: String?,
    onOpenImdb: (String) -> Unit
) {
    val voteAverageLabel = stringResource(R.string.vote_average)
    val voteCountLabel = stringResource(R.string.vote_count)
    val releaseDateLabel = stringResource(R.string.release_date)
    val runtimeLabel = stringResource(DesignR.string.runtime)
    val budgetLabel = stringResource(DesignR.string.budget)
    val revenueLabel = stringResource(DesignR.string.revenue)
    val statusLabel = stringResource(DesignR.string.status)
    val imdbLabel = stringResource(DesignR.string.imdb)
    val openLabel = stringResource(DesignR.string.open)

    val row1 = listOf(
        voteAverageLabel to "%.1f".format(movie.voteAverage),
        voteCountLabel to movie.voteCount.toString(),
        releaseDateLabel to (formattedDate.ifBlank { movie.releaseDate })
    )
    val row2 = listOf(
        runtimeLabel to (runtimeText ?: movie.runtime?.let { "${it} min" } ?: "—"),
        budgetLabel to (if (movie.budget > 0L) movie.budget.formatMoney() else "—"),
        revenueLabel to (if (movie.revenue > 0L) movie.revenue.formatMoney() else "—")
    )
    val row3 = buildList {
        add(statusLabel to (movie.status.ifBlank { "—" }))
        if (movie.imdbLink != null) add(imdbLabel to openLabel)
    }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        DetailTableRow(row1)
        DetailTableRow(row2)
        DetailTableRow(
            row3,
            onImdbClick = movie.imdbLink?.let { link -> { onOpenImdb(link) } },
            imdbLabel = imdbLabel
        )
    }
}
