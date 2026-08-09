/*
 * ArchiveTune (2026)
 * © Rukamori — github.com/rukamori
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package moe.rukamori.archivetune.ui.screens

import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import moe.rukamori.archivetune.BuildConfig
import moe.rukamori.archivetune.constants.UpdateChannel
import moe.rukamori.archivetune.defaultUpdateChannel
import moe.rukamori.archivetune.musicrecognition.MusicRecognitionRoute
import moe.rukamori.archivetune.musicrecognition.MusicRecognitionDetailsRoute
import moe.rukamori.archivetune.ui.screens.BrowseScreen
import moe.rukamori.archivetune.ui.screens.artist.ArtistAlbumsScreen
import moe.rukamori.archivetune.ui.screens.artist.ArtistItemsScreen
import moe.rukamori.archivetune.ui.screens.artist.ArtistScreen
import moe.rukamori.archivetune.ui.screens.artist.ArtistSongsScreen
import moe.rukamori.archivetune.ui.screens.library.LibraryScreen
import moe.rukamori.archivetune.ui.screens.library.LocalSongScreen
import moe.rukamori.archivetune.ui.screens.musicrecognition.MusicRecognitionScreen
import moe.rukamori.archivetune.ui.screens.musicrecognition.MusicRecognitionDetailsScreen
import moe.rukamori.archivetune.ui.screens.playlist.AutoPlaylistScreen
import moe.rukamori.archivetune.ui.screens.playlist.CachePlaylistScreen
import moe.rukamori.archivetune.ui.screens.playlist.LocalPlaylistScreen
import moe.rukamori.archivetune.ui.screens.playlist.OnlinePlaylistScreen
import moe.rukamori.archivetune.ui.screens.playlist.SpotifyPlaylistScreen
import moe.rukamori.archivetune.ui.screens.playlist.TopPlaylistScreen
import moe.rukamori.archivetune.ui.screens.search.OnlineSearchResult
import moe.rukamori.archivetune.ui.screens.search.OnlineSearchResultArgument
import moe.rukamori.archivetune.ui.screens.search.OnlineSearchResultRoute
import moe.rukamori.archivetune.ui.screens.search.OnlineSearchResultRoutePrefix
import moe.rukamori.archivetune.ui.screens.search.SearchScreen
import moe.rukamori.archivetune.ui.screens.settings.AboutScreen
import moe.rukamori.archivetune.ui.screens.settings.AccountSettings
import moe.rukamori.archivetune.ui.screens.settings.AiIntegrationSettings
import moe.rukamori.archivetune.ui.screens.settings.AodCustomizedScreen
import moe.rukamori.archivetune.ui.screens.settings.AppearanceSettings
import moe.rukamori.archivetune.ui.screens.settings.BackupAndRestore
import moe.rukamori.archivetune.ui.screens.settings.ChangelogScreen
import moe.rukamori.archivetune.ui.screens.settings.ChiperSettings
import moe.rukamori.archivetune.ui.screens.settings.ContentSettings
import moe.rukamori.archivetune.ui.screens.settings.CustomizeBackground
import moe.rukamori.archivetune.ui.screens.settings.DebugSettings
import moe.rukamori.archivetune.ui.screens.settings.DiscordSettings
import moe.rukamori.archivetune.ui.screens.settings.HiddenPlaylistsScreen
import moe.rukamori.archivetune.ui.screens.settings.IconScreen
import moe.rukamori.archivetune.ui.screens.settings.IntegrationScreen
import moe.rukamori.archivetune.ui.screens.settings.InternetSettings
import moe.rukamori.archivetune.ui.screens.settings.LastFMSettings
import moe.rukamori.archivetune.ui.screens.settings.LogcatScreen
import moe.rukamori.archivetune.ui.screens.settings.LyricsAnimationSettings
import moe.rukamori.archivetune.ui.screens.settings.LyricsSettings
import moe.rukamori.archivetune.ui.screens.settings.MusicTogetherScreen
import moe.rukamori.archivetune.ui.screens.settings.PO_TOKEN_ROUTE
import moe.rukamori.archivetune.ui.screens.settings.PalettePickerScreen
import moe.rukamori.archivetune.ui.screens.settings.PlayerSettings
import moe.rukamori.archivetune.ui.screens.settings.PoTokenScreen
import moe.rukamori.archivetune.ui.screens.settings.PrivacySettings
import moe.rukamori.archivetune.ui.screens.settings.SettingsScreen
import moe.rukamori.archivetune.ui.screens.settings.StorageSettings
import moe.rukamori.archivetune.ui.screens.settings.ThemeCreatorScreen
import moe.rukamori.archivetune.ui.screens.settings.UpdateScreen
import moe.rukamori.archivetune.viewmodels.OnlineSearchSort

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.navigationBuilder(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    latestVersionName: () -> String,
    disableAnimations: Boolean = false,
    onClearUpdateBadge: () -> Unit = {},
    homeScrollConnection: NestedScrollConnection? = null,
    searchScrollConnection: NestedScrollConnection? = null,
    onlineSearchSort: OnlineSearchSort = OnlineSearchSort.DEFAULT,
) {
    val enterAnim = if (disableAnimations) null else slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
    val exitAnim = if (disableAnimations) null else slideOutHorizontally(targetOffsetX = { -it / 3 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
    val popEnterAnim = if (disableAnimations) null else slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
    val popExitAnim = if (disableAnimations) null else slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))

    composable(
        route = Screens.Home.route,
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        HomeScreen(navController, headerScrollConnection = homeScrollConnection)
    }
    composable(
        route = Screens.Library.route,
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        LibraryScreen(navController)
    }
    composable(
        route = Screens.Search.route,
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        SearchScreen(
            navController = navController,
            onSearchClick = {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("openSearch", true)
            },
            headerScrollConnection = searchScrollConnection,
        )
    }
    composable(
        route = "local_songs",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        LocalSongScreen(navController)
    }
    composable(
        route = "history",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        HistoryScreen(navController)
    }
    composable(
        route = "stats",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        StatsScreen(navController)
    }
    composable(
        route = "news",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        NewsScreen(navController)
    }
    composable(
        route = "view_news/{newsId}",
        arguments =
            listOf(
                navArgument("newsId") { type = NavType.StringType },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        ViewNewsScreen(navController)
    }
    composable(
        route = "year_in_music?year={year}",
        arguments =
            listOf(
                navArgument("year") {
                    type = NavType.IntType
                    defaultValue = -1
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) { backStackEntry ->
        val selectedYear = backStackEntry.arguments?.getInt("year")?.takeIf { it > 0 }
        YearInMusicScreen(
            navController = navController,
            initialYear = selectedYear,
        )
    }
    composable(
        route = MusicRecognitionRoute,
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        MusicRecognitionScreen(navController)
    }
    composable(
        route = MusicRecognitionDetailsRoute,
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) { backStackEntry ->
        val encodedTrack = backStackEntry.arguments?.getString("encodedTrack").orEmpty()
        MusicRecognitionDetailsScreen(navController, encodedTrack)
    }
    composable(
        route = Screens.MoodAndGenres.route,
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        MoodAndGenresScreen(navController)
    }
    composable(
        route = "account",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        AccountScreen(navController, scrollBehavior)
    }
    composable(
        route = "new_release",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        NewReleaseScreen(navController, scrollBehavior)
    }
    composable(
        route = "charts_screen",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        ChartsScreen(navController)
    }
    composable(
        route = "browse/{browseId}",
        arguments =
            listOf(
                navArgument("browseId") {
                    type = NavType.StringType
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        BrowseScreen(
            navController,
            scrollBehavior,
            it.arguments?.getString("browseId"),
        )
    }
    composable(
        route = OnlineSearchResultRoute,
        arguments =
            listOf(
                navArgument(OnlineSearchResultArgument) {
                    type = NavType.StringType
                },
            ),
        enterTransition = {
            if (disableAnimations) {
                fadeIn(tween(0))
            } else {
                fadeIn(tween(250))
            }
        },
        exitTransition = {
            if (disableAnimations) {
                fadeOut(tween(0))
            } else if (targetState.destination.route?.startsWith(OnlineSearchResultRoutePrefix) == true) {
                fadeOut(tween(200))
            } else {
                fadeOut(tween(200)) + slideOutHorizontally { -it / 2 }
            }
        },
        popEnterTransition = {
            if (disableAnimations) {
                fadeIn(tween(0))
            } else if (initialState.destination.route?.startsWith(OnlineSearchResultRoutePrefix) == true) {
                fadeIn(tween(250))
            } else {
                fadeIn(tween(250)) + slideInHorizontally { -it / 2 }
            }
        },
        popExitTransition = {
            if (disableAnimations) {
                fadeOut(tween(0))
            } else {
                fadeOut(tween(200))
            }
        },
    ) {
        OnlineSearchResult(
            navController = navController,
            searchSort = onlineSearchSort,
        )
    }
    composable(
        route = "album/{albumId}",
        arguments =
            listOf(
                navArgument("albumId") {
                    type = NavType.StringType
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        AlbumScreen(navController, scrollBehavior)
    }
    composable(
        route = "artist/{artistId}",
        arguments =
            listOf(
                navArgument("artistId") {
                    type = NavType.StringType
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        ArtistScreen(navController, scrollBehavior)
    }
    composable(
        route = "artist/{artistId}/songs",
        arguments =
            listOf(
                navArgument("artistId") {
                    type = NavType.StringType
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        ArtistSongsScreen(navController, scrollBehavior)
    }
    composable(
        route = "artist/{artistId}/albums",
        arguments =
            listOf(
                navArgument("artistId") {
                    type = NavType.StringType
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        ArtistAlbumsScreen(navController, scrollBehavior)
    }
    composable(
        route = "artist/{artistId}/items?browseId={browseId}&params={params}",
        arguments =
            listOf(
                navArgument("artistId") {
                    type = NavType.StringType
                },
                navArgument("browseId") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("params") {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        ArtistItemsScreen(navController, scrollBehavior)
    }
    composable(
        route = "online_playlist/{playlistId}",
        arguments =
            listOf(
                navArgument("playlistId") {
                    type = NavType.StringType
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        OnlinePlaylistScreen(navController, scrollBehavior)
    }
    composable(
        route = "local_playlist/{playlistId}",
        arguments =
            listOf(
                navArgument("playlistId") {
                    type = NavType.StringType
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        LocalPlaylistScreen(navController, scrollBehavior)
    }
    composable(
        route = "spotify_playlist/{playlistId}",
        arguments =
            listOf(
                navArgument("playlistId") {
                    type = NavType.StringType
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        SpotifyPlaylistScreen(navController, scrollBehavior)
    }
    composable(
        route = "auto_playlist/{playlist}?tab={tab}",
        arguments =
            listOf(
                navArgument("playlist") {
                    type = NavType.StringType
                },
                navArgument("tab") {
                    type = NavType.StringType
                    defaultValue = "downloaded"
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        AutoPlaylistScreen(navController, scrollBehavior)
    }
    composable(
        route = "cache_playlist/{playlist}",
        arguments =
            listOf(
                navArgument("playlist") {
                    type = NavType.StringType
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        CachePlaylistScreen(navController, scrollBehavior)
    }
    composable(
        route = "top_playlist/{top}",
        arguments =
            listOf(
                navArgument("top") {
                    type = NavType.StringType
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        TopPlaylistScreen(navController, scrollBehavior)
    }
    composable(
        route = "youtube_browse/{browseId}?params={params}",
        arguments =
            listOf(
                navArgument("browseId") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("params") {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        YouTubeBrowseScreen(navController)
    }
    composable(
        route = "settings",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        SettingsScreen(navController, latestVersionName())
    }
    composable(
        route = "settings/account",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        AccountSettings(navController, latestVersionName())
    }
    composable(
        route = "settings/hidden_playlists",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        HiddenPlaylistsScreen(navController)
    }
    composable(
        route = "settings/appearance",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        AppearanceSettings(navController)
    }
    composable(
        route = "settings/appearance/icon",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        IconScreen(navController)
    }
    composable(
        route = "settings/appearance/aod_customized",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        AodCustomizedScreen(navController)
    }
    composable(
        route = "settings/appearance/palette_picker",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        PalettePickerScreen(navController)
    }
    composable(
        route = "settings/appearance/lyrics_animations",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        LyricsAnimationSettings(navController)
    }
    composable(
        route = "settings/appearance/theme_creator",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        ThemeCreatorScreen(navController)
    }
    composable(
        route = "settings/content",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        ContentSettings(navController)
    }
    composable(
        route = "settings/lyrics",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        LyricsSettings(navController)
    }
    composable(
        route = "settings/internet",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        InternetSettings(navController)
    }
    composable(
        route = "settings/player",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        PlayerSettings(navController)
    }
    composable(
        route = "settings/player/chiper",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        ChiperSettings(navController)
    }
    composable(
        route = "settings/storage",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        StorageSettings(navController)
    }
    composable(
        route = "settings/privacy",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        PrivacySettings(navController)
    }
    composable(
        route = "settings/backup_restore",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        BackupAndRestore(navController)
    }
    composable(
        route = "settings/discord",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        DiscordSettings(navController)
    }
    composable(
        route = "settings/integration",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        IntegrationScreen(navController)
    }
    composable(
        route = "settings/ai_integration",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        AiIntegrationSettings(navController)
    }
    composable(
        route = "settings/music_together",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        MusicTogetherScreen(navController)
    }
    composable(
        route = "settings/lastfm",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        LastFMSettings(navController)
    }
    composable(
        route = "settings/discord/experimental",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        moe.rukamori.archivetune.ui.screens.settings
            .DiscordExperimental(navController)
    }
    composable(
        route = "settings/misc",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        DebugSettings(navController)
    }
    composable(
        route = "settings/logcat",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        LogcatScreen(navController)
    }
    if (BuildConfig.UPDATER_AVAILABLE) {
        composable(
            route = "settings/update",
            enterTransition = { enterAnim },
            exitTransition = { exitAnim },
            popEnterTransition = { popEnterAnim },
            popExitTransition = { popExitAnim },
        ) {
            UpdateScreen(navController, onUpToDate = onClearUpdateBadge)
        }
    }
    composable(
        route = "settings/changelog?channel={channel}",
        arguments =
            listOf(
                navArgument("channel") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) { backStackEntry ->
        val channelName = backStackEntry.arguments?.getString("channel")
        val channel = UpdateChannel.fromStoredName(channelName, defaultUpdateChannel)
        ChangelogScreen(navController, channel = channel)
    }
    composable(
        route = "settings/about",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        AboutScreen(navController)
    }
    composable(
        route = PO_TOKEN_ROUTE,
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        PoTokenScreen(navController)
    }
    composable(
        route = "customize_background",
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) {
        CustomizeBackground(navController)
    }
    composable(
        route = "$LOGIN_ROUTE?$LOGIN_URL_ARGUMENT={$LOGIN_URL_ARGUMENT}",
        arguments =
            listOf(
                navArgument(LOGIN_URL_ARGUMENT) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        enterTransition = { enterAnim },
        exitTransition = { exitAnim },
        popEnterTransition = { popEnterAnim },
        popExitTransition = { popExitAnim },
    ) { backStackEntry ->
        LoginScreen(
            navController,
            startUrl = backStackEntry.arguments?.getString(LOGIN_URL_ARGUMENT)?.let(Uri::decode),
        )
    }
}
