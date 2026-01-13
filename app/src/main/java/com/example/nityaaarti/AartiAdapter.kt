package com.example.nityaaarti

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AartiAdapter(
    private val items: List<AartiListItem>,
    private val savedAartis: List<String>,
    private val onAddClick: (String) -> Unit,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == AartiListItem.TYPE_HEADER) {
            val view = inflater.inflate(R.layout.item_category_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_aarti_row, parent, false)
            AartiViewHolder(view)
        }
    }

    override fun getItemCount(): Int = items.size

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvCategoryTitle)
    }

    class AartiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvAartiName)
        val btnAdd: ImageView = itemView.findViewById(R.id.btnAdd)
        val imgDrag: ImageView = itemView.findViewById(R.id.imgDragHandle)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is HeaderViewHolder) {
            holder.tvTitle.text = item.name
        } else if (holder is AartiViewHolder) {
            holder.tvName.text = item.name

            holder.imgDrag.visibility = View.GONE

            if (savedAartis.contains(item.name)) {
                holder.btnAdd.visibility = View.GONE
            } else {
                holder.btnAdd.visibility = View.VISIBLE

                holder.btnAdd.setOnClickListener {
                    onAddClick(item.name)
                    holder.btnAdd.visibility = View.GONE
                }
            }

            holder.itemView.setOnClickListener {
                onItemClick(item.name)
            }
        }
    }
}