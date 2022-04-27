import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import outsonar.OutSonarAdapter
import weightsensor.WeightSensorAdapter
import thermometer.ThermometerAdapter
import org.junit.Test
import org.junit.BeforeClass
import org.junit.Assert.*
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import java.lang.Thread.sleep


class SensorsTest() {
	lateinit var outsonarAd : OutSonarAdapter
	lateinit var weightsensorAd : WeightSensorAdapter
	lateinit var thermometerAd : ThermometerAdapter
	lateinit var client: MqttClient

	
	@Test
	fun testOutSonarReceive(){
		outsonarAd = OutSonarAdapter()
		client.connect()
		val msg = MsgUtil.buildEvent("outSonar", "test1", "1000")
		val message = MqttMessage()
		message.payload = msg.toString().toByteArray()
		client.publish("sonar/data", message)
		sleep(100)
		assertEquals("1000", outsonarAd.getValue())
	}
	
	@Test
	fun testWeightSensorReceive(){
		weightsensorAd = WeightSensorAdapter()
		client = MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId())
		System.out.println(	client.connect())
		val msg = MsgUtil.buildEvent("weightSensor", "test1", "1000")
		val message = MqttMessage()
		message.payload = msg.toString().toByteArray()
		client.publish("weightSensor/data", message)
		sleep(100)
		assertEquals("1000", weightsensorAd.getValue())
	}
	
	@Test
	fun testThermometerReceive(){
		thermometerAd = ThermometerAdapter()
		client = MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId())
		client.connect()
		val msg = MsgUtil.buildEvent("temperature", "test1", "25")
		val message = MqttMessage()
		message.payload = msg.toString().toByteArray()
		client.publish("thermometer/data", message)
		sleep(100)
		assertEquals("25", thermometerAd.getValue())
	}
	
}

