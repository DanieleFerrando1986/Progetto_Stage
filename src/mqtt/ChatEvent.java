package mqtt;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import login.LoginJFrame;
import mqtt.Publisher.Topic;

/**
 * Classe che estende la classe JFrame implementando la parte grafica della vista 
 * @author Daniele Ferrando
 * @version 1.0
 */
public class ChatEvent extends JFrame {
	
	public Button logoutButton;
	public Button button;
	public Button sendButton; /*Button: presenta un bottone*/
	public TextField textField; /*TextField: è un oggetto che consente la modifica di una singola riga di testo*/
	public Label username; /*Label: etichetta per posizionare il testo in un contenitore, visualizzando una singola riga di testo di sola lettura*/
	public Label usernameLabel; /*Label: etichetta per posizionare il testo in un contenitore, visualizzando una singola riga di testo di sola lettura*/
	public Label receivedLabel; /*Label: rappresenta l'etichetta*/
	public Label facolta;
	/*TextArea: è un'area multilinea in cui viene visualizzato il testo. 
	  Può essere impostato per consentire la modifica o per essere di sola lettura*/
	public TextArea messagesReceived;
	public TextArea textToSend;
	public Choice topics; /*Choice: presenta un menu a tendina*/
	public ArrayList<String> selectedTopics = new ArrayList<String>();
	/*MqttClient: contiene la parte logica del protocollo per connettersi e comunicare al broker mqtt 
	  cioè all'indirizzo 'tcp://127.0.0.1:1883'. 
	  127.0.0.1: e' l'indirizzo locale della macchina; mentre 1883 è la porta*/
	public MqttClient c;
	
