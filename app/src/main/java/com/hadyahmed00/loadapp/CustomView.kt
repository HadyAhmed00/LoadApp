package com.hadyahmed00.loadapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.shapes.ArcShape
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var lift = 0.0f
    var start =0.0f
    private var width = 0.0f
    private var height = 0.0f
    private var textDraw = "Download"
    private var canvasBackgroundColor = Color.WHITE
    private var buttonBackgroundColor = 0
    private var buttonTextColor = 0
    private var buttonLoadingColor = 0
    private var buttonCircleColor = 0
    private val backgroundRect = RectF()
    private val animationRect = RectF()



    private val animator = ValueAnimator.ofFloat(0f,360f)

    private var loader =0f

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        if(new == ButtonState.Loading )
        {
            textDraw = "Loading"
            animator.start()
        }
        else{
            textDraw = "Download"
            animator.cancel()
            loader = 0f
        }

        invalidate()
    }
    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonBackgroundColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            buttonTextColor = getColor(R.styleable.LoadingButton_textColor, 0)
            buttonLoadingColor = getColor(R.styleable.LoadingButton_buttonLoadingColor, 0)
            buttonCircleColor = getColor(R.styleable.LoadingButton_buttonCircleColor, 0)
        }
        buttonState = ButtonState.Loading

        animator.apply {
            duration = 3000L
            addUpdateListener {
                loader=it.animatedValue as Float
                invalidate()
            }
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }


    }



    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        width = w-lift
        height = h-start
        super.onSizeChanged(w, h, oldw, oldh)

    }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = 70f
    }

    private fun getDimensions(canvas: Canvas?) {
        lift = 10.0f
        start = 10.0f
        width = canvas?.width!!.toFloat() - lift
        height = canvas.height.toFloat() - start
        backgroundRect.apply {
            this.top = start
            this.left = lift
            this.right = width
            this.bottom = height
        }

        animationRect.apply {
            top = start
            left = lift
            right =width * loader/360
            bottom = height
        }
    }

    override fun onDraw(canvas: Canvas?) {
        getDimensions(canvas)
        canvas?.drawColor(canvasBackgroundColor)

        paint.color = buttonBackgroundColor
        canvas?.drawRect(backgroundRect, paint)

        paint.color = buttonLoadingColor
        canvas?.drawRect(animationRect,paint)

        paint.color = buttonCircleColor
        canvas?.drawArc(width-200f,50f,width - 100f,150f,0f, loader, true, paint)

        paint.color = buttonTextColor
        canvas?.drawText(textDraw, (width/2), (height/2)+20.0f,paint)

    }
}


sealed class ButtonState {
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}


