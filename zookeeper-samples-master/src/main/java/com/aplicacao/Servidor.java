package com.aplicacao;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.zookeeper.ZooKeeper;

public class Servidor {

    private ConnectionHelper _connectionHelper;
    private ZooKeeper _zooKeeper;
    private static EmbeddedZooKeeperServer _embeddedServer;
    private static File _dataDirectory;
    public static final int ZK_PORT = 2181;
    public static final String ZK_CONNECTION_STRING = "localhost:" + ZK_PORT;
    
	public Servidor() throws IOException, InterruptedException {
        String dataDirectoryName = "C:\\target\\zookeeper-data";
        _dataDirectory = new File(dataDirectoryName);
        //deleteDataDirectoryIfExists();

        _embeddedServer = new EmbeddedZooKeeperServer(ZK_PORT, dataDirectoryName, 750);
        _embeddedServer.start();
        
        _connectionHelper = new ConnectionHelper();  
        _zooKeeper = _connectionHelper.connect(ZK_CONNECTION_STRING);
	}
    
    private static void deleteDataDirectoryIfExists() throws IOException {
        if (_dataDirectory.exists()) {
            FileUtils.deleteDirectory(_dataDirectory);
        }
    }	
    
	@Override
	protected void finalize() throws Throwable {
        if (_zooKeeper != null) {
            System.out.printf("Closing zooKeeper with session id: %d\n", _zooKeeper.getSessionId());
            _zooKeeper.close();
            _zooKeeper = null;
        }		
        _embeddedServer.shutdown();
        deleteDataDirectoryIfExists();
		super.finalize();
	}

	/**
	 * @return the _connectionHelper
	 */
	public ConnectionHelper get_connectionHelper() {
		return _connectionHelper;
	}

	/**
	 * @param _connectionHelper the _connectionHelper to set
	 */
	public void set_connectionHelper(ConnectionHelper _connectionHelper) {
		this._connectionHelper = _connectionHelper;
	}

	/**
	 * @return the _zooKeeper
	 */
	public ZooKeeper get_zooKeeper() {
		return _zooKeeper;
	}

	/**
	 * @param _zooKeeper the _zooKeeper to set
	 */
	public void set_zooKeeper(ZooKeeper _zooKeeper) {
		this._zooKeeper = _zooKeeper;
	}

	/**
	 * @return the _embeddedServer
	 */
	public static EmbeddedZooKeeperServer get_embeddedServer() {
		return _embeddedServer;
	}

	/**
	 * @param _embeddedServer the _embeddedServer to set
	 */
	public static void set_embeddedServer(EmbeddedZooKeeperServer _embeddedServer) {
		Servidor._embeddedServer = _embeddedServer;
	}

	/**
	 * @return the _dataDirectory
	 */
	public static File get_dataDirectory() {
		return _dataDirectory;
	}

	/**
	 * @param _dataDirectory the _dataDirectory to set
	 */
	public static void set_dataDirectory(File _dataDirectory) {
		Servidor._dataDirectory = _dataDirectory;
	}

	/**
	 * @return the zkPort
	 */
	public static int getZkPort() {
		return ZK_PORT;
	}

	/**
	 * @return the zkConnectionString
	 */
	public static String getZkConnectionString() {
		return ZK_CONNECTION_STRING;
	}	
}