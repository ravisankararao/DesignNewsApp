package com.example.designnewsapp.ui.search

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.designnewsapp.data.model.Article
import com.example.designnewsapp.databinding.SearchListItemBinding
import com.example.designnewsapp.utils.getDateTimeDifference

class NewsAdapter :
    PagingDataAdapter<Article, NewsAdapter.NewsViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    inner class NewsViewHolder(private val binding: SearchListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.apply {

                val date = getDateTimeDifference(article.publishedAt.toString())

                title.text = article.title
                sourceName.text = article.source.name

                if (date.days.toInt() == 0) {
                    timePublished.text = "${date.hours} hours ago"
                }

                if (date.hours.toInt() == 0) {
                    timePublished.text = "${date.minutes} minutes ago"
                } else if (date.minutes.toInt() == 0) {
                    timePublished.text = "${date.seconds} seconds ago"
                }

                val imgUrl = article.urlToImage

                imgUrl?.let {
                    val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                    newsImage.load(imgUri) {
                        transformations(RoundedCornersTransformation(20f))
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            SearchListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

}

