 package com.Server.ninja.Server;

public class BuNhin {
    protected int charID;
    protected short x;
    protected short y;
    protected String name;
    protected boolean isInjure;
    protected int hp;
    protected short dx;
    protected short dy;
    protected int timeRemove;

    public BuNhin(final int charID, final String name, final short x, final short y, final int hp, final short dx, final short dy, final int remove) {
        this.charID = charID;
        this.x = x;
        this.y = y;
        this.name = name;
        this.hp = hp;
        this.dx = dx;
        this.dy = dy;
        this.timeRemove = remove;
    }
}
