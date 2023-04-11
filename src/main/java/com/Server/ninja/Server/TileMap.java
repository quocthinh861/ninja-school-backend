package com.Server.ninja.Server;

import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import com.Server.ninja.template.MapTemplate;
import java.awt.Color;
import java.util.HashMap;
import java.awt.Image;
import com.Server.io.Lock;
import com.Server.io.Message;
import java.io.IOException;
import lombok.val;

import java.util.ArrayList;

public class TileMap
{
    protected Map map;
    protected final ArrayList<Waypoint> aVgo;
    protected ArrayList<ItemTree> aItemTreeFront;
    protected ArrayList<ItemTree> aItemTreeBehind;
    protected ArrayList<ItemTree> aItemTreeBetwen;
    protected final ArrayList<Char> aCharInMap;
    protected final ArrayList<Mob> aMob;
    protected final ArrayList<BuNhin> aBuNhin;
    protected final ArrayList<Npc> aNpc;
    protected final ArrayList<ItemMap> aItemMap;
    protected final ArrayList<Party> aParty;
    protected int numPlayer;
    protected int numParty;
    protected short maxMobId;
    protected final Lock lock;
    protected short itemMapID;
    protected static final int T_EMPTY = 0;
    protected static final int T_TOP = 2;
    protected static final int T_LEFT = 4;
    protected static final int T_RIGHT = 8;
    protected static final int T_TREE = 16;
    protected static final int T_WATERFALL = 32;
    protected static final int T_WATERFLOW = 64;
    protected static final int T_TOPFALL = 128;
    protected static final int T_OUTSIDE = 256;
    protected static final int T_DOWN1PIXEL = 512;
    protected static final int T_BRIDGE = 1024;
    protected static final int T_UNDERWATER = 2048;
    protected static final int T_SOLIDGROUND = 4096;
    protected static final int T_BOTTOM = 8192;
    protected static final int T_DIE = 16384;
    protected static final int T_HEBI = 32768;
    protected static final int T_BANG = 65536;
    protected static final int T_JUM8 = 131072;
    protected static final int T_NT0 = 262144;
    protected static final int T_NT1 = 524288;
    protected int tmw;
    protected int tmh;
    protected int pxw;
    protected int pxh;
    protected int tileID;
    protected char[] maps;
    protected int[] types;
    protected Image imgTileSmall;
    protected Image imgMiniMap;
    protected Image imgWaterfall;
    protected Image imgTopWaterfall;
    protected Image imgWaterflow;
    protected Image imgLeaf;
    protected Image imgflowRiver;
    protected byte size;
    private int bx;
    private int dbx;
    private int fx;
    private int dfx;
    protected String[] instruction;
    protected int[] iX;
    protected int[] iY;
    protected int[] iW;
    protected int iCount;
    protected String mapName;
    protected byte versionMap;
    protected byte zoneID;
    protected byte bgID;
    protected byte typeMap;
    protected short mapID;
    protected short oldMapID;
    protected int cmtoYmini;
    protected int cmyMini;
    protected int cmdyMini;
    protected int cmvyMini;
    protected int cmtoXMini;
    protected int cmxMini;
    protected int cmdxMini;
    protected int cmvxMini;
    protected int wMiniMap;
    protected int hMiniMap;
    protected int posMiniMapX;
    protected int posMiniMapY;
    protected long timeTranMini;
    protected ArrayList vGo;
    protected static final byte MAP_NORMAL = 0;
    protected static final byte MAP_DAUTRUONG = 1;
    protected static final byte MAP_PB = 2;
    protected static final byte MAP_CHIENTRUONG = 3;
    protected HashMap locationStand;
    private int defaultSolidTile;
    protected int totalTileLoad;
    protected ArrayList totalWater;
    protected int[] totalTile;
    protected int sizeMiniMap;
    protected int gssx;
    protected int gssxe;
    protected int gssy;
    protected int gssye;
    protected int countx;
    protected int county;
    private int[] colorMini;
    protected Image[] imgTileMap;
    protected Color[][][] colorMiniMap;
    protected Color[] blackAr;
    protected int miniSize;
    protected int saveTileId;
    protected boolean isStopping;
    protected float volume;
    
    protected TileMap(final Map map, final byte zoneID) {
        this.map = null;
        this.aVgo = new ArrayList<>();
        this.aItemTreeFront = new ArrayList<>();
        this.aItemTreeBehind = new ArrayList<>();
        this.aItemTreeBetwen = new ArrayList<>();
        this.aCharInMap = new ArrayList<>();
        this.aMob = new ArrayList<>();
        this.aBuNhin = new ArrayList<>();
        this.aNpc = new ArrayList<>();
        this.aItemMap = new ArrayList<>();
        this.aParty = new ArrayList<>();
        this.numPlayer = 0;
        this.numParty = 0;
        this.maxMobId = 0;
        this.lock = new Lock();
        this.itemMapID = 0;
        this.size = 24;
        this.mapName = "";
        this.versionMap = 1;
        this.vGo = new ArrayList<>();
        this.locationStand = new HashMap<>();
        this.totalTileLoad = 0;
        this.totalWater = new ArrayList<>();
        this.totalTile = new int[] { 120, 141, 143, 103 };
        this.sizeMiniMap = 2;
        this.colorMini = new int[] { 5257738, 8807192 };
        this.imgTileMap = new Image[150];
        this.colorMiniMap = new Color[4][][];
        this.miniSize = 2;
        this.saveTileId = -1;
        this.map = map;
        this.mapID = map.template.mapID;
        this.tileID = map.template.tileID;
        this.bgID = map.template.bgID;
        this.typeMap = map.template.typeMap;
        this.mapName = map.template.mapName;
        this.zoneID = zoneID;
        for (byte i = 0; i < map.template.npcID.length; ++i) {
            this.aNpc.add(new Npc(i, map.template.npcX[i], map.template.npcY[i], map.template.npcID[i], map.template.npcType[i]));
        }
        this.loadMapFromResource();
        this.loadMap(this.tileID);
        this.initMob();
        this.lock.setUp();
    }
    
