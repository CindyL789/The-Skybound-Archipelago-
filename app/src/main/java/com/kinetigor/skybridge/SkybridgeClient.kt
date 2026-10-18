package com.kinetigor.skybridge

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

// ═══════════════════════════════════════════════════════════
//  Skybridge v2 — Frontier SDK for Story App (Android / Kotlin)
//  Streaming Nami · Adaptive Quests · Voice I/O · Cross-App Sync
//  Offline DPO Cache · DPO Provenance · Difficulty Adaptation
// ═══════════════════════════════════════════════════════════

// ─── Data Classes ───────────────────────────────────────────

@Serializable
data class LoreLayers(val blueGlassSecret: String, val amberLore: String, val moonlightTruth: String)

@Serializable
data class NarrativeRequest(val vulnClass: String, val chapterId: String? = null, val playerChoice: String? = null, val loreLayer: String = "blueGlassSecret")

@Serializable
data class NarrativeResponse(val prose: String, val choices: List<String>, val loreLayers: LoreLayers)

@Serializable
data class NamiRequest(val playerMessage: String, val context: String, val mode: String = "story", val playerId: String? = null, val difficulty: String = "adaptive")

@Serializable
data class NamiResponse(val guidance: String, val animation: String, val moonlightTruth: String)

// Streaming Nami events
@Serializable
data class NamiStreamEvent(val type: String, val value: String, val index: Int? = null)

@Serializable
data class DPOMetadata(val vulnClass: String, val source: String, val chapterId: String? = null, val questId: String? = null, val playerId: String? = null, val difficulty: String = "adaptive", val sessionDuration: Int? = null)

@Serializable
data class DPOProvenance(val playerId: String, val consent: Boolean = true, val timestamp: String = "", val signature: String = "", val appVersion: String = "", val deviceId: String = "")

@Serializable
data class DPOPair(val prompt: String, val chosen: String, val rejected: String, val metadata: DPOMetadata, val provenance: DPOProvenance? = null)

@Serializable
data class DPOCaptureResponse(val captured: Boolean, val pairId: String? = null, val totalPairs: Int, val retrainThreshold: Int, val retrainReady: Boolean, val provenanceSigned: Boolean? = null)

@Serializable
data class DPOBatch(val pairs: List<DPOPair>)

@Serializable
data class QuestRequest(val vulnClass: String)

@Serializable
data class AdaptiveQuestRequest(val vulnClass: String, val playerId: String, val playerSkill: Int = 1, val biome: String? = null, val difficulty: String = "adaptive", val seed: String? = null)

@Serializable
data class NPC(val name: String, val role: String, val dialogue: String)

@Serializable
data class Economy(val currency: String, val baseReward: Int, val bonusLoot: List<String>, val difficultyMultiplier: Double? = null)

@Serializable
data class QuestResponse(val vulnClass: String, val biome: String, val district: String, val npcs: List<NPC>, val gear: List<String>, val skills: List<String>, val economy: Economy)

@Serializable
data class AdaptiveQuestResponse(val vulnClass: String, val biome: String, val district: String, val difficulty: String, val playerSkill: Int, val npcs: List<NPC>, val gear: List<String>, val skills: List<String>, val economy: Economy, val hints: Int, val seed: String, val adaptive: Boolean)

@Serializable
data class ArtRequest(val scene: String, val mood: String? = null, val style: String = "skybound_archipelago_watercolor")

@Serializable
data class ArtResponse(val prompt: String, val negativePrompt: String, val aspectRatio: String)

@Serializable
data class NarrationRequest(val text: String, val voice: String = "calm_narrator", val speed: Double = 1.0)

@Serializable
data class NarrationResponse(val script: String, val voiceDirections: String, val estimatedDuration: Double)

@Serializable
data class VoiceSTTRequest(val audioBase64: String, val language: String = "en", val playerId: String? = null)

@Serializable
data class VoiceSTTResponse(val transcript: String, val confidence: Double, val language: String, val readyForNami: Boolean)

@Serializable
data class VoiceTTSRequest(val text: String, val voice: String = "nami_gentle", val speed: Double = 0.95, val emotion: String = "calm")

