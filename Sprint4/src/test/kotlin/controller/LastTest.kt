package controller

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.QakContext
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import org.junit.BeforeClass
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.runApplication
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import outsonar.OutSonarAdapter
import resources.CoapObserverForTesting
import resources.ParkingState
import resources.ParkingStateToSend
import thermometer.ThermometerAdapter
import weightsensor.WeightSensorAdapter


@SpringBootTest
@AutoConfigureMockMvc
class LastTest {

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


    @OptIn(ObsoleteCoroutinesApi::class)
    @Test
    fun productOwnerTest() {
        //state initalization
        ParkingState.slotState[1] = "150520220651211"
        ParkingState.slotState[6] = "150520220655096"
        ParkingState.slotState[2] = ""
        ParkingState.slotState[3] = ""
        ParkingState.slotState[4] = ""
        ParkingState.slotState[5] = ""
        ParkingState.fanState = "off"
        ParkingState.highTemperature = false
        ParkingState.outdoorFree = true
        ParkingState.temperature = 20

        runBlocking {
            launch(newSingleThreadContext("SpringThread")) {
                ParkingState.indoorFree = true
                //Send reqenter
                mockMvc.perform(get("/reqenter")).andDo(print()).andExpect(status().isOk)


                println(ParkingStateToSend(ParkingState.trolleyState, ParkingState.fanState, ParkingState.temperature, ParkingState.slotState).toString())
                delay(2000)
                ParkingState.indoorFree = false

                //Send carenter

                mockMvc.perform(get("/carenter?slotnum=2")).andDo(print()).andExpect(status().isOk)

                println(ParkingStateToSend(ParkingState.trolleyState, ParkingState.fanState, ParkingState.temperature, ParkingState.slotState).toString())
                delay(2000)

                val tokenId = "150520220651211"
                //Send reqexit
                mockMvc.perform(get("/reqexit?tokenid="+tokenId)).andDo(print()).andExpect(status().isOk)

                println(ParkingStateToSend(ParkingState.trolleyState, ParkingState.fanState, ParkingState.temperature, ParkingState.slotState).toString())

                //delay(50000)

            }


        }
    }

}