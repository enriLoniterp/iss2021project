package main

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
import org.junit.After
 
 
class prova {
		
	companion object{
		var testingObserver   : CoapObserverForTesting ? = null
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
				myactor=QakContext.getActor("transport_trolley_controller")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor=QakContext.getActor("transport_trolley_controller")
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
    fun stepUntilObstacle(){
		println("+++++++++ stepUntilObstacle ")
		val stepRequest = MsgUtil.buildDispatch("tester", "moveToIn", "moveToIn(indoor)", "transport_trolley_controller")
 		val channelForObserver = Channel<String>()		
		//val testingObserver    = CoapObserverForTesting("obsstep")
		testingObserver!!.addObserver( channelForObserver,"moveToIn" )
		
		runBlocking{ 
 		    //var result  = ""
			//while( true ){
				MsgUtil.sendMsg(stepRequest, myactor!!)
			/*	
			result = channelForObserver.receive()
				if( result == "step(350)") {
					result = channelForObserver.receive()
				}
				println("+++++++++  stepUntilObstacle RESULT=$result for $stepRequest")
	 			if( result.contains("stepFail")) break
				delay(300)  //just to slow down a bit and allow to do a backstep...
			//}
			//After a stepFail, the basicrobot has hit an obstacle and
			//performs a back step (that avoid the repetition of obstacle events)
  		    assertTrue( result.contains("stepFail("))
 			//MsgUtil.sendMsg("cmd","cmd(l)",myactor!!) //just to change the state ...
 */
		}
 	 	 
		
		  
	}
}