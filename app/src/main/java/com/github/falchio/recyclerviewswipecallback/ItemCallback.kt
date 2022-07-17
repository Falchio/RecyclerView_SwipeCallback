package com.github.falchio.recyclerviewswipecallback

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


private const val TAG = "ItemCallback"

class ItemCallback : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
) {
    private var isSwipeBack = false
    private var buttonState = ButtonState.ALL_GONE
    private val tempButtonWidth = 300

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //TODO:
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
        // '-' - это свайп влево
        // '+' - это свайп вправо
        Log.e(TAG, "onChildDraw: $dX")
        if (dX > 0) {
            (viewHolder as CustomAdapter.ViewHolder).setBorder(dX.toInt())
        } else if (dX < 0) {
            (viewHolder as CustomAdapter.ViewHolder).setBorder(dX.toInt())
        }

        //TODO: определять размер кнопки
        //TODO: блокировать увеличение кнопки после определенного момента

//        if (actionState == ACTION_STATE_SWIPE) {
//            setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//        }
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { view, event ->
            isSwipeBack =
                isUserLetGoItem(event) // Пока свайпом тянут элемент isSwipeBack всё время false, как только элемент отпущен - true

            if (isSwipeBack) {

                calcButtonState(dX)

                if (buttonState != ButtonState.ALL_GONE) {
                    if (buttonState != ButtonState.ALL_GONE) {
                        setTouchDownListener(
                            c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                        );
                        setItemsClickable(recyclerView, false);
                    }
                }
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
            false
        }
    }


    private fun calcButtonState(dX: Float) {
        if (dX < -tempButtonWidth) {
            buttonState = ButtonState.RIGHT_VISIBLE
        } else if (dX > tempButtonWidth) {
            buttonState = ButtonState.LEFT_VISIBLE
        }
    }

    /** Данная функция вычисляет отпустил ли пользователь элемент после свайпа */
    private fun isUserLetGoItem(event: MotionEvent?) =
        (event?.action == MotionEvent.ACTION_CANCEL
                || event?.action == MotionEvent.ACTION_UP)


    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    0f,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                recyclerView.setOnTouchListener { _, _ -> false }
                setItemsClickable(recyclerView, true)
                isSwipeBack = false
                buttonState = ButtonState.ALL_GONE
            }
            false
        }
    }

    private fun setItemsClickable(
        recyclerView: RecyclerView,
        isClickable: Boolean
    ) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }
}