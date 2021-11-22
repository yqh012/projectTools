package com.yqh.base.recycler.holder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<VB, INFO>(val viewBinding: VB) :
    RecyclerView.ViewHolder(viewBinding.root) where VB : ViewBinding {
    abstract fun itemBind(item: INFO)
}