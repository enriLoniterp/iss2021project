package it.unibo.webspring.demo

import connQak.connQakBase
import connQak.connQakTcp

import it.unibo.webspring.demo.MainCtxcarparking

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.QakContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.BeforeClass
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
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
		//var mainpark:MainCtxcarparking= MainCtxcarparking()
        @JvmStatic
        @BeforeClass


		@BeforeAll
		fun init() {
			GlobalScope.launch {
				main()
			}
		}
		@JvmStatic
		@AfterAll
		fun terminate() {
			println("terminate the testing")
		}
	}

	@Test
	fun ParkingPhaseCorrect() {

		//all parking occupied, not 3
		ParkingState.slotState.set(1, "1603202222:00-1")
		ParkingState.slotState.set(2, "1603202222:02-2")
		ParkingState.slotState.set(4, "1603202222:03-4")
		ParkingState.slotState.set(5, "1603202222:03-5")
		ParkingState.slotState.set(6, "1603202222:05-6")

		//Send reqenter
		// We expect to receive the slot number3.
		mockMvc.perform(get("/reqenter")).andDo(print()).andExpect(status().isOk)
			.andExpect(content().json(Json.encodeToString("3")))


		//Send carenter with our received slotnum=3
		val result =
			mockMvc.perform(get("/client/carenter?slotnum=3")).andDo(print()).andExpect(status().isOk).andReturn()
		//get the tokenid
		val tokenId: String = Json.decodeFromString(result.response.contentAsString)
		val stringTokenizer = StringTokenizer(tokenId, "-")
		assertTrue(stringTokenizer.nextToken().equals("3"))
	}

	@Test
	fun IndoorAreaFull() {
		ParkingState.slotState.set(1, "16/03/2022-22:00")
		ParkingState.slotState.set(2, "16/03/2022-22:02")
		ParkingState.slotState.set(5, "16/03/2022-22:03")
		ParkingState.slotState.set(6, "16/03/2022-22:05")
// We expect to receive numer 0 which indicate that parking is full
		var respo = mockMvc.perform(get("/reqenter")).andDo(print()).andExpect(status().isOk)
			.andReturn()
		var result:String = Json.decodeFromString(respo.response.contentAsString)
		var resint = result.toInt()
		assertTrue(resint > 0 && resint <7)
		val carenter =
			mockMvc.perform(get("/client/carenter?slotnum="+resint)).andDo(print()).andExpect(status().isOk).andReturn()
		val ris:String = Json.decodeFromString(carenter.response.contentAsString)
		assertTrue(ris.equals("wait"))

	}


	@Test
	fun AllParkingSlotEngaged() {
		ParkingState.slotState.set(1, "1603202222:00-1")
		ParkingState.slotState.set(2, "1603202222:02-2")
		ParkingState.slotState.set(4, "1603202222:03-4")
		ParkingState.slotState.set(5, "1603202222:03-5")
		ParkingState.slotState.set(6, "1603202222:05-6")
		ParkingState.slotState.set(6, "1603202222:05-6")
// We expect to receive numer 0 which indicate that parking is full
		var result = mockMvc.perform(get("/reqenter")).andDo(print()).andExpect(status().isOk)
			.andExpect(content().json(Json.encodeToString("0")))
	}


	@Test
	fun Pickupphasecorrect() {

		val tokenId:String= "1703202022:03-3"
		//Outsonar detects that there is not car's inside the outdoor-area
		outSonar.updateDistance("100")
		//parkng slot 3 is occupied and it is required
		ParkingState.slotState.set(1, "1603202022:00-1")
		ParkingState.slotState.set(2, "1603202023:02-3")
		ParkingState.slotState.set(4, "1603202022:03-4")
		////////////////////////////////////////////////
		ParkingState.slotState.set(3, "1703202022:03-3")
		////////////////////////////////////////////////
		ParkingState.slotState.set(6, "1603202022:05-6")

		//Send acceptout
		// We expect to receive 0. That means the car is been succesfully recovered

		mockMvc.perform(get("/reqexit?tokeind="+tokenId)).andDo(print()).andExpect(status().isOk)
			.andExpect(content().json(Json.encodeToString("0")))

	}


	@Test
	fun Pickupphasewrong() {
		//same situation as before, but in this case outsonar detects the presence of a car
		val tokenId:String= "1703202022:03-3"
		//Outsonar detects that there is not car's inside the outdoor-area
		outSonar.updateDistance("100")
		//parkng slot 3 is occupied and it is required
		ParkingState.slotState.set(1, "1603202022:00-1")
		ParkingState.slotState.set(2, "1603202023:02-3")
		ParkingState.slotState.set(4, "1603202022:03-4")
		////////////////////////////////////////////////
		ParkingState.slotState.set(3, "1703202022:03-3")
		////////////////////////////////////////////////
		ParkingState.slotState.set(6, "1603202022:05-6")

		//Send acceptout
		// We expect to receive 1. That means that that the request is not completed

		mockMvc.perform(get("/reqexit?tokeind="+tokenId)).andDo(print()).andExpect(status().isOk)
			.andExpect(content().json(Json.encodeToString("!")))

	}

}