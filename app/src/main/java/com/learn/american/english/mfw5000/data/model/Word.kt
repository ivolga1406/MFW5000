package com.learn.american.english.mfw5000.data.model

data class Word(
    var  id: String? = null,
    var  number: Int? = 0,
    var  word: String? = null,
    var  part_of_speech: String? = null,
    var  definition: String? = null,
    var  example_en: String? = null,
    var  example_ru: String? = null
)
//):Item()




//data class Series(
//    override var id: String? = null,
//    override var title: String? = null,
//    override var difficulty: String? = null
//):Item()