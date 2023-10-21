package alphalabs.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.core.scope.Scope

abstract class BaseFragment<B : ViewDataBinding> : Fragment(), AndroidScopeComponent {
    override val scope: Scope by fragmentScope()
    lateinit var binding: B

    protected abstract fun getLayoutId(): Int
    protected abstract fun onView()
    protected abstract fun onCreateView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        onCreateView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.post { if (isActive()) onView() }
    }

    override fun onDestroy() {
        scope.close()
        super.onDestroy()
    }

    /**
     * true: Fragment Valid
     *
     * false: Fragment Illegal
     * */
    fun isActive(): Boolean {
        val activity = activity
        return activity != null && !activity.isFinishing && !activity.isDestroyed && isAdded
    }
}