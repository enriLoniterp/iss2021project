package resources

import controller.GuiAdapter
import kotlinx.serialization.Serializable
import kotlin.properties.Delegates
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

@Serializable
object ParkingState{
    var highTemperature : Boolean = false
    var trolleyState: String = "idle"
    var fanState : String = "off"
    var temperature : Int = 20
    var indoorFree : Boolean = true
    var outdoorFree : Boolean = true
    var slotState : HashMap<Int, String> = HashMap<Int,String>()
}

