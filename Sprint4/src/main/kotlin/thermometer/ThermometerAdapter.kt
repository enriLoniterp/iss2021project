package thermometer

import adapters.FanAdapter
import fan.Fan
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttSecurityException
import resources.SensorPort
import resources.ParkingState

class ThermometerAdapter : SensorPort{
    private var temperature: String = "0"
	private  var fan :FanAdapter
	private val thermometerCB: ThermometerCallback = ThermometerCallback(this)
	lateinit var observer: () -> Unit
	private val client: MqttClient? = MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId(), null)
	             
    init{
		client!!.setCallback(thermometerCB)
		client.connect()
		client.subscribe("thermometer/data")
		fan = FanAdapter()
	}

	fun addObserver(lmbd: () -> Unit) {
		observer = lmbd
	}
	
	
	fun updateTemperature(temperature : String){
		println(this.temperature + " "+ temperature.toInt())
		if(this.temperature.toInt() <30 && temperature.toInt()>=30){
			//this.fan.sendCommand("on")
			ParkingState.highTemperature = true
			println("HIGH TEMPERATURE")
			observer()

		}else if(this.temperature.toInt() >=30 && temperature.toInt()<30){
			this.fan.sendCommand("off")
			ParkingState.highTemperature = false
			observer()
		}
		this.temperature = temperature
		ParkingState.temperature = temperature.toInt()

	}

	fun setFan(fan : FanAdapter){
		this.fan = fan
	}
	
	override fun getValue() : String{
		return this.temperature
	}


}