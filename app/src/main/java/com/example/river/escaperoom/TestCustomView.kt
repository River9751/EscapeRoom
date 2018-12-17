package com.example.river.escaperoom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class TestCustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    /**
     *
     */

    val drawerRect = arrayListOf(0, 50, 100, 200) //left, top, right, bottom

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        checkRect(event!!.x.toInt(), event.y.toInt())
//        Toast.makeText(context, "${event?.x}, ${event?.y}", Toast.LENGTH_LONG).show()

        return super.onTouchEvent(event)
    }

    fun checkRect(x: Int, y: Int) {
        if (x in drawerRect[0]..drawerRect[2] && y in drawerRect[1]..drawerRect[3]) {
            Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)

        val painter = Paint()
        painter.color = Color.TRANSPARENT
        painter.strokeWidth = 3f

        val rect = Rect(0, 50, 100, 200)
        canvas.drawRect(rect, painter)
    }
}