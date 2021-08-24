package com.anaf.healthyliving.networking.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


/**
 * Domain models
 */

@Parcelize
data class Articles(
    @SerializedName("response")
    val response: Response = Response(),
) : Parcelable

@Parcelize
data class Response(
    @SerializedName("docs")
    val articles: List<Article> = listOf(),
) : Parcelable

@Parcelize
data class Article(
    @SerializedName("abstract")
    val `abstract`: String = "",
    @SerializedName("byline")
    val byline: Byline = Byline(),
    @SerializedName("headline")
    val headline: Headline = Headline(),
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("lead_paragraph")
    val leadParagraph: String = "",
    @SerializedName("multimedia")
    val multimedia: List<Multimedia>? = null,
    @SerializedName("news_desk")
    val newsDesk: String = "",
    @SerializedName("web_url")
    val webUrl: String = "",
) : Parcelable

@Parcelize
data class Multimedia(
    val url: String
) : Parcelable

@Parcelize
data class Byline(
    @SerializedName("original")
    val original: String = "",
) : Parcelable

@Parcelize
data class Headline(
    @SerializedName("main")
    val main: String = "",
) : Parcelable


