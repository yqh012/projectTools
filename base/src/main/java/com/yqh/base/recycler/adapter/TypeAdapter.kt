package com.yqh.base.recycler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.yqh.base.recycler.diff.DiffCallback
import com.yqh.base.recycler.holder.BaseViewHolder

class TypeAdapter<T>(
    private val createHolder: (ViewGroup, Int) -> BaseViewHolder<*, T>,
    private val itemViewType: (Int, T) -> Int = { _, _ -> -1 },
    compareItem: (T, T) -> Boolean,
    contentsSame: (T, T) -> Boolean
) :
    ListAdapter<T, BaseViewHolder<*, T>>(DiffCallback<T>(compareItem, contentsSame)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, T> {
        return createHolder.invoke(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, T>, position: Int) {
        holder.itemBind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return itemViewType.invoke(position, getItem(position))
    }

    override fun submitList(list: MutableList<T>?) {
        val newDataLists = mutableListOf<T>()
        if (list.isNullOrEmpty()) return
        newDataLists.addAll(list)
        super.submitList(newDataLists)
    }
}