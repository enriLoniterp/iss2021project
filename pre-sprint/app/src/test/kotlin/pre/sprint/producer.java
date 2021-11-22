package prova;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

public class producer {
	
	public static void main (String[] args) {
	MqttClient client = null;
	System.out.println("Prod");
	try {
		client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
	} catch (MqttException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		client.connect();
	} catch (MqttSecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (MqttException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	MqttMessage message = new MqttMessage();
	message.setPayload("Hello world from Java".getBytes());
	try {
		client.publish("iot_data", message);
	} catch (MqttPersistenceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (MqttException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		client.disconnect();
	} catch (MqttException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
