package android.pvt.softtecotest.recycler

import android.pvt.softtecotest.R
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PositionViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
    var itemPosition: ImageView = itemView.findViewById(R.id.itemPosition)

    fun bind(){
        itemPosition.setImageResource(R.drawable.position_item_inactive)
    }

}