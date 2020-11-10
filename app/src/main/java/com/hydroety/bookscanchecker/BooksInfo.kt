package com.hydroety.bookscanchecker

import com.google.gson.annotations.SerializedName

data class BooksInfo(
        @SerializedName("kind")
        val kind: String,
        @SerializedName("totalItems")
        val totalItems: Int,
        @SerializedName("items")
        val items: List<Volume>
)

data class Volume (
        @SerializedName("kind")
        val kind: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("etag")
        val etag: String,
        @SerializedName("selfLink")
        val selfLink: String,
        @SerializedName("volumeInfo")
        val volumeInfo: VolumeInfo
)

data class VolumeInfo (
        @SerializedName("title")
        val title: String,
        @SerializedName("authors")
        val authors: List<String>,
        @SerializedName("publisher")
        val publisher: String,
        @SerializedName("publishedDate")
        val publishedDate: String,
        @SerializedName("pageCount")
        val pageCount: String,
        @SerializedName("imageLinks")
        val imageLinks: ImageLinks
)

data class ImageLinks (
        @SerializedName("smallThumbnail")
        val smallThumbnail: String,
        @SerializedName("thumbnail")
        val thumbnail: String
)