 package com.Server.ninja.Server;

import com.Server.ninja.template.MapTemplate;

import java.util.ArrayList;

public class TestDun
{
    protected Map[] maps;
    protected byte testdunID;
    protected int coinTotal;
    protected Char[] blacks;
    protected Char[] whites;
    protected String black;
    protected int level_black;
    protected String white;
    protected int level_white;
    protected byte typeWin;
    protected int timeLength;
    protected boolean isFinght;
    protected int numChar;
    protected int input_charId;
    private static final short[] arrMapId;
    protected static byte maxCreate;
    protected static int setTimeChuanBi;
    protected static int setTimeChien;
    protected static int minCoin;
    protected static byte baseId;
    protected static final Object LOCK;
    protected static ArrayList<TestDun> arrTestDun;

    public boolean isTalent;

    public TestDun() {
        this.black = "";
        this.level_black = 1;
        this.white = "";
        this.level_white = 1;
        this.typeWin = -1;
        this.isFinght = false;
        this.numChar = 0;
        this.input_charId = -9999;
    }
    
    protected static TestDun OpenTestDun() {
        synchronized (TestDun.LOCK) {
            final TestDun testDun;
            final TestDun test = testDun = new TestDun();
            final byte baseId = TestDun.baseId;
            TestDun.baseId = (byte)(baseId + 1);
            testDun.testdunID = baseId;
            test.coinTotal = 0;
            test.maps = new Map[TestDun.arrMapId.length];
            for (byte i = 0; i < TestDun.arrMapId.length; ++i) {
                final MapTemplate template = GameScr.mapTemplates[TestDun.arrMapId[i]];
                test.maps[i] = new Map(template.mapID, (byte)20, template.numZone);
                test.maps[i].testDun = test;
            }
            for (byte i = 0; i < test.maps.length; ++i) {
                final Map map = test.maps[i];
                if (map != null) {
                    for (byte j = 0; j < map.tileMaps.length; ++j) {
                        final TileMap tileMap = map.tileMaps[j];
                        if (tileMap != null) {
                            tileMap.initWaypoint(3);
                        }
                    }
                }
            }
            for (byte i = 0; i < test.maps.length; ++i) {
                final Map map = test.maps[i];
                if (map != null) {
                    map.start();
                }
            }
            test.timeLength = (int)(System.currentTimeMillis() / 1000L + TestDun.setTimeChuanBi);
            TestDun.arrTestDun.add(test);
            return test;
        }
    }
    
    protected static Map getMap(final TestDun test, final short mapId) {
        for (byte i = 0; i < test.maps.length; ++i) {
            if (test.maps[i].template.mapID == mapId) {
                return test.maps[i];
            }
        }
        return null;
    }
    
