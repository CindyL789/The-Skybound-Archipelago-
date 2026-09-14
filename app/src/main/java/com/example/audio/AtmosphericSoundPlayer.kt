package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.sin

enum class SoundscapePreset(val title: String, val description: String) {
  CLOUD_SEA("Cloud Sea Winds", "Airy high-altitude breeze and delicate glass lantern harmonics"),
  SALT_RAIN("Salt Pier Rain", "Soft rain pattering on slate tiles and harbor water ripples"),
  CHAIN_STORM("Great Chain Storm", "Distant thunder rumbles and low resonant iron cable hums")
}

/**
 * Procedural ambient synthesizer that generates soothing atmospheric soundscapes
 * of the Skybound Archipelago with volume control, soundscape presets, and speech ducking.
 * Runs completely offline with zero external audio assets.
 */
class AtmosphericSoundPlayer {
  private var audioTrack: AudioTrack? = null
  private var synthesisJob: Job? = null
  private val scope = CoroutineScope(Dispatchers.Default)

  @Volatile private var isPlaying = false
  @Volatile private var currentPreset = SoundscapePreset.CLOUD_SEA
  @Volatile private var masterVolume = 0.7f
  @Volatile private var isDucked = false

  fun isPlaying(): Boolean = isPlaying
  fun getPreset(): SoundscapePreset = currentPreset
  fun getVolume(): Float = masterVolume

  fun setPreset(preset: SoundscapePreset) {
    currentPreset = preset
  }

  fun setVolume(volume: Float) {
    masterVolume = volume.coerceIn(0.0f, 1.0f)
  }

  fun setDucking(ducked: Boolean) {
    isDucked = ducked
  }

  fun start(preset: SoundscapePreset = currentPreset) {
    currentPreset = preset
    if (isPlaying) return
    isPlaying = true

    synthesisJob = scope.launch {
      try {
        val sampleRate = 22050
        val bufferSize = AudioTrack.getMinBufferSize(
          sampleRate,
          AudioFormat.CHANNEL_OUT_MONO,
          AudioFormat.ENCODING_PCM_16BIT
        ).coerceAtLeast(4096)

        audioTrack = AudioTrack.Builder()
          .setAudioAttributes(
            AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_MEDIA)
              .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
              .build()
          )
          .setAudioFormat(
            AudioFormat.Builder()
              .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
              .setSampleRate(sampleRate)
              .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
              .build()
          )
          .setBufferSizeInBytes(bufferSize)
          .setTransferMode(AudioTrack.MODE_STREAM)
          .build()

        audioTrack?.play()

        val buffer = ShortArray(1024)
        var phasePrimary = 0.0
        var phaseSecondary = 0.0
        var decayTransient = 0.0
        var transientFreq = 587.33 // D5 chime
        val random = Random()
        var eventCounter = 0

        while (isActive && isPlaying) {
          eventCounter++

          val preset = currentPreset
          val effectiveVol = masterVolume * (if (isDucked) 0.25f else 1.0f)

          when (preset) {
            SoundscapePreset.CLOUD_SEA -> {
              // Soft glass lantern harmonic every ~4-6 seconds
              if (eventCounter > 85 && random.nextFloat() < 0.04f) {
                eventCounter = 0
                decayTransient = 1.0
                transientFreq = listOf(440.0, 523.25, 587.33, 659.25, 783.99)[random.nextInt(5)]
              }
            }
            SoundscapePreset.SALT_RAIN -> {
              // Occasional distant droplet chime or wave roll
              if (eventCounter > 40 && random.nextFloat() < 0.12f) {
                eventCounter = 0
                decayTransient = 0.6
                transientFreq = listOf(880.0, 987.77, 1046.5, 1174.66)[random.nextInt(4)]
              }
            }
            SoundscapePreset.CHAIN_STORM -> {
              // Deep low thunder rumble or tension ping
              if (eventCounter > 120 && random.nextFloat() < 0.035f) {
                eventCounter = 0
                decayTransient = 1.0
                transientFreq = listOf(65.41, 82.41, 110.0, 130.81)[random.nextInt(4)]
              }
            }
          }

          for (i in buffer.indices) {
            val totalSample: Double
            when (preset) {
              SoundscapePreset.CLOUD_SEA -> {
                val noise = (random.nextDouble() * 2.0 - 1.0) * 0.14
                phasePrimary += 0.0035
                val windLfo = 0.5 + 0.5 * sin(phasePrimary)

                var chimeSample = 0.0
                if (decayTransient > 0.001) {
                  phaseSecondary += (2.0 * Math.PI * transientFreq) / sampleRate
                  chimeSample = sin(phaseSecondary) * decayTransient * 0.22
                  decayTransient *= 0.99982
                }
                totalSample = (noise * windLfo + chimeSample).coerceIn(-1.0, 1.0)
              }

              SoundscapePreset.SALT_RAIN -> {
                // Rain white/pink noise modulated with gentle wave swell
                val rainNoise = (random.nextDouble() * 2.0 - 1.0) * 0.12
                phasePrimary += 0.002
                val swell = 0.6 + 0.4 * sin(phasePrimary)

                var dripSample = 0.0
                if (decayTransient > 0.001) {
                  phaseSecondary += (2.0 * Math.PI * transientFreq) / sampleRate
                  dripSample = sin(phaseSecondary) * decayTransient * 0.12
                  decayTransient *= 0.9994
                }
                totalSample = (rainNoise * swell + dripSample).coerceIn(-1.0, 1.0)
              }

              SoundscapePreset.CHAIN_STORM -> {
                // Low-frequency rumble + gusting wind
                val rumbleNoise = (random.nextDouble() * 2.0 - 1.0) * 0.18
                phasePrimary += 0.006
                val gust = 0.4 + 0.6 * sin(phasePrimary)

                var thunderSample = 0.0
                if (decayTransient > 0.001) {
                  phaseSecondary += (2.0 * Math.PI * transientFreq) / sampleRate
                  thunderSample = (sin(phaseSecondary) + 0.5 * sin(phaseSecondary * 0.5)) * decayTransient * 0.28
                  decayTransient *= 0.99988
                }
                totalSample = (rumbleNoise * gust + thunderSample).coerceIn(-1.0, 1.0)
              }
            }

            buffer[i] = (totalSample * 32767 * 0.4 * effectiveVol).toInt().toShort()
          }

          audioTrack?.write(buffer, 0, buffer.size)
        }
      } catch (e: Exception) {
        Log.e("AtmosphericPlayer", "Audio synthesis error: ${e.message}")
      } finally {
        cleanup()
      }
    }
  }

  fun stop() {
    isPlaying = false
    synthesisJob?.cancel()
    synthesisJob = null
    cleanup()
  }

  private fun cleanup() {
    try {
      audioTrack?.stop()
      audioTrack?.release()
    } catch (e: Exception) {
      // ignore
    }
    audioTrack = null
  }
}
