 package com.Server.ninja.Server;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemMap
{
    protected short itemMapID;
    protected Item item;
    protected int playerId;
    protected long timeRemove;
    protected short x;
    protected short y;
    protected boolean isItemTask;
    
    protected ItemMap(final Item item, final int playerId, final int timeRemove, final short x, final short y, final boolean isItemTask) {
        this.item = null;
        this.playerId = 0;
        this.timeRemove = -1L;
        this.item = item;
        this.playerId = playerId;
        this.timeRemove = timeRemove + System.currentTimeMillis();
        this.x = x;
        this.y = y;
        this.isItemTask = isItemTask;
    }
}
