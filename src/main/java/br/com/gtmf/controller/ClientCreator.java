package br.com.gtmf.controller;

import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.gtmf.rmi.ConstantsRmi;
import br.com.gtmf.rmi.IServerController;
import br.com.gtmf.rmi.RmiCreator;
import br.com.gtmf.window.ListenerWindow;

/**
 * Inicializa o Cliente RMI
 * 
 * 
 * @author Gabriel Tavares
 *
 */
public class ClientCreator extends RmiCreator {
	
	private static final Logger LOG = LoggerFactory.getLogger(ClientCreator.class);
	
	private String username;
	private String ipServer;
	private int port = ConstantsRmi.SERVICE_PORT;
	private ListenerWindow listener;
	
	private ClientController clientController;
	private Registry registry;
	

	public ClientCreator(String nome, String ip, int porta, ListenerWindow listener) {
		super(ClientController.class);
		
		this.username = nome;
		this.ipServer = ip;
		this.port = porta;
		this.listener = listener;

		customRmi();
	}

	@Override
	public boolean customRmi() {
		try {			
			clientController = new ClientController();
			clientController.setListener(listener);
			
			// Obtem a referencia ao Registrador de Nomes atraves do IP e Porta
			registry = LocateRegistry.getRegistry(ipServer, port);
			
			// Mapeia o objeto em um nome
			registry.rebind(username, clientController);
			
			try {
				// Obtem uma referencia de um determinado objeto
				IServerController server = (IServerController) registry
						.lookup(ConstantsRmi.SERVICE_NAME);

				clientController.setRefServer(server);
				clientController.setUsername(username);
				clientController.setIpServer(ipServer);
				clientController.connect();
				clientController.setConnected(true);
				
			} catch (NotBoundException e) {
				clientController.setConnected(false);
//				LOG.error(e.getMessage(), e);
				LOG.error(e.getMessage());
				registry.unbind(username);
				return false;
			}

		} catch (Exception e) {
			clientController.setConnected(false);
//			LOG.error(e.getMessage(), e);
			LOG.error(e.getMessage());
			return false;
		}
		
		return true;
	}

	public void send(String json) {
		clientController.send(json);
	}

	public void close() {
		try {
			if(registry != null){
				clientController.close();
				clientController = null;
				registry.unbind(username);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}		
	}
	
	public boolean isConectado() {
		return clientController == null ? false : clientController.isConnected();
	}

	public String getUsername() {
		return this.username;
	}

	public String getIpServer() {
		return this.ipServer + ":" + this.port;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
