package com.oajstudios.pocketshop.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oajstudios.pocketshop.utils.extensions.inflate


class BaseAdapter<T>(
    private val layout: Int,
    val onBind: (view: View, item: T, position: Int) -> Unit,
) : RecyclerView.Adapter<BaseAdapter<T>.ViewHolder<T>>() {
    var items = ArrayList<T>()
    var onItemClick: ((pos: Int, view: View, item: T) -> Unit)? = null
    var size: Int? = null

    fun addItem(item: T) {
        this.items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun addMoreItems(aList: java.util.ArrayList<T>) {
        items.addAll(aList)
        setModelSize(itemCount + items.size)
        notifyDataSetChanged()
    }

    fun addNewItem(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun getModel(): java.util.ArrayList<T> {
        return items
    }

    fun addItems(items: List<T>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun removeItem(pos: Int) {
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }

    fun setModelSize(aSize: Int) {
        size = aSize
        notifyDataSetChanged()
    }

    inner class ViewHolder<T>(
        private val view: View,
        val onBind: (view: View, item: T, position: Int) -> Unit,
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        override fun onClick(p0: View?) {
            onItemClick?.invoke(adapterPosition, p0!!, items[adapterPosition])
        }

        fun bind(item: T, position: Int) {
            view.setOnClickListener(this)
            onBind(view, item, position)
        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder<T> {
        return ViewHolder(p0.inflate(layout), onBind)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder<T>, pos: Int) {
        holder.bind(items[pos], pos)
    }

}
