package test

import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import it.unibo.kactor.QakContext
import org.junit.Before
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import org.junit.AfterClass
import org.junit.After


class TrolleyTest {

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
                myactor=QakContext.getActor("trolley_controller")
                while(  myactor == null )		{
                    println("+++++++++ waiting for system startup ...")
                    delay(500)
                    myactor=QakContext.getActor("trolley_controller")
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
    fun testBackToHome(){
        println("+++++++++  testBackToHome ")
        val stepDispatch = MsgUtil.buildDispatch("tester", "moveToSlot", "moveToSlot(1)", "trolley_controller")
        val channelForObserver = Channel<String>()
        //val testingObserver    = CoapObserverForTesting("obsstep")
        testingObserver!!.addObserver( channelForObserver,"trolley in HOME" )

        runBlocking{
            var result  = ""
            MsgUtil.sendMsg(stepDispatch, myactor!!)
            result = channelForObserver.receive()
            assertTrue( result.equals("trolley in HOME"))
        }
    }
 


    @Test
    fun testCompleteCycle(){
        println("+++++++++ testCompleteCicle ")
        var stepDispatch = MsgUtil.buildDispatch("tester", "moveToIn", "moveToIn(X)", "trolley_controller")
        val channelForObserver = Channel<String>()
        //val testingObserver    = CoapObserverForTesting("obsstep")
        testingObserver!!.addObserver( channelForObserver,"trolley" )

        runBlocking{
            var result  = ""
            MsgUtil.sendMsg(stepDispatch, myactor!!)
            result = channelForObserver.receive()
            assertTrue( result.equals("trolley in INDOOR"))

            stepDispatch = MsgUtil.buildDispatch("tester", "moveToSlot", "moveToSlot(3)", "trolley_controller")
            MsgUtil.sendMsg(stepDispatch, myactor!!)
            result = channelForObserver.receive()
            assertTrue( result.equals("trolley in slot 3"))
            result = channelForObserver.receive()

            stepDispatch = MsgUtil.buildDispatch("tester", "moveToOut", "moveToOut(X)", "trolley_controller")
            MsgUtil.sendMsg(stepDispatch, myactor!!)
            result = channelForObserver.receive()
            println(result)
            assertTrue( result.equals("trolley in OUTDOOR"))

            stepDispatch = MsgUtil.buildDispatch("tester", "moveToHome", "moveToHome(X)", "trolley_controller")
            MsgUtil.sendMsg(stepDispatch, myactor!!)
            result = channelForObserver.receive()
            assertTrue( result.equals("trolley in HOME"))

        }
    }


    @Test
    fun testHaltFunctionality(){
        println("+++++++++ test halt ")
        var stepDispatch = MsgUtil.buildDispatch("tester", "moveToSlot", "moveToSlot(1)", "trolley_controller")
        val channelForObserver = Channel<String>()
        //val testingObserver    = CoapObserverForTesting("obsstep")
        testingObserver!!.addObserver( channelForObserver,"trolley" )

        runBlocking{
            var result  = ""
            MsgUtil.sendMsg(stepDispatch, myactor!!)
            stepDispatch = MsgUtil.buildDispatch("tester", "stop", "stop(1)", "trolley_controller")
			MsgUtil.sendMsg(stepDispatch, myactor!!)
            result = channelForObserver.receive()
            assertTrue( result.equals("trolley in slot 1"))

            stepDispatch = MsgUtil.buildDispatch("tester", "resume", "resume(1)", "trolley_controller")
			MsgUtil.sendMsg(stepDispatch, myactor!!)
			 result = channelForObserver.receive()
            assertTrue( result.equals("trolley in HOME"))

        }
    }
 





}