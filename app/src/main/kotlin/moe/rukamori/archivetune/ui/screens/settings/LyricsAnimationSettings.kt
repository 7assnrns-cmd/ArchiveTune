/*
 * ArchiveTune (2026)
 * © Rukamori — github.com/rukamori
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package moe.rukamori.archivetune.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import moe.rukamori.archivetune.LocalPlayerAwareWindowInsets
import moe.rukamori.archivetune.R
import moe.rukamori.archivetune.constants.LyricsV2BounceFactorKey
import moe.rukamori.archivetune.constants.LyricsV2FillTransitionWidthKey
import moe.rukamori.archivetune.constants.LyricsV2GlowFactorKey
import moe.rukamori.archivetune.constants.LyricsV2LrcBounceEnabledKey
import moe.rukamori.archivetune.ui.component.IconButton
import moe.rukamori.archivetune.ui.component.PreferenceGroup
import moe.rukamori.archivetune.ui.component.SliderPreference
import moe.rukamori.archivetune.ui.component.SwitchPreference
import moe.rukamori.archivetune.ui.utils.backToMain
import moe.rukamori.archivetune.utils.rememberPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LyricsAnimationSettings(navController: NavController) {
    val (bounceFactor, onBounceFactorChange) =
        rememberPreference(LyricsV2BounceFactorKey, defaultValue = 1f)
    val (glowFactor, onGlowFactorChange) =
        rememberPreference(LyricsV2GlowFactorKey, defaultValue = 1f)
    val (fillTransitionWidth, onFillTransitionWidthChange) =
        rememberPreference(LyricsV2FillTransitionWidthKey, defaultValue = 8f)
    val (lrcBounceEnabled, onLrcBounceEnabledChange) =
        rememberPreference(LyricsV2LrcBounceEnabledKey, defaultValue = true)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.lyrics_animation_style),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navController::navigateUp,
                        onLongClick = navController::backToMain,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = null,
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
            )
        },
    ) { innerPadding ->
        val topPadding = innerPadding.calculateTopPadding()

        Column(
            modifier =
                Modifier
                    .padding(top = topPadding)
                    .windowInsetsPadding(
                        LocalPlayerAwareWindowInsets.current.only(
                            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
                        ),
                    ).verticalScroll(rememberScrollState())
                    .padding(bottom = SettingsDimensions.ScreenBottomPadding),
        ) {
            PreferenceGroup(title = stringResource(R.string.animation_tuning)) {
                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.line_bounce_effect)) },
                        description = stringResource(R.string.line_bounce_effect_desc),
                        icon = { Icon(painterResource(R.drawable.animation), null) },
                        checked = lrcBounceEnabled,
                        onCheckedChange = onLrcBounceEnabledChange,
                    )
                }

                item {
                    SliderPreference(
                        title = { Text(stringResource(R.string.bounce_amplitude)) },
                        icon = { Icon(painterResource(R.drawable.animation), null) },
                        value = (bounceFactor * 100).toInt(),
                        onValueChange = { onBounceFactorChange(it / 100f) },
                    )
                }

                item {
                    SliderPreference(
                        title = { Text(stringResource(R.string.glow_intensity)) },
                        icon = { Icon(painterResource(R.drawable.lyrics), null) },
                        value = (glowFactor * 100).toInt(),
                        onValueChange = { onGlowFactorChange(it / 100f) },
                    )
                }

                item {
                    SliderPreference(
                        title = { Text(stringResource(R.string.animation_tuning)) },
                        icon = { Icon(painterResource(R.drawable.lyrics), null) },
                        value = fillTransitionWidth.toInt(),
                        onValueChange = { onFillTransitionWidthChange(it.toFloat()) },
                    )
                }
            }
        }
    }
}
