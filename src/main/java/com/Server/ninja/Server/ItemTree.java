 package com.Server.ninja.Server;

public class ItemTree
{
    public int idTree;
    public int xTree;
    public int yTree;
    public short x;
    public short y;
    
    protected ItemTree(final int x, final int y) {
        final short x2 = (short)x;
        this.x = x2;
        this.xTree = x2 * 24 + 12;
        final short y2 = (short)y;
        this.y = y2;
        this.yTree = y2 * 24 + 24 + 2;
    }
}
