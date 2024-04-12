package com.example.diabout.helpers

var InfoList = mutableListOf<Info>()

data class Info(
    var title : String,
    var description : String,
    var link: String,
    val id: Int? = InfoList.size
)
