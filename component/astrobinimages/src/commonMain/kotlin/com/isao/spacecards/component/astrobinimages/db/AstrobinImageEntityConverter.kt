package com.isao.spacecards.component.astrobinimages.db

import com.isao.spacecards.astrobinimages.data.AstrobinImageEntity
import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage

internal fun AstrobinImageEntity.toDomainModel(): AstrobinImage = AstrobinImage(
  bookmarks = this.bookmarks,
  comments = this.comments,
  dataSource = this.dataSource,
  description = this.description,
  id = this.id,
  license = this.license,
  licenseName = this.licenseName,
  likes = this.likes,
  publishedAt = this.publishedAt,
  title = this.title,
  updatedAt = this.updatedAt,
  uploadedAt = this.uploadedAt,
  urlGallery = this.urlGallery,
  urlHd = this.urlHd,
  urlHistogram = this.urlHistogram,
  urlReal = this.urlReal,
  urlRegular = this.urlRegular,
  urlThumb = this.urlThumb,
  user = this.user,
  views = this.views,
  viewedAt = this.viewedAt,
  bookmarkedAt = this.bookmarkedAt,
)

fun AstrobinImage.toEntityModel(): AstrobinImageEntity = AstrobinImageEntity(
  id = this.id,
  bookmarks = this.bookmarks,
  comments = this.comments,
  dataSource = this.dataSource,
  description = this.description,
  license = this.license,
  licenseName = this.licenseName,
  likes = this.likes,
  publishedAt = this.publishedAt,
  title = this.title,
  updatedAt = this.updatedAt,
  uploadedAt = this.uploadedAt,
  urlGallery = this.urlGallery,
  urlHd = this.urlHd,
  urlHistogram = this.urlHistogram,
  urlReal = this.urlReal,
  urlRegular = this.urlRegular,
  urlThumb = this.urlThumb,
  user = this.user,
  views = this.views,
  bookmarkedAt = this.bookmarkedAt,
  viewedAt = this.viewedAt,
)
