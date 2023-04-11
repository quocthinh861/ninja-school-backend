// 
// Decompiled by Procyon v0.5.36
// 

package com.Server.io;

public interface IMessageHandler
{
    void onConnectOK();
    
    void onConnectionFail();
    
    void onDisconnected();
    
    void onMessage(final Message p0);
}
