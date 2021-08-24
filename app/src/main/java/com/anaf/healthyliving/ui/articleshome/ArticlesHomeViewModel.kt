package com.anaf.healthyliving.ui.articleshome

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaf.healthyliving.networking.CoroutineDispatcherProvider
import com.anaf.healthyliving.networking.models.Article
import com.anaf.healthyliving.networking.repositories.ArticlesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.reflect.Modifier.PRIVATE
import javax.inject.Inject

/**
 * Creating a ViewModel to prevent data from reloading on config changes and also
 * separating logic from View for better testability
 */
@HiltViewModel
class ArticlesHomeViewModel @Inject constructor(
    private val repository: ArticlesRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    /**
     * Using command livedata to communicate to View uni-directionally
     */
    private val _uiCommandLiveData = MutableLiveData<ArticlesViewModelUiCommand>()
    val uiCommandLiveData: LiveData<ArticlesViewModelUiCommand> = _uiCommandLiveData

    /**
     * Using observer pattern thu databinding to control progress state
     */
    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean> = _isProgressBarVisible

    /**
     * Using observer pattern thu databinding to load the item and possibly post reloads and updates if needed in the future
     */
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    /**
     * Starting a coroutine in viewModelScope to get the lifecycle management for free
     * The work will be cancelled when ViewModel is destroyed so we don't have to cancel the job if user for example kills the app
     * or presses our of the screen.
     *
     * Launching the coroutine in the io dispatcher as it is tuned for IO operations as networking
     *
     * Adding try catch as coroutines with retrofit have such behavior that all 200 success responses are dropped into the try block and
     * 400 <= are in the catch block with an exception.
     */
    fun loadArticles() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            _isProgressBarVisible.postValue(true)
            try {
                repository.loadArticles().run {
                    _isProgressBarVisible.postValue(false)
                    onArticlesReceived(response.articles)
                }
            } catch (ex: Exception) {
                onArticlesLoadError()
            }
        }
    }

    @VisibleForTesting(otherwise = PRIVATE)
    fun onArticlesReceived(articles: List<Article>) {
        _articles.postValue(articles)
    }

    /**
     * Not differentiating errors at this point, just commanding View to show error state
     */
    @VisibleForTesting(otherwise = PRIVATE)
    fun onArticlesLoadError() {
        _isProgressBarVisible.postValue(false)
        _uiCommandLiveData.postValue(ArticlesViewModelUiCommand.ShowError)
    }

    /**
     * Creating a sealed class for UI commands to support uni-directional relationship between ViewModel
     * and View
     */
    sealed class ArticlesViewModelUiCommand {
        object ShowError : ArticlesViewModelUiCommand()
    }
}