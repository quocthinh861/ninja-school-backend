package com.Server.ninja.Server;

import com.Server.ninja.template.MapTemplate;

import java.util.ArrayList;

public class ClanManor {
    protected boolean openTru80;
    protected boolean openTru81;
    protected boolean openTru82;
    protected boolean openTru83;
    protected boolean openTru84;
    protected boolean openTru85;
    protected boolean openTru86;
    protected boolean openTru87;
    protected boolean openTru88;
    protected boolean openTru89;
    protected boolean isBaoDanh;
    protected byte[] refesh = new byte[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2};

    protected static byte baseId;
    protected int clanManorId;
    protected int timeLength;
    protected final ArrayList<Char> memberInManor;
    protected final ArrayList<String> memberAcceptManor;
    protected Map[] maps;
    protected static final short[] arrMapId;
    protected static final Object LOCK;
    protected final Object ClanManor_LOCK;
    protected static ArrayList<ClanManor> arrClanManor;

    public ClanManor() {
        this.memberInManor = new ArrayList<>();
        this.memberAcceptManor = new ArrayList<>();
        this.ClanManor_LOCK = new Object();
    }

    protected static void setClanManor(Clan clan) {
        synchronized (ClanManor.LOCK) {
            final ClanManor clanManor1;
            final ClanManor manor = clanManor1 = new ClanManor();
            final byte baseId = ClanManor.baseId;
            ClanManor.baseId = (byte) (baseId + 1);
            clanManor1.clanManorId = baseId;
            manor.maps = new Map[ClanManor.arrMapId.length];
            for (byte i = 0; i < ClanManor.arrMapId.length; ++i) {
                final MapTemplate template = GameScr.mapTemplates[ClanManor.arrMapId[i]];
                manor.maps[i] = new Map(template.mapID, (byte) 20, template.numZone);
                manor.maps[i].clanManor = manor;
            }
            for (byte i = 0; i < manor.maps.length; ++i) {
                final Map map = manor.maps[i];
                if (map != null) {
                    for (byte j = 0; j < map.tileMaps.length; ++j) {
                        final TileMap tileMap = map.tileMaps[j];
                        if (tileMap != null) {
                            tileMap.initWaypoint(7);
                        }
                    }
                }
            }
            for (byte i = 0; i < manor.maps.length; ++i) {
                final Map map = manor.maps[i];
                if (map != null) {
                    map.start();
                }
            }
            manor.isBaoDanh = true;
            manor.timeLength = (int) (System.currentTimeMillis() / 1000L + 600);
            ClanManor.arrClanManor.add(manor);
            clan.clanManor = manor;
            clan.openDun--;
        }
    }

    protected static Map getMap(final ClanManor manor, final short mapId) {
        for (byte i = 0; i < manor.maps.length; ++i) {
            if (manor.maps[i].template.mapID == mapId) {
                return manor.maps[i];
            }
        }
        return null;
    }

    protected void addChar(final Char _char) {
        synchronized (this.memberInManor) {
            this.memberInManor.add(_char);
        }
    }

    protected void removeChar(final Char _char) {
        synchronized (this.memberInManor) {
            for (byte i = 0; i < this.memberInManor.size(); ++i) {
                final Char player = this.memberInManor.get(i);
                if (player != null && player.charID == _char.charID) {
                    this.memberInManor.remove(i);
                }
            }
        }
    }

    protected static void close() {
        synchronized (ClanManor.LOCK) {
            for (int i = ClanManor.arrClanManor.size() - 1; i >= 0; --i) {
                final ClanManor manor = ClanManor.arrClanManor.get(i);
                ClanManor.arrClanManor.remove(i);
                manor.CLOSE();
            }
        }
    }

    protected void CLOSE() {
        final Char[] arrChar = new Char[this.memberInManor.size()];
        synchronized (this.memberInManor) {
            for (byte i = 0; i < arrChar.length; ++i) {
                arrChar[i] = this.memberInManor.get(i);
            }
        }
        for (final Char player : arrChar) {
            if (player != null) {
                player.clan.clanManor = null;
                TileMap.getMapLTD(player);
            }
        }
        for (final Map map2 : this.maps) {
            if (map2 != null) {
                map2.close();
            }
        }
    }

    protected void addCoinManor(final int coin, final int charID) {
        Item itemUp = new Item(null, (short) 262, coin, -1, true, (byte) 0, 5);
        for (final Char player : this.memberInManor) {
            if (player != null) {
                if (player.charID != charID) {
                    synchronized (player.LOCK) {
                        player.user.player.ItemBagAddQuantity(itemUp);
                        Service.ServerMessage(player, String.format(Text.get(0, 403), coin));
                    }
                } else {
                    player.user.player.ItemBagAddQuantity(itemUp);
                    Service.ServerMessage(player, String.format(Text.get(0, 403), coin));
                }
            }
        }
    }

