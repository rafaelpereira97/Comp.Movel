package com.example.compmovel.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey
    var uid: Int = 0,
    var note: String? = "",
    var description: String? = "",
    var lat: String? = "",
    var long: String? = "",
    var date: String? = "",
)