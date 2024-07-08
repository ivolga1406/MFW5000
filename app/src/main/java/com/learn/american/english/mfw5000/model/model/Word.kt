package com.learn.american.english.mfw5000.data.model.model

import com.google.firebase.firestore.PropertyName

data class Word(
    var id: String? = null,
    var number: Int? = 0,
    var word: String? = null,

    @get:PropertyName("part_of_speech")
    @set:PropertyName("part_of_speech")
    var partOfSpeech: String? = null,

    var definition: String? = null,

    @get:PropertyName("example_en")
    @set:PropertyName("example_en")
    var exampleEn: String? = null,

    @get:PropertyName("example_ru")
    @set:PropertyName("example_ru")
    var exampleRu: String? = null
)
