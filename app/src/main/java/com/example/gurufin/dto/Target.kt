package com.example.gurufin.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "targetTbl")
data class Target(
    @PrimaryKey(autoGenerate = true) var num: Int, //넘버
    val title: String,
    val contents: String, //목표 string
    val timestamp: String,
    var isChecked: Boolean
) : Serializable {
}

