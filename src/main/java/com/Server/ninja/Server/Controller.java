 package com.Server.ninja.Server;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.time.Instant;

import com.Server.io.MySQL;
import com.Server.ninja.option.ItemOption;
import com.Server.io.Message;

import java.util.Random;

public class Controller {
    public static void onMessage(final Message msg, final Session_ME session) {
        User user = null;
        Char _char = null;
        TileMap tileMap = null;
        if (msg != null) {
            if (session.user != null) {
                user = session.user;
                if (session.user.player != null) {
                    _char = session.user.player.getMyChar();
                    tileMap = _char.tileMap;
                }
            }
            try {
                Util.log("Send msg " + msg.getCommand());
                //System.out.println("Send msg " + msg.getCommand());
                switch (msg.getCommand()) {
                    case -30: {
                        if (_char != null) {
                            messageSubCommand(msg, _char);
                            break;
                        }
                        break;
                    }
                    case -29: {
                        messageNotLogin(msg, session);
                        break;
                    }
                    case -28: {
                        if (user != null) {
                            messageNotMap(msg, user, _char);
                            break;
                        }
                        break;
                    }
                    case -23: {
                        if (user != null && _char != null && tileMap != null) {
                            try {
                                final String text = msg.reader().readUTF();
                                if (text.equals("admin") || text.equals("admin")) {
                                    Admin.opMenu(_char);
                                    return;
                                }
                                if (text.equals("reload") || text.equals("RELOAD")) {
                                    Init.ReLoadItemShore();
                                    return;
                                }
                                if (Util.CheckString(text, "^a \\d* \\d*$")) {
                                    String[] s = text.split(" ");
                                    Item item1 = new Item(ItemOption.arrOptionDefault(Short.parseShort(s[1]), (byte) 0), Short.parseShort(s[1]), Integer.parseInt(s[2]), -1, false, (byte) 0, 5);
                                    _char.user.player.ItemBagAdd(item1);
                                }
                                for (short i = 0; i < tileMap.numPlayer; ++i) {
                                    final Char player = tileMap.aCharInMap.get(i);
                                    if (player != null && player.user != null && player.user.session != null) {
                                        Service.chatMap(player, _char.charID, text);
                                    }
                                }
                            } catch (Exception e2) {
                            }
                            break;
                        }
                        break;
                    }
                    case -22: {
                        if (user != null && _char != null && tileMap != null) {
                            final Char player2 = Client.getPlayer(msg.reader().readUTF());
                            final String text2 = msg.reader().readUTF();
                            if (player2 != null) {
                                Service.chatPrivate(player2, _char.cName, text2);
                            }
                            break;
                        }
                        break;
                    }
                    case -21: {
                        if (user != null && _char != null && tileMap != null && !_char.user.player.isTrade) {
                            GameScr.chatKTG(_char, msg.reader().readUTF());
                            break;
                        }
                        break;
                    }
                    case -20: {
                        if (user != null && _char != null && tileMap != null && _char.party != null) {
                            synchronized (_char.party.LOCK) {
                                final String text2 = msg.reader().readUTF();
                                for (byte j = 0; j < _char.party.numPlayer; ++j) {
                                    final Char player3 = _char.party.aChar.get(j);
                                    if (player3 != null) {
                                        Service.chatParty(player3, _char.cName, text2);
                                    }
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case -19: {
                        if (user != null && _char != null && tileMap != null && _char.clan != null && !_char.cClanName.isEmpty()) {
                            final Clan clan = _char.clan;
                            final String text2 = msg.reader().readUTF();
                            synchronized (clan.LOCK_CLAN) {
                                clan.chatClan(_char.cName, text2);
                            }
                            break;
                        }
                        break;
                    }
                    case -17: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            Char.ChangeMap(_char);
                            break;
                        }
                        break;
                    }
                    case -16: {
                        if (user != null && _char != null && tileMap != null) {
                            Service.ClearMap(_char);
                            break;
                        }
                        break;
                    }
                    case -14: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            tileMap.PickItemMap(_char, msg.reader().readShort());
                            break;
                        }
                        break;
                    }
                    case -12: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            final Item item = _char.user.player.ItemBag(msg.reader().readByte());
                            if (item != null && !item.isLock) {
                                final ItemMap itemMap = new ItemMap(item, 0, 60000, _char.cx, _char.cy, false);
                                try {
                                    tileMap.lock.lock("Bo item");
                                    try {
                                        tileMap.ItemMapADD(itemMap);
                                    } finally {
                                        tileMap.lock.unlock();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                _char.user.player.ItemBag[item.indexUI] = null;
                                Service.throwItem(_char, (byte) item.indexUI, itemMap);
                                Service.PlayerThow(_char, _char.charID, itemMap);
                                try {
                                    for (int k = 0; k < tileMap.numPlayer; ++k) {
                                        if (tileMap.aCharInMap.get(k).user != null && tileMap.aCharInMap.get(k).user.session != null && _char.charID != tileMap.aCharInMap.get(k).charID) {
                                            Service.PlayerThow(tileMap.aCharInMap.get(k), _char.charID, itemMap);
                                        }
                                    }
                                } catch (Exception ex) {
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case -10: {
                        if (user == null || _char == null || tileMap == null || _char.statusMe != 14 || _char.user.player.delayLiveGold > 0 || MapServer.notLive(_char.tileMap.mapID)) {
                            break;
                        }
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                            break;
                        }
                        if (_char.user.luong < 1) {
                            GameCanvas.startOKDlg(session, Text.get(0, 50));
                            break;
                        }
                        _char.user.player.upGold(-1L, (byte) 1);
                        _char.cHP = _char.cMaxHP();
                        _char.cMP = _char.cMaxMP();
                        _char.statusMe = 1;
                        _char.user.player.delayLiveGold = 5000;
                        Service.MeLive(_char);
                        try {
                            for (int l = 0; l < tileMap.numPlayer; ++l) {
                                if (tileMap.aCharInMap.get(l).user != null && tileMap.aCharInMap.get(l).user.session != null && _char.charID != tileMap.aCharInMap.get(l).charID) {
                                    Service.PlayerLoadLive(tileMap.aCharInMap.get(l), _char.charID, _char.cHP, _char.cMaxHP(), _char.cx, _char.cy);
                                }
                            }
                        } catch (Exception e2) {
                        }
                        break;
                    }
                    case -9: {
                        // hồi sinh
                        if (user != null && _char != null && tileMap != null && _char.statusMe == 14 && !_char.user.player.isTrade) {
                            Map map;
                            if (tileMap.map.isBackCaveMap()) {
                                map = tileMap.map.backCave.maps[0];
                            } else if (tileMap.map.isChienTruong() && tileMap.map.chientruong.aCharWhite.contains(_char.user.player.playerId)) {
                                map = ChienTruong.getMap(tileMap.map.chientruong, (short) 98);
                            } else if (tileMap.map.isChienTruong() && tileMap.map.chientruong.aCharBlack.contains(_char.user.player.playerId)) {
                                map = ChienTruong.getMap(tileMap.map.chientruong, (short) 104);
                            } else if (tileMap.map.isClanManor()) {
                                map = ClanManor.getMap(tileMap.map.clanManor, (short) 80);
                            } else if (tileMap.map.isSevenBeasts()) {
                                map = SevenBeasts.getMap(tileMap.map.sevenBeasts, (short) 113);
                            } else if (tileMap.map.isWarClanMap()) {
                                short mapId = 22;
                                if (_char.clan.typeWar == 4) {
                                    mapId = 118;
                                }
                                if (_char.clan.typeWar == 5) {
                                    mapId = 119;
                                }
                                map = WarClan.getMap(tileMap.map.warClan, mapId);
                            } else {
                                map = MapServer.getMapServer(_char.mapLTDId);
                            }
                            if (map != null) {
                                final TileMap tile = map.getSlotZone(_char);
                                if (tile == null) {
                                    GameCanvas.startOKDlg(session, Text.get(0, 24));
                                } else {
                                    tileMap.Exit(_char);
                                    _char.cHP = _char.cMaxHP();
                                    _char.cMP = _char.cMaxMP();
                                    _char.statusMe = 1;
                                    _char.cx = tile.map.template.goX;
                                    _char.cy = tile.map.template.goY;
                                    Service.MeLive(_char);
                                    tile.Join(_char);
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 1: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            Char.MoveLast(_char, msg.reader().readShort(), msg.reader().readShort());
                            break;
                        }
                        break;
                    }
                    case 4:
                    case 73: {
                        if (user != null && _char != null && tileMap != null && _char.getEffType((byte) 10) == null && !_char.user.player.isTrade) {
                            final Skill skillFight = _char.myskill;
                            if (skillFight != null) {
                                if (_char.ItemBody[1] == null) {
                                    Service.ServerMessage(_char, Text.get(0, 32));
                                } else if (_char.cMP < skillFight.manaUse) {
                                    Service.MELoadMP(_char);
                                    Service.ServerMessage(_char, Text.get(0, 283));
                                } else if (_char.ASkill.size() > 0) {
                                    final byte size = msg.reader().readByte();
                                    if (size >= 0 && size <= skillFight.maxFight) {
                                        final Mob[] arrMob = new Mob[size];
                                        final Char[] arrChar = new Char[skillFight.maxFight];
                                        try {
                                            for (byte m = 0; m < arrMob.length && m < skillFight.maxFight; ++m) {
                                                final Mob mob = tileMap.getMob(msg.reader().readUnsignedByte());
                                                if ((!mob.isMobBlack() && !mob.isMobBlack()) || (mob.isMobBlack() && _char.cTypePk != 5) || (mob.isMobWhite() && _char.cTypePk != 4)) {
                                                    arrMob[m] = mob;
                                                }
                                            }
                                            for (byte m = 0; m < arrChar.length && m < skillFight.maxFight; ++m) {
                                                final Char player4 = _char.findCharInMap(msg.reader().readInt());
                                                if (player4 != null && !player4.isInvisible && !player4.isNhanban && (_char.cTypePk == 3 || player4.cTypePk == 3 || (_char.cTypePk == 1 && player4.cTypePk == 1) || (player4.cTypePk == 4 && _char.cTypePk == 5) || (player4.cTypePk == 5 && _char.cTypePk == 4) || (_char.isTest && player4.isTest && _char.testCharID == player4.charID && _char.charID == player4.testCharID) || _char.KillCharId == player4.charID || player4.KillCharId == _char.charID)) {
                                                    arrChar[m] = player4;
                                                }
                                            }
                                        } catch (Exception ex2) {
                                        }
                                        SkillUse.PlayerAttack(_char.tileMap, skillFight, _char, _char, arrMob, arrChar, (byte) 0);
                                        if (_char.isHuman && _char.Nhanban != null && _char.isBatNhanban) {
                                            SkillUse.PlayerAttack(_char.tileMap, _char.Nhanban.myskill, _char, _char.Nhanban, arrMob, arrChar, (byte) 1);
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 12: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            ItemUse.useItemChangeMap(_char, msg.reader().readByte(), msg.reader().readByte());
                            break;
                        }
                        break;
                    }
                    case 11: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            ItemUse.ItemUse(_char, msg.reader().readByte());
                            break;
                        }
                        break;
                    }
                    case 13: {// Buy shop
                        if (user == null || _char == null || tileMap == null || _char.user.player.isTrade || _char.statusMe == 14) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        final byte typeUI = msg.reader().readByte();
                        final int indexUI = msg.reader().readUnsignedByte();
                        int quantity = 1;
                        try {
                            quantity = msg.reader().readUnsignedShort();
                        } catch (Exception ex3) {
                        }
                        Item item2 = null;
                        try {
                            item2 = GameScr.itemStores.get(typeUI)[indexUI];
                            item2.typeUI = typeUI;
                        } catch (Exception ex4) {
                        }
                        if (MapServer.notTrade(_char.tileMap.mapID)) {
                            Service.ServerMessage(_char, Text.get(0, 300));
                        } else if (item2 != null && quantity > 0) {
                            if (_char.ctaskId < 3 || (_char.ctaskId == 3 && item2.template.id != 23)) {
                                GameCanvas.startOKDlg(session, Text.get(0, 29));
                            } else if (item2.isTypeUIClanShop()) {
                                if (_char.clan == null || _char.ctypeClan < 3) {
                                    GameCanvas.startOKDlg(session, Text.get(0, 229));
                                } else {
                                    final Clan clan2 = _char.clan;
                                    synchronized (clan2.LOCK_CLAN) {
                                        if (Clan.itemLevel(item2.template.id) > clan2.itemLevel) {
                                            GameCanvas.startOKDlg(session, Text.get(0, 232));
                                        } else if (clan2.coin < quantity * (long) item2.buyCoin) {
                                            GameCanvas.startOKDlg(session, Text.get(0, 227));
                                        } else {
                                            if (item2.isItemEgg()) {
                                                if (_char.clan.level < 26) {
                                                    GameCanvas.startOKDlg(session, "Yêu câu gia tộc cấp độ 26");
                                                } else if (_char.clan.clan_thanThu.size() >= 1) {
                                                    GameCanvas.startOKDlg(session, "Mỗi gia tộc chỉ được sở hữu tối đa 2 thần thú.");
                                                } else {
                                                    clan2.updateCoin(-(item2.buyCoin * (long) quantity));
                                                    Clan_ThanThu clan_thanThu = new Clan_ThanThu();
                                                    clan_thanThu.iconThanThu = item2.template.iconID;
                                                    clan_thanThu.iconMini = item2.template.iconID;
                                                    if (item2.template.id == 596) {
                                                        clan_thanThu.type = 1;
                                                    } else if (item2.template.id == 601) {
                                                        clan_thanThu.type = 2;
                                                    }
                                                    clan_thanThu.items = new Item(ItemOption.arrOptionDefault(item2.template.id, (byte) 0), item2.template.id, 1, -1, true, (byte) 0, 5);
                                                    clan2.clan_thanThu.add(clan_thanThu);
                                                    clan2.ClanEndWait(String.format(Text.get(0, 230), clan2.name, item2.template.name));
                                                }
                                            } else {
                                                clan2.updateCoin(-(item2.buyCoin * (long) quantity));
                                                boolean isBuy = false;
                                                for (byte i2 = 0; i2 < clan2.items.size(); ++i2) {
                                                    final Item it = clan2.items.get(i2);
                                                    if (it != null && it.template.id == item2.template.id) {
                                                        final Item item8 = it;
                                                        item8.quantity += quantity;
                                                        isBuy = true;
                                                        break;
                                                    }
                                                }
                                                if (!isBuy) {
                                                    final Item itnew = Event.itemBuyStore(item2.clone());
                                                    itnew.quantity = quantity;
                                                    itnew.typeUI = 39;
                                                    for (short m2 = 0; m2 < itnew.options.size(); ++m2) {
                                                        itnew.options.get(m2).param = Util.nextInt(itnew.options.get(m2).getOptionShopParam(), itnew.options.get(m2).param);
                                                    }
                                                    clan2.items.add(itnew);
                                                }
                                                clan2.ClanEndWait(String.format(Text.get(0, 230), clan2.name, item2.template.name));
                                            }
                                        }
                                    }
                                }
                            } else if (GameScr.vEvent_1 == 1 && item2.template.id == 824 && (Event_1.buy_Count < quantity || Event_1.timeSale > 0L)) {
                                GameCanvas.startOKDlg(session, String.format(Text.get(0, 328), Util.getStrTime(Event_1.timeSale)));
                            } else if (_char.user.player.yen < quantity * (long) item2.buyCoinLock) {
                                GameCanvas.startOKDlg(session, Text.get(0, 26));
                            } else if (_char.user.player.xu < quantity * (long) item2.buyCoin) {
                                GameCanvas.startOKDlg(session, Text.get(0, 27));
                            } else if (_char.user.luong < quantity * (long) item2.buyGold) {
                                GameCanvas.startOKDlg(session, Text.get(0, 28));
                            } else if ((item2.template.isUpToUp && _char.user.player.ItemBagIndexNull() == -1) || (!item2.template.isUpToUp && _char.user.player.ItemBagSlotNull() < quantity)) {
                                GameCanvas.startOKDlg(session, Text.get(0, 15));
                            } else {
                                _char.user.player.upCoin(-item2.buyCoin * (long) quantity, (byte) 0);
                                _char.user.player.upCoinLock(-item2.buyCoinLock * (long) quantity, (byte) 0);
                                _char.user.player.upGold(-item2.buyGold * (long) quantity, (byte) 0);
                                if (GameScr.vEvent_1 == 1 && item2.template.id == 824) {
                                    Event_1.buy_Count -= quantity;
                                    if (Event_1.buy_Count <= 0) {
                                        Event_1.timeSale = 7200000L;
                                    }
                                }
                                Service.Buy(_char);
                                for (int i3 = 0; i3 < quantity; ++i3) {
                                    final Item itemADD = Event.itemBuyStore(item2.clone());
                                    for (short m3 = 0; m3 < itemADD.options.size(); ++m3) {
                                        itemADD.options.get(m3).param = Util.nextInt(itemADD.options.get(m3).getOptionShopParam(), itemADD.options.get(m3).param);
                                    }
                                    if (itemADD.expires != -1L) {
                                        itemADD.expires = Util.TimeMillis(item2.expires);
                                        itemADD.isExpires = true;
                                    }
                                    if (item2.template.isUpToUp) {
                                        itemADD.quantity = quantity;
                                        _char.user.player.ItemBagAddQuantity(itemADD);
                                        break;
                                    }
                                    if (_char.ctaskId == 3 && _char.ctaskIndex == 0 && item2.template.id == 23) {
                                        _char.uptaskMaint();
                                        if (_char.ctaskIndex == 1) {
                                            Service.openUISay(_char, (short) 4, Talk.getTask(0, 32));
                                        }
                                    }
                                    itemADD.quantity = 1;
                                    _char.user.player.ItemBagAdd(itemADD);
                                }
                            }
                        }
                        break;
                    }
                    case 14: {
                        if (user != null && _char != null && tileMap != null && !_char.user.player.isTrade) {
                            final byte indexUI2 = msg.reader().readByte();
                            int quantity2 = 1;
                            try {
                                quantity2 = msg.reader().readInt();
                            } catch (Exception ex5) {
                            }
                            final Item item3 = _char.user.player.ItemBag(indexUI2);
                            if (item3 != null && quantity2 > 0) {
                                if (_char.ctaskId < 3) {
                                    GameCanvas.startOKDlg(session, Text.get(0, 18));
                                } else if (item3.isItemRare()) {
                                    GameCanvas.startOKDlg(session, Text.get(0, 96));
                                } else if (item3.template.type < 10 && item3.upgrade > 0) {
                                    GameCanvas.startOKDlg(session, Text.get(0, 97));
                                } else if (item3.isItemNgoc()) {
                                    GameCanvas.startOKDlg(session, Text.get(0, 97));
                                } else {
                                    if (item3.quantity >= quantity2) {
                                        final long coinlocksale = item3.saleCoinLock * (long) quantity2;
                                        final Item item9 = item3;
                                        item9.quantity -= quantity2;
                                        _char.user.player.upCoinLock(coinlocksale, (byte) 0);
                                    }
                                    if (item3.quantity <= 0) {
                                        _char.user.player.ItemBag[indexUI2] = null;
                                    }
                                    Service.ItemSale(_char, indexUI2, (short) quantity2);
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 15: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            Player.itemBodyToBag(_char, msg.reader().readByte());
                            break;
                        }
                        break;
                    }
                    case 16: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            Player.itemBoxToBag(_char, msg.reader().readByte());
                            break;
                        }
                        break;
                    }
                    case 17: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            Player.itemBagToBox(_char, msg.reader().readByte());
                            break;
                        }
                        break;
                    }
                    case 19: {
                        if (user == null || _char == null || tileMap == null || _char.statusMe == 14 || _char.user.player.isTrade || TileMap.NPCNear(_char, (short) 6) == null) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        final Item[] items = new Item[24];
                        try {
                            for (byte i4 = 0; i4 < items.length; ++i4) {
                                final Item item3 = _char.user.player.ItemBag[msg.reader().readByte()];
                                if (item3 == null || Item.isIndexUI(item3.indexUI, items) || !item3.isTypeCrystal()) {
                                    return;
                                }
                                items[i4] = item3;
                            }
                        } catch (Exception ex6) {
                        }
                        Player.Uppearl(_char, items, true);
                        break;
                    }
                    case 20: {
                        if (user == null || _char == null || tileMap == null || _char.statusMe == 14 || _char.user.player.isTrade || TileMap.NPCNear(_char, (short) 6) == null) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        final Item[] items = new Item[24];
                        try {
                            for (byte i4 = 0; i4 < items.length; ++i4) {
                                final Item item3 = _char.user.player.ItemBag[msg.reader().readByte()];
                                if (item3 == null || Item.isIndexUI(item3.indexUI, items) || !item3.isTypeCrystal()) {
                                    return;
                                }
                                items[i4] = item3;
                            }
                        } catch (Exception ex7) {
                        }
                        Player.Uppearl(_char, items, false);
                        break;
                    }
                    case 21: {
                        if (user == null || _char == null || tileMap == null || _char.statusMe == 14 || _char.user.player.isTrade || TileMap.NPCNear(_char, (short) 6) == null) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        final boolean isGold = msg.reader().readBoolean();
                        try {
                            final Item item4 = _char.user.player.ItemBag[msg.reader().readByte()];
                            if (item4 != null && item4.isTypeBody() && item4.template.type < 10) {
                                final Item[] items2 = new Item[18];
                                try {
                                    for (byte i5 = 0; i5 < items2.length; ++i5) {
                                        final Item item5 = _char.user.player.ItemBag[msg.reader().readByte()];
                                        if (item5 == null || Item.isIndexUI(item5.indexUI, items2)) {
                                            return;
                                        }
                                        if (!item5.isTypeCrystal() && item5.template.type != 28) {
                                            GameCanvas.startOKDlg(session, Text.get(0, 77));
                                            return;
                                        }
                                        items2[i5] = item5;
                                    }
                                } catch (Exception ex8) {
                                }
                                Player.UpGrade(_char, item4, items2, isGold);
                            }
                        } catch (Exception ex9) {
                        }
                        break;
                    }
                    case 22: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade && TileMap.NPCNear(_char, (short) 6) != null) {
                            final byte indexUI2 = msg.reader().readByte();
                            try {
                                final Item item4 = _char.user.player.ItemBag[indexUI2];
                                if (item4 != null && item4.template.type < 10 && item4.upgrade > 0) {
                                    Player.splitItem(_char, item4);
                                }
                            } catch (Exception ex10) {
                            }
                            break;
                        }
                        break;
                    }
                    case 23: {
                        if (user != null && _char != null && tileMap != null) {
                            final String name = msg.reader().readUTF();
                            final Player player5 = Client.getPlayer(name);
                            if (player5 != null) {
                                final Char cplayer = player5.getMyChar();
                                if (cplayer != null && cplayer.party != null) {
                                    synchronized (_char.aPartyInvate) {
                                        if (_char.party != null && _char.party.partyId == cplayer.party.partyId) {
                                            Service.ServerMessage(_char, Text.get(0, 99));
                                        } else if (_char.party != null) {
                                            Service.ServerMessage(_char, Text.get(0, 100));
                                        } else if (cplayer.party.numPlayer >= 6) {
                                            Service.ServerMessage(_char, Text.get(0, 107));
                                        } else if (_char.findPartyInvate(cplayer.charID) != null) {
                                            Service.ServerMessage(_char, Text.get(0, 101));
                                        } else if (cplayer.party.isLock) {
                                            Service.ServerMessage(_char, Text.get(0, 115));
                                        } else {
                                            _char.aPartyInvate.add(new Please(cplayer.party.partyId, cplayer.charID, 10000));
                                            Service.pleaseInputParty(cplayer, _char.cName);
                                        }
                                    }
                                }
                            } else {
                                Service.ServerMessage(_char, Text.get(0, 113));
                            }
                            break;
                        }
                        break;
                    }
                    case 24: {
                        if (user != null && _char != null && tileMap != null && _char.party != null && _char.party.charID == _char.charID) {
                            final String name = msg.reader().readUTF();
                            final Player player5 = Client.getPlayer(name);
                            if (player5 != null) {
                                final Char cplayer = player5.getMyChar();
                                if (cplayer != null) {
                                    synchronized (cplayer.aPartyInvate) {
                                        if (cplayer.party != null && _char.party.partyId == cplayer.party.partyId) {
                                            Service.ServerMessage(_char, String.format(Text.get(0, 106), cplayer.cName));
                                        } else if (cplayer.party != null) {
                                            Service.ServerMessage(_char, Text.get(0, 105));
                                        } else if (_char.party.numPlayer >= 6) {
                                            Service.ServerMessage(_char, Text.get(0, 107));
                                        } else if (cplayer.findPartyInvate(_char.charID) != null) {
                                            _char.party.addPlayerParty(cplayer);
                                            cplayer.tileMap.addParty(_char.party);
                                            cplayer.removePartyInvate(_char.charID);
                                            _char.party.refreshPlayer();
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 28: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            final byte zoneId = msg.reader().readByte();
                            final byte indexUI3 = msg.reader().readByte();
                            final Item item3 = _char.user.player.ItemBag(indexUI3);
                            final Map map2 = tileMap.map;
                            if (zoneId >= 0 && zoneId < map2.tileMaps.length && ((indexUI3 != -1 && item3 != null && (item3.template.id == 35 || item3.template.id == 37)) || (indexUI3 == -1 && TileMap.NPCNear(_char, (short) 13) != null))) {
                                final TileMap newtileMap = tileMap.map.tileMaps[zoneId];
                                if (newtileMap.numPlayer >= newtileMap.map.maxPlayer && (_char.party == null || newtileMap.getNumPlayerParty(_char.party.partyId) == 0)) {
                                    GameCanvas.EndWait(_char, Text.get(0, 116));
                                } else if (_char.party != null && newtileMap.getNumPlayerParty(_char.party.partyId) == 0 && newtileMap.numParty >= 4) {
                                    GameCanvas.EndWait(_char, Text.get(0, 114));
                                } else {
                                    if (item3 != null && item3.template.id == 35) {
                                        _char.user.player.ItemBagUse(indexUI3, 1);
                                    }
                                    tileMap.Exit(_char);
                                    newtileMap.Join(_char);
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 29: {
                        if (user == null || _char == null || tileMap == null || _char.user.player.isTrade) {
                            break;
                        }
                        if (_char.menuType >= 0) {
                            if (user.session.versionNja < 203) {
                                Menu.Menu(_char, msg.reader().readByte(), msg.reader().readByte(), msg.reader().readByte());
                            } else {
                                msg.reader().readByte();
                                Menu.Menu(_char, msg.reader().readByte(), msg.reader().readByte(), msg.reader().readByte());
                            }
                            break;
                        }
                        if (_char.menuType < 0) {
                            if (user.session.versionNja < 203) {
                                Menu.MenuNew(_char, msg.reader().readByte(), msg.reader().readByte(), msg.reader().readByte());
                            } else {
                                msg.reader().readByte();
                                Menu.MenuNew(_char, msg.reader().readByte(), msg.reader().readByte(), msg.reader().readByte());
                            }
                            break;
                        }
                        break;
                    }
                    case 36: {
                        if (user == null || _char == null || tileMap == null || _char.statusMe == 14 || _char.user.player.isTrade) {
                            break;
                        }
                        if (_char.tileMap.map.mapNotUIZone()) {
                            break;
                        }
                        if (TileMap.NPCNear(_char, (short) 13) != null || _char.user.player.ItemBagQuantity((short) 35) > 0 || _char.user.player.ItemBagQuantity((short) 37) > 0) {
                            Service.openUIZone(_char, _char.tileMap.map);
                            break;
                        }
                        _char.upDie((byte) 0);
                        break;
                    }
                    case 40: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            Menu.openMenu(_char, msg.reader().readShort());
                            break;
                        }
                        break;
                    }
                    case 41: {
                        if (user != null && _char != null && _char.ASkill.size() > 0 && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            final short skillTemplateId = msg.reader().readShort();
                            Util.log("Sellect Skill " + skillTemplateId);
                            Char.selectSkill(_char, skillTemplateId);
                            break;
                        }
                        break;
                    }
                    case 42: {
                        if (user != null && _char != null && tileMap != null) {
                            Item item = null;
                            final byte typeUI2 = msg.reader().readByte();
                            final int indexUI4 = msg.reader().readUnsignedByte();
                            try {
                                Util.log("typeUI" + typeUI2 + " indexUI" + indexUI4);
                                if (typeUI2 == 3) {
                                    item = _char.user.player.ItemBag[indexUI4];
                                } else if (typeUI2 == 4) {
                                    switch (_char.user.player.menuCaiTrang) {
                                        case 0: {
                                            if (indexUI4 >= 0 && indexUI4 < 30) {
                                                item = _char.user.player.ItemBox[indexUI4];
                                            }
                                            break;
                                        }
                                        case 1: { // bộ sưu tập Khổng Minh Tiến
                                            if (indexUI4 >= 0 && indexUI4 < 9) {
                                                item = _char.user.player.ItemBST[indexUI4];
                                            }
                                            break;
                                        }
                                        case 2: { // cải trang Khổng Minh Tiến
                                            if (indexUI4 >= 0 && indexUI4 < 18) {
                                                item = _char.user.player.ItemCaiTrang[indexUI4];
                                            }
                                            break;
                                        }
                                    }
                                } else if (typeUI2 == 5) {
                                    item = _char.ItemBody[indexUI4];
                                } else if (typeUI2 == 30) {
                                    if (_char.user.player.isTrade) {
                                        final Char player3 = _char.findCharInMap(_char.user.player.tradeCharId);
                                        item = player3.user.player.itemTrade[indexUI4];
                                    }
                                } else if (typeUI2 == 39) {
                                    if (!_char.cClanName.isEmpty() && _char.clan != null) {
                                        item = _char.clan.items.get(indexUI4);
                                    }
                                } else {
                                    try {
                                        item = GameScr.itemStores.get(typeUI2)[indexUI4];
                                    } catch (Exception ex11) {
                                    }
                                }
                            } catch (Exception ex12) {
                            }
                            if (item != null) {
                                Service.ItemInfo(_char, item, typeUI2, indexUI4);
                            }
                            break;
                        }
                        break;
                    }
                    case 43: {
                        if (user == null || _char == null || tileMap == null || _char.statusMe == 14 || _char.user.player.isTrade) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        Player.tradeInvite(_char, msg.reader().readInt());
                        break;
                    }
                    case 44: {
                        if (user == null || _char == null || tileMap == null || _char.statusMe == 14 || _char.user.player.isTrade) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        Player.tradeAccept(_char, msg.reader().readInt());
                        break;
                    }
                    case 45: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && _char.user.player.isTrade && _char.user.player.tradeType == 0) {
                            try {
                                final Char player2 = _char.findCharInMap(_char.user.player.tradeCharId);
                                final int coin = msg.reader().readInt();
                                if (coin > 0 && coin <= _char.user.player.xu) {
                                    _char.user.player.tradeCoin = coin;
                                } else {
                                    _char.user.player.tradeCoin = 0;
                                }
                                final byte num = msg.reader().readByte();
                                byte num2 = 0;
                                for (byte m = 0; m < num; ++m) {
                                    final Item item6 = _char.user.player.ItemBag[msg.reader().readByte()];
                                    if (Item.isIndexUI(item6.indexUI, _char.user.player.itemTrade)) {
                                        Player.endTrade(_char);
                                    } else if (!item6.isLock) {
                                        _char.user.player.itemTrade[m] = item6;
                                        ++num2;
                                    }
                                }
                                if (num2 > 12 || num2 > player2.user.player.ItemBagSlotNull()) {
                                    Player.endTrade(_char);
                                } else {
                                    final Player player7 = _char.user.player;
                                    ++player7.tradeType;
                                    Service.TradeLockItem(player2, coin, _char.user.player.itemTrade);
                                }
                            } catch (Exception e2) {
                                Player.endTrade(_char);
                            }
                            break;
                        }
                        break;
                    }
                    case 46: {
                        if (user == null || _char == null || tileMap == null || _char.statusMe == 14 || !_char.user.player.isTrade || _char.user.player.tradeType != 1) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        if (_char.user.player.tradeType < 3) {
                            final Player player8 = _char.user.player;
                            ++player8.tradeType;
                            final Char player2 = _char.findCharInMap(_char.user.player.tradeCharId);
                            if (player2 != null) {
                                Service.TradeAccept(player2);
                                if (player2.user.player.tradeType == 2 && _char.user.player.tradeType == 2) {
                                    Logs.giaodichl(player2.cName, player2.user.player.tradeCoin, _char.cName, player2.user.player.itemTrade);
                                    Logs.giaodichl(_char.cName, _char.user.player.tradeCoin, player2.cName, _char.user.player.itemTrade);
                                    if (_char.user.player.tradeCoin > 0) {
                                        _char.user.player.upCoin(-_char.user.player.tradeCoin, (byte) 0);
                                        player2.user.player.upCoin(_char.user.player.tradeCoin, (byte) 0);
                                    }
                                    if (player2.user.player.tradeCoin > 0) {
                                        player2.user.player.upCoin(-player2.user.player.tradeCoin, (byte) 0);
                                        _char.user.player.upCoin(player2.user.player.tradeCoin, (byte) 0);
                                    }
                                    for (byte i4 = 0; i4 < _char.user.player.itemTrade.length; ++i4) {
                                        final Item item3 = _char.user.player.itemTrade[i4];
                                        if (item3 != null) {
                                            _char.user.player.ItemBag[item3.indexUI] = null;
                                        }
                                    }
                                    for (byte i4 = 0; i4 < player2.user.player.itemTrade.length; ++i4) {
                                        final Item item3 = player2.user.player.itemTrade[i4];
                                        if (item3 != null) {
                                            player2.user.player.ItemBag[item3.indexUI] = null;
                                        }
                                    }
                                    for (byte i4 = 0; i4 < player2.user.player.itemTrade.length; ++i4) {
                                        final Item item3 = player2.user.player.itemTrade[i4];
                                        if (item3 != null) {
                                            if (item3.template.isUpToUp) {
                                                _char.user.player.ItemBagAddQuantity(item3);
                                            } else {
                                                _char.user.player.ItemBagAdd(item3);
                                            }
                                        }
                                    }
                                    for (byte i4 = 0; i4 < _char.user.player.itemTrade.length; ++i4) {
                                        final Item item3 = _char.user.player.itemTrade[i4];
                                        if (item3 != null) {
                                            if (item3.template.isUpToUp) {
                                                player2.user.player.ItemBagAddQuantity(item3);
                                            } else {
                                                player2.user.player.ItemBagAdd(item3);
                                            }
                                        }
                                    }
                                    for (byte i4 = 0; i4 < _char.user.player.itemTrade.length; ++i4) {
                                        _char.user.player.itemTrade[i4] = null;
                                    }
                                    _char.user.player.tradeCoin = 0;
                                    _char.user.player.tradeType = 0;
                                    _char.user.player.tradeCharId = -9999;
                                    _char.user.player.tradeCharIdwait = -9999;
                                    _char.user.player.delayCancelTrade = 0;
                                    _char.user.player.isTrade = false;
                                    for (byte i4 = 0; i4 < player2.user.player.itemTrade.length; ++i4) {
                                        player2.user.player.itemTrade[i4] = null;
                                    }
                                    player2.user.player.tradeCoin = 0;
                                    player2.user.player.tradeType = 0;
                                    player2.user.player.tradeCharId = -9999;
                                    player2.user.player.tradeCharIdwait = -9999;
                                    player2.user.player.delayCancelTrade = 0;
                                    player2.user.player.isTrade = false;
                                    Service.okTrade(_char);
                                    Service.okTrade(player2);
                                    _char.user.flush();
                                    player2.user.flush();
                                }
                            } else {
                                Player.endTrade(_char);
                            }
                            break;
                        }
                        break;
                    }
                    case 47: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            Task.getTask(_char, msg.reader().readByte(), msg.reader().readByte(), (byte) (-1));
                            break;
                        }
                        break;
                    }
                    case 56: {
                        if (user != null && _char != null && tileMap != null && !_char.user.player.isTrade) {
                            _char.user.player.delayCancelTrade = 0;
                            _char.user.player.tradeCharIdwait = -9999;
                            break;
                        }
                        break;
                    }
                    case 57: {
                        if (user != null && _char != null && tileMap != null && _char.user.player.isTrade) {
                            Player.endTrade(_char);
                            break;
                        }
                        break;
                    }
                    case 59: {
                        if (user == null || _char == null || tileMap == null) {
                            break;
                        }
                        if (_char.user.player.vFriend.size() < GameScr.max_Friend) {
                            _char.user.player.addFriend(msg.reader().readUTF());
                            break;
                        }
                        Service.ServerMessage(_char, Text.get(0, 42));
                        break;
                    }
                    case 60: {
                        if (user != null && _char != null && tileMap != null && _char.getEffType((byte) 10) == null && !_char.user.player.isTrade) {
                            final Skill skillFight = _char.myskill;
                            if (skillFight != null) {
                                if (_char.ItemBody[1] == null) {
                                    Service.ServerMessage(_char, Text.get(0, 32));
                                } else if (_char.cMP < skillFight.manaUse) {
                                    Service.MELoadMP(_char);
                                    Service.ServerMessage(_char, Text.get(0, 283));
                                } else if (_char.ASkill.size() > 0) {
                                    final Mob[] arrMob2 = new Mob[skillFight.maxFight];
                                    try {
                                        for (byte j = 0; j < arrMob2.length && j < skillFight.maxFight; ++j) {
                                            final Mob mob2 = tileMap.getMob(msg.reader().readUnsignedByte());
                                            if ((!mob2.isMobBlack() && !mob2.isMobBlack()) || (mob2.isMobBlack() && _char.cTypePk != 5) || (mob2.isMobWhite() && _char.cTypePk != 4)) {
                                                arrMob2[j] = mob2;
                                            }
                                        }
                                    } catch (Exception ex13) {
                                    }
                                    SkillUse.PlayerAttack(_char.tileMap, skillFight, _char, _char, arrMob2, null, (byte) 0);
                                    if (_char.isHuman && _char.Nhanban != null && _char.isBatNhanban) {
                                        SkillUse.PlayerAttack(_char.tileMap, _char.Nhanban.myskill, _char, _char.Nhanban, arrMob2, null, (byte) 1);
                                    }
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 61: {
                        if (user != null && _char != null && tileMap != null && _char.getEffType((byte) 10) == null && !_char.user.player.isTrade) {
                            final Skill skillFight = _char.myskill;
                            if (skillFight != null) {
                                if (_char.ItemBody[1] == null) {
                                    Service.ServerMessage(_char, Text.get(0, 32));
                                } else if (_char.cMP < skillFight.manaUse) {
                                    Service.MELoadMP(_char);
                                    Service.ServerMessage(_char, Text.get(0, 283));
                                } else if (_char.ASkill.size() > 0) {
                                    final Char[] arrChar2 = new Char[skillFight.maxFight];
                                    try {
                                        for (byte j = 0; j < arrChar2.length && j < skillFight.maxFight; ++j) {
                                            final Char player3 = _char.findCharInMap(msg.reader().readInt());
                                            if (player3 != null && !player3.isInvisible && !player3.isNhanban && (_char.cTypePk == 3 || player3.cTypePk == 3 || (_char.cTypePk == 1 && player3.cTypePk == 1) || (player3.cTypePk == 4 && _char.cTypePk == 5) || (player3.cTypePk == 5 && _char.cTypePk == 4) || (_char.isTest && player3.isTest && _char.testCharID == player3.charID && _char.charID == player3.testCharID) || _char.KillCharId == player3.charID || player3.KillCharId == _char.charID)) {
                                                arrChar2[j] = player3;
                                            }
                                        }
                                    } catch (Exception ex14) {
                                    }
                                    if (arrChar2!= null && arrChar2[0].isBot) {// đánh Bot
                                        int dame = _char.cdameDown();
                                        arrChar2[0].upHP(-dame);
                                        for (short i = 0; i < tileMap.aCharInMap.size(); ++i) {
                                            if (tileMap.aCharInMap.get(i).user != null && tileMap.aCharInMap.get(i).user.session != null) {
                                                Service.HavePlayerAttack(tileMap.aCharInMap.get(i), arrChar2[0], dame);
                                            }
                                        }
                                    }
                                    SkillUse.PlayerAttack(_char.tileMap, skillFight, _char, _char, null, arrChar2, (byte) 0);
                                    if (_char.isHuman && _char.Nhanban != null && _char.isBatNhanban) {
                                        SkillUse.PlayerAttack(_char.tileMap, _char.Nhanban.myskill, _char, _char.Nhanban, null, arrChar2, (byte) 1);
                                    }
                                    if (_char.mobMe != null && Mob.arrMobTemplate[_char.mobMe.templateId].isAttack && _char.statusMe != 14 && Math.abs(_char.cx - arrChar2[0].cx) < 100 && Math.abs(_char.cy - arrChar2[0].cx) < 80) {
                                        if (_char.mobMe.fighttime <= 0) {
                                            _char.mobMe.fighttime = 3000;
                                            Mob.MobMeAtk(_char, null, arrChar2[0]);
                                        } else {
                                            final Mob mobMe2;
                                            final Mob mobMe = mobMe2 = _char.mobMe;
                                            mobMe2.fighttime -= 500;
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 65: {
                        if (user == null || _char == null || tileMap == null || _char.isTest || _char.statusMe == 14 || _char.user.player.isTrade) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        if (tileMap.map.isPKMap()) {
                            Service.ServerMessage(_char, Text.get(0, 306));
                            break;
                        }
                        Char player2 = _char.findCharInMap(_char.testCharID);
                        if (_char.testCharID != -9999 && player2 != null) {
                            Service.ServerMessage(_char, Text.get(0, 31));
                        } else {
                            player2 = _char.findCharInMap(msg.reader().readInt());
                            if (player2 != null && player2.tileMap != null && !player2.isTest) {
                                _char.testCharID = player2.charID;
                                _char.delaytestCharID = 10;
                                Service.TestInvite(player2, _char.charID);
                            }
                        }
                        break;
                    }
                    case 66: {
                        if (user == null || _char == null || tileMap == null || _char.isTest || _char.statusMe == 14 || _char.user.player.isTrade) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        final Char player2 = _char.findCharInMap(msg.reader().readInt());
                        if (player2 != null && player2.testCharID == _char.charID && !player2.isTest) {
                            _char.testCharID = player2.charID;
                            _char.isTest = true;
                            player2.isTest = true;
                            try {
                                for (int i6 = 0; i6 < tileMap.numPlayer; ++i6) {
                                    if (tileMap.aCharInMap.get(i6).user != null && tileMap.aCharInMap.get(i6).user.session != null) {
                                        Service.TestAccept(tileMap.aCharInMap.get(i6), _char.charID, player2.charID);
                                    }
                                }
                            } catch (Exception ex15) {
                            }
                        }
                        break;
                    }
                    case 68: {
                        if (user == null || _char == null || tileMap == null || _char.statusMe == 14) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        if (MapServer.notCombat(tileMap.mapID)) {
                            Service.ServerMessage(_char, Text.get(0, 46));
                            break;
                        }
                        if (_char.cPk >= 14) {
                            Service.ServerMessage(_char, Text.get(0, 47));
                            break;
                        }
                        if (_char.findCharInMap(_char.KillCharId) != null) {
                            Service.ServerMessage(_char, Text.get(0, 48));
                            break;
                        }
                        final Char player2 = _char.findCharInMap(msg.reader().readInt());
                        if (player2 != null && player2.statusMe != 14) {
                            _char.KillCharId = player2.charID;
                            Service.AddCuuSat(player2, _char.charID);
                            Service.MeCuuSat(_char, player2.charID);
                        }
                        break;
                    }
                    case 74: {
                        if (user != null && _char != null && tileMap != null) {
                            final Skill skillbuff = _char.myskill;
                            if (skillbuff != null) {
                                if (_char.ItemBody[1] == null) {
                                    Service.ServerMessage(_char, Text.get(0, 32));
                                } else if (_char.cMP < _char.myskill.manaUse) {
                                    Service.MELoadMP(_char);
                                    Service.ServerMessage(_char, Text.get(0, 283));
                                } else if (_char.ASkill.size() > 0) {
                                    final byte cdir = msg.reader().readByte();
                                    Util.log("Use skill ______ " + cdir + " id:" + skillbuff.template.id);
                                    if (skillbuff.template.type == 2) {
                                        SkillUse.myBuff(_char.tileMap, _char, _char, skillbuff, cdir);
                                    }
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 79: {
                        if (user != null && _char != null && tileMap != null) {
                            final String name = msg.reader().readUTF();
                            final Player player5 = Client.getPlayer(name);
                            if (player5 != null) {
                                final Char cplayer = player5.getMyChar();
                                synchronized (_char.aPartyInvite) {
                                    if (_char.findPartyInvite(cplayer.charID) != null) {
                                        Service.ServerMessage(_char, Text.get(0, 98));
                                    } else if (_char.party != null) {
                                        if (_char.party.charID != _char.charID) {
                                            Service.ServerMessage(_char, Text.get(0, 102));
                                        } else if (_char.party.numPlayer >= 6) {
                                            Service.ServerMessage(_char, Text.get(0, 107));
                                        } else if (cplayer.party != null && _char.party.partyId == cplayer.party.partyId) {
                                            Service.ServerMessage(_char, String.format(Text.get(0, 106), cplayer.cName));
                                        } else if (cplayer.party != null) {
                                            Service.ServerMessage(_char, Text.get(0, 105));
                                        } else {
                                            _char.aPartyInvite.add(new Please(_char.party.partyId, cplayer.charID, 10000));
                                            Service.inviteParty(cplayer, _char.cName, _char.charID);
                                        }
                                    } else if (cplayer.party != null) {
                                        Service.ServerMessage(_char, Text.get(0, 105));
                                    } else if (_char.tileMap.numParty >= 4) {
                                        Service.ServerMessage(_char, Text.get(0, 114));
                                    } else {
                                        final Party party = new Party(_char);
                                        _char.party = party;
                                        _char.tileMap.addParty(party);
                                        _char.aPartyInvite.add(new Please(party.partyId, cplayer.charID, 10000));
                                        Service.PlayerInParty(_char, party);
                                        Service.inviteParty(cplayer, _char.cName, _char.user.player.charID);
                                    }
                                }
                            } else {
                                Service.ServerMessage(_char, Text.get(0, 104));
                            }
                            break;
                        }
                        break;
                    }
                    case 80: {
                        if (user != null && _char != null && tileMap != null) {
                            final int charId = msg.reader().readInt();
                            final Player player5 = Client.getPlayer(charId);
                            if (player5 != null) {
                                final Char cplayer = player5.getMyChar();
                                if (cplayer.party != null) {
                                    synchronized (cplayer.aPartyInvite) {
                                        if (_char.party != null && _char.party.partyId == cplayer.party.partyId) {
                                            Service.ServerMessage(_char, Text.get(0, 99));
                                        } else if (_char.party != null) {
                                            Service.ServerMessage(_char, Text.get(0, 108));
                                        } else if (cplayer.party.numPlayer >= 6) {
                                            Service.ServerMessage(_char, Text.get(0, 107));
                                        } else if (cplayer.findPartyInvite(_char.charID) != null) {
                                            cplayer.party.addPlayerParty(_char);
                                            cplayer.removePartyInvite(_char.charID);
                                            _char.party.refreshPlayer();
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 83: {
                        if (user != null && _char != null && tileMap != null && _char.party != null) {
                            final Party party2 = _char.party;
                            if (party2 != null) {
                                synchronized (party2.LOCK) {
                                    for (byte j = 0; j < party2.numPlayer; ++j) {
                                        final Char player3 = party2.aChar.get(j);
                                        if (player3 != null && player3.charID != _char.charID) {
                                            Service.ServerMessage(player3, String.format(Text.get(0, 110), _char.cName));
                                        }
                                    }
                                }
                                party2.removePlayer(_char.charID);
                            }
                            break;
                        }
                        break;
                    }
                    case 92: {
                        if (user != null && _char != null && tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            final short menuId = msg.reader().readShort();
                            final String str = msg.reader().readUTF();
                            byte type = -1;
                            try {
                                type = msg.reader().readByte();
                            } catch (Exception ex16) {
                            }
                            TextBox.openTextBoxId(_char, menuId, str, type);
                            break;
                        }
                        break;
                    }
                    case 93: {
                        if (user != null && _char != null && tileMap != null && !_char.user.player.isTrade) {
                            final String name = msg.reader().readUTF();
                            if (name.equals(_char.cName)) {
                                Service.viewInfo(_char, _char);
                                Service.updateInfoMe(_char);
                            } else {
                                final Player player5 = Client.getPlayer(name);
                                if (player5 != null) {
                                    final Char cplayer = player5.getMyChar();
                                    if (_char.user.player.viewPlayer != null) {
                                        Service.viewInfo(_char, cplayer);
                                        Service.updateInfoPlayer(_char, cplayer);
                                    }
                                    Service.ServerMessage(cplayer, String.format(Text.get(0, 20), _char.cName));
                                    _char.user.player.viewPlayer = name;
                                } else {
                                    Service.ServerMessage(_char, Text.get(0, 104));
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 94: {
                        if (user != null && _char != null && tileMap != null && !_char.user.player.isTrade && _char.user.player.viewPlayer != null) {
                            final Player player6 = Client.getPlayer(_char.user.player.viewPlayer);
                            final int charId2 = msg.reader().readInt();
                            final byte indexUI5 = msg.reader().readByte();
                            if (player6 != null && player6.getMyChar().charID == charId2) {
                                final Char cplayer2 = player6.getMyChar();
                                try {
                                    final Item item7 = cplayer2.ItemBody[indexUI5];
                                    if (item7 != null) {
                                        Service.requestItemPlayer(_char, item7);
                                    }
                                } catch (Exception ex17) {
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 99: {
                        if (user == null || tileMap == null || _char == null || _char.statusMe == 14 || _char.user.player.isTrade || _char.tileMap.map.isTestDunMap()) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        final Char player2 = _char.findCharInMap(msg.reader().readInt());
                        if (player2 != null && player2.charID != _char.charID && player2.testDunCharID == _char.charID) {
                            final TestDun test = TestDun.OpenTestDun();
                            final TileMap tile2 = test.maps[0].getSlotZone(_char);
                            _char.tileMap.Exit(_char);
                            _char.cx = (short) (tile2.map.template.goX + 100);
                            _char.cy = tile2.map.template.goY;
                            if (test.black == null || test.black.isEmpty()) {
                                test.black = _char.cName;
                                _char.testDunPhe = 1;
                            }
                            tile2.Join(_char);
                            player2.tileMap.Exit(player2);
                            player2.cx = tile2.map.template.goX;
                            player2.cy = tile2.map.template.goY;
                            if (test.white == null || test.white.isEmpty()) {
                                test.white = player2.cName;
                                player2.testDunPhe = 0;
                            }
                            tile2.Join(player2);
                            _char.testDunCharID = -9999;
                            _char.delaytestDunCharID = 0;
                            player2.testDunCharID = -9999;
                            player2.delaytestDunCharID = 0;
                        }
                        break;
                    }
                    case 100: {
                        if (user != null && tileMap != null && _char != null && _char.statusMe != 14 && !_char.user.player.isTrade && !_char.tileMap.map.isTestDunMap()) {
                            synchronized (TestDun.LOCK) {
                                final byte zoneId2 = msg.reader().readByte();
                                byte j = 0;
                                while (j < TestDun.arrTestDun.size()) {
                                    final TestDun test2 = TestDun.arrTestDun.get(j);
                                    if (test2 != null && zoneId2 == test2.testdunID) {
                                        final TileMap tile3 = test2.maps[1].getSlotZone(_char);
                                        if (tile3 == null) {
                                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                            break;
                                        }
                                        _char.tileMap.Exit(_char);
                                        _char.cx = 378;
                                        _char.cy = 336;
                                        tile3.Join(_char);
                                        break;
                                    } else {
                                        ++j;
                                    }
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 102: {
                        if (user == null || tileMap == null || _char == null || _char.statusMe == 14 || _char.user.player.isTrade) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        final byte indexUI2 = msg.reader().readByte();
                        final int value = msg.reader().readInt();
                        if (MapServer.notTrade(_char.tileMap.mapID)) {
                            Service.ServerMessage(_char, Text.get(0, 300));
                        } else if (value <= 0 || value > ItemStands.maxCoinSale || value < ItemStands.minCoinSale) {
                            GameCanvas.startOKDlg(session, String.format(Text.get(0, 247), ItemStands.maxCoinSale, ItemStands.minCoinSale));
                        } else if (_char.user.player.xu < ItemStands.fireCoin) {
                            GameCanvas.startOKDlg(session, Text.get(0, 27));
                        } else {
                            final int numSale = ItemStands.numbSale(_char.cName, -1);
                            if (numSale >= 15) {
                                GameCanvas.startOKDlg(session, Text.get(0, 248));
                            }
                            if (_char.cLevel < 50) {
                                GameCanvas.startOKDlg(session, "Yêu cầu trình độ cấp 50.");
                            } else {
                                final Item item2 = _char.user.player.ItemBag(indexUI2);
                                if (item2 != null && !item2.isLock && !item2.isExpires) {
                                    ItemStands.sendItem(item2, _char.cName, value);
                                    _char.user.player.ItemBag[indexUI2] = null;
                                    _char.user.player.upCoin(-ItemStands.fireCoin, (byte) 0);
                                    Service.sendItemToAuction(_char, indexUI2, _char.user.player.xu);
                                }
                            }
                        }
                        break;
                    }
                    case 104: {
                        if (user != null && tileMap != null && _char != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            final int itemId = msg.reader().readInt();
                            synchronized (ItemStands.LOCK) {
                                ItemStands itemStands = null;
                                for (byte i5 = 0; i5 < ItemStands.arrItemStands.length && itemStands == null; ++i5) {
                                    for (int j2 = 0; j2 < ItemStands.arrItemStands[i5].size(); ++j2) {
                                        if (ItemStands.arrItemStands[i5].get(j2).itemId == itemId) {
                                            itemStands = ItemStands.arrItemStands[i5].get(j2);
                                            break;
                                        }
                                    }
                                }
                                if (itemStands != null) {
                                    Service.viewItemAuction(_char, itemStands);
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 105: {
                        if (user == null || tileMap == null || _char == null || _char.statusMe == 14 || _char.user.player.isTrade) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        final int itemId = msg.reader().readInt();
                        if (MapServer.notTrade(_char.tileMap.mapID)) {
                            Service.ServerMessage(_char, Text.get(0, 300));
                        } else if (_char.user.player.ItemBagIndexNull() == -1) {
                            GameCanvas.startOKDlg(session, Text.get(0, 15));
                        } else {
                            synchronized (ItemStands.LOCK) {
                                ItemStands itemStands = null;
                                byte type2 = -1;
                                int indexUI6 = -1;
                                for (byte i7 = 0; i7 < ItemStands.arrItemStands.length && itemStands == null; ++i7) {
                                    for (int j3 = 0; j3 < ItemStands.arrItemStands[i7].size(); ++j3) {
                                        if (ItemStands.arrItemStands[i7].get(j3).itemId == itemId) {
                                            itemStands = ItemStands.arrItemStands[i7].get(j3);
                                            type2 = i7;
                                            indexUI6 = j3;
                                            break;
                                        }
                                    }
                                }
                                if (itemStands != null) {
                                    if (itemStands.price > _char.user.player.xu) {
                                        Service.ServerMessage(_char, Text.get(0, 245));
                                    } else {
                                        ItemStands.arrItemStands[type2].remove(indexUI6);
                                        if (itemStands.item.template.isUpToUp) {
                                            _char.user.player.ItemBagAddQuantity(itemStands.item);
                                        } else {
                                            _char.user.player.ItemBagAdd(itemStands.item);
                                        }
                                        final int coin2 = (int) (itemStands.price * 90L / 100L);
                                        ItemWait.addCoin(-1, itemStands.seller, coin2);
                                        _char.user.player.upCoin(-itemStands.price, (byte) 2);
                                    }
                                } else {
                                    Service.ServerMessage(_char, Text.get(0, 246));
                                }
                            }
                        }
                        break;
                    }
                    case 107: {
                        if (user != null && tileMap != null && _char != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            Confirm.openConfirmID(_char, msg.reader().readByte());
                            break;
                        }
                        break;
                    }
                    case 108: {
                        if (user != null && tileMap != null && _char != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            Player.itemMonToBag(_char, msg.reader().readByte());
                            break;
                        }
                        break;
                    }
                    case 110: {
                        if (user == null || tileMap == null || _char == null || _char.statusMe == 14 || _char.user.player.isTrade) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        TinhLuyen.LuyenTinhThach(_char, msg);
                        break;
                    }
                    case 111: {
                        if (user == null || tileMap == null || _char == null || _char.statusMe == 14 || _char.user.player.isTrade) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        TinhLuyen.Tinhluyen(_char, msg);
                        break;
                    }
                    case 112: {
                        if (user == null || tileMap == null || _char == null || _char.statusMe == 14 || _char.user.player.isTrade) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        TinhLuyen.DichChuyen(_char, msg);
                        break;
                    }
                    case 117: {
                        if (user != null && tileMap != null && _char != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                            switch (msg.reader().readByte()) {
                                case 0: {
                                    Player.itemViThuToBag(_char, msg.reader().readByte());
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    case 118: {
                        if (user.isTester) {
                            String uname = msg.reader().readUTF();
                            String passw = msg.reader().readUTF();
                            String email = msg.reader().readUTF();
                            final MySQL mySQL = new MySQL(1);
                            try {
                                ResultSet red = mySQL.stat.executeQuery("SELECT * FROM `user` WHERE (`user` LIKE '" + Util.strSQL(uname) + "') LIMIT 1;");
                                if (!red.first()) {
                                    mySQL.stat.executeUpdate("UPDATE `user` SET `user`= '" + uname + "', `password` = '" + passw + "', `email` = '" + email + "', `tester` = 0 WHERE `userId`=" + user.userId + " LIMIT 1;");
                                    Service.createNewPlayer(session, uname, passw);
                                } else {
                                    GameCanvas.startOKDlg(session, "Tên tài khoản đã tồn tại.");
                                    return;
                                }
                                mySQL.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    case 124: {
                        if (user == null || tileMap == null || _char == null || _char.statusMe == 14 || _char.user.player.isTrade) {
                            break;
                        }
                        if (!_char.isHuman) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                            break;
                        }
                        final byte type3 = msg.reader().readByte();
                        if (type3 == 0) {
                            NgocKham.KhamNgoc(_char, msg);
                        } else if (type3 == 1) {
                            NgocKham.LuyenNgocKham(_char, msg);
                        } else if (type3 == 2) {
                            NgocKham.GotNgoc(_char, msg.reader().readByte());
                        } else if (type3 == 3) {
                            NgocKham.Split(_char, msg.reader().readByte());
                        }
                        break;
                    }
                    case 125: {
                        if (user == null || _char == null || tileMap == null) {
                            break;
                        }
                        final byte b = msg.reader().readByte();
                        if (b == 1) {
                            GameCanvas.getImgEffect(session, msg.reader().readShort());
                            break;
                        }
                        if (b == 2) {
                            GameCanvas.getDataEffect(session, msg.reader().readShort());
                            break;
                        }
                        break;
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            } finally {
                msg.cleanup();
            }
        }
    }

    private static void messageSubCommand(final Message msg, final Char _char) {
        try {
            final TileMap tileMap = _char.tileMap;
            final byte b = msg.reader().readByte();
            Util.log("messageSubCommand " + b);
            //System.out.println("messageSubCommand " + b);
            switch (b) {
                case -109: {
                    if (tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                        try {
                            final byte index = msg.reader().readByte();
                            final short point = msg.reader().readShort();
                            if (_char.nClass > 0 && point > 0 && point <= _char.pPoint) {
                                final int[] potential = _char.potential; // oday
                                final byte b2 = index;
                                potential[b2] += point;
                                _char.pPoint -= point;
                                _char.cHP = _char.cMaxHP();
                                _char.cMP = _char.cMaxMP();
                                Service.potentialUp(_char);
                                try {
                                    for (int i = 0; i < tileMap.numPlayer; ++i) {
                                        if (tileMap.aCharInMap.get(i).user != null && tileMap.aCharInMap.get(i).user.session != null && _char.charID != tileMap.aCharInMap.get(i).charID) {
                                            Service.PlayerLoadAll(tileMap.aCharInMap.get(i), _char);
                                        }
                                    }
                                } catch (Exception ex) {
                                }
                                if (_char.ctaskId == 9 && _char.ctaskIndex == 2) {
                                    _char.uptaskMaint();
                                }
                            }
                        } catch (Exception e2) {
                        }
                        break;
                    }
                    break;
                }
                case -108: {
                    if (tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                        final short skillTemplateId = msg.reader().readShort();
                        final byte point2 = msg.reader().readByte();
                        final Skill skill = _char.getSkill(skillTemplateId);
                        if (skill != null && point2 > 0) {
                            if (skill.point + point2 > skill.template.maxPoint) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 91));
                            } else if (_char.cLevel < skill.template.skills[skill.point + point2].level) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 92));
                            } else if (point2 > _char.sPoint) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 93));
                            } else if (skillTemplateId >= 67 && skillTemplateId <= 72) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 94));
                            } else {
                                _char.sPoint -= point2;
                                _char.upPointSkill(skillTemplateId, (byte) (-1), point2);
                            }
                        }
                        break;
                    }
                    break;
                }
                case -107: {
                    if (tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                        Player.ItemBagSort(_char.user.player);
                        break;
                    }
                    break;
                }
                case -106: {
                    if (tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                        Player.ItemBoxSort(_char.user.player);
                        break;
                    }
                    break;
                }
                case -105: {
                    if (tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                        Player.BoxIn(_char.user.player, msg.reader().readInt());
                        break;
                    }
                    break;
                }
                case -104: {
                    if (tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade) {
                        Player.BoxOut(_char.user.player, msg.reader().readInt());
                        break;
                    }
                    break;
                }
                case -103: {
                    if (tileMap != null) {
                        final byte typeUI = msg.reader().readByte();
                        if (typeUI == 4) {
                            switch (_char.user.player.menuCaiTrang) {
                                case 0: {
                                    Service.openUIBox(_char);
                                    break;
                                }
                                case 1: {
                                    Service.openMenuBST(_char);
                                    break;
                                }
                                case 2: {
                                    Service.openMenuCaiTrang(_char);
                                    break;
                                }
                            }
                        } else {
                            Item[] arrItem = null;
                            try {
                                arrItem = GameScr.itemStores.get(typeUI);
                            } catch (Exception ex2) {
                            }
                            if (arrItem != null) {
                                Service.openUIShop(_char, typeUI, arrItem);
                            }
                        }
                        break;
                    }
                    break;
                }
                case -93: {
                    if (tileMap == null) {
                        break;
                    }
                    if (!_char.isHuman) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                        break;
                    }
                    final byte typePk = msg.reader().readByte();
                    if (tileMap.map.isPKMap()) {
                        Service.ServerMessage(_char, Text.get(0, 305));
                    } else if (typePk != 0 && _char.cPk >= 15) {
                        Service.ServerMessage(_char, Text.get(0, 45));
                    } else if (typePk == 0 || typePk == 1 || typePk == 3) {
                        _char.cTypePk = typePk;
                        try {
                            for (int j = 0; j < tileMap.numPlayer; ++j) {
                                if (tileMap.aCharInMap.get(j).user != null && tileMap.aCharInMap.get(j).user.session != null) {
                                    Service.updateTypePk(tileMap.aCharInMap.get(j), _char.charID, _char.cTypePk);
                                }
                            }
                        } catch (Exception ex3) {
                        }
                    }
                    break;
                }
                case -88: {
                    if (tileMap != null && _char.party == null && _char.tileMap.numParty <= 4) {
                        final Party party = new Party(_char);
                        _char.party = party;
                        _char.tileMap.addParty(party);
                        Service.PlayerInParty(_char, party);
                        break;
                    }
                    break;
                }
                case -87: {
                    if (tileMap != null && _char.party != null && _char.party.charID == _char.charID) {
                        try {
                            final byte index = msg.reader().readByte();
                            final Char player = _char.party.aChar.get(index);
                            if (player != null && player.charID != _char.charID) {
                                synchronized (_char.party.LOCK) {
                                    for (byte k = 0; k < _char.party.numPlayer; ++k) {
                                        final Char cplayer = _char.party.aChar.get(k);
                                        if (cplayer != null && cplayer.charID == _char.charID) {
                                            _char.party.aChar.set(k, player);
                                            _char.party.charID = player.charID;
                                            break;
                                        }
                                    }
                                    _char.party.aChar.set(index, _char);
                                }
                                _char.party.refreshPlayer();
                                _char.party.TeamMessage(String.format(Text.get(0, 109), player.cName));
                            }
                        } catch (Exception e2) {
                        }
                        break;
                    }
                    break;
                }
                case -86: {
                    if (tileMap != null && _char.party != null && _char.party.charID == _char.charID) {
                        try {
                            final Char player2 = _char.party.aChar.get(msg.reader().readByte());
                            if (player2 != null && player2.charID != _char.charID) {
                                _char.party.removePlayer(player2.charID);
                                Service.ServerMessage(player2, Text.get(0, 112));
                                _char.party.TeamMessage(String.format(Text.get(0, 111), player2.cName));
                            }
                        } catch (Exception e2) {
                        }
                        break;
                    }
                    break;
                }
                case -85: {
                    if (tileMap != null) {
                        Service.requestFriend(_char.user.player);
                        break;
                    }
                    break;
                }
                case -84: {
                    if (tileMap != null) {
                        Service.requestEnemies(_char.user.player);
                        break;
                    }
                    break;
                }
                case -83:
                case -82: {
                    if (tileMap != null) {
                        Player.removeFriend(_char.user.player, msg.reader().readUTF());
                        break;
                    }
                    break;
                }
                case -79: {
                    if (tileMap != null) {
                        Skill skillbuff = null;
                        for (short l = 0; l < _char.ASkill.size(); ++l) {
                            final Skill skill = _char.ASkill.get(l);
                            if (skill != null && skill.template.id == 49) {
                                skillbuff = skill;
                                break;
                            }
                        }
                        if (skillbuff != null && skillbuff.template.id == 49) {
                            if (_char.ItemBody[1] == null) {
                                Service.ServerMessage(_char, Text.get(0, 32));
                            } else if (_char.cMP < skillbuff.manaUse) {
                                Service.MELoadMP(_char);
                                Service.ServerMessage(_char, Text.get(0, 283));
                            } else if (_char.ASkill.size() > 0) {
                                final Char player = _char.findCharInMap(msg.reader().readInt());
                                SkillUse.BuffLive(_char, tileMap, skillbuff, player);
                            }
                        }
                        break;
                    }
                    break;
                }
                case -77: {
                    if (tileMap != null) {
                        Service.findParty(_char);
                        break;
                    }
                    break;
                }
                case -76: {
                    if (tileMap != null && _char.party != null && _char.party.charID == _char.charID) {
                        _char.party.isLock = msg.reader().readBoolean();
                        _char.party.refreshLock();
                        break;
                    }
                    break;
                }
                case -67: {
                    if (_char.ASkill.size() > 2) {
                        try {
                            final String key = msg.reader().readUTF();
                            final int length = msg.reader().readInt();
                            if (key.equals("KSkill")) {
                                for (int i = 0; i < length; ++i) {
                                    final Skill skill2 = _char.getSkill(msg.reader().readByte());
                                    if (skill2 != null) {
                                        _char.KSkill[i] = skill2.template.id;
                                    }
                                }
                                final byte type = msg.reader().readByte();
                            }
                            if (key.equals("OSkill")) {
                                for (int i = 0; i < length; ++i) {
                                    final Skill skill2 = _char.getSkill(msg.reader().readByte());
                                    if (skill2 != null) {
                                        _char.OSkill[i] = skill2.template.id;
                                    }
                                }
                                final byte type = msg.reader().readByte();
                            }
                            if (key.equals("CSkill")) {
                                for (int i = 0; i < length; ++i) {
                                    final Skill skill2 = _char.getSkill(msg.reader().readByte());
                                    if (skill2 != null) {
                                        _char.CSkill[i] = skill2.template.id;
                                    }
                                }
                                final byte type = msg.reader().readByte();
                            }
                        } catch (Exception e2) {
                        }
                        break;
                    }
                    break;
                }
                case -65: {
                    final String text = msg.reader().readUTF();
                    if (text.equals("KSkill")) {
                        Service.loadRMS(_char, _char.KSkill, text);
                    }
                    if (text.equals("OSkill")) {
                        Service.loadRMS(_char, _char.OSkill, text);
                    }
                    if (text.equals("CSkill")) {
                        if (_char.CSkill[0] == -1) {
                            for (byte m = 0; m < _char.ASkill.size(); ++m) {
                                if (_char.ASkill.get(m).template.type != 0) {
                                    _char.CSkill[0] = _char.ASkill.get(m).template.id;
                                    _char.myskill = _char.ASkill.get(m);
                                }
                            }
                        } else {
                            for (byte m = 0; m < _char.ASkill.size(); ++m) {
                                if (_char.ASkill.get(m).template.id == _char.CSkill[0]) {
                                    _char.myskill = _char.ASkill.get(m);
                                }
                            }
                        }
                        Service.loadRMS(_char, _char.CSkill, text);
                        break;
                    }
                    break;
                }
                case -63: {
                    if (_char.clan == null || _char.tileMap == null || _char.statusMe == 14 || _char.user.player.isTrade || _char.member.typeClan <= 1) {
                        break;
                    }
                    if (!_char.isHuman) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                        break;
                    }
                    final Char player = _char.findCharInMap(msg.reader().readInt());
                    if (player != null) {
                        synchronized (_char.aPartyInvite) {
                            if (_char.findClanInvite(player.charID) != null) {
                                Service.ServerMessage(_char, Text.get(0, 204));
                            } else if (Math.abs(player.cx - _char.cx) > 60 || Math.abs(player.cy - _char.cy) > 60) {
                                Service.ServerMessage(_char, String.format(Text.get(0, 200), player.cName));
                            } else if (!player.cClanName.isEmpty() && player.cClanName.equals(_char.cClanName)) {
                                Service.ServerMessage(_char, String.format(Text.get(0, 201), player.cName));
                            } else if (!player.cClanName.isEmpty()) {
                                Service.ServerMessage(_char, String.format(Text.get(0, 202), player.cName));
                            } else if (_char.clan.members.size() >= Clan.getMemMax(_char.clan.level)) {
                                Service.ServerMessage(_char, Text.get(0, 203));
                            } else {
                                Service.clanInvite(player, _char.charID, _char.cClanName);
                                _char.aClanInvite.add(new Please(_char.clan.clanId, player.charID, 15000));
                            }
                        }
                    }
                    break;
                }
                case -62: {
                    if (_char.tileMap == null || _char.statusMe == 14 || _char.user.player.isTrade) {
                        break;
                    }
                    if (!_char.isHuman) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                        break;
                    }
                    final Char player = _char.findCharInMap(msg.reader().readInt());
                    if (player != null && player.clan != null && player.member.typeClan > 1) {
                        if (_char.clan != null && _char.cClanName.equals(player.cClanName)) {
                            Service.ServerMessage(_char, Text.get(0, 207));
                        } else if (!_char.cClanName.isEmpty() || _char.clan != null) {
                            Service.ServerMessage(_char, Text.get(0, 208));
                        } else {
                            synchronized (player.aClanInvite) {
                                if (player.findClanInvite(_char.charID) == null) {
                                    Service.ServerMessage(_char, Text.get(0, 205));
                                } else if (Math.abs(_char.cx - player.cx) > 60 || Math.abs(_char.cy - player.cy) > 60) {
                                    Service.ServerMessage(_char, Text.get(0, 206));
                                } else {
                                    final Clan clan = player.clan;
                                    synchronized (clan.LOCK_CLAN) {
                                        if (clan.members.size() >= Clan.getMemMax(clan.level)) {
                                            Service.ServerMessage(_char, Text.get(0, 203));
                                        } else {
                                            _char.clan = clan;
                                            _char.cClanName = clan.name;
                                            final Member member = _char.member;
                                            final byte b3 = 0;
                                            member.typeClan = b3;
                                            _char.ctypeClan = b3;
                                            if (Clan.getMem(clan, _char.cName) == null) {
                                                clan.members.add(_char.member);
                                                final Clan clan2 = clan;
                                                ++clan2.numMember;
                                            }
                                            try {
                                                for (short i2 = 0; i2 < tileMap.numPlayer; ++i2) {
                                                    if (tileMap.aCharInMap.get(i2).user != null && tileMap.aCharInMap.get(i2).user.session != null) {
                                                        Service.clanInviteAccept(tileMap.aCharInMap.get(i2), _char.charID, _char.cClanName, _char.ctypeClan);
                                                    }
                                                }
                                            } catch (Exception ex4) {
                                            }
                                            Service.ServerMessage(player, String.format(Text.get(0, 211), _char.cName, clan.name));
                                            player.removeClanInvite(_char.charID);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
                case -61: {
                    if (_char.tileMap == null || _char.statusMe == 14 || _char.user.player.isTrade) {
                        break;
                    }
                    if (!_char.isHuman) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                        break;
                    }
                    final Char player = _char.findCharInMap(msg.reader().readInt());
                    if (player != null && player.clan != null) {
                        if (_char.cClanName.equals(player.cClanName)) {
                            Service.ServerMessage(_char, Text.get(0, 212));
                        } else if (!_char.cClanName.isEmpty() || _char.clan != null) {
                            Service.ServerMessage(_char, Text.get(0, 213));
                        }
                        synchronized (_char.aClanInvate) {
                            if (_char.findClanInvate(player.charID) != null) {
                                Service.ServerMessage(_char, Text.get(0, 214));
                            } else if (Math.abs(player.cx - _char.cx) > 60 || Math.abs(player.cy - _char.cy) > 60) {
                                Service.ServerMessage(_char, Text.get(0, 215));
                            } else if (player.clan.members.size() >= Clan.getMemMax(player.clan.level)) {
                                Service.ServerMessage(_char, Text.get(0, 203));
                            } else {
                                Service.clanPlease(player, _char.charID);
                                _char.aClanInvate.add(new Please(player.clan.clanId, player.charID, 15000));
                            }
                        }
                    }
                    break;
                }
                case -60: {
                    if (_char.tileMap == null || _char.statusMe == 14 || _char.user.player.isTrade || _char.clan == null || _char.ctypeClan <= 1 || _char.cClanName.isEmpty()) {
                        break;
                    }
                    if (!_char.isHuman) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                        break;
                    }
                    final Char player = _char.findCharInMap(msg.reader().readInt());
                    if (player == null) {
                        break;
                    }
                    if (player.clan != null && _char.cClanName.equals(player.cClanName)) {
                        Service.ServerMessage(_char, String.format(Text.get(0, 201), player.cName));
                        break;
                    }
                    if (!player.cClanName.isEmpty() || player.clan != null) {
                        Service.ServerMessage(_char, String.format(Text.get(0, 202), player.cName));
                        break;
                    }
                    synchronized (player.aClanInvate) {
                        if (player.findClanInvate(_char.charID) == null) {
                            Service.ServerMessage(_char, Text.get(0, 216));
                        } else if (Math.abs(_char.cx - player.cx) > 60 || Math.abs(_char.cy - player.cy) > 60) {
                            Service.ServerMessage(_char, String.format(Text.get(0, 217), player.cName));
                        } else {
                            final Clan clan = _char.clan;
                            synchronized (clan.LOCK_CLAN) {
                                if (clan.members.size() >= Clan.getMemMax(clan.level)) {
                                    Service.ServerMessage(_char, Text.get(0, 203));
                                } else {
                                    player.clan = clan;
                                    player.cClanName = clan.name;
                                    final Char char1 = player;
                                    final Member member2 = player.member;
                                    final byte b4 = 0;
                                    member2.typeClan = b4;
                                    char1.ctypeClan = b4;
                                    if (Clan.getMem(clan, player.cName) == null) {
                                        clan.members.add(player.member);
                                        final Clan clan3 = clan;
                                        ++clan3.numMember;
                                    }
                                    try {
                                        for (short i2 = 0; i2 < tileMap.numPlayer; ++i2) {
                                            if (tileMap.aCharInMap.get(i2).user != null && tileMap.aCharInMap.get(i2).user.session != null) {
                                                Service.clanInviteAccept(tileMap.aCharInMap.get(i2), player.charID, player.cClanName, player.ctypeClan);
                                            }
                                        }
                                    } catch (Exception ex5) {
                                    }
                                    Service.ServerMessage(_char, String.format(Text.get(0, 211), player.cName, clan.name));
                                    player.removeClanInvate(_char.charID);
                                }
                            }
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    private static void messageNotLogin(final Message msg, final Session_ME session) {
        try {
            final byte b = msg.reader().readByte();
            Util.log("messageNotLogin " + b);
            //System.out.println("messageNotLogin " + b);
            switch (b) {
                case -127: {
                    if (!session.login) {
                        String uname = msg.reader().readUTF();
                        String passw = msg.reader().readUTF();
                        String version = msg.reader().readUTF();
                        final String t1 = msg.reader().readUTF();
                        final String packages = msg.reader().readUTF();
                        final String random = msg.reader().readUTF();
                        final byte sv = msg.reader().readByte();
                        int vers = Integer.parseInt(version.replaceAll("\\.", ""));
                        msg.cleanup();
                        if (uname.equals("-1") && passw.equals("12345")) {
                            try {
                                final MySQL mySQL = new MySQL(1);
                                uname = getCreateUser();
                                passw = getCreatePass();
                                mySQL.stat.executeUpdate("INSERT INTO `user` (`user`,`password`, `created_at`) VALUES ('" + Util.strSQL(uname) + "','" + Util.strSQL(passw) + "','" + Util.strSQL(Util.toDateString(Date.from(Instant.now()))) + "');");
                                Service.createNewPlayer(session, uname, passw);
                                mySQL.close();
                                final User user = User.Login(session, uname, passw);
                                if (user != null) {
                                    session.user = user;
                                    session.login = true;
                                    Service.requestLogin(session);
                                    session.setDelayOut(120000, false);
                                    Util.log("Login Sucessfully " + uname + " passs " + passw);
                                } else {
                                    Util.log("Login Faild " + uname + " passs " + passw + " random " + random + " t1 " + t1 + " pack " + packages + " sv " + sv);
                                }
                                session.versionNja = vers;
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            final User user = User.Login(session, uname, passw);
                            if (user != null) {
                                session.user = user;
                                session.login = true;
                                Service.requestLogin(session);
                                session.setDelayOut(120000, false);
                                Util.log("Login Sucessfully " + uname + " passs " + passw);
                            } else {
                                Util.log("Login Faild " + uname + " passs " + passw + " random " + random + " t1 " + t1 + " pack " + packages + " sv " + sv);
                            }
                            session.versionNja = vers;
                            break;
                        }
                    }
                    Service.ClearCache(session);
                    session.setDelayOut(10, true);
                    break;
                }
                case -125: {
                    if (!session.login) {
                        session.client_type = msg.reader().readByte();
                        session.zoomLevel = msg.reader().readByte();
                        session.isGPS = msg.reader().readBoolean();
                        session.width = msg.reader().readInt();
                        session.height = msg.reader().readInt();
                        session.isQwert = msg.reader().readBoolean();
                        session.isTouch = msg.reader().readBoolean();
                        session.plastfrom = msg.reader().readUTF();
                        session.versionIp = msg.reader().readInt();
                        msg.reader().readByte();
                        session.languageId = msg.reader().readByte();
                        session.provider = msg.reader().readInt();
                        session.agent = msg.reader().readUTF();
                        Util.log("Client type " + session.client_type + " zoomlevel " + session.zoomLevel + " width " + session.width + " height " + session.height);
                        System.out.println("Client type " + session.client_type + " zoomlevel " + session.zoomLevel + " width " + session.width + " height " + session.height);
                        break;
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    private static void messageNotMap(final Message msg, final User user, Char _char) {
        try {
            final byte b = msg.reader().readByte();
            Util.log("messageNotMap " + b);
            //System.out.println("messageNotMap " + b);
            switch (b) {
                case -126: {
                    if (_char != null) {
                        break;
                    }
                    _char = User.selectCharToPlay(user, msg.reader().readUTF());
                    if (_char != null) {
                        Top.sortTop(1, _char.cName, null, _char.cLevel, null);
                        Top.sortTop(4, _char.cName, null, _char.user.player.epoint, null);
                        Map map = MapServer.getMapServer(_char.mapId);
                        if (map == null || map.isMonsterMap() || _char.statusMe == 14 || _char.statusMe == 5) {
                            map = MapServer.getMapServer(_char.mapLTDId);
                            _char.cx = map.template.goX;
                            _char.cy = map.template.goY;
                        }
                        _char.statusMe = 1;
                        final TileMap tileMap = map.getSlotZone(_char);
                        if (tileMap != null) {
                            final Calendar old = Util.Calendar(_char.lastTimeOnline);
                            final Calendar now = Util.Calendar(System.currentTimeMillis());
                            Service.clearForcus(_char);
                            if (now.get(1) > old.get(1) || now.get(6) > old.get(6)) {
                                for (short i = 0; i < user.player.ItemBag.length; ++i) {
                                    final Item item = user.player.ItemBag[i];
                                    if (item != null && item.isTypeMounts() && item.isLock) {
                                        for (short j = 0; j < item.options.size(); ++j) {
                                            final ItemOption option = item.options.get(j);
                                            if (option != null && option.optionTemplate.id == 66) {
                                                final ItemOption itemOption = option;
                                                itemOption.param -= 100;
                                                if (option.param < 0) {
                                                    option.param = 0;
                                                }
                                            }
                                        }
                                    }
                                }
                                for (short i = 0; i < user.player.ItemBox.length; ++i) {
                                    final Item item = user.player.ItemBox[i];
                                    if (item != null && item.isTypeMounts() && item.isLock) {
                                        for (short j = 0; j < item.options.size(); ++j) {
                                            final ItemOption option = item.options.get(j);
                                            if (option != null && option.optionTemplate.id == 66) {
                                                final ItemOption itemOption2 = option;
                                                itemOption2.param -= 100;
                                                if (option.param < 0) {
                                                    option.param = 0;
                                                }
                                            }
                                        }
                                    }
                                }
                                for (short i = 0; i < user.player.ItemBox.length; ++i) {
                                    final Item item = user.player.ItemBox[i];
                                    if (item != null && item.isTypeMounts() && item.isLock) {
                                        for (short j = 0; j < item.options.size(); ++j) {
                                            final ItemOption option = item.options.get(j);
                                            if (option != null && option.optionTemplate.id == 66) {
                                                final ItemOption itemOption2 = option;
                                                itemOption2.param -= 100;
                                                if (option.param < 0) {
                                                    option.param = 0;
                                                }
                                            }
                                        }
                                    }
                                }
                                for (short i = 0; i < _char.ItemMounts.length; ++i) {
                                    final Item item = _char.ItemMounts[i];
                                    if (item != null && item.isTypeMounts() && item.isLock) {
                                        for (short j = 0; j < item.options.size(); ++j) {
                                            final ItemOption option = item.options.get(j);
                                            if (option != null && option.optionTemplate.id == 66) {
                                                final ItemOption itemOption3 = option;
                                                itemOption3.param -= 100;
                                                if (option.param < 1) {
                                                    option.param = 1;
                                                }
                                            }
                                        }
                                    }
                                }
                                for (int k = _char.ItemUseLimit.size() - 1; k >= 0; --k) {
                                    final Limit limit = _char.ItemUseLimit.get(k);
                                    if (limit != null && Limit.getisItemDelNextDay((short) limit.id)) {
                                        _char.ItemUseLimit.remove(k);
                                    }
                                }
                                _char.countPB = 1;
                                _char.countTaskHangNgay = 0;
                                _char.countTaskTaThu = 2;
                                _char.taskHangNgay[6] = 0;
                                _char.taskTaThu[6] = 2;
                                DanhVong.reset(_char);
                                _char.lastTimeOnline = System.currentTimeMillis();
                            }
                            Service.MELoadALL(_char);
                            Service.typeActive(_char);
                            try {
                                for (byte l = 0; l < _char.aEff.size(); ++l) {
                                    final Effect effect = _char.aEff.get(l);
                                    if (effect != null) {
                                        Service.MeAddEfect(_char, effect);
                                    }
                                }
                            } catch (Exception ex) {
                            }
                            final short mobTemplateId = Mob.itemMob(_char.ItemBody[10]);
                            if (mobTemplateId > 0) {
                                final Char char1 = _char;
                                final Mob mobMe = new Mob(_char.tileMap, (short) (-1), mobTemplateId, (byte) 1, 0, 0, _char.cx, (short) (_char.cy - 40), (byte) 5, (byte) 0, Mob.arrMobTemplate[mobTemplateId].isBoss, -1);
                                char1.mobMe = mobMe;
                                final Mob mob = mobMe;
                                Service.MELoadThuNuoi(_char, mob);
                            }
                            final short mobViThuId = Mob.itemMob(_char.arrItemViThu[4]);
                            if (mobViThuId > 0) {
                                final Char char1 = _char;
                                final Mob mobVithu = new Mob(_char.tileMap, (short) (-1), mobViThuId, (byte) 1, 0, 0, _char.cx, (short) (_char.cy - 40), (byte) 5, (byte) 0, Mob.arrMobTemplate[mobViThuId].isBoss, -1);
                                char1.mobViThu = mobVithu;
                                Service.onChangeVithu(_char, _char.charID, mobVithu);
                            }
                            tileMap.Join(_char);
                            if (_char.ctaskIndex != -1 && _char.ctaskId < GameScr.tasks.length) {
                                Service.getTask(_char);
                                Task.requestLevel(_char);
                            }
                            if (_char.ItemMounts[4] != null) {
                                Service.LoadThuCuoi(_char, _char.charID, _char.ItemMounts);
                            }
                            if (_char.arrItemViThu[4] != null) {
                                Service.onVithuInfo(_char, _char.charID, _char.arrItemViThu);
                            }
                            final String str = new String(NinjaUtil.getFile("assets/text/Introduce.txt"));
                            final String[] arrText = str.split("\n", 2);
                            //Service.AlertMessage(_char, arrText[0], String.format(arrText[1], (int)(Client.sizeUser() * 1.5)));
                            _char.user.session.setDelayOut(600000, false);
                        } else {
                            GameCanvas.startOKDlg(user.session, Text.get(0, 9));
                        }
                        break;
                    }
                    break;
                }
                case -125: {
                    if (_char == null) {
                        final Player newChar = User.Create(user.session, msg.reader().readUTF().toLowerCase(), msg.reader().readByte(), msg.reader().readByte());
                        if (newChar != null) {
                            final Player[] chars = new Player[user.players.length];
                            for (byte num = 1; num < chars.length; ++num) {
                                chars[num] = user.players[num - 1];
                            }
                            chars[0] = newChar;
                            user.players = chars;
                            ++user.numPlayer;
                            user.flush();
                            Service.initSelectChar(user);
                        }
                        break;
                    }
                    break;
                }
                case -122: {
                    Service.sendData(user);
                    break;
                }
                case -121: {
                    Service.sendMap(user);
                    break;
                }
                case -120: {
                    Service.sendSkill(user);
                    break;
                }
                case -119: {
                    Service.sendItem(user);
                    break;
                }
                case -115: {
                    SmallImage.reciveImage(user, msg.reader().readInt());
                    break;
                }
                case -114: {
                    if (_char.clan != null) {
                        Service.requestClanLog(_char, _char.clan);
                        break;
                    }
                    break;
                }
                case -113: {
                    if (_char.clan != null) {
                        Service.requestClanInfo(_char, _char.clan);
                        break;
                    }
                    break;
                }
                case -112: {
                    if (_char.clan != null) {
                        Service.requestClanMember(_char, _char.clan);
                        break;
                    }
                    break;
                }
                case -111: {
                    if (_char.clan != null) {
                        Service.requestClanItem(_char, _char.clan);
                        break;
                    }
                    break;
                }
                case -109: {
                    if (_char != null && _char.tileMap != null) {
                        Service.requestMapTemplate(_char, msg.reader().readUnsignedByte());
                        break;
                    }
                    break;
                }
                case -108: {
                    if (_char != null && _char.tileMap != null) {
                        if (user.session.versionNja < 203) {
                            Service.requestModTemplate(_char, msg.reader().readUnsignedByte());
                        } else {
                            Service.requestModTemplate(_char, msg.reader().readUnsignedShort());
                        }
                        break;
                    }
                    break;
                }
                case -101: {
                    Service.initSelectChar(user);
                    break;
                }
                case -95: {
                    if (_char.clan != null && _char.tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade && _char.member.typeClan > 2) {
                        final String str2 = msg.reader().readUTF();
                        if (str2.length() > 200) {
                            GameCanvas.startOKDlg(_char.user.session, String.format(Text.get(0, 198), 200));
                        } else if (!str2.isEmpty()) {
                            _char.clan.alert = String.format(Text.get(0, 199), _char.cName, str2);
                        } else {
                            _char.clan.alert = "";
                        }
                        Service.requestClanInfo(_char, _char.clan);
                        break;
                    }
                    break;
                }
                case -94: {
                    if (_char.clan != null && _char.tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade && _char.member.typeClan > 3) {
                        final String name = msg.reader().readUTF();
                        final byte type = msg.reader().readByte();
                        if (!name.equals(_char.cName) && (type == 3 || type == 2 || type == 0)) {
                            final Clan clan = _char.clan;
                            synchronized (clan.LOCK_CLAN) {
                                if (type == 0 || (type == 3 && clan.assist_name.isEmpty()) || (type == 2 && (clan.elder1_name.isEmpty() || clan.elder2_name.isEmpty() || clan.elder3_name.isEmpty() || clan.elder4_name.isEmpty() || clan.elder5_name.isEmpty()))) {
                                    for (short i = 0; i < clan.members.size(); ++i) {
                                        final Member mem = clan.members.get(i);
                                        if (mem != null && mem.cName.equals(name) && ((type == 0 && mem.typeClan > 1) || (type != 0 && mem.typeClan < 2))) {
                                            clan.changeTypeClan(name, type);
                                            if (type == 3) {
                                                clan.ClanMessage(String.format(Text.get(0, 221), name));
                                            } else if (type == 2) {
                                                clan.ClanMessage(String.format(Text.get(0, 223), name));
                                            } else if (type == 0) {
                                                clan.ClanMessage(String.format(Text.get(0, 222), name));
                                            }
                                            Service.requestClanMember(_char, clan);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                    break;
                }
                case -93: {
                    if (_char.clan != null && _char.tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade && _char.member.typeClan > 2) {
                        final String cName = msg.reader().readUTF();
                        final Clan clan2 = _char.clan;
                        synchronized (clan2.LOCK_CLAN) {
                            for (short m = 0; m < clan2.members.size(); ++m) {
                                final Member mem2 = clan2.members.get(m);
                                if (mem2 != null && ((_char.ctypeClan == 3 && mem2.typeClan < 2) || _char.ctypeClan == 4) && mem2.cName.equals(cName) && !mem2.cName.equals(_char.cName)) {
                                    final int coinDown = Clan.arrCoinOutClan[mem2.typeClan];
                                    clan2.writeLog(mem2.cName, 1, coinDown, Util.toDateString(Date.from(Instant.now())));
                                    clan2.moveMember(cName, String.format(Text.get(0, 219), clan2.name));
                                    Service.requestClanMember(_char, clan2);
                                    clan2.ClanMessage(String.format(Text.get(0, 218), mem2.cName, coinDown));
                                    clan2.updateCoin(-coinDown);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    break;
                }
                case -92: {
                    if (_char.clan != null && _char.tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade && _char.member.typeClan < 4) {
                        final Clan clan3 = _char.clan;
                        synchronized (clan3.LOCK_CLAN) {
                            final int coinDown2 = Clan.arrCoinOutClan[_char.member.typeClan];
                            if (coinDown2 > _char.user.player.xu) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 27));
                            } else {
                                _char.user.player.upCoin(-coinDown2, (byte) 2);
                                clan3.moveMember(_char.cName, String.format(Text.get(0, 209), _char.cName, coinDown2));
                                clan3.ClanMessage(String.format(Text.get(0, 220), _char.cName, _char.cName, coinDown2));
                            }
                        }
                        break;
                    }
                    break;
                }
                case -91: {
                    if (_char.clan != null && _char.tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade && _char.member.typeClan > 2) {
                        final Clan clan3 = _char.clan;
                        synchronized (clan3.LOCK_CLAN) {
                            final int expNext = Clan.getexpNext(clan3.level);
                            final int coinDown3 = Clan.getCoinUp(clan3.level);
                            if (expNext > clan3.exp) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 226));
                            } else if (clan3.coin < coinDown3) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 227));
                            } else {
                                clan3.updateCoin(-coinDown3);
                                clan3.updateExp(-expNext);
                                final Clan clan4 = clan3;
                                ++clan4.level;
                                final Clan clan5 = clan3;
                                clan5.maxMember += 5;
                                clan3.writeLog(_char.cName, 5, coinDown3, Util.toDateString(Date.from(Instant.now())));
                                Top.sortTop(2, clan3.name, clan3.main_name, clan3.level, new int[]{clan3.numMember, clan3.maxMember});
                                Service.requestClanInfo(_char, clan3);
                                clan3.ClanMessage(String.format(Text.get(0, 195), clan3.level));
                            }
                        }
                        break;
                    }
                    break;
                }
                case -90: {
                    if (_char.clan == null || _char.tileMap == null || _char.statusMe == 14 || _char.user.player.isTrade) {
                        break;
                    }
                    if (MapServer.notTrade(_char.tileMap.mapID)) {
                        Service.ServerMessage(_char, Text.get(0, 300));
                        break;
                    }
                    final int coin = msg.reader().readInt();
                    if (coin > 0 && coin <= _char.user.player.xu) {
                        _char.clan.inputCoinClan(_char, coin);
                        _char.user.player.upCoin(-coin, (byte) 2);
                    }
                    break;
                }
                case -88: {
                    if (_char == null || _char.tileMap == null || _char.statusMe == 14 || _char.user.player.isTrade || TileMap.NPCNear(_char, (short) 6) == null) {
                        break;
                    }
                    if (!_char.isHuman) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                        break;
                    }
                    try {
                        final Item item2 = _char.user.player.ItemBag[msg.reader().readByte()];
                        final Item item3 = _char.user.player.ItemBag[msg.reader().readByte()];
                        final Item item4 = _char.user.player.ItemBag[msg.reader().readByte()];
                        if (item2 != null && item3 != null && item4 != null && item2.upgrade > 0 && item3.upgrade == 0 && item4.isItemConvertUpgrade()) {
                            Player.doConvertUpgrade(_char, item2, item3, item4);
                        }
                    } catch (Exception e2) {
                    }
                    break;
                }
                case -87: {
                    if (_char.clan.clanManor != null) {
                        if (_char.clan.clanManor.isBaoDanh) {
                            String name = msg.reader().readUTF();
                            Player.inviteManor(_char, name);
                        } else {
                            Service.ServerMessage(_char, "Đã hết giờ mời thành viên.");
                        }
                    }
                    break;
                }
                case -85: {
                    if (_char != null && _char.tileMap != null && !_char.user.player.isTrade) {
                        Player.inputNumSplit(_char.user.player, msg.reader().readByte(), msg.reader().readInt());
                        break;
                    }
                    break;
                }
                case -82: {
                    if (_char == null || _char.tileMap == null || _char.pointPB <= 0 || _char.statusMe == 14 || _char.user.player.isTrade || TileMap.NPCNear(_char, (short) 0) == null) {
                        break;
                    }
                    if (!_char.isHuman) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                        break;
                    }
                    final int count = BackCave.reWaed(_char.pointPB, _char.timeFinishCave, _char.countPartyPB);
                    if (_char.user.player.ItemBagIndexNull() == -1) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                    } else {
                        _char.pointPB = 0;
                        _char.pointHoatDong++;
                        short templateId = 272;
                        if (_char.cLevel >= 90) {
                            templateId = 647;
                        } else if (_char.cLevel >= 50) {
                            templateId = 282;
                        }
                        final Item item5 = new Item(null, templateId, count, -1, true, (byte) 0, 0);
                        if (item5.template.isUpToUp) {
                            _char.user.player.ItemBagAddQuantity(item5);
                        } else {
                            _char.user.player.ItemBagAdd(item5);
                        }
                        Top.sortTop(3, _char.cName, item5.template.name, count, null);
                    }
                    break;
                }
                case -79: {
                    if (_char != null && _char.tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade && TileMap.NPCNear(_char, (short) 25) != null) {
                        ChienTruong.reward(_char, ChienTruong.chien_truong);
                        break;
                    }
                    break;
                }
                case -72: {
                    if (_char == null || _char.tileMap == null || _char.statusMe == 14 || _char.user.player.isTrade || TileMap.NPCNear(_char, (short) 30) == null) {
                        break;
                    }
                    if (!_char.isHuman) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 310));
                        break;
                    }
                    LatHinh.selectCard(_char, msg.reader().readByte());
                    break;
                }
                case -62: {
                    if (_char.clan != null && _char.tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade && _char.member.typeClan > 2) {
                        final Clan clan3 = _char.clan;
                        synchronized (clan3.LOCK_CLAN) {
                            final int coinDown2 = clan3.getCoinOpen();
                            if (clan3.itemLevel < 5) {
                                if (clan3.level < 5 * (clan3.itemLevel + 1)) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 228));
                                } else if (clan3.coin < coinDown2) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 227));
                                } else {
                                    clan3.updateCoin(-coinDown2);
                                    final Clan clan6 = clan3;
                                    ++clan6.itemLevel;
                                    Service.openClanItem(_char, (byte) clan3.itemLevel);
                                    Service.requestClanInfo(_char, clan3);
                                    clan3.ClanMessage(String.format(Text.get(0, 196), clan3.itemLevel));
                                }
                            }
                        }
                        break;
                    }
                    break;
                }
                case -61: {
                    if (_char.clan != null && _char.tileMap != null && _char.statusMe != 14 && !_char.user.player.isTrade && _char.member.typeClan > 2) {
                        final int indexSelect = msg.reader().readByte();
                        final Player player = Client.getPlayer(msg.reader().readUTF());
                        if (player != null && player.ItemBagIndexNull() != -1 && player.clan != null && player.clan.clanId == _char.clan.clanId) {
                            final Clan clan = _char.clan;
                            synchronized (clan.LOCK_CLAN) {
                                try {
                                    final Item item6 = clan.items.get(indexSelect);
                                    if (item6 != null) {
                                        final Item it = item6.clone();
                                        final Item item7 = item6;
                                        --item7.quantity;
                                        if (item6.quantity <= 0) {
                                            clan.items.remove(indexSelect);
                                        }
                                        it.isLock = true;
                                        it.quantity = 1;
                                        final Item item8 = it;
                                        item8.expires += System.currentTimeMillis();
                                        Service.requestClanItem(_char, clan);
                                        player.ItemBagAdd(it);
                                    }
                                } catch (Exception ex2) {
                                }
                            }
                        }
                        break;
                    }
                    break;
                }
                case 122: {
                    if (_char == null || _char.tileMap == null) {
                        break;
                    }
                    final byte type = msg.reader().readByte();
                    final short id = (short) msg.reader().readUnsignedByte();
                    GameCanvas.sendDataEffAuto(_char, type, id);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static String getCreateUser() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        while (true) {
            String randomUP = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            randomUP = "tmpusr_" + randomUP;
            try {
                final MySQL mySQL = new MySQL(1);
                try {
                    ResultSet red = mySQL.stat.executeQuery("SELECT * FROM `user` WHERE (`user` LIKE '" + Util.strSQL(randomUP) + "') LIMIT 1;");
                    if (!red.first()) {
                        return randomUP;
                    }
                } finally {
                    mySQL.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String getCreatePass() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String pass = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return pass;
    }
}
