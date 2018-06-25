package co.xtrava.rulerDemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myScrollingValuePicker.setOnScrollChangedListener({
            value_text.text = getReading(it, false)
        }, 200L)
    }

    fun getReading(xValue: Int, isMetric: Boolean): String {
        val step = myScrollingValuePicker.step
        return if (isMetric) {
            val value = ((xValue.toFloat() - step) / step) / 10
            val finalVal = "%.1f".format(value).toFloat()
            val correctedX = (finalVal * step * 10) + step
            myScrollingValuePicker.scrollTo(correctedX)
            "${finalVal}cm"
        } else {
            val value = ((xValue.toFloat() - step) / step) / 12
            val feet = value.toInt()
            val remainder = value - feet
            val inches = (remainder * 12).roundToInt()
            val correctedX = (feet * step * 12) + step + (inches * step)
            myScrollingValuePicker.scrollTo(correctedX)
            "${feet}ft ${inches}in"
        }
    }
}
