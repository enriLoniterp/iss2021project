/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package weightsensor

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttSecurityException
import resources.SensorPort
import resources.ParkingState

class WeightSensorAdapter : SensorPort{
    private var weight: String = "9999999"
	private lateinit var ps : ParkingState
    private val weightSensorCB: WeightSensorCallback = WeightSensorCallback(this)
	private val client: MqttClient? = MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId())
	             
    init{
		client!!.setCallback(weightSensorCB)
		client.connect()
		client.subscribe("weightSensor/data")

	}   
	
	
	fun updateWeight(weight : String){
		this.weight = weight
		if(weight.toInt() > 0){
			this.ps.indoorFree = false
		}else{
			this.ps.indoorFree = true
		}
	}
	
	override fun getValue() : String{
		return this.weight
	}
	
	fun setParkingState( ps : ParkingState){
		this.ps = ps
	}
		

}