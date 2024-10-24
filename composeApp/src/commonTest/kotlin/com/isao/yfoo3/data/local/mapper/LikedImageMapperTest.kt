package com.isao.yfoo3.data.local.mapper

import com.isao.yfoo3.data.model.ImageSource
import com.isao.yfoo3.data.model.LikedImageCached
import com.isao.yfoo3.domain.model.LikedImage
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

    val entityModel = LikedImageCached(
        id = domainModel.id,
        imageId = domainModel.imageId,
        source = domainModel.source,
        dateAdded = domainModel.dateAdded
    )

    test("domain to entity") {
        domainModel.toEntityModel() shouldBe entityModel
    }

    test("entity to domain") {
        entityModel.toDomainModel() shouldBe domainModel
    }
})