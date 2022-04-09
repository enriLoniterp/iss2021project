package weightsensor

import it.unibo.kactor.ApplMessage
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import java.lang.Exception
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class WeightSensorCallback(adapter : WeightSensorAdapter) : MqttCallback {
    private var WeightSensorAdapter: WeightSensorAdapter = adapter
    
    override fun connectionLost(throwable: Throwable?) {
        println("Connection to MQTT broker lost!")
    }
 

    override fun messageArrived(s: String?, mqttMessage: MqttMessage) {
        println("""Message received:${String(mqttMessage.payload)}""")
        val msg = ApplMessage(String(mqttMessage.payload))
        WeightSensorAdapter.updateWeight(msg.msgContent())
    }

    override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken?) {
        // not used
    }
}