    protected void liveMob(final Map map) {
        try {
            final TileMap tileMap = map.tileMaps[0];
            if (tileMap != null) {
                try {
                    for (short j = 0; j < tileMap.aMob.size(); ++j) {
                        final Mob mob = tileMap.aMob.get(j);
                        if (mob != null && mob.status == 0 && mob.levelBoss == 1) {
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

    protected void openTru(Char _char, int id, Map map) {
        switch (id) {
            case 80:
                if (!openTru80) {
                    if (_char.user.player.ItemBagQuantity((short) 260) < 1) {
                        Service.ServerMessage(_char, "Bạn không có chìa khoá để mở.");
                        return;
                    } else {
                        openTru80 = true;
                        isBaoDanh = false;
                        _char.user.player.ItemBagUses((short) 260, 1);
                        timeLength = (int) (System.currentTimeMillis() / 1000L + 5400);
                        for (Char c : memberInManor) {
                            Service.ServerMessage(c, String.format(Text.get(0, 402), _char.cName, map.template.mapName));
                            Service.mapTime(c, (int) (this.timeLength - System.currentTimeMillis() / 1000L));
                        }
                    }
                }
                break;
            case 81:
                if (!openTru81) {
                    if (_char.user.player.ItemBagQuantity((short) 260) < 1) {
                        Service.ServerMessage(_char, "Bạn không có chìa khoá để mở.");
                        return;
                    } else {
                        openTru81 = true;
                        _char.user.player.ItemBagUses((short) 260, 1);
                        for (Char c : memberInManor) {
                            Service.ServerMessage(c, String.format(Text.get(0, 402), _char.cName, map.template.mapName));
                        }
                    }
                }
                break;
            case 82:
                if (!openTru82) {
                    if (_char.user.player.ItemBagQuantity((short) 260) < 1) {
                        Service.ServerMessage(_char, "Bạn không có chìa khoá để mở.");
                        return;
                    } else {
                        openTru82 = true;
                        _char.user.player.ItemBagUses((short) 260, 1);
                        for (Char c : memberInManor) {
                            Service.ServerMessage(c, String.format(Text.get(0, 402), _char.cName, map.template.mapName));
                        }
                    }
                }
                break;
            case 83:
                if (!openTru83) {
                    if (_char.user.player.ItemBagQuantity((short) 260) < 1) {
                        Service.ServerMessage(_char, "Bạn không có chìa khoá để mở.");
                        return;
                    } else {
                        openTru83 = true;
                        _char.user.player.ItemBagUses((short) 260, 1);
                        for (Char c : memberInManor) {
                            Service.ServerMessage(c, String.format(Text.get(0, 402), _char.cName, map.template.mapName));
                        }
                    }
                }
                break;
            case 84:
                if (!openTru84) {
                    if (_char.user.player.ItemBagQuantity((short) 260) < 1) {
                        Service.ServerMessage(_char, "Bạn không có chìa khoá để mở.");
                        return;
                    } else {
                        openTru84 = true;
                        _char.user.player.ItemBagUses((short) 260, 1);
                        for (Char c : memberInManor) {
                            Service.ServerMessage(c, String.format(Text.get(0, 402), _char.cName, map.template.mapName));
                        }
                    }
                }
                break;
            case 85:
                if (!openTru85) {
                    if (_char.user.player.ItemBagQuantity((short) 260) < 1) {
                        Service.ServerMessage(_char, "Bạn không có chìa khoá để mở.");
                        return;
                    } else {
                        openTru85 = true;
                        _char.user.player.ItemBagUses((short) 260, 1);
                        for (Char c : memberInManor) {
                            Service.ServerMessage(c, String.format(Text.get(0, 402), _char.cName, map.template.mapName));
                        }
                    }
                }
                break;
            case 86:
                if (!openTru86) {
                    if (_char.user.player.ItemBagQuantity((short) 260) < 1) {
                        Service.ServerMessage(_char, "Bạn không có chìa khoá để mở.");
                        return;
                    } else {
                        openTru86 = true;
                        _char.user.player.ItemBagUses((short) 260, 1);
                        for (Char c : memberInManor) {
                            Service.ServerMessage(c, String.format(Text.get(0, 402), _char.cName, map.template.mapName));
                        }
                    }
                }
                break;
            case 87:
                if (!openTru87) {
                    if (_char.user.player.ItemBagQuantity((short) 260) < 1) {
                        Service.ServerMessage(_char, "Bạn không có chìa khoá để mở.");
                        return;
                    } else {
                        openTru87 = true;
                        _char.user.player.ItemBagUses((short) 260, 1);
                        Service.AlertMessage(_char, "Ghi chú", map.template.mapDescription);
                        for (Char c : memberInManor) {
                            Service.ServerMessage(c, String.format(Text.get(0, 402), _char.cName, map.template.mapName));
                        }
                    }
                }
            case 88:
                if (!openTru88) {
                    if (_char.user.player.ItemBagQuantity((short) 260) < 1) {
                        Service.ServerMessage(_char, "Bạn không có chìa khoá để mở.");
                        return;
                    } else {
                        openTru88 = true;
                        _char.user.player.ItemBagUses((short) 260, 1);
                        Service.AlertMessage(_char, "Ghi chú", map.template.mapDescription);
                        for (Char c : memberInManor) {
                            Service.ServerMessage(c, String.format(Text.get(0, 402), _char.cName, map.template.mapName));
                        }
                    }
                }
                break;
            case 89:
                if (!openTru89) {
                    if (_char.user.player.ItemBagQuantity((short) 260) < 1) {
                        Service.ServerMessage(_char, "Bạn không có chìa khoá để mở.");
                        return;
                    } else {
                        openTru89 = true;
                        _char.user.player.ItemBagUses((short) 260, 1);
                        Service.AlertMessage(_char, "Ghi chú", map.template.mapDescription);
                        for (Char c : memberInManor) {
                            Service.ServerMessage(c, String.format(Text.get(0, 402), _char.cName, map.template.mapName));
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    static {
        arrMapId = new short[]{80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90};
        LOCK = new Object();
        ClanManor.arrClanManor = new ArrayList<>();
    }
}
