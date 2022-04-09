package it.unibo.webspring.demo

import connQak.connQakBase
import connQak.connQakTcp
import controller.MainCtxcarparking

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.QakContext
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.BeforeClass
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.runApplication
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import outsonar.OutSonarAdapter
import resources.CoapObserverForTesting
import resources.ParkingState
import weightsensor.WeightSensorAdapter
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

	@Autowired
	lateinit var mockMvc: MockMvc

	companion object {

        var testingObserver   : CoapObserverForTesting? = null
        var systemStarted         = false
        val channelSyncStart      = Channel<String>()
		val init:Array<String>   ?=    null
        var myactor               : ActorBasic? = null
        var counter               = 1
		var weightSensor:WeightSensorAdapter= WeightSensorAdapter()
		var outSonar:OutSonarAdapter= OutSonarAdapter()
		val actors: Array<String> = arrayOf("park_client_service")
		var args : Array<String> = arrayOf()
		//var mainpark:MainCtxcarparking= MainCtxcarparking()
        @JvmStatic
        @BeforeClass

		// Inizialization
		@BeforeAll
		fun init() {
			GlobalScope.launch {
				launch(newSingleThreadContext("QakThread")) {
					QakContext.createContexts("localhost", this, "carparking.pl", "sysRules.pl")
				}
				launch(newSingleThreadContext("SpringThread")) {
					runApplication<MainCtxcarparking>(*args)
				}
			}
			GlobalScope.launch {
				while (!actorsReady()) {
					println("waiting for system startup ...")
					delay(500)
				}
				delay(2000)
				channelSyncStart.send("starttesting")
			}
		}
		@JvmStatic
		@AfterAll
		fun terminate() {
			println("terminate the testing")
		}

		fun actorsReady(): Boolean {
			for (actor in actors) {
				if (QakContext.getActor(actor) == null)
					return false
			}
			return true
		}
	}


	//Clients request a free-slot
	//The only slot available is number 3
	//Clients receive the correct slot and send back another request when in the INDOOR AREA
	//Clients receive tokenid representing the time they entered
	//We check our tokenId contains the slotnumber at the end
	//Clients has parked their cars correctly
	@Test
	fun ParkingPhaseCorrect() {

		//all parking occupied, not 3
		ParkingState.slotState.set(1, "160320222200051")
		ParkingState.slotState.set(2, "160320222202052")
		ParkingState.slotState.set(4, "160320222203054")
		ParkingState.slotState.set(5, "160320222203055")
		ParkingState.slotState.set(6, "160320222205056")

		//Send reqenter
		// We expect to receive the slot number3.
		var res1 = mockMvc.perform(get("/reqenter")).andDo(print()).andExpect(status().isOk).andExpect(content().string("3"))


		//Send carenter with our received slotnum=3
		val result = mockMvc.perform(get("/carenter?slotnum=3")).andDo(print()).andExpect(status().isOk).andReturn()
		//get the tokenid

		val tokenId: String = result.response.contentAsString
		//tokenid has to mantain slot-number
		assertTrue(tokenId.endsWith("3"))
	}


	//Clients receive the slot correctly
	//Indoor area is full
	//Clients receive wait message
	@Test
	fun IndoorAreaFull() {
		ParkingState.slotState.set(1, "160320222200051")
		ParkingState.slotState.set(2, "160320222202052")
		ParkingState.slotState.set(4, "160320222203054")
		ParkingState.slotState.set(5, "160320222203055")
		ParkingState.slotState.set(6, "160320222205056")
		ParkingState.indoorFree = false
		var respo = mockMvc.perform(get("/reqenter")).andDo(print()).andExpect(status().isOk).andReturn()
		var result:String = respo.response.contentAsString
		assertFalse(result == "3")
	}


	//Client request for a free-slot
	//Client receives 0 because all parking-slots are occupied
	@Test
	fun AllParkingSlotEngaged() {
		ParkingState.slotState.set(1, "160320222200051")
		ParkingState.slotState.set(2, "160320222202052")
		ParkingState.slotState.set(3, "160320222203053")
		ParkingState.slotState.set(4, "160320222203054")
		ParkingState.slotState.set(5, "160320222203055")
		ParkingState.slotState.set(6, "160320222205056")
		// We expect to receive number 0 which indicates that parking is full
		var result = mockMvc.perform(get("/reqenter")).andDo(print()).andExpect(status().isOk).andExpect(content().string("0"))
	}



	////PICK-UP////



	//Client sends the tokenId to recover his car
	//Client completes the procedure and receives success
	@Test
	fun PickupPhaseCorrect() {

		val tokenId:String= "170320202203053"
		//Outsonar detects that there is not car's inside the outdoor-area
		outSonar.updateDistance("100")
		//parkng slot 3 is occupied and it is required
		ParkingState.slotState.set(1, "160320202200051")
		ParkingState.slotState.set(2, "160320202302052")
		ParkingState.slotState.set(4, "160320202203054")
		////////////////////////////////////////////////
		ParkingState.slotState.set(3, "170320202203053")
		////////////////////////////////////////////////
		ParkingState.slotState.set(6, "160320202205056")

		//Send acceptout
		// We expect to receive 0. That means the car is been succesfully recovered

		mockMvc.perform(get("/reqexit?tokenid="+tokenId)).andDo(print()).andExpect(status().isOk).andExpect(content().string("Success"))

	}


	//test invalid parking slot? non ha senso che nell'url sia diverso... argomento da sicurezza informatica.
	@Test
	fun PickupPhaseWrong() {
		//same situation as before, but in this case outsonar detects the presence of a car
		val tokenId:String= "170320202203053"
		//Outsonar detects that there is a car inside the outdoor-area
		outSonar.updateDistance("5")
		//parkng slot 3 is occupied and it is required
		ParkingState.slotState.set(1, "160320202200051")
		ParkingState.slotState.set(2, "160320202302052")
		ParkingState.slotState.set(4, "160320202203054")
		////////////////////////////////////////////////
		ParkingState.slotState.set(3, "170320202203053")
		////////////////////////////////////////////////
		ParkingState.slotState.set(6, "160320202205056")

		//Send acceptout
		// We expect to receive 1. That means that the request is not completed
		var result = mockMvc.perform(get("/reqexit?tokenid="+tokenId)).andDo(print()).andExpect(status().isOk).andReturn().response.contentAsString
		assertFalse(result == "Success")
	}


	// The client sends his tokenId
	// The client's tokenId is not valid (not currently existing)
	// The client receives an error message
	@Test fun InvalidTokenId(){
		val tokenId:String= "180320202203053"
		//Outsonar detects that there is not car's inside the outdoor-area
		outSonar.updateDistance("100")
		//parkng slot 3 is occupied and it is required
		ParkingState.slotState.set(1, "160320202200051")
		ParkingState.slotState.set(2, "160320202302052")
		ParkingState.slotState.set(4, "160320202203054")
		////////////////////////////////////////////////
		ParkingState.slotState.set(3, "170320202203053")
		////////////////////////////////////////////////
		ParkingState.slotState.set(6, "160320202205056")


		// Send acceptout
		var result = mockMvc.perform(get("/reqexit?tokenid="+tokenId)).andDo(print()).andExpect(status().isOk).andReturn().response.contentAsString
		assertFalse(result == "Success")
	}

}