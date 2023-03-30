package com.example.letmeknow.model

data class GlobalData(
    val Question: String? = "",
    val Date: String? = "",
    val Time: String? = "",
    val List: ArrayList<String>,
    val uid: String? = "",
    val picURL: String? = ""
)
