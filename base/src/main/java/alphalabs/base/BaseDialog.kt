package alphalabs.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDialog<VB : ViewDataBinding>(context: Context) : Dialog(context) {
    lateinit var binding: VB
    abstract fun getLayoutId(): Int

    abstract fun onView(binding: VB)

    private fun initView(context: Context) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutId(), null, false)
        setContentView(binding.root)
        binding.root.post { onView(binding) }
    }

    private fun initWindow() {
        if (window != null) {
            window!!.requestFeature(Window.FEATURE_NO_TITLE)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun show() {
        super.show()
        val windowWidth = context.resources.displayMetrics.widthPixels * 0.85f
        window!!.setLayout(windowWidth.toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
    }

    init {
        initWindow()
        initView(context)
    }
}