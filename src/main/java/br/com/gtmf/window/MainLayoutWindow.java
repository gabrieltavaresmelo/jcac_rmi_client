package br.com.gtmf.window;

import java.awt.Color;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import br.com.gtmf.controller.ClientCreator;
import br.com.gtmf.model.Bundle;
import br.com.gtmf.rmi.ConstantsRmi;
import br.com.gtmf.utils.AppUtils;
import br.com.gtmf.utils.Constants;
import br.com.gtmf.utils.FXOptionPane;
import br.com.gtmf.utils.FXOptionPane.Response;
import br.com.gtmf.utils.ImageUtils;
import br.com.gtmf.utils.NetworkUtils;

/**
 * Classe que executa os eventos do Layout da
 * Tela MainLayout
 * 
 * @author Gabriel tavares
 *
 */
public class MainLayoutWindow implements ListenerWindow {

	private static final Logger LOG = LoggerFactory.getLogger(MainLayoutWindow.class);

	// Widgets    
	@FXML private TextField tfUsername;
	@FXML private TextField tfIpServer;
    @FXML private ToggleButton tbStatus;
    @FXML private Label lbStatus;
    @FXML private Label lbQuestionEnemy;
    @FXML private RadioButton rbYes;
    @FXML private RadioButton rbNo;
    @FXML private Button btSendAnswer;
    @FXML private Label lbQuestionMine;
    @FXML private TextField tfSendQuestion;
    @FXML private Label lbAnswer;
    @FXML private Label lbAnswerValue;
    @FXML private Button btSendQuestion;    
    @FXML private ListView<String> lvUsersOn;
    @FXML private TextArea taHistory;
    @FXML private TextField tfSendChat;
    @FXML private Button btSendChat;
    @FXML private MenuItem mniStartGame;
    @FXML private MenuItem mniRiddle;
    
    @FXML private ImageView img01;
    @FXML private ImageView img02;
    @FXML private ImageView img03;
    @FXML private ImageView img04;
    @FXML private ImageView img05;
    @FXML private ImageView img06;
    @FXML private ImageView img07;
    @FXML private ImageView img08;
    @FXML private ImageView img09;
    @FXML private ImageView img10;
    @FXML private ImageView img11;
    @FXML private ImageView img12;
    @FXML private ImageView img13;
    @FXML private ImageView img14;
    @FXML private ImageView img15;
    @FXML private ImageView img16;
    @FXML private ImageView img17;
    @FXML private ImageView img18;
    @FXML private ImageView img19;
    @FXML private ImageView img20;
    @FXML private ImageView img21;
    @FXML private ImageView img22;
    @FXML private ImageView img23;
    @FXML private ImageView img24;

    private ImageView [] imgs = null;
    private boolean [] imgsSelected = null;
    
	// Referencia a Stage
	private Stage stage;
	private MigPane migPane;
	
	private ClientCreator clientCreator = null;
	
