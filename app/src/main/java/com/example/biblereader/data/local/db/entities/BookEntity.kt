package com.example.biblereader.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bookId")
    val bookId: Int? = null,
    
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    val name: String,
    
    @ColumnInfo(name = "testament", typeAffinity = ColumnInfo.TEXT)
    val testament: String,
    
    @ColumnInfo(name = "bookOrder")
    val bookOrder: Int
)
