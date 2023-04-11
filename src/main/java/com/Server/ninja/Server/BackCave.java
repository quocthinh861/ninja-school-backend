 package com.Server.ninja.Server;

import com.Server.ninja.template.MapTemplate;

import java.util.ArrayList;

public class BackCave {
    private static final short[] arrMinLevel;
    private static final short[] arrMaxLevel;
    private static final short[] mapId35;
    private static final short[] mapId45;
    private static final short[] mapId55;
    private static final short[] mapId65;
    private static final short[] mapId75;
    private static final short[] mapId95;
    private static final byte[] cave_35_MapMaxLength;
    private static final byte[] cave_45_MapMaxLength;
    private static final byte[] cave_55_MapMaxLength;
    private static final byte[] cave_65_MapMaxLength;
    private static final byte[] cave_75_MapMaxLength;
    private static final byte[] cave_95_MapMaxLength;
    private static final byte[] maxPlayer35;
    private static final byte[] maxPlayer45;
    private static final byte[] maxPlayer55;
    private static final byte[] maxPlayer65;
    private static final byte[] maxPlayer75;
    private static final byte[] maxPlayer95;
    private static final int[] arrMaxCaveLevel;
    protected static int baseId;
    protected static final Object LOCK;
    protected static final ArrayList<BackCave> arrCave;
    protected int caveId;
    protected byte caveLevel;
    protected int timeLength;
    protected Map[] maps;
    protected byte[] cave_MapMaxLength;
    protected byte[] maxPlayer;
    protected final ArrayList<Char> ACharInCave;
    protected final Object LOCK_CAVE;
    protected boolean isFinish;
    protected int level;
    protected int point;

    public BackCave() {
        this.ACharInCave = new ArrayList<>();
        this.LOCK_CAVE = new Object();
        this.isFinish = false;
        this.level = 0;
        this.point = 0;
    }

