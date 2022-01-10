package test

import de.regnis.q.sequence.core.QSequenceAssert.assertTrue
import adapters.FanAdapter
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.QakContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import main.CoapObserverForTesting
import org.junit.*

class FanTest {
    companion object{
        var testingObserver   : CoapObserverForTesting? = null
        var systemStarted         = false
        val channelSyncStart      = Channel<String>()
        //var testingObserver       : CoapObserverForTesting? = null
        var myactor               : ActorBasic? = null
        var counter               = 1
        @JvmStatic
        @BeforeClass
        //@Target([AnnotationTarget.FUNCTION]) annotation class BeforeClass
        //@Throws(InterruptedException::class, UnknownHostException::class, IOException::class)
        fun init() {
            GlobalScope.launch{
                it.unibo.ctxcarparking.main() //keep the control
            }
            GlobalScope.launch{
                myactor= QakContext.getActor("fan")
                while(  myactor == null )		{
                    println("+++++++++ waiting for system startup ...")
                    delay(500)
                    myactor= QakContext.getActor("fan")
                }
                delay(2000)	//Give time to move lr
                channelSyncStart.send("starttesting")
            }
        }//init

        @JvmStatic
        @AfterClass
        fun terminate() {
            println("terminate the testing")
        }

    }//companion object

    @Before
    fun checkSystemStarted()  {
        println("\n=================================================================== ")
        println("+++++++++ BEFOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE testingObserver=$testingObserver")
        if( ! systemStarted ) {
            runBlocking{
                channelSyncStart.receive()
                systemStarted = true
                println("+++++++++ checkSystemStarted resumed ")
            }
        }
        if( testingObserver == null) testingObserver = CoapObserverForTesting("obstesting${counter++}")
    }

    @After
    fun removeObs(){
        println("+++++++++ AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${testingObserver!!.name}")
        testingObserver!!.terminate()
        testingObserver = null

    }

    @Test
    fun fanChangeStatus(){
        println("+++++++++ fanChangeStatus ")
        val fanAdapter = FanAdapter()
        val channelForObserver = Channel<String>()
        //val testingObserver    = CoapObserverForTesting("obsstep")
        testingObserver!!.addObserver( channelForObserver, "status" )

        runBlocking{
            var result  = ""
            fanAdapter.sendCommand("ON")
            result = channelForObserver.receive()
            println(result)
            assertTrue(result.equals("status ON"))
            fanAdapter.sendCommand("OFF")
            result = channelForObserver.receive()
            println(result)
            assertTrue(result.equals("status OFF"))

        }
    }
}