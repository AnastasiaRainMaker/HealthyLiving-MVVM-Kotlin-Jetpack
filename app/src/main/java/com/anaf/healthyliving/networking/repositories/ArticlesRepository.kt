package com.anaf.healthyliving.networking.repositories

import com.anaf.healthyliving.networking.NetworkingService
import javax.inject.Inject

class ArticlesRepository @Inject constructor(private val networkingService: NetworkingService) {

   suspend fun loadArticles() = networkingService.loadHealthArticles()

}