package com.anaf.healthyliving.ui.articleshome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.anaf.healthyliving.networking.CoroutineDispatcherProvider
import com.anaf.healthyliving.networking.models.Articles
import com.anaf.healthyliving.networking.repositories.ArticlesRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.lang.IllegalStateException

@ExperimentalCoroutinesApi
class ArticlesHomeViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val repository = mockk<ArticlesRepository>(relaxed = true)
    private val coroutineDispatcherProvider = mockk<CoroutineDispatcherProvider> {
        every {
            io
        } returns testDispatcher
    }
    private val viewModel = spyk(ArticlesHomeViewModel(repository, coroutineDispatcherProvider))

    @Test
    fun `test loadArticles turns on progress`() {
        //using an approach of recording all posted values to inProgress to
        //make sure we first turn it on and the off
        val observer = mockk<Observer<Boolean>>()
        val slot = slot<Boolean>()
        val list = arrayListOf<Boolean>()

        //start observing
        viewModel.isProgressBarVisible.observeForever(observer)

        //capture value on every call
        every { observer.onChanged(capture(slot)) } answers {
            //store captured value
            list.add(slot.captured)
        }
        viewModel.loadArticles()
        assertTrue(list[0])
        assertFalse(list[1])
    }

    @Test
    fun `test loadArticles calls repo`() {
        viewModel.loadArticles()
        coVerify {
            repository.loadArticles()
        }
    }

    @Test
    fun `test onEmployeesReceived is called when response returned`() {
        val mockkResponse = Articles()
        coEvery { repository.loadArticles() } returns mockkResponse
        viewModel.loadArticles()
        verify {
            viewModel.onArticlesReceived(mockkResponse.response.articles)
        }
    }

    @Test
    fun `test onEmployeeLoadError is called when error returned`() {
        coEvery { repository.loadArticles() } throws IllegalStateException()
        viewModel.loadArticles()
        verify {
            viewModel.onArticlesLoadError()
        }
    }

    @Test
    fun `test onEmployeeLoadError dismisses progress`() {
        coEvery { repository.loadArticles() } throws IllegalStateException()
        viewModel.loadArticles()
        assertTrue(viewModel.isProgressBarVisible.value == false)
    }

    @Test
    fun `test onEmployeeLoadError posts error`() {
        coEvery { repository.loadArticles() } throws IllegalStateException()
        viewModel.loadArticles()
        assertTrue(viewModel.uiCommandLiveData.value == ArticlesHomeViewModel.ArticlesViewModelUiCommand.ShowError)
    }


}