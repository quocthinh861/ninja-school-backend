// 
// Decompiled by Procyon v0.5.36
// 

package com.Server.io;

import java.util.concurrent.TimeUnit;

public class Lock
{
    private boolean isLocked;
    private Thread lockedBy;
    private int lockedCount;
    private long timeLock;
    private Thread thread;
    private boolean isSetup;
    private final Object LOCK;
    private final int lockId;
    private String logs;
    private static final int MaxTimeOut = 5000;
    private static final int DELAY = 1000;
    private static int baseId;
    private static final Object L;
    
    public Lock() {
        this.isLocked = false;
        this.lockedBy = null;
        this.lockedCount = 0;
        this.timeLock = -1L;
        this.thread = null;
        this.isSetup = false;
        this.LOCK = new Object();
        this.logs = "";
        synchronized (Lock.L) {
            this.lockId = Lock.baseId++;
        }
    }
    
    public void setUp() {
        if (!this.isSetup) {
            this.isSetup = true;
            (this.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (Lock.this.isSetup) {
                        try {
                            if (Lock.this.timeLock != -1L && Lock.this.timeLock + 5000L <= System.currentTimeMillis()) {
                                Lock.this.unlock();
                                System.err.println("Unlock timeout " + Lock.this.logs);
                            }
                            TimeUnit.MILLISECONDS.sleep(1000L);
                        }
                        catch (InterruptedException var2) {
                            var2.printStackTrace();
                        }
                    }
                }
            })).start();
        }
    }
    
    public synchronized void lock(final String logs) throws InterruptedException {
        final Thread callingThread = Thread.currentThread();
        while (this.isLocked && this.lockedBy != callingThread) {
            this.logs = logs;
            this.wait(5000L);
        }
        this.isLocked = true;
        ++this.lockedCount;
        this.lockedBy = callingThread;
        this.timeLock = System.currentTimeMillis();
    }
    
    public void unlock() {
        this.unlocked();
    }
    
    public synchronized void unlocked() {
        if (this.isLocked) {
            --this.lockedCount;
            if (this.lockedCount <= 0) {
                this.isLocked = false;
                this.timeLock = -1L;
                this.notify();
            }
        }
    }
    
    public synchronized void unlockAll() {
        if (this.isLocked) {
            this.lockedCount = 0;
            this.isLocked = false;
            this.notifyAll();
        }
    }
    
    public void close() {
        this.unlockAll();
        this.isSetup = false;
    }
    
    static {
        Lock.baseId = 0;
        L = new Object();
    }
}
