 package com.Server.ninja.Server;

import java.util.ArrayList;

public class Party
{
    private static int baseId;
    protected static final byte MAX_PLAYER = 6;
    protected int partyId;
    protected int charID;
    protected byte numPlayer;
    protected ArrayList<Char> aChar;
    protected boolean isLock;
    protected final Object LOCK;
    private static final Object LOCK_LOCAL;
    
    protected Party(final Char _char) {
        this.numPlayer = 0;
        this.aChar = new ArrayList<>();
        this.isLock = false;
        this.LOCK = new Object();
        synchronized (Party.LOCK_LOCAL) {
            this.partyId = Party.baseId++;
            ++this.numPlayer;
            this.aChar.add(_char);
            this.charID = _char.charID;
        }
    }
    
    protected void addPlayerParty(final Char _char) {
        synchronized (this.LOCK) {
            ++this.numPlayer;
            this.aChar.add(_char);
            _char.party = this;
        }
    }
    
    protected void removePlayer(final int charId) {
        synchronized (this.LOCK) {
            byte i = 0;
            while (i < this.numPlayer) {
                final Char _char = this.aChar.get(i);
                if (_char != null && _char.charID == charId) {
                    --this.numPlayer;
                    this.aChar.remove(_char);
                    _char.party = null;
                    Service.OutParty(_char);
                    if (_char.tileMap.getNumPlayerParty(this.partyId) < 1) {
                        _char.tileMap.removeParty(this.partyId);
                        break;
                    }
                    if (this.numPlayer > 0 && _char.charID == this.charID) {
                        this.charID = this.aChar.get(0).charID;
                        for (byte j = 0; j < this.numPlayer; ++j) {
                            final Char player = this.aChar.get(j);
                            if (player != null) {
                                Service.ServerMessage(player, String.format(Text.get(0, 109), this.aChar.get(0).cName));
                            }
                        }
                        break;
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
        }
        this.refreshPlayer();
    }
    
    protected Char findChar(final int charID) {
        for (byte i = 0; i < this.numPlayer; ++i) {
            final Char _char = this.aChar.get(i);
            if (_char != null && _char.charID == charID) {
                return _char;
            }
        }
        return null;
    }
    
    protected void clear() {
        synchronized (this.LOCK) {
            if (this.aChar != null) {
                for (byte i = 0; i < this.numPlayer; ++i) {
                    final Char player = this.aChar.get(i);
                    if (player != null && player.party != null) {
                        player.tileMap.removeParty(this.partyId);
                        player.party = null;
                    }
                }
                this.numPlayer = 0;
                this.aChar.clear();
                this.aChar = null;
            }
        }
    }
    
    protected void refreshLock() {
        synchronized (this.LOCK) {
            for (byte i = 0; i < this.numPlayer; ++i) {
                final Char player = this.aChar.get(i);
                if (player != null) {
                    Service.lockParty(player, this.isLock);
                }
            }
        }
    }
    
    protected void refreshPlayer() {
        synchronized (this.LOCK) {
            for (byte i = 0; i < this.numPlayer; ++i) {
                final Char player = this.aChar.get(i);
                if (player != null) {
                    Service.PlayerInParty(player, this);
                }
            }
        }
    }
    
    protected void TeamMessage(final String str) {
        synchronized (this.LOCK) {
            for (byte i = 0; i < this.numPlayer; ++i) {
                final Char player = this.aChar.get(i);
                if (player != null) {
                    Service.ServerMessage(player, str);
                }
            }
        }
    }
    
    protected void TeamOPenCave(final Char _char, final int caveId) {
        synchronized (this.LOCK) {
            for (byte i = 0; i < this.numPlayer; ++i) {
                final Char player = this.aChar.get(i);
                if (player != null && player.charID != _char.charID) {
                    player.partyCaveId = caveId;
                    Service.ServerMessage(player, String.format(Text.get(0, 263), _char.cName));
                }
            }
        }
    }

    protected boolean checkLevelParty(final Char _char) {
        synchronized (this.LOCK) {
            for (byte i = 0; i < this.numPlayer; ++i) {
                final Char player = this.aChar.get(i);
                if (player != null && player.charID != _char.charID && player.cLevel < 50) {
                    return true;
                }
            }
            return false;
        }
    }

    static {
        Party.baseId = 10000;
        LOCK_LOCAL = new Object();
    }
}
