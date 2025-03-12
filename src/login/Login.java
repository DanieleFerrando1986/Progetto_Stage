package login;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.*;
import mqtt.ChatEvent;
import mqtt.Subscriber;


/**
 * Classe che implementa l'interfaccia e gli oggetti creati ActionListener che è l'interfaccia dell'ascoltatore per ricevere eventi 
 * d'azione, e KeyListener è l'interfaccia del listener della tastiera java estendendo l'interfaccia EventListener
 * @author Daniele Ferrando
 * @version 1.0
 */
public class Login implements ActionListener, KeyListener {
	
	private ChatEvent obj;
	private LoginJFrame login;
	
	/**
	 * Costruttore che genera un oggetto creato
	 * @param login
	 */
	public Login(LoginJFrame login) {
		this.login=login;
	}
	
	/**
	 * Metodo invocato quando e' stata digitata una chiave
	 */
	@Override
	public void keyTyped(KeyEvent e) {}

	/**
	 * Metodo invocato quando si attiva il pulsante login 
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
        /*CONDIZIONE UNO: l'utente digita il bottone 'Login' dopo aver inserito i campi 'Username' e 'Password' 
          di cui a seguire si accede al frame della chat messaggistica.
		  getSource(): restituisce l'oggetto su cui si e' verificato l'evento confrontandolo con il pulsante 'Login' accedendo 
	      al frame della chat messaggistica*/
        //System.out.println(e.getSource());
        if(e.getSource() == login.loginButton) {
            try {
                /* controllo se i campi sono stati inseriti, se non lo sono faccio uscire un form con errore. */
                if(login.usernameField==null || login.usernameField.getText().isEmpty() || login.passwordField==null || login.passwordField.getPassword().length==0) {
                	this.login.error.setText("Nessun nome utente o password forniti");
                    this.login.error.setVisible(true);
                    return;
                }
                /*imposto utente e password per connettersi alla coda.
                  MqttConnectOptions: contiene il set di opzioni che controllano il modo in cui il client si connette a un server*/
                MqttConnectOptions opt = new MqttConnectOptions();
                opt.setUserName(login.usernameField.getText());
                opt.setPassword(login.passwordField.getPassword());

                login.publisher.client.connect(opt);
       
                login.setVisible(false); /*chiudo il frame corrente*/
                
                /*e' andato a buon fine e apriro' il form successivo*/
                new ChatEvent(login.publisher.client, login.usernameField.getText());
            }
            catch(MqttException cEx) {
            	this.login.error.setText("Connessione fallita: " +cEx.getMessage());
                this.login.error.setVisible(true);
            }
            catch(Exception e1) { //secondo catch(): serve ad evitare di spaccare il file in caso di eccezioni diverse
            	System.out.println("Errore: " +e1.getMessage());
            	this.login.error.setText("Errore: " +e1.getMessage());
                this.login.error.setVisible(true);
                e1.printStackTrace();
            }
        }
        /*CONDIZIONE DUE: l'utente preme il bottone 'Reset' per reimpostare nuovamente i campi 'Username' e 'Password'
          getSource(): restituisce l'oggetto su cui si e' verificato l'evento confrontandolo con il pulsante 'Reset' reimpostando 
	      nuovamente i campi*/
        if(e.getSource() == login.resetButton) {
            try {
                login.passwordField.setText(null);//setText(null): serve per settare le proprietà text a null, cioè inzialmente è vuota
                login.usernameField.setText(null);
            }
            catch(Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
