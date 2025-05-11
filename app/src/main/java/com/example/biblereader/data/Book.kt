package com.example.biblereader.data

/**
 * Represents a book in the Bible.
 * This is a simplified model used for UI purposes, separate from the database entity.
 */
data class Book(
    val id: Int,
    val name: String,
    val testament: String,
    val abbreviation: String,
    val bookOrder: Int
) 