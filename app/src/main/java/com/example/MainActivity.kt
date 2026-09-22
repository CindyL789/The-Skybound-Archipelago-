package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NavigationTab
import com.example.ui.StoryViewModel
import com.example.ui.components.AudioOptionsSheet
import com.example.ui.components.IllustrationDetailDialog
import com.example.ui.components.IllustrationStudioSheet
import com.example.ui.components.LanternModeBar
import com.example.ui.components.VeoStudioDialog
import com.example.ui.components.VeoVideoPlayerDialog
import com.example.ui.screens.ArchipelagoMapScreen
import com.example.ui.screens.CharacterGeneratorScreen
import com.example.ui.screens.CodexScreen
import com.example.ui.screens.CourierSatchelScreen
import com.example.ui.screens.IllustrationAtelierScreen
import com.example.ui.screens.StoryReaderScreen
import com.example.ui.theme.BlueGlass
import com.example.ui.theme.DeepNight
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NightCardBorder
import com.example.ui.theme.TextMutedNight
import com.example.ui.theme.VoidDark

class MainActivity : ComponentActivity() {
  private val viewModel: StoryViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val uiState by viewModel.uiState.collectAsState()
      val currentChapter = viewModel.chapters.getOrElse(uiState.currentChapterId) { viewModel.chapters.first() }

      MyApplicationTheme(isParchment = uiState.isParchmentMode) {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          contentWindowInsets = WindowInsets(0, 0, 0, 0),
          bottomBar = {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
              // Show Lantern Mode Controls only on the Story Reader
              if (uiState.activeTab == NavigationTab.READER) {
                LanternModeBar(
                  activeMode = uiState.lanternMode,
                  onModeSelected = { viewModel.setLanternMode(it) },
                  isAudioPlaying = uiState.isAudioPlaying,
                  isNarrating = uiState.isNarrating,
                  onToggleAudio = { viewModel.toggleAudio() },
                  onOpenAudioOptions = { viewModel.toggleAudioOptions(true) }
                )
              }

              // M3 Navigation Bar with proper insets
              StoryNavigationBar(
                activeTab = uiState.activeTab,
                onTabSelected = { viewModel.setTab(it) }
              )
            }
          }
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          ) {
            when (uiState.activeTab) {
              NavigationTab.READER -> StoryReaderScreen(
                viewModel = viewModel,
                uiState = uiState
              )
              NavigationTab.CHARACTER -> CharacterGeneratorScreen(
                viewModel = viewModel,
                uiState = uiState
              )
              NavigationTab.MAP -> ArchipelagoMapScreen(
                viewModel = viewModel
              )
              NavigationTab.ATELIER -> IllustrationAtelierScreen(
                viewModel = viewModel,
                uiState = uiState
              )
              NavigationTab.SATCHEL -> CourierSatchelScreen(
                viewModel = viewModel,
                uiState = uiState
              )
              NavigationTab.CODEX -> CodexScreen()
            }

            // Global Audio Options Sheet
            if (uiState.showAudioOptions) {
              AudioOptionsSheet(
                viewModel = viewModel,
                uiState = uiState,
                currentChapter = currentChapter,
                onDismiss = { viewModel.toggleAudioOptions(false) }
              )
            }

            // Imagen Illustration Studio Sheet
            if (uiState.showIllustrationStudio) {
              IllustrationStudioSheet(
                viewModel = viewModel,
                uiState = uiState,
                onDismiss = { viewModel.closeIllustrationStudio() }
              )
            }

            // Illustration Detail Dialog
            uiState.activeIllustrationDetail?.let { detail ->
              IllustrationDetailDialog(
                illustration = detail,
                viewModel = viewModel,
                onDismiss = { viewModel.closeIllustrationDetail() }
              )
            }

            // Veo Cinema Studio Dialog
            if (uiState.showVeoStudioDialog) {
              VeoStudioDialog(
                viewModel = viewModel,
                uiState = uiState,
                onDismiss = { viewModel.closeVeoStudio() }
              )
            }

            // Veo Video Player Dialog
            uiState.activeViewingVideo?.let { video ->
              VeoVideoPlayerDialog(
                video = video,
                onDismiss = { viewModel.setActiveViewingVideo(null) },
                onDelete = { viewModel.deleteVeoVideo(video.id) }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun StoryNavigationBar(
  activeTab: NavigationTab,
  onTabSelected: (NavigationTab) -> Unit,
  modifier: Modifier = Modifier
) {
  val tabs = listOf(
    Pair(NavigationTab.READER, Icons.Default.AutoStories),
    Pair(NavigationTab.CHARACTER, Icons.Default.Person),
    Pair(NavigationTab.MAP, Icons.Default.Map),
    Pair(NavigationTab.ATELIER, Icons.Default.Palette),
    Pair(NavigationTab.SATCHEL, Icons.Default.Work),
    Pair(NavigationTab.CODEX, Icons.Default.MenuBook)
  )

  NavigationBar(
    modifier = modifier
      .fillMaxWidth()
      .border(1.dp, NightCardBorder),
    containerColor = DeepNight,
    contentColor = BlueGlass
  ) {
    tabs.forEach { (tab, icon) ->
      val isSelected = activeTab == tab
      NavigationBarItem(
        selected = isSelected,
        onClick = { onTabSelected(tab) },
        icon = {
          Icon(
            imageVector = icon,
            contentDescription = tab.label,
            modifier = Modifier.size(20.dp)
          )
        },
        label = {
          Text(
            text = tab.label,
            fontSize = 11.sp
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = VoidDark,
          selectedTextColor = BlueGlass,
          indicatorColor = BlueGlass,
          unselectedIconColor = TextMutedNight,
          unselectedTextColor = TextMutedNight
        ),
        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
      )
    }
  }
}
