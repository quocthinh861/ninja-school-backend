package com.Server.ninja.Server;

import com.Server.ninja.template.MapTemplate;

import java.util.ArrayList;
import java.util.HashMap;

public class SevenBeasts {
    protected static boolean isBaoDanh;
    protected static boolean isStart;
    protected static boolean isRun;
    protected static ArrayList<Char> arrChar = new ArrayList<>();
    protected final ArrayList<Char> arrCharInMap;
    protected ArrayList<Char> arrCharParty;
    protected static byte baseId = 0;
    protected int partyId;
    protected int timeLength;
    protected static long time;
    protected int countMocNhan;
    protected int countRefresh;
    protected int countMob;
    protected boolean isHaveMocNhan;
    protected Map[] maps;
    protected static final short[] arrMapId;
    protected static final short[] mobId;
    protected static final short[] mobX;
    protected static final short[] mobY;
    protected static final Object LOCK;
    protected final Object SevenBeasts_LOCK;
    protected static HashMap<Integer, SevenBeasts> sevenBeasts = new HashMap<>();

    public SevenBeasts() {
        this.SevenBeasts_LOCK = new Object();
        this.partyId = baseId;
        this.arrCharParty = new ArrayList<>();
        this.arrCharInMap = new ArrayList<>();
        this.countMocNhan = 0;
        this.countRefresh = 0;
    }

    protected void setSevenBeasts(final Char _char) {
        synchronized (SevenBeasts.LOCK) {
            final SevenBeasts sb = new SevenBeasts();
            final byte baseId = SevenBeasts.baseId;
            SevenBeasts.baseId = (byte) (baseId + 1);
            sb.maps = new Map[SevenBeasts.arrMapId.length];
            for (byte i = 0; i < SevenBeasts.arrMapId.length; ++i) {
                final MapTemplate template = GameScr.mapTemplates[SevenBeasts.arrMapId[i]];
                sb.maps[i] = new Map(template.mapID, (byte) 6, template.numZone);
                sb.maps[i].sevenBeasts = sb;
            }
            for (byte i = 0; i < sb.maps.length; ++i) {
                final Map map = sb.maps[i];
                if (map != null) {
                    for (byte j = 0; j < map.tileMaps.length; ++j) {
                        final TileMap tileMap = map.tileMaps[j];
                        if (tileMap != null) {
                            tileMap.initWaypoint(8);
                        }
                    }
                }
            }
            for (byte i = 0; i < sb.maps.length; ++i) {
                final Map map = sb.maps[i];
                if (map != null) {
                    map.start();
                }
            }
            sb.timeLength = (int) ((SevenBeasts.time + 150000L - System.currentTimeMillis()) / 1000L);
            sb.arrCharParty.addAll(_char.party.aChar);
            SevenBeasts.refreshMob(sb);
            SevenBeasts.sevenBeasts.put(sb.partyId, sb);
        }
    }

    protected static Map getMap(final SevenBeasts sb, final short mapId) {
        for (byte i = 0; i < sb.maps.length; ++i) {
            if (sb.maps[i].template.mapID == mapId) {
                return sb.maps[i];
            }
        }
        return null;
    }

    protected void addChar(final Char _char) {
        synchronized (this.arrCharInMap) {
            arrCharInMap.add(_char);
        }
    }

    protected void removeChar(final Char _char) {
        synchronized (this.arrCharInMap) {
            for (byte i = 0; i < this.arrCharInMap.size(); ++i) {
                final Char player = this.arrCharInMap.get(i);
                if (player != null && player.charID == _char.charID) {
                    this.arrCharInMap.remove(i);
                }
            }
        }
    }

    protected void sendTB(final Char c, String mobName, final Item item, int type) {
        synchronized (this.arrCharInMap) {
            for (byte i = 0; i < this.arrCharInMap.size(); ++i) {
                final Char player = this.arrCharInMap.get(i);
                switch (type) {
                    case 0:
                        Service.ServerMessage(player, String.format(Text.get(0, 406), c.cName, item.template.name, mobName));
                        break;
                    case 1:
                        Service.ServerMessage(player, String.format(Text.get(0, 405), c.cName));
                        break;
                    case 2:
                        Service.ServerMessage(player, "Đã hoàn thành thất thú ải.");
                        break;
                    case 3:
                        Service.ServerMessage(player, "Trưởng nhóm đã báo danh Thất thú ải");
                        break;
                }
            }
        }
    }