    protected static void JoinCave(final Char _char, final byte cave_level, final short npcTemplateId) {
        if (_char.party != null && _char.partyCaveId == -1 && _char.charID != _char.party.charID) {
            Service.openUISay(_char, npcTemplateId, Text.get(0, 264));
        } else if (_char.party != null && _char.party.numPlayer > 1 && cave_level == 3) {
            Service.openUISay(_char, npcTemplateId, Text.get(0, 268));
        } else if (BackCave.arrMinLevel[cave_level] > _char.cLevel || BackCave.arrMaxLevel[cave_level] < _char.cLevel) {
            Service.openUISay(_char, npcTemplateId, Text.get(0, 265));
        } else {
            if (_char.party != null && _char.charID == _char.party.charID) {
                final Party party = _char.party;
                synchronized (party.LOCK) {
                    for (byte i = 0; i < party.numPlayer; ++i) {
                        final Char player = party.aChar.get(i);
                        if (player != null && (player.countPB <= 0 || BackCave.arrMinLevel[cave_level] > player.cLevel || BackCave.arrMaxLevel[cave_level] < player.cLevel)) {
                            Service.openUISay(_char, npcTemplateId, String.format(Text.get(0, 267), player.cName));
                            return;
                        }
                    }
                }
            }
            synchronized (BackCave.LOCK) {
                BackCave cave = getCave(_char.caveId);
                if (cave == null) {
                    _char.caveId = -1;
                }
                if (_char.countPB <= 0 && cave == null) {
                    Service.openUISay(_char, npcTemplateId, Text.get(0, 266));
                } else {
                    if (cave == null && _char.partyCaveId != -1 && cave_level != 3) {
                        cave = getCave(_char.partyCaveId);
                    }
                    if (cave == null) {
                        cave = BackCave(cave_level);
                        BackCave.arrCave.add(cave);
                    }
                    if (cave != null) {
                        if (_char.caveId == -1) {
                            _char.pointPB = 0;
                            --_char.countPB;
                            _char.timeFinishCave = 0;
                            _char.countPartyPB = 0;
                            _char.partyCaveId = cave.caveId;
                            if (_char.party != null && _char.party.charID == _char.charID) {
                                _char.party.TeamOPenCave(_char, cave.caveId);
                            }
                        }
                        _char.caveId = cave.caveId;
                        final Map map = cave.maps[0];
                        final TileMap tileMap = map.getSlotZone(_char);
                        if (tileMap != null) {
                            _char.tileMap.Exit(_char);
                            _char.cx = map.template.goX;
                            _char.cy = map.template.goY;
                            tileMap.Join(_char);
                        } else {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 24));
                        }
                    }
                }
            }
        }
    }

    private static BackCave BackCave(final byte cave_level) {
        final BackCave cave = new BackCave();
        cave.caveId = BackCave.baseId++;
        cave.caveLevel = cave_level;
        short[] arrMapId = null;
        switch (cave_level) {
            case 0: {
                arrMapId = BackCave.mapId35;
                cave.cave_MapMaxLength = BackCave.cave_35_MapMaxLength;
                cave.maxPlayer = BackCave.maxPlayer35;
                break;
            }
            case 1: {
                arrMapId = BackCave.mapId45;
                cave.cave_MapMaxLength = BackCave.cave_45_MapMaxLength;
                cave.maxPlayer = BackCave.maxPlayer45;
                break;
            }
            case 2: {
                arrMapId = BackCave.mapId55;
                cave.cave_MapMaxLength = BackCave.cave_55_MapMaxLength;
                cave.maxPlayer = BackCave.maxPlayer55;
                break;
            }
            case 3: {
                arrMapId = BackCave.mapId65;
                cave.cave_MapMaxLength = BackCave.cave_65_MapMaxLength;
                cave.maxPlayer = BackCave.maxPlayer65;
                break;
            }
            case 4: {
                arrMapId = BackCave.mapId75;
                cave.cave_MapMaxLength = BackCave.cave_75_MapMaxLength;
                cave.maxPlayer = BackCave.maxPlayer75;
                break;
            }
            case 5: {
                arrMapId = BackCave.mapId95;
                cave.cave_MapMaxLength = BackCave.cave_95_MapMaxLength;
                cave.maxPlayer = BackCave.maxPlayer95;
                break;
            }
        }
        if (arrMapId == null) {
            return null;
        }
        cave.maps = new Map[arrMapId.length];
        for (byte i = 0; i < arrMapId.length; ++i) {
            final MapTemplate template = GameScr.mapTemplates[arrMapId[i]];
            cave.maps[i] = new Map(template.mapID, (byte) 20, template.numZone);
            cave.maps[i].backCave = cave;
        }
        for (byte i = 0; i < cave.maps.length; ++i) {
            final Map map = cave.maps[i];
            if (map != null) {
                for (byte j = 0; j < map.tileMaps.length; ++j) {
                    final TileMap tileMap = map.tileMaps[j];
                    if (tileMap != null) {
                        tileMap.initWaypoint(2);
                    }
                }
            }
        }
        for (byte i = 0; i < cave.maps.length; ++i) {
            final Map map = cave.maps[i];
            if (map != null) {
                map.start();
            }
        }
        cave.timeLength = (int) (System.currentTimeMillis() / 1000L + 3600L);
        return cave;
    }

    protected static Map getMap(final BackCave cave, final short mapId) {
        for (byte i = 0; i < cave.maps.length; ++i) {
            if (cave.maps[i].template.mapID == mapId) {
                return cave.maps[i];
            }
        }
        return null;
    }

    protected static BackCave getCave(final int caveId) {
        for (int i = 0; i < BackCave.arrCave.size(); ++i) {
            final BackCave cave = BackCave.arrCave.get(i);
            if (cave != null && cave.caveId == caveId) {
                return cave;
            }
        }
        return null;
    }

    protected static int reWaed(final int point, final int timeFinish, final byte countParty) {
        int count = point / 10;
        count += countParty * 2;
        if (timeFinish > 0) {
            count += (3600 - timeFinish) / 120;
        }
        return count;
    }

    protected void addChar(final Char _char) {
        synchronized (this.ACharInCave) {
            this.ACharInCave.add(_char);
        }
    }

    protected void removeChar(final Char _char) {
        synchronized (this.ACharInCave) {
            for (byte i = 0; i < this.ACharInCave.size(); ++i) {
                final Char player = this.ACharInCave.get(i);
                if (player != null && player.charID == _char.charID) {
                    this.ACharInCave.remove(i);
                }
            }
        }
    }

    protected void upPointPB(final short point, final int charID) {
        for (byte i = 0; i < this.ACharInCave.size(); ++i) {
            final Char player = this.ACharInCave.get(i);
            if (player != null) {
                if (player.charID != charID) {
                    synchronized (player.LOCK) {
                        final Char char1 = player;
                        char1.pointPB += point;
                        Service.pointPB(player, player.pointPB);
                    }
                } else {
                    final Char char2 = player;
                    char2.pointPB += point;
                    Service.pointPB(player, player.pointPB);
                }
            }
        }
    }

    protected void nexPoint(final TileMap tileMap, final Mob mob, final Char _char) {
        synchronized (this.ACharInCave) {
            ++this.point;
            final int level_old = this.level;
            switch (this.caveLevel) {
                case 0 -> {
                    if (mob.levelBoss == 2) {
                        this.upPointPB((short) 10, _char.charID);
                    } else {
                        this.upPointPB((short) 1, _char.charID);
                    }   if (this.point >= tileMap.aMob.size()) {
                        ++this.level;
                        this.point = 0;
                    }
                }
                case 1 -> {
                    if (mob.levelBoss == 2) {
                        this.upPointPB((short) 40, _char.charID);
                    } else {
                        this.upPointPB((short) 1, _char.charID);
                    }   if (this.point >= tileMap.aMob.size()) {
                        ++this.level;
                        this.point = 0;
                    }
                }
                case 2 -> {
                    if (mob.levelBoss == 2) {
                        this.upPointPB((short) 50, _char.charID);
                    } else {
                        this.upPointPB((short) 1, _char.charID);
                    }   if (this.point >= tileMap.aMob.size()) {
                        ++this.level;
                        this.point = 0;
                    }
                }
                case 3 -> {
                    this.upPointPB((short) 1, _char.charID);
                    if (this.point >= tileMap.aMob.size()) {
                        ++this.level;
                        this.point = 0;
                        if (this.level > 2) {
                            for (short k = 0; k < tileMap.aMob.size(); ++k) {
                                final Mob mob2 = tileMap.aMob.get(k);
                                if (mob2 != null) {
                                    if (mob2.mobId == 82 || mob2.mobId == 85) {
                                        mob2.levelBoss = 1;
                                    }
                                    int hp = Mob.arrMobTemplate[mob2.templateId].hp * (10 * (this.level - 2) + 100) / 100;
                                    if (mob2.levelBoss == 1) {
                                        hp *= 10;
                                    }
                                    if (mob2.levelBoss == 2) {
                                        hp *= 100;
                                    }
                                    if (mob2.levelBoss == 3) {
                                        hp *= 200;
                                    }
                                    final int dameSys = mob2.level * (mob2.level / (3 + mob2.level / 20)) * (10 * (this.level - 2) + 100) / 100;
                                    Mob.LiveMob(mob2, (byte) Util.nextInt(1, 3), (byte) mob2.levelBoss, hp, dameSys);
                                }
                            }
                        }
                    }
                }
                case 4 -> {
                    mob.removeWhenDie = true;
                    if (mob.isBoss) {
                        this.upPointPB((short) 10, _char.charID);
                    } else {
                        this.upPointPB((short) 1, _char.charID);
                    }   if (this.point == tileMap.aMob.size() - 2) {
                        for (short k = 0; k < tileMap.aMob.size(); ++k) {
                            final Mob mob2 = tileMap.aMob.get(k);
                            if (mob2 != null && mob2.isBoss) {
                                mob2.RefreshTimeDie = 3000;
                                mob2.startDie();
                                mob2.removeWhenDie = false;
                            }
                        }
                    }   if (this.point >= tileMap.aMob.size()) {
                        ++this.level;
                        this.point = 0;
                    }
                }
                case 5 -> {
                    this.upPointPB((short) 1, _char.charID);
                    if (mob.isBoss && ((mob.templateId == 199 && this.level == 0) || (mob.templateId == 200 && this.level == 1) || (mob.templateId == 198 && this.level == 2))) {
                        ++this.level;
                        mob.removeWhenDie = true;
                        mob.RefreshTimeDie = -1;
                        short points = 0;
                        for (short i = 0; i < tileMap.aMob.size(); ++i) {
                            final Mob mob3 = tileMap.aMob.get(i);
                            if (mob3 != null && mob3.mobId != mob.mobId) {
                                synchronized (mob3.LOCK) {
                                    mob3.removeWhenDie = true;
                                    mob3.RefreshTimeDie = -1;
                                    if (mob3.status != 0 || !mob3.injureThenDie) {
                                        mob3.startDie();
                                        ++points;
                                        try {
                                            for (short j = 0; j < tileMap.aCharInMap.size(); ++j) {
                                                if (tileMap.aCharInMap.get(j).user != null && tileMap.aCharInMap.get(j).user.session != null) {
                                                    Service.MobDie(tileMap.aCharInMap.get(j), mob3, mob3.hp, false, null);
                                                }
                                            }
                                        } catch (Exception ex) {
                                        }
                                    }
                                }
                            }
                        }
                        this.upPointPB(points, _char.charID);
                    }
                }
                default -> {
                }
            }
            if (this.level == BackCave.arrMaxCaveLevel[this.caveLevel]) {
                if (!this.isFinish) {
                    synchronized (this.LOCK_CAVE) {
                        final int length = this.timeLength;
                        if (this.caveLevel != 3) {
                            this.timeLength = (int) (System.currentTimeMillis() / 1000L + 60L);
                        }
                        for (byte l = 0; l < this.ACharInCave.size(); ++l) {
                            final Char player = this.ACharInCave.get(l);
                            if (player != null) {
                                player.caveId = -1;
                                player.countPartyPB = -1;
                                player.countPartyPB = (byte) this.ACharInCave.size();
                                player.timeFinishCave = (int) (System.currentTimeMillis() / 1000L) - (length - 3600);
                                Char.upPointClan(player, 10);
                                Service.ServerMessage(player, Text.get(0, 269));
                                if (player.ctaskId == 39 && player.ctaskIndex == 2) {
                                    player.uptaskMaint();
                                }
                                Service.mapTime(player, (int) (this.timeLength - System.currentTimeMillis() / 1000L));
                            }
                        }
                    }
                }
                this.isFinish = true;
            } else if (level_old < this.level && this.cave_MapMaxLength.length > this.level && this.maps.length >= this.cave_MapMaxLength[this.level] && this.cave_MapMaxLength[this.level] > this.cave_MapMaxLength[level_old]) {
                for (byte m = this.cave_MapMaxLength[level_old]; m < this.cave_MapMaxLength[this.level]; ++m) {
                    for (byte i2 = 0; i2 < this.ACharInCave.size(); ++i2) {
                        final Char player2 = this.ACharInCave.get(i2);
                        if (player2 != null) {
                            Service.ServerMessage(player2, String.format(Text.get(0, 273), this.maps[m].template.mapName));
                        }
                    }
                }
            }
        }
    }

    protected void CLOSE() {
        final Char[] arrChar = new Char[this.ACharInCave.size()];
        synchronized (this.ACharInCave) {
            for (byte i = 0; i < arrChar.length; ++i) {
                arrChar[i] = this.ACharInCave.get(i);
            }
        }
        for (byte j = 0; j < arrChar.length; ++j) {
            final Char player = arrChar[j];
            if (player != null) {
                TileMap.getMapLTD(player);
            }
        }
        for (byte j = 0; j < this.maps.length; ++j) {
            final Map map2 = this.maps[j];
            if (map2 != null) {
                map2.close();
            }
        }
    }

    protected static void close() {
        synchronized (BackCave.LOCK) {
            for (int i = BackCave.arrCave.size() - 1; i >= 0; --i) {
                final BackCave cave = BackCave.arrCave.get(i);
                BackCave.arrCave.remove(i);
                cave.CLOSE();
            }
        }
    }

    protected static void ItemUse(final Char _char, final Item item) {
        Item it = null;
        if (Util.nextInt(100) < 45) {
            if (item.template.id == 272) {
                it = new Item(null, (short) Util.nextInt(2, 3), 1, -1, true, (byte) 0, 0);
            } else {
                it = new Item(null, (short) Util.nextInt(3, 4), 1, -1, true, (byte) 0, 0);
            }
        } else if (Util.nextInt(100) < 5 && item.template.id == 647) {
            it = new Item(null, (short) Util.nextInt(5, 7), 1, -1, true, (byte) 0, 0);
        } else if (Util.nextInt(100) < 1) {
            it = new Item(null, (short) 242, 1, -1, false, (byte) 0, 0);
        } else if (Util.nextInt(100) < 1) {
            it = new Item(null, (short) 269, 1, -1, false, (byte) 0, 0);
        } else if (item.template.id == 647 && Util.nextInt(100) < 1) {
            it = new Item(null, (short) 436, 1, -1, false, (byte) 0, 0);
        } else if (item.template.id == 647 && Util.nextInt(100) < 1) {
            it = new Item(null, (short) 491, 1, -1, false, (byte) 0, 0);
        } else if ((item.template.id == 282 || item.template.id == 647) && Util.nextInt(1000) < 5) {
            it = new Item(null, (short) 283, 1, -1, false, (byte) 0, 0);
        } else if (item.template.id == 647 && Util.nextInt(1000) < 5) {
            it = new Item(null, (short) Util.nextInt(539, 540), 1, -1, false, (byte) 0, 0);
        } else if (item.template.id == 647 && Util.nextInt(1000) < 5) {
            it = ItemServer.getItemStore((short) Util.nextInt(618, 631), (byte) Util.nextInt(1, 3), (byte) 1);
        } else if (item.template.id == 647 && Util.nextInt(1000) < 5) {
            it = ItemServer.getItemStore((short) Util.nextInt(632, 637), (byte) Util.nextInt(1, 3), (byte) 1);
        } else {
            switch (item.template.id) {
                case 272: {
                    _char.user.player.upCoinLock(Util.nextInt(100, 500), (byte) 2);
                    break;
                }
                case 282: {
                    _char.user.player.upCoinLock(Util.nextInt(500, 100), (byte) 2);
                    break;
                }
                case 647: {
                    _char.user.player.upCoinLock(Util.nextInt(1000, 3000), (byte) 2);
                    break;
                }
            }
        }
        if (it != null) {
            if (it.template.isUpToUp) {
                _char.user.player.ItemBagAddQuantity(it);
            } else {
                _char.user.player.ItemBagAdd(it);
            }
            if (it.template.id == 269 || it.template.id == 283) {
                Client.alertServer(String.format(Text.get(0, 271), _char.cName, it.template.name));
            }
        }
    }

    static {
        arrMinLevel = new short[]{30, 40, 50, 60, 70, 90};
        arrMaxLevel = new short[]{39, 49, 59, 69, 130, 130};
        mapId35 = new short[]{91, 92, 93};
        mapId45 = new short[]{94, 95, 96, 97};
        mapId55 = new short[]{105, 106, 107, 108, 109};
        mapId65 = new short[]{114, 115, 116};
        mapId75 = new short[]{125, 126, 127, 128};
        mapId95 = new short[]{157, 158, 159};
        cave_35_MapMaxLength = new byte[]{1, 2, 3};
        cave_45_MapMaxLength = new byte[]{1, 2, 3, 4};
        cave_55_MapMaxLength = new byte[]{1, 4, 4, 4, 5};
        cave_65_MapMaxLength = new byte[]{1, 2, 3};
        cave_75_MapMaxLength = new byte[]{1, 2, 3, 4};
        cave_95_MapMaxLength = new byte[]{3, 3, 3};
        maxPlayer35 = new byte[]{6, 6, 6};
        maxPlayer45 = new byte[]{6, 6, 6, 6};
        maxPlayer55 = new byte[]{6, 2, 2, 2, 6};
        maxPlayer65 = new byte[]{1, 1, 1};
        maxPlayer75 = new byte[]{6, 6, 6, 6};
        maxPlayer95 = new byte[]{6, 6, 6};
        arrMaxCaveLevel = new int[]{3, 4, 5, 3, 4, 3};
        BackCave.baseId = 0;
        LOCK = new Object();
        arrCave = new ArrayList<>();
    }
}
