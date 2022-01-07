package adapters

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import main.ActuatorPort

class FanAdapter( ) : ActuatorPort {
	
	private var uriFan="coap://localhost:8002/ctxcarparking/fan"
	val cmdDispatch   = "msg(command, dispatch,kotlin,fan,command(VALUE),1)"
	private var client : CoapClient = CoapClient(uriFan)
	
	override fun sendCommand(command : String){
		println("c")
		val message = cmdDispatch.replace("VALUE", command)
		println(message)
		client.put(message, MediaTypeRegistry.TEXT_PLAIN )
		println("c")
	}
    
}

