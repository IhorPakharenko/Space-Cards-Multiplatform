package com.isao.spacecards.component.astrobinimages.domain

import kotlinx.datetime.Instant

data class AstrobinImage(
  val bookmarks: Int,
  val comments: Int,
  val dataSource: String?,
  val description: String?,
  val id: Int,
  val license: Int,
  val licenseName: String?,
  val likes: Int,
  //TODO ensure consistent naming in all properties of type Instant
  val published: Instant,
  val title: String?,
  val updated: Instant,
  val uploaded: Instant,
  val urlGallery: String?,
  val urlHd: String,
  val urlHistogram: String?,
  val urlReal: String,
  val urlRegular: String?,
  val urlThumb: String?,
  val user: String?,
  val views: Int,
  val bookmarkedAt: Instant? = null,
  val viewedAt: Instant? = null,
)
