package com.aplicacao;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import com.nearinfinity.examples.zookeeper.misc.ConnectionWatcher;

public class Operador extends ConnectionWatcher {

	public String nome;
	public String node;
	public Stat stat = new Stat();
	
    public void setDataByteArray(byte[] value) throws KeeperException, InterruptedException {
    	zk.setData(this.node, value, stat.getVersion());
    }
    
    public byte[] getDataByteArray() throws KeeperException, InterruptedException {
    	return zk.getData(this.node, false, stat);
    }
    
    public void setDataInt(int value) throws KeeperException, InterruptedException {
    	byte[] data = ByteBuffer.allocate(4).putInt(value).array();
    	this.setDataByteArray(data);
    }
    
    public int getDataInt() throws KeeperException, InterruptedException {
    	byte[] data = this.getDataByteArray();
    	if (data != null)
    		return ByteBuffer.wrap(data).getInt();
    	return 0;
    }
    
    public void setDataFloat(float value) throws KeeperException, InterruptedException {
    	byte[] data = ByteBuffer.allocate(8).putFloat(customRound(value, 2)).array();
    	this.setDataByteArray(data);
    }
    
    public float getDataFloat() throws KeeperException, InterruptedException {
    	byte[] data = this.getDataByteArray();
    	if (data != null)
    		return ByteBuffer.wrap(data).getFloat();
    	return 0;
    }
    
    public static Float customRound(float value, int dec) {
        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(dec, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
      }
}
