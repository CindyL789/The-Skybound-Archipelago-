package com.example.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import com.example.ai.ScenePromptsCatalog
import java.io.File
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AnnotatedTerm
import com.example.data.Chapter
import com.example.data.LanternMode
import com.example.data.StoryParagraph
import com.example.data.StoryRepository
import com.example.ui.StoryUiState
import com.example.ui.StoryViewModel
import com.example.ui.components.CourierFootnoteSheet
import com.example.ui.components.InteractiveChoiceCard
import com.example.ui.components.MiniAudioBar
import com.example.ui.components.ScaleCompass
import com.example.ui.theme.AmberLamp
import com.example.ui.theme.AmberLampSoft
import com.example.ui.theme.BlueGlass
import com.example.ui.theme.DeepNight
import com.example.ui.theme.MoonSilver
import com.example.ui.theme.NightCardBorder
import com.example.ui.theme.NightSurface
import com.example.ui.theme.NightSurfaceVariant
import com.example.ui.theme.ParchmentBg
import com.example.ui.theme.ParchmentSurface
import com.example.ui.theme.ParchmentText
import com.example.ui.theme.ParchmentTextSecondary
import com.example.ui.theme.TextMutedNight
import com.example.ui.theme.TextPrimaryNight
import com.example.ui.theme.TextSecondaryNight
import com.example.ui.theme.VermilionCourier
import com.example.ui.theme.VoidDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryReaderScreen(
  viewModel: StoryViewModel,
  uiState: StoryUiState,
  modifier: Modifier = Modifier
) {
  val chapter = viewModel.chapters.getOrElse(uiState.currentChapterId) { viewModel.chapters.first() }
  val listState = rememberLazyListState()
  val isBookmarked = uiState.bookmarks.contains(chapter.id)

  LaunchedEffect(uiState.currentChapterId) {
    listState.scrollToItem(0)
  }

  // Auto-scroll to active paragraph during speech narration
  LaunchedEffect(uiState.narratingParagraphIndex, uiState.isNarrating) {
    if (uiState.isNarrating) {
      val offset = if (chapter.imageRes != null) 2 else 1
      val targetIndex = (uiState.narratingParagraphIndex + offset).coerceAtLeast(0)
      listState.animateScrollToItem(targetIndex)
    }
  }

  val textColor = if (uiState.isParchmentMode) ParchmentText else TextPrimaryNight
  val secondaryTextColor = if (uiState.isParchmentMode) ParchmentTextSecondary else TextSecondaryNight
  val backgroundColor = if (uiState.isParchmentMode) ParchmentBg else VoidDark

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(backgroundColor)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Reader Top Header Bar with Audio Options trigger
      ReaderHeaderBar(
        chapter = chapter,
        totalChapters = viewModel.chapters.size,
        isBookmarked = isBookmarked,
        isParchment = uiState.isParchmentMode,
        isAudioActive = uiState.isAudioPlaying || uiState.isNarrating,
        onToggleBookmark = { viewModel.toggleBookmark(chapter.id) },
        onToggleParchment = { viewModel.toggleParchmentMode() },
        onOpenDirectory = { viewModel.toggleChapterDirectory(true) },
        onOpenCompass = { viewModel.toggleCompassModal(true) },
        onOpenAudioOptions = { viewModel.toggleAudioOptions(true) },
        onFontDec = { viewModel.adjustFontSize(-0.1f) },
        onFontInc = { viewModel.adjustFontSize(0.1f) }
      )

      // Main Chapter Content
      LazyColumn(
        state = listState,
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp)
      ) {
        // Act header tag & Audio Quick Listen button
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(VermilionCourier.copy(alpha = 0.2f))
                .border(1.dp, VermilionCourier.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = chapter.actTitle.uppercase(),
                color = VermilionCourier,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
              )
            }

            Text(
              text = "Book One · Chapter ${chapter.id}",
              color = secondaryTextColor,
              fontSize = 11.sp,
              fontFamily = FontFamily.Serif
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Chapter Title
          Text(
            text = chapter.title,
            color = textColor,
            fontSize = (24 * uiState.fontScale).sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            lineHeight = (30 * uiState.fontScale).sp
          )

          if (chapter.subtitle != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = chapter.subtitle,
              color = AmberLamp,
              fontSize = (14 * uiState.fontScale).sp,
              fontStyle = FontStyle.Italic,
              fontFamily = FontFamily.Serif
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Quick Action Bar: Audio & Illustration
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Read Aloud
            Row(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(if (uiState.isNarrating) BlueGlass.copy(alpha = 0.25f) else NightSurface)
                .border(
                  1.dp,
                  if (uiState.isNarrating) BlueGlass else NightCardBorder,
                  RoundedCornerShape(20.dp)
                )
                .clickable {
                  if (uiState.isNarrating && !uiState.isNarrationPaused) {
                    viewModel.pauseNarration()
                  } else if (uiState.isNarrating && uiState.isNarrationPaused) {
                    viewModel.resumeNarration()
                  } else {
                    viewModel.startNarration(chapter, 0)
                  }
                }
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("chapter_read_aloud_button"),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = if (uiState.isNarrating && !uiState.isNarrationPaused) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null,
                tint = if (uiState.isNarrating) BlueGlass else AmberLamp,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (uiState.isNarrating && !uiState.isNarrationPaused) "Pause" else if (uiState.isNarrating) "Resume" else "Listen",
                color = if (uiState.isNarrating) BlueGlass else TextPrimaryNight,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }

            // Illustrate Scene with Imagen
            Row(
              modifier = Modifier
                .weight(1.3f)
                .clip(RoundedCornerShape(20.dp))
                .background(AmberLamp.copy(alpha = 0.12f))
                .border(1.dp, AmberLamp.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                .clickable {
                  val curated = ScenePromptsCatalog.getPromptsForChapter(chapter.id).firstOrNull()
                  viewModel.openIllustrationStudio(
                    sceneTitle = "${chapter.title}: Scene Plate",
                    prompt = curated?.prompt ?: "An atmospheric painterly fantasy illustration of the Skybound Archipelago",
                    style = curated?.defaultStyle ?: com.example.ai.PainterlyStyle.PAINTERLY_OIL
                  )
                }
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("chapter_illustrate_button"),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = null,
                tint = AmberLamp,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Illustrate Scene",
                color = AmberLamp,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
        }

        // Hero Illustration (either custom user-generated or curated chapter plate)
        val customIllustration = uiState.illustrationGallery.firstOrNull { it.chapterId == chapter.id && it.isUserGenerated }
        val customBitmap = customIllustration?.localFilePath?.let { path ->
          val file = File(path)
          if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
        }

        if (customBitmap != null || chapter.imageRes != null) {
          item {
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .border(1.dp, NightCardBorder, RoundedCornerShape(16.dp))
                .testTag("chapter_hero_image"),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = NightSurface)
            ) {
              Column {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                ) {
                  if (customBitmap != null) {
                    Image(
                      bitmap = customBitmap.asImageBitmap(),
                      contentDescription = customIllustration?.title ?: chapter.title,
                      modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                      contentScale = ContentScale.Crop
                    )
                  } else if (chapter.imageRes != null) {
                    Image(
                      painter = painterResource(id = chapter.imageRes),
                      contentDescription = chapter.imageCaption ?: chapter.title,
                      modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                      contentScale = ContentScale.Crop
                    )
                  }

                  // Badge indicating source
                  if (customIllustration != null) {
                    Box(
                      modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(6.dp))
                        .background(AmberLamp)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                      Text(
                        text = "IMAGEN 3 CONJURED",
                        color = VoidDark,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold
                      )
                    }
                  }

                  // Re-illustrate chip overlay
                  Box(
                    modifier = Modifier
                      .padding(10.dp)
                      .align(Alignment.BottomEnd)
                      .clip(RoundedCornerShape(16.dp))
                      .background(VoidDark.copy(alpha = 0.8f))
                      .border(1.dp, AmberLamp.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                      .clickable {
                        val curated = ScenePromptsCatalog.getPromptsForChapter(chapter.id).firstOrNull()
                        viewModel.openIllustrationStudio(
                          sceneTitle = "${chapter.title}: Variation",
                          prompt = customIllustration?.prompt ?: curated?.prompt ?: "A painterly fantasy view of this scene",
                          style = customIllustration?.style ?: curated?.defaultStyle ?: com.example.ai.PainterlyStyle.PAINTERLY_OIL
                        )
                      }
                      .padding(horizontal = 10.dp, vertical = 5.dp)
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = AmberLamp,
                        modifier = Modifier.size(13.dp)
                      )
                      Spacer(modifier = Modifier.width(4.dp))
                      Text(
                        text = if (customIllustration != null) "Re-Conjure Plate" else "Conjure with Imagen",
                        color = AmberLamp,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                }

                val caption = customIllustration?.prompt ?: chapter.imageCaption
                if (caption != null) {
                  Text(
                    text = caption,
                    color = secondaryTextColor,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(12.dp)
                  )
                }
              }
            }
          }
        }

        // Story Paragraphs with active narration highlight
        itemsIndexed(chapter.paragraphs, key = { _, it -> it.id }) { index, paragraph ->
          val isCurrentSpoken = uiState.isNarrating && uiState.narratingParagraphIndex == index

          ParagraphView(
            paragraph = paragraph,
            fontScale = uiState.fontScale,
            lanternMode = uiState.lanternMode,
            isParchment = uiState.isParchmentMode,
            isCurrentlyNarrating = isCurrentSpoken,
            textColor = textColor,
            onTermClick = { term -> viewModel.showFootnote(term) },
            onNarrateFromHere = {
              viewModel.startNarration(chapter, index)
            }
          )
          Spacer(modifier = Modifier.height(14.dp))
        }

        // Interactive Choice at pivotal chapter endings
        if (chapter.choice != null) {
          item {
            InteractiveChoiceCard(
              choice = chapter.choice,
              selectedOptionId = viewModel.getSelectedOptionId(chapter.choice.id),
              onOptionSelected = { option ->
                viewModel.selectOption(chapter.choice.id, option)
              }
            )
          }
        }

        // Chapter Navigation Controls
        item {
          Spacer(modifier = Modifier.height(24.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedButton(
              onClick = { viewModel.prevChapter() },
              enabled = chapter.id > 0,
              modifier = Modifier.testTag("prev_chapter_button")
            ) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Previous")
            }

            Button(
              onClick = { viewModel.nextChapter() },
              enabled = chapter.id < viewModel.chapters.size - 1,
              colors = ButtonDefaults.buttonColors(
                containerColor = BlueGlass,
                contentColor = VoidDark
              ),
              modifier = Modifier.testTag("next_chapter_button")
            ) {
              Text("Next")
              Spacer(modifier = Modifier.width(6.dp))
              Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
          }
        }
      }

      // Mini Audio Player Bar (docked above bottom controls when active)
      MiniAudioBar(
        viewModel = viewModel,
        uiState = uiState,
        currentChapter = chapter,
        onOpenAudioOptions = { viewModel.toggleAudioOptions(true) }
      )
    }

    // Modal Footnote Bottom Sheet
    if (uiState.activeFootnote != null) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = 0.5f))
          .clickable { viewModel.showFootnote(null) },
        contentAlignment = Alignment.BottomCenter
      ) {
        CourierFootnoteSheet(
          term = uiState.activeFootnote,
          onDismiss = { viewModel.showFootnote(null) },
          modifier = Modifier.clickable(enabled = false) {}
        )
      }
    }

    // Chapter Table of Contents Modal
    if (uiState.showChapterDirectory) {
      ModalBottomSheet(
        onDismissRequest = { viewModel.toggleChapterDirectory(false) },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = DeepNight,
        modifier = Modifier.testTag("chapter_directory_sheet")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
          Text(
            text = "TABLE OF CONTENTS",
            color = VermilionCourier,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
          )
          Text(
            text = "The Skybound Archipelago",
            color = TextPrimaryNight,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(16.dp))

          viewModel.chapters.forEach { ch ->
            val isCurrent = ch.id == chapter.id
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (isCurrent) BlueGlass.copy(alpha = 0.15f) else Color.Transparent)
                .clickable { viewModel.selectChapter(ch.id) }
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Chapter ${ch.id}: ${ch.title}",
                  color = if (isCurrent) BlueGlass else TextPrimaryNight,
                  fontSize = 15.sp,
                  fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                  text = ch.actTitle,
                  color = TextMutedNight,
                  fontSize = 11.sp
                )
              }
              if (uiState.bookmarks.contains(ch.id)) {
                Icon(
                  imageVector = Icons.Default.Bookmark,
                  contentDescription = "Bookmarked",
                  tint = VermilionCourier,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }

    // Interactive Scale Compass Full Screen / Modal Dialog
    if (uiState.showCompassModal) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = 0.75f))
          .clickable { viewModel.toggleCompassModal(false) },
        contentAlignment = Alignment.Center
      ) {
        Card(
          modifier = Modifier
            .padding(24.dp)
            .border(1.dp, BlueGlass, RoundedCornerShape(20.dp))
            .clickable(enabled = false) {},
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = DeepNight)
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "SCALE COMPASS",
              color = VermilionCourier,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 2.sp
            )
            Text(
              text = "Salt Pier Bearing",
              color = TextPrimaryNight,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Drag needle to tune navigation currents",
              color = TextSecondaryNight,
              fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            ScaleCompass(size = 180.dp)
            Spacer(modifier = Modifier.height(20.dp))
            Button(
              onClick = { viewModel.toggleCompassModal(false) },
              colors = ButtonDefaults.buttonColors(
                containerColor = BlueGlass,
                contentColor = VoidDark
              )
            ) {
              Text("Lock Heading")
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ReaderHeaderBar(
  chapter: Chapter,
  totalChapters: Int,
  isBookmarked: Boolean,
  isParchment: Boolean,
  isAudioActive: Boolean,
  onToggleBookmark: () -> Unit,
  onToggleParchment: () -> Unit,
  onOpenDirectory: () -> Unit,
  onOpenCompass: () -> Unit,
  onOpenAudioOptions: () -> Unit,
  onFontDec: () -> Unit,
  onFontInc: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(if (isParchment) ParchmentSurface else DeepNight)
      .border(
        width = 1.dp,
        color = if (isParchment) Color(0xFFC7BCA7) else NightCardBorder
      )
      .padding(horizontal = 8.dp, vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    IconButton(
      onClick = onOpenDirectory,
      modifier = Modifier.testTag("directory_button")
    ) {
      Icon(
        imageVector = Icons.Default.List,
        contentDescription = "Chapters",
        tint = if (isParchment) ParchmentText else BlueGlass
      )
    }

    // Mini Compass Icon Trigger
    ScaleCompass(
      size = 32.dp,
      onClick = onOpenCompass
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
      // Audio Options Trigger
      IconButton(
        onClick = onOpenAudioOptions,
        modifier = Modifier.testTag("header_audio_options_button")
      ) {
        Icon(
          imageVector = if (isAudioActive) Icons.Default.GraphicEq else Icons.Default.Headphones,
          contentDescription = "Audio Options",
          tint = if (isAudioActive) AmberLamp else if (isParchment) ParchmentText else TextSecondaryNight,
          modifier = Modifier.size(20.dp)
        )
      }

      // Font Adjust Buttons
      IconButton(onClick = onFontDec, modifier = Modifier.size(32.dp)) {
        Text("A-", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isParchment) ParchmentText else TextSecondaryNight)
      }
      IconButton(onClick = onFontInc, modifier = Modifier.size(32.dp)) {
        Text("A+", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isParchment) ParchmentText else TextSecondaryNight)
      }

      // Theme toggle (Parchment vs Obsidian Night)
      IconButton(
        onClick = onToggleParchment,
        modifier = Modifier.testTag("theme_toggle_button")
      ) {
        Icon(
          imageVector = if (isParchment) Icons.Default.NightlightRound else Icons.Default.Lightbulb,
          contentDescription = "Toggle Reading Atmosphere",
          tint = if (isParchment) ParchmentText else AmberLamp,
          modifier = Modifier.size(18.dp)
        )
      }

      // Bookmark
      IconButton(
        onClick = onToggleBookmark,
        modifier = Modifier.testTag("bookmark_button")
      ) {
        Icon(
          imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
          contentDescription = "Bookmark",
          tint = if (isBookmarked) VermilionCourier else if (isParchment) ParchmentText else TextMutedNight,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}

@Composable
private fun ParagraphView(
  paragraph: StoryParagraph,
  fontScale: Float,
  lanternMode: LanternMode,
  isParchment: Boolean,
  isCurrentlyNarrating: Boolean,
  textColor: Color,
  onTermClick: (AnnotatedTerm) -> Unit,
  onNarrateFromHere: () -> Unit
) {
  val backgroundBrush = if (isCurrentlyNarrating) {
    Brush.horizontalGradient(
      listOf(BlueGlass.copy(alpha = 0.18f), AmberLamp.copy(alpha = 0.08f), Color.Transparent)
    )
  } else {
    Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(backgroundBrush)
      .then(
        if (isCurrentlyNarrating) {
          Modifier
            .border(
              width = 1.dp,
              color = BlueGlass.copy(alpha = 0.5f),
              shape = RoundedCornerShape(8.dp)
            )
            .padding(start = 12.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
        } else {
          Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        }
      )
  ) {
    // If narrating this paragraph, show audio indicator
    if (isCurrentlyNarrating) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.GraphicEq,
          contentDescription = "Now Speaking",
          tint = BlueGlass,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "SPEAKING PARAGRAPH",
          color = BlueGlass,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }
    }

    // Render text with clickable keyword highlights
    val annotatedString = buildAnnotatedString {
      val text = paragraph.text

      val matches = mutableListOf<Pair<IntRange, AnnotatedTerm>>()
      StoryRepository.glossaryTerms.forEach { term ->
        var idx = text.indexOf(term.term, ignoreCase = true)
        while (idx >= 0) {
          matches.add(Pair(idx until (idx + term.term.length), term))
          idx = text.indexOf(term.term, idx + term.term.length, ignoreCase = true)
        }
      }

      val sortedMatches = matches.sortedBy { it.first.first }
      var curPos = 0

      for ((range, term) in sortedMatches) {
        if (range.first >= curPos) {
          append(text.substring(curPos, range.first))
          pushStringAnnotation(tag = "TERM", annotation = term.term)
          withStyle(
            style = SpanStyle(
              color = if (isParchment) Color(0xFF8B263E) else BlueGlass,
              fontWeight = FontWeight.SemiBold,
              fontStyle = FontStyle.Normal
            )
          ) {
            append(text.substring(range))
          }
          pop()
          curPos = range.last + 1
        }
      }
      if (curPos < text.length) {
        append(text.substring(curPos))
      }
    }

    Text(
      text = annotatedString,
      color = if (isCurrentlyNarrating) (if (isParchment) Color(0xFF1E293B) else Color.White) else textColor,
      fontSize = (16 * fontScale).sp,
      fontFamily = FontFamily.Serif,
      lineHeight = (25 * fontScale).sp,
      modifier = Modifier.clickable {
        val match = StoryRepository.glossaryTerms.find { paragraph.text.contains(it.term, ignoreCase = true) }
        if (match != null) {
          onTermClick(match)
        }
      }
    )

    // Revealed secret notes based on active Lantern Mode!
    AnimatedVisibility(
      visible = lanternMode == LanternMode.BLUE_GLASS && paragraph.blueGlassSecret != null,
      enter = fadeIn() + expandVertically()
    ) {
      if (paragraph.blueGlassSecret != null) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .border(1.dp, BlueGlass.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(containerColor = BlueGlass.copy(alpha = 0.12f))
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Navigation,
              contentDescription = null,
              tint = BlueGlass,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = paragraph.blueGlassSecret,
              color = BlueGlass,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }

    AnimatedVisibility(
      visible = lanternMode == LanternMode.AMBER_LAMP && paragraph.amberLore != null,
      enter = fadeIn() + expandVertically()
    ) {
      if (paragraph.amberLore != null) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .border(1.dp, AmberLamp.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(containerColor = AmberLamp.copy(alpha = 0.12f))
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Lightbulb,
              contentDescription = null,
              tint = AmberLamp,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = paragraph.amberLore,
              color = AmberLamp,
              fontSize = 12.sp,
              fontStyle = FontStyle.Italic
            )
          }
        }
      }
    }

    AnimatedVisibility(
      visible = lanternMode == LanternMode.MOONLIGHT && paragraph.moonlightTruth != null,
      enter = fadeIn() + expandVertically()
    ) {
      if (paragraph.moonlightTruth != null) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .border(1.dp, MoonSilver.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0x22CFE2FE))
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.NightlightRound,
              contentDescription = null,
              tint = MoonSilver,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = paragraph.moonlightTruth,
              color = MoonSilver,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }
  }
}