    protected void TestDunMessage(final String str) {
        for (byte i = 0; i < this.maps.length; ++i) {
            final Map map = this.maps[i];
            if (map != null) {
                for (byte j = 0; j < map.tileMaps.length; ++j) {
                    final TileMap tileMap = map.tileMaps[j];
                    if (tileMap != null) {
                        try {
                            tileMap.lock.lock("Chat loi dai");
                            try {
                                for (short k = (short)(tileMap.aCharInMap.size() - 1); k >= 0; --k) {
                                    final Char player = tileMap.aCharInMap.get(k);
                                    if (player != null && player.user != null && player.user.session != null) {
                                        Service.ServerMessage(player, str);
                                    }
                                }
                            }
                            finally {
                                tileMap.lock.unlock();
                            }
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    
    protected void complete(final byte type) {
        this.isFinght = false;
        this.timeLength = (int)(System.currentTimeMillis() / 1000L + 10L);
        final TileMap tileMap = this.maps[1].tileMaps[0];
        final long coin = this.coinTotal * 2L * 9L / 10L;
        if (type == -1) {
            for (short i = 0; i < tileMap.numPlayer; ++i) {
                final Char player = tileMap.aCharInMap.get(i);
                if (player != null && player.user != null && player.user.session != null) {
                    if (this.isTalent) {
                        Service.ServerMessage(player,"Hai đội hoà nhau mỗi bên được 1 điểm.");
                        if (player.cName.equals(this.white)) {
                            ++player.pointTalent;
                        }
                        else if (player.cName.equals(this.black)) {
                            ++player.pointTalent;
                        }
                    } else {
                        if (player.cName.equals(this.white)) {
                            player.user.player.upCoin(coin / 2L, (byte)1);
                        }
                        else if (player.cName.equals(this.black)) {
                            player.user.player.upCoin(coin / 2L, (byte)1);
                        }
                        Service.ServerMessage(player, String.format(Text.get(0, 303), this.white, this.black, coin / 2L));
                    }
                    Service.mapTime(player, (int)(this.timeLength - System.currentTimeMillis() / 1000L));
                }
            }
        }
        else if (type == 0) {
            for (short i = 0; i < tileMap.numPlayer; ++i) {
                final Char player = tileMap.aCharInMap.get(i);
                if (player != null && player.user != null && player.user.session != null) {
                    if (this.isTalent) {
                        Service.ServerMessage(player, String.format("Phe %s chiến thắng nhận được 3 điểm", this.white));
                        if (player.cName.equals(this.white)) {
                            player.pointTalent += 3;
                        }
                    } else {
                        if (player.cName.equals(this.white)) {
                            player.user.player.upCoin(coin, (byte)1);
                            if (player.ctaskId == 42 && player.ctaskIndex == 1) {
                                player.uptaskMaint();
                            }
                        }
                        Service.ServerMessage(player, String.format(Text.get(0, 302), this.white, coin));
                    }
                    Service.mapTime(player, (int)(this.timeLength - System.currentTimeMillis() / 1000L));
                }
            }
        }
        else if (type == 1) {
            for (short i = 0; i < tileMap.numPlayer; ++i) {
                final Char player = tileMap.aCharInMap.get(i);
                if (player != null && player.user != null && player.user.session != null) {
                    if (this.isTalent == true) {
                        Service.ServerMessage(player, String.format("Phe %s chiến thắng nhận được 3 điểm", this.black));
                        if (player.cName.equals(this.black)) {
                            player.pointTalent += 3;
                        }
                    } else {
                        if (player.cName.equals(this.black)) {
                            player.user.player.upCoin(coin, (byte) 1);
                            if (player.ctaskId == 42 && player.ctaskIndex == 1) {
                                player.uptaskMaint();
                            }
                        }
                        Service.ServerMessage(player, String.format(Text.get(0, 302), this.black, coin));
                    }
                    Service.mapTime(player, (int)(this.timeLength - System.currentTimeMillis() / 1000L));
                }
            }
        }
    }
    
    protected void CLOSE() {
        if (this.isFinght) {
            this.complete((byte)(-1));
        }
        for (byte i = 0; i < this.maps.length; ++i) {
            final Map map = this.maps[i];
            if (map != null) {
                for (byte j = 0; j < map.tileMaps.length; ++j) {
                    final TileMap tileMap = map.tileMaps[j];
                    if (tileMap != null) {
                        try {
                            tileMap.lock.lock("Dong loi dai");
                            try {
                                for (short k = (short)(tileMap.aCharInMap.size() - 1); k >= 0; --k) {
                                    final Char player = tileMap.aCharInMap.get(k);
                                    if (player != null && player.isHuman) {
                                        final Map ltd = MapServer.getMapServer(player.mapLTDId);
                                        if (ltd != null) {
                                            final TileMap tile = ltd.getSlotZone(player);
                                            if (tile == null) {
                                                GameCanvas.startOKDlg(player.user.session, Text.get(0, 9));
                                            }
                                            else {
                                                player.testDunPhe = -1;
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
                            }
                            finally {
                                tileMap.lock.unlock();
                            }
                        }
                        catch (InterruptedException e) {
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
    
    protected static void close() {
        synchronized (TestDun.LOCK) {
            for (int i = TestDun.arrTestDun.size() - 1; i >= 0; --i) {
                final TestDun test = TestDun.arrTestDun.get(i);
                TestDun.arrTestDun.remove(i);
                test.CLOSE();
            }
        }
    }

    protected void update() {
        Char char1 = Client.getPlayer(this.black);
        Char char2 = Client.getPlayer(this.white);
        if (char1 == null || char2 == null && !this.isFinght) {
            TestDunMessage("Trận đấu bị huỷ bỏ.");
            CLOSE();
        }
        if (char1 != null && char2 != null && !this.isFinght) {
            Char c = char1.findCharInMap(char2.charID);
            if (c == null) {
                TestDunMessage("Trận đấu bị huỷ bỏ.");
                this.CLOSE();
            }
        }
    }

    static {
        arrMapId = new short[] { 110, 111 };
        TestDun.maxCreate = 100;
        TestDun.setTimeChuanBi = 300;
        TestDun.setTimeChien = 600;
        TestDun.minCoin = 1000;
        LOCK = new Object();
        
        TestDun.arrTestDun = new ArrayList<>();
    }
}
