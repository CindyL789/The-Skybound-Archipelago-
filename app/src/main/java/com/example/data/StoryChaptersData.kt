package com.example.data

object StoryChaptersData {
  val chapters: List<Chapter> by lazy {
    StoryChaptersPart1.chapters + StoryChaptersPart2.chapters
  }
}
