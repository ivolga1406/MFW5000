package com.learn.american.english.mfw5000.data.model

abstract class Item {

    open var id: String? = null
    open var number: Int? = 0
    open var word: String? = null
    open var part_of_speech: String? = null
    open var definition: String? = null
    open var example_en: String? = null
    open var example_ru: String? = null
}