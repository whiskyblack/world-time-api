package alphalabs.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseView<T : ViewDataBinding>(context: Context, attrs: AttributeSet?) :
    FrameLayout(context, attrs) {

    private var _binding: T? = null
    protected val viewBinding get() = _binding!!
    protected var isDestroy = false

    init {
        init()
    }

    private fun init() {
        val layoutInflater = LayoutInflater.from(context)
        _binding = DataBindingUtil.inflate(layoutInflater, runLayout(), this, false)
        addView(viewBinding.root)
        runUI()
    }

    @LayoutRes
    protected abstract fun runLayout(): Int

    protected abstract fun runUI()

    override fun onDetachedFromWindow() {
        _binding = null
        isDestroy = true
        super.onDetachedFromWindow()
    }

    fun isAttached(): Boolean {
        return _binding != null && isAttachedToWindow
    }

}