	public void setParams(Stage stage, MigPane migPane) {
		this.stage = stage;
		this.migPane = migPane;
		
		// Evento de clique do mouse
		migPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent t) {
	        	if(imgs != null && imgsSelected != null){	        	
		        	for (int i = 0; i < imgs.length; i++) {
						ImageView tmp = imgs[i];
						
						// Verifica se a imagem ja foi selecionada
						boolean isImgViewNotSelected = imgsSelected[i];
						
						// Checa se o mouse clicou sobre uma das imagens-face
			        	if(t.getTarget().equals(tmp)){
//				            System.out.println("clicado na imagem: " + tmp.getId());
			        		if(!isImgViewNotSelected){
				        		imgsSelected[i] = true;			        		
								tmp.setImage(ImageUtils.makeTranslucentImage(
										tmp.getImage(), 0.60f));
			        		} else{
			        			imgsSelected[i] = false;
								tmp.setImage(new Image(getClass()
										.getResourceAsStream(
												Constants.DIR_FACES
														+ tmp.getId() + ".png")));
			        		}
			        		
			        		
			        		int qtdImgsSelected = 0;
			        		String person = null;
			        		
			        		for (int j = 0; j < imgsSelected.length; j++) {
			        			boolean isImgViewSelected = imgsSelected[j];
			        			
								if(isImgViewSelected){
									qtdImgsSelected++;
								} else{
									ImageView imgNotSelected = imgs[j];
				        			
									// Obtem o nome da pessoa de acordo com o ImageView (atraves do Mapeamento)
				        			person = Constants.PERSONS.get(imgNotSelected.getId().substring(3));									
								}
							}
			        		
			        		if(qtdImgsSelected >= imgsSelected.length-1 && person != null){
			        			System.out.println(person);
			        			sendMessage(Bundle.RIDDLE_PERSONA_SEND, person);
			        		}
			        	}
					}
	        	}
	        }
	    });
	}
	
	/**
	 * Inicializa a classe controller. 
	 * Eh chamado automaticamente depois do fxml ter sido carregado 
	 */
	@FXML
	private void initialize() {
		// Inicializa os componentes de entrada de texto dinamicamente
		tfUsername.setText(NetworkUtils.getIpHostnameLocal());
		tfIpServer.setText("localhost:9009");
		
		// Inicializa o ToogleButton: Sim/Nao
		toogleButtonYesNo();
        
		// Desabilita os campos
        setLockToConnectUI(false);
        stopGame();
		
        // Mapeia as faces
		AppUtils.loadPersons();
	}

	/**
	 * Instancia o componente de selecao unica para o RadioButton
	 */
	private void toogleButtonYesNo() {
		final ToggleGroup group = new ToggleGroup();
        rbNo.setToggleGroup(group);                
        rbYes.setToggleGroup(group);

        rbYes.setSelected(true);
	}
	
	/**
	 * Carrega as imagens do arquivo para a tela
	 */
	private void loadFacesToView() {
		img01.setImage(new Image(getClass().getResourceAsStream(Constants.face01)));
		img02.setImage(new Image(getClass().getResourceAsStream(Constants.face02)));
		img03.setImage(new Image(getClass().getResourceAsStream(Constants.face03)));
		img04.setImage(new Image(getClass().getResourceAsStream(Constants.face04)));
		img05.setImage(new Image(getClass().getResourceAsStream(Constants.face05)));
		img06.setImage(new Image(getClass().getResourceAsStream(Constants.face06)));
		img07.setImage(new Image(getClass().getResourceAsStream(Constants.face07)));
		img08.setImage(new Image(getClass().getResourceAsStream(Constants.face08)));
		img09.setImage(new Image(getClass().getResourceAsStream(Constants.face09)));		
		img10.setImage(new Image(getClass().getResourceAsStream(Constants.face10)));
		img11.setImage(new Image(getClass().getResourceAsStream(Constants.face11)));
		img12.setImage(new Image(getClass().getResourceAsStream(Constants.face12)));
		img13.setImage(new Image(getClass().getResourceAsStream(Constants.face13)));
		img14.setImage(new Image(getClass().getResourceAsStream(Constants.face14)));
		img15.setImage(new Image(getClass().getResourceAsStream(Constants.face15)));
		img16.setImage(new Image(getClass().getResourceAsStream(Constants.face16)));
		img17.setImage(new Image(getClass().getResourceAsStream(Constants.face17)));
		img18.setImage(new Image(getClass().getResourceAsStream(Constants.face18)));
		img19.setImage(new Image(getClass().getResourceAsStream(Constants.face19)));
		img20.setImage(new Image(getClass().getResourceAsStream(Constants.face20)));
		img21.setImage(new Image(getClass().getResourceAsStream(Constants.face21)));
		img22.setImage(new Image(getClass().getResourceAsStream(Constants.face22)));
		img23.setImage(new Image(getClass().getResourceAsStream(Constants.face23)));
		img24.setImage(new Image(getClass().getResourceAsStream(Constants.face24)));

		imgs = new ImageView[]{
				img01, img02, img03, img04, img05, img06, img07, img08,
				img09, img10, img11, img12, img13, img14, img15, img16, 
				img17, img18, img19, img20, img21, img22, img23, img24
		};

        imgsSelected = new boolean[24];
	}

	
	// --------------------------------------------------------------------
	// -- 
	// -- 	Metodos que mudam o estado de visualizacao dos componentes
	// --
	// --------------------------------------------------------------------
	
	private void statusVisibilityFieldsImage(boolean isVisible) {
		img01.setVisible(isVisible);
		img02.setVisible(isVisible);
		img03.setVisible(isVisible);
		img04.setVisible(isVisible);
		img05.setVisible(isVisible);
		img06.setVisible(isVisible);
		img07.setVisible(isVisible);
		img08.setVisible(isVisible);
		img09.setVisible(isVisible);		
		img10.setVisible(isVisible);
		img11.setVisible(isVisible);
		img12.setVisible(isVisible);
		img13.setVisible(isVisible);
		img14.setVisible(isVisible);
		img15.setVisible(isVisible);
		img16.setVisible(isVisible);
		img17.setVisible(isVisible);
		img18.setVisible(isVisible);
		img19.setVisible(isVisible);
		img20.setVisible(isVisible);
		img21.setVisible(isVisible);
		img22.setVisible(isVisible);
		img23.setVisible(isVisible);
		img24.setVisible(isVisible);
	}
	
	private void statusVisibilityFieldsAnswer(boolean isVisible){
		rbYes.setVisible(isVisible);
		rbNo.setVisible(isVisible);
		btSendAnswer.setVisible(isVisible);
	}
	
	private void statusVisibilityFieldsQuestion(boolean isVisible){		
		lbQuestionMine.setVisible(isVisible);
		tfSendQuestion.setVisible(isVisible);
		btSendQuestion.setVisible(isVisible);
		
		lbQuestionEnemy.setVisible(isVisible);
		lbAnswer.setVisible(isVisible);
		lbAnswerValue.setVisible(isVisible);
	}
	
	private void statusFieldsAnswer(boolean isDisable){
		rbYes.setDisable(isDisable);
		rbNo.setDisable(isDisable);
		btSendAnswer.setDisable(isDisable);
	}
	
	private void statusFieldsQuestion(boolean isDisable){
		lbQuestionMine.setDisable(isDisable);
		tfSendQuestion.setDisable(isDisable);
		btSendQuestion.setDisable(isDisable);
	}
	
	private void disableFieldsAnswer(){
		statusFieldsAnswer(true);
	}
	
	private void enableFieldsAnswer(){
		statusFieldsAnswer(false);
	}
	
	private void disableFieldsQuestion(){
		statusFieldsQuestion(true);
	}
	
	private void enableFieldsQuestion(){
		statusFieldsQuestion(false);
	}

	
	// ---------------------------------------------------
	// -- 
	// -- 	Metodos dos Eventos dos componentes da tela
	// --
	// ---------------------------------------------------

	@FXML
	private void handleExit() {
		finish();
	}
	
	@FXML
	private void handleAbout() {
		String body = "";

		body += "Curso:";
		body += "\n";
		body += "Engenharia de Computação";
		body += "\n";
		body += "\n";
		
		body += "Disciplina:";
		body += "\n";
		body += "Programação Paralela e Distribuída";
		body += "\n";
		body += "18/11/2013";
		body += "\n";
		body += "\n";

		body += "Trabalho:";
		body += "\n";
		body += "Jogo: Cara-a-Cara utilizando WebSockets";
		body += "\n";
		body += "\n";
		
		body += "Aluno:";
		body += "\n";
		body += "Gabriel Tavares";
		body += "\n";
		body += "gabrieltavaresmelo@gmail.com";
		body += "\n";
		body += "https://github.com/gabrieltavaresmelo";
		body += "\n";
		body += "\n";

		FXOptionPane.showMessageDialog(stage, body, "Sobre", "OK");
	}
	
	@FXML
	public void sendChat() {
		String textToSend = tfSendChat.getText();
		
		if(!textToSend.trim().equals("")){
			tfSendChat.setText("");		
			sendMessage(Bundle.CHAT_MSG_SEND,  tfUsername.getText() + ": " + textToSend);
		}
	}
	
	@FXML
	public void sendQuestion() {
		String textToSend = tfSendQuestion.getText();
		
		if(!textToSend.trim().equals("")){
			tfSendQuestion.setText("");
			sendMessage(Bundle.QUESTION_SEND, textToSend);
			disableFieldsQuestion();
		}
	}
	
	@FXML
	public void sendAnswer() {
		String result = rbYes.isSelected() ? "Sim" : "Não";		
		sendMessage(Bundle.ANSWER_SEND, result);
		
		disableFieldsAnswer();
		enableFieldsQuestion();
	}
	
	@FXML
	public void handleStartGame() {
		// Inicializa as imagens lidas dos arquivos
        loadFacesToView();
        
        // Seleciona uma imagem aleatoria
		int max = Constants.FACES.length;
		Random random = new Random();
		int rand = random.nextInt(max);
		ImageView randomImageView = imgs[rand];
//		System.out.println(randomImageView.getId());
		
		// Obtem o nome da pessoa de acordo com o ImageView (atraves do Mapeamento)
		String person = Constants.PERSONS.get(randomImageView.getId().substring(3));
		
		// Desenha a borda
		Image imgBordered = ImageUtils.drawBorder(randomImageView.getImage(),
				new Color(236, 151, 31), 7);
		randomImageView.setImage(imgBordered);
		
		// Envia a solicitacao pro servidor iniciar o jogo
		sendMessage(Bundle.NEW_GAME_SEND, tfUsername.getText() + ";" + person);
	}

	@FXML
	public void handleRiddle() {
		String person = FXOptionPane.showInputDialog(stage,
				"Qual o nome do Personagem?", "Adivinhar", "Confirmar",
				"Cancelar", "");
		
		sendMessage(Bundle.RIDDLE_PERSONA_SEND, person);
	}
	
	@FXML
	public void tbStatusItemChange() {
		if(tbStatus.isSelected()){
			turnOn();
		} else{			
			turnOff();			
		}
	}

	/**
	 * Evento ON do ToggleButton
	 */
	@FXML
	public void turnOn(){
		tbStatus.setText("Desconectar");

		try {
			String username = tfUsername.getText();
			String ipPort = tfIpServer.getText();
			String [] split = ipPort.split(":");
			
			String ipServer = split[0];
			String port = split[1];
			
			clientCreator = new ClientCreator(username,	ipServer, Integer.parseInt(port), this);
			
			if(clientCreator.isConectado()){
				setLockToConnectUI(true);
				
				if(!tbStatus.isSelected()){
					tbStatus.setSelected(true);
				}
			} else{
				clientCreator = null;
				
				turnOff();
				setConnected(false);
				setLockToConnectUI(false);
				tbStatus.setSelected(false);
				
				FXOptionPane.showMessageDialog(stage, 
						"Servidor indisponível na porta: " + port,
						"Servidor OFFLINE", "OK");
				
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			LOG.error("[Erro] Insira IP:PORTA do servidor! Ex.: 192.168.0.12:9009");
			tbStatus.setSelected(false);
			turnOff();
		}
	}

	/**
	 * Evento OFF do ToggleButton
	 */
	public void turnOff(){
		tbStatus.setText("Conectar");
		lbStatus.setText("Desconectando");
		
		try {
			if(clientCreator != null && clientCreator.isConectado()){
				clientCreator.close();
				clientCreator = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	// --------------------------------------------
	// -- 
	// -- 	Metodos da implementacao do Listener
	// --
	// --------------------------------------------
	
	/**
	 * Recebe a lista de usuarios e mostra na tela
	 */
	@Override
	public void lvUsers(final Map<String, String> users) {
		final Collection<String > usersSet = users.keySet();
				
		// Para enviar dados de outra thread para a GUI deve-se usar o Platform
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {	
				ObservableList<String> items = FXCollections.observableArrayList(usersSet);
				lvUsersOn.setItems(items);
	        }
		});
	}


	@Override
	public void receive(final Bundle message) {
		// Para enviar dados de outra thread para a GUI deve-se usar o Platform
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {		
				try {
					String messageBody = "";
					
					switch (message.getHead()) {
					
						case Bundle.USER_FIRST:
							messageBody = message.getUser();
							
							if(messageBody.equals(tfUsername.getText())){
								disableFieldsAnswer();
								enableFieldsQuestion();
							} else{
								disableFieldsAnswer();
								disableFieldsQuestion();
							}
							
							break;
					
						case Bundle.USER_IN:
							messageBody = message.getUser();
							tfUsername.setText(messageBody);
							lbStatus.setText("Conectando " + tfUsername.getText()
									+ " com " + tfIpServer.getText());
							
							clientCreator.setUsername(tfUsername.getText());
							
							handleStartGame();
							
							break;
					
						case Bundle.USER_OUT:
							
							// Desabilita os campos
					        stopGame();
					        
							turnOff();
							setConnected(false);
					        setLockToConnectUI(false);
							tbStatus.setSelected(false);
							
							FXOptionPane.showMessageDialog(stage, "O adversário saiu do jogo!",
									"Jogo Encerrado", "OK");
							
							break;
					
						case Bundle.SERVER_OUT:
							
							// Desabilita os campos
					        setLockToConnectUI(false);
					        stopGame();
							
							clientCreator = null;
					        tbStatus.setSelected(false);
							turnOff();
							
							FXOptionPane.showMessageDialog(stage, "Servidor indisponível!",
									"Jogo Encerrado", "OK");
							
							break;
							
						case Bundle.QUESTION_RECEIVE:
							messageBody = message.getQuestion();
							lbQuestionEnemy.setText(messageBody);
							
							enableFieldsAnswer();
							
							break;
							
						case Bundle.ANSWER_RECEIVE:
							messageBody = message.getAnswer();
							lbAnswerValue.setText(messageBody);
														
							break;
							
						case Bundle.CHAT_MSG_RECEIVE:
							messageBody = message.getChatMsg();
							
							if(messageBody != null){
								if(taHistory.getLength() > 700){
									taHistory.clear();
								}
								
								taHistory.appendText(messageBody + "\n");
							}
							break;
							
						case Bundle.NEW_GAME_RECEIVE:
							messageBody = message.getUser();
							
							statusVisibilityFieldsAnswer(true);
							statusVisibilityFieldsQuestion(true);
							statusVisibilityFieldsImage(true);

			    			mniRiddle.setDisable(false);
			    			
							break;
							
						case Bundle.RIDDLE_PERSONA_RECEIVE:
							messageBody = message.getPersona();
							
							String split[] = messageBody.split("=");
							
							String username = split[0];
							boolean isWin = Boolean.parseBoolean(split[1]);
							String result = "";
							
							boolean isMe = username.equals(tfUsername.getText()) ? true : false;
							
							if(isMe && isWin){
								result  = "Você venceu!";
								
							} else if(isMe && !isWin){
								result = "Você perdeu!";
								
							} else if(!isMe && isWin){
								result  = username + " venceu!";
								
							} else if(!isMe && !isWin){
								result = username + " perdeu!";
							}
							
							FXOptionPane.showMessageDialog(stage, result,
									"Resultado", "OK");
			    			
							handleStartGame();
							
							break;
					}					
					
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
	        }
		});		
	}

	@Override
	public void setConnected(final boolean isConnected) {
		// Para enviar dados de outra thread para a GUI deve-se usar o Platform
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {						
	        	if(isConnected){
	    			String username = clientCreator.getUsername();
	    			String ipPort = clientCreator.getIpServer();
	    			
	    			lbStatus.setText("Conectado " + username + " com " + ipPort);
	    			System.out.println("Conectado " + username + " com " + ipPort);
	    			
	    			mniStartGame.setDisable(false);
	    			
	    		} else{
	    			lbStatus.setText("Desconectado");
	    			
	    			if(clientCreator != null){
	    				clientCreator = null;
	    			}
	    		}
	        }
		});		
	}

	@Override
	public void setLockToConnectUI(final boolean isLock) {
		// Para enviar dados de outra thread para a GUI deve-se usar o Platform
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {		
	        	tfUsername.setDisable(isLock);
	        	tfIpServer.setDisable(isLock);
				
				lvUsersOn.setVisible(isLock);
				taHistory.setVisible(isLock);
				tfSendChat.setVisible(isLock);
				btSendChat.setVisible(isLock);
				
//				lbQuestionEnemy.setVisible(isLock);
//				lbAnswer.setVisible(isLock);
//				lbAnswerValue.setVisible(isLock);
	        }
		});	
	}

	@Override
	public void error(String body) {
		LOG.error(body);
	}

	@Override
	public void error(Exception ex) {
		LOG.error(ex.getMessage(), ex);
	}

	
	// --------------------------------------------
	// -- 
	// -- 	Metodos auxiliares
	// --
	// --------------------------------------------
	
	/**
	 * Envia uma mensagem (Bundle-JSON) generica para o servidor
	 * 
	 * @param head
	 * @param textToSend
	 */
	private void sendMessage(Bundle bundle) {		
		try {
			String json = bundle.toJson();			
//			LOG.debug(json);		
			if(clientCreator != null){
				clientCreator.send(json);
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	private void sendMessage(String head, String textToSend) {		
		try {
			Bundle bundle = new Bundle(head, textToSend);
			sendMessage(bundle);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Encerra a aplicacao
	 */
	public void finish() {
		Response resp = FXOptionPane.showConfirmDialog(stage,
				"Deseja encerrar a Aplicação?", "Sair", "Sim", "Não");
		
		if(resp == Response.YES){
			turnOff();
			stage.close();
	//		Platform.exit();
			System.exit(0); // FIXME Procurar um metodo menos "agressivel"
		}
	}

	private void stopGame() {
        statusVisibilityFieldsAnswer(false);
		statusVisibilityFieldsQuestion(false);
		statusVisibilityFieldsImage(false);
		mniStartGame.setDisable(true);
		mniRiddle.setDisable(true);
	}
}
