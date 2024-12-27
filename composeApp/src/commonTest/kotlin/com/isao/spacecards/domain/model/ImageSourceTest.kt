package com.isao.spacecards.domain.model

import com.isao.spacecards.core.db.model.ImageSource
import com.isao.spacecards.utils.FakeRandom
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ImageSourceTest : FunSpec({
    context("Source: This waifu does not exist") {
        val subject = ImageSource.THIS_WAIFU_DOES_NOT_EXIST

        test("websiteUrl") {
            subject.websiteUrl shouldBe "https://www.thiswaifudoesnotexist.net/"
        }

        test("websiteName") {
            subject.websiteName shouldBe "This Waifu Does Not Exist"
        }

        test("getImageUrl") {
            val id = "abc"
            subject.getImageUrl(id) shouldBe "https://www.thiswaifudoesnotexist.net/example-$id.jpg"
        }

        test("getRandomImageId") {
            val fakeRandom = FakeRandom(expectedIntUntil = 100000 + 1)
            subject.getRandomImageId(fakeRandom) shouldBe Int.MIN_VALUE.toString()
        }
    }

    context("Source: These cats do not exist") {
        val subject = ImageSource.THESE_CATS_DO_NOT_EXIST

        test("websiteUrl") {
            subject.websiteUrl shouldBe "https://thesecatsdonotexist.com/"
        }

        test("websiteName") {
            subject.websiteName shouldBe "These Cats Do Not Exist"
        }

        test("getImageUrl") {
            val id = "abc"
            subject.getImageUrl(id) shouldBe "https://d2ph5fj80uercy.cloudfront.net/01/cat$id.jpg"
        }

        test("getRandomImageId") {
            val fakeRandom = FakeRandom(expectedIntUntil = 8000 + 1)
            subject.getRandomImageId(fakeRandom) shouldBe Int.MIN_VALUE.toString()
        }
    }

    context("Source: This Night Sky Does Not Exist") {
        val subject = ImageSource.THIS_NIGHT_SKY_DOES_NOT_EXIST

        test("websiteUrl") {
            subject.websiteUrl shouldBe "https://www.arthurfindelair.com/thisnightskydoesnotexist/"
        }

        test("websiteName") {
            subject.websiteName shouldBe "This Night Sky Does Not Exist"
        }

        test("getImageUrl") {
            val id = "abc"
            subject.getImageUrl(id) shouldBe "https://firebasestorage.googleapis.com/v0/b/thisnightskydoesnotexist.appspot.com/o/images%2Fseed$id.jpg?alt=media"
        }

        test("getRandomImageId") {
            val fakeRandom = FakeRandom(expectedIntFrom = 1000, expectedIntUntil = 5000 + 1)
            subject.getRandomImageId(fakeRandom) shouldBe Int.MIN_VALUE.toString()
        }
    }
})