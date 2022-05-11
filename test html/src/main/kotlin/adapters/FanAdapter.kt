package adapters

import connQak.connQakBase
import connQak.connQakTcp
import controller.ApplMessageUtil
import it.unibo.kactor.MsgUtil
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import resources.ActuatorPort
import resources.ParkingState

class FanAdapter( ) : ActuatorPort {
	/*
	private var uriFan="coap://localhost:8002/parkingarea/fan"
	private var client : CoapClient = CoapClient(uriFan)

	 */
	final val connParkClientService: connQakBase = connQakTcp()

	init{
		connParkClientService.createConnection("localhost", 8002)
	}

	
	override fun sendCommand(command : String){
		if(command == "off"){ParkingState.fanState = "off"}
		else{
			ParkingState.fanState = "on"
		}
		val request = MsgUtil.buildDispatch("fanadapter", "command", "command($command)", "fan")
		connParkClientService.forward(request)

	}
    
}

