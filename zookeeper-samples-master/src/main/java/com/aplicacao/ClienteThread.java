package com.aplicacao;

public class ClienteThread extends Thread {

	public Cliente cliente = null;
	
	@Override
	public void run() {
		try {
			if (cliente != null)
				cliente.executar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
