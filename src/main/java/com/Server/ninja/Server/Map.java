//
// Decompiled by Procyon v0.5.36
//

package com.Server.ninja.Server;

import java.util.concurrent.TimeUnit;

import com.Server.ninja.template.MapTemplate;
import org.jetbrains.annotations.Nullable;

public class Map {
    public static final int[] arrLang;
    public static final int[] arrTruong;
    protected static final byte MAX_CREATE_PARTY = 4;
    protected MapTemplate template;
    protected TileMap[] tileMaps;
    protected Npc[] npcs;
    protected byte maxPlayer;
    protected WarClan warClan;
    protected BackCave backCave;
    protected TestDun testDun;
    protected ChienTruong chientruong;
    protected ClanManor clanManor;
    protected SevenBeasts sevenBeasts;
    // tài năng
    protected NJTalent njTalent;
    private boolean isrun;
    private final Thread threadUpdate;
    private static final int DELAY = 500;
    public static int[] idMapThuong = new int[]{4, 5, 7, 8, 9, 11, 12, 13, 14, 15, 16, 18, 19, 24, 28, 29, 30, 31, 33, 34, 35, 36, 37, 39, 40, 41, 42, 46, 47, 48, 49, 50, 51, 52, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68};

    protected static void liveBoss(final Map map, final byte zone, final byte type) {
        try {
            final TileMap tileMap = map.tileMaps[zone];
            if (tileMap != null) {
                tileMap.lock.lock("Live boss");
                try {
                    for (short j = 0; j < tileMap.aMob.size(); ++j) {
                        final Mob mob = tileMap.aMob.get(j);
                        if (mob != null && mob.status == 0 && (mob.isBoss || mob.templateId == 202)) {
                            Mob.LiveMob(mob, (byte) Util.nextInt(1, 3), (byte) mob.levelBoss, -1, -1);
                        }
                    }
                } finally {
                    tileMap.lock.unlock();
                }
            }
        } catch (Exception ex) {
        }
    }

    protected Map(final short id, final byte maxPlayer, final byte numZone) {
        this.warClan = null;
        this.backCave = null;
        this.testDun = null;
        this.chientruong = null;
        this.clanManor = null;
        this.sevenBeasts = null;
        // tài năng
        this.njTalent = null;
        this.threadUpdate = new Thread(new update());
        this.template = GameScr.mapTemplates[id];
        this.maxPlayer = maxPlayer;
        this.tileMaps = new TileMap[numZone];
        for (byte i = 0; i < numZone; ++i) {
            this.tileMaps[i] = new TileMap(this, i);
        }
        this.npcs = new Npc[this.template.npcType.length];
    }

