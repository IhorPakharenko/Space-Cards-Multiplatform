package com.isao.spacecards.presentation.liked.mapper

import com.isao.spacecards.data.model.ImageSource
import com.isao.spacecards.domain.model.LikedImage
import com.isao.spacecards.presentation.liked.model.LikedImageDisplayable
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