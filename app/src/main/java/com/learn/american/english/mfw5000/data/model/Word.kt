package com.learn.american.english.mfw5000.data.model

data class Word(
    var  id: String? = null,
    var  number: Int? = 0,
    var  word: String? = null,
    var  partOfSpeech: String? = null,
    var  definition: String? = null,
    var  exampleEn: String? = null,
    var  exampleRu: String? = null
)