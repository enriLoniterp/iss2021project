package Thermometer

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import java.lang.Exception
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class ThermometerCallback(adapter : ThermometerAdapter) : MqttCallback {
    private var thermometerAdapter: thermometerAdapter = adapter
    
    override fun connectionLost(throwable: Throwable?) {
        println("Connection to MQTT broker lost!")
    }
 

    override fun messageArrived(s: String?, mqttMessage: MqttMessage) {
        println("""Message received:${String(mqttMessage.getPayload())}""")
        thermometerAdapter.getTemperature(String(mqttMessage.getPayload()))
    }

    override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken?) {
        // not used
    }
}