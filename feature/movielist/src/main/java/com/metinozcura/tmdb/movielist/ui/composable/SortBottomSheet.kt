package com.metinozcura.tmdb.movielist.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metinozcura.tmdb.design.R as DesignR
import com.metinozcura.tmdb.movielist.model.GenreItemUiData
import com.metinozcura.tmdb.movielist.R
import com.metinozcura.tmdb.movielist.model.SortBy
import com.metinozcura.tmdb.movielist.model.SortOrder
import kotlin.collections.forEach

@Composable
internal fun SortBottomSheetContent(
    genres: List<GenreItemUiData>,
    initialGenreIds: Set<Int>,
    initialSortBy: SortBy,
    initialSortOrder: SortOrder,
    onApply: (Set<Int>, SortBy, SortOrder) -> Unit,
    onCancel: () -> Unit
) {
    val pendingGenreIdsSaver: Saver<MutableState<Set<Int>>, List<Int>> = Saver(
        save = { it.value.toList() },
        restore = { value -> mutableStateOf((value as? List<*>).orEmpty().mapNotNull { (it as? Number)?.toInt() }.toSet()) }
    )
    val pendingSortBySaver: Saver<MutableState<SortBy>, String> = Saver(
        save = { it.value.name },
        restore = { raw -> mutableStateOf(runCatching { SortBy.valueOf(raw) }.getOrDefault(SortBy.POPULARITY)) }
    )
    val pendingSortOrderSaver: Saver<MutableState<SortOrder>, String> = Saver(
        save = { it.value.name },
        restore = { raw -> mutableStateOf(runCatching { SortOrder.valueOf(raw) }.getOrDefault(SortOrder.ASCENDING)) }
    )
    var pendingGenreIds by rememberSaveable(saver = pendingGenreIdsSaver) { mutableStateOf(initialGenreIds) }
    var pendingSortBy by rememberSaveable(saver = pendingSortBySaver) { mutableStateOf(initialSortBy) }
    var pendingSortOrder by rememberSaveable(saver = pendingSortOrderSaver) { mutableStateOf(initialSortOrder) }

    // Sync pending from initial only when applied filter changes (e.g. user tapped Apply), not on rotation
    var lastSyncedGenreIds by remember { mutableStateOf(initialGenreIds) }
    var lastSyncedSortBy by remember { mutableStateOf(initialSortBy) }
    var lastSyncedSortOrder by remember { mutableStateOf(initialSortOrder) }

    LaunchedEffect(initialGenreIds, initialSortBy, initialSortOrder) {
        if (initialGenreIds != lastSyncedGenreIds || initialSortBy != lastSyncedSortBy || initialSortOrder != lastSyncedSortOrder) {
            pendingGenreIds = initialGenreIds
            pendingSortBy = initialSortBy
            pendingSortOrder = initialSortOrder
            lastSyncedGenreIds = initialGenreIds
            lastSyncedSortBy = initialSortBy
            lastSyncedSortOrder = initialSortOrder
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .padding(bottom = 72.dp)
        ) {
            Text(
                text = stringResource(R.string.filter_sort),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Text(stringResource(DesignR.string.genre), modifier = Modifier.padding(bottom = 8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = pendingGenreIds.isEmpty(),
                    onClick = { pendingGenreIds = emptySet() },
                    label = { Text(stringResource(DesignR.string.all)) }
                )
                genres.forEach { genre ->
                    FilterChip(
                        selected = genre.id in pendingGenreIds,
                        onClick = {
                            pendingGenreIds = if (genre.id in pendingGenreIds) {
                                pendingGenreIds - genre.id
                            } else {
                                pendingGenreIds + genre.id
                            }
                        },
                        label = { Text(genre.name) }
                    )
                }
            }

            Text(stringResource(DesignR.string.sort), modifier = Modifier.padding(top = 20.dp, bottom = 8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    SortBy.POPULARITY to stringResource(DesignR.string.popularity),
                    SortBy.TITLE to stringResource(DesignR.string.title),
                    SortBy.RELEASE_DATE to stringResource(R.string.release_date)
                ).forEach { (option, label) ->
                    FilterChip(
                        selected = pendingSortBy == option,
                        onClick = { pendingSortBy = option },
                        label = { Text(label) }
                    )
                }
            }

            Text(stringResource(DesignR.string.order), modifier = Modifier.padding(top = 20.dp, bottom = 8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    SortOrder.ASCENDING to stringResource(DesignR.string.ascending),
                    SortOrder.DESCENDING to stringResource(DesignR.string.descending)
                ).forEach { (option, label) ->
                    FilterChip(
                        selected = pendingSortOrder == option,
                        onClick = { pendingSortOrder = option },
                        label = { Text(label) }
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(DesignR.string.cancel))
                }
                Button(
                    onClick = { onApply(pendingGenreIds, pendingSortBy, pendingSortOrder) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(DesignR.string.apply))
                }
            }
        }
    }
}