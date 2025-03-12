package mqtt;

import java.awt.event.ActionEvent;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.*;

/**
 * Classe che implementa l'interfaccia e l'oggetto creato del protocollo di messaggistica MqttCallback
 * @author Daniele Ferrando
 * @version 1.0
 */
public class Subscriber implements MqttCallback {
	
	public enum Topic {INFORMATICA,CHIMICA,BIOLOGIA,MATEMATICA,FISICA,ECONOMIA,FILOSOFIA}; //tipo enumeratore
	public ChatEvent visualize;
	public MqttClient client;
	
	/**
	 * Costruttore che genera un oggetto creato
	 * @param event
	 */
	public Subscriber(ChatEvent event) {
		this.visualize=event;
	}

	/**
	 * Metodo che viene chiamato quando il client ha perso la connessione al server
	 */
	@Override
	public void connectionLost(Throwable arg0) {}

	/**
	 * Metodo quando la pubblicazione in uscita e' completa
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {}

	/**
	 * Metodo che stampa il messaggio ricevuto dal server
	 */
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		String strmsg = new String(message.getPayload(), StandardCharsets.UTF_8);//si prendono i byte della stringa
		System.out.println("Messaggio ricevuto per il topic " +topic+ ": " +strmsg);
		
		/*ActionEvent: evento che compie un'azione nel premere un pulsante da parte dell'utente*/
		//ActionEvent ae = new ActionEvent(visualize.button, ActionEvent.ACTION_PERFORMED, "");
		/*append(): e' un metodo che aggiunge il valore stringa del suo argomento nella textArea dei Messaggi Ricevuti*/
		visualize.messagesReceived.append("Topic-" +topic+ System.lineSeparator()+ " -> " +strmsg+ System.lineSeparator());
		System.out.println("Topic-" +topic+ System.lineSeparator()+ " -> " +strmsg+ System.lineSeparator());
	}
}
