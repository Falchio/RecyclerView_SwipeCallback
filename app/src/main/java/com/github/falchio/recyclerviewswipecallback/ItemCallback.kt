package com.github.falchio.recyclerviewswipecallback

import android.graphics.Canvas
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView


private const val TAG = "ItemCallback"

class ItemCallback : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
) {

    private var isSwipeBack = false;

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    /** Данный метод переопределен для того, чтобы убрать возможность свайпом выкидывать элемент из RecyclerView
     * пока этот метод возвращает "0", свайпиться ни влево ни вправо не будет.
     * @see isSwipeBack - это переключатель, который следит за состоянием, как только свайп прекращается,
     * т.е. элемент отпустили, то он переключается на состояние true, затем в этом методе сразу на обратно на false
     * @see TouchListener - для подробностей по isSwipeBack */
    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (isSwipeBack) {
            Log.e(TAG, "convertToAbsoluteDirection: ")
            isSwipeBack = false
            return 0
        }

        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {

        if (actionState == ACTION_STATE_SWIPE) {
            setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener(TouchListener())
    }

    inner class TouchListener : View.OnTouchListener {
        /** Пока свайпом тянут элемент isSwipeBack всё время false, как только элемент отпущен - true */
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            isSwipeBack =
                event?.action == MotionEvent.ACTION_CANCEL
                        || event?.action == MotionEvent.ACTION_UP
            return false
        }

    }
}