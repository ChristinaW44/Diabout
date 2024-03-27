package com.example.diabout.database

import java.util.Date
import java.text.SimpleDateFormat
import java.time.LocalDate

data class Activity (
    var id : Int,
    var userid: Int,
    var time: String,
    var steps: Int
)