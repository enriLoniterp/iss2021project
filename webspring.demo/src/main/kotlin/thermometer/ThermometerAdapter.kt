package thermometer

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttSecurityException
import resources.SensorPort
import resources.ParkingState

class ThermometerAdapter : SensorPort{
    private var temperature: String = "9999"
    private lateinit var ps : ParkingState
    private val thermometerCB: ThermometerCallback = ThermometerCallback(this)
	private val client: MqttClient? = MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId())
	             
    init{
		client!!.setCallback(thermometerCB)
		client.connect()
		client.subscribe("thermometer/data")
	}   
	
	
	fun updateTemperature(temperature : String){
		this.temperature = temperature
		this.ps.temperature = temperature.toInt()
		
	}
	
	override fun getValue() : String{
		return this.temperature
	}
	
	fun setParkingState( ps : ParkingState){
		this.ps = ps
	}

}