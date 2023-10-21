package alphalabs.base

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import org.koin.android.ext.koin.androidContext
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.scope.LifecycleScopeDelegate
import org.koin.androidx.scope.activityScope
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.scope.Scope
import alphalabs.BaseApplication
import alphalabs.ads.appopen.AdmobAppOpen
import alphalabs.ads.appopen.AppOpenAdsListener

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity(), AndroidScopeComponent {

    override val scope: Scope by contextAwareActivityScope()

    lateinit var binding: B
    private var toast: Toast? = null

    protected abstract fun getLayoutId(): Int
    protected abstract fun onCreate()
    protected abstract fun onView()

    open fun onAppStart(appOpenManager: AdmobAppOpen?) {}

    open fun onAppOpenShowed() {}

    open fun onAppOpenDismiss() {}

    open fun onAppOpenFailedToShow(admobAppOpen: AdmobAppOpen?) {}

    open fun setStatusStyle(statusBarColor: Int = Color.BLACK, lightMode: Boolean = true) {
        window.statusBarColor = statusBarColor
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            lightMode
    }

    open fun hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    open fun consumeSystemBars(callback: ((statusBarHeight: Int, bottomBarHeight: Int) -> Unit)? = null) {
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val bottomBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            if (binding.root is ViewGroup && binding.root.paddingTop + binding.root.paddingBottom != statusBarHeight + bottomBarHeight) {
                binding.root.setPadding(0, statusBarHeight, 0, bottomBarHeight)
            }
            callback?.invoke(statusBarHeight, bottomBarHeight)
            WindowInsetsCompat.CONSUMED
        }
    }

    open fun getSystemBarHeight(callback: ((statusBarHeight: Int, bottomBarHeight: Int) -> Unit)? = null) {
        var statusBarHeight = 0
        var bottomBarHeight = 0
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
            if (statusBarHeight + bottomBarHeight == 0) {
                statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                bottomBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                callback?.invoke(statusBarHeight, bottomBarHeight)
            }
            insets
        }
    }

    var inForceGround = false
    var admobAppOpen: AdmobAppOpen? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            admobAppOpen =
                (applicationContext as BaseApplication).admobAppOpen
        } catch (e: Exception) {
        }

        binding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), null, false)
        onCreate()
        setContentView(binding.root)
        binding.lifecycleOwner = this
        setupKoinFragmentFactory(scope)
        binding.root.post { if (isActive()) onView() }
    }

    override fun onStart() {
        admobAppOpen?.setAppOpenAdsListener(object :
            AppOpenAdsListener() {
            override fun onAppStart() {
                this@BaseActivity.onAppStart(admobAppOpen)
            }

            override fun onAppOpenShowed() {
                this@BaseActivity.onAppOpenShowed()
            }

            override fun onAppOpenDismiss() {
                this@BaseActivity.onAppOpenDismiss()
            }

            override fun onAppOpenFailedToShow() {
                this@BaseActivity.onAppOpenFailedToShow(admobAppOpen)
            }
        })
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        inForceGround = true
    }

    override fun onPause() {
        inForceGround = false
        super.onPause()
    }

    override fun onDestroy() {
        if (::binding.isInitialized) binding.unbind()
        admobAppOpen?.setAppOpenAdsListener(null)
        scope.close()
        super.onDestroy()
    }

    /**
     * true: Activity run
     *
     * false: Activity destroy
     * */
    fun isActive(): Boolean {
        return !isFinishing && !isDestroyed
    }

    fun showToast(message: String) {
        toast?.cancel()
        toast = null
        toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast?.setText(message)
        toast?.show()
    }

    private fun ComponentActivity.getKoin(): Koin {
        return if (this is KoinComponent) {
            getKoin()
        } else {
            GlobalContext.getOrNull() ?: startKoin {
                androidContext(applicationContext)
                modules((applicationContext as BaseApplication).moduleList)
            }.koin
        }
    }

    private fun ComponentActivity.contextAwareActivityScope() = runCatching {
        LifecycleScopeDelegate<Activity>(
            lifecycleOwner = this,
            koin = getKoin()
        )
    }.getOrElse { activityScope() }
}