package mqtt;

import java.awt.Component;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.*;
import login.LoginJFrame;

/*ACTIONLISTENER: interfaccia dell'ascoltatore per ricevere eventi d'azione.
Implementa l'interfaccia e l'oggetto creato
KEYLISTENER: e' l'interfaccia del listener della tastiera java che estende l'interfaccia EventListener e presenta le seguenti firme
dei metodi:
- @Override
  public void keyTyped(KeyEvent e) {}

- @Override
  public void keyPressed(KeyEvent e) {}

- @Override
  public void keyReleased(KeyEvent e) {}*/
/**
 * Classe che implementa l'interfaccia e gli oggetti creati ActionListener che è l'interfaccia dell'ascoltatore per ricevere eventi 
 * d'azione, e KeyListener è l'interfaccia del listener della tastiera java estendendo l'interfaccia EventListener
 * @author Daniele Ferrando
 * @version 1.0
 */
public class ActionSubscriber implements ActionListener, KeyListener {
	
	private LoginJFrame logoutButton;
	public ChatEvent obj;
	/*MqttConnectOptions: contiene il set di opzioni che controllano il modo in cui il client si connette a un server per evitare
	  che il file si spacchi*/
	public MqttConnectOptions opts = new MqttConnectOptions();
	
	/**
	 * Costruttore che genera un oggetto creato
	 * @param obj
	 */
	public ActionSubscriber(ChatEvent obj) {
		this.obj=obj;
	}

	/**
	 * Metodo invocato quando e' stata digitata una chiave
	 */
	@Override
	public void keyTyped(KeyEvent e) {}

	/**
	 * Metodo invocato quando si attiva il pulsante connect
	 */
	@Override
	public void keyPressed(KeyEvent e) {}

	/**
	 * Metodo invocato quando il pulsante e' stato rilasciato
	 */
	@Override
	public void keyReleased(KeyEvent e) {}

	/**
	 * Metodo richiamato automaticamente quando l'utente digitera' il pulsante sul componente registrato verificandosi un'azione
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		/*CONDIZIONE UNO: l'utente preme il pulsante 'INVIA', quindi riguarda il 2° controllo dell'if() permettendo di inviare e pubblicare 
  	  	  i messaggi nella textarea dei 'messaggi ricevuti'*/
  	    /*getSource(): restituisce l'oggetto su cui si e' verificato l'evento confrontandolo con il pulsante inviando i messaggi
  	      aprendo la chat sui messaggi ricevuti e scrivendoli*/ 
		if(e.getSource() == obj.sendButton) {
			try {
				
				/*String m: viene popolato un messaggio m di tipo stringa. Per popolarlo concateno tra [] il nome che e' stato inserito 
  			  	  nella casella 'Username' in questa maniera viene tra [] username dell'utente che la inviato, concatenato viene scritto
  			      dopo il messaggio, cioe' l'utente che ha scritto dentro la casella di testo 'Username' che voleva inviare. Quando
  			      l'utente digita INVIO, l'evento relativo a quel bottone, finisce nella textarea dei messaggi ricevuti, compone una stringa
  			      con dentro il nome di chi l'ha messo tra []: il tasto del messaggio. Per mandarlo dentro la coda non lo si puo' mandare 
  			      direttamente ma bisogna formattarlo correttamente, quindi si istanzia una variabile 'msg' di tipo oggetto 
  			      MqttMessage, si prendono i byte della stringa, quando si e' creato/istanziato questo oggetto viene pubblicato,
  			      una volta pubblicato scattera' poi l'evento che legge dalla coda e infine va a popolare la textarea dei messaggi ricevuti*/
				String m = "[" +obj.username.getText()+ "]:" +obj.textToSend.getText();
				MqttMessage msg = new MqttMessage(m.getBytes(StandardCharsets.UTF_8));
				/*il metodo PUBLISH pubblica su una coda con i parametri publish(nome della mia coda, messaggio che voglio pubblicare),
  			      quindi in 'selectedTopics' ho la coda in cui voglio pubblicare, mentre in 'msg' il messaggio*/
				if(!obj.c.isConnected()) { /*controlla se il client e' connesso, se non lo e' si connette*/
					obj.c.connect(opts);
				}
				
				/*Inviera' e pubblichera' i messaggi ai topic che l'utente ha selezionato*/
				for(int i=0; i<obj.selectedTopics.size(); i++) {
					obj.c.publish(obj.selectedTopics.get(i), msg);
				}
				obj.textToSend.setText(null);/*pulisco la text area dopo aver pubblicato sulla coda*/
			}
			catch(MqttException e1) {
				e1.printStackTrace();
			}
		}
		//CONDIZIONE DUE: l'utente digita il bottone Logout uscendo dal frame della chat messaggistica ritornando al frame di autenticazione
		if(e.getSource() == obj.logoutButton) {
            try {
                obj.setVisible(false); /*chiudo il frame corrente*/
                
                /*gira tutti i topic ancora selezionati e si disconnette, nel senso non fara' l'operazione di deselezione, 
                  perche' l'utente accedera' al login ricominciando con tutti i topic NON selezionati*/
                for(int i=0; i<obj.selectedTopics.size(); i++) {
                	try {
                		obj.c.unsubscribe(obj.selectedTopics.get(i));
                	}
                	catch(Exception e2) {
                		e2.printStackTrace();
                	}
                }             
                new LoginJFrame(null); //e' andato a buon fine e apriro' il form successivo
            }
            catch(Exception e1) { //secondo catch(): serve ad evitare di spaccare il file in caso di eccezioni diverse
            	System.out.println("Errore: " +e1.getMessage());
            	this.logoutButton.error.setText("Errore: " +e1.getMessage());
                this.logoutButton.error.setVisible(true);
                e1.printStackTrace();
            }
        }
	}
}
