package mqttmain;

import login.LoginJFrame;
import mqtt.Publisher;

public class PublisherMain {

	public static void main(String[] args) {
		Publisher p = new Publisher();
		new LoginJFrame(p);
	}
}
