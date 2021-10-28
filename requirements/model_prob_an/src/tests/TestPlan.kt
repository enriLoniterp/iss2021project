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
		val mainUrl = "coap://localhost:8002/ctxcarparking/parkingmanagerservice"
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
				myactor =QakContext.getActor("parkingmanagerservice")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor =QakContext.getActor("parkingmanagerservice")
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
		 
       logicClient = CoapClient(mainUrl)
       logicClient.timeout = 1000L
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
	
	
	
	 @Test fun IndoorOccupied() {
        var result  = ""
		 runBlocking{
        	MsgUtil.sendMsg("cW","cW(o)", myactor!!)
			val channelForObserver = Channel<String>()
 			testingObserver!!.addObserver( channelForObserver,"car(wait)")
        	val richiesta: ApplMessage = MsgUtil.buildRequest("coap1", "carenter", "carenter(6)", "parkingmanagerservice")
        	val acceptInResponse: CoapResponse? = logicClient.put(richiesta.toString(), MediaTypeRegistry.TEXT_PLAIN)
        	result = channelForObserver.receive()
			MsgUtil.sendMsg("cW","cW(o)", myactor!!)
			println("+++++++++  RESULT=$result")
			assertEquals( result, "car(wait)")
			
        }
	}
	
	
	 @Test fun IndoorFree() {
        var result  = ""
		 runBlocking{
        	//MsgUtil.sendMsg("cW","cW(o)", myactor!!)
			val channelForObserver = Channel<String>()
 			testingObserver!!.addObserver( channelForObserver,"car(CP1494)")
        	val richiesta: ApplMessage = MsgUtil.buildRequest("coap1", "carenter", "carenter(6)", "parkingmanagerservice")
        	val acceptInResponse: CoapResponse? = logicClient.put(richiesta.toString(), MediaTypeRegistry.TEXT_PLAIN)
        	result = channelForObserver.receive()
			println("+++++++++  RESULT=$result")
			assertEquals( result, "car(CP1494)")
        }
	}
	
	 @Test fun slotAvailable() {
        var result  = ""
		 runBlocking{
        	//MsgUtil.sendMsg("cW","cW(o)", myactor!!)
			val channelForObserver = Channel<String>()
 			testingObserver!!.addObserver( channelForObserver,"enter(6)")
        	val richiesta: ApplMessage = MsgUtil.buildRequest("coap1", "reqenter", "requenter(Alex)", "parkingmanagerservice")
        	val acceptInResponse: CoapResponse? = logicClient.put(richiesta.toString(), MediaTypeRegistry.TEXT_PLAIN)
        	result = channelForObserver.receive()
			println("+++++++++  RESULT=$result")
			assertEquals( result, "enter(6)")
        }
	}
	
	 @Test fun slotNotAvailable() {
        var result  = ""
		 runBlocking{
        	MsgUtil.sendMsg("cS","cS(o)", myactor!!)
			val channelForObserver = Channel<String>()
 			testingObserver!!.addObserver( channelForObserver,"enter(0)")
        	val richiesta: ApplMessage = MsgUtil.buildRequest("coap1", "reqenter", "requenter(Alex)", "parkingmanagerservice")
        	val acceptInResponse: CoapResponse? = logicClient.put(richiesta.toString(), MediaTypeRegistry.TEXT_PLAIN)
        	result = channelForObserver.receive()
        	MsgUtil.sendMsg("cS","cS(o)", myactor!!)
			println("+++++++++  RESULT=$result")
			assertEquals( result, "enter(0)")
			
			 
        }
	}

    
	
	
 
	
 }