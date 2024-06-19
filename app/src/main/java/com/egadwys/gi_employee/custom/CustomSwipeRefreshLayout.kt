package com.egadwys.gi_employee.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class CustomSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {
    private var touchSlop: Int = 1660

    init {
        // Default touch slop
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    fun setTouchSlop(newTouchSlop: Int) {
        touchSlop = newTouchSlop
    }

    override fun canChildScrollUp(): Boolean {
        // Adjust sensitivity here if needed
        return super.canChildScrollUp()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                // Handle the touch slop adjustment here
                val newEvent = MotionEvent.obtain(ev)
                newEvent.offsetLocation(touchSlop.toFloat(), touchSlop.toFloat())
                return super.onInterceptTouchEvent(newEvent)
            }
            else -> return super.onInterceptTouchEvent(ev)
        }
    }
}
