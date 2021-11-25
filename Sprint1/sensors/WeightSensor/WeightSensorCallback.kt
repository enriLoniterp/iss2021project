package WeightSensor

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import java.lang.Exception
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import Thermometer.WeightSensorAdapter

class WeightSensorCallback(adapter : WeightSensorAdapter) : MqttCallback {
    private var WeightSensorAdapter: WeightSensorAdapter = adapter
    
    override fun connectionLost(throwable: Throwable?) {
        println("Connection to MQTT broker lost!")
    }
 

    override fun messageArrived(s: String?, mqttMessage: MqttMessage) {
        println("""Message received:${String(mqttMessage.getPayload())}""")
        WeightSensorAdapter.updateWeight(String(mqttMessage.getPayload()))
		System.out.print(String(mqttMessage.getPayload()))

	    }

    override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken?) {
        // not used
    }
}