@Serializable
data class VoiceTTSResponse(val audioBase64: String, val format: String, val voice: String, val emotion: String, val speed: Double, val duration: Double)

@Serializable
data class HealthResponse(val status: String, val version: String, val dpoPairs: Int, val retrainThreshold: Int, val retrainReady: Boolean, val kinModel: String, val dpoDataset: String, val features: List<String>)

@Serializable
data class MappingEntry(val vulnClass: String, val storyTitle: String, val questBiome: String)

@Serializable
data class ProgressionSyncRequest(val playerId: String, val app: String, val completedChapters: List<String> = emptyList(), val completedQuests: List<String> = emptyList(), val skillsUnlocked: List<String> = emptyList(), val dpoContributed: Int = 0, val achievements: List<String> = emptyList())

@Serializable
data class ProgressionResponse(val synced: Boolean, val playerId: String, val progress: JsonObject, val newUnlocks: List<String>)

@Serializable
data class DifficultyRequest(val playerId: String, val correctChoices: Int = 0, val totalChoices: Int = 0, val averageResponseTime: Int? = null)

@Serializable
data class DifficultyResponse(val playerId: String, val level: String, val accuracy: Double, val totalChoices: Int, val leveledUp: Boolean)

// ─── Offline DPO Cache ─────────────────────────────────────

class DPOCache {
    private val cache = mutableListOf<DPOPair>()
    private val maxSize = 500

    fun add(pair: DPOPair) {
        if (cache.size >= maxSize) cache.removeAt(0)
        cache.add(pair)
    }

    fun flush(): List<DPOPair> {
        val batch = cache.toList()
        cache.clear()
        return batch
    }

    fun size(): Int = cache.size
    fun isEmpty(): Boolean = cache.isEmpty()
}

// ─── Skybridge Client v2 ───────────────────────────────────

