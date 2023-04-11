// 
// Decompiled by Procyon v0.5.36
// 

package com.Server.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class Message
{
    private byte command;
    private ByteArrayOutputStream os;
    private DataOutputStream dos;
    private ByteArrayInputStream is;
    private DataInputStream dis;
    
    public Message(final int command) {
        this((byte)command);
    }
    
    public Message(final byte command) {
        this.command = command;
        this.os = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(this.os);
    }
    
    public Message(final byte command, final byte[] data) {
        this.command = command;
        this.is = new ByteArrayInputStream(data);
        this.dis = new DataInputStream(this.is);
    }
    
    public byte getCommand() {
        return this.command;
    }
    
    public void setCommand(final int cmd) {
        this.setCommand((byte)cmd);
    }
    
    public void setCommand(final byte cmd) {
        this.command = cmd;
    }
    
    public byte[] getData() {
        return this.os.toByteArray();
    }
    
    public DataInputStream reader() {
        return this.dis;
    }
    
    public DataOutputStream writer() {
        return this.dos;
    }
    
    public void cleanup() {
        try {
            if (this.dis != null) {
                this.dis.close();
            }
            if (this.dos != null) {
                this.dos.close();
            }
        }
        catch (IOException ex) {}
    }
}
