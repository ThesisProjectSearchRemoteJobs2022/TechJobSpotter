package com.rogergcc.techjobspotter.ui.utils.extensions


/**
 * Created on enero.
 * year 2025 .
 */
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.TranslateAnimation
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.rogergcc.techjobspotter.R
import java.util.concurrent.atomic.AtomicBoolean

fun View.showView() {
    this.visibility = View.VISIBLE
}

fun View.hideView() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.onThrottledClick(
    throttleDelay: Long = 500L,
    onClick: (View) -> Unit,
) {
    setOnClickListener {
        onClick(this)
        isClickable = false
        postDelayed({ isClickable = true }, throttleDelay)
    }
}

fun View.setOnSingleClickListener(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}

class OnSingleClickListener(
    private val clickListener: View.OnClickListener,
    private val intervalMs: Long = 1000,
) : View.OnClickListener {
    private var canClick = AtomicBoolean(true)

    override fun onClick(v: View?) {
        if (canClick.getAndSet(false)) {
            v?.run {
                postDelayed({
                    canClick.set(true)
                }, intervalMs)
                clickListener.onClick(v)
            }
        }
    }
}


fun View.toggleVisibility(): View {
    visibility = if (visibility == View.VISIBLE) {
        View.GONE
    } else {
        View.VISIBLE
    }
    return this
}

fun View.showCustomSnackbar(message: String,
                            duration: Int = Snackbar.LENGTH_SHORT,
                            topMargin: Int = 200) {
    val snackbar = Snackbar.make(this, message, duration)
    val layoutParams = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
    layoutParams.setMargins(50, 0, 50, topMargin)
    snackbar.view.layoutParams = layoutParams
    snackbar.show()
}

fun View.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}

fun Context.showSnackbarShort(view: View,message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(
        view,
        message,
        duration
    ).show()
}
fun Context.snackbar(view: View, msg: String, action: (() -> Unit)? = null) {
    Snackbar.make(
        view,
        msg,
        Snackbar.LENGTH_LONG
    ).also {
        it.setAction(
            getString(R.string.ok)
        ) { action?.invoke() }
    }.show()
}


fun View.slideUp(duration: Long = 250L) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
    animate.interpolator = AccelerateInterpolator()
    animate.duration = duration
    animate.fillAfter = true


    this.startAnimation(animate)
}

fun View.slideDown(duration: Long = 250L) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
    animate.interpolator = AccelerateInterpolator()
    animate.duration = duration
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.setMarginTop(marginTop: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, marginTop, 0, 0)
    this.layoutParams = menuLayoutParams
}