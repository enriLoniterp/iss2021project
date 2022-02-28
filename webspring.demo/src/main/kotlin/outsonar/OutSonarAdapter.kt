/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package outsonar

import org.eclipse.paho.client.mqttv3.MqttClient
import resources.SensorPort
import resources.ParkingState
import java.util.Timer

class OutSonarAdapter : SensorPort{
    private var distance: String = "9999"
	private lateinit var ps : ParkingState
	private var t : Timer? = null
    private val outsonarCB: OutSonarCallback = OutSonarCallback(this)
	private val client: MqttClient? = MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId())
	             
    init{
		client!!.setCallback(outsonarCB)
		client.connect()
		client.subscribe("sonar/data")
	}   
	
	
	fun updateDistance(distance : String){
		this.distance = distance
		if(distance.toInt() < 50){
			ps.outdoorFree = false
		}else{
			ps.outdoorFree = true
			if(t != null){
				t!!.cancel()
				t!!.purge()
				deleteTimer()
			}
		}
	}
	
	override fun getValue() : String{
		return this.distance
	}
	
	fun setParkingState( ps : ParkingState){
		this.ps = ps
	}
	
	fun setTimer(t : Timer){
		this.t = t
	}
	
	fun deleteTimer(){
		this.t = null
	}
		
		

}