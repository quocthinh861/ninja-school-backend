package com.Server.ninja.Server;

import com.Server.ninja.template.MapTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class ChienTruong {
    private static final short[] arrMapId;
    protected static final Object LOCK;
    protected static byte baseId;
    protected static final String[] TITLE_CT;
    protected static final String[] BATDAULUC_CT;
    protected int chientruongId;
    protected byte type;
    protected final ArrayList<Integer> aCharBlack;
    protected final ArrayList<Integer> aCharWhite;
    protected final ArrayList<Char> AChar;
    protected int totalBlack;
    protected int totalWhite;
    protected boolean isBaoDanh;
    protected int timeLength;
    protected long timeTru;
    protected boolean isBaoTru;
    protected Map[] maps;
    protected byte chientruong_type;
    protected byte typeWin;
    protected byte level;
    protected final Object CHIENTRUONG_LOCK;
    protected static ChienTruong chien_truong;

    public ChienTruong() {
        this.aCharBlack = new ArrayList<>();
        this.aCharWhite = new ArrayList<>();
        this.AChar = new ArrayList<>();
        this.totalBlack = 0;
        this.totalWhite = 0;
        this.isBaoDanh = true;
        this.timeLength = 1800;
        this.timeTru = -1L;
        this.isBaoTru = false;
        this.typeWin = -1;
        this.level = 0;
        this.CHIENTRUONG_LOCK = new Object();
    }

    protected static void setChienTruong(final byte type) {
        synchronized (ChienTruong.LOCK) {
            Calendar rightnow = Calendar.getInstance();
            final int min = rightnow.get(Calendar.MINUTE);
            final int sec = rightnow.get(Calendar.SECOND);
            Top.tops[5].clear();
            final ChienTruong chienTruong;
            final ChienTruong ct = chienTruong = new ChienTruong();
            final byte baseId = ChienTruong.baseId;
            ChienTruong.baseId = (byte) (baseId + 1);
            chienTruong.chientruongId = baseId;
            ct.type = type;
            ct.maps = new Map[ChienTruong.arrMapId.length];
            for (byte i = 0; i < ChienTruong.arrMapId.length; ++i) {
                final MapTemplate template = GameScr.mapTemplates[ChienTruong.arrMapId[i]];
                ct.maps[i] = new Map(template.mapID, (byte) 20, template.numZone);
                ct.maps[i].chientruong = ct;
            }
            for (byte i = 0; i < ct.maps.length; ++i) {
                final Map map = ct.maps[i];
                if (map != null) {
                    for (byte j = 0; j < map.tileMaps.length; ++j) {
                        final TileMap tileMap = map.tileMaps[j];
                        if (tileMap != null) {
                            tileMap.initWaypoint(4);
                        }
                    }
                }
            }
            for (byte i = 0; i < ct.maps.length; ++i) {
                final Map map = ct.maps[i];
                if (map != null) {
                    map.start();
                }
            }
            ct.timeLength = (int) (System.currentTimeMillis() / 1000L + ((29 - min) * 60 + (60 - sec)));
            ChienTruong.chien_truong = ct;
        }
    }

    protected static Map getMap(final ChienTruong ct, final short mapId) {
        for (byte i = 0; i < ct.maps.length; ++i) {
            if (ct.maps[i].template.mapID == mapId) {
                return ct.maps[i];
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

    protected static void liveTru(final Map map, final byte zone) {
        try {
            final TileMap tileMap = map.tileMaps[zone];
            if (tileMap != null) {
                try {
                    for (short j = 0; j < tileMap.aMob.size(); ++j) {
                        final Mob mob = tileMap.aMob.get(j);
                        if (mob != null && mob.status == 0 && (mob.templateId == 98 || mob.templateId == 99)) {
                            Mob.LiveMob(mob, (byte) Util.nextInt(1, 3), (byte) mob.levelBoss, -1, -1);
                        }
                    }
                } finally {
                    tileMap.lock.unlock();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected static void reward(final Char _char, final ChienTruong ct) {
        if (ct != null && _char.pointChienTruong != 0 && ct.level == 2 && _char.getCT() > 0) {
            if (ct.aCharWhite.contains(_char.user.player.playerId) || ct.aCharBlack.contains(_char.user.player.playerId)) {
                switch (_char.getCT()) {
                    case 1 -> {
                        if (_char.user.player.ItemBagSlotNull() < 1) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                            break;
                        }
                        _char.updateExp(5000000L);
                        _char.user.player.ItemBagAdd(new Item(null, (short) 3, 1, -1, false, (byte) 0, 0));
                    }
                    case 2 -> {
                        if (_char.user.player.ItemBagSlotNull() < 1) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                            break;
                        }
                        _char.updateExp(10000000L);
                        _char.user.player.ItemBagAdd(new Item(null, (short) 4, 1, -1, false, (byte) 0, 0));
                    }
                    case 3 -> {
                        if (_char.user.player.ItemBagSlotNull() < 2) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                            break;
                        }
                        _char.updateExp(15000000L);
                        _char.user.player.ItemBagAdd(new Item(null, (short) 5, 1, -1, false, (byte) 0, 0));
                        _char.user.player.ItemBagAdd(new Item(null, (short) 5, 1, -1, false, (byte) 0, 0));
                    }
                    case 4 -> {
                        if (_char.user.player.ItemBagSlotNull() < 3) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                            break;
                        }
                        _char.updateExp(30000000L);
                        _char.user.player.ItemBagAdd(new Item(null, (short) 6, 1, -1, false, (byte) 0, 0));
                        _char.user.player.ItemBagAdd(new Item(null, (short) Util.nextInt(275, 278), 1, -1, false, (byte) 0, 0));
                        if ((ct.typeWin == 0 && ct.aCharWhite.contains(_char.user.player.playerId)) || (ct.typeWin == 1 && ct.aCharBlack.contains(_char.user.player.playerId))) {
                            _char.user.player.ItemBagAdd(new Item(null, (short) 4, 1, -1, false, (byte) 0, 0));
                        }
                    }
                }
            }
            _char.pointChienTruong = 0;
        }
    }

    protected void update() {
        if (this.level == 1) {
            if (this.timeTru != -1L && this.timeTru - System.currentTimeMillis() <= 60000L && !this.isBaoTru) {
                this.isBaoTru = true;
                Client.alertServer(Text.get(0, 347));
            }
            if (this.timeTru < System.currentTimeMillis()) {
                if (this.timeTru != -1L) {
                    final Map white = getMap(this, (short) 99);
                    if (white != null) {
                        liveTru(white, (byte) Util.nextInt(1, 10));
                    }
                    final Map black = getMap(this, (short) 103);
                    if (black != null) {
                        liveTru(black, (byte) Util.nextInt(1, 10));
                    }
                }
                this.isBaoTru = false;
                this.timeTru = System.currentTimeMillis() + 600000L;
            }
        }
    }

    protected static void close() {
        synchronized (ChienTruong.LOCK) {
            if (ChienTruong.chien_truong != null) {
                ChienTruong.chien_truong.CLOSE();
                ChienTruong.chien_truong = null;
            }
        }
    }

    protected void CLOSE() {
        final Char[] arrChar = new Char[this.AChar.size()];
        synchronized (this.AChar) {
            for (byte i = 0; i < arrChar.length; ++i) {
                arrChar[i] = this.AChar.get(i);
            }
        }
        for (final Char player : arrChar) {
            if (player != null) {
                TileMap.getMapLTD(player);
            }
        }
        for (final Map map2 : this.maps) {
            if (map2 != null) {
                map2.close();
            }
        }
    }

    static {
        Calendar rightnow = Calendar.getInstance();
        final int min = rightnow.get(Calendar.MINUTE);
        arrMapId = new short[]{98, 99, 100, 101, 102, 103, 104};
        LOCK = new Object();
        ChienTruong.baseId = 0;
        TITLE_CT = new String[]{"Học Giả", "Hạ Nhẫn", "Trung Nhẫn", "Thượng nhẫn", "Nhẫn Giả"};
        BATDAULUC_CT = new String[]{"Chiến trường bắt đầu sau " + (29 - min) + " phút nữa.", ""};
        ChienTruong.chien_truong = null;
    }
}
