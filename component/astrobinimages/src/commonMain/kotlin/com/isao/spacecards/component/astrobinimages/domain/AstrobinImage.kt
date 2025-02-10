package com.isao.spacecards.component.astrobinimages.domain

import kotlinx.datetime.Instant

data class AstrobinImage(
  val id: Int,
  val user: String,
  val title: String?,
  val description: String?,
  val bookmarks: Int,
  val comments: Int,
  val dataSource: String?,
  val license: Int,
  val licenseName: String?,
  val likes: Int,
  val publishedAt: Instant,
  val updatedAt: Instant,
  val uploadedAt: Instant,
  val urlGallery: String?,
  val urlHd: String,
  val urlHistogram: String?,
  val urlReal: String,
  val urlRegular: String?,
  val urlThumb: String?,
  val views: Int,
  val bookmarkedAt: Instant?,
  val viewedAt: Instant?,
) {
  // Let's just hope the url schema never ever changes
  val urlPost get() = urlReal.removeSuffix("0/rawthumb/real/")

  val urlAuthor get() = "https://www.astrobin.com/users/$user/"
}
