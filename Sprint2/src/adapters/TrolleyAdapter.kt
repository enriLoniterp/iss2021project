package adapters




import main.ActuatorPort
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import java.io.BufferedReader
import java.io.FileReader


class TrolleyAdapter( ) : ActuatorPort {
	
	val stepRequest   = "msg(step,request,kotlin,basicrobot,step(1400), 1)"
	val cmdDispatch   = "msg(cmd, dispatch,kotlin,basicrobot,cmd(VALUE),1)"

	var fi = BufferedReader(FileReader("connTrolley.txt"))
	var url = fi.readLine()
	private var client : CoapClient = CoapClient(url)
	
	override fun sendCommand(command : String){
		println(url)
		if(command == "w"){
			client.put(stepRequest, MediaTypeRegistry.TEXT_PLAIN )
		}else{
			val message = cmdDispatch.replace("VALUE", command)
			println(message)
			client.put(message, MediaTypeRegistry.TEXT_PLAIN )
		}
	}	
    
}