class SkybridgeClient(
    private val baseUrl: String = "https://kinetigor--skybridge-web.modal.run",
    private val appVersion: String = "1.0.0",
    private val deviceId: String = ""
) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; prettyPrint = false })
        }
    }

    val dpoCache = DPOCache()

    // ─── Core Endpoints ──────────────────────────────────

    suspend fun getHealth(): HealthResponse =
        client.get("$baseUrl/health").body()

    suspend fun getNarrative(req: NarrativeRequest): NarrativeResponse =
        client.post("$baseUrl/narrative") { contentType(ContentType.Application.Json); setBody(req) }.body()

    suspend fun getNamiGuidance(req: NamiRequest): NamiResponse =
        client.post("$baseUrl/nami") { contentType(ContentType.Application.Json); setBody(req) }.body()

    // ─── Streaming Nami (SSE) ────────────────────────────

    /**
     * Streams Nami's guidance character-by-character via SSE.
     * Yields NamiStreamEvent objects: type = "animation" | "char" | "truth" | "done"
     *
     * Usage:
     *   skybridge.streamNami(NamiRequest(...)).collect { event ->
     *       when (event.type) {
     *           "animation" -> updateNamiAnimation(event.value)
     *           "char" -> appendCharToSpeechBubble(event.value)
     *           "truth" -> revealMoonlightTruth(event.value)
     *           "done" -> finishNamiResponse()
     *       }
     *   }
     */
    fun streamNami(req: NamiRequest): Flow<NamiStreamEvent> = flow {
        val response = client.post("$baseUrl/nami/stream") {
            contentType(ContentType.Application.Json)
            setBody(req)
            accept(ContentType.Text.EventStream)
        }
        val body = response.bodyAsText()
        // Parse SSE events
        body.split("data: ").drop(1).forEach { eventData ->
            val json = eventData.substringBefore("\n\n").trim()
            if (json.isNotEmpty()) {
                val obj = Json.decodeFromString<JsonObject>(json)
                emit(NamiStreamEvent(
                    type = obj["type"]?.jsonPrimitive?.content ?: "",
                    value = obj["value"]?.jsonPrimitive?.content ?: "",
                    index = obj["index"]?.jsonPrimitive?.content?.toIntOrNull()
                ))
            }
        }
    }

    // ─── DPO with Provenance ─────────────────────────────

    suspend fun captureDPO(pair: DPOPair): DPOCaptureResponse {
        val signedPair = if (pair.provenance == null && pair.metadata.playerId != null) {
            pair.copy(provenance = DPOProvenance(
                playerId = pair.metadata.playerId,
                appVersion = appVersion,
                deviceId = deviceId
            ))
        } else pair
        return client.post("$baseUrl/dpo") { contentType(ContentType.Application.Json); setBody(signedPair) }.body()
    }

    /**
     * Cache a DPO pair offline. Flush when network is available.
     * The batch is synced with provenance signatures.
     */
    fun cacheDPO(pair: DPOPair) {
        val signedPair = if (pair.provenance == null && pair.metadata.playerId != null) {
            pair.copy(provenance = DPOProvenance(
                playerId = pair.metadata.playerId,
                appVersion = appVersion,
                deviceId = deviceId
            ))
        } else pair
        dpoCache.add(signedPair)
    }

    suspend fun flushDPOCache(): DPOCaptureResponse {
        if (dpoCache.isEmpty()) return DPOCaptureResponse(false, null, 0, 100, false, false)
        val batch = dpoCache.flush()
        val response: DPOCaptureResponse = client.post("$baseUrl/dpo/batch") {
            contentType(ContentType.Application.Json)
            setBody(DPOBatch(batch))
        }.body()
        return response
    }

    // ─── Adaptive Quest Generation ──────────────────────

    suspend fun generateAdaptiveQuest(req: AdaptiveQuestRequest): AdaptiveQuestResponse =
        client.post("$baseUrl/quest/generate") { contentType(ContentType.Application.Json); setBody(req) }.body()

    suspend fun getQuest(req: QuestRequest): QuestResponse =
        client.post("$baseUrl/quest") { contentType(ContentType.Application.Json); setBody(req) }.body()

    // ─── Art & Narration ─────────────────────────────────

    suspend fun generateIllustrationPrompt(req: ArtRequest): ArtResponse =
        client.post("$baseUrl/art") { contentType(ContentType.Application.Json); setBody(req) }.body()

    suspend fun generateNarrationScript(req: NarrationRequest): NarrationResponse =
        client.post("$baseUrl/narration") { contentType(ContentType.Application.Json); setBody(req) }.body()

    // ─── Voice I/O ───────────────────────────────────────

    suspend fun speechToText(req: VoiceSTTRequest): VoiceSTTResponse =
        client.post("$baseUrl/voice/stt") { contentType(ContentType.Application.Json); setBody(req) }.body()

    suspend fun textToSpeech(req: VoiceTTSRequest): VoiceTTSResponse =
        client.post("$baseUrl/voice/tts") { contentType(ContentType.Application.Json); setBody(req) }.body()

    /**
     * Voice round-trip: player speaks → STT → Nami processes → TTS → audio back.
     * One call. Returns Nami's audio response.
     */
    suspend fun voiceNamiConversation(audioBase64: String, context: String, playerId: String): VoiceTTSResponse {
        val stt = speechToText(VoiceSTTRequest(audioBase64, playerId = playerId))
        val nami = getNamiGuidance(NamiRequest(playerMessage = stt.transcript, context = context, playerId = playerId))
        return textToSpeech(VoiceTTSRequest(text = nami.guidance, voice = "nami_gentle", emotion = when(nami.animation) { "flickers_with_concern" -> "concerned"; "glows_steady" -> "confident"; else -> "calm" }))
    }

    // ─── Cross-App Progression ───────────────────────────

    suspend fun syncProgression(req: ProgressionSyncRequest): ProgressionResponse =
        client.post("$baseUrl/progression/sync") { contentType(ContentType.Application.Json); setBody(req) }.body()

    suspend fun getProgression(playerId: String): JsonObject =
        client.get("$baseUrl/progression/$playerId").body()

    // ─── Adaptive Difficulty ─────────────────────────────

    suspend fun updateDifficulty(req: DifficultyRequest): DifficultyResponse =
        client.post("$baseUrl/adaptive/difficulty") { contentType(ContentType.Application.Json); setBody(req) }.body()

    // ─── Mappings ────────────────────────────────────────

    suspend fun getMappings(): List<MappingEntry> =
        client.get("$baseUrl/mappings").body()

    fun close() { client.close() }
}
