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

---
### StorageExt.kt
    mmkv 实现的相关存储封装


---
### DataBindingExt.kt
    封装列表adapter适配器的ViewBinding扩展方式

```
SimpleViewHolder(parent.inflate(R.layout.layout_user_item))
```

---

### NullableDelegate
    声明不可空对象，但是可以释放的代理实现

```
    /**
     * 属性代理不可为空的可释放类型，简便主程序业务代码的实现
     * @see(NullableDelegate)
     */
    protected var viewBinding: T by releasableNotNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflater(layoutInflater)
        setContentView(viewBinding.root)
        initialize()
        initListener()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        // viewBinding 为不可空类型，在DonDestroy中通过 release 释放属性代理中get()中value的值
        ::viewBinding.release()
    }
```

---

### Repository注解
    创建并声明 Repository 实现类，模拟注入实现，后续使用直接在相关model中通过属性代理 by repositoryOf 

- 通过创建Repository时继承父类AbsRepository类，并在类中添加注解 @Repository
- 项目重新编译
- 会在项目中创建app/src/main/assets/repository.txt文件
- 文件中内容为Repository实现类的全类名
- 注意: 如果在非AbsRepository的子类中声明该注解，会发生异常崩溃提醒

```
/**
 * 创建 Repository 业务实现类
 */
@Repository
class UserRepository : AbsRepository(){
    init {
        println("UserRepository init ....")
    }
    fun seeHello() {
        println("hello")
    }
}


/**
 * 使用
 */
class MainViewModel : ViewModel() {
    // 通过属性代理获取当前Repository对象，模拟注入事件
    private val userRepository: UserRepository by repositoryOf()

    fun userRepositorySee() {
        userRepository.seeHello()
    }

}
```