	/**
	 * Costruttore che genera la parte logica del protocollo per connettersi e comunicare al broker mqtt
	 * @param cli
	 * @param user
	 */
	public ChatEvent(MqttClient cli, String user) {
		c=cli;
		ActionSubscriber s = new ActionSubscriber(this); /*crea una nuova istanza di ActionSubscriber*/
		/*assegna la callback creando un nuovo subscriber passando this (che e' la classe stessa).
		  La classe ChatEvent implementa i metodi che servono per gestire gli eventi di un Subscriber*/
		c.setCallback(new Subscriber(this));
		usernameLabel = new Label("Username");
		usernameLabel.setBounds(10,50,170,20);
		add(usernameLabel);
		username = new Label();
		username.setBounds(10,70,170,20);/*setBounds(x,y,width,height): specifica la posizione e le dimensioni di un componente GUI, coordinate x,y*/
		username.setText(null);/*setText(null): serve per settare le proprietà text a null, cioè inzialmente è vuota*/
		
		username.setText(user); /*inserimento username di autenticazione (Login)*/
		username.setFont(new Font(null,Font.ITALIC,20));
		
		/*addKeyListener(s): attiva un listener sul tasto della tastiera, ascoltando gli eventi quando l'utente digiterà il tasto gestendo
		  i 3 metodi public void keyPressed(KeyEvent e), public void keyPressed(KeyEvent e), public void keyReleased(KeyEvent e) 
		  e infine public void actionPerformed(ActionEvent e)*/
		username.addKeyListener(s);
		add(username);
		setSize(650,660); /*setSize(WIDTH, HEIGHT): imposta la dimensione della larghezza e dell'altezza del Frame*/
		setLayout(null); /*il gestore del layout posizionerà il componente 'Messaggi ricevuti' dell'interfaccia grafica sul contenitore*/
		
		/*Viene creata l'etichetta 'Facoltà' specificando le posizioni e le coordinate e infine si passa la variabile aggiungendola che è stata 
		  creata come attributo di tipo oggetto Label*/
		facolta = new Label("Facoltà");
		facolta.setBounds(10,94,40,20); /*setBounds(x,y,width,height): specifica la posizione e le dimensioni di un componente GUI, coordinate x,y*/
		add(facolta);
		
		
		//ETICHETTA LABEL E TEXTAREA PER RICEVERE MESSAGGI INVIATI DALL'UTENTE
		receivedLabel = new Label("Messaggi ricevuti");
		receivedLabel.setBounds(300,50,150,20);
		add(receivedLabel);
		messagesReceived = new TextArea(null,10,1,1);
		messagesReceived.setBounds(300,70,300,500);
		messagesReceived.setEnabled(false);//disabilito non modificando i 'Messaggi ricevuti' in chat (es: se abilito setEnabled(true)).
		textToSend = new TextArea();
		textToSend.setBounds(10,150,220,200);/*setBounds(x,y,width,height): specifica la posizione e le dimensioni di un componente GUI, coordinate x,y*/
		textToSend.addKeyListener(s); /*eseguirà una determinata azione inviando messaggi inviati dall'utente attivando un listener sul tasto della tastiera*/
		add(textToSend);
		//messagesReceived.addKeyListener(s);
		add(messagesReceived);
		
		
		//AGGIUNGERE questo se si vuole mandare messaggi solo uno per TOPIC (servira' cambiare action subscriber)
		//topics = new Choice();
		//topics.setBounds(10,360,100,30);/*setBounds(x,y,width,height): specifica la posizione e le dimensioni di un componente GUI, coordinate x,y*/
		//add(topics);
		//for(Topic t : Topic.values()) {
			//System.out.println(t);
			//topics.add(t.name());
		//}
		
		
		//PULSANTE INVIA
		sendButton = new Button("Invia");
		sendButton.setBounds(150,360,80,30);/*setBounds(x,y,width,height): specifica la posizione e le dimensioni di un componente GUI, coordinate x,y*/
		sendButton.setBackground(Color.YELLOW);
		sendButton.addActionListener(s);/*eseguirà una determinata azione inviando messaggi tramite il pulsante 'Invia' attivando un listener sul tasto della tastiera'*/
		add(sendButton);	
		
		
		//PULSANTE LOGOUT
		logoutButton = new Button("Logout");
		logoutButton.setBounds(280,580,80,30);
		logoutButton.setBackground(Color.CYAN);
		logoutButton.addActionListener(s);/*eseguirà un'azione uscendo dalla chat tramite il pulsante 'Logout' attivando un listener sul tasto della tastiera'*/
		add(logoutButton);
		
		
		/*setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE): configurerà la finestra quando l'utente la chiuderà nel caso di utilizzo JFrame.
		  EXIT_ON_CLOSE: applicazione di uscita quando l'utente chiuderà la finestra*/
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true); //rende visibile il Frame grafico
		MultiSelectComboBox();
	}
	
	/**
	 * Metodo per far comparire la select multipla utilizzando un JPopupMenu
	 */
	public void MultiSelectComboBox() {
		ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();/*crea un array di check box*/
		JPopupMenu popup = new JPopupMenu(); /*crea il popup menu per far visualizzare la check box*/
		
		for(Topic option : Topic.values()) {
			JCheckBox checkBox = new JCheckBox(option.name()); /*crea un check box con il nome del topic, cioe' ne crea uno per topic (tipi enumerativi)*/
			checkBoxes.add(checkBox); /*lo aggiungera' all'array check box*/
			popup.add(checkBox); /*lo aggiungera' al popup menu*/
			
			/*aggiungo azioni al click della singola checkbox*/
			checkBox.addActionListener(evt -> {
				/*Se la seleziono mi connetto altrimenti mi disconnetto*/
				if(checkBox.isSelected()) {
					System.out.println(checkBox.getText()+ " selezionato");
					try {
						c.subscribe(checkBox.getText()); /*appena selezionato il check box si connette*/
						/*aggiunge in una variabile di topic selezionati*/
						selectedTopics.add(checkBox.getText());
					} 
					catch (MqttException e) {
						e.printStackTrace();
					}
				} 
				else 
				{
					System.out.println(checkBox.getText()+ " deselezionato");
					try {
						c.unsubscribe(checkBox.getText()); /*appena deselezionato il check box si sconnette*/
						/*rimuove da una variabile di topic selezionati la coda deselezionata, per poter sapere quali sono stati selezionati
						  in fase di logout.
						  (es: quando l'utente deselezionera' i topic vecchi e selezionera' quelli nuovi, i messaggi di chat verranno
						  ricevuti con i nuovi topic selezionati e non piu' con i topic vecchi che erano stati deselezionati dall'utente) */
						selectedTopics.remove(checkBox.getText());
					}
					catch (MqttException e) {
						e.printStackTrace();
					}
				}
			});
		}
		/*Crea la combobox di selezione*/
		JComboBox comboBox = new JComboBox<>(new String[]{"Seleziona"});
        
        /*apre il popup quando l'utente clicca sulla combobox*/
        comboBox.addActionListener(e -> {
            popup.show(comboBox, 0, comboBox.getHeight());
        });
		
		add(comboBox);
        comboBox.setBounds(10,115,100,30); /*setBounds(x,y,width,height): specifica la posizione e le dimensioni di un componente GUI, coordinate x,y*/
	}
}
