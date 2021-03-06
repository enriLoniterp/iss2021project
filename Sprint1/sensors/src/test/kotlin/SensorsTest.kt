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
		var testvalue : String = "100"
		println("ok")
		sleep(5000)
		assertEquals(testvalue, outsonarAd.getValue())
	}
	
	@Test
	fun testWeightSensorReceive(){
		weightsensorAd = WeightSensorAdapter()
		var testvalue : String = "1"
		println("ok")
		sleep(5000)
		assertEquals(testvalue, weightsensorAd.getValue())
	}
	
	@Test
	fun testThermometerReceive(){
		thermometerAd = ThermometerAdapter()
		println("ok")
		var testvalue : String = "45"
		sleep(5000)
		assertEquals(testvalue, thermometerAd.getValue())
	}
	
}

