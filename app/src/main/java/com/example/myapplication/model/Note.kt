package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.util.Constants.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val text : String,
    val title: String,
    val priority : Int
)