package com.oajstudios.pocketshop.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.utils.extensions.changeTextSecondaryColor

class SpinnerAdapter(
    ctx: Context?,
    texts: ArrayList<String>,
) :
    ArrayAdapter<String>(ctx!!, android.R.layout.simple_spinner_item, texts as List<String?>) {
    @SuppressLint("ViewHolder")
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
    ): View {
        val row: View =
            LayoutInflater.from(parent.context).inflate(R.layout.spinner_items, parent, false)
        val textView = row.findViewById<View>(R.id.tvItem) as TextView
        textView.text = getItem(position)
        textView.setPadding(4, 20, 4, 8)
        textView.changeTextSecondaryColor()
        return row
    }

}