
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.ScreenUtils

/**
 * 获取屏幕宽度
 */
fun getScreenWidth() = ScreenUtils.getScreenWidth()

/**
 * 获取屏幕高度
 */
fun getScreenHeight() = ScreenUtils.getScreenHeight()

/**
 * 适配 pt 转 px
 */
fun pt2px(pt: Float) = AdaptScreenUtils.pt2Px(pt)

/**
 * 适配 px 转 pt
 */
fun px2pt(px: Float) = AdaptScreenUtils.px2Pt(px)
