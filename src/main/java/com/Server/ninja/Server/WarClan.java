package com.Server.ninja.Server;

import com.Server.ninja.template.MapTemplate;

import java.util.ArrayList;

public class WarClan {
    protected Map[] maps;
    protected byte warClanID;
    protected int coinTotal;
    protected ArrayList<Integer> blacks;
    protected ArrayList<Integer> white;
    protected final ArrayList<Char> AChar;

    protected Clan clanBlack;
    protected Clan clanWhite;
    protected String clanBlacks;
    protected String clanWhites;
    protected byte typeWin;
    protected int timeLength;
    protected boolean isFinght;
    protected boolean isInvite;
    protected boolean isWait;
    protected static final String[] TITLE;
    private static final short[] arrMapId;
    protected static int setTimeChien;
    public int pointBlack;
    public int pointWhite;
    protected static byte baseId;
    protected static final Object LOCK;
    protected static ArrayList<WarClan> arrWarClan;

    public WarClan() {
        this.white = new ArrayList<>();
        this.blacks = new ArrayList<>();
        this.AChar = new ArrayList<>();
        this.clanBlacks = "";
        this.clanWhites = "";
        this.pointBlack = 0;
        this.pointWhite = 0;
        this.timeLength = 0;
        this.typeWin = -1;
    }

    protected static WarClan setWarClan() {
        synchronized (WarClan.LOCK) {
            final WarClan warClan;
            final WarClan war = warClan = new WarClan();
            final byte baseId = WarClan.baseId;
            WarClan.baseId = (byte) (baseId + 1);
            warClan.warClanID = baseId;
            war.maps = new Map[WarClan.arrMapId.length];
            for (byte i = 0; i < WarClan.arrMapId.length; ++i) {
                final MapTemplate template = GameScr.mapTemplates[WarClan.arrMapId[i]];
                war.maps[i] = new Map(template.mapID, (byte) 20, template.numZone);
                war.maps[i].warClan = war;
            }
            for (byte i = 0; i < war.maps.length; ++i) {
                final Map map = war.maps[i];
                if (map != null) {
                    for (byte j = 0; j < map.tileMaps.length; ++j) {
                        final TileMap tileMap = map.tileMaps[j];
                        if (tileMap != null) {
                            tileMap.initWaypoint(1);
                        }
                    }
                }
            }
            for (byte i = 0; i < war.maps.length; ++i) {
                final Map map = war.maps[i];
                if (map != null) {
                    map.start();
                }
            }
            war.timeLength = (int) (System.currentTimeMillis() / 1000L + 300);
            WarClan.arrWarClan.add(war);
            return war;
        }
    }

    protected static Map getMap(final WarClan war, final short mapId) {
        for (byte i = 0; i < war.maps.length; ++i) {
            if (war.maps[i].template.mapID == mapId) {
                return war.maps[i];
            }
        }
        return null;
    }

    protected void addChar(final Char _char) {
        synchronized (this.AChar) {
            this.AChar.add(_char);
        }
    }

    protected void removeChar(final Char _char) {
        synchronized (this.AChar) {
            for (byte i = 0; i < this.AChar.size(); ++i) {
                final Char player = this.AChar.get(i);
                if (player != null && player.charID == _char.charID) {
                    this.AChar.remove(i);
                }
            }
        }
    }

    protected static void close() {
        synchronized (WarClan.LOCK) {
            for (int i = WarClan.arrWarClan.size() - 1; i >= 0; --i) {
                final WarClan war = WarClan.arrWarClan.get(i);
                WarClan.arrWarClan.remove(i);
                war.CLOSE();
            }
        }
    }

    protected void finish(final byte type) {
        this.isFinght = false;
        final TileMap tileMap = this.maps[1].tileMaps[0];
        final long coin = this.coinTotal * 2L * 9L / 10L;
        if (type == -1) {
            clanWhite.updateCoin(coin / 2L);
            clanBlack.updateCoin(coin / 2L);
            for (final Char player : AChar) {
                if (player != null && player.user != null && player.user.session != null) {
                    Service.ServerMessage(player, "Hai gia tộc hoà nhau và nhận lại " + coin + " xu gia tộc.");
                }
            }
        } else if (type == 0) {
            clanWhite.updateCoin(coin);
            for (final Char player : AChar) {
                if (player != null && player.user != null && player.user.session != null) {
                    Service.ServerMessage(player, "Gia tộc " + clanWhite.name + " giành chiến thắng và nhận được " + coin + " xu gia tộc.");
                }
            }
        } else if (type == 1) {
            clanBlack.updateCoin(coin);
            for (final Char player : AChar) {
                if (player != null && player.user != null && player.user.session != null) {
                    Service.ServerMessage(player, "Gia tộc " + clanBlack.name + " giành chiến thắng và nhận được " + coin + " xu gia tộc.");
                }
            }
        }
    }

