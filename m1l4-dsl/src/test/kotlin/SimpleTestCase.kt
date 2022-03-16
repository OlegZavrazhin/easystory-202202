import org.junit.Test

class SimpleTestCase {
    @Test
    fun myTest() {
        sout {
            1 + 123
        }
    }
    @Test
    fun soutWithPrefix() {
        soutWithTimeStamp {
            "${time()}: my line"
        }
    }
}