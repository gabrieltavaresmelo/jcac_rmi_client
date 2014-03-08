package br.com.gtmf.controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.gtmf.model.Bundle;
import br.com.gtmf.rmi.IServerController;
import br.com.gtmf.rmi.Notify;
import br.com.gtmf.window.ListenerWindow;

/**
 * Classe que implementa a interface remota Notify.
 * 
 * 
 * Recebe as mensagens vindas do Servidor (Notify)
 * Envia mensagens para o servidor (IServerController)
 * 
 * 
 * @author Gabriel Tavares
 *
 */
public class ClientController extends UnicastRemoteObject implements Notify {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);
	
	private boolean connected = false;
	
	private ListenerWindow listener = null;
	private IServerController server = null;
	
	private String ipServer;
	private String username;
	
	
	public ClientController() throws RemoteException {
		super();
	}
	
	public void setListener(ListenerWindow listener) {
		this.listener = listener;
	}
	
	public void setConnected(boolean isConnected) {
		this.connected = isConnected;
	}
	
	public boolean isConnected() {
		return connected;
	}

	@Override
	public String getUsername() throws RemoteException {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setIpServer(String ipServer) {
		this.ipServer = ipServer;
	}
	
	public String getIpServer() {
		return ipServer;
	}

	public void setRefServer(IServerController server) {
		this.server = server;
	}
	
	public IServerController getRefServer() {
		return server;
	}

	public void connect() throws RemoteException {
		if(server != null && !connected){ 
			server.onOpen(this, username);
		}
	}

	public void close() throws RemoteException {
		if(server != null && connected){    
			try {
				outMessage();
				server.onClose(this, username);
				
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}			
		}
	}
	
	public void send(String message) {
		if(server != null && connected){
			try {
				server.onMessage(this, message);
				
			} catch (RemoteException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	// Recebe a confirmacao de que entrou
	@Override
	public void onOpen(String name) throws RemoteException {
		try {			
			LOG.debug(username + " conectado.");
			
			username = name;
			connected = true;
			inMessage();

			if(listener != null){
				listener.setConnected(connected);
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	// Mensagem recebida
	@Override
	public void onMessage(String message) throws RemoteException {
		try {
			Bundle bundle = new Bundle(message);
			
			switch (bundle.getHead()) {
			case Bundle.LIST_USERS_ON:
				if(listener != null)
					listener.lvUsers(bundle.getUsersOn());
				
				break;
				
			default:
				if(listener != null)
					listener.receive(bundle);
				
				break;
			}
			
		} catch (Exception e) {
			LOG.warn("Formato de mensagem nao identificado!");
		}
	}

	// Recebe a confirmacao de que saiu
	@Override
	public void onClose(String name) throws RemoteException {
		try {
			LOG.debug(username + " desconectado");
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	private void outMessage() throws Exception {
		if(server != null && connected){
			Bundle bundle = new Bundle(Bundle.USER_OUT, username);
			send(bundle.toJson());
		}
	}
	
	private void inMessage() throws Exception {
		if(server != null && connected){
			Bundle bundle = new Bundle(Bundle.USER_IN, username);
			send(bundle.toJson());
		}
	}
}
