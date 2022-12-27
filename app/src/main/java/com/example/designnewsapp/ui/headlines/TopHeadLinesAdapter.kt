package com.example.designnewsapp.ui.headlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.designnewsapp.data.model.Article
import com.example.designnewsapp.databinding.ListItemBinding
import com.example.designnewsapp.utils.getDateTimeDifference

class TopHeadLinesAdapter :
    PagingDataAdapter<Article, TopHeadLinesAdapter.HeadlinesViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlinesViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeadlinesViewHolder(binding)
    }


    inner class HeadlinesViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.apply {
                newsTitle.text = article.title
                source.text = article.source.name

                val date = getDateTimeDifference(article.publishedAt.toString())

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
                        transformations(RoundedCornersTransformation(25f))
                    }
                }
            }
        }

    }

    override fun onBindViewHolder(holder: HeadlinesViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

}