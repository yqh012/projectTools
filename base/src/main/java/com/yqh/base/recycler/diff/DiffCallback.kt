package com.yqh.base.recycler.diff

import androidx.recyclerview.widget.DiffUtil

class DiffCallback<T>(
    private val compareItem: (T, T) -> Boolean,
    private val contentsSame: (T, T) -> Boolean
) :
    DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = compareItem.invoke(oldItem, newItem)
    override fun areContentsTheSame(oldItem: T, newItem: T) = contentsSame(oldItem, newItem)
}