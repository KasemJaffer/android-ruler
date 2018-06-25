package co.xtrava.rulerDemo

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.roundToInt

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val ss = 12.452222
        val s = ss - ss.roundToInt()
        assertEquals(4, 2 + 2)
    }
}