    protected static Npc NPCNear(final Char _char, final short templateId) {
        final TileMap tileMap = _char.tileMap;
        for (byte i = 0; i < tileMap.aNpc.size(); ++i) {
            final Npc npc = tileMap.aNpc.get(i);
            if (npc != null && npc.template.npcTemplateId == templateId && Math.abs(_char.cx - npc.cx) <= 60 && Math.abs(_char.cy - npc.cy) <= 60) {
                return npc;
            }
        }
        return null;
    }
    
    protected short touchY(final short x, short y) {
        while (y < this.pxh) {
            if (this.tileTypeAt(x, y, 2)) {
                return y;
            }
            ++y;
        }
        return (short)this.pxh;
    }
    
    protected Mob getMob(final int mobId) {
        try {
            for (short i = 0; i < this.aMob.size(); ++i) {
                final Mob mob = this.aMob.get(i);
                if (mob != null && mob.mobId == mobId) {
                    return mob;
                }
            }
        }
        catch (Exception ex) {}
        return null;
    }
    
    protected Party getParty(final int partyId) {
        try {
            for (short i = 0; i < this.numParty; ++i) {
                final Party party = this.aParty.get(i);
                if (party != null && party.partyId == partyId) {
                    return party;
                }
            }
        }
        catch (Exception ex) {}
        return null;
    }
    
