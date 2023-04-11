package com.Server.ninja.Server;

import java.util.ArrayList;

import com.Server.ninja.template.MapTemplate;

/**
 * @author KHONG MINH TIEN
 */
public class NJTalent {
    protected Map[] maps;
    protected static ArrayList<Char> memjoin;
    protected static ArrayList<Char> group5065;
    protected static ArrayList<Char> group6680;
    protected static ArrayList<Char> group8199;
    protected static ArrayList<Char> group100115;
    protected static ArrayList<Char> group116130;
    protected static boolean isRun;
    protected boolean isPk;
    private static final short[] arrMapId;
    protected static final short mapChuanBi = 129;
    protected static final Object LOCK;
    protected final Object NJ_LOCK;
    protected static NJTalent ninja_talent;

    // khởi tạo
    public NJTalent() {
        isRun = true;
        memjoin = new ArrayList<>();
        group5065 = new ArrayList<>();
        group6680 = new ArrayList<>();
        group8199 = new ArrayList<>();
        group100115 = new ArrayList<>();
        group116130 = new ArrayList<>();
        this.NJ_LOCK = new Object();
    }

    protected static void startRun() {
        synchronized (NJTalent.LOCK) {
            final NJTalent nJTalent;
            final NJTalent njtalent = nJTalent = new NJTalent();
            njtalent.maps = new Map[NJTalent.arrMapId.length];
            for (byte i = 0; i < NJTalent.arrMapId.length; ++i) {
                final MapTemplate template = GameScr.mapTemplates[NJTalent.arrMapId[i]];
                njtalent.maps[i] = new Map(template.mapID, (byte) 30, template.numZone);
                njtalent.maps[i].njTalent = njtalent;
            }
            for (byte i = 0; i < njtalent.maps.length; ++i) {
                final Map map = njtalent.maps[i];
                if (map != null) {
                    for (byte j = 0; j < map.tileMaps.length; ++j) {
                        final TileMap tileMap = map.tileMaps[j];
                        if (tileMap != null) {
                            tileMap.initWaypoint(5);
                        }
                    }
                }
            }
            for (byte i = 0; i < njtalent.maps.length; ++i) {
                final Map map = njtalent.maps[i];
                if (map != null) {
                    map.start();
                }
            }
            NJTalent.ninja_talent = njtalent;
        }
    }

    protected static Map getMap(final NJTalent njtl, final short mapId) {
        for (byte i = 0; i < njtl.maps.length; ++i) {
            if (njtl.maps[i].template.mapID == mapId) {
                return njtl.maps[i];
            }
        }
        return null;
    }

    protected void addChar(final Char _char) {
        synchronized (memjoin) {
            memjoin.add(_char);
            int level = _char.cLevel;
            if (50 <= level && level <= 65) {
                group5065.add(_char);
            } else if (66 <= level && level <= 80) {
                group6680.add(_char);
            } else if (81 <= level && level <= 99) {
                group8199.add(_char);
            } else if (100 <= level && level <= 115) {
                group100115.add(_char);
            } else if (116 <= level && level <= 130) {
                group116130.add(_char);
            }
        }
    }

    protected void removeChar(final Char _char) {
        synchronized (memjoin) {
            for (byte i = 0; i < memjoin.size(); ++i) {
                final Char player = memjoin.get(i);
                if (player != null && player.charID == _char.charID) {
                    memjoin.remove(i);
                    int level = _char.cLevel;
                    if (50 <= level && level <= 65) {
                        group5065.remove(_char);
                    } else if (66 <= level && level <= 80) {
                        group6680.remove(_char);
                    } else if (81 <= level && level <= 99) {
                        group8199.remove(_char);
                    } else if (100 <= level && level <= 115) {
                        group100115.remove(_char);
                    } else if (116 <= level && level <= 130) {
                        group116130.remove(_char);
                    }
                }
            }
        }
    }

    protected static void close() {
        synchronized (LOCK) {
            if (ninja_talent != null) {
                ninja_talent.CLOSE();
                ninja_talent = null;
                isRun = false;
            }
        }
    }

    protected void CLOSE() {
        final Char[] arrChar = new Char[memjoin.size()];
        synchronized (memjoin) {
            for (byte i = 0; i < arrChar.length; ++i) {
                arrChar[i] = memjoin.get(i);
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

    public static void Group(ArrayList<Char> group) {
        if (group != null) {
            while (group.size() > 1) {
                for (int i = 0; i < group.size(); i++) {
                    int c1 = Util.nextInt(group.size());
                    Char char1 = group.get(c1);
                    group.remove(c1);
                    int c2 = Util.nextInt(group.size());
                    Char char2 = group.get(c2);
                    group.remove(c2);
                    setPk(char1, char2);
                }
            }
            if (group.size() == 1) {
                Char _char = group.get(0);
                Service.ServerMessage(_char, "Không tìm thấy đối thủ bạn nhận được 3 điểm");
                _char.pointTalent += 3;
            }
        }
    }

    public static void setPk(Char char1, Char char2) {
        final TestDun test = TestDun.OpenTestDun();
        test.isTalent = true;
        test.coinTotal = 0;
        test.TestDunMessage(Text.get(0, 299));
        test.timeLength = (int) (System.currentTimeMillis() / 1000L + 300);
        final TileMap tileMap = test.maps[1].getSlotZone(char1);
        final Map map = test.maps[0];
        if (map != null) {
            for (byte j = 0; j < map.tileMaps.length; ++j) {
                final TileMap tile = map.tileMaps[j];
                if (tile != null) {
                    //tile.lock.lock("Bat dau loi dai");
                    try {
                        if (char1 != null && !char1.isNhanban) {
                            char1.tileMap.EXIT(char1);
                            char1.testDunPhe = 1;
                            Char.setEffect(char1, (byte) 14, -1, 10000, (short) 0, null, (byte) 0);
                            char1.cx = tileMap.map.template.goX;
                            char1.cx += 400;
                            char1.cy = tileMap.map.template.goY;
                            test.black = char1.cName;
                            tileMap.addChar(char1);
                        }
                        if (char2 != null && !char2.isNhanban) {
                            char1.tileMap.EXIT(char2);
                            char2.testDunPhe = 0;
                            Char.setEffect(char2, (byte) 14, -1, 10000, (short) 0, null, (byte) 0);
                            char2.cx = tileMap.map.template.goX;
                            char2.cy = tileMap.map.template.goY;
                            test.white = char2.cName;
                            tileMap.addChar(char2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        test.isFinght = true;
    }

    public static void startPk() {
        synchronized (memjoin) {
            if (isRun) {
                Group(group5065);
                Group(group6680);
                Group(group8199);
                Group(group100115);
                Group(group116130);
            }
        }
    }

    static {
        arrMapId = new short[]{111, 129};
        LOCK = new Object();
        NJTalent.ninja_talent = null;
    }
}
