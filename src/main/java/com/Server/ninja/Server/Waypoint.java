 package com.Server.ninja.Server;

public class Waypoint
{
    protected short minX;
    protected short minY;
    protected short maxX;
    protected short maxY;
    protected short goX;
    protected short goY;
    protected Map mapGo;
    
    protected Waypoint(final short minX, final short minY, final short maxX, final short maxY, final short goX, final short goY, final Map mapGo) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.goX = goX;
        this.goY = goY;
        this.mapGo = mapGo;
    }
}
