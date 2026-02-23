package com.metinozcura.tmdb.navigation

import android.app.Activity
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.material3.MaterialTheme
import com.metinozcura.tmdb.R
import com.metinozcura.tmdb.design.theme.AppTheme
import com.metinozcura.tmdb.moviedetail.ui.MovieDetailScreen
import com.metinozcura.tmdb.moviedetail.viewmodel.MovieDetailEffect
import com.metinozcura.tmdb.moviedetail.viewmodel.MovieDetailViewModel
import com.metinozcura.tmdb.movielist.ui.MovieListScreen
import com.metinozcura.tmdb.movielist.viewmodel.MovieListEffect
import com.metinozcura.tmdb.movielist.viewmodel.MovieListViewModel
import com.metinozcura.tmdb.splash.ui.SplashScreen
import com.metinozcura.tmdb.splash.viewmodel.SplashEffect
import com.metinozcura.tmdb.splash.viewmodel.SplashViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Root navigation graph. Start at [Routes.Splash]; navigate to [Routes.MovieList] when
 * configuration loads successfully. Extend with more composable entries as needed.
 */
@Composable
fun AppNavigation() {
    AppTheme {
        val navController = rememberNavController()
        val context = LocalContext.current


        val listScrollStateSaver: Saver<LazyGridState, Any> = listSaver(
            save = { listOf(it.firstVisibleItemIndex, it.firstVisibleItemScrollOffset) },
            restore = { LazyGridState(it.getOrElse(0) { 0 }, it.getOrElse(1) { 0 }) }
        )
        val movieListScrollState = rememberSaveable(saver = listScrollStateSaver) {
            LazyGridState()
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Routes.Splash,
                modifier = Modifier.padding(innerPadding)
            ) {
            composable(Routes.Splash) {
                val viewModel: SplashViewModel = koinViewModel()

                LaunchedEffect(Unit) {
                    viewModel.effect.collect { effect ->
                        when (effect) {
                            is SplashEffect.NavigateToMain -> {
                                navController.navigate(Routes.MovieList) {
                                    popUpTo(Routes.Splash) { inclusive = true }
                                }
                            }
                            is SplashEffect.QuitApp -> (context as? Activity)?.finish()
                            is SplashEffect.LoadConfiguration -> { /* handled by ViewModel */ }
                        }
                    }
                }

                SplashScreen(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize(),
                    iconContent = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            modifier = Modifier.size(120.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                )
            }

            composable(Routes.MovieList) {
                val viewModel: MovieListViewModel = koinViewModel()

                LaunchedEffect(Unit) {
                    viewModel.effect.collect { effect ->
                        when (effect) {
                            is MovieListEffect.NavigateToMovieDetail -> {
                                navController.navigate("${Routes.MovieDetail}/${effect.movieId}")
                            }
                            MovieListEffect.LoadMovies,
                            MovieListEffect.LoadGenres -> { /* handled by ViewModel */ }
                        }
                    }
                }

                MovieListScreen(
                    listState = movieListScrollState,
                    viewModel = viewModel
                )
            }
            composable(
                route = "${Routes.MovieDetail}/{movieId}",
                arguments = listOf(
                    navArgument("movieId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
                if (movieId != null) {
                    val viewModel: MovieDetailViewModel = koinViewModel()

                    LaunchedEffect(Unit) {
                        viewModel.effect.collect { effect ->
                            when (effect) {
                                MovieDetailEffect.NavigateBack -> navController.popBackStack()
                                is MovieDetailEffect.LoadMovieDetails -> { /* handled by ViewModel */ }
                            }
                        }
                    }

                    MovieDetailScreen(
                        movieId = movieId,
                        viewModel = viewModel
                    )
                }
            }
            }
        }
    }
}
