package com.example.finalyearproject.ui.home

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalyearproject.GlideApp
import com.example.finalyearproject.R
import com.google.firebase.storage.StorageReference

class HomeRecyclerAdapter(var imageList: List<StorageReference>, var titleList: List<String>,
                          var subtitleList: List<String>, var dateList: List<String>,
                          val context: Context): RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.home_card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    override fun onBindViewHolder(holder: HomeRecyclerAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = titleList[position]
        holder.itemSubtitle.text = subtitleList[position]
        holder.itemDate.text = dateList[position]
        var placeholder : Int = 0
        when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {placeholder = R.drawable.ic_baseline_filter_vintage_100_white}
            Configuration.UI_MODE_NIGHT_NO -> {placeholder = R.drawable.ic_baseline_filter_vintage_100_black}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {placeholder = R.drawable.ic_baseline_filter_vintage_100_black}
        }
        GlideApp.with(context).load(imageList[position]).placeholder(placeholder).into(holder.itemImage)
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemSubtitle: TextView
        var itemDate: TextView

        init {
            itemImage = itemView.findViewById(R.id.homeCardViewImageView)
            itemTitle = itemView.findViewById(R.id.homeCardViewTitleTextView)
            itemSubtitle = itemView.findViewById(R.id.homeCardViewSubTextView)
            itemDate = itemView.findViewById(R.id.homeCardViewDateTextView)
        }
    }

}