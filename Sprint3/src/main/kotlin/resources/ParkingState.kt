package resources


import controller.GuiAdapter
import kotlinx.serialization.Serializable
import kotlin.collections.HashMap
import kotlin.properties.Delegates

@Serializable
object ParkingState {
    var trolleyState: String = "idle"
    var fanState : String = "off"
    var temperature : Int = 20
    var indoorFree : Boolean = true
    var outdoorFree : Boolean = true
    var slotState : HashMap<Int, String> = HashMap<Int, String>()

}

