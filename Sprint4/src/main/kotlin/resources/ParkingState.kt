package resources

import kotlinx.serialization.Serializable

@Serializable
object ParkingState{
    var trolleyState: String = "idle"
    var fanState : String = "off"
    var temperature : Int = 20
    var indoorFree : Boolean = true
    var outdoorFree : Boolean = true
    var slotState : HashMap<Int, String> = HashMap<Int, String>()


}

