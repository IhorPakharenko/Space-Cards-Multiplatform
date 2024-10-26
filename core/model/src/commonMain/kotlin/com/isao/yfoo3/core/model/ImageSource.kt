package com.isao.yfoo3.core.model

import kotlin.random.Random

enum class ImageSource {
    THIS_WAIFU_DOES_NOT_EXIST,
    THESE_CATS_DO_NOT_EXIST,
    THIS_NIGHT_SKY_DOES_NOT_EXIST;

    val websiteUrl
        get() = when (this) {
            THIS_WAIFU_DOES_NOT_EXIST -> "https://www.thiswaifudoesnotexist.net/"
            THESE_CATS_DO_NOT_EXIST -> "https://thesecatsdonotexist.com/"
            THIS_NIGHT_SKY_DOES_NOT_EXIST -> "https://www.arthurfindelair.com/thisnightskydoesnotexist/"
        }

    val websiteName
        get() = when (this) {
            THIS_WAIFU_DOES_NOT_EXIST -> "This Waifu Does Not Exist"
            THESE_CATS_DO_NOT_EXIST -> "These Cats Do Not Exist"
            THIS_NIGHT_SKY_DOES_NOT_EXIST -> "This Night Sky Does Not Exist"
        }

    fun getImageUrl(imageId: String) = when (this) {
        THIS_WAIFU_DOES_NOT_EXIST -> "https://www.thiswaifudoesnotexist.net/example-$imageId.jpg"
        THESE_CATS_DO_NOT_EXIST -> "https://d2ph5fj80uercy.cloudfront.net/01/cat$imageId.jpg"
        THIS_NIGHT_SKY_DOES_NOT_EXIST -> "https://firebasestorage.googleapis.com/v0/b/thisnightskydoesnotexist.appspot.com/o/images%2Fseed$imageId.jpg?alt=media"
    }

    fun getRandomImageId(random: Random) = when (this) {
        THIS_WAIFU_DOES_NOT_EXIST -> random.nextInt(100000 + 1).toString()
        THESE_CATS_DO_NOT_EXIST -> random.nextInt(8000 + 1).toString()
        THIS_NIGHT_SKY_DOES_NOT_EXIST -> random.nextInt(1000, 5000 + 1).toString()
    }
}