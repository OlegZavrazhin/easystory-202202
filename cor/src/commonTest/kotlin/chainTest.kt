import ru.otus.otuskotlin.easystory.cor.chain
import ru.otus.otuskotlin.easystory.cor.rootChain
import ru.otus.otuskotlin.easystory.cor.worker
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.easystory.cor.parallel
import kotlin.test.assertEquals
import kotlin.test.assertTrue

enum class Statuses {
    NONE,
    RUNNING,
    FAILING,
    DONE,
    ERROR
}

data class StubContext(
    var status: Statuses = Statuses.NONE,
    var info: String = "",
    var infoInt: Int = 0
)

class ChainTest {

    companion object {
        val chain = rootChain<StubContext> {
            worker {
                title = "First worker"
                on { status == Statuses.NONE }
                handle {
                    info = "First worker info";
                    infoInt = 1;
                    status = Statuses.RUNNING
                }
                except { status = Statuses.ERROR }
            }
//            worker(
//                title = "Second worker"
//            ) {
//                on { status == Statuses.RUNNING }
//                handle {  }
//            }
            chain {
                on {
                    infoInt == 1;
                    info.length < 50;
                }
                worker {
                    title = "Second worker"
                    on { infoInt > 0 }
                    handle { infoInt++; info = "Second worker info" }
                    except { status = Statuses.ERROR }
                }
                worker {
                    title = "Third worker"
                    on { infoInt < 5 }
                    handle { infoInt++; info = "Third worker info" }
                    except { status = Statuses.ERROR }
                }
            }

        }.build()

        val chain2 = rootChain<StubContext> {
            worker {
                title = "First worker chain2"
                on { status == Statuses.RUNNING }
                handle {
                    info = "First worker info chain2";
                    infoInt = infoInt++;
                }
                except { status = Statuses.ERROR }
            }
            parallel {
                on {
                    (infoInt > 1) && (infoInt < 5);
                    info.length < 50;
                }
                worker {
                    title = "Second worker chain2"
                    on { infoInt > 0 }
                    handle { infoInt++; info = "Second worker info chain2" }
                    except { status = Statuses.ERROR }
                }
                worker {
                    title = "Third worker chain2"
                    on { infoInt < 5 }
                    handle { infoInt++; info = "Third worker info chain2" }
                    except { status = Statuses.ERROR }
                }
            }

        }.build()
    }

    @Test
    fun runTest() = runTest {
        val context = StubContext(info = "StubContext")
        val chain = ChainTest.chain
        val chain2 = ChainTest.chain2

        chain.exec(context)
        println("chain context: $context")
        assertEquals("Third worker info", context.info)

        assertEquals(Statuses.RUNNING, context.status)
        assertEquals(3, context.infoInt)

        chain2.exec(context)
        println("chain2 context: $context")
        assertTrue(listOf("Second worker info chain2","Third worker info chain2").contains(context.info))
        assertEquals(5, context.infoInt)


    }

}