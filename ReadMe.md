# 开发工具(Kotlin)
---
## kotlin快速开发工具包

---


### 页面基类
- **SuperActivity** 屏幕适配
   + screenWidth() : Int
      + 方法返回通过width宽度进行适配，从重写并返回对应设计中的宽度
   + getResources(): Resources
      + 获取设置了对应设计图宽度后的Resource资源。 
      
```
override fun getResources(): Resources {
        return AdaptScreenUtils.adaptWidth(super.getResources(), screenWidth())
    }
    //重写此处数值，按照设计图标注设置对应pt后，会自动做相关转换
    open fun screenWidth(): Int = 1920
```

---

### 列表适配器
- TypeAdapter 列表适配器
   + createHolder holder创建(BaseViewHolder)
   + itemViewType itemViewType类型返回
   + compareItem Diff 校验对象比对
   + contentsSame Diff 校验内容比对

---

- BaseViewHolder 基类ViewHolder
   + itemBind 数据绑定

---
- DiffCallback 数据比对
   + compareItem 对象比对
   + contentsSame 内容比对

---
- 注解辅助类
   + ==@Model==
      + 实体类.toMap() : 提供实体转Map
      + Map.toInfo() : 提供Map转实体
      + 实体类.contrast() : 提供实体类参数比对
         + ==@FieldIngore== : 忽略某些提供的构造函数内的字段比对


```
private val userAdapter by lazy {
        TypeAdapter(
            createHolder = { parent: ViewGroup, viewType: Int ->
                when (viewType) {
                    1 -> SimpleViewHolder(
                        DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            R.layout.layout_user_item, parent, false
                        )
                    )
                    else -> StudentsViewHolder(
                        DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            R.layout.layout_students_item,
                            parent,
                            false
                        )
                    )
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
```

---

### View.clickAnim 
###### 提供view点击按下缩小，松开放大动画以及连续点击过滤(300ms过滤时长)