    protected void CLOSE() {
        if (isWait) {
            final TileMap tileMap = this.maps[0].tileMaps[0];
            for (short i = 0; i < tileMap.numPlayer; ++i) {
                final Char player = tileMap.aCharInMap.get(i);
                if (player != null && player.user != null && player.user.session != null) {
                    Service.ServerMessage(player, "Trận đấu bị huỷ bỏ.");
                }
            }
        } else if (this.isFinght) {
            if (pointWhite == pointBlack) {
                this.finish((byte) (-1));
            } else if (pointWhite > pointBlack) {
                this.finish((byte) (0));
            } else {
                this.finish((byte) (1));
            }
        }
        for (byte i = 0; i < this.maps.length; ++i) {
            final Map map = this.maps[i];
            if (map != null) {
                for (byte j = 0; j < map.tileMaps.length; ++j) {
                    final TileMap tileMap = map.tileMaps[j];
                    if (tileMap != null) {
                        try {
                            tileMap.lock.lock("Close war clan");
                            try {
                                for (short k = (short) (tileMap.aCharInMap.size() - 1); k >= 0; --k) {
                                    final Char player = tileMap.aCharInMap.get(k);
                                    if (player != null && player.isHuman) {
                                        final Map ltd = MapServer.getMapServer(player.mapLTDId);
                                        if (ltd != null) {
                                            final TileMap tile = ltd.getSlotZone(player);
                                            if (tile == null) {
                                                GameCanvas.startOKDlg(player.user.session, Text.get(0, 9));
                                            } else {
                                                player.tileMap.EXIT(player);
                                                player.cx = tile.map.template.goX;
                                                player.cy = tile.map.template.goY;
                                                player.cHP = player.cMaxHP();
                                                player.cMP = player.cMaxMP();
                                                player.statusMe = 1;
                                                Service.MeLive(player);
                                                tile.Join(player);
                                            }
                                        }
                                    }
                                }
                            } finally {
                                tileMap.lock.unlock();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        for (byte i = 0; i < this.maps.length; ++i) {
            final Map map = this.maps[i];
            if (map != null) {
                map.close();
            }
        }
    }

    protected void WarClanMessage(final String str) {
        for (byte i = 0; i < this.maps.length; ++i) {
            final Map map = this.maps[i];
            if (map != null) {
                for (byte j = 0; j < map.tileMaps.length; ++j) {
                    final TileMap tileMap = map.tileMaps[j];
                    if (tileMap != null) {
                        try {
                            tileMap.lock.lock("Chat gtc");
                            try {
                                for (short k = (short) (tileMap.aCharInMap.size() - 1); k >= 0; --k) {
                                    final Char player = tileMap.aCharInMap.get(k);
                                    if (player != null && player.user != null && player.user.session != null) {
                                        Service.ServerMessage(player, str);
                                    }
                                }
                            } finally {
                                tileMap.lock.unlock();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    protected static void upPointWarClan(final Char _char, short pointPlus) {
        if (_char.tileMap.map.isWarClanMap()) {
            if (_char.pointWarClan + pointPlus > 32767) {
                pointPlus = 0;
            }
            _char.pointWarClan += pointPlus;
            if (_char.clan.typeWar == 4) {
                _char.tileMap.map.warClan.pointWhite += pointPlus;
            } else if (_char.clan.typeWar == 5) {
                _char.tileMap.map.warClan.pointBlack += pointPlus;
            }
            Service.pointChienTruong(_char, _char.pointWarClan);
        }
    }

    static {
        arrMapId = new short[]{117, 118, 119, 120, 121, 122, 123, 124};
        LOCK = new Object();
        WarClan.setTimeChien = 3600;
        TITLE = new String[]{"Học Giả", "Hạ Nhẫn", "Trung Nhẫn", "Thượng nhẫn", "Nhẫn Giả"};
        WarClan.arrWarClan = new ArrayList<>();
    }
}
