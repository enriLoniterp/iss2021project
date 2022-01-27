package adapters

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import resources.ActuatorPort

class FanAdapter( ) : ActuatorPort {
	
	private var uriFan="coap://localhost:8080/parkingarea/fan"
	private var client : CoapClient = CoapClient(uriFan)
	
	
	override fun sendCommand(command : String){
		println((client.put(command, MediaTypeRegistry.TEXT_PLAIN )).responseText)
	}
    
}

