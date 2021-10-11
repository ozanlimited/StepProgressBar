package com.ozan.progress

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import com.ozan.progress.stepprogressbar.R


class StepProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val defaultHeight =
        resources.getDimensionPixelSize(R.dimen.step_progressbar_default_height)

    private var needInitial = true

    var max: Int = DEFAULT_MAX
        set(value) {
            field = value
            makeStepView()
        }

    var step: Int = DEFAULT_STEP
        set(value) {
            if (value > max) return
            field = value
            makeStepView()
        }

    var stepDoneDrawable: Drawable? = null
        set(value) {
            field = value
            makeStepView()
        }
    var stepUndoneDrawable: Drawable? = null
        set(value) {
            field = value
            makeStepView()
        }

    var stepMargin = resources.getDimensionPixelSize(R.dimen.step_progressbar_default_margin)
        set(value) {
            field = value
            makeStepView()
        }


    init {
        orientation = HORIZONTAL
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(
                    attrs,
                    R.styleable.StepProgressBar, defStyleAttr, 0
                )

            max = typedArray.getInt(R.styleable.StepProgressBar_max, max)
            step = typedArray.getInt(R.styleable.StepProgressBar_step, step)
            stepDoneDrawable =
                typedArray.getDrawable(R.styleable.StepProgressBar_stepDoneDrawable)
                    ?: ContextCompat.getDrawable(context, R.drawable.done_drawable)
            stepUndoneDrawable =
                typedArray.getDrawable(R.styleable.StepProgressBar_stepUndoneDrawable)
                    ?: ContextCompat.getDrawable(context, R.drawable.undone_drawable)
            stepMargin =
                typedArray.getDimensionPixelSize(R.styleable.StepProgressBar_stepMargin, stepMargin)

            typedArray.recycle()
        }
    }

    @CallSuper
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultHeight(defaultHeight, heightMeasureSpec)
        super.onMeasure(width, height)
        if (needInitial) {
            needInitial = false
            doOnPreDraw { makeStepView(width, height) }
        }
    }

    private fun getDefaultHeight(size: Int, measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST -> size
            else -> size
        }
    }

    private fun makeStepView(width: Int = getWidth(), height: Int = getHeight()) {
        if (needInitial) {
            return
        }
        removeAllViewsInLayout()
        val undoneStepCount = max - step
        val totalViewWidth = width - stepMargin * max * 2
        val stepItemView = totalViewWidth / max
        repeat(step) { addDoneView(stepItemView, height) }
        repeat(undoneStepCount) { addUndoneView(stepItemView, height) }
    }

    private fun addDoneView(doneViewWidth: Int, height: Int) {
        addView(Button(context).apply {
            layoutParams = LayoutParams(doneViewWidth, height)
                .apply {
                    leftMargin = stepMargin
                    rightMargin = stepMargin
                }
            background = stepDoneDrawable
        })

    }

    private fun addUndoneView(stepItemWidth: Int, height: Int) {
        addView(Button(context).apply {
            layoutParams = LayoutParams(stepItemWidth, height)
                .apply {
                    leftMargin = stepMargin
                    rightMargin = stepMargin
                }
            background = stepUndoneDrawable
        })
    }

    companion object {
        private const val DEFAULT_MAX = 10
        private const val DEFAULT_STEP = 0
    }

}
