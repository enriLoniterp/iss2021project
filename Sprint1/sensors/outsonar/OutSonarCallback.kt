package outsonar

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import java.lang.Exception
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class OutSonarCallback(adapter : OutSonarAdapter) : MqttCallback {
    private var sonarAdapter: OutSonarAdapter = adapter
    
    override fun connectionLost(throwable: Throwable?) {
        println("Connection to MQTT broker lost!")
    }
 

    override fun messageArrived(s: String?, mqttMessage: MqttMessage) {
        println("""Message received:${String(mqttMessage.getPayload())}""")
        sonarAdapter.updateDistance(String(mqttMessage.getPayload()))
    }

    override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken?) {
        // not used
    }
}