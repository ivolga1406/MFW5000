package com.learn.american.english.mfw5000.data.model

data class Word(
    override var  id: String? = null,
    override var  number: Int? = 0,
    override var  word: String? = "",
    override var  part_of_speech: String? = null,
    override var  definition: String? = null,
    override var  example_en: String? = null,
    override var  example_ru: String? = null
):Item()




//data class Series(
//    override var id: String? = null,
//    override var title: String? = null,
//    override var difficulty: String? = null
//):Item()