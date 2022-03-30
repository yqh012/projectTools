package com.yqh.tv.screen.tools.activity

import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import com.yqh.base.ext.inflate
import com.yqh.base.recycler.adapter.TypeAdapter
import com.yqh.base.recycler.holder.BaseViewHolder
import com.yqh.tv.screen.tools.BR
import com.yqh.tv.screen.tools.R
import com.yqh.tv.screen.tools.base.BaseActivity
import com.yqh.tv.screen.tools.databinding.ActivityMainBinding
import com.yqh.tv.screen.tools.databinding.LayoutStudentsItemBinding
import com.yqh.tv.screen.tools.databinding.LayoutUserItemBinding
import com.yqh.tv.screen.tools.domain.UserInfo
import com.yqh.tv.screen.tools.domain.contrast
import com.yqh.tv.screen.tools.viewmodel.MainViewModel
import pt2px

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    val model: MainViewModel by viewModels()

    class SimpleViewHolder(viewBinding: LayoutUserItemBinding) :
        BaseViewHolder<LayoutUserItemBinding, UserInfo>(viewBinding) {

        init {
            viewBinding.root.let { root ->
                root.setOnClickListener {
                    (root.getTag(R.id.recycler_item_info) as? UserInfo)?.apply {
                        Toast.makeText(root.context, name, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        override fun itemBind(item: UserInfo) {
            viewBinding.root.setTag(R.id.recycler_item_info, item)
            viewBinding.setVariable(BR.user, item)
            viewBinding.executePendingBindings()
        }
    }

    class StudentsViewHolder(viewBinding: LayoutStudentsItemBinding) :
        BaseViewHolder<LayoutStudentsItemBinding, UserInfo>(viewBinding) {
        init {
            viewBinding.root.let { root ->
                root.setOnClickListener {
                    (root.getTag(R.id.recycler_item_info) as? UserInfo)?.apply {
                        Toast.makeText(root.context, name, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        override fun itemBind(item: UserInfo) {
            viewBinding.root.setTag(R.id.recycler_item_info, item)
            viewBinding.setVariable(BR.user, item)
            viewBinding.executePendingBindings()
        }
    }

    private val userAdapter by lazy {
        TypeAdapter(
            createHolder = { parent: ViewGroup, viewType: Int ->
                when (viewType) {
                    1 -> SimpleViewHolder(parent.inflate(R.layout.layout_user_item))
                    else -> StudentsViewHolder(parent.inflate(R.layout.layout_students_item))
                }
            },
            itemViewType = { position: Int, item: UserInfo ->
                if (item.age % 2 == 0) 1 else 2
            },
            compareItem = { old: UserInfo, new: UserInfo ->
                old.contrast(new)
            },
            contentsSame = { old: UserInfo, new: UserInfo ->
                old.contrast(new)
            }
        )
    }

    override fun initialize() {
        viewBinding.listView.apply {
            setNumColumns(1)
            setItemSpacing(pt2px(5f))
            adapter = userAdapter
        }

        model.userRepositorySee()
    }

    override fun initListener() {

    }

    override fun initData() {
        var userLists = mutableListOf<UserInfo>()
        for (i in 1..20) {
            userLists.add(UserInfo(i, "小明 $i", i, "男"))
        }
        userAdapter.submitList(userLists)

        //获取列表集合
        Toast.makeText(this, "${userAdapter.currentList.size}", Toast.LENGTH_SHORT).show()
    }

    override fun showLoading(tag: String?) {
//        TODO("Not yet implemented")
    }

    override fun dismissLoading(tag: String?) {
//        TODO("Not yet implemented")
    }

}