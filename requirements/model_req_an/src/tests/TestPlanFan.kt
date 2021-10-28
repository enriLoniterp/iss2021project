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
import it.unibo.kactor.ApplMessage
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import org.eclipse.californium.core.CoapClient
 
 
class TestPlan1 {
		
	companion object{
		var testingObserver   : CoapObserverForTesting? = null
		var systemStarted         = false
		val channelSyncStart      = Channel<String>()
		//var testingObserver       : CoapObserverForTesting? = null
		var myactor               : ActorBasic? = null
		var counter               = 1
		val mainUrl = "coap://localhost:8002/ctxcarparking/fan"
		var logicClient = CoapClient(mainUrl)
		@JvmStatic
        @BeforeClass
		//@Target([AnnotationTarget.FUNCTION]) annotation class BeforeClass
		//@Throws(InterruptedException::class, UnknownHostException::class, IOException::class)
		fun init() {
			
			logicClient.timeout = 1000L
			GlobalScope.launch{
				it.unibo.ctxcarparking.main() //keep the control
			}
			GlobalScope.launch{
				myactor =QakContext.getActor("fan")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor =QakContext.getActor("fan")
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
		if( testingObserver == null) testingObserver = CoapObserverForTesting("obstesting${counter++}","ctxcarparking","fan","8002")
  	}
	
	@After
	fun removeObs(){
		println("+++++++++ AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${testingObserver!!.name}")
		testingObserver!!.terminate()
		testingObserver = null
		runBlocking{
			delay(1000)
		}
 	}

	@Test
	fun testSwitchOn(){
 		println("+++++++++ testSwitchOn ")
		
		//Send a command and look at the result
		var result  = ""
		runBlocking{
 			val channelForObserver = Channel<String>()
 			testingObserver!!.addObserver( channelForObserver,"switched(on)")
			MsgUtil.sendMsg("on","on(o)", myactor!!)
			result = channelForObserver.receive()
			println("+++++++++  RESULT=$result")
			assertEquals( result, "switched(on)")			
		}	
	}
	
	@Test
	fun testSwitchOff(){
 		println("+++++++++ testSwitchOff ")
		
		//Send a command and look at the result
		var result  = ""
		runBlocking{
 			val channelForObserver = Channel<String>()
 			testingObserver!!.addObserver( channelForObserver,"switched(off)")
			MsgUtil.sendMsg("off","off(o)", myactor!!)
			result = channelForObserver.receive()
			println("+++++++++  RESULT=$result")
			assertEquals( result, "switched(off)")
		}	
	}
		
 }