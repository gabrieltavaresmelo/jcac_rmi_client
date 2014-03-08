package br.com.gtmf.window;

import java.util.Map;

import br.com.gtmf.model.Bundle;

public interface ListenerWindow {

	public void lvUsers(Map<String, String> users);
	
	public void receive(Bundle message);

	public void setConnected(boolean isConnected);

	public void setLockToConnectUI(boolean isLock);
	
	public void error(String body);
	
	public void error(Exception ex);
	
}
