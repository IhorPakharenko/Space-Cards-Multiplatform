package com.isao.spacecards.component.astrobinimages.network.model

import com.isao.spacecards.component.astrobinimages.domain.AstrobinImage
import com.isao.spacecards.component.common.serializer.InstantWithNoExplicitTimezone
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponse(
  @SerialName("meta") val meta: Meta,
  @SerialName("objects") val objects: List<AstrobinImageNetwork>,
) {
  @Serializable
  data class Meta(
    @SerialName("limit") val limit: Int,
    @SerialName("next") val next: String?,
    @SerialName("offset") val offset: Int,
    @SerialName("previous") val previous: String?,
    @SerialName("total_count") val totalCount: Int,
  )
}

@Serializable
data class AstrobinImageNetwork(
  @SerialName("bookmarks") val bookmarks: Int,
  @SerialName("comments") val comments: Int,
  @SerialName("data_source") val dataSource: String?,
  @SerialName("description") val description: String?,
  @SerialName("id") val id: Int,
  @SerialName("license") val license: Int,
  @SerialName("license_name") val licenseName: String?,
  @SerialName("likes") val likes: Int,
  @Serializable(with = InstantWithNoExplicitTimezone::class)
  @SerialName("published") val published: Instant,
  @SerialName("title") val title: String?,
  @Serializable(with = InstantWithNoExplicitTimezone::class)
  @SerialName("updated") val updated: Instant,
  @Serializable(with = InstantWithNoExplicitTimezone::class)
  @SerialName("uploaded") val uploaded: Instant,
  @SerialName("url_gallery") val urlGallery: String?,
  @SerialName("url_hd") val urlHd: String,
  @SerialName("url_histogram") val urlHistogram: String?,
  @SerialName("url_real") val urlReal: String,
  @SerialName("url_regular") val urlRegular: String?,
  @SerialName("url_thumb") val urlThumb: String?,
  @SerialName("user") val user: String?,
  @SerialName("views") val views: Int,
)

internal fun AstrobinImageNetwork.toDomainModel() = AstrobinImage(
  bookmarks = this.bookmarks,
  comments = this.comments,
  dataSource = this.dataSource,
  description = this.description,
  id = this.id,
  license = this.license,
  licenseName = this.licenseName,
  likes = this.likes,
  published = this.published,
  title = this.title,
  updated = this.updated,
  uploaded = this.uploaded,
  urlGallery = this.urlGallery,
  urlHd = this.urlHd,
  urlHistogram = this.urlHistogram,
  urlReal = this.urlReal,
  urlRegular = this.urlRegular,
  urlThumb = this.urlThumb,
  user = this.user,
  views = this.views,
)
