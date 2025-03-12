package login;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import mqtt.ActionSubscriber;
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttException;
import mqtt.Publisher;
//import mqtt.Subscriber;


/**
 * Classe che estende la classe JFrame implementando la parte grafica della vista 
 * @author Daniele Ferrando
 * @version 1.0
 */
public class LoginJFrame extends JFrame {
	
	public Button loginButton;
	public Button resetButton;
	public TextField usernameField;
	public JPasswordField passwordField;
	public Label usernameLabel;
	public Label passwordLabel;
	public Label messageLabel;
	public Label error;
    public Publisher publisher;
    
    /**
     * Costruttore che genera la parte logica del protocollo per connettersi e comunicare al broker mqtt
     * @param pub
     */
    public LoginJFrame(Publisher pub) {
    	Login l = new Login(this); /*crea una nuova istanza di Login*/
    	this.publisher = new Publisher(); /*fara' un set della variabile locale di tipo Publisher istanziandone una nuova*/
        usernameLabel = new Label("Username");
		usernameLabel.setBounds(50,100,75,25);
		add(usernameLabel);
		
		passwordLabel = new Label("Password");
        passwordLabel.setBounds(50,150,75,25);
        add(passwordLabel);
        
        messageLabel = new Label();
        messageLabel.setBounds(125,250,250,35); /*setBounds(x,y,width,height): specifica la posizione e le dimensioni di un componente GUI, coordinate x,y*/
        messageLabel.setFont(new Font(null,Font.ITALIC,25));
        
        usernameField = new TextField();
        usernameField.setBounds(125,100,200,25);
        usernameField.setText(null);
        add(usernameField);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(125,150,200,25);
        passwordField.setText(null); /*setText(null): serve per settare le proprietà text a null, cioè inzialmente è vuota*/
        add(passwordField);
        
        //PULSANTE LOGIN
        loginButton = new Button("Login");
        loginButton.setBounds(125,200,100,25);
        loginButton.setBackground(Color.ORANGE);
        loginButton.addActionListener(l);/*eseguirà un'azione entrando tramite il pulsante 'Login' attivando un listener sul tasto della tastiera*/
        add(loginButton);
        
        //PULSANTE RESET
        resetButton = new Button("Reset");
        resetButton.setBounds(225,200,100,25);
        resetButton.setBackground(Color.CYAN);
        resetButton.addActionListener(l);/*eseguirà un'azione pulendo tutti i campi tramite il pulsante 'Reset' attivando un listener sul tasto della tastiera*/
        add(resetButton);
        
        error = new Label();
        error.setBounds(25,250,350,100);
        error.setFont(new Font(null,Font.ITALIC,12));
        error.setForeground(Color.RED); 
        add(error);
        
        /*setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE): configurerà la finestra quando l'utente la chiuderà nel caso di utilizzo JFrame.
		  EXIT_ON_CLOSE: applicazione di uscita quando l'utente chiuderà la finestra*/
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420,420);
        setLayout(null);
        setVisible(true); //rende visibile il Frame grafico
    }
}
