package com.example.letmeknow.model

data class InputData(
    val Question:String?="",
    val Date :String?="",
    val Time :String?="",
    val List:ArrayList<String>,
    val uid:String?=""
)