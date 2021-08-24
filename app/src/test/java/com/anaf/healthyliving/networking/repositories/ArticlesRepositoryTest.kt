package com.anaf.healthyliving.networking.repositories

import com.anaf.healthyliving.networking.NetworkingService
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class ArticlesRepositoryTest {

    private val mockNetworkingInterface = mockk<NetworkingService>(relaxed = true)

    private val repository = ArticlesRepository(mockNetworkingInterface)

    @Test
    fun `test load articles calls network service`() {
        runBlockingTest {
            repository.loadArticles()
        }
        coVerify {
            mockNetworkingInterface.loadHealthArticles()
        }
    }


}