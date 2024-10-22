package com.isao.yfoo3.presentation.liked.mapper

import com.isao.yfoo3.domain.model.ImageSource
import com.isao.yfoo3.domain.model.LikedImage
import com.isao.yfoo3.presentation.liked.model.LikedImageDisplayable
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Clock

class LikedImageMapperTest : FunSpec({

    val domainModel = LikedImage(
        id = "id",
        imageId = "imageId",
        source = ImageSource.THIS_WAIFU_DOES_NOT_EXIST,
        dateAdded = Clock.System.now()
    )

    val presentationModel = LikedImageDisplayable(
        id = domainModel.id,
        imageUrl = "https://www.thiswaifudoesnotexist.net/example-${domainModel.imageId}.jpg",
        source = domainModel.source
    )

    test("domain to displayable") {
        domainModel.toPresentationModel() shouldBe presentationModel
    }
})