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

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (isSwipeBack) {
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
        Log.e(TAG, "Action state: $actionState ")
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
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {

            Log.e(TAG, "MotionEvent: ${MotionEvent.ACTION_CANCEL} ")
            isSwipeBack =
                event?.action == MotionEvent.ACTION_CANCEL
                        || event?.action == MotionEvent.ACTION_UP
            return false
        }

    }
}