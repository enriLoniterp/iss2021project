package adapters




import interaction.MsgRobotUtil
import resources.ActuatorPort
import resources.IssWsSupport
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileReader
import java.net.URI
import interaction.IssObserver
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking
import resources.VirtualTrolleyObserver


class TrolleyAdapter( ) : ActuatorPort/*, IssObserver*/ {
	
	val stepRequest   = "msg(step,request,kotlin,basicrobot,step(1400), 1)"
	val cmdDispatch   = "msg(cmd, dispatch,kotlin,basicrobot,cmd(VALUE),1)"
       
	var  real : Boolean = false
	var url : String
	
	private lateinit var client : CoapClient
	lateinit var support : IssWsSupport
	lateinit var rto : RealTrolleyObserver
	lateinit var vto : VirtualTrolleyObserver
	var myactor  : ActorBasic? = null
	
	init {
		var fi = BufferedReader(FileReader("connTrolley.txt"))
		var type = fi.readLine()
		url = fi.readLine()
		if(type == "real"){
			real = true
		}
		println(real)
		
		if(real){
			client = CoapClient(url)
			rto = RealTrolleyObserver(url)
			rto.observe()			
		}else{
			support = IssWsSupport("host.docker.internal:8091")
			vto = VirtualTrolleyObserver()
			support.registerObserver(vto)
		}
		fi.close()
			
	}
	
	

	override fun sendCommand(command : String){
		if(command == "w"){
			if(real){
				client.put(stepRequest, MediaTypeRegistry.TEXT_PLAIN )
			}else{
				support.request(MsgRobotUtil.forwardMsg)
			}
		}else{
			if(real){
				val message = cmdDispatch.replace("VALUE", command)
				client.put(message, MediaTypeRegistry.TEXT_PLAIN )
			}else{
				if(command=="l"){	
					support.request(MsgRobotUtil.turnLeftMsg)	
				}else{	
					support.request(MsgRobotUtil.turnRightMsg)
				}
			}		
		}
	}
	
}