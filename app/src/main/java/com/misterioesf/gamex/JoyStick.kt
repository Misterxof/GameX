package com.misterioesf.gamex

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import android.view.View.OnTouchListener
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.misterioesf.gamex.model.Point
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class JoyStick : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    var heightC: Int
    var widthC: Int
    var paint = Paint()
    private var x = 150f // Later center from size
    private var y = 150f
    private var centralX = 150f
    private var centralY = 150f
    private var radius = 150f
    private lateinit var holder: SurfaceHolder
    private var moveUpdateListener: MoveUpdateListener? = null

    init {
        heightC = context.resources.displayMetrics.heightPixels
        widthC = context.resources.displayMetrics.widthPixels
        Log.e("W", "height ${context.resources.displayMetrics.heightPixels}")
        Log.e("W", "widthC ${context.resources.displayMetrics.widthPixels}")
    }

    fun setMoveUpdateListener(moveUpdateListener: MoveUpdateListener) {
        this.moveUpdateListener = moveUpdateListener
    }

    fun normalizeVector(): Float {
        return (1 / sqrt((x - centralX) * (x - centralX) + (y - centralY) * (y - centralY)))
    }

    fun getNewDirectionVector(): Point {
        val dx = x - centralX
        val dy = y - centralY

        val normal = 1 / sqrt(dx * dx + dy * dy)

        val vx = dx * normal
        val vy = dy * normal

        return Point(vx, vy)
    }

    fun getMovementX(): Float {
        val inv = normalizeVector()
        return if (x > centralX) x * inv else -(x * inv)
//        return if (x > centralX) x - x.toInt() else -(x - x.toInt())
    }

    fun getMovementY(): Float {
        val inv = normalizeVector()
        return if (y > centralY) y * inv else -(y * inv)
//        return if (y > centralY) y - y.toInt() else -(y - y.toInt())
    }

    fun upd(event: MotionEvent?) {
        paint.color = Color.WHITE

        if (event == null) {
            moveUpdateListener?.onPositionUpdate(Point(0f, 0f))
            return
        }

        val distance = sqrt(
            abs((event?.x!! - centralX).toDouble()).pow(2.0) + abs((event?.y!! - centralY).toDouble()).pow(
                2.0
            )
        )

        if (distance - radius < 0) {
            x = event?.x!!
            y = event?.y!!
        }

        moveUpdateListener?.onPositionUpdate(getNewDirectionVector())
        invalidate()

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                upd(event)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                upd(event)
                return true
            }
            MotionEvent.ACTION_UP -> {
                upd(null)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        centralX = MeasureSpec.getSize(widthMeasureSpec).toFloat() / 2
        centralY = MeasureSpec.getSize(heightMeasureSpec).toFloat() / 2
        x = centralX
        y = centralY

        radius = MeasureSpec.getSize(widthMeasureSpec).toFloat() / 3
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        paint.color = Color.WHITE
        canvas?.drawCircle(centralX, centralY, radius - 10, paint)

        paint.style = Paint.Style.FILL

        paint.color = Color.WHITE
        paint.alpha = 100
        canvas?.drawCircle(centralX, centralY, radius - 10, paint)

        paint.style = Paint.Style.STROKE
        paint.color = Color.GRAY
        paint.strokeWidth = 5f
        canvas?.drawCircle(x, y, 45f, paint)

        paint.style = Paint.Style.FILL

        paint.color = Color.WHITE
        paint.alpha = 200
        canvas?.drawCircle(x, y, 45f, paint)
    }
}