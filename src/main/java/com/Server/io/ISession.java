// 
// Decompiled by Procyon v0.5.36
// 

package com.Server.io;

public interface ISession
{
    boolean isConnected();
    
    void sendMessage(final Message p0);
    
    void close();
}
