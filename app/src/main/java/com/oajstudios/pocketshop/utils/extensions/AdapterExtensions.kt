package com.oajstudios.pocketshop.utils.extensions

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

internal class RecyclerAdapter<T> : RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    @LayoutRes
    private val itemLayout: Int
    private var itemSize: Int = 0
    private var items: Array<T>? = null
    private var itemsList: MutableList<T>? = null
    private val onBindView: View.(T, Int) -> Unit
    private val itemClick: (T, Int) -> Unit
    private val itemLongClick: (T, Int) -> Unit

    constructor(itemLayout: Int, items: Array<T>, itemSize: Int = 0, onBindView: View.(T, Int) -> Unit, itemClick: (T, Int) -> Unit = { _, _ -> }, itemLongClick: (T, Int) -> Unit = { _, _ -> }) {
        this.itemLayout = itemLayout
        this.items = items
        this.itemSize = itemSize
        this.onBindView = onBindView
        this.itemClick = itemClick
        this.itemLongClick = itemLongClick
    }

    constructor(itemSize: Int = 0, itemLayout: Int, items: MutableList<T>, onBindView: View.(T, Int) -> Unit, itemClick: (T, Int) -> Unit = { _, _ -> }, itemLongClick: (T, Int) -> Unit = { _, _ -> }) {
        this.itemLayout = itemLayout
        this.itemsList = items
        this.itemSize = itemSize
        this.onBindView = onBindView
        this.itemClick = itemClick
        this.itemLongClick = itemLongClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent inflate itemLayout)

    override fun getItemCount(): Int {
        return itemSize
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = if (items == null) {
            itemsList!![position]
        } else {
            items!![position]
        }

        holder.itemView.setOnClickListener { itemClick(item, position) }
        holder.itemView.setOnLongClickListener { itemLongClick(item, position);true }
        onBindView(holder.itemView, item, position)
    }

    internal class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!)
}

