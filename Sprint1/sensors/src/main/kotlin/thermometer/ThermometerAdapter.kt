package thermometer

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttSecurityException
import main.SensorPort

class ThermometerAdapter : SensorPort{
    private var temperature: String = "9999"
    private val thermometerCB: ThermometerCallback = ThermometerCallback(this)
	private val client: MqttClient? = MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId())
	             
    init{
		client!!.setCallback(thermometerCB)
		client.connect()
		client.subscribe("thermometer/data")
	}   
	
	
	fun updateTemperature(temperature : String){
		this.temperature = temperature
	}
	
	override fun getValue() : String{
		return this.temperature
	}
		

}