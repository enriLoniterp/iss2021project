package resources

import kotlinx.serialization.Serializable

//@Serializable
object ParkingState: java.io.Serializable{
    var trolleyState: String = "idle"
    var temperature : Int = 20
    var indoorFree : Boolean = true
    var outdoorFree : Boolean = true
    var slotState : HashMap<Int, String> = HashMap<Int, String>()

}

