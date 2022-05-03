package outsonar

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import it.unibo.kactor.*

class OutSonarCallback(adapter : OutSonarAdapter) : MqttCallback {
    private var sonarAdapter: OutSonarAdapter = adapter
	lateinit var msg : ApplMessage 
    
    override fun connectionLost(throwable: Throwable?) {
        println("Connection to MQTT broker lost!")
    }
 

    override fun messageArrived(s: String?, mqttMessage: MqttMessage) {
        println("""Message received:${String(mqttMessage.payload)}""")
		val msg = ApplMessage(String(mqttMessage.payload))
        println(msg.msgContent())
        sonarAdapter.updateDistance(msg.msgContent())
    }

    override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken?) {
        // not used
    }
}