    protected static boolean checkBaoDanh(final Party party) {
        for (Char aChar : arrChar) {
            for (int j = 0; j < party.aChar.size(); j++) {
                if (aChar.cName.equals(party.aChar.get(j).cName)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected static boolean checkBaoDanh(final Char _char) {
        for (Char aChar : arrChar) {
            if (aChar.cName.equals(_char.cName)) {
                return true;
            }
        }
        return false;
    }

    protected static int getKey(final Char _char) {
        for (int i = 0; i < sevenBeasts.size(); i++) {
            SevenBeasts sb = sevenBeasts.get(i);
            if (sb != null) {
                for (int j = 0; j < sb.arrCharParty.size(); j++) {
                    if (_char.cName.equals(sb.arrCharParty.get(j).cName)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    protected void close() {
        synchronized (arrCharInMap) {
            for (byte j = 0; j < arrCharInMap.size(); ++j) {
                final Char player = arrCharInMap.get(j);
                if (player != null) {
                    TileMap.getMapLTD(player);
                }
                arrCharInMap.remove(j);
            }
        }
        for (byte j = 0; j < this.maps.length; ++j) {
            final Map map2 = this.maps[j];
            if (map2 != null) {
                map2.close();
            }
        }
        SevenBeasts.sevenBeasts.remove(this.partyId);
    }
    protected static void clear() {
        synchronized (SevenBeasts.LOCK) {
            for (int i = SevenBeasts.sevenBeasts.size() - 1; i >= 0; --i) {
                final SevenBeasts sb = SevenBeasts.sevenBeasts.get(i);
                if (sb != null) {
                    sb.close();
                }
                SevenBeasts.sevenBeasts.remove(i);
            }
        }
    }
    public static SevenBeasts getSevenBeasts(final Char _char) {
        int key = SevenBeasts.getKey(_char);
        SevenBeasts sb = SevenBeasts.sevenBeasts.get(key);
        return sb;
    }
    protected static void refreshMob(final SevenBeasts sb) {
        TileMap tileMap = sb.maps[0].tileMaps[0];
        tileMap.aMob.clear();
        if (sb.countRefresh < 5) {
            for (byte i = 0; i < SevenBeasts.mobX.length; ++i) {
                final int iD = mobId[sb.countRefresh];
                final byte sys = (byte) Util.nextInt(1, 3);
                final int hp = Mob.arrMobTemplate[iD].hp;
                final int level = 68;
                final short x = mobX[i];
                final short y = mobY[i];
                final byte status = 5;
                final byte levelBoss = 0;
                final boolean isBoss = Mob.arrMobTemplate[iD].isBoss;
                final Mob mob = new Mob(tileMap, i, iD, sys, hp, level, x, y, status, levelBoss, isBoss, -1);
                tileMap.aMob.add(mob);
                Mob.LiveMob(mob, (byte) 1, (byte) 0, -1, -1);
            }
        } else {
            final int iD = mobId[sb.countRefresh];
            final byte sys = (byte) Util.nextInt(1, 3);
            final int hp = Mob.arrMobTemplate[iD].hp;
            final int level = 68;
            final short x = 571;
            final short y = 384;
            final byte status = 5;
            final byte levelBoss = 0;
            final boolean isBoss = Mob.arrMobTemplate[iD].isBoss;
            final Mob mob = new Mob(tileMap, (short) 0, iD, sys, hp, level, x, y, status, levelBoss, isBoss, -1);
            tileMap.aMob.add(mob);
            Mob.LiveMob(mob, (byte) 1, (byte) 0, -1, -1);
        }
    }

    protected static void joinMap(SevenBeasts sb, Char _char) {
        final Map map3 = SevenBeasts.getMap(sb, sb.maps[1].template.mapID);
        if (map3 != null) {
            final TileMap tileMap2 = map3.getSlotZone(_char);
            if (tileMap2 != null) {
                _char.tileMap.Exit(_char);
                _char.cx = map3.template.goX;
                _char.cy = map3.template.goY;
                tileMap2.Join(_char);
            } else {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
            }
        }
    }

    static {
        mobId = new short[]{106, 107, 108, 109, 110, 111, 112};
        mobX = new short[]{145, 120, 195, 211, 234, 333, 363, 387, 423, 426, 446, 561, 585, 627, 657, 682, 710, 768, 798, 828, 945, 1044, 945, 882, 728, 758, 778, 808, 828, 848, 137, 202, 247, 127, 217, 134, 228, 311, 335, 350, 1092, 1128, 950, 848, 1043, 944, 1046, 1142, 947};
        mobY = new short[]{144, 144, 126, 216, 216, 168, 120, 120, 120, 216, 216, 96, 96, 96, 216, 216, 216, 144, 144, 144, 96, 168, 240, 288, 576, 576, 576, 576, 576, 576, 288, 336, 336, 408, 480, 528, 576, 576, 576, 576, 552, 432, 456, 408, 408, 360, 336, 288, 240};
        arrMapId = new short[]{112, 113};
        LOCK = new Object();
    }
}
