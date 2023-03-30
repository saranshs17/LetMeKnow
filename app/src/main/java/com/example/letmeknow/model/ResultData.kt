package com.example.letmeknow.model

data class ResultData(
    val Question: String? = "",
    val Date: String? = "",
    val Time: String? = "",
    val List: ArrayList<String>,
    val uid: String? = "",
    val picURL: String? = ""
)
