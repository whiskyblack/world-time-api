package alphalabs.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BasePopupDialog<VB : ViewDataBinding>(val context: Context) : PopupWindow(context) {
    lateinit var binding: VB
    abstract fun getLayoutId(): Int

    abstract fun onView(binding: VB)

    private fun initView(context: Context) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutId(), null, false)
        contentView = binding.root
        binding.root.post { onView(binding) }
    }

    init {
        initWindow()
        initView(context)
    }

    private fun initWindow() {
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        height = heightCustom()
        width = widthCustom(context)
    }

    open fun widthCustom(context: Context): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    open fun heightCustom(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }
}