package com.simplepeople.watcha.ui.main.moviedetails

import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.Genre
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.common.composables.ImageWithMessage
import com.simplepeople.watcha.ui.common.composables.LoadingIndicator
import com.simplepeople.watcha.ui.common.composables.topbar.ActionTopAppBar
import com.simplepeople.watcha.ui.common.utils.ScreenPaletteColors
import kotlinx.coroutines.Dispatchers
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MovieDetailsScreen(
    movieId: Long,
    navigateBack: () -> Unit,
    navigateToYoutubeScreen: (String) -> Unit,
    movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel<MovieDetailsViewModel, MovieDetailsViewModel.MovieDetailsViewModelFactory>(
        creationCallback = { factory ->
            factory.create(movieId = movieId)
        }
    )
) {
    val movieDetailsUiState by movieDetailsViewModel.movieDetailsState.collectAsState()
    val scrollStateArray = arrayOf(
        rememberLazyListState(),
        rememberLazyListState(),
        rememberLazyListState(),
        rememberLazyListState(),
        rememberLazyListState()
    )
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val surfaceColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface
    val screenPalette = remember {
        mutableStateOf(
            ScreenPaletteColors(
                dominantColor = surfaceColor,
                accentColor = surfaceColor,
                highlightColor = textColor,
                mediumLightColor = textColor
            )
        )
    }
    val colorAnimationTime = 500
    val animatedDominantColor by animateColorAsState(
        targetValue = screenPalette.value.dominantColor,
        label = "Dominant color",
        animationSpec = tween(durationMillis = colorAnimationTime)
    )
    val animatedAccentColor by animateColorAsState(
        targetValue = screenPalette.value.accentColor,
        label = "Accent color",
        animationSpec = tween(durationMillis = colorAnimationTime)
    )
    val animatedHighlightColor by animateColorAsState(
        targetValue = screenPalette.value.highlightColor,
        label = "Highlight color",
        animationSpec = tween(durationMillis = colorAnimationTime)
    )
    val animatedMediumColor by animateColorAsState(
        targetValue = screenPalette.value.mediumLightColor,
        label = "Medium color",
        animationSpec = tween(durationMillis = colorAnimationTime)
    )

    var movieWebUrl by remember { mutableStateOf("") }

    var targetAlpha by remember { mutableStateOf(0f) }
    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = 500),
        label = "Alpha animation"
    )

    LaunchedEffect(movieDetailsUiState.snackBarItem) {
        if (movieDetailsUiState.snackBarItem.show) {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = context.getString(movieDetailsUiState.snackBarItem.message),
                duration = SnackbarDuration.Short
            )
            movieDetailsViewModel.resetSnackbar()
        }
    }

    LaunchedEffect(movieDetailsUiState.scrollToTop) {
        if (movieDetailsUiState.scrollToTop) {
            scrollStateArray.forEach {
                it.scrollToItem(0)
            }
            movieDetailsViewModel.resetScrollingToTop()
        }
    }

    LaunchedEffect(movieDetailsUiState.movieState) {
        if (movieDetailsUiState.movieState is MovieDetailsMovieState.Success) {
            targetAlpha = 1f
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = animatedDominantColor,
        contentColor = textColor,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },

        topBar = {
            ActionTopAppBar(
                navigateBack = navigateBack,
                scrollBehavior = scrollBehavior,
                backgroundColor = animatedDominantColor,
                actionIcons = if (movieDetailsUiState.movieState is MovieDetailsMovieState.Success) {
                    listOf(
                        Pair(
                            Icons.Filled.Share
                        ) {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, movieWebUrl)
                            }
                            context.startActivity(
                                Intent.createChooser(
                                    intent,
                                    context.getString(R.string.share_movie)
                                )
                            )
                        }
                    )
                } else null,
                tintColor = animatedHighlightColor
            )
        }
    ) { paddingContent ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingContent)
        ) {
            when (val state = movieDetailsUiState.movieState) {
                is MovieDetailsMovieState.Error -> {
                    ImageWithMessage(
                        modifier = Modifier
                            .align(Alignment.Center),
                        image = R.drawable.movie_list_error,
                        message = state.message
                    )
                }

                is MovieDetailsMovieState.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                is MovieDetailsMovieState.Success -> {
                    val movie = state.movie
                    movieWebUrl = movie.webUrl

                    val paletteDefaultColor = MaterialTheme.colorScheme.surface.toArgb()

                    val backdropModel = ImageRequest.Builder(LocalContext.current)
                        .data(movie.backdrop)
                        .dispatcher(Dispatchers.IO)
                        .crossfade(true)
                        .memoryCacheKey(movie.backdrop)
                        .diskCacheKey(movie.backdrop)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .fallback(R.drawable.backdrop_placeholder)
                        .error(R.drawable.backdrop_error_placeholder)
                        .allowHardware(false)
                        .listener(
                            onSuccess = { _, result ->
                                val bitmap = result.drawable.toBitmap()
                                Palette.Builder(bitmap).generate { palette ->
                                    palette?.let {
                                        val dominantColor =
                                            Color(palette.getDarkVibrantColor(paletteDefaultColor))
                                        screenPalette.value = screenPalette.value.copy(
                                            dominantColor = dominantColor,
                                            accentColor = Color(
                                                ColorUtils.blendARGB(
                                                    dominantColor.toArgb(),
                                                    Color.Black.toArgb(),
                                                    0.2f
                                                )
                                            ),
                                            highlightColor = Color(
                                                ColorUtils.blendARGB(
                                                    dominantColor.toArgb(),
                                                    Color.White.toArgb(),
                                                    0.7f
                                                )
                                            ),
                                            mediumLightColor = Color(
                                                ColorUtils.blendARGB(
                                                    dominantColor.toArgb(),
                                                    Color.White.toArgb(),
                                                    0.35f
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        )
                        .build()

                    val wholePaddingModifier = Modifier
                        .padding(16.dp)

                    val topBottomPaddingModifier = Modifier
                        .padding(
                            top = 16.dp,
                            bottom = 16.dp
                        )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(alpha),
                        state = scrollStateArray[0]
                    ) {
                        item {
                            Header(
                                poster = movie.picture,
                                backdropModel = backdropModel,
                                isFavorite = movie.isFavorite,
                                voteAverage = movie.voteAverage,
                                toggleFavorite = {
                                    movieDetailsViewModel.toggleFavorite()
                                }
                            )
                        }
                        item {
                            Title(
                                modifier = wholePaddingModifier,
                                title = movie.title,
                                releaseYear = if (movie.releaseDate.isNotBlank()) LocalDate.parse(
                                    movie.releaseDate
                                ).year.toString() else "",
                                highlightColor = animatedHighlightColor
                            )
                        }
                        item {
                            Info(
                                runTime = movie.runTime,
                                genres = movie.genres,
                                releaseDate = movie.releaseDate,
                                navigateToTrailer = movie.trailer?.let { trailerKey ->
                                    {
                                        navigateToYoutubeScreen(trailerKey)
                                    }
                                },
                                accentColor = animatedAccentColor
                            )
                        }
                        item {
                            Overview(
                                modifier = wholePaddingModifier
                                    .animateItemPlacement(),
                                tagline = movie.tagline,
                                overview = movie.overview,
                                accentColor = animatedHighlightColor
                            )
                        }
                        item {
                            Credits(
                                modifier = topBottomPaddingModifier
                                    .animateItemPlacement(),
                                credits = movie.credits,
                                scrollState = scrollStateArray[1]
                            )
                        }
                        item {
                            Review(
                                modifier = topBottomPaddingModifier
                                    .animateItemPlacement(),
                                review = movie.review,
                                accentColor = animatedAccentColor,
                                highlightColor = animatedHighlightColor,
                                mediumLightColor = animatedMediumColor
                            )
                        }
                        item {
                            Images(
                                modifier = topBottomPaddingModifier
                                    .animateItemPlacement(),
                                images = movie.images,
                                scrollState = scrollStateArray[2]
                            )
                        }
                        item {
                            Suggestions(
                                modifier = topBottomPaddingModifier
                                    .animateItemPlacement(),
                                movieList = movie.similar,
                                loadMovie = { movieId ->
                                    movieDetailsViewModel.loadMovie(movieId)
                                },
                                title = movie.title,
                                scrollState = scrollStateArray[3]
                            )
                        }
                        item {
                            Suggestions(
                                modifier = topBottomPaddingModifier
                                    .animateItemPlacement(),
                                movieList = movie.recommendations,
                                loadMovie = { movieId ->
                                    movieDetailsViewModel.loadMovie(movieId)
                                },
                                scrollState = scrollStateArray[4]
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(
    poster: String,
    backdropModel: ImageRequest,
    isFavorite: Boolean,
    voteAverage: Int?,
    toggleFavorite: () -> Unit,
) {

    val backgroundColor: Color
    val fillColor: Color

    when (voteAverage) {
        in (0..30) -> {
            backgroundColor = RatingColors.LOWER_REVIEW.backgroundColor
            fillColor = RatingColors.LOWER_REVIEW.fillerColor
        }

        in (31..49) -> {
            backgroundColor = RatingColors.BAD_REVIEW.backgroundColor
            fillColor = RatingColors.BAD_REVIEW.fillerColor
        }

        in (50..69) -> {
            backgroundColor = RatingColors.OK_REVIEW.backgroundColor
            fillColor = RatingColors.OK_REVIEW.fillerColor
        }

        in (70..89) -> {
            backgroundColor = RatingColors.GOOD_REVIEW.backgroundColor
            fillColor = RatingColors.GOOD_REVIEW.fillerColor
        }

        in (90..100) -> {
            backgroundColor = RatingColors.EXCELLENT_REVIEW.backgroundColor
            fillColor = RatingColors.EXCELLENT_REVIEW.fillerColor
        }

        else -> {
            backgroundColor = RatingColors.NO_REVIEW.backgroundColor
            fillColor = RatingColors.NO_REVIEW.fillerColor
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth(),
            model = backdropModel,
            contentDescription = stringResource(id = R.string.backdrop),
            contentScale = ContentScale.FillWidth
        )

        ElevatedCard(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(maxWidth / 4)
                .padding(
                    start = 16.dp
                )
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(poster)
                    .dispatcher(Dispatchers.IO)
                    .crossfade(true)
                    .memoryCacheKey(poster)
                    .diskCacheKey(poster)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .fallback(R.drawable.poster_placeholder)
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.movie_error_placeholder)
                    .build(),
                contentDescription = stringResource(id = R.string.poster),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(id = R.drawable.poster_placeholder)
            )
        }


        IconButton(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.BottomEnd)
                .padding(0.dp, 0.dp, 16.dp, 16.dp),
            onClick = {
                toggleFavorite()
            },
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = stringResource(R.string.movie_mark_favorite),
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        CircularRatingIndicator(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 16.dp,
                    end = 16.dp
                ),
            voteAverage = voteAverage,
            backgroundColor = backgroundColor,
            fillColor = fillColor
        )

    }
}


@Composable
private fun CircularRatingIndicator(
    modifier: Modifier = Modifier,
    voteAverage: Int?,
    strokeWidth: Float = 15f,
    backgroundColor: Color,
    fillColor: Color,
) {

    val sweepAngle = ((voteAverage ?: 0) / 100f) * 360f

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(30.dp)
        ) {
            // Draw background circle
            drawCircle(
                color = backgroundColor,
                style = Stroke(width = strokeWidth)
            )

            // Draw filled arc
            withTransform({
                rotate(degrees = -90f) // Start from the top
            }) {
                drawArc(
                    color = fillColor,
                    startAngle = 0f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
            }
        }
        Text(
            fontSize = 12.sp,
            text = voteAverage?.let {
                if (it > 0) it.toString()
                else stringResource(id = R.string.not_rated)
            } ?: stringResource(id = R.string.not_rated)
        )
    }
}

@Composable
private fun Title(
    modifier: Modifier,
    title: String,
    releaseYear: String,
    highlightColor: Color,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        if (releaseYear.isNotBlank()) {
            Text(
                modifier = Modifier
                    .padding(
                        start = 2.dp
                    ),
                text = "($releaseYear)",
                color = highlightColor,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun Info(
    runTime: Int,
    releaseDate: String,
    genres: List<Genre>,
    fontSize: Int = 14,
    accentColor: Color,
    navigateToTrailer: (() -> Unit)? = null,
) {

    val runTimeHours = runTime / 60
    val runTimeMinutes = runTime % 60
    val context = LocalContext.current

    val borderColor =
        Color(ColorUtils.blendARGB(accentColor.toArgb(), Color.Black.toArgb(), 0.2f))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(accentColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(2.dp)
                .background(borderColor)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = LocalDate.parse(releaseDate).toString(),
                fontSize = fontSize.sp
            )
            if (runTime > 0) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 3.dp,
                            end = 3.dp
                        ),
                    text = "·",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
                Text(
                    fontSize = fontSize.sp,
                    text = "${runTimeHours}h ${runTimeMinutes}m "
                )
            }
            navigateToTrailer?.let {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .clickable {
                            navigateToTrailer()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(
                                end = 3.dp
                            ),
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = Icons.Filled.PlayArrow.name
                    )
                    Text(
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Medium,
                        text = stringResource(id = R.string.play_trailer)
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Medium,
                text = genres.joinToString(", ") { context.getString(it.title) }
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(2.dp)
                .background(borderColor)
        )
    }
}

@Composable
private fun Overview(
    modifier: Modifier,
    tagline: String,
    overview: String,
    accentColor: Color,
) {
    if (overview.isNotBlank()) {
        Column(
            modifier = modifier
        ) {
            if (tagline.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 6.dp
                        ),
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = accentColor,
                    text = tagline
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 6.dp
                    ),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = stringResource(id = R.string.overview)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 14.sp,
                text = overview,
                textAlign = TextAlign.Justify,
                lineHeight = 20.sp,
                style = TextStyle.Default.copy(
                    lineBreak = LineBreak.Paragraph
                )
            )
        }
    }
}

@Composable
private fun Credits(
    modifier: Modifier,
    credits: List<Movie.CastMember>,
    scrollState : LazyListState
) {
    if (credits.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            SubHeaderTitle(
                text = stringResource(id = R.string.top_billed_cast)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp
                ),
                state = scrollState
            ) {
                items(
                    items = credits,
                    key = { castMember ->
                        castMember.name
                    }
                ) { castMember ->
                    Column(
                        modifier = Modifier
                            .width(100.dp)
                    ) {
                        Column {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(castMember.profilePath)
                                    .dispatcher(Dispatchers.IO)
                                    .crossfade(true)
                                    .memoryCacheKey(castMember.profilePath)
                                    .diskCacheKey(castMember.profilePath)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .fallback(R.drawable.cast_placeholder)
                                    .placeholder(R.drawable.cast_placeholder)
                                    .error(R.drawable.cast_error_placeholder)
                                    .build(),
                                contentDescription = stringResource(id = R.string.cast_member),
                                contentScale = ContentScale.Fit,
                                placeholder = painterResource(id = R.drawable.cast_placeholder)
                            )

                            Column(
                                modifier = Modifier
                                    .padding(3.dp)
                                    .height(60.dp)
                            ) {
                                Text(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    text = castMember.name,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 12.sp
                                )
                                Text(
                                    fontSize = 11.sp,
                                    text = castMember.character,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 12.sp
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Review(
    modifier: Modifier,
    review: Movie.Review?,
    accentColor: Color,
    fontSize: Int = 14,
    highlightColor: Color,
    mediumLightColor: Color,
) {
    review?.let {
        val reviewDate = ZonedDateTime.parse(review.updatedAt, DateTimeFormatter.ISO_DATE_TIME)
        val dateFormatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)
        val formattedDate = dateFormatter.format(reviewDate)

        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(accentColor),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 12.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                shape = (CircleShape),
                                color = mediumLightColor
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        review.avatarPath?.let { avatarUrl ->
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .align(Alignment.Center),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(avatarUrl)
                                    .dispatcher(Dispatchers.IO)
                                    .crossfade(true)
                                    .memoryCacheKey(avatarUrl)
                                    .diskCacheKey(avatarUrl)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .fallback(R.drawable.cast_placeholder)
                                    .placeholder(R.drawable.cast_placeholder)
                                    .error(R.drawable.cast_placeholder)
                                    .build(),
                                contentDescription = review.author,
                                contentScale = ContentScale.FillBounds,
                                clipToBounds = true,
                                placeholder = painterResource(id = R.drawable.cast_placeholder)
                            )
                        } ?: Text(
                            modifier = Modifier
                                .align(Alignment.Center),
                            fontSize = (fontSize + 8).sp,
                            fontWeight = FontWeight.ExtraBold,
                            text = "${review.author[0]}",
                            textAlign = TextAlign.Center
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 12.dp
                            )
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = (fontSize + 2).sp,
                            text = stringResource(id = R.string.review_written_by, it.author),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            it.rating?.let { rating ->

                                val formattedRating = (rating * 10).toString()

                                Row(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(
                                            end = 4.dp
                                        )
                                        .background(
                                            color = mediumLightColor,
                                            shape = RoundedCornerShape(4.dp)
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(
                                                start = 4.dp,
                                                end = 4.dp,
                                                top = 2.dp,
                                                bottom = 2.dp
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .padding(
                                                    end = 2.dp
                                                ),
                                            imageVector = Icons.Default.Star,
                                            contentDescription = Icons.Default.Star.name
                                        )
                                        Text(
                                            fontSize = fontSize.sp,
                                            text = formattedRating
                                        )
                                        Text(
                                            fontSize = (fontSize - 2).sp,
                                            text = "%"
                                        )
                                    }
                                }
                            }
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontSize = (fontSize - 2).sp,
                                fontWeight = FontWeight.Light,
                                color = highlightColor,
                                lineHeight = 16.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                text = stringResource(
                                    id = R.string.review_author_sub,
                                    review.author,
                                    formattedDate
                                )
                            )
                        }
                    }
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = review.content,
                    maxLines = 12,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Justify,
                    fontSize = fontSize.sp,
                    lineHeight = 20.sp,
                    style = TextStyle.Default.copy(
                        lineBreak = LineBreak.Paragraph
                    )
                )
            }
        }
    }
}

@Composable
private fun Images(
    modifier: Modifier,
    images: List<String>,
    scrollState : LazyListState
) {
    if (images.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            SubHeaderTitle(
                text = stringResource(id = R.string.images)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp
                ),
                state = scrollState
            ) {
                items(
                    items = images,
                    key = { it }
                ) { imageUrl ->
                    Column(
                        modifier = Modifier
                            .width(200.dp)
                    ) {
                        Column {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrl)
                                    .dispatcher(Dispatchers.IO)
                                    .crossfade(true)
                                    .memoryCacheKey(imageUrl)
                                    .diskCacheKey(imageUrl)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .fallback(R.drawable.backdrop_placeholder)
                                    .placeholder(R.drawable.backdrop_placeholder)
                                    .error(R.drawable.backdrop_error_placeholder)
                                    .build(),
                                contentDescription = stringResource(id = R.string.images),
                                contentScale = ContentScale.Fit,
                                placeholder = painterResource(id = R.drawable.backdrop_placeholder)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Suggestions(
    modifier: Modifier,
    movieList: List<Movie.MovieItem>,
    loadMovie: (Long) -> Unit,
    title: String? = null,
    scrollState : LazyListState
) {
    if (movieList.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            SubHeaderTitle(
                text = title?.let {
                    stringResource(id = R.string.similar, title)
                } ?: stringResource(id = R.string.recommendations)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp
                ),
                state = scrollState
            ) {
                items(
                    items = movieList,
                    key = { movie ->
                        movie.id
                    }
                ) { movie ->
                    Column(
                        modifier = Modifier
                            .clickable {
                                loadMovie(movie.id)
                            }
                            .width(100.dp)
                    ) {
                        Column {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(movie.posterPath)
                                    .dispatcher(Dispatchers.IO)
                                    .crossfade(true)
                                    .memoryCacheKey(movie.posterPath)
                                    .diskCacheKey(movie.posterPath)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .fallback(R.drawable.poster_placeholder)
                                    .placeholder(R.drawable.poster_placeholder)
                                    .error(R.drawable.movie_error_placeholder)
                                    .build(),
                                contentDescription = movie.title,
                                contentScale = ContentScale.Fit,
                                placeholder = painterResource(id = R.drawable.poster_placeholder)
                            )

                            Text(
                                fontSize = 12.sp,
                                text = movie.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SubHeaderTitle(
    text: String,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 6.dp,
                start = 16.dp
            ),
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        text = text
    )
}

private enum class RatingColors(val backgroundColor: Color, val fillerColor: Color) {
    LOWER_REVIEW(Color(0xFFF0E1E1), Color(0xFFa10505)),
    BAD_REVIEW(Color(0xFFE7DFDA), Color(0xFFdb7e48)),
    OK_REVIEW(Color(0xFFE7E5D7), Color(0xFFb59a16)),
    GOOD_REVIEW(Color(0xFFE4E7D7), Color(0xFF87a80f)),
    EXCELLENT_REVIEW(Color(0xB7D5E0D3), Color(0xFF0f5c02)),
    NO_REVIEW(Color(0xFF5E5B5B), Color(0xFF5E5B5B))
}