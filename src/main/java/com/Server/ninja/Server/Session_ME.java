 package com.Server.ninja.Server;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import com.Server.io.Message;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import com.Server.io.ISession;
import com.Server.io.MySQL;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class Session_ME implements ISession
{
    private static final byte[] key;
    private static final int DELAY = 10;
    int delayOut;
    boolean isLockOut;
    static final int setBackupTime = 5000;
    int backupTime;
    boolean isLockBackup;
    private boolean getKeyComplete;
    protected Socket sc;
    protected DataInputStream dis;
    protected DataOutputStream dos;
    protected int id;
    protected User user;
    boolean connected;
    boolean login;
    private byte curR;
    private byte curW;
    private final Sender sender;
    private Thread collectorThread;
    protected Thread sendThread;
    protected int sendByteCount;
    protected int recvByteCount;
    byte client_type;
    protected byte zoomLevel;
    boolean isGPS;
    int width;
    int height;
    boolean isQwert;
    boolean isTouch;
    String plastfrom;
    int versionIp;
    byte languageId;
    int provider;
    String agent;
    protected int versionNja;
    
    protected Session_ME(final Socket sc, final int id) throws IOException {
        this.delayOut = 15000;
        this.isLockOut = false;
        this.backupTime = 60000;
        this.isLockBackup = false;
        this.getKeyComplete = false;
        this.sc = sc;
        this.id = id;
        this.dis = new DataInputStream(sc.getInputStream());
        this.dos = new DataOutputStream(sc.getOutputStream());
        this.connected = true;
        this.sendThread = new Thread(this.sender = new Sender());
        this.collectorThread = new Thread(new MessageCollector());
    }
    
    public void run() {
        this.sendThread.start();
        this.collectorThread.start();
    }
    
    @Override
    public boolean isConnected() {
        return this.connected;
    }
    
    @Override
    public void sendMessage(final Message message) {
        if (this.isConnected()) {
            this.sender.AddMessage(message);
        }
    }
    
    protected void doSendMessage(final Message m) {
        try {
            if (m != null) {
                final byte[] data = m.getData();
                byte b = m.getCommand();
                if (data != null) {
                    final int size = data.length;
                    if (size > 65535) {
                        b = -32;
                    }
                    if (this.getKeyComplete) {
                        this.dos.writeByte(this.writeKey(b));
                    }
                    else {
                        this.dos.writeByte(b);
                    }
                    if (b == -32) {
                        b = m.getCommand();
                        if (this.getKeyComplete) {
                            this.dos.writeByte(this.writeKey(b));
                        }
                        else {
                            this.dos.writeByte(b);
                        }
                        final int byte1 = this.writeKey((byte)(size >> 24));
                        this.dos.writeByte(byte1);
                        final int byte2 = this.writeKey((byte)(size >> 16));
                        this.dos.writeByte(byte2);
                        final int byte3 = this.writeKey((byte)(size >> 8));
                        this.dos.writeByte(byte3);
                        final int byte4 = this.writeKey((byte)(size & 0xFF));
                        this.dos.writeByte(byte4);
                    }
                    else if (this.getKeyComplete) {
                        final int byte1 = this.writeKey((byte)(size >> 8));
                        this.dos.writeByte(byte1);
                        final int byte2 = this.writeKey((byte)(size & 0xFF));
                        this.dos.writeByte(byte2);
                    }
                    else {
                        final int byte1 = (byte)(size & 0xFF00);
                        this.dos.writeByte(byte1);
                        final int byte2 = (byte)(size & 0xFF);
                        this.dos.writeByte(byte2);
                    }
                    if (this.getKeyComplete) {
                        for (int i = 0; i < size; ++i) {
                            data[i] = this.writeKey(data[i]);
                        }
                    }
                    this.sendByteCount += 5 + data.length;
                    this.dos.write(data);
                    Util.log("do mss " + b + " szie " + (this.sendByteCount + this.recvByteCount) / 1024 + "." + (this.sendByteCount + this.recvByteCount) % 1024 / 102 + " kb");
                }
                else {
                    this.sendByteCount += 5;
                }
                this.dos.flush();
            }
        }
        catch (Exception ex) {}
    }
    
    private byte readKey(final byte b) {
        final byte[] key = Session_ME.key;
        final byte curR = this.curR;
        this.curR = (byte)(curR + 1);
        final byte i = (byte)((key[curR] & 0xFF) ^ (b & 0xFF));
        if (this.curR >= Session_ME.key.length) {
            this.curR %= (byte)Session_ME.key.length;
        }
        return i;
    }
    
    private byte writeKey(final byte b) {
        final byte[] key = Session_ME.key;
        final byte curW = this.curW;
        this.curW = (byte)(curW + 1);
        final byte i = (byte)((key[curW] & 0xFF) ^ (b & 0xFF));
        if (this.curW >= Session_ME.key.length) {
            this.curW %= (byte)Session_ME.key.length;
        }
        return i;
    }
    
    @Override
    public void close() {
        try {
            final MySQL mySQL = new MySQL(1);
            if (this.user != null) {
                this.user.close();
                mySQL.stat.executeUpdate("UPDATE `user` SET `online`= 0 WHERE `userId`=" + user.userId + " LIMIT 1;");
                this.user = null;
            }
            Client.removeClinet(this);
            this.cleanNetwork();
            mySQL.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cleanNetwork() {
        this.curR = 0;
        this.curW = 0;
        try {
            this.connected = false;
            this.login = false;
            if (this.dos != null) {
                this.dos.close();
                this.dos = null;
            }
            if (this.dis != null) {
                this.dis.close();
                this.dis = null;
            }
            this.sendThread = null;
            this.collectorThread = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String toString() {
        if (this.user != null) {
            return this.user.toString();
        }
        return "Client " + this.id;
    }
    
    private void getKey() {
        try {
            final Message msg = new Message(-27);
            msg.writer().writeByte(Session_ME.key.length);
            for (int i = 0; i < Session_ME.key.length; ++i) {
                msg.writer().writeByte(Session_ME.key[i]);
            }
            this.doSendMessage(msg);
            msg.cleanup();
            this.getKeyComplete = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void disconnect() {
        if (this.isConnected()) {
            this.close();
        }
    }
    
    protected synchronized void setBackupTime(final int time, final boolean lock) {
        if (!this.isLockBackup) {
            this.backupTime = time;
            this.isLockBackup = lock;
        }
    }
    
    protected synchronized void setDelayOut(final int time, final boolean lock) {
        if (!this.isLockOut) {
            this.delayOut = time;
            this.isLockOut = lock;
        }
    }
    
    static {
        key = new byte[] { 68 };
    }
    
    private class Sender implements Runnable
    {
        private final ArrayList<Message> sendingMessage;
        
        protected Sender() {
            this.sendingMessage = new ArrayList<Message>();
        }
        
        protected void AddMessage(final Message message) {
            if (Session_ME.this.isConnected()) {
                this.sendingMessage.add(message);
            }
        }
        
        @Override
        public void run() {
            try {
                while (Session_ME.this.isConnected()) {
                    while (Session_ME.this.sender.sendingMessage.size() > 0 && Session_ME.this.isConnected()) {
                        final Message m = Session_ME.this.sender.sendingMessage.remove(0);
                        Session_ME.this.doSendMessage(m);
                    }
                    if (Session_ME.this.isConnected()) {
                        if (Session_ME.this.user != null && Session_ME.this.user.player != null) {
                            final Char charz = Session_ME.this.user.player.getMyChar();
                            if (charz == null || charz.tileMap != null) {}
                        }
                        if (Session_ME.this.backupTime > 0) {
                            final Session_ME this$0 = Session_ME.this;
                            this$0.backupTime -= 10;
                            if (Session_ME.this.backupTime <= 0) {
                                if (Session_ME.this.user != null) {
                                    Session_ME.this.user.flush();
                                }
                                Session_ME.this.backupTime = 5000;
                            }
                        }
                        if (Session_ME.this.delayOut > 0) {
                            final Session_ME this$2 = Session_ME.this;
                            this$2.delayOut -= 10;
                            if (Session_ME.this.delayOut <= 0) {
                                Session_ME.this.disconnect();
                            }
                        }
                        if (Server.backupTime != -1) {
                            Session_ME.this.setBackupTime(Server.backupTime, true);
                        }
                        if (Server.TimeDissConnect != -1) {
                            Session_ME.this.setDelayOut(Server.TimeDissConnect, true);
                        }
                    }
                    TimeUnit.MILLISECONDS.sleep(10L);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private class MessageCollector implements Runnable
    {
        @Override
        public void run() {
            try {
                while (Session_ME.this.connected) {
                    final Message message = this.readMessage();
                    if (!Server.start) {
                        break;
                    }
                    if (message.getCommand() == -27) {
                        Session_ME.this.getKey();
                    }
                    else {
                        Controller.onMessage(message, Session_ME.this);
                    }
                    message.cleanup();
                    TimeUnit.MILLISECONDS.sleep(10L);
                }
            }
            catch (Exception ex) {}
            Util.log("Error read message from client " + Session_ME.this + " size connection " + Client.sizeClients);
            Session_ME.this.disconnect();
        }
        
        private Message readMessage() throws Exception {
            byte cmd = Session_ME.this.dis.readByte();
            if (Session_ME.this.getKeyComplete) {
                cmd = Session_ME.this.readKey(cmd);
            }
            int size;
            if (cmd == -31) {
                size = Session_ME.this.dis.readShort();
            }
            else if (Session_ME.this.getKeyComplete) {
                final byte b1 = Session_ME.this.dis.readByte();
                final byte b2 = Session_ME.this.dis.readByte();
                size = ((Session_ME.this.readKey(b1) & 0xFF) << 8 | (Session_ME.this.readKey(b2) & 0xFF));
            }
            else {
                size = Session_ME.this.dis.readUnsignedShort();
            }
            final byte[] data = new byte[size];
            for (int len = 0, byteRead = 0; len != -1 && byteRead < size; byteRead += len) {
                len = Session_ME.this.dis.read(data, byteRead, size - byteRead);
                if (len > 0) {}
            }
            if (Session_ME.this.getKeyComplete) {
                for (int i = 0; i < data.length; ++i) {
                    data[i] = Session_ME.this.readKey(data[i]);
                }
            }
            final Session_ME this$0 = Session_ME.this;
            this$0.recvByteCount += 5 + size;
            final Message msg = new Message(cmd, data);
            return msg;
        }
    }
    
    public static void logIP() {
        try {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            for (int j = Client.clients.size() - 1; j >= 0; --j) {
                final Session_ME conn = Client.clients.get(j);
                if (conn != null && conn.user != null){
                    addIP(map, conn.user.ipUser);
                }
            }
            File fold = new File("listip.txt");
            fold.delete();
            for (String key : map.keySet()) {
                String a = key + " đăng nhập " + map.get(key) + " tài khoản.";
                File fnew = new File("listip.txt");
                FileWriter fw = new FileWriter(fnew,true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(a);
                bw.newLine();
                bw.close();
                fw.close();
            }
        } catch (IOException e) {
        }
    }
    
    public static void addIP(HashMap<String, Integer> map, String ipUser) {
        if (map.containsKey(ipUser)) {
            int count = map.get(ipUser) + 1;
            map.put(ipUser, count);
        } else {
            map.put(ipUser, 1);
        }
    }
    
    protected int checkIP(String str) {
        try {
            int sizeIP = 0;
            for (int j = Client.clients.size() - 1; j >= 0; --j) {
                final Session_ME conn = Client.clients.get(j);
                if (conn != null && conn.user != null){
                    if (str.equalsIgnoreCase(conn.user.ipUser)){
                        sizeIP++;
                    }
                }
            }
            return sizeIP;
        } catch (Exception e) {
            System.err.println("readIP ERROR");
            return 6;
        }
    }
    
}
