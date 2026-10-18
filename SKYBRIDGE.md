# Skybridge — Nami, powered by KIN

The app embeds the Kinetigor Skybridge SDK (`com.kinetigor.skybridge`) so **Nami**,
Sera Venn's Moon-Koi companion, can narrate, guide, and weave cybersecurity
learning into the story.

## What's wired in

- `SkybridgeClient.kt` — the SDK (Ktor + kotlinx.serialization, streaming SSE)
- `NamiCompanion.kt` — fail-soft helpers wired to the app's real types:
  - `enrichParagraph()` — fills `blueGlassSecret` / `amberLore` / `moonlightTruth` on `StoryParagraph`
  - `narrateChapter()` — streams Nami narration per `NarratorPersona`
  - `ask()` — one-shot Nami guidance
  - `onChapterComplete()` — cross-app progression sync to the floating-market

## Fail-soft by design

Every Nami call degrades to the original app behavior if the backend is
unreachable. Nothing crashes, and reading/narration work fully offline.

## Running the backend

The backend is verified (36/36 tests) but **not yet live-deployed** (the Modal
URL is pending). To use it locally:

```bash
cd skybridge/backend   # from the bountywarz repo
pip install fastapi uvicorn
KIN_FABRIC_DISABLED_TIERS=ollama,hf-router,gateway python -m uvicorn server:app --port 8000
```

Then point the client at `http://10.0.2.2:8000` (Android emulator host loopback):

```kotlin
val nami = NamiCompanion(SkybridgeClient(baseUrl = "http://10.0.2.2:8000"))
```

Questions: Kiran on Slack, channel **#cindy-kinetigor**.
