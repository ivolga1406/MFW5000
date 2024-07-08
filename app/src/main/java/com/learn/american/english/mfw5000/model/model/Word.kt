package com.learn.american.english.mfw5000.model.model

import com.google.firebase.firestore.PropertyName

data class Word(
    var id: String? = null,
    var number: Int? = 0,
    var word: String? = null,

    @PropertyName("part_of_speech")
    var partOfSpeech: String? = null,

    var definition: String? = null,

    @PropertyName("example_en")
    var exampleEn: String? = null,

    @PropertyName("example_ru")
    var exampleRu: String? = null
)
