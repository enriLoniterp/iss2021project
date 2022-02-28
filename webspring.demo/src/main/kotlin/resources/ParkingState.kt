package resources

import kotlinx.serialization.Serializable

@Serializable
data class ParkingState(var trolleyState : String, var temperature : Int, var indoorFree : Boolean, var outdoorFree : Boolean, var slotState : HashMap<Int, String>){
	companion object {
        @Volatile
        @JvmStatic
        private var INSTANCE: ParkingState? = null

        @JvmStatic
        @JvmOverloads
        fun getInstance(trolleyState: String = "idle", temperature : Int = 0, indoorFree : Boolean = true, outdoorFree : Boolean = true, slotState : HashMap<Int, String> = HashMap<Int, String>()): ParkingState = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ParkingState(trolleyState, temperature, indoorFree, outdoorFree, slotState).also { INSTANCE = it }
        }
    }
}

