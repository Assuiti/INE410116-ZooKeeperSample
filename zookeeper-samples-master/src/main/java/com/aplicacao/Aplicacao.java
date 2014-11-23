package com.aplicacao;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class Aplicacao {
	
	public static void main(String[] args) throws Exception {
		
		Servidor servidor = criarServidor();
		
        String topZnodePath = "/conta";
        
        criarNode(servidor, topZnodePath);
        
        ClienteThread cliente1 = new ClienteThread();
        cliente1.cliente = new Cliente("1", topZnodePath);
        cliente1.start();
        
        ClienteThread cliente2 = new ClienteThread();
        cliente2.cliente = new Cliente("2", topZnodePath);
        cliente2.start();
        
        ClienteThread cliente3 = new ClienteThread();
        cliente3.cliente = new Cliente("3", topZnodePath);
        cliente3.start();
	}
	
	public static Servidor criarServidor() throws IOException, InterruptedException {
		Servidor servidor = new Servidor();
		if (servidor.get_zooKeeper().getState() == ZooKeeper.States.CONNECTED)
			System.out.println("Conectado ao zooKeeper.");
		return servidor;
	}
	
	public static void criarNode(Servidor servidor, String nome) throws KeeperException, InterruptedException {
        if (servidor.get_zooKeeper().exists(nome, false) == null) {
            System.out.println("Node criado: " + nome);
            servidor.get_zooKeeper().create(nome, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
	}
}
