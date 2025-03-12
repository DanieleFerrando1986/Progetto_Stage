package mqtt;

import java.awt.event.ActionEvent;
import java.nio.charset.StandardCharsets;
import org.eclipse.paho.client.mqttv3.*;
import java.util.*;

/**
 * Classe che genera eventi inviando i messaggi a un topic, e tutti i subscriber registrati ricevono quei messaggi 
 * @author Daniele Ferrando
 * @version 1.0
 */
public class Publisher {
	
	public enum Topic {INFORMATICA,CHIMICA,BIOLOGIA,MATEMATICA,FISICA,ECONOMIA,FILOSOFIA}; //tipo enumeratore
	
	/*MqttClient: contiene la parte logica del protocollo per connettersi e comunicare al broker mqtt 
	  cioè all'indirizzo 'tcp://127.0.0.1:1883'. 
	  127.0.0.1: e' l'indirizzo locale della macchina; mentre 1883 è la porta*/
	public MqttClient client;
	
	/**
	 * Costruttore che genera l'indirizzo locale della macchina, e genera un identificatore client in modo casuale in base al login
	 * dell'utente corrente 
	 */
	public Publisher() {
		/*Indirizzo del broker MQTT; 127.0.0.1: e' l'indirizzo locale della macchina; mentre 1883 è la porta*/
		//String brokerURL = "tcp://127.0.0.1:1883";
		
		/*ritorna e genera un identificatore client in modo casuale in base al login dell'utente corrente*/ 
		//String clientId = MqttClient.generateClientId(); 
				
		try {
			client = new MqttClient("tcp://127.0.0.1:1883", MqttClient.generateClientId());
		}
		catch(MqttException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo che chiama internamente il metodo run dell'interfaccia Runnable ed esegue il codice specificato nel metodo run 
	 * sovrascrivibile nel nostro thread
	 * @param sub
	 */
	public void start(Subscriber sub) {
		try {
			/*MqttConnectOptions: contiene il set di opzioni che controllano il modo in cui il client si connette a un server*/
			MqttConnectOptions options = new MqttConnectOptions();
			
			client.connect(options); //si connette a un server MQTT utilizzando le opzioni specificate 
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
