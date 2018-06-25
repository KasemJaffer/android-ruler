package co.xtrava.ruler.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RotateDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import co.xtrava.ruler.ObservableHorizontalScrollView
import co.xtrava.ruler.R
import kotlinx.android.synthetic.main.units.view.*
import kotlin.math.roundToInt


class ScrollingValuePicker : FrameLayout {

    private val mLeftSpacer: View
    private val mRightSpacer: View
    private val mScrollView: ObservableHorizontalScrollView
    private val rulerAccentColor: Int
    private val rulerPrimaryColor: Int
    private val rulerBackgroundColor: Int
    private val rulerPointerOutlineColor: Int
    private val rulerPointerBackgroundColor: Int
    private val rulerMaxValue: Int
    private val rulerPlaceValue: Int
    private val rulerDigits: Int
    private val rulerPointerThickness: Int
    val step by lazy { 16f.dpAsPixels(context) }

    constructor(context: Context) : super(context) {
        mLeftSpacer = View(context)
        mRightSpacer = View(context)
        mScrollView = ObservableHorizontalScrollView(context)

        rulerAccentColor = Color.BLACK
        rulerPrimaryColor = Color.GRAY
        rulerBackgroundColor = Color.LTGRAY
        rulerPointerOutlineColor = Color.GRAY
        rulerPointerBackgroundColor = Color.WHITE
        rulerMaxValue = 10
        rulerPlaceValue = 1
        rulerDigits = 10
        rulerPointerThickness = 2f.dpAsPixels(context).toInt()

        initialize()
    }

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {
        mLeftSpacer = View(context)
        mRightSpacer = View(context)
        mScrollView = ObservableHorizontalScrollView(context)

        val styledAttributes = context.obtainStyledAttributes(attr, R.styleable.ScrollingValuePicker, 0, 0)
        rulerAccentColor = if (styledAttributes.hasValue(R.styleable.ScrollingValuePicker_rulerAccentColor)) {
            styledAttributes.getColor(R.styleable.ScrollingValuePicker_rulerAccentColor, Color.BLACK)
        } else
            Color.BLACK

        rulerPrimaryColor = if (styledAttributes.hasValue(R.styleable.ScrollingValuePicker_rulerPrimaryColor)) {
            styledAttributes.getColor(R.styleable.ScrollingValuePicker_rulerPrimaryColor, Color.GRAY)
        } else
            Color.GRAY

        rulerBackgroundColor = if (styledAttributes.hasValue(R.styleable.ScrollingValuePicker_rulerBackgroundColor)) {
            styledAttributes.getColor(R.styleable.ScrollingValuePicker_rulerBackgroundColor, Color.LTGRAY)
        } else
            Color.LTGRAY

        rulerPointerOutlineColor = if (styledAttributes.hasValue(R.styleable.ScrollingValuePicker_rulerPointerOutlineColor)) {
            styledAttributes.getColor(R.styleable.ScrollingValuePicker_rulerPointerOutlineColor, Color.GRAY)
        } else
            Color.GRAY

        rulerPointerBackgroundColor = if (styledAttributes.hasValue(R.styleable.ScrollingValuePicker_rulerPointerBackgroundColor)) {
            styledAttributes.getColor(R.styleable.ScrollingValuePicker_rulerPointerBackgroundColor, Color.WHITE)
        } else
            Color.WHITE

        rulerMaxValue = if (styledAttributes.hasValue(R.styleable.ScrollingValuePicker_rulerMaxValue)) {
            styledAttributes.getInt(R.styleable.ScrollingValuePicker_rulerMaxValue, 10)
        } else
            10

        rulerPlaceValue = if (styledAttributes.hasValue(R.styleable.ScrollingValuePicker_rulerPlaceValue)) {
            styledAttributes.getInt(R.styleable.ScrollingValuePicker_rulerPlaceValue, 1)
        } else
            1

        rulerPointerThickness = 2f.dpAsPixels(context).toInt()

        rulerDigits = if (styledAttributes.hasValue(R.styleable.ScrollingValuePicker_rulerDigits)) {
            styledAttributes.getInt(R.styleable.ScrollingValuePicker_rulerDigits, 10)
        } else
            10

        styledAttributes.recycle()

        initialize()
    }

