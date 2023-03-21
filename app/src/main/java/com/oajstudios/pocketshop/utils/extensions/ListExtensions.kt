package com.oajstudios.pocketshop.utils.extensions

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

fun Spinner.create(@LayoutRes itemLayout: Int, @IdRes textViewId: Int, items: Array<String>,
                   onItemSelected: (String, Int) -> Unit = { _, _ -> }) {
    val aAdapter = ArrayAdapter(context, itemLayout, textViewId, items)
    adapter = aAdapter
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(items[position], position)
        }
    }
}

fun Spinner.create(@LayoutRes itemLayout: Int, @IdRes textViewId: Int, items: MutableList<String>,
                   onItemSelected: (String, Int) -> Unit = { _, _ -> }) {
    val aAdapter = ArrayAdapter(context, itemLayout, textViewId, items)
    adapter = aAdapter
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(items[position], position)
        }
    }
}

fun AutoCompleteTextView.create(@LayoutRes itemLayout: Int, @IdRes textViewId: Int, items: Array<String>,
                                onItemSelected: (String, Int) -> Unit = { _, _ -> }) {
    val adapter = ArrayAdapter(context, itemLayout, textViewId, items)
    setAdapter(adapter)
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(items[position], position)
        }
    }
}

fun AutoCompleteTextView.create(@LayoutRes itemLayout: Int, @IdRes textViewId: Int, items: MutableList<String>,
                                onItemSelected: (String, Int) -> Unit = { _, _ -> }) {
    val adapter = ArrayAdapter(context, itemLayout, textViewId, items)
    setAdapter(adapter)
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(items[position], position)
        }
    }
}

fun <T> RecyclerView.create(itemSize: Int = 0, itemLayout: Int, items: Array<T>, layoutMgr: RecyclerView.LayoutManager,
                            onBindView: View.(T, Int) -> Unit,
                            itemClick: (T, Int) -> Unit = { _, _ -> },
                            itemLongClick: (T, Int) -> Unit = { _, _ -> },
                            onScrollTop: () -> Unit = {},
                            onScrollBottom: () -> Unit = {}) {
    adapter = RecyclerAdapter(itemLayout, items, itemSize, onBindView, itemClick, itemLongClick)
    layoutManager = layoutMgr
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            when {
                dy < 0 -> onScrollTop()
                dy > 0 -> onScrollBottom()
            }
        }
    })
}

fun <T> RecyclerView.create(itemSize: Int = 0, itemLayout: Int, items: MutableList<T>, layoutMgr: RecyclerView.LayoutManager,
                            onBindView: View.(item: T, position: Int) -> Unit,
                            itemClick: (item: T, position: Int) -> Unit = { _, _ -> },
                            itemLongClick: (item: T, position: Int) -> Unit = { _, _ -> },
                            onScrollTop: () -> Unit = {},
                            onScrollBottom: () -> Unit = {}) {
    adapter = RecyclerAdapter(itemSize, itemLayout, items, onBindView, itemClick, itemLongClick)
    layoutManager = layoutMgr
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            when {
                dy < 0 -> onScrollTop()
                dy > 0 -> onScrollBottom()
            }
        }
    })
}

fun ViewPager.onPageSelected(onPageSelected: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            onPageSelected(position)
        }

    })
}