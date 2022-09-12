package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.withStyledAttributes
import kotlinx.android.synthetic.main.content_main.view.*
import java.lang.reflect.Type
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize= 0
    private var heightSize= 0
    private var beginAngle=0f
    private var endAngle=0f
    private var label: String
    private var action= false
    private var width= 0f
    private var color0= 0
    private var color1= 0
    private var txtColor=0

    private var valueAnimator = ValueAnimator()

    private val style = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        typeface = Typeface.MONOSPACE
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }

    init {
        context.withStyledAttributes(attrs,R.styleable.LoadingButton){
            color0 = getColor(R.styleable.LoadingButton_color,0)
            color1 = getColor(R.styleable.LoadingButton_colorr,0)
            txtColor = getColor(R.styleable.LoadingButton_txtColor,0)
        }
        label = resources.getString(R.string.app_name)
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        when(new){
            ButtonState.Clicked -> {
                invalidate()
                state(ButtonState.Loading)
                action = true
                label = resources.getString(R.string.app_description)
            }
            ButtonState.Loading -> {
                myRect()
                myCircle()
            }
            ButtonState.Completed -> {
                style.color = color0
                action = false
                label = resources.getString(R.string.done)
                invalidate()

            }
        }
    }

    private fun myRect(){
        valueAnimator = ValueAnimator.ofFloat(0F, measuredWidth.toFloat()).apply {
            addUpdateListener { animate ->
                animate.repeatMode = ValueAnimator.REVERSE
                animate.interpolator = AccelerateInterpolator()
                animate.repeatCount = ValueAnimator.INFINITE
                width = animate.animatedValue as Float
                invalidate()
            }
            duration = 2300
            start()
        }
    }

    private fun myCircle(){
        valueAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
            addUpdateListener { animate ->
                animate.interpolator = AccelerateInterpolator()
                endAngle = animate.animatedValue as Float
                invalidate()
            }
            duration = 2300
            start()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    custom_button.isEnabled = false
                }
                override fun onAnimationEnd(animation: Animator?) {
                    custom_button.isEnabled = true
                    state(ButtonState.Completed)
                }
            })
        }
    }
    fun state(State: ButtonState){
        buttonState = State
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        style.textAlign = Paint.Align.CENTER
        style.color = color0
        canvas?.drawText(label, widthSize.toFloat() / 3, heightSize.toFloat() / 3, style)
        canvas?.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), style)
        canvas?.drawColor(style.color)
        if (action){
            style.color = color1
            canvas?.drawText(label, widthSize.toFloat() / 2, heightSize.toFloat() / 2, style)
            canvas?.drawRect(0f, 0f, width, measuredHeight.toFloat(), style)
            style.color = Color.YELLOW
            canvas?.drawArc((widthSize - 180f), (heightSize / 2) - 50f, (widthSize - 100f), (heightSize / 2) + 50f,
                beginAngle, endAngle, true, style
            )
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}