package com.anaf.healthyliving.ui.articlesdetails

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
class ArticlesDetailsViewModel @Inject constructor() : ViewModel() {

    /**
     * Using command livedata to communicate to View uni-directionally
     */
    private val _uiCommandLiveData = MutableLiveData<ArticlesViewModelUiCommand>()
    val uiCommandLiveData: LiveData<ArticlesViewModelUiCommand> = _uiCommandLiveData

    /**
     * Using observer pattern thu databinding to control progress state
     */
    private val _isProgressBarVisible = MutableLiveData<Boolean>(true)
    val isProgressBarVisible: LiveData<Boolean> = _isProgressBarVisible

    var article: Article? = null
        set(value) {
            if (value != null) {
                _isProgressBarVisible.value = false
            }
            field = value
        }

    /**
     * Creating a sealed class for UI commands to support uni-directional relationship between ViewModel
     * and View
     */
    sealed class ArticlesViewModelUiCommand {
        object ShowError : ArticlesViewModelUiCommand()
    }
}