    private fun initialize() {

        mScrollView.isHorizontalScrollBarEnabled = false
        addView(mScrollView)

        // Create a horizontal (by default) LinearLayout as our child container
        val container = LinearLayout(context)
        mScrollView.addView(container)

        // Our actual content is an ImageView, but doesn't need to be

        for (i in 0..rulerMaxValue) {
            val view = LayoutInflater.from(context).inflate(R.layout.units, null) as LinearLayout
            //Top line
            view.getChildAt(0).setBackgroundColor(rulerPrimaryColor)

            //Bottom line
            view.getChildAt(view.childCount-1).setBackgroundColor(rulerPrimaryColor)


            val l = view.getChildAt(1) as LinearLayout
            for (j in 1..(l.childCount - 1)) {
                l.getChildAt(j).setBackgroundColor(rulerPrimaryColor)
            }

            if (rulerDigits == 10) {
                l.removeViewAt(1)
                l.removeViewAt(6)
            }

            val value = i * rulerPlaceValue
            view.unit_value.text = value.toString()
            view.main_unit.setBackgroundColor(rulerAccentColor)
            view.unit_value.setTextColor(rulerAccentColor)
            view.setBackgroundColor(rulerBackgroundColor)
            container.addView(view)

            if (value >= rulerMaxValue)
                break
        }

        // Create the left and right spacers, don't worry about their dimensions, yet.

        container.addView(mLeftSpacer, 0)
        container.addView(mRightSpacer)

        val pointer = ImageView(context)
        val shape = ContextCompat.getDrawable(context, R.drawable.pointer) as LayerDrawable
        val triangle = (shape.findDrawableByLayerId(R.id.triangle) as RotateDrawable).drawable as GradientDrawable
        triangle.setColor(rulerPointerBackgroundColor)
        triangle.setStroke(rulerPointerThickness, rulerPointerOutlineColor)
        pointer.setImageDrawable(shape)
        pointer.rotation = 90f

        val params = FrameLayout.LayoutParams(100, 100)
        params.gravity = Gravity.CENTER_HORIZONTAL
        pointer.layoutParams = params
        addView(pointer)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        if (changed) {
            // Layout the spacers now that we are measured
            val width = width

            val leftParams = mLeftSpacer.layoutParams
            leftParams.width = width / 2
            mLeftSpacer.layoutParams = leftParams

            val rightParams = mRightSpacer.layoutParams
            rightParams.width = width / 2
            mRightSpacer.layoutParams = rightParams
        }
    }

    private var listener: ObservableHorizontalScrollView.OnScrollChangedListener? = null

    fun setOnScrollChangedListener(listener: ObservableHorizontalScrollView.OnScrollChangedListener?,
                                   throttleMillis: Long = 0L) {
        this.listener = listener
        mScrollView.setOnScrollChangedListener(listener, throttleMillis)
    }

    fun setOnScrollChangedListener(listener: (x: Int) -> Unit, throttleMillis: Long = 0L) {
        this.listener = object : ObservableHorizontalScrollView.OnScrollChangedListener {
            override fun onScrollChanged(x: Int) {
                listener(x)
            }
        }
        mScrollView.setOnScrollChangedListener(this.listener, throttleMillis)
    }

    private var runnable: Runnable? = null

    fun scrollTo(x: Float) {
        mScrollView.handler.removeCallbacks(runnable)
        runnable = Runnable {
            mScrollView.scrollTo(x.roundToInt(), 0)
        }
        mScrollView.handler.post(runnable)
    }

    private fun Float.dpAsPixels(context: Context): Float {
        val scale = context.resources.displayMetrics.density
        return this * scale
    }

    // do stuff with the scroll listener we created early to make our values usable.
}