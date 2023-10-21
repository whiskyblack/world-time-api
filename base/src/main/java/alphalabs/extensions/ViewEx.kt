package alphalabs.extensions

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import alphalabs.HAS_TRANSITION

fun View.click(result: ((View) -> Unit)) {
    setOnClickListener {
        isEnabled = false
        result(this)
        kotlin.runCatching { postDelayed({ isEnabled = true }, 300) }
    }
}

fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.switchToView(view: View) {
    zoomOut {
        view.zoomIn {  }
    }
}

fun View.skewIn(duration: Long = 250, endCallback: (() -> Unit)? = null) {
    YoYo.with(Techniques.FlipInX)
        .duration(duration)
        .onStart {
            isEnabled = false
            visibility = View.VISIBLE
        }.onEnd {
            isEnabled = true
            endCallback?.invoke()
        }.playOn(this)
}

fun View.skewOut(duration: Long = 250, endCallback: (() -> Unit)? = null) {
    YoYo.with(Techniques.FlipOutX)
        .duration(duration)
        .onStart { isEnabled = false }
        .onEnd {
            isEnabled = true
            visibility = View.GONE
            endCallback?.invoke()
        }.playOn(this)
}

fun View.fadeInUp(duration: Long = 250, endCallback: (() -> Unit)? = null) {
    YoYo.with(Techniques.FadeInUp)
        .duration(duration)
        .onStart {
            isEnabled = false
            visibility = View.VISIBLE
        }.onEnd {
            isEnabled = true
            endCallback?.invoke()
        }.playOn(this)
}

fun View.fadeOutDown(duration: Long = 250, endCallback: (() -> Unit)? = null) {
    YoYo.with(Techniques.FadeOutDown)
        .duration(duration)
        .onStart { isEnabled = false }
        .onEnd {
            isEnabled = true
            visibility = View.GONE
            endCallback?.invoke()
        }.playOn(this)
}

fun View.lock(duration: Long = 500) {
    isEnabled = false
    Handler(Looper.getMainLooper()).postDelayed({
        isEnabled = true
    }, duration)
}

fun View.zoomIn(duration: Long = 250, endCallback: (() -> Unit)? = null) {
    YoYo.with(Techniques.ZoomIn)
        .duration(duration)
        .onStart {
            isEnabled = false
            visibility = View.VISIBLE
        }.onEnd {
            isEnabled = true
            endCallback?.invoke()
        }.playOn(this)
}

fun View.zoomOut(duration: Long = 250, endCallback: (() -> Unit)? = null) {
    YoYo.with(Techniques.ZoomOut)
        .duration(duration)
        .onStart { isEnabled = false }
        .onEnd {
            isEnabled = true
            visibility = View.GONE
            endCallback?.invoke()
        }.playOn(this)
}

fun View.fadeIn(duration: Long = 250, endCallback: (() -> Unit)? = null) {
    YoYo.with(Techniques.FadeIn)
        .duration(duration)
        .onStart {
            isEnabled = false
            visibility = View.VISIBLE
        }.onEnd {
            isEnabled = true
            endCallback?.invoke()
        }.playOn(this)
}

fun View.fadeOut(duration: Long = 250, visibility: Int = View.GONE, endCallback: (() -> Unit)? = null) {
    YoYo.with(Techniques.FadeOut)
        .duration(duration)
        .onStart { isEnabled = false }
        .onEnd {
            isEnabled = true
            this.visibility = visibility
            endCallback?.invoke()
        }.playOn(this)
}

fun View.changeBackgroundColor(color: Int) {
    background?.let {
        it.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC)
    }
}

fun beginDelayTransition(view: ViewGroup) {
    val transition = ChangeBounds()
    transition.addListener(object : Transition.TransitionListener {
        override fun onTransitionStart(transition: Transition) {
            view.tag = HAS_TRANSITION
        }

        override fun onTransitionEnd(transition: Transition) {
            view.tag = null
        }

        override fun onTransitionCancel(transition: Transition) {

        }

        override fun onTransitionPause(transition: Transition) {

        }

        override fun onTransitionResume(transition: Transition) {

        }

    })
    TransitionManager.beginDelayedTransition(view, transition)
}