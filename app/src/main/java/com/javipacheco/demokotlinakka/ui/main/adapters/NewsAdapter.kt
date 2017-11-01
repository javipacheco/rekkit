package com.javipacheco.demokotlinakka.ui.main.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.javipacheco.demokotlinakka.R
import com.javipacheco.demokotlinakka.models.Events
import kategory.ListKW
import kotlinx.android.synthetic.main.news_row.view.*

class NewsAdapter(val items: ListKW<Events.RedditNewsDataEvent>): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun populate(item: Events.RedditNewsDataEvent) {
            itemView.news_title.setText(item.title)
            itemView.news_author.setText(item.author)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.populate(items.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(
                parent?.getContext()).inflate(R.layout.news_row,
                parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

}