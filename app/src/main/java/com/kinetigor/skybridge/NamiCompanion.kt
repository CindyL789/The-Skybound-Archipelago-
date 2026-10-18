package com.kinetigor.skybridge

import com.example.audio.NarratorPersona
import com.example.data.Chapter
import com.example.data.LanternMode
import com.example.data.StoryParagraph

/**
 * NamiCompanion — fail-soft wiring between the Skybound Archipelago app and KIN.
 *
 * Every call degrades gracefully: if the Skybridge backend is unreachable
 * (it is not yet live-deployed — run it locally, see SKYBRIDGE.md), the app
 * behaves exactly as before. No crash, no blocked reading flow.
 */
class NamiCompanion(
    private val skybridge: SkybridgeClient = SkybridgeClient()
) {

    // ─── 3-layer lore for a StoryParagraph ────────────────────

    /**
     * Enriches a paragraph with blueGlassSecret / amberLore / moonlightTruth.
     * Falls back to the original paragraph on any error.
     */
    suspend fun enrichParagraph(
        paragraph: StoryParagraph,
        lanternMode: LanternMode,
        chapterId: String? = null
    ): StoryParagraph = try {
        val response = skybridge.getNarrative(
            NarrativeRequest(
                vulnClass = lanternMode.name,
                chapterId = chapterId,
                loreLayer = when (lanternMode) {
                    LanternMode.BLUE_GLASS -> "blueGlassSecret"
                    LanternMode.AMBER_LAMP -> "amberLore"
                    LanternMode.MOONLIGHT -> "moonlightTruth"
                    LanternMode.STANDARD -> "blueGlassSecret"
                }
            )
        )
        paragraph.copy(
            blueGlassSecret = response.loreLayers.blueGlassSecret.ifBlank { paragraph.blueGlassSecret },
            amberLore = response.loreLayers.amberLore.ifBlank { paragraph.amberLore },
            moonlightTruth = response.loreLayers.moonlightTruth.ifBlank { paragraph.moonlightTruth }
        )
    } catch (e: Exception) {
        paragraph
    }

    // ─── Streaming Nami narration for a chapter ───────────────

    /**
     * Streams a Nami narration for the chapter text.
     * [onChunk] receives each text fragment as it arrives (type "char" events),
     * [onAnimation] receives Nami's animation cues, [onTruth] the moonlight truth reveal.
     * Returns the full narration text, or an empty string on failure (fail-soft).
     */
    suspend fun narrateChapter(
        chapter: Chapter,
        persona: NarratorPersona = NarratorPersona.LANTERNKEEPER,
        playerId: String? = null,
        onChunk: (String) -> Unit = {},
        onAnimation: (String) -> Unit = {},
        onTruth: (String) -> Unit = {}
    ): String = try {
        val combined = chapter.paragraphs.joinToString(" ") { it.text }
        val builder = StringBuilder()
        skybridge.streamNami(
            NamiRequest(
                playerMessage = combined,
                context = "Chapter ${chapter.actNumber}: ${chapter.actTitle} — ${chapter.title}",
                mode = persona.id,
                playerId = playerId
            )
        ).collect { event ->
            when (event.type) {
                "animation" -> onAnimation(event.value)
                "char" -> { builder.append(event.value); onChunk(event.value) }
                "truth" -> onTruth(event.value)
            }
        }
        builder.toString()
    } catch (e: Exception) {
        ""
    }

    // ─── Ask Nami anything ─────────────────────────────────────

    /**
     * One-shot Nami guidance. Returns null on failure (caller keeps its own fallback).
     */
    suspend fun ask(
        playerMessage: String,
        context: String,
        playerId: String? = null
    ): String? = try {
        skybridge.getNamiGuidance(
            NamiRequest(playerMessage = playerMessage, context = context, playerId = playerId)
        ).guidance
    } catch (e: Exception) {
        null
    }

    // ─── Cross-app progression sync ────────────────────────────

    /**
     * Report a completed chapter. Unlocks float in the floating-market app
     * (quest_chapter_1 -> active, dawn_dock, storm_run, maelstrom_vortex).
     * Fire-and-forget: never throws.
     */
    suspend fun onChapterComplete(playerId: String, chapter: Chapter) {
        try {
            skybridge.syncProgression(
                ProgressionSyncRequest(
                    playerId = playerId,
                    app = "story",
                    completedChapters = listOf(chapter.id.toString())
                )
            )
        } catch (e: Exception) {
            // fail-soft: progression sync is best-effort
        }
    }
}
