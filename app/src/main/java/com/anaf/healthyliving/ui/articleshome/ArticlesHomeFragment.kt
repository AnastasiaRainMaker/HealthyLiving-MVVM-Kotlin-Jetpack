package com.anaf.healthyliving.ui.articleshome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.anaf.healthyliving.databinding.ArticlesHomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.anaf.healthyliving.R
import com.anaf.healthyliving.networking.models.Article

@AndroidEntryPoint
class ArticlesHomeFragment : Fragment() {

    private val viewModel: ArticlesHomeViewModel by viewModels()

    /**
     * Using databinding to support observer pattern
     * Making it nullable to prevent memory leaks and nullifying it onDestroyView
     */
    private var binding: ArticlesHomeFragmentBinding? = null

    /**
     * Using this field to track showing dialog to dismiss it if Fragment is killed to prevent memory leak
     */
    private var showingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ArticlesHomeFragmentBinding.inflate(inflater).run {
            binding = this
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.articlesRecycler.layoutManager = LinearLayoutManager(context)
            it.articlesRecycler.adapter =
                ArticlesAdapter { article: Article -> navigateToDetailsPage(article) }
            subscribeToViewCommands()
            // preventing double request here since ViewModel persists results through config changes
            if (viewModel.articles.value.isNullOrEmpty()) {
                viewModel.loadArticles()
            }
        }
    }

    private fun navigateToDetailsPage(article: Article) {
        findNavController().navigate(
            ArticlesHomeFragmentDirections.fromListToDetails(article)
        )
    }

    /**
     * Using reactive pattern here to observe commands from ViewModel
     */
    private fun subscribeToViewCommands() {
        viewModel.uiCommandLiveData.observe(viewLifecycleOwner, {
            when (it) {
                ArticlesHomeViewModel.ArticlesViewModelUiCommand.ShowError -> showErrorDialog()
            }
        })
    }

    /**
     * Reusing dialog instance
     */
    private fun showErrorDialog() {
        showingDialog?.let {
            if (!it.isShowing) {
                it.show()
            }
        } ?: createAlertDialog()
        showingDialog?.show()
    }

    private fun createAlertDialog() {
        context?.let {
            val builder: AlertDialog.Builder = AlertDialog.Builder(it)
            builder.setMessage(it.getString(R.string.failed_to_load_articles))
            builder.setCancelable(true)
            builder.setPositiveButton(
                it.getString(R.string.ok)
            ) { dialog, _ -> dialog.dismiss() }
            builder.create().run {
                showingDialog = this
            }
        }
    }

    /**
     * Releasing resources here to avoid memory leaks
     */
    override fun onDestroyView() {
        binding = null
        showingDialog?.dismiss()
        showingDialog = null
        super.onDestroyView()
    }

}
