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
 
 
class TestPlan0 {
		
	companion object{
		var testingObserver   : CoapObserverForTesting? = null
		var systemStarted         = false
		val channelSyncStart      = Channel<String>()
		//var testingObserver       : CoapObserverForTesting? = null
		var myactor               : ActorBasic? = null
		var counter               = 1
		val mainUrl = "coap://localhost:8002/ctxcarparking/trolley"
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
				myactor =QakContext.getActor("trolley")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor =QakContext.getActor("trolley")
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
		runBlocking{
			delay(1000)
		}
 	}

	@Test
	fun testMoveToIndoor(){
 		println("+++++++++ testMoveToIndoor ")
		
		//Send a command and look at the result
		var result  = ""
		runBlocking{
 			val channelForObserver = Channel<String>()
 			testingObserver!!.addObserver( channelForObserver,"moved(indoor)")
			val richiesta: ApplMessage = MsgUtil.buildRequest("coap1", "moveToIn", "moveToIn(m)", "trolley")
            val acceptInResponse: CoapResponse? = logicClient.put(richiesta.toString(), MediaTypeRegistry.TEXT_PLAIN)
			result = channelForObserver.receive()
			println("+++++++++  RESULT=$result")
			assertEquals( result, "moved(indoor)")
		}	
	}
	
	@Test
	fun testMoveToSLotIn(){
 		println("+++++++++ testMoveToSlotIn ")
		
		//Send a command and look at the result
		var result  = ""
		runBlocking{
 			val channelForObserver = Channel<String>()
 			testingObserver!!.addObserver( channelForObserver,"moved(slotIn)")
			val richiesta: ApplMessage = MsgUtil.buildRequest("coap1", "moveToSlotIn", "moveToSlotIn(2,2)", "trolley")
            val acceptInResponse: CoapResponse? = logicClient.put(richiesta.toString(), MediaTypeRegistry.TEXT_PLAIN)
			result = channelForObserver.receive()
			println("+++++++++  RESULT=$result")
			assertEquals( result, "moved(slotIn)")
		}	
	}
	
	
	@Test
	fun testMoveToSLotOut(){
 		println("+++++++++ testMoveToSlotOut ")
		
		//Send a command and look at the result
		var result  = ""
		runBlocking{
 			val channelForObserver = Channel<String>()
 			testingObserver!!.addObserver( channelForObserver,"moved(slotOut)")
			val richiesta: ApplMessage = MsgUtil.buildRequest("coap1", "moveToSlotOut", "moveToSlotOut(2,2)", "trolley")
			val acceptInResponse: CoapResponse? = logicClient.put(richiesta.toString(), MediaTypeRegistry.TEXT_PLAIN)
			result = channelForObserver.receive()
			println("+++++++++  RESULT=$result")
			assertEquals( result, "moved(slotOut)") 
		}	
	}
	
	@Test
	fun testMoveToOut(){
 		println("+++++++++ testMoveToOut ")
		
		//Send a command and look at the result
		var result  = ""
		runBlocking{
 			val channelForObserver = Channel<String>()
 			testingObserver!!.addObserver( channelForObserver,"moved(out)")
			val richiesta: ApplMessage = MsgUtil.buildRequest("coap1", "moveToOut", "moveToOut(m)", "trolley")
			val acceptInResponse: CoapResponse? = logicClient.put(richiesta.toString(), MediaTypeRegistry.TEXT_PLAIN)
			result = channelForObserver.receive()
			println("+++++++++  RESULT=$result")
			assertEquals( result, "moved(out)") 	
		}	
	}
	
	@Test
	fun testBackToHome(){
 		println("+++++++++ testBackToHome ")
		
		//Send a command and look at the result
		var result  = ""
		runBlocking{
 			val channelForObserver = Channel<String>()
 			testingObserver!!.addObserver( channelForObserver,"moved(home)")
			val richiesta: ApplMessage = MsgUtil.buildRequest("coap1", "backToHome", "backToHome(m)", "trolley")
			val acceptInResponse: CoapResponse? = logicClient.put(richiesta.toString(), MediaTypeRegistry.TEXT_PLAIN)
			result = channelForObserver.receive()
			println("+++++++++  RESULT=$result")
			assertEquals( result, "moved(home)") 
		}	
	} 
	
 }