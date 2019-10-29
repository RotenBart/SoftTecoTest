package android.pvt.softtecotest.recycler

import android.pvt.softtecotest.R
import android.pvt.softtecotest.entity.Post
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val postId = itemView.findViewById<TextView>(R.id.postId)
    private val postTitle = itemView.findViewById<TextView>(R.id.postTitle)

    fun bind(post: Post) {
        postId.text = post.id.toString()
        postTitle.text = post.title
    }

    fun setItemWidth(itemWidth: Int) {
        itemView.layoutParams = RecyclerView.LayoutParams(itemWidth, RecyclerView.LayoutParams.MATCH_PARENT)
    }
}