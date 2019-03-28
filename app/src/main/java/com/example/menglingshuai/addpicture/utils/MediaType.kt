package com.example.menglingshuai.addpicture.utils

enum class MediaType(val type: String, val suffix: String) {
    IMAGE_JPG("image", ".jpg"),
    IMAGE_JPEG("image", ".jpeg"),
    IMAGE_PNG("image", ".png"),
    IMAGE_GIF("image", ".gif"),
    AUDIO("audio", ".mp3"),
    VIDEO("video", ".mp4")
}