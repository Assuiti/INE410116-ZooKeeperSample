package com.aplicacao;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.recipes.lock.LockListener;
import org.apache.zookeeper.recipes.lock.WriteLock;

public class BlockingWriteLock {

    private String _name;
    private String _path;
    private WriteLock _writeLock;
    private CountDownLatch _lockAcquiredSignal = new CountDownLatch(1);

    public static final List<ACL> DEFAULT_ACL = ZooDefs.Ids.OPEN_ACL_UNSAFE;

    public BlockingWriteLock(String name, ZooKeeper zookeeper, String path) {
        this(name, zookeeper, path, DEFAULT_ACL);
    }

    public BlockingWriteLock(String name, ZooKeeper zookeeper, String path, List<ACL> acl) {
        _name = name;
        _path = path;
        _writeLock = new WriteLock(zookeeper, path, acl, new SyncLockListener());
    }

    @SuppressWarnings("static-access")
	public void lock() throws InterruptedException, KeeperException {
        _writeLock.lock();
    	Thread.currentThread().sleep(50);
        _lockAcquiredSignal.await();
    }

    @SuppressWarnings("static-access")
    public boolean lock(long timeout, TimeUnit unit) throws InterruptedException, KeeperException {
    	_writeLock.lock();
    	Thread.currentThread().sleep(50);
        return _lockAcquiredSignal.await(timeout, unit);
    }

    public boolean tryLock() throws InterruptedException, KeeperException {
        return lock(2, TimeUnit.SECONDS);
    }

    @SuppressWarnings("static-access")
    public void unlock() throws InterruptedException {
    	Thread.currentThread().sleep(50);
    	String temp1 = _writeLock.getId();
    	String temp2 = _writeLock.getDir();
    	
        try {
			_writeLock.unlock();
		} catch (IllegalArgumentException e) {
			System.out.println("------------------" + temp1 + " --- " + temp2);
			e.printStackTrace();
		}
    	Thread.currentThread().sleep(50);
    }

    class SyncLockListener implements LockListener {

        @Override
        public void lockAcquired() {
            _lockAcquiredSignal.countDown();
        }

        @Override
        public void lockReleased() {
        }
    }
}
