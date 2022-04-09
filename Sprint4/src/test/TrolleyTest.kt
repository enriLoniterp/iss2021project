package test

import org.junit.Assert.*
import java.net.UnknownHostException
import org.junit.BeforeClass
import cli.System.IO.IOException
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
import it.unibo.kactor.sysUtil
import it.unibo.kactor.ApplMessage
import main.CoapObserverForTesting
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
        println("+++++++++ back to home ")
        val stepDispatch = MsgUtil.buildDispatch("tester", "moveToSlot", "moveToSlot(1)", "trolley_controller")
        val channelForObserver = Channel<String>()
        //val testingObserver    = CoapObserverForTesting("obsstep")
        testingObserver!!.addObserver( channelForObserver,"trolley at HOME" )

        runBlocking{
            var result  = ""
            //while( true ){
            MsgUtil.sendMsg(stepDispatch, myactor!!)
            //delay(10000)
            result = channelForObserver.receive()
            assertTrue( result.equals("trolley at HOME"))
        }
    }

    @Test
    fun testCompleteCycle(){
        println("+++++++++ stepUntilObstacle ")
        var stepDispatch = MsgUtil.buildDispatch("tester", "moveToIn", "moveToIn(X)", "trolley_controller")
        val channelForObserver = Channel<String>()
        //val testingObserver    = CoapObserverForTesting("obsstep")
        testingObserver!!.addObserver( channelForObserver,"trolley at INDOOR" )

        runBlocking{
            var result  = ""
            //while( true ){
            MsgUtil.sendMsg(stepDispatch, myactor!!)
            //delay(10000)
            result = channelForObserver.receive()
            assertTrue( result.equals("trolley at INDOOR"))


            stepDispatch = MsgUtil.buildDispatch("tester", "moveToSlot", "moveToSlot(3)", "trolley_controller")
            MsgUtil.sendMsg(stepDispatch, myactor!!)
            //delay(10000)
            result = channelForObserver.receive()
            assertTrue( result.equals("trolley in slot 3"))

            stepDispatch = MsgUtil.buildDispatch("tester", "moveToSlot", "moveToSlot(3)", "trolley_controller")
            MsgUtil.sendMsg(stepDispatch, myactor!!)
            //delay(10000)
            result = channelForObserver.receive()
            assertTrue( result.equals("trolley in slot 3"))

            stepDispatch = MsgUtil.buildDispatch("tester", "moveToOut", "moveToOut(X)", "trolley_controller")
            MsgUtil.sendMsg(stepDispatch, myactor!!)
            //delay(10000)
            result = channelForObserver.receive()
            assertTrue( result.equals("trolley in OUTDOOR"))

            stepDispatch = MsgUtil.buildDispatch("tester", "moveToHome", "moveToHome(X)", "trolley_controller")
            MsgUtil.sendMsg(stepDispatch, myactor!!)
            //delay(10000)
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
        testingObserver!!.addObserver( channelForObserver,"trolley at slot 1" )

        runBlocking{
            var result  = ""
            //while( true ){
            MsgUtil.sendMsg(stepDispatch, myactor!!)
            stepDispatch = MsgUtil.buildDispatch("tester", "stop", "stop(1)", "trolley_controller")
            //delay(10000)
            result = channelForObserver.receive()
            assertTrue( result.equals("trolley at slot 1"))

            stepDispatch = MsgUtil.buildDispatch("tester", "resume", "resume(1)", "trolley_controller")
            testingObserver!!.addObserver( channelForObserver,"trolley at HOME" )
            result = channelForObserver.receive()
            assertTrue( result.equals("trolley at HOME"))

        }
    }





}