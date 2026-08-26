package com.example.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object Videos : Screen("videos")
    object Feedback : Screen("feedback")
    object Sales : Screen("sales")
    object Search : Screen("search")
    object Profile : Screen("profile")
    object AdminUpload : Screen("admin_upload")
    object VideoPlayer : Screen("video_player/{videoId}") {
        fun createRoute(videoId: String) = "video_player/$videoId"
    }
}
