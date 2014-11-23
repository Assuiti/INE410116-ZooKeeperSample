package com.aplicacao;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

public class Cliente extends Operador {

    public static final int DEPOSITAR = 1;
    public static final int RETIRAR = 2;
    public static final int CORRIGIR = 3;
    
	public Cliente(String nome, String node) {
		this.nome = nome;
		this.node = node;
	}

	public String getNode() {
		return node;
	}
	
    public void executar() throws Exception {
        try {
    		System.out.println("_________________________________________________");
        	
        	connect(Servidor.ZK_CONNECTION_STRING);
    		System.out.println("-------------------------------------");
        	
            for (int index = 1; index <= 40; index++) {
                enviarMensagem(index);
            }
            System.out.println("AEEEEEEEEEEEEEEEEEEEEEEE");
            close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }	
    
    public void enviarMensagem(int index) throws KeeperException, InterruptedException, IOException {
    	
    	BlockingWriteLock lock = null;
    	
		try {
			lock = new BlockingWriteLock(this.node, zk, this.node);
			if (lock != null)
				lock.tryLock();

	    	int op = sortearOperacao();
	    	float dataOLD = getDataFloat();
	    	float dataNEW = 0;
	    	
	    	switch (op) {
			case DEPOSITAR:
				dataNEW = dataOLD + 100;
				break;

			case RETIRAR:
				dataNEW = dataOLD - 40;
				break;

			case CORRIGIR:
				dataNEW = dataOLD + (dataOLD / 100 * 10);
				break;

			default:
				break;
			}
    	
	    	log(index, op, dataOLD, dataNEW);
	    	setDataFloat(dataNEW);
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		} 
		finally {
			if (lock != null)
				lock.unlock();
		}    	
    }
    
    private Integer sortearOperacao() {
        int cdOperacao = ((int) (3 * Math.random())) + 1;
        return cdOperacao;
    }
    
    public void log(int index, int op, float dataOLD, float dataNEW) {
    	String strOP = null;
    	switch (op) {
		case DEPOSITAR:
			strOP = "DEPOSITAR";
			break;

		case RETIRAR:
			strOP = "RETIRAR  ";
			break;

		case CORRIGIR:
			strOP = "CORRIGIR ";
			break;

		default:
			break;
		}
    	System.out.println("Cliente: " + nome + "  " + this.node + " Index: " + index + " OP: " + strOP + " Data: " + dataOLD + " Saldo: " + dataNEW);
    }
}