package main

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry

class FanAdapter( ) : ActuatorPort {
	
	private var uriFan="coap://localhost:8003/parkingarea/fan"
	val url = "localhost:8080"
	private var client : CoapClient = CoapClient(uriFan)
	
	override fun sendCommand(command : String){
		println("c")
		println((client.put(command, MediaTypeRegistry.TEXT_PLAIN )).responseText)
		println("c")
	}
    
}

