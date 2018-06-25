Widget library to pick measurement value from a ruler
=======

![Release](https://jitpack.io/v/KasemJaffer/android-ruler.svg)
https://jitpack.io/#KasemJaffer/android-ruler

![Logo](https://github.com/KasemJaffer/android-ruler/blob/master/app/src/main/res/drawable-mdpi/screenshot_1.jpg)

How to use
------------------------

```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
    implementation 'com.github.KasemJaffer:android-ruler:1.0.0'
}
```


Full example
------------------------

```xml

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#FFFFFF"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/value_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:textSize="32sp"
        tools:text="1" />


    <co.xtrava.ruler.widget.ScrollingValuePicker
        android:id="@+id/myScrollingValuePicker"
        android:layout_width="match_parent"
        android:layout_height="85dp"
        android:layout_marginTop="8dp"
        app:rulerAccentColor="#50c4ae"
        app:rulerBackgroundColor="#f9fafb"
        app:rulerDigits="twelve"
        app:rulerMaxValue="50"
        app:rulerPlaceValue="ones"
        app:rulerPointerBackgroundColor="#FFFFFF"
        app:rulerPointerOutlineColor="#eaebec"
        app:rulerPrimaryColor="#eaebec" />

</LinearLayout>
```

```kotlin

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
```