package android.pvt.softtecotest.recycler

import android.pvt.softtecotest.R
import android.pvt.softtecotest.entity.Post
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PostGridAdapter(private val postList: List<Post>, private val itemWidth: Int, private val listener: OnClickListener) :
    RecyclerView.Adapter<PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        val holder = PostViewHolder(view)

        holder.itemView.setOnClickListener {
            listener.onItemClick(postList[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postList[position])
        holder.setItemWidth(itemWidth)
    }

    interface OnClickListener {
        fun onItemClick(post: Post)
    }

}