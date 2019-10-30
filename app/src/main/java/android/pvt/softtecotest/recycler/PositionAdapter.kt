package android.pvt.softtecotest.recycler

import android.pvt.softtecotest.R
import android.pvt.softtecotest.entity.Post
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_position.view.*

class PositionAdapter(private val listener: OnClickListener) :
    RecyclerView.Adapter<PositionViewHolder>() {

    private var postlist: List<Post> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_position, parent, false)
        val holder = PositionViewHolder(view)
        holder.itemView.setOnClickListener {
            listener.onItemCLick(holder.layoutPosition, view)
            Log.e("POSLAY", holder.layoutPosition.toString())
            Log.e("POSAD", holder.adapterPosition.toString())
        }
        return holder
    }

    fun setPostList(postlist: List<Post>) {
        this.postlist = postlist
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (postlist.size % 6 == 0) {
            postlist.size / 6
        } else postlist.size / 6 + 1
    }

    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
        if (position == 0) {
            holder.itemView.itemPosition.setImageResource(R.drawable.position_item)
        } else holder.bind()
    }

    interface OnClickListener {
        fun onItemCLick(position: Int, v: View)
    }
}