package com.anaf.healthyliving.ui.articleshome

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anaf.healthyliving.databinding.ItemArticleBinding
import com.anaf.healthyliving.networking.models.Article


class ArticlesAdapter(val openDetailsCallback: (Article) -> Unit) :
    RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

    private val articles = mutableListOf<Article>()

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.article = article
            binding.root.setOnClickListener {
                openDetailsCallback.invoke(article)
            }
        }
    }

    /** Create new binding (invoked by the layout manager) **/
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = ItemArticleBinding.inflate(LayoutInflater.from(viewGroup.context))
        return ArticleViewHolder(view)
    }

    /** Binding data to view here **/
    override fun onBindViewHolder(viewHolder: ArticleViewHolder, position: Int) {
        viewHolder.bind(articles[position])
    }

    /** Controlling the number of elements in recycler by associating with data set size **/
    override fun getItemCount() = articles.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newList: List<Article>) {
        articles.clear()
        articles.addAll(newList)
        notifyDataSetChanged()
    }

}