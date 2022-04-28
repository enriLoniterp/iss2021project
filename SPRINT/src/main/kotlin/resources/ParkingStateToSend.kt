package resources

import kotlinx.serialization.Serializable

@Serializable
data class ParkingStateToSend(var trolleyState: String,  var fanState : String, var temperature : Int, var slotState : HashMap<Int, String>) {}