package com.anaf.healthyliving.ui.articlesdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import com.anaf.healthyliving.R
import com.anaf.healthyliving.databinding.ArticlesDetailsFragmentBinding

@AndroidEntryPoint
class ArticlesDetailsFragment : Fragment() {

    private val viewModel: ArticlesDetailsViewModel by viewModels()

    /**
     * Using databinding to support observer pattern
     * Making it nullable to prevent memory leaks and nullifying it onDestroyView
     */
    private var binding: ArticlesDetailsFragmentBinding? = null

    /**
     * Using this field to track showing dialog to dismiss it if Fragment is killed to prevent memory leak
     */
    private var showingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ArticlesDetailsFragmentBinding.inflate(inflater).run {
            binding = this
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.article = arguments?.let { ArticlesDetailsFragmentArgs.fromBundle(it) }?.article
        binding?.run {
            toolbar.navigationIcon =
                context?.let {
                    AppCompatResources.getDrawable(
                        it,
                        androidx.appcompat.R.drawable.abc_ic_ab_back_material
                    )
                }
            toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
            viewModel = this@ArticlesDetailsFragment.viewModel
            subscribeToViewCommands()
        }
    }

    /**
     * Using reactive pattern here to observe commands from ViewModel
     */
    private fun subscribeToViewCommands() {
        viewModel.uiCommandLiveData.observe(viewLifecycleOwner, {
            when (it) {
                ArticlesDetailsViewModel.ArticlesViewModelUiCommand.ShowError -> showErrorDialog()
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
