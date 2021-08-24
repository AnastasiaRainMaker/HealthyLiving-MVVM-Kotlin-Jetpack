package com.anaf.healthyliving.networking

import com.anaf.healthyliving.networking.models.Articles
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkingService {

    @GET("svc/search/v2/articlesearch.json?api-key=${API_KEY}&sort=newest")
    suspend fun loadHealthArticles(
        @Query("fq") facet: String = "news_desk:(\"Health\")"
    ): Articles

    companion object {
        private const val API_KEY = "ZvbnL1zMhfpxWvuV2404Q7vAPmwUlce0"
    }
}