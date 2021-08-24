package com.anaf.healthyliving.ui.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.anaf.healthyliving.R
import com.anaf.healthyliving.networking.models.Article
import com.anaf.healthyliving.ui.articleshome.ArticlesAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

/**
 * Using some custom binding adapters here to support observer pattern
 */

/**
 * Using Glide to load and cache images because it is open source time-proven library.
 * Also based on the performance assessment it is considered more performing than Picasso
 */
@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    Glide.with(view)
        .load(url)
        .placeholder(R.drawable.loading_placeholder)
        .error(R.drawable.loading_placeholder)
        .transform(RoundedCorners(15))
        .into(view)
}

@BindingAdapter("articles")
fun loadArticles(recycler: RecyclerView, articles: LiveData<List<Article>>?) {
    (recycler.adapter as? ArticlesAdapter)?.updateDataSet(articles?.value.orEmpty())
}