    protected TileMap getSlotZone(final Char _char) {
        try {
            if (_char.party != null && _char.party.charID != _char.charID) {
                for (byte i = 0; i < this.tileMaps.length; ++i) {
                    final TileMap tileMap = this.tileMaps[i];
                    for (short j = 0; j < tileMap.numPlayer; ++j) {
                        final Char player = tileMap.aCharInMap.get(j);
                        if (player != null && player.charID == _char.party.charID) {
                            return tileMap;
                        }
                    }
                }
            }
            if (_char.party != null) {
                synchronized (_char.party.LOCK) {
                    byte index = -1;
                    int numMax = 1;
                    for (byte k = 0; k < this.tileMaps.length; ++k) {
                        final TileMap tileMap2 = this.tileMaps[k];
                        final int numParty = tileMap2.getNumPlayerParty(_char.party.partyId);
                        if (numMax < numParty) {
                            index = k;
                            numMax = numParty;
                        }
                    }
                    if (index != -1) {
                        return this.tileMaps[index];
                    }
                }
            }
            if (_char.party != null) {
                synchronized (_char.party.LOCK) {
                    for (byte n = 0; n < _char.party.numPlayer; ++n) {
                        final Char pplayer = _char.party.aChar.get(n);
                        for (byte k = 0; k < this.tileMaps.length; ++k) {
                            final TileMap tileMap2 = this.tileMaps[k];
                            for (short l = 0; l < tileMap2.numPlayer; ++l) {
                                final Char cplayer = tileMap2.aCharInMap.get(l);
                                if (cplayer != null && cplayer.charID == pplayer.charID) {
                                    return tileMap2;
                                }
                            }
                        }
                    }
                }
            }
            for (byte i = 0; i < this.tileMaps.length; ++i) {
                final TileMap tileMap = this.tileMaps[i];
                tileMap.lock.lock("Tim khu");
                try {
                    if (tileMap.numPlayer < this.maxPlayer && (_char.party == null || tileMap.numParty < 6)) {
                        return tileMap;
                    }
                } finally {
                    tileMap.lock.unlock();
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }

    @Nullable
    public TileMap getFreeArea() {
        for (int i = 0; i < this.tileMaps.length; i++) {
            if (this.tileMaps[i].aCharInMap.isEmpty()) {
                return this.tileMaps[i];
            }
        }
        return null;
    }

    private void update() {
        for (byte num = 0; num < this.tileMaps.length; ++num) {
            final TileMap tileMap = this.tileMaps[num];
            tileMap.updateTask(System.currentTimeMillis());
            try {
                tileMap.lock.lock("Update tileMap");
                try {
                    for (int i = tileMap.aCharInMap.size() - 1; i >= 0; --i) {
                        final Char _char = tileMap.aCharInMap.get(i);
                        if (_char != null && _char.user.player != null) {
                            if (_char.tileMap == null || (_char.isHuman && Client.getPlayer(_char.charID) == null) || _char.tileMap.mapID != this.template.mapID || _char.tileMap.zoneID != tileMap.zoneID || (!_char.isHuman && !_char.isNhanban && _char.user.player.isBatNhanban && _char.timeLiveNhanban != -1 && _char.timeLiveNhanban <= System.currentTimeMillis() / 1000L) || (_char.isHuman && _char.Nhanban != null && _char.isBatNhanban && _char.Nhanban.timeLiveNhanban != -1 && _char.Nhanban.timeLiveNhanban <= System.currentTimeMillis() / 1000L)) {
                                if (!_char.isHuman && !_char.isNhanban && _char.user.player.isBatNhanban) {
                                    Player.toChar(_char);
                                    _char.timeLiveNhanban = -1;
                                } else if (_char.isHuman && _char.Nhanban != null && _char.isBatNhanban) {
                                    _char.Nhanban.timeLiveNhanban = -1;
                                    _char.isBatNhanban = false;
                                    tileMap.aCharInMap.remove(_char.Nhanban);
                                    final TileMap tileMap4;
                                    final TileMap tileMap2 = tileMap4 = tileMap;
                                    --tileMap4.numPlayer;
                                    for (int j = 0; j < tileMap.numPlayer; ++j) {
                                        if (tileMap.aCharInMap.get(j).user != null && tileMap.aCharInMap.get(j).user.session != null) {
                                            Service.PlayerRemove(tileMap.aCharInMap.get(j), _char.Nhanban.charID);
                                        }
                                    }
                                } else {
                                    tileMap.aCharInMap.remove(_char);
                                    final TileMap tileMap5;
                                    final TileMap tileMap3 = tileMap5 = tileMap;
                                    --tileMap5.numPlayer;
                                    for (int j = 0; j < tileMap.numPlayer; ++j) {
                                        if (tileMap.aCharInMap.get(j).user != null && tileMap.aCharInMap.get(j).user.session != null) {
                                            Service.PlayerRemove(tileMap.aCharInMap.get(j), _char.charID);
                                        }
                                    }
                                }
                            } else if (_char.user != null) {
                                synchronized (_char.user.LOCK) {
                                    for (byte k = 0; k < _char.user.player.ItemBag.length; ++k) {
                                        final Item item = _char.user.player.ItemBag[k];
                                        if (item != null && item.expires != -1L && item.expires <= System.currentTimeMillis()) {
                                            _char.user.player.ItemBagClear(k);
                                        }
                                    }
                                    final Item[] itemBody = _char.ItemBody;
                                    for (byte l = 0; l < itemBody.length; ++l) {
                                        final Item item2 = itemBody[l];
                                        if (item2 != null && item2.expires != -1L && item2.expires <= System.currentTimeMillis()) {
                                            _char.ItemBodyClear(l);
                                            Service.updateInfoMe(_char);
                                            Player.updateInfoPlayer(_char);
                                            if (_char.ItemBody[10] != null) {
                                                Player.updateThuNuoiPlayer(_char);
                                            }
                                            if (_char.arrItemViThu[4] != null) {
                                                Player.LoadViThu(_char);
                                            }
                                        }
                                    }
                                    synchronized (_char.aEff) {
                                        for (byte m = 0; m < _char.aEff.size(); ++m) {
                                            final Effect effect = _char.aEff.get(m);
                                            if (effect != null) {
                                                if ((effect.timeStart == -1 && effect.timeLenght / 1000 <= 0) || (effect.timeStart != -1 && System.currentTimeMillis() - effect.timeStart * 1000L >= effect.timeLenght)) {
                                                    Char.removeEffect(_char, effect.template.id);
                                                    ++m;
                                                } else {
                                                    if (effect.timeStart == -1) {
                                                        final Effect effect3;
                                                        final Effect effect2 = effect3 = effect;
                                                        effect3.timeLenght -= 500;
                                                    }
                                                    if (effect.template.type == 0 || effect.template.type == 12) {
                                                        _char.upHP(effect.param);
                                                        _char.upMP(effect.param);
                                                    } else if (effect.template.type == 5) {
                                                        synchronized (_char.aBurn) {
                                                            final Char char6;
                                                            final Char char1 = char6 = _char;
                                                            char6.timeBurn -= 500;
                                                            if (_char.timeBurn <= 0) {
                                                                for (short k2 = 0; k2 < _char.aBurn.size(); ++k2) {
                                                                    final Mob mob = _char.aBurn.get(k2);
                                                                    if (mob != null) {
                                                                        if (!mob.injureThenDie && Math.abs(mob.x - _char.cx) <= _char.dXYBurn && Math.abs(mob.y - _char.cy) <= _char.dXYBurn) {
                                                                            for (short n = 0; n < tileMap.numPlayer; ++n) {
                                                                                if (tileMap.aCharInMap.get(n).user != null && tileMap.aCharInMap.get(n).user.session != null) {
                                                                                    Service.thieuDot(tileMap.aCharInMap.get(n), mob.mobId);
                                                                                }
                                                                            }
                                                                            SkillUse.AttackMob(_char.tileMap, _char, _char, mob, _char.cDame(mob.sys, 0, 0, 0, 0, 0, 0, 0, 0, false, 0, 0) * effect.param / 100, false, (byte) 1);
                                                                        } else if (mob.injureThenDie) {
                                                                            _char.aBurn.remove(k2);
                                                                            --k2;
                                                                        }
                                                                    }
                                                                }
                                                                _char.timeBurn = 1000;
                                                            }
                                                        }
                                                    } else if (effect.template.type == 13 && _char.statusMe != 14) {
                                                        _char.upHP(-(_char.cMaxHP() * 3 / 100));
                                                    } else if (effect.template.type == 17 || effect.template.type == 4) {
                                                        _char.upHP(effect.param);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (_char.delaytestCharID > 0 && !_char.isTest) {
                                        final Char char7;
                                        final Char char2 = char7 = _char;
                                        --char7.delaytestCharID;
                                        if (_char.delaytestCharID == 0) {
                                            _char.testCharID = -9999;
                                        }
                                    }
                                    if (_char.delaytestDunCharID > 0) {
                                        final Char char8;
                                        final Char char3 = char8 = _char;
                                        --char8.delaytestDunCharID;
                                        if (_char.delaytestDunCharID <= 0) {
                                            _char.delaytestDunCharID = 0;
                                            _char.testDunCharID = -9999;
                                        }
                                    }
                                    if (_char.user.player.delayTrade > 0 && !_char.user.player.isTrade) {
                                        final Player player8;
                                        final Player player3 = player8 = _char.user.player;
                                        player8.delayTrade -= 500;
                                        if (_char.user.player.delayTrade == 0) {
                                            _char.user.player.tradeCharId = -9999;
                                        }
                                    }
                                    if (_char.user.player.delayCancelTrade > 0 && !_char.user.player.isTrade) {
                                        final Player player9;
                                        final Player player4 = player9 = _char.user.player;
                                        --player9.delayCancelTrade;
                                        if (_char.user.player.delayCancelTrade == 0) {
                                            _char.user.player.tradeCharIdwait = -9999;
                                        }
                                    }
                                    if (_char.mobMe != null && Mob.arrMobTemplate[_char.mobMe.templateId].isAttack && _char.statusMe != 14 && _char.mobFocus != null && !_char.mobFocus.injureThenDie && Math.abs(_char.cx - _char.mobFocus.x) < 100 && Math.abs(_char.cy - _char.mobFocus.y) < 80) {
                                        if (_char.mobMe.fighttime <= 0) {
                                            _char.mobMe.fighttime = 3000;
                                            Mob.MobMeAtk(_char, _char.mobFocus, null);
                                        } else {
                                            final Mob mobMe2;
                                            final Mob mobMe = mobMe2 = _char.mobMe;
                                            mobMe2.fighttime -= 500;
                                        }
                                    }
                                    synchronized (_char.aPartyInvate) {
                                        if (_char.party != null) {
                                            _char.aPartyInvate.clear();
                                        } else {
                                            for (short j2 = 0; j2 < _char.aPartyInvate.size(); ++j2) {
                                                final Please Invate = _char.aPartyInvate.get(j2);
                                                if (Invate != null) {
                                                    final Please please5;
                                                    final Please please = please5 = Invate;
                                                    please5.timeLive -= 500;
                                                    if (Invate.timeLive <= 0) {
                                                        _char.aPartyInvate.remove(j2);
                                                        --j2;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    synchronized (_char.aPartyInvite) {
                                        if (_char.party == null) {
                                            _char.aPartyInvite.clear();
                                        } else {
                                            for (short j2 = 0; j2 < _char.aPartyInvite.size(); ++j2) {
                                                final Please Invate = _char.aPartyInvite.get(j2);
                                                if (Invate != null) {
                                                    final Please please6;
                                                    final Please please2 = please6 = Invate;
                                                    please6.timeLive -= 500;
                                                    if (Invate.timeLive <= 0) {
                                                        _char.aPartyInvite.remove(j2);
                                                        --j2;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    synchronized (_char.aClanInvate) {
                                        if (_char.clan != null) {
                                            _char.aClanInvate.clear();
                                        } else {
                                            for (short j2 = 0; j2 < _char.aClanInvate.size(); ++j2) {
                                                final Please Invate = _char.aClanInvate.get(j2);
                                                if (Invate != null) {
                                                    final Please please7;
                                                    final Please please3 = please7 = Invate;
                                                    please7.timeLive -= 500;
                                                    if (Invate.timeLive <= 0) {
                                                        _char.aClanInvate.remove(j2);
                                                        --j2;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    synchronized (_char.aClanInvite) {
                                        if (_char.clan == null) {
                                            _char.aClanInvite.clear();
                                        } else {
                                            for (short j2 = 0; j2 < _char.aClanInvite.size(); ++j2) {
                                                final Please Invate = _char.aClanInvite.get(j2);
                                                if (Invate != null) {
                                                    final Please please8;
                                                    final Please please4 = please8 = Invate;
                                                    please8.timeLive -= 500;
                                                    if (Invate.timeLive <= 0) {
                                                        _char.aClanInvite.remove(j2);
                                                        --j2;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (_char.user.player.delayLiveGold > 0) {
                                    final Player player10;
                                    final Player player5 = player10 = _char.user.player;
                                    player10.delayLiveGold -= 500;
                                }
                                if (_char.delaythacau > 0) {
                                    final Char char9;
                                    final Char char4 = char9 = _char;
                                    char9.delaythacau -= 500;
                                    if (_char.delaythacau <= 0) {
                                        Service.ServerMessage(_char, "C\u00e2u c\u00e1i n\u1ecbt...");
                                    }
                                }
                            }
                            final Char char10;
                            final Char char5 = char10 = _char;
                            char10.delayEffect -= 500;
                            if (_char.delayEffect <= 0) {
                                _char.delayEffect = 5000;
                                final byte ngoc = _char.full_Ngoc();
                                if (ngoc >= 6) {
                                    byte effectId = 0;
                                    switch (_char.sys()) {
                                        case 1: {
                                            effectId = 9;
                                            break;
                                        }
                                        case 2: {
                                            effectId = 3;
                                            break;
                                        }
                                        case 3: {
                                            effectId = 6;
                                            break;
                                        }
                                    }
                                    if (ngoc >= 10) {
                                        effectId += 2;
                                    } else if (ngoc >= 8) {
                                        ++effectId;
                                    } else if (ngoc >= 6) {
                                        effectId += 0;
                                    }
                                    try {
                                        for (short k3 = 0; k3 < _char.tileMap.numPlayer; ++k3) {
                                            if (_char.tileMap.aCharInMap.get(k3).user != null && _char.tileMap.aCharInMap.get(k3).user.session != null) {
                                                GameCanvas.getEffect(_char.tileMap.aCharInMap.get(k3), (byte) 0, _char.charID, effectId, 5000, 5000, false);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if (_char.typeCaiTrang != -1 && _char.typeCaiTrang != 10) {
                                byte effId = 0;
                                if (_char.typeCaiTrang == 0 && CaiTrang.checkUpgradeCT(_char, 0) >= 10) {
                                    effId = 23;
                                } else if (_char.typeCaiTrang == 0 && CaiTrang.checkUpgradeCT(_char, 0) >= 6) {
                                    effId = 22;
                                }
                                if (_char.typeCaiTrang == 1 && CaiTrang.checkUpgradeCT(_char, 1) >= 10) {
                                    effId = 25;
                                } else if (_char.typeCaiTrang == 1 && CaiTrang.checkUpgradeCT(_char, 1) >= 6) {
                                    effId = 24;
                                }
                                if (_char.typeCaiTrang == 2 && CaiTrang.checkUpgradeCT(_char, 2) >= 10) {
                                    effId = 27;
                                } else if (_char.typeCaiTrang == 2 && CaiTrang.checkUpgradeCT(_char, 2) >= 6) {
                                    effId = 26;
                                }
                                try {
                                    for (short k3 = 0; k3 < _char.tileMap.numPlayer; ++k3) {
                                        if (_char.tileMap.aCharInMap.get(k3).user != null && _char.tileMap.aCharInMap.get(k3).user.session != null) {
                                            GameCanvas.getEffect(_char.tileMap.aCharInMap.get(k3), (byte) 0, _char.charID, effId, 3000, 3000, false);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (_char.user != null) { // effect auto in map
                                byte idEff = -1;
                                switch (GameScr.vEvent) {
                                    case 2:
                                        idEff = 12;
                                        break;
                                    case 3:
                                        idEff = 13;
                                        break;
                                    case 4:
                                        break;
                                }
                                if (idEff != -1) {
                                    if (_char.tileMap.mapID == 1) {
                                        Service.sendEffectAuto(_char, idEff, 1212, 168, (byte) -1, (short) -1);
                                    } else if (_char.tileMap.mapID == 27) {
                                        Service.sendEffectAuto(_char, idEff, 492, 408, (byte) -1, (short) -1);
                                        Service.sendEffectAuto(_char, idEff, 1860, 360, (byte) -1, (short) -1);
                                        Service.sendEffectAuto(_char, idEff, 2052, 360, (byte) -1, (short) -1);
                                    } else if (_char.tileMap.mapID == 72) {
                                        Service.sendEffectAuto(_char, idEff, 780, 648, (byte) -1, (short) -1);
                                        Service.sendEffectAuto(_char, idEff, 1428, 552, (byte) -1, (short) -1);
                                    } else if (_char.tileMap.mapID == 10) {
                                        Service.sendEffectAuto(_char, idEff, 564, 168, (byte) -1, (short) -1);
                                        Service.sendEffectAuto(_char, idEff, 180, 192, (byte) -1, (short) -1);
                                    } else if (_char.tileMap.mapID == 17) {
                                        Service.sendEffectAuto(_char, idEff, 300, (short) 288, (byte) -1, (short) -1);
                                    } else if (_char.tileMap.mapID == 22) {
                                        Service.sendEffectAuto(_char, idEff, 1428, 264, (byte) -1, (short) -1);
                                    } else if (_char.tileMap.mapID == 32) {
                                        Service.sendEffectAuto(_char, idEff, 660, 312, (byte) -1, (short) -1);
                                        Service.sendEffectAuto(_char, idEff, 1812, 288, (byte) -1, (short) -1);
                                    } else if (_char.tileMap.mapID == 38) {
                                        Service.sendEffectAuto(_char, idEff, 492, 336, (byte) -1, (short) -1);
                                    } else if (_char.tileMap.mapID == 43) {
                                        Service.sendEffectAuto(_char, idEff, 2124, 456, (byte) -1, (short) -1);
                                    } else if (_char.tileMap.mapID == 48) {
                                        Service.sendEffectAuto(_char, idEff, 444, 360, (byte) -1, (short) -1);
                                        Service.sendEffectAuto(_char, idEff, 1188, 360, (byte) -1, (short) -1);
                                    }
                                }
                            }
                            if (!_char.isHuman && _char.cHP <= 0) {
                                Player.toChar(_char);
                            }
                            if (_char.ctaskId == 17 && _char.ctaskIndex == 1 && !tileMap.haveBotInMap(_char)) {
                                Service.npcPlayerUpdate(_char,(byte) tileMap.aNpc.get(0).index,(byte) 1);
                            } else if (_char.ctaskId == 17 && _char.ctaskIndex == 1 && tileMap.haveBotInMap(_char)) {
                                Service.npcPlayerUpdate(_char,(byte) tileMap.aNpc.get(0).index,(byte) 15);
                            }
                            if (this.isMapvip() && this.isMapLangCo() && (_char.cHP <= 0 || _char.cPk > 0) && _char.isHuman && _char.tileMap.map.isMapLangCo()&& _char.tileMap.map.isMapvip()) {
                                final Map map = MapServer.getMapServer(22);
                                if (map != null) {
                                    final TileMap ltd = map.getSlotZone(_char);
                                    if (ltd == null) {
                                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                    } else {
                                        tileMap.EXIT(_char);
                                        if (_char.statusMe == 14) {
                                            _char.cHP = _char.cMaxHP();
                                            _char.cMP = _char.cMaxMP();
                                            _char.statusMe = 1;
                                            Service.MeLive(_char);
                                        }
                                        _char.cx = ltd.map.template.goX;
                                        _char.cy = ltd.map.template.goY;
                                        ltd.Join(_char);
                                    }
                                }
                            }
                        }
                    }
                    for (short i2 = 0; i2 < tileMap.aBuNhin.size(); ++i2) {
                        final BuNhin bunhin = tileMap.aBuNhin.get(i2);
                        if (bunhin != null) {
                            final BuNhin buNhin2;
                            final BuNhin buNhin = buNhin2 = bunhin;
                            buNhin2.timeRemove -= 500;
                            if (bunhin.timeRemove <= 0) {
                                tileMap.aBuNhin.remove(i2);
                                try {
                                    for (short k4 = 0; k4 < tileMap.numPlayer; ++k4) {
                                        if (tileMap.aCharInMap.get(k4).user != null && tileMap.aCharInMap.get(k4).user.session != null) {
                                            Service.ClearBunhin(tileMap.aCharInMap.get(k4), i2);
                                        }
                                    }
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                                --i2;
                            }
                        }
                    }
                    for (byte i3 = 0; i3 < tileMap.aNpc.size(); ++i3) {
                        final Npc npc = tileMap.aNpc.get(i3);
                        if (npc.template.npcTemplateId == 31) {
                            if (npc.Lighttime > 0) {
                                final Npc npc4;
                                final Npc npc2 = npc4 = npc;
                                npc4.Lighttime -= 500;
                            }
                            if (Server.Lighttime && npc.Lighttime <= 0 && npc.type == 15) {
                                tileMap.NpcPlayerUpdate((byte) npc.index, (byte) 1);
                            } else if (!Server.Lighttime && npc.type != 15) {
                                tileMap.NpcPlayerUpdate((byte) npc.index, (byte) 15);
                            }
                        } else if (npc.template.npcTemplateId == 34 && (GameScr.vEvent == 3 || GameScr.vEvent == 4)) {
                            npc.type = 1;
                        }
                        if (npc.isQC) {
                            final Npc npc5;
                            final Npc npc3 = npc5 = npc;
                            npc5.delayQC -= 500;
                            if (npc.delayQC <= 0) {
                                for (short n2 = 0; n2 < tileMap.numPlayer; ++n2) {
                                    final Char player6 = tileMap.aCharInMap.get(n2);
                                    if (player6 != null && player6.user != null && player6.user.session != null) {
                                        Service.openUISay(player6, (short) npc.template.npcTemplateId, Text.get(0, 276));
                                    }
                                }
                                npc.delayQC = 5000;
                            }
                        }
                    }
                    for (short i2 = 0; i2 < tileMap.aItemMap.size(); ++i2) {
                        final ItemMap itemMap = tileMap.aItemMap.get(i2);
                        if (itemMap != null) {
                            if (itemMap.timeRemove <= System.currentTimeMillis() && !itemMap.isItemTask()) {
                                tileMap.aItemMap.remove(i2);
                                try {
                                    for (short k5 = 0; k5 < tileMap.numPlayer; ++k5) {
                                        if (tileMap.aCharInMap.get(k5).user != null && tileMap.aCharInMap.get(k5).user.session != null) {
                                            Service.removeItemMap(tileMap.aCharInMap.get(k5), itemMap.itemMapID);
                                        }
                                    }
                                } catch (Exception e3) {
                                    e3.printStackTrace();
                                }
                                --i2;
                            } else if (itemMap.playerId != 0 && itemMap.timeRemove - System.currentTimeMillis() < 45000L) {
                                itemMap.playerId = 0;
                            }
                        }
                    }
                    for (int i = tileMap.aMob.size() - 1; i >= 0; --i) {
                        final Mob mob2 = tileMap.aMob.get(i);
                        if (mob2 != null) {
                            synchronized (mob2.LOCK) {
                                boolean isXoa = false;
                                if (mob2.playerId != -1) {
                                    isXoa = true;
                                    for (short j3 = 0; j3 < tileMap.aCharInMap.size(); ++j3) {
                                        final Char player7 = tileMap.aCharInMap.get(j3);
                                        if (player7 != null && !player7.isNhanban && player7.user.player.playerId == mob2.playerId) {
                                            isXoa = false;
                                            break;
                                        }
                                    }
                                    if (isXoa) {
                                        tileMap.aMob.remove(i);
                                    }
                                }
                                if (!isXoa) {
                                    if (mob2.timeStartDie + mob2.RefreshTimeDie < System.currentTimeMillis() && !mob2.removeWhenDie && mob2.injureThenDie) {
                                        byte levelBoss = (byte) mob2.levelBoss;
                                        if (mob2.levelBoss != 3 && !mob2.isBoss && mob2.level > 9 && !this.isBackCaveMap() && !this.isClanManor() && mob2.templateId != 98 && mob2.templateId != 99) {
                                            levelBoss = 0;
                                            if (tileMap.getlevelBoss((byte) 1) < 2 && Util.nextInt(1000) < 10 && !this.isChienTruong()) {
                                                levelBoss = 1;
                                            } else if (tileMap.getlevelBoss((byte) 2) < 1 && Util.nextInt(1000) < 5) {
                                                levelBoss = 2;
                                            }
                                        }
                                        Mob.LiveMob(mob2, (byte) Util.nextInt(1, 3), levelBoss, -1, -1);
                                    } else if (mob2.status == 1) {
                                        mob2.status = 0;
                                    } else if (mob2.status != 0 && mob2.status != 1) {
                                        if (Mob.arrMobTemplate[mob2.templateId].isAttack) {
                                            final Mob mob9;
                                            final Mob mob3 = mob9 = mob2;
                                            mob9.fighttime -= 500;
                                            if (mob2.fighttime <= 0) {
                                                Mob.MobAttack(mob2);
                                            }
                                        }
                                        if (mob2.isDisable) {
                                            final Mob mob10;
                                            final Mob mob4 = mob10 = mob2;
                                            mob10.timeDisable -= 500;
                                            if (mob2.timeDisable <= 0) {
                                                Mob.upDisable(mob2, false, 0);
                                            }
                                        }
                                        if (mob2.isDontMove) {
                                            final Mob mob11;
                                            final Mob mob5 = mob11 = mob2;
                                            mob11.timeDontMove -= 500;
                                            if (mob2.timeDontMove <= 0) {
                                                Mob.upMove(mob2, false, 0);
                                            }
                                        }
                                        if (mob2.isFire) {
                                            final Mob mob12;
                                            final Mob mob6 = mob12 = mob2;
                                            mob12.timeFrie -= 500;
                                            if (mob2.timeFrie <= 0) {
                                                Mob.upFire(mob2, false, 0);
                                            }
                                        }
                                        if (mob2.isIce) {
                                            final Mob mob13;
                                            final Mob mob7 = mob13 = mob2;
                                            mob13.timeIce -= 500;
                                            if (mob2.timeIce <= 0) {
                                                Mob.upIce(mob2, false, 0);
                                            }
                                        }
                                        if (mob2.isWind) {
                                            final Mob mob14;
                                            final Mob mob8 = mob14 = mob2;
                                            mob14.timeWind -= 500;
                                            if (mob2.timeWind <= 0) {
                                                Mob.upWind(mob2, false, 0);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (tileMap.map.isChienTruong() && tileMap.map.chientruong.level == 2 && !tileMap.map.isMapBlack() && !tileMap.map.isMapWhite()) {
                        for (int i = tileMap.aCharInMap.size() - 1; i >= 0; --i) {
                            final Char _char = tileMap.aCharInMap.get(i);
                            if (_char != null && !_char.isNhanban) {
                                Map map2 = null;
                                if (tileMap.map.isChienTruong() && tileMap.map.chientruong.aCharWhite.contains(_char.user.player.playerId)) {
                                    map2 = ChienTruong.getMap(tileMap.map.chientruong, (short) 98);
                                } else if (tileMap.map.isChienTruong() && tileMap.map.chientruong.aCharBlack.contains(_char.user.player.playerId)) {
                                    map2 = ChienTruong.getMap(tileMap.map.chientruong, (short) 104);
                                }
                                if (map2 != null) {
                                    final TileMap tile = map2.getSlotZone(_char);
                                    if (tile == null) {
                                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 24));
                                    } else {
                                        tileMap.EXIT(_char);
                                        if (_char.statusMe == 14) {
                                            _char.cHP = _char.cMaxHP();
                                            _char.cMP = _char.cMaxMP();
                                            _char.statusMe = 1;
                                            Service.MeLive(_char);
                                        }
                                        _char.cx = tile.map.template.goX;
                                        _char.cy = tile.map.template.goY;
                                        tile.Join(_char);
                                    }
                                }
                            }
                        }
                    }
                } finally {
                    tileMap.lock.unlock();
                }
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
    }

    public void start() {
        if (this.isrun) {
            this.close();
        }
        this.isrun = true;
        this.threadUpdate.start();
    }

    protected boolean isMapLangCo() {
        return this.template.mapID >= 134 && this.template.mapID <= 138;
    }
    
    protected boolean isMapvip() {
        return this.template.mapID == 161;
    }

    
    protected boolean isMonsterMap() {
        return this.template.mapID >= 139 && this.template.mapID <= 148;
    }

    protected boolean isWarClanMap() {
        return this.template.mapID >= 117 && this.template.mapID <= 124;
    }

    protected boolean isBackCaveMap() {
        return (this.template.mapID >= 91 && this.template.mapID <= 97) || (this.template.mapID >= 105 && this.template.mapID <= 109) || (this.template.mapID >= 114 && this.template.mapID <= 116) || (this.template.mapID >= 125 && this.template.mapID <= 128) || (this.template.mapID >= 157 && this.template.mapID <= 159);
    }

    protected boolean isTestDunMap() {
        return this.template.mapID == 110 || this.template.mapID == 111;
    }

    protected boolean isChienTruong() {
        return this.template.mapID >= 98 && this.template.mapID <= 104;
    }

    protected boolean isClanManor() {
        return this.template.mapID >= 80 && this.template.mapID <= 90;
    }

    protected boolean isSevenBeasts() {
        return this.template.mapID >= 112 && this.template.mapID <= 113;
    }

    protected boolean isMapWhite() {
        return this.template.mapID == 98;
    }

    protected boolean isMapTalent() {
        return this.template.mapID == 129;
    }

    protected boolean isMapBlack() {
        return this.template.mapID == 104;
    }

    protected boolean isPKMap() {
        return (this.template.mapID >= 98 && this.template.mapID <= 104) || this.template.mapID == 111;
    }

    protected boolean mapNotUIZone() {
        return this.template.mapID == 56 || this.template.mapID == 0 || this.template.mapID == 73 || this.template.mapID == 111;
    }
    
    public boolean mapThuong() {
        int i;
        for (i = 0; i < this.idMapThuong.length; i++) {
            if (this.template.mapID == this.idMapThuong[i]) {
                return true;
            }
        }
        return false;
    }

    protected void close() {
        this.isrun = false;
        for (byte i = 0; i < this.tileMaps.length; ++i) {
            this.tileMaps[i].lock.close();
        }
    }

    static {
        arrLang = new int[]{10, 17, 22, 32, 38, 43, 48};
        arrTruong = new int[]{1, 27, 72};
    }

    private class update implements Runnable {
        @Override
        public void run() {
            while (Map.this.isrun) {
                try {
                    final long l1 = System.currentTimeMillis();
                    Map.this.update();
                    final long l2 = System.currentTimeMillis() - l1;
                    if (500L - l2 < 0L) {
                        continue;
                    }
                    TimeUnit.MILLISECONDS.sleep(500L - l2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