    protected void addParty(final Party party) {
        try {
            this.lock.lock("Them nhom");
            try {
                if (!this.aParty.contains(party)) {
                    ++this.numParty;
                    this.aParty.add(party);
                }
            }
            finally {
                this.lock.unlock();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    protected void removeParty(final int partyId) {
        try {
            this.lock.lock("Xoa nhom");
            try {
                for (short i = 0; i < this.numParty; ++i) {
                    final Party party = this.aParty.get(i);
                    if (party != null && party.partyId == partyId) {
                        --this.numParty;
                        this.aParty.remove(i);
                        return;
                    }
                }
            }
            finally {
                this.lock.unlock();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    protected int getNumPlayerParty(final int partyId) {
        int num = 0;
        try {
            this.lock.lock("dem tv nhom");
            try {
                for (short i = 0; i < this.numPlayer; ++i) {
                    final Char player = this.aCharInMap.get(i);
                    if (player != null && player.party != null && player.party.partyId == partyId) {
                        ++num;
                    }
                }
            }
            finally {
                this.lock.unlock();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return num;
    }
    
    protected void NpcPlayerUpdate(final byte index, final byte statusMe) {
        final Npc npc = this.aNpc.get(index);
        if (npc != null) {
            npc.type = statusMe;
            try {
                for (short i = 0; i < this.numPlayer; ++i) {
                    final Char player = this.aCharInMap.get(i);
                    if (player != null && player.user != null && player.user.session != null) {
                        Service.npcPlayerUpdate(player, index, statusMe);
                    }
                }
            }
            catch (Exception ex) {}
        }
    }
    
    protected byte getlevelBoss(final byte levelBoss) {
        byte num = 0;
        try {
            for (short i = 0; i < this.aMob.size(); ++i) {
                final Mob mob = this.aMob.get(i);
                if (mob != null && !mob.injureThenDie && mob.status != 0 && mob.status != 1 && mob.levelBoss == levelBoss) {
                    ++num;
                }
            }
        }
        catch (Exception ex) {}
        return num;
    }
    
    protected short getIdMobAdd() {
        short mobId = -1;
        for (short index = this.maxMobId; index < Mob.arrMobTemplate.length; ++index) {
            boolean isTimra = true;
            for (short i = this.maxMobId; i < this.aMob.size(); ++i) {
                final Mob mob = this.aMob.get(i);
                if (mob != null && mob.mobId == index) {
                    isTimra = false;
                    break;
                }
            }
            if (isTimra) {
                mobId = index;
                break;
            }
        }
        return mobId;
    }
    
    protected void Join(final Char _char) {
        try {
            this.lock.lock("Vao khu");
            try {
                this.JOIN(_char);
            }
            finally {
                this.lock.unlock();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    protected void JOIN(final Char _char) {
        this.addChar(_char);
        if (_char.isHuman && _char.Nhanban != null && _char.isBatNhanban) {
            _char.Nhanban.cx = _char.cx;
            _char.Nhanban.cy = _char.cy;
            this.addChar(_char.Nhanban);
        }
    }
    
    protected void addChar(final Char _char) {
        if (!this.aCharInMap.contains(_char)) {
            _char.mapId = this.mapID;
            _char.tileMap = this;
            if (!_char.isHuman && !_char.isNhanban) {
                _char.user.player.tileMap = _char.tileMap;
                _char.user.player.mapId = _char.mapId;
            }
            if (_char.user != null && _char.user.session != null) {
                Service.mapItem(_char, this);
                Service.MapInfo(this, _char);
            }
            if (!_char.isNhanban && (_char.cTypePk == 4 || _char.cTypePk == 5 || this.map.isPKMap())) {
                if ((_char.testDunPhe == 0 || (this.map.isChienTruong() && this.map.chientruong.aCharWhite.contains(_char.user.player.playerId))) && this.map.isPKMap()) {
                    _char.cTypePk = 4;
                }
                else if ((_char.testDunPhe == 1 || (this.map.isChienTruong() && this.map.chientruong.aCharBlack.contains(_char.user.player.playerId))) && this.map.isPKMap()) {
                    _char.cTypePk = 5;
                } else if (this.map.isWarClanMap()) {
                    _char.cTypePk = _char.clan.typeWar;
                }
                else {
                    _char.cTypePk = 0;
                }
                Service.updateTypePk(_char, _char.charID, _char.cTypePk);
            }
            for (int i = 0; i < this.numPlayer; ++i) {
                if (this.aCharInMap.get(i).user != null && this.aCharInMap.get(i).user.session != null) {
                    Service.PlayerADD(this.aCharInMap.get(i), _char);
                    if (_char.coat() != -1) {
                        Service.PlayerLoadAoChoang(this.aCharInMap.get(i), _char.charID, _char.cHP, _char.cMaxHP(), _char.coat());
                    }
                    if (_char.glove() != -1) {
                        Service.PlayerLoadGiaToc(this.aCharInMap.get(i), _char.charID, _char.cHP, _char.cMaxHP(), _char.glove());
                    }
                    if (_char.mobMe != null) {
                        Service.PlayerLoadThuNuoi(this.aCharInMap.get(i), _char.charID, _char.mobMe);
                    }
                    if (_char.mobViThu != null) {
                        Service.onChangeVithu(this.aCharInMap.get(i), _char.charID, _char.mobViThu);
                    }
                    if (_char.ItemMounts[4] != null) {
                        Service.LoadThuCuoi(this.aCharInMap.get(i), _char.charID, _char.ItemMounts);
                    }
                }
            }
            if (_char.user != null && _char.user.session != null) {
                for (int i = 0; i < this.numPlayer; ++i) {
                    Service.PlayerADD(_char, this.aCharInMap.get(i));
                    if (this.aCharInMap.get(i).coat() != -1) {
                        Service.PlayerLoadAoChoang(_char, this.aCharInMap.get(i).charID, this.aCharInMap.get(i).cHP, this.aCharInMap.get(i).cMaxHP(), this.aCharInMap.get(i).coat());
                    }
                    if (this.aCharInMap.get(i).glove() != -1) {
                        Service.PlayerLoadGiaToc(_char, this.aCharInMap.get(i).charID, this.aCharInMap.get(i).cHP, this.aCharInMap.get(i).cMaxHP(), this.aCharInMap.get(i).glove());
                    }
                    if (this.aCharInMap.get(i).mobMe != null) {
                        Service.PlayerLoadThuNuoi(_char, this.aCharInMap.get(i).charID, this.aCharInMap.get(i).mobMe);
                    }
                    if (this.aCharInMap.get(i).mobViThu != null) {
                        Service.onChangeVithu(_char, this.aCharInMap.get(i).charID, this.aCharInMap.get(i).mobViThu);
                    }
                    if (this.aCharInMap.get(i).ItemMounts[4] != null) {
                        Service.LoadThuCuoi(_char, this.aCharInMap.get(i).charID, this.aCharInMap.get(i).ItemMounts);
                    }
                }
            }
            if (_char.user != null && _char.user.session != null) {
                Task.inMap(_char);
                if (this.map.isWarClanMap()) {
                    Service.mapTime(_char, (int)(this.map.warClan.timeLength - System.currentTimeMillis() / 1000L));
                    Service.pointChienTruong(_char, _char.pointWarClan);
                }
                else if (this.map.isBackCaveMap()) {
                    Service.mapTime(_char, (int)(this.map.backCave.timeLength - System.currentTimeMillis() / 1000L));
                    Service.pointPB(_char, _char.pointPB);
                }
                else if (this.map.isTestDunMap()) {
                    Service.mapTime(_char, (int)(this.map.testDun.timeLength - System.currentTimeMillis() / 1000L));
                }
                else if (this.map.isClanManor()) {
                    Service.mapTime(_char, (int)(this.map.clanManor.timeLength - System.currentTimeMillis() / 1000L));
                }
                else if (this.map.isChienTruong()) {
                    Service.mapTime(_char, (int)(this.map.chientruong.timeLength - System.currentTimeMillis() / 1000L));
                    Service.pointChienTruong(_char, _char.pointChienTruong);
                } else if (this.map.isSevenBeasts()) {
                    SevenBeasts sb = SevenBeasts.getSevenBeasts(_char);
                    Service.mapTime(_char, sb.timeLength);
                }
            }
            boolean isSet = false;
            SevenBeasts sb = SevenBeasts.getSevenBeasts(_char);
            for (int j = 0; j < this.numPlayer; ++j) {
                final Char player = this.aCharInMap.get(j);
                if (player != null && player.isNhanban == _char.isNhanban && player.cName.equals(_char.cName)) {
                    this.aCharInMap.set(j, _char);
                    isSet = true;
                    break;
                }
            }
            if (!isSet) {
                this.aCharInMap.add(_char);
                ++this.numPlayer;
            }
            if (_char.party != null && _char.party.numPlayer > 1 && !this.aParty.contains(_char.party)) {
                this.aParty.add(_char.party);
                ++this.numParty;
            }
            if (!_char.isNhanban && this.map.isBackCaveMap()) {
                this.map.backCave.addChar(_char);
            }
            else if (!_char.isNhanban && this.map.isChienTruong()) {
                this.map.chientruong.addChar(_char);
            }
            else if (!_char.isNhanban && this.map.isClanManor()) {
                this.map.clanManor.addChar(_char);
            }
            else if (!_char.isNhanban && this.map.isSevenBeasts()) {
                sb.addChar(_char);
            } else if (!_char.isNhanban && this.map.isWarClanMap()) {
                Service.updateTypePk(_char, _char.charID, _char.clan.typeWar);
                this.map.warClan.addChar(_char);
            } else if (!_char.isNhanban && this.map.template.mapID == 129) {
                this.map.njTalent.addChar(_char);
            }
        }
    }
    
    protected void Exit(final Char _char) {
        try {
            this.lock.lock("Thoat khu");
            try {
                this.EXIT(_char);
            }
            finally {
                this.lock.unlock();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    protected void EXIT(final Char _char) {
        this.removeChar(_char);
        if (_char.isHuman && _char.Nhanban != null && _char.isBatNhanban) {
            this.removeChar(_char.Nhanban);
        }
    }
    
    protected void removeChar(final Char _char) {
        if (this.aCharInMap.contains(_char)) {
            if (_char.user != null && _char.user.session != null) {
                Service.ClearMap(_char);
                Player.endTrade(_char);
                _char.endTest(0);
                _char.KillCharId = -9999;
                _char.clearCuuSat();
            }
            _char.mobFocus = null;
            for (int i = 0; i < this.numPlayer; ++i) {
                final Char player = this.aCharInMap.get(i);
                if (player != null && player.isNhanban == _char.isNhanban && player.cName.equals(_char.cName)) {
                    this.aCharInMap.remove(i);
                    --this.numPlayer;
                    break;
                }
            }
            if (!_char.isNhanban && this.map.isBackCaveMap()) {
                this.map.backCave.removeChar(_char);
            }
            else if (!_char.isNhanban && this.map.isChienTruong()) {
                this.map.chientruong.removeChar(_char);
            }
            else if (!_char.isNhanban && this.map.isClanManor()) {
                this.map.clanManor.removeChar(_char);
            }
            else if (!_char.isNhanban && this.map.isSevenBeasts()) {
                if (this.mapID == 112 && this.map.sevenBeasts.countRefresh < 5) {
                    this.map.sevenBeasts.sendTB(_char, null, null, 1);
                }
                this.map.sevenBeasts.removeChar(_char);
            }
            else if (!_char.isNhanban && this.map.template.mapID == 129) {
                this.map.njTalent.removeChar(_char);
            }
            else if (!_char.isNhanban && _char.testDunPhe > -1 && _char.tileMap.map.isTestDunMap() && _char.tileMap.map.testDun.isFinght) {
                short blacks = 0;
                short whites = 0;
                for (short j = 0; j < _char.tileMap.numPlayer; ++j) {
                    final Char player2 = _char.tileMap.aCharInMap.get(j);
                    if (player2 != null) {
                        if (player2.testDunPhe == 0 && player2.statusMe != 14) {
                            ++whites;
                        }
                        if (player2.testDunPhe == 1 && player2.statusMe != 14) {
                            ++blacks;
                        }
                    }
                }
                if (whites == 0 && blacks == 0) {
                    _char.tileMap.map.testDun.complete((byte)(-1));
                }
                else if (whites > 0 && blacks == 0) {
                    _char.tileMap.map.testDun.complete((byte)0);
                }
                else if (whites == 0 && blacks > 0) {
                    _char.tileMap.map.testDun.complete((byte)1);
                }
            }
            if (_char.party != null) {
                int num = 0;
                for (short k = 0; k < this.numPlayer; ++k) {
                    final Char player3 = this.aCharInMap.get(k);
                    if (player3 != null && player3.party != null && player3.party.partyId == _char.party.partyId) {
                        ++num;
                    }
                }
                if (num <= 0 && this.aParty.contains(_char.party)) {
                    this.aParty.remove(_char.party);
                    --this.numParty;
                }
            }
            for (int i = 0; i < this.numPlayer; ++i) {
                if (this.aCharInMap.get(i).user != null && this.aCharInMap.get(i).user.session != null) {
                    Service.PlayerRemove(this.aCharInMap.get(i), _char.charID);
                    System.out.println("remove " + i + " " + _char.charID);
                }
            }
        }
        if (_char.party != null && _char.party.numPlayer <= 1) {
            _char.party.clear();
        }
        synchronized (_char.aBurn) {
            _char.aBurn.clear();
        }
    }
    
    protected void ItemMapADD(final ItemMap itemMap) {
        if (this.aItemMap.size() > 126) {
            final ItemMap move = this.aItemMap.remove(0);
            try {
                for (int i = 0; i < this.numPlayer; ++i) {
                    if (this.aCharInMap.get(i).user != null && this.aCharInMap.get(i).user.session != null) {
                        Service.removeItemMap(this.aCharInMap.get(i), move.itemMapID);
                    }
                }
            }
            catch (Exception ex) {}
        }
        itemMap.y = this.touchY(itemMap.x, itemMap.y);
        final short itemMapID = this.itemMapID;
        this.itemMapID = (short)(itemMapID + 1);
        itemMap.itemMapID = itemMapID;
        this.aItemMap.add(itemMap);
    }
    
    protected void PickItemMap(final Char _char, final short itemMapID) {
        try {
            this.lock.lock("Nhat item");
            try {
                for (byte i = 0; i < this.aItemMap.size(); ++i) {
                    final ItemMap iMap = this.aItemMap.get(i);
                    if (iMap != null && itemMapID == iMap.itemMapID) {
                        if (iMap.playerId != 0 && iMap.playerId != _char.user.player.playerId) {
                            Service.ServerMessage(_char, Text.get(0, 25));
                        }
                        else if (iMap.item.template.type != 19 && _char.user.player.ItemBagSlotNull() == 0) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                        }
                        else {
                            this.aItemMap.remove(i);
                            final Item item = iMap.item;
                            if (item != null) {
                                if (_char.user != null && _char.user.session != null) {
                                    Service.MyPickItemMap(_char, iMap);
                                }
                                if (item.template.type == 19) {
                                    _char.user.player.upCoinLock(item.quantity, (byte)2);
                                }
                                else if (item.template.type == 21) {
                                    if (_char.statusMe != 14) {
                                        _char.upHP(item.quantity);
                                        Service.MELoadHP(_char);
                                        try {
                                            for (short j = 0; j < this.aCharInMap.size(); ++j) {
                                                if (this.aCharInMap.get(j).user != null && this.aCharInMap.get(j).user.session != null && this.aCharInMap.get(j).charID != _char.charID) {
                                                    Service.PlayerLoadHP(this.aCharInMap.get(j), _char.charID, _char.cHP);
                                                }
                                            }
                                        }
                                        catch (Exception e2) {}
                                    }
                                }
                                else if (item.template.isUpToUp) {
                                    _char.user.player.ItemBagAddQuantity(item);
                                }
                                else {
                                    _char.user.player.ItemBagAdd(item);
                                }
                                if (Task.itemPick(_char, item.template.id)) {
                                    _char.uptaskMaint();
                                    if (_char.party != null) {
                                        for (short k = 0; k < this.numPlayer; ++k) {
                                            final Char player = this.aCharInMap.get(k);
                                            if (player != null && player.user != null && player.party != null && player.charID != _char.charID && player.party.partyId == _char.party.partyId && player.ctaskId == _char.ctaskId && player.ctaskIndex == _char.ctaskIndex && (player.user.player.ItemBagIndexNull() != -1 || player.user.player.ItemBagIndex(item.template.id, item.isLock) != -1)) {
                                                player.uptaskMaint();
                                                if (item.template.isUpToUp) {
                                                    player.user.player.ItemBagAddQuantity(item.clone());
                                                }
                                                else {
                                                    player.user.player.ItemBagAdd(item.clone());
                                                }
                                            }
                                        }
                                    }
                                }
                                try {
                                    for (short j = 0; j < this.aCharInMap.size(); ++j) {
                                        if (this.aCharInMap.get(j).user != null && this.aCharInMap.get(j).user.session != null && this.aCharInMap.get(j).charID != _char.charID) {
                                            Service.PlayerPickItemMap(this.aCharInMap.get(j), iMap.itemMapID, _char.charID);
                                        }
                                    }
                                }
                                catch (Exception ex) {}
                            }
                        }
                        return;
                    }
                }
                Service.removeItemMap(_char, itemMapID);
            }
            finally {
                this.lock.unlock();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    protected void initWaypoint(final int type) {
        final MapTemplate template = this.map.template;
        if (type == 0 || type == 1 || type == 2 || type == 3 || type == 4 || type == 5 || type == 6 || type == 7 || type == 8) {
            for (byte i = 0; i < template.WmapID.length; ++i) {
                Map vmap = null;
                switch (type) {
                    case 0: {
                        vmap = MapServer.getMapServer(template.WmapID[i]);
                        break;
                    }
                    case 1: {
                        vmap = WarClan.getMap(this.map.warClan, template.WmapID[i]);
                        break;
                    }
                    case 2: {
                        vmap = BackCave.getMap(this.map.backCave, template.WmapID[i]);
                        break;
                    }
                    case 3: {
                        vmap = TestDun.getMap(this.map.testDun, template.WmapID[i]);
                        break;
                    }
                    case 4: {
                        vmap = ChienTruong.getMap(this.map.chientruong, template.WmapID[i]);
                        break;
                    }
                    case 5: {
                        vmap = NJTalent.getMap(this.map.njTalent, template.WmapID[i]);
                        break;
                    }
                    case 7: {
                        vmap = ClanManor.getMap(this.map.clanManor, template.WmapID[i]);
                        break;
                    }
                    case 8: {
                        vmap = SevenBeasts.getMap(this.map.sevenBeasts, template.WmapID[i]);
                        break;
                    }
                }
                if (vmap != null) {
                    final Waypoint vgo = new Waypoint(template.WminX[i], template.WminY[i], template.WmaxX[i], template.WmaxY[i], template.WgoX[i], template.WgoY[i], vmap);
                    this.aVgo.add(vgo);
                }
            }
        }
    }
    
    private void initMob() {
        this.aMob.clear();
        final MapTemplate maptemplate = GameScr.mapTemplates[this.mapID];
        for (byte i = 0; i < maptemplate.mobID.length; ++i) {
            final int id = maptemplate.mobID[i];
            final byte sys = (byte)Util.nextInt(1, 3);
            final int hp = Mob.arrMobTemplate[id].hp;
            final int level = maptemplate.mobLevel[i];
            final short x = maptemplate.mobX[i];
            final short y = maptemplate.mobY[i];
            final byte status = maptemplate.mobStatus[i];
            final byte levelBoss = maptemplate.moblevelBoss[i];
            final boolean isBoss = Mob.arrMobTemplate[id].isBoss;
            final Mob mob = new Mob(this, i, id, sys, hp, level, x, y, status, levelBoss, isBoss, maptemplate.mobRefreshTime[i]);
            this.aMob.add(mob);
        }
        this.maxMobId = (short)this.aMob.size();
    }

    protected boolean tileTypeAt(final int px, final int py, final int t) {
        boolean result;
        try {
            result = ((this.types[py / 24 * this.tmw + px / 24] & t) == t);
        }
        catch (Exception ex) {
            result = false;
        }
        return result;
    }
    private void loadMap(final int tileId) {
        this.totalTileLoad = 0;
        this.pxh = this.tmh * this.size;
        this.pxw = this.tmw * this.size;
        try {
            for (int i = 0; i < this.tmw * this.tmh; ++i) {
                if (this.locationStand != null && this.locationStand.get(String.valueOf(i)) != null) {
                    final int[] types = this.types;
                    final int n = i;
                    types[n] |= 0x2;
                }
                if (tileId == 4) {
                    if (this.maps[i] == '\u0001' || this.maps[i] == '\u0002' || this.maps[i] == '\u0003' || this.maps[i] == '\u0004' || this.maps[i] == '\u0005' || this.maps[i] == '\u0006' || this.maps[i] == '\t' || this.maps[i] == '\n' || this.maps[i] == 'O' || this.maps[i] == 'P' || this.maps[i] == '\r' || this.maps[i] == '\u000e' || this.maps[i] == '+' || this.maps[i] == ',' || this.maps[i] == '-' || this.maps[i] == '2') {
                        final int[] types2 = this.types;
                        final int n2 = i;
                        types2[n2] |= 0x2;
                    }
                    if (this.maps[i] == '\t' || this.maps[i] == '\u000b') {
                        final int[] types3 = this.types;
                        final int n3 = i;
                        types3[n3] |= 0x4;
                    }
                    if (this.maps[i] == '\n' || this.maps[i] == '\f') {
                        final int[] types4 = this.types;
                        final int n4 = i;
                        types4[n4] |= 0x8;
                    }
                    if (this.maps[i] == '\r' || this.maps[i] == '\u000e') {
                        final int[] types5 = this.types;
                        final int n5 = i;
                        types5[n5] |= 0x400;
                    }
                    if (this.maps[i] == 'L' || this.maps[i] == 'M') {
                        final int[] types6 = this.types;
                        final int n6 = i;
                        types6[n6] |= 0x40;
                        if (this.maps[i] == 'N') {
                            final int[] types7 = this.types;
                            final int n7 = i;
                            types7[n7] |= 0x1000;
                        }
                    }
                }
                if (tileId == 1) {
                    if (this.maps[i] == '\u0016') {
                        this.defaultSolidTile = this.maps[i] - '\u0001';
                    }
                    if (this.maps[i] == '\u0001' || this.maps[i] == '\u0002' || this.maps[i] == '\u0003' || this.maps[i] == '\u0004' || this.maps[i] == '\u0005' || this.maps[i] == '\u0006' || this.maps[i] == '\u0007' || this.maps[i] == '$' || this.maps[i] == '%' || this.maps[i] == '6' || this.maps[i] == '[' || this.maps[i] == '\\' || this.maps[i] == ']' || this.maps[i] == '^' || this.maps[i] == 'I' || this.maps[i] == 'J' || this.maps[i] == 'a' || this.maps[i] == 'b' || this.maps[i] == 't' || this.maps[i] == 'u' || this.maps[i] == 'v' || this.maps[i] == 'x' || this.maps[i] == '=') {
                        final int[] types8 = this.types;
                        final int n8 = i;
                        types8[n8] |= 0x2;
                    }
                    if (this.maps[i] == '\u0002' || this.maps[i] == '\u0003' || this.maps[i] == '\u0004' || this.maps[i] == '\u0005' || this.maps[i] == '\u0006' || this.maps[i] == '\u0014' || this.maps[i] == '\u0015' || this.maps[i] == '\u0016' || this.maps[i] == '\u0017' || this.maps[i] == '$' || this.maps[i] == '%' || this.maps[i] == '&' || this.maps[i] == '\'' || this.maps[i] == '=') {
                        final int[] types9 = this.types;
                        final int n9 = i;
                        types9[n9] |= 0x1000;
                    }
                    if (this.maps[i] == '\b' || this.maps[i] == '\t' || this.maps[i] == '\n' || this.maps[i] == '\f' || this.maps[i] == '\r' || this.maps[i] == '\u000e' || this.maps[i] == '\u001e') {
                        final int[] types10 = this.types;
                        final int n10 = i;
                        types10[n10] |= 0x10;
                    }
                    if (this.maps[i] == '\u0011') {
                        final int[] types11 = this.types;
                        final int n11 = i;
                        types11[n11] |= 0x20;
                    }
                    if (this.maps[i] == '\u0012') {
                        final int[] types12 = this.types;
                        final int n12 = i;
                        types12[n12] |= 0x80;
                    }
                    if (this.maps[i] == '%' || this.maps[i] == '&' || this.maps[i] == '=') {
                        final int[] types13 = this.types;
                        final int n13 = i;
                        types13[n13] |= 0x4;
                    }
                    if (this.maps[i] == '$' || this.maps[i] == '\'' || this.maps[i] == '=') {
                        final int[] types14 = this.types;
                        final int n14 = i;
                        types14[n14] |= 0x8;
                    }
                    if (this.maps[i] == '\u0013') {
                        final int[] types15 = this.types;
                        final int n15 = i;
                        types15[n15] |= 0x40;
                        if ((this.types[i - this.tmw] & 0x1000) == 0x1000) {
                            final int[] types16 = this.types;
                            final int n16 = i;
                            types16[n16] |= 0x1000;
                        }
                    }
                    if (this.maps[i] == '#') {
                        final int[] types17 = this.types;
                        final int n17 = i;
                        types17[n17] |= 0x800;
                    }
                    if (this.maps[i] == '\u0007') {
                        final int[] types18 = this.types;
                        final int n18 = i;
                        types18[n18] |= 0x400;
                    }
                    if (this.maps[i] == ' ' || this.maps[i] == '!' || this.maps[i] == '\"') {
                        final int[] types19 = this.types;
                        final int n19 = i;
                        types19[n19] |= 0x100;
                    }
                }
                if (tileId == 2) {
                    if (this.maps[i] == '\u0016' || this.maps[i] == 'g' || this.maps[i] == 'o') {
                        this.defaultSolidTile = this.maps[i] - '\u0001';
                    }
                    if (this.maps[i] == '\u0001' || this.maps[i] == '\u0002' || this.maps[i] == '\u0003' || this.maps[i] == '\u0004' || this.maps[i] == '\u0005' || this.maps[i] == '\u0006' || this.maps[i] == '\u0007' || this.maps[i] == '$' || this.maps[i] == '%' || this.maps[i] == '6' || this.maps[i] == '=' || this.maps[i] == 'I' || this.maps[i] == 'L' || this.maps[i] == 'M' || this.maps[i] == 'N' || this.maps[i] == 'O' || this.maps[i] == 'R' || this.maps[i] == 'S' || this.maps[i] == 'b' || this.maps[i] == 'c' || this.maps[i] == 'd' || this.maps[i] == 'f' || this.maps[i] == 'g' || this.maps[i] == 'l' || this.maps[i] == 'm' || this.maps[i] == 'n' || this.maps[i] == 'p' || this.maps[i] == 'q' || this.maps[i] == 't' || this.maps[i] == 'u' || this.maps[i] == '}' || this.maps[i] == '~' || this.maps[i] == '\u007f' || this.maps[i] == '\u0081' || this.maps[i] == '\u0082') {
                        final int[] types20 = this.types;
                        final int n20 = i;
                        types20[n20] |= 0x2;
                    }
                    if (this.maps[i] == '\u0001' || this.maps[i] == '\u0003' || this.maps[i] == '\u0004' || this.maps[i] == '\u0005' || this.maps[i] == '\u0006' || this.maps[i] == '\u0014' || this.maps[i] == '\u0015' || this.maps[i] == '\u0016' || this.maps[i] == '\u0017' || this.maps[i] == '$' || this.maps[i] == '%' || this.maps[i] == '&' || this.maps[i] == '\'' || this.maps[i] == '7' || this.maps[i] == 'm' || this.maps[i] == 'o' || this.maps[i] == 'p' || this.maps[i] == 'q' || this.maps[i] == 'r' || this.maps[i] == 's' || this.maps[i] == 't' || this.maps[i] == '\u007f' || this.maps[i] == '\u0081' || this.maps[i] == '\u0082') {
                        final int[] types21 = this.types;
                        final int n21 = i;
                        types21[n21] |= 0x1000;
                    }
                    if (this.maps[i] == '\b' || this.maps[i] == '\t' || this.maps[i] == '\n' || this.maps[i] == '\f' || this.maps[i] == '\r' || this.maps[i] == '\u000e' || this.maps[i] == '\u001e' || this.maps[i] == '\u0087') {
                        final int[] types22 = this.types;
                        final int n22 = i;
                        types22[n22] |= 0x10;
                    }
                    if (this.maps[i] == '\u0011') {
                        final int[] types23 = this.types;
                        final int n23 = i;
                        types23[n23] |= 0x20;
                    }
                    if (this.maps[i] == '\u0012') {
                        final int[] types24 = this.types;
                        final int n24 = i;
                        types24[n24] |= 0x80;
                    }
                    if (this.maps[i] == '=' || this.maps[i] == '%' || this.maps[i] == '&' || this.maps[i] == '\u007f' || this.maps[i] == '\u0082' || this.maps[i] == '\u0083') {
                        final int[] types25 = this.types;
                        final int n25 = i;
                        types25[n25] |= 0x4;
                    }
                    if (this.maps[i] == '=' || this.maps[i] == '$' || this.maps[i] == '\'' || this.maps[i] == '\u007f' || this.maps[i] == '\u0081' || this.maps[i] == '\u0084') {
                        final int[] types26 = this.types;
                        final int n26 = i;
                        types26[n26] |= 0x8;
                    }
                    if (this.maps[i] == '\u0013') {
                        final int[] types27 = this.types;
                        final int n27 = i;
                        types27[n27] |= 0x40;
                        if ((this.types[i - this.tmw] & 0x1000) == 0x1000) {
                            final int[] types28 = this.types;
                            final int n28 = i;
                            types28[n28] |= 0x1000;
                        }
                    }
                    if (this.maps[i] == '\u0086') {
                        final int[] types29 = this.types;
                        final int n29 = i;
                        types29[n29] |= 0x40;
                        if ((this.types[i - this.tmw] & 0x1000) == 0x1000) {
                            final int[] types30 = this.types;
                            final int n30 = i;
                            types30[n30] |= 0x1000;
                        }
                    }
                    if (this.maps[i] == '#') {
                        final int[] types31 = this.types;
                        final int n31 = i;
                        types31[n31] |= 0x800;
                    }
                    if (this.maps[i] == '\u0007') {
                        final int[] types32 = this.types;
                        final int n32 = i;
                        types32[n32] |= 0x400;
                    }
                    if (this.maps[i] == ' ' || this.maps[i] == '!' || this.maps[i] == '\"') {
                        final int[] types33 = this.types;
                        final int n33 = i;
                        types33[n33] |= 0x100;
                    }
                    if (this.maps[i] == '=' || this.maps[i] == '\u007f') {
                        final int[] types34 = this.types;
                        final int n34 = i;
                        types34[n34] |= 0x2000;
                    }
                }
                if (tileId == 3) {
                    if (this.maps[i] == '\f' || this.maps[i] == '3' || this.maps[i] == 'X' || this.maps[i] == 't' || this.maps[i] == '\u0080') {
                        this.defaultSolidTile = this.maps[i] - '\u0001';
                    }
                    if (this.maps[i] == 'm' || this.maps[i] == 'n') {
                        this.defaultSolidTile = this.maps[i];
                    }
                    if (this.maps[i] == '\u0001' || this.maps[i] == '\u0002' || this.maps[i] == '\u0003' || this.maps[i] == '\u0004' || this.maps[i] == '\u0005' || this.maps[i] == '\u0006' || this.maps[i] == '\u0007' || this.maps[i] == '\u000b' || this.maps[i] == '\u000e' || this.maps[i] == '\u0011' || this.maps[i] == '+' || this.maps[i] == '3' || this.maps[i] == '?' || this.maps[i] == 'A' || this.maps[i] == 'C' || this.maps[i] == 'D' || this.maps[i] == 'G' || this.maps[i] == 'H' || this.maps[i] == 'S' || this.maps[i] == 'T' || this.maps[i] == 'U' || this.maps[i] == 'W' || this.maps[i] == '[' || this.maps[i] == '^' || this.maps[i] == 'a' || this.maps[i] == 'b' || this.maps[i] == 'j' || this.maps[i] == 'k' || this.maps[i] == 'o' || this.maps[i] == 'q' || this.maps[i] == 'u' || this.maps[i] == 'v' || this.maps[i] == 'w' || this.maps[i] == '}' || this.maps[i] == '~' || this.maps[i] == '\u0081' || this.maps[i] == '\u0082' || this.maps[i] == '\u0083' || this.maps[i] == '\u0085' || this.maps[i] == '\u0088' || this.maps[i] == '\u008a' || this.maps[i] == '\u008b' || this.maps[i] == '\u008e') {
                        final int[] types35 = this.types;
                        final int n35 = i;
                        types35[n35] |= 0x2;
                    }
                    if (this.maps[i] == '|' || this.maps[i] == 't' || this.maps[i] == '{' || this.maps[i] == ',' || this.maps[i] == '\f' || this.maps[i] == '\u000f' || this.maps[i] == '\u0010' || this.maps[i] == '-' || this.maps[i] == '\n' || this.maps[i] == '\t') {
                        final int[] types36 = this.types;
                        final int n36 = i;
                        types36[n36] |= 0x1000;
                    }
                    if (this.maps[i] == '\u0017') {
                        final int[] types37 = this.types;
                        final int n37 = i;
                        types37[n37] |= 0x20;
                    }
                    if (this.maps[i] == '\u0018') {
                        final int[] types38 = this.types;
                        final int n38 = i;
                        types38[n38] |= 0x80;
                    }
                    if (this.maps[i] == '\u0006' || this.maps[i] == '\u000f' || this.maps[i] == '3' || this.maps[i] == '_' || this.maps[i] == 'a' || this.maps[i] == 'j' || this.maps[i] == 'o' || this.maps[i] == '{' || this.maps[i] == '}' || this.maps[i] == '\u008a' || this.maps[i] == '\u008c') {
                        final int[] types39 = this.types;
                        final int n39 = i;
                        types39[n39] |= 0x4;
                    }
                    if (this.maps[i] == '\u0007' || this.maps[i] == '\u0010' || this.maps[i] == '3' || this.maps[i] == '`' || this.maps[i] == 'b' || this.maps[i] == 'k' || this.maps[i] == 'o' || this.maps[i] == '|' || this.maps[i] == '~' || this.maps[i] == '\u008b' || this.maps[i] == '\u008d') {
                        final int[] types40 = this.types;
                        final int n40 = i;
                        types40[n40] |= 0x8;
                    }
                    if (this.maps[i] == '\u0019') {
                        final int[] types41 = this.types;
                        final int n41 = i;
                        types41[n41] |= 0x40;
                        if ((this.types[i - this.tmw] & 0x1000) == 0x1000) {
                            final int[] types42 = this.types;
                            final int n42 = i;
                            types42[n42] |= 0x1000;
                        }
                    }
                    if (this.maps[i] == '\"') {
                        final int[] types43 = this.types;
                        final int n43 = i;
                        types43[n43] |= 0x800;
                    }
                    if (this.maps[i] == '\u0011') {
                        final int[] types44 = this.types;
                        final int n44 = i;
                        types44[n44] |= 0x400;
                    }
                    if (this.maps[i] == '!' || this.maps[i] == 'g' || this.maps[i] == 'h' || this.maps[i] == 'i' || this.maps[i] == '\u001a' || this.maps[i] == '!') {
                        final int[] types45 = this.types;
                        final int n45 = i;
                        types45[n45] |= 0x100;
                    }
                    if (this.maps[i] == '3' || this.maps[i] == 'o' || this.maps[i] == 'D') {
                        final int[] types46 = this.types;
                        final int n46 = i;
                        types46[n46] |= 0x2000;
                    }
                    if (this.maps[i] == 'R' || this.maps[i] == 'n' || this.maps[i] == '\u008f') {
                        final int[] types47 = this.types;
                        final int n47 = i;
                        types47[n47] |= 0x4000;
                    }
                    if (this.maps[i] == 'q') {
                        final int[] types48 = this.types;
                        final int n48 = i;
                        types48[n48] |= 0x10000;
                    }
                    if (this.maps[i] == '\u008e') {
                        final int[] types49 = this.types;
                        final int n49 = i;
                        types49[n49] |= 0x8000;
                    }
                    if (this.maps[i] == '(' || this.maps[i] == ')') {
                        final int[] types50 = this.types;
                        final int n50 = i;
                        types50[n50] |= 0x20000;
                    }
                    if (this.maps[i] == 'n') {
                        final int[] types51 = this.types;
                        final int n51 = i;
                        types51[n51] |= 0x40000;
                    }
                    if (this.maps[i] == '\u008f') {
                        final int[] types52 = this.types;
                        final int n52 = i;
                        types52[n52] |= 0x80000;
                    }
                }
            }
        }
        catch (Exception var4) {
            var4.printStackTrace();
        }
    }
    
    private void loadMapFromResource() {
        final DataInputStream resourceAsStream = new DataInputStream(new ByteArrayInputStream(NinjaUtil.getFile("cache/map/" + this.mapID)));
        try {
            this.tmw = Util.ushort((short)resourceAsStream.read());
            this.tmh = Util.ushort((short)resourceAsStream.read());
            this.maps = new char[resourceAsStream.available()];
            for (int i = 0; i < this.tmw * this.tmh; ++i) {
                this.maps[i] = (char)resourceAsStream.read();
            }
            this.types = new int[this.maps.length];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected class PosWater
    {
        protected int x;
        protected int y;
        
        protected PosWater() {
            this.x = -1;
            this.y = -1;
        }
    }
    protected void setItemTask() {
        if (map.template.mapID == 29) {
            Item item = new Item(null, (short) 212, 1, -1, true, (byte) 0, 0);
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 660, (short) 480, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 480, (short) 456, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 516, (short) 360, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 997, (short) 288, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 234, (short) 336, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 325, (short) 96, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 1141, (short) 360, true));
        } else if (map.template.mapID == 64) {
            Item item = new Item(null, (short) 236, 1, -1, true, (byte) 0, 0);
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 134, (short) 720, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 119, (short) 624, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 299, (short) 456, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 499, (short) 408, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 495, (short) 120, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 380, (short) 168, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 125, (short) 216, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 127, (short) 432, true));
        } else if (map.template.mapID == 42) {
            Item item = new Item(null, (short) 347, 1, -1, true, (byte) 0, 0);
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 744, (short) 480, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 744, (short) 408, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 694, (short) 336, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 533, (short) 240, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 482, (short) 192, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 656, (short) 912, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 685, (short) 720, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 550, (short) 600, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 625, (short) 600, true));
            ItemMapADD(new ItemMap(item, 0, 65000, (short) 700, (short) 600, true));
        }
    }
    protected void updateTask(long time) {
        if (this.mapID == 74 || this.mapID == 78) {
            if (aCharInMap.size() == 1) {
                val _char = aCharInMap.get(0);
                if (_char == null) {
                    return;
                }
                if (_char.timeTask == -1 || time > _char.timeTask) {
                    for (int i = 0; i < aCharInMap.size(); i++) {
                        getMapLTD(_char);
                    }
                }
                if (this.aItemMap.size() > 0 && _char.timeTask != (10000L + System.currentTimeMillis())) {
                    _char.timeTask = 10000L + System.currentTimeMillis();
                    Service.mapTime(_char, 10);
                }
            } else if (aCharInMap.size() > 1) {
                for (Char c : aCharInMap) {
                    getMapLTD(c);
                }
            }
        }
    }
    protected static void getMapLTD(final Char _char) {
        if (_char == null) {
            return;
        }
        final Map map = MapServer.getMapServer(_char.mapLTDId);
        _char.timeTask = -1;
        if (map != null) {
            final TileMap tile = map.getSlotZone(_char);
            if (tile == null) {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
            } else {
                _char.tileMap.Exit(_char);
                _char.cx = tile.map.template.goX;
                _char.cy = tile.map.template.goY;
                tile.Join(_char);
            }
        }
    }
    protected boolean findCharInMap(int id) {
        boolean have = false;
        for (int i = 0; i < aCharInMap.size(); i++) {
            if (id == aCharInMap.get(i).charID) {
                have = true;
                break;
            }
        }
        return have;
    }
    public void sendMessage(Message m) {
        try {
            for (int i = this.aCharInMap.size() - 1; i >= 0; --i) {
                if (this.aCharInMap.get(i) != null && (this.aCharInMap.get(i)).user.session != null) {
                    if (this.aCharInMap.get(i).isBot) {
                        continue;
                    }
                    (this.aCharInMap.get(i)).user.session.sendMessage(m);
                }
            }
            m.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }
    public void move(int id, short x, short y) {
        Message m = null;
        try {
            m = new Message(1);
            m.writer().writeInt(id);
            m.writer().writeShort(x);
            m.writer().writeShort(y);
            m.writer().flush();
            this.sendMessage(m);
        } catch (IOException var5) {
            var5.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }
    public boolean haveBotInMap(Char _char) {
        boolean have = false;
        for (int i = 0; i < Robot.gI().arrBot.size(); i++) {
            Robot.Bot c = Robot.gI().arrBot.get(i);
            if (c.map.mapID == 33 && c.idChar == _char.charID) {
                have = true;
                break;
            }
        }
        return have;
    }
}
