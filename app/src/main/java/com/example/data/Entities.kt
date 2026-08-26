package com.example.data

data class ChannelLink(
    var id: String = "",
    val title: String = "",
    val url: String = "",
    val platform: String = "" // e.g., "YouTube", "Instagram"
)

data class VideoPost(
    var id: String = "",
    val title: String = "",
    val videoUrl: String = "",
    val thumbnailUrl: String = "",
    val description: String = "",
    val creator: String = "",
    val views: Int = 0,
    val tags: String = "",
    val isFeatured: Boolean = false,
    var duration: String = "10:00",
    var uploadDate: Long = 0L
)

data class FeedbackMsg(
    var id: String = "",
    val user: String = "",
    val message: String = "",
    val timestamp: Long = 0L
)

data class SalesLink(
    var id: String = "",
    val name: String = "",
    val description: String = "",
    val url: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0,
    val discount: String = ""
)
