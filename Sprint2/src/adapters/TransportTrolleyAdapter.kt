package adapters




import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import main.ActuatorPort


class TransportTrolleyAdapter( ) : ActuatorPort {
	
	val stepRequest   = "msg(step,request,kotlin,basicrobot,step(1400), 1)"
	val cmdDispatch   = "msg(cmd, dispatch,kotlin,basicrobot,cmd(VALUE),1)"
	
	private val port: String = "8020"
	private var uriTrolley="coap://192.168.178.112:8020"
	val url = "coap://192.168.178.112:8020/ctxbasicrobot/basicrobot"
	private var client : CoapClient = CoapClient(url)
	
	override fun sendCommand(command : String){
		if(command == "w"){
			client.put(stepRequest, MediaTypeRegistry.TEXT_PLAIN )
		}else{
			val message = cmdDispatch.replace("VALUE", command)
			println(message)
			client.put(message, MediaTypeRegistry.TEXT_PLAIN )
		}
	}	
    
}