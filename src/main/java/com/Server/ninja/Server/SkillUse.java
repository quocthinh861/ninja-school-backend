 package com.Server.ninja.Server;

import java.util.ArrayList;
import com.Server.ninja.option.ItemOption;

public class SkillUse
{
    protected static void PlayerAttack(final TileMap tileMap, final Skill skill, final Char myChar, final Char _char, final Mob[] arrMob, final Char[] arrChar, final byte type) {
        if (skill != null && skill.template.type == 2) {
            myBuff(tileMap, _char, myChar, skill, (byte)(-1));
        }
        else if (_char.statusMe != 14 && !_char.isIce && !_char.isWind && !_char.isLockAttack && skill != null && skill.lastTimeUseThisSkill <= System.currentTimeMillis()) {
            try {
                tileMap.lock.lock("Attack ");
                try {
                    if (type == 0) {
                        _char.upMP(-skill.manaUse);
                    }
                    if (arrMob != null && arrChar != null) {
                        try {
                            for (short i = 0; i < tileMap.aCharInMap.size(); ++i) {
                                if (tileMap.aCharInMap.get(i).user != null && tileMap.aCharInMap.get(i).user.session != null && tileMap.aCharInMap.get(i).charID != _char.charID) {
                                    Service.PlayerAttack(tileMap.aCharInMap.get(i), _char.charID, skill.template.id, arrMob, arrChar);
                                }
                            }
                        }
                        catch (Exception e2) {}
                    }
                    else if (arrMob != null) {
                        try {
                            for (short i = 0; i < tileMap.aCharInMap.size(); ++i) {
                                if (tileMap.aCharInMap.get(i).user != null && tileMap.aCharInMap.get(i).user.session != null && tileMap.aCharInMap.get(i).charID != _char.charID) {
                                    Service.PlayerAttack(tileMap.aCharInMap.get(i), _char.charID, skill.template.id, arrMob);
                                }
                            }
                        }
                        catch (Exception e2) {}
                    }
                    else if (arrChar != null) {
                        try {
                            for (short i = 0; i < tileMap.aCharInMap.size(); ++i) {
                                if (tileMap.aCharInMap.get(i).user != null && tileMap.aCharInMap.get(i).user.session != null && tileMap.aCharInMap.get(i).charID != _char.charID) {
                                    Service.PlayerAttack(tileMap.aCharInMap.get(i), _char.charID, skill.template.id, arrChar);
                                }
                            }
                        }
                        catch (Exception ex) {}
                    }
                    if (skill.template.type == 3 && arrMob != null && arrMob.length > 0) {
                        SkillUse(_char.tileMap, _char, skill, arrMob[0], null);
                    }
                    else if (skill.template.id == 24) {
                        if (arrMob != null && arrMob.length > 0) {
                            SkillUse(_char.tileMap, _char, skill, arrMob[0], null);
                        }
                        else if (arrChar != null && arrChar.length > 0) {
                            SkillUse(_char.tileMap, _char, skill, null, arrChar[0]);
                        }
                    }
                    else if (skill.template.id == 40 && arrMob != null && arrMob.length > 0) {
                        SkillUse(_char.tileMap, _char, skill, arrMob[0], null);
                    }
                    else {
                        if (skill.template.id == 42) {
                            if (arrMob != null && arrMob.length > 0) {
                                SkillUse(_char.tileMap, _char, skill, arrMob[0], null);
                            }
                            else if (arrChar != null && arrChar.length > 0) {
                                SkillUse(_char.tileMap, _char, skill, null, arrChar[0]);
                            }
                        }
                        int addpercentdame = 0;
                        synchronized (_char.aEff) {
                            for (short j = 0; j < _char.aEff.size(); ++j) {
                                final Effect effect = _char.aEff.get(j);
                                if (effect != null && (effect.template.id == 15 || effect.template.id == 16)) {
                                    if (effect.template.id == 15) {
                                        addpercentdame += _char.PramSkillTotal(61);
                                    }
                                    Char.removeEffect(_char, effect.template.id);
                                    --j;
                                }
                            }
                        }
                        if (arrChar != null) {
                            for (byte k = 0; k < arrChar.length; ++k) {
                                final Char player = arrChar[k];
                                if (player != null && (myChar.isTest || (myChar.cTypePk == 4 && player.cTypePk == 5) || (myChar.cTypePk == 5 && player.cTypePk == 4) || (myChar.cTypePk == 1 && player.cTypePk == 1 && !MapServer.notCombat(myChar.tileMap.mapID)) || ((player.cTypePk == 3 || myChar.cTypePk == 3) && !MapServer.notCombat(myChar.tileMap.mapID) && (!player.isNotPK() || _char.isNhanban)) || (player.cTypePk == 4 && _char.cTypePk == 5) || (player.cTypePk == 5 && _char.cTypePk == 4) || (_char.isTest && player.isTest && _char.testCharID == player.charID && _char.charID == player.testCharID) || (_char.KillCharId == player.charID && (!player.isNotPK() || _char.isNhanban)) || player.KillCharId == _char.charID)) {
                                    int fantal = _char.cFatal();
                                    if (fantal > 1000) {
                                        fantal = 1000;
                                    }
                                    final boolean flag = Util.nextInt(2500) < fantal || addpercentdame > 0;
                                    int dame = _char.cDame(player.sys(), addpercentdame, player.dameDownSys(_char.sys()), player.ResFire(), player.ResIce(), player.ResWind(), player.cdameDown(), 0, player.cMiss(), flag, player.ResFantalDame(), player.FantalDameDown());
                                    if (Math.abs(myChar.cx - player.cx) > skill.dx + Util.nextInt(40) + k * 30 || Math.abs(myChar.cy - player.cy) > skill.dy + Util.nextInt(40) + k * 10) {
                                        dame = 0;
                                    }
                                    dame += dame * addpercentdame / 100;
                                    int newdame = Util.nextInt(dame * 9 / 10, dame) / 12;
                                    if (!_char.isHuman) {
                                        newdame = newdame * _char.percentPow / 100; // dame phan than
                                    }
                                    if (dame > 0 && newdame <= 0) {
                                        newdame = 1;
                                    }
                                    if (player.isFire) {
                                        newdame *= 2;
                                    }
                                    if (newdame > 0 && skill.template.id == 42) {
                                        SkillUse(_char.tileMap, _char, skill, null, player);
                                    }
                                    if ((player.cTypePk == 4 && _char.cTypePk == 5) || (player.cTypePk == 5 && _char.cTypePk == 4)) {
                                        AttackPlayer(tileMap, myChar, _char, player, newdame, flag, (byte)3);
                                    }
                                    else {
                                        AttackPlayer(tileMap, myChar, _char, player, newdame, flag, (byte)1);
                                    }
                                }
                            }
                        }
                        if (arrMob != null) {
                            for (byte k = 0; k < arrMob.length; ++k) {
                                final Mob mob = arrMob[k];
                                if (mob != null) {
                                    int fantal = _char.cFatal();
                                    if (fantal > 1000) {
                                        fantal = 1000;
                                    }
                                    final boolean flag = Util.nextInt(2000) < fantal || addpercentdame > 0;
                                    int dame = _char.cDame(mob.sys, addpercentdame, 0, 0, 0, 0, 0, 0, 0, flag, 0, 0);
                                    if (Math.abs(myChar.cx - mob.x) > skill.dx + Util.nextInt(40) + k * 30 || Math.abs(myChar.cy - mob.y) > skill.dy + Util.nextInt(40) + k * 10) {
                                        dame = 0;
                                    }
                                    else {
                                        dame += dame * addpercentdame / 100;
                                        if (!_char.isHuman) {
                                            dame = dame * _char.percentPow / 100;
                                        }
                                        if (mob.isBoss && Math.abs(mob.level - _char.cLevel) > 15 && !tileMap.map.isBackCaveMap() && mob.templateId != 230) {
                                            dame = 1;
                                        }
                                        if (mob.templateId == 230 && dame > 100) {
                                            dame /= 5;
                                        }
                                        if (mob.isFire) {
                                            dame *= 2;
                                        }
                                        if (dame > 0) {
                                            if (k == 0) {
                                                _char.mobFocus = mob;
                                            }
                                            if (skill.template.id == 42) {
                                                SkillUse(_char.tileMap, _char, skill, mob, null);
                                            }
                                        }
                                    }
                                    if (tileMap.mapID == 82 && Util.nextInt(10) < 6) { // cửa né tránh
                                        dame = 0;
                                    }
                                    if (tileMap.mapID == 83 && Util.nextInt(10) < 6) { // cửa phản đòn
                                        Mob.MobAttackPlayer(mob, Util.nextInt(5500, 10000), _char);
                                    }
                                    AttackMob(tileMap, myChar, _char, mob, Util.nextInt(dame * 9 / 10, dame), flag, (byte)0);
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
            skill.lastTimeUseThisSkill = System.currentTimeMillis() + skill.coolDown;
        }
    }
    
    private static void AttackPlayer(final TileMap tileMap, final Char myChar, final Char _char, final Char player, int dame, final boolean flag, final byte type) {
        if (player != null && player.statusMe != 14) {
            if (myChar.isTest && myChar.testCharID == player.charID && player.isTest && player.testCharID == myChar.charID && dame > player.cHP) {
                player.endTest(player.cHP);
            }
            else {
                player.upHP(-dame);
            }
            if (player.cHP <= 0) {
                dame += player.cHP;
                if (myChar.KillCharId == player.charID) {
                    myChar.upPk(2);
                    if (player.user != null) {
                        player.user.player.addEnemis(myChar.cName);
                    }
                }
                else if (myChar.cTypePk == 3) {
                    myChar.upPk(1);
                    if (player.user != null) {
                        player.user.player.addEnemis(myChar.cName);
                    }
                }
                player.upDie(type);
                if (tileMap.map.isChienTruong()) {
                    Service.ServerMessage(myChar, String.format(Text.get(0, 332), player.cName));
                    Service.ServerMessage(player, String.format(Text.get(0, 333), myChar.cName));
                    myChar.user.player.upPointChientruong((short)5);
                }
                if (tileMap.map.isWarClanMap()) {
                    Service.ServerMessage(myChar, String.format(Text.get(0, 332), player.cName));
                    Service.ServerMessage(player, String.format(Text.get(0, 333), myChar.cName));
                    WarClan.upPointWarClan(_char, (short) 5);
                }
            }
            try {
                if (flag) {
                    dame *= -1;
                }
                for (short i = 0; i < tileMap.aCharInMap.size(); ++i) {
                    if (tileMap.aCharInMap.get(i).user != null && tileMap.aCharInMap.get(i).user.session != null) {
                        Service.HavePlayerAttack(tileMap.aCharInMap.get(i), player, dame);
                    }
                }
            }
            catch (Exception ex) {}
            if (player.statusMe != 14 && type > 0 && dame > 0) {
                if (_char.percentFire() > Util.nextInt(100)) {
                    int timeFire = _char.timeFire();
                    timeFire -= player.cFireDownTime();
                    Char.setEffect(player, (byte)5, -1, timeFire, (short)0, null, (byte)0);
                }
                if (_char.percentIce() > Util.nextInt(100)) {
                    int timeIce = _char.timeIce();
                    timeIce -= player.cIceDownTime();
                    Char.setEffect(player, (byte)6, -1, timeIce, (short)0, null, (byte)0);
                }
                if (_char.percentWind() > Util.nextInt(100)) {
                    int timeWind = _char.timeWind();
                    timeWind -= player.cWindDownTime();
                    Char.setEffect(player, (byte)7, -1, timeWind, (short)0, null, (byte)0);
                }
                if (player.PramSkillTotal(69) > Util.nextInt(100)) {
                    int timeIce = player.PramSkillTotal(70);
                    timeIce -= player.cIceDownTime();
                    Char.setEffect(myChar, (byte)6, -1, timeIce, (short)0, null, (byte)0);
                }
            }
        }
    }
    
    protected static void AttackMob(final TileMap tileMap, final Char myChar, final Char _char, final Mob mob, int dame, final boolean flag, final byte type) {
        ItemMap itemmap = null;
        if (mob != null && (mob.playerId == -1 || mob.playerId == myChar.user.player.playerId)) {
            synchronized (mob.LOCK) {
                if (mob.templateId == 82 && _char.getEffId((byte) 23) == null) {
                    Service.ServerMessage(_char, "Hãy đi tìm và sử dụng bí dược để khai sáng mới có thể đánh.");
                    return;
                }
                if (mob.injureThenDie && _char.user != null && _char.user.session != null) {
                    Service.MobDie(_char, mob, 0, flag, null);
                }
                if (!mob.injureThenDie && mob.status != 0 && mob.status != 1) {
                    if (dame > 0) {
                        if (mob.templateId != 0) {
                            mob.hp -= dame;
                        }
                        else {
                            mob.hp -= mob.maxHp / 5;
                        }
                        if (mob.hp <= 0) {
                            dame += mob.hp;
                        }
                        if (myChar.user != null) {
                            mob.CharFight(myChar.charID, dame);
                        }
                    }
                    if (mob.hp <= 0) {
                        mob.startDie();
                        if (_char.mobFocus != null && _char.mobFocus.mobId == mob.mobId) {
                            _char.mobFocus = null;
                        }
                        final int playerId = mob.sortPlayerFight();
                        if (Task.isExtermination(myChar, mob)) {
                            myChar.uptaskMaint();
                            if (myChar.party != null) {
                                for (short i = 0; i < tileMap.numPlayer; ++i) {
                                    final Char player = tileMap.aCharInMap.get(i);
                                    if (player != null && player.user != null && player.party != null && player.charID != myChar.charID && player.party.partyId == myChar.party.partyId && player.ctaskId == myChar.ctaskId && player.ctaskIndex == myChar.ctaskIndex) {
                                        player.uptaskMaint();
                                    }
                                }
                            }
                        }
                        if (!tileMap.map.isBackCaveMap() && myChar.isHuman) {
                            if (myChar.nvDV[1] == 0 && Math.abs(myChar.cLevel - mob.level) <= 10 && mob.levelBoss == 1) {
                                myChar.nextNVDV();
                            }
                            else if (myChar.nvDV[1] == 1 && Math.abs(myChar.cLevel - mob.level) <= 10 && mob.levelBoss == 2) {
                                myChar.nextNVDV();
                            }
                            else if (myChar.nvDV[1] == 2 && Math.abs(myChar.cLevel - mob.level) <= 10) {
                                myChar.nextNVDV();
                            }
                            if (_char.taskHangNgay[5] == 1 && mob.templateId == _char.taskHangNgay[3] && _char.taskHangNgay[0] == 0 && _char.taskHangNgay[1] < _char.taskHangNgay[2] && (mob.levelBoss == 0)) {
                                if (_char.party != null) {
                                    for (int i = 0; i < _char.party.aChar.size(); i++) {
                                        Char c = _char.party.aChar.get(i);
                                        Char c1 = _char.findCharInMap(c.charID);
                                        if (c1 != null) {
                                            _char.taskHangNgay[1]++;
                                            Service.updateTaskOrder(c1, (byte)0);
                                        }
                                    }
                                } else {
                                    _char.taskHangNgay[1]++;
                                    Service.updateTaskOrder(_char, (byte)0);
                                }
                            }
                            if ((_char.taskTaThu[5] == 1) && (mob.templateId == _char.taskTaThu[3]) && (mob.levelBoss == 3) && (_char.taskTaThu[0] == 1) && (_char.taskTaThu[1] < _char.taskTaThu[2])) {
                                if (_char.party != null) {
                                    for (int i = 0; i < _char.party.aChar.size(); i++) {
                                        Char c = _char.party.aChar.get(i);
                                        Char c1 = _char.findCharInMap(c.charID);
                                        if (c1 != null) {
                                            c1.taskTaThu[1]++;
                                            Service.updateTaskOrder(c1, (byte)1);
                                        }
                                    }
                                } else {
                                    _char.taskTaThu[1]++;
                                    Service.updateTaskOrder(_char, (byte) 1);
                                }
                            }
                        }
                        if (tileMap.map.isClanManor() && mob.templateId == 82) {
                            Item item = null;
                            ClanManor manor = _char.clan.clanManor;
                            for (int i = 0; i < 20; i++) {
                                if (Util.nextInt(10) < 6) {
                                    item = new Item(null, (short) 12, 50000, -1, true, (byte)0, 0);
                                } else if (Util.nextInt(10) < 6) {
                                    item = new Item(null, (short) Util.nextInt(7,9), 1, -1, true, (byte)0, 0);
                                } else if (Util.nextInt(20) < 5) {
                                    item = new Item(null, (short) Util.nextInt(788,789), 1, -1, false, (byte)0, 0);
                                }
                                if (item != null) {
                                    itemmap = new ItemMap(item, playerId, 65000, (short) Util.nextInt((mob.x -30),(mob.x + 30)), mob.y, false);
                                    tileMap.ItemMapADD(itemmap);
                                }
                            }
                            manor.timeLength = (int)(System.currentTimeMillis() / 1000L + 60);
                            for (int i = 0; i < manor.memberInManor.size(); i++) {
                                Char c = manor.memberInManor.get(i);
                                c.user.player.upCoin(300000L,(byte) 2);
                                c.pointHoatDong += 5;
                                Service.ServerMessage(c,"Gia tộc bạn đã hoàn thành Lãnh Địa Gia Tộc, bạn nhận được 5 điểm hoạt động.");
                                Service.ServerMessage(c,"Cửa Lãnh Địa Gia Tộc sẽ khép lại sau 1 phút nữa");
                                Service.mapTime(c, (int) (manor.timeLength - System.currentTimeMillis() / 1000L));
                            }
                        }
                        SevenBeasts sb = SevenBeasts.getSevenBeasts(_char);
                        if (sb != null && tileMap.map.isSevenBeasts() && tileMap.map.template.mapID == 112) {
                            sb.countMob++;
                            if (sb.countMob >= tileMap.aMob.size()) {
                                if (sb.countRefresh <= 5) {
                                    sb.countRefresh++;
                                    sb.countMob = 0;
                                    SevenBeasts.refreshMob(sb);
                                    SevenBeasts.joinMap(sb, _char);
                                }
                            }
                        }
                        if (tileMap.map.isChienTruong()) {
                            if (mob.templateId == 98 || mob.templateId == 99) {
                                myChar.user.player.upPointChientruong((short)100);
                                Client.alertServer(String.format(Text.get(0, 348), Mob.arrMobTemplate[mob.templateId].name));
                            }
                            else if (mob.levelBoss == 2) {
                                myChar.user.player.upPointChientruong((short)50);
                            }
                            else {
                                myChar.user.player.upPointChientruong((short)1);
                            }
                        }
                        if (tileMap.map.isWarClanMap()) {
                            if (mob.levelBoss == 2) {
                                WarClan.upPointWarClan(_char, (short) 50);
                            } else if (mob.levelBoss == 1) {
                                WarClan.upPointWarClan(_char, (short) 10);
                            } else {
                                WarClan.upPointWarClan(_char, (short) 1);
                            }
                        }
                        if (sb != null && tileMap.map.isSevenBeasts() && (mob.templateId == 112 || mob.templateId == 113)) {
                            final Item itemTTB = new Item(null, (short) 288, 1, 600, true, (byte) 0, 0);
                            _char.user.player.ItemBagAdd(itemTTB);
                            sb.sendTB(_char, Mob.arrMobTemplate[mob.templateId].name, itemTTB, 0);
                            if (mob.templateId == 113) {
                                sb.isHaveMocNhan = false;
                            }
                            if (mob.templateId == 112) {
                                sb.sendTB(_char, null,null, 2);
                                sb.close();
                            }
                        }
                        else {
                            if (mob.levelBoss == 1) {
                                myChar.user.player.upCoinLock(mob.level * 50, (byte)2);
                                if (tileMap.map.isMapLangCo()) {
                                    myChar.user.player.upCoinLock(mob.level * 500, (byte)2);
                                }
                                if (tileMap.map.isClanManor()) {
                                    Item item = new Item(null, (short) 260, 1, 1800, true, (byte)0, 0);
                                    itemmap = new ItemMap(item, playerId, 65000, mob.x, mob.y, false);
                                    tileMap.ItemMapADD(itemmap);
                                }
                                if ((myChar.ctaskId == 29 && mob.templateId == 44 && _char.ctaskIndex == 1)
                                        || (myChar.ctaskId == 37 && mob.templateId == 58 && _char.ctaskIndex == 1)) {
                                    myChar.uptaskMaint();
                                }
                            }
                            if (mob.levelBoss == 2) {
                                myChar.user.player.upCoinLock(mob.level * 100, (byte)2);
                                if (tileMap.map.isMapLangCo()) {
                                    myChar.user.player.upCoinLock(mob.level * 1000, (byte)2);
                                }
                                if (myChar.ctaskId == 37 && mob.templateId == 59 && _char.ctaskIndex == 2) {
                                    myChar.uptaskMaint();
                                }
                            }
                            if (mob.templateId != 0 && mob.levelBoss != 3 && !mob.isBoss && !tileMap.map.isClanManor()) {
                                Item item = null;
                                if (Task.itemTemplateId(myChar, mob) == 221 || Task.itemTemplateId(myChar, mob) == 232) {
                                    item = new Item(null, Task.itemTemplateId(myChar, mob), 1, -1, true, (byte)0, 0);
                                } else if (Util.nextInt(100) < 50 && Task.itemTemplateId(myChar, mob) != -1) {
                                    item = new Item(null, Task.itemTemplateId(myChar, mob), 1, -1, true, (byte)0, 0);
                                }
                                else if ((((!tileMap.map.isMonsterMap() && Util.nextInt(100) <= 15) || (tileMap.map.isMonsterMap() && Util.nextInt(100) <= 5)) && Math.abs(myChar.cLevel - mob.level) <= 7) || (tileMap.map.isMapLangCo() && Util.nextInt(100) <= 10)) {
                                    int coinLock = 100 + mob.level * 15;
                                    if (tileMap.map.isMapLangCo()) {
                                        coinLock = 10000 * Util.nextInt(1, 2) + Util.nextInt(3000);
                                    }
                                    item = new Item(null, (short)12, Util.nextInt(coinLock / 8 * 10, coinLock), -1, true, (byte)0, 0);
                                }
                                else if (Util.nextInt(100) < 20 && Math.abs(myChar.cLevel - mob.level) <= 7) {
                                    int HPMPId;
                                    if (Util.nextInt(2) == 0) {
                                        HPMPId = ((mob.level < 10) ? 13 : ((mob.level < 30) ? 14 : ((mob.level < 50) ? 15 : ((mob.level < 60) ? 16 : ((tileMap.map.isMapLangCo() || tileMap.map.isMonsterMap()) ? 17 : -1)))));
                                    }
                                    else {
                                        HPMPId = ((mob.level < 10) ? 18 : ((mob.level < 30) ? 19 : ((mob.level < 50) ? 20 : ((mob.level < 60) ? 21 : ((tileMap.map.isMapLangCo() || tileMap.map.isMonsterMap()) ? 22 : -1)))));
                                    }
                                    if (HPMPId != -1) {
                                        item = new Item(null, (short)HPMPId, 1, -1, false, (byte)0, 0);
                                    }
                                }
                                else if (Util.nextInt(100) < 15 && Math.abs(myChar.cLevel - mob.level) <= 7) {
                                    final int crystalId = (mob.level < 10) ? Util.nextInt(0, 1) : ((mob.level < 25) ? Util.nextInt(0, 2) : ((mob.level < 50) ? Util.nextInt(2, 3) : Util.nextInt(3, 4)));
                                    item = new Item(null, (short)crystalId, 1, -1, mob.level != 0, (byte)0, 0);
                                }
                                else if (Util.nextInt(100) < 15 && Math.abs(myChar.cLevel - mob.level) <= 7) {
                                    final int level = (mob.level >= 10 && mob.level < 20) ? 10 : ((mob.level >= 20 && mob.level < 30) ? 20 : ((mob.level >= 30 && mob.level < 40) ? 30 : ((mob.level >= 40 && mob.level < 50) ? 40 : ((mob.level >= 50 && mob.level < 60) ? 50 : ((mob.level >= 60 && mob.level < 70) ? 60 : 1)))));
                                    item = ItemServer.itemDrop(_char, mob,(byte)Util.nextInt(1, 2),(byte)Util.nextInt(1, 6));
                                }
                                else if (tileMap.map.isMapLangCo() && Util.nextInt(100) <= 50) {
                                    // lang co
                                    if (Util.nextInt(100) < 15) {
                                        _char.user.player.upGold(Util.nextInt(5, 10), (byte)2);
                                    } else if (Util.nextInt(100) <= 18) {
                                        item = new Item(null, (short)455, 1, 2592000, false, (byte)0, 0);
                                    }
                                    else if (Util.nextInt(100) <= 10) {
                                        item = new Item(null, (short)456, 1, 2592000, false, (byte)0, 0);
                                    }
                                    else if (Util.nextInt(100) <= 50) {
                                        final short templateId = (short)Util.nextInt(439, 442);
                                        if (Util.nextInt(100) <= 30) {
                                            item = new Item(ItemOption.arrOptionDefault(templateId, (byte)0), templateId, 1, -1, false, (byte)0, 5);
                                        }
                                        else {
                                            item = new Item(null, templateId, 1, -1, false, (byte)0, 0);
                                        }
                                    }
                                    else if (Util.nextInt(100) <= 50) {
                                        final short templateId = (short)Util.nextInt(486, 489);
                                        if (Util.nextInt(100) <= 30) {
                                            item = new Item(ItemOption.arrOptionDefault(templateId, (byte)0), templateId, 1, -1, false, (byte)0, 5);
                                        }
                                        else {
                                            item = new Item(null, templateId, 1, -1, false, (byte)0, 0);
                                        }
                                    }
                                    else if (Util.nextInt(100) < 2) {
                                        item = new Item(null, (short)454, 1, 2592000, false, (byte)0, 0);
                                    }
                                }
                                else if (tileMap.map.isMonsterMap() && Util.nextInt(100) <= 10 && Math.abs(myChar.cLevel - mob.level) <= 7) {
                                    final Effect effect = myChar.getEffType((byte)25);
                                    if (Util.nextInt(100) <= 5) {
                                        final short templateId2 = (short)Util.nextInt(552, 557);
                                        item = new Item(null, templateId2, 1, -1, false, (byte)0, 0);
                                    }
                                    else if (Util.nextInt(100) <= 5) {
                                        item = new Item(null, (short)547, 1, -1, false, (byte)0, 0);
                                    }
                                    else if (Util.nextInt(100) <= 15) {
                                        final short templateId2 = (short)Util.nextInt(573, 578);
                                        item = new Item(null, templateId2, 1, -1, false, (byte)0, 0);
                                    }
                                    else if (Util.nextInt(100) <= 5) {
                                        item = new Item(null, (short)545, 1, 86400, false, (byte)0, 0);
                                    }
                                    else if (Util.nextInt(100) <= 10) {
                                        final short templateId2 = (short)Util.nextInt(469, 473);
                                        item = new Item(null, templateId2, 1, -1, false, (byte)0, 0);
                                    }
                                    else if (effect != null && effect.template.id == 41) {
                                        if (Util.nextInt(100) <= 10) {
                                            final short templateId2 = (short)Util.nextInt(439, 442);
                                            if (Util.nextInt(100) <= 80) {
                                                item = new Item(ItemOption.arrOptionDefault(templateId2, (byte)0), templateId2, 1, -1, false, (byte)0, 5);
                                            }
                                            else {
                                                item = new Item(null, templateId2, 1, -1, false, (byte)0, 0);
                                            }
                                        }
                                        else if (Util.nextInt(100) <= 10) {
                                            final short templateId2 = (short)Util.nextInt(486, 489);
                                            if (Util.nextInt(100) <= 80) {
                                                item = new Item(ItemOption.arrOptionDefault(templateId2, (byte)0), templateId2, 1, -1, false, (byte)0, 5);
                                            }
                                            else {
                                                item = new Item(null, templateId2, 1, -1, false, (byte)0, 0);
                                            }
                                        }
                                    }
                                    else if (effect != null && effect.template.id == 40) {
                                        if (Util.nextInt(100) < 5) {
                                            final short templateId2 = (short)Util.nextInt(455, 456);
                                            item = new Item(null, templateId2, 1, 2592000, false, (byte)0, 0);
                                        }
                                        else if (Util.nextInt(1000) < 5) {
                                            item = new Item(null, (short)454, 1, -1, false, (byte)0, 0);
                                        }
                                        else if (Util.nextInt(100) < 35) {
                                            final short templateId2 = (short)Util.nextInt(439, 442);
                                            item = new Item(null, templateId2, 1, -1, false, (byte)0, 0);
                                        }
                                        else if (Util.nextInt(1000) < 35) {
                                            final short templateId2 = (short)Util.nextInt(486, 489);
                                            item = new Item(null, templateId2, 1, -1, false, (byte)0, 0);
                                        }
                                    }
                                }
                                else if (GameScr.vEvent != 0) {
                                    item = Event.itemEventMob(myChar, mob);
                                }
                                if (item != null) {
                                    itemmap = new ItemMap(item, playerId, 65000, mob.x, mob.y, false);
                                    tileMap.ItemMapADD(itemmap);
                                }
                            }
                            else if (mob.isBoss) {
                                final ArrayList<ItemMap> AitemMap = new ArrayList<>();
                                Event.finghtBoss(myChar, mob);
                                if (mob.templateId == 138) {
                                    for (byte j = 0; j < Util.nextInt(5, 15); ++j) {
                                        short x = mob.x;
                                        final short y = mob.y;
                                        if (tileMap.map.isMonsterMap()) {
                                            x += (short)Util.nextInt(-300, 300);
                                        }
                                        else {
                                            x += (short)Util.nextInt(-25, 25);
                                        }
                                        final Item item2 = new Item(null, (short)Util.nextInt(2, 4), 1, -1, false, (byte)0, 0);
                                        AitemMap.add(new ItemMap(item2, playerId, 65000, x, y, false));
                                    }
                                }
                                if (!tileMap.map.isMapLangCo() && mob.templateId != 230) {
                                    if (mob.templateId == 114 || mob.templateId == 115 || mob.templateId == 116 || mob.templateId == 139 || mob.templateId == 201 || mob.templateId == 203 || mob.templateId == 204) {
                                        int quantity = tileMap.map.isMonsterMap() ? 10 : 4;
                                        final int preId = tileMap.map.isMonsterMap() ? 4 : ((mob.templateId == 115) ? Util.nextInt(2, 3) : Util.nextInt(3, 4));
                                        for (byte k = 0; k < 4 && quantity > 0; quantity -= (tileMap.map.isMonsterMap() ? 2 : 1), ++k) {
                                            for (byte m = 0; m < quantity; ++m) {
                                                short x2 = mob.x;
                                                final short y2 = mob.y;
                                                if (tileMap.map.isMonsterMap()) {
                                                    x2 += (short)Util.nextInt(-300, 300);
                                                }
                                                else {
                                                    x2 += (short)Util.nextInt(-25, 25);
                                                }
                                                final Item item3 = new Item(null, (short)(preId + k), 1, -1, false, (byte)0, 0);
                                                AitemMap.add(new ItemMap(item3, playerId, 65000, x2, y2, false));
                                            }
                                        }
                                        if (tileMap.map.isMonsterMap()) {
                                            for (byte k = 0; k < Util.nextInt(0, 2); ++k) {
                                                short x3 = mob.x;
                                                final short y3 = mob.y;
                                                if (tileMap.map.isMonsterMap()) {
                                                    x3 += (short)Util.nextInt(-300, 300);
                                                }
                                                else {
                                                    x3 += (short)Util.nextInt(-25, 25);
                                                }
                                                final Item item4 = new Item(null, (short)8, 1, -1, false, (byte)0, 0);
                                                AitemMap.add(new ItemMap(item4, playerId, 65000, x3, y3, false));
                                            }
                                        }
                                    }
                                    if (mob.templateId == 114 || mob.templateId == 116 || mob.templateId == 139 || mob.templateId == 201 || mob.templateId == 203 || mob.templateId == 204) {
                                        if (Util.nextInt(10) >= 5) {
                                            short x4 = mob.x;
                                            final short y4 = mob.y;
                                            if (tileMap.map.isMonsterMap()) {
                                                x4 += (short)Util.nextInt(-300, 300);
                                            }
                                            else {
                                                x4 += (short)Util.nextInt(-25, 25);
                                            }
                                            AitemMap.add(new ItemMap(new Item(null, (short)(tileMap.map.isMonsterMap() ? Util.nextInt(558, 563) : Util.nextInt(311, 316)), 1, -1, false, (byte)0, 0), playerId, 65000, x4, y4, false));
                                        }
                                        if (tileMap.map.isMonsterMap() && Util.nextInt(10) == 0) {
                                            short x4 = mob.x;
                                            final short y4 = mob.y;
                                            if (tileMap.map.isMonsterMap()) {
                                                x4 += (short)Util.nextInt(-300, 300);
                                            }
                                            else {
                                                x4 += (short)Util.nextInt(-25, 25);
                                            }
                                            final Item item5 = new Item(null, (short)Util.nextInt(383, 384), 1, -1, false, (byte)0, 0);
                                            AitemMap.add(new ItemMap(item5, playerId, 65000, x4, y4, false));
                                        }
                                    }
                                    if (mob.templateId == 114 || mob.templateId == 115 || mob.templateId == 116 || mob.templateId == 139 || mob.templateId == 201 || mob.templateId == 203 || mob.templateId == 204) {
                                        short x4 = mob.x;
                                        final short y4 = mob.y;
                                        if (tileMap.map.isMonsterMap()) {
                                            x4 += (short)Util.nextInt(-300, 300);
                                        }
                                        else {
                                            x4 += (short)Util.nextInt(-25, 25);
                                        }
                                        AitemMap.add(new ItemMap(new Item(null, (short)340, tileMap.map.isMonsterMap() ? 5 : 1, -1, false, (byte)0, 0), playerId, 65000, x4, y4, false));
                                    }
                                    if (mob.templateId == 114 || mob.templateId == 115 || mob.templateId == 116 || mob.templateId == 139 || mob.templateId == 201 || mob.templateId == 203 || mob.templateId == 204) {
                                        // diet boss
                                        //DropItem.BossDrop(tileMap, mob, playerId);
                                        for (byte j = 0; j < Util.nextInt(4, 5); ++j) {
                                            short x = mob.x;
                                            final short y = mob.y;
                                            if (tileMap.map.isMonsterMap()) {
                                                x += (short)Util.nextInt(-300, 300);
                                            }
                                            else {
                                                x += (short)Util.nextInt(-25, 25);
                                            }
                                            final Item item2 = ItemServer.itemWPShop(Util.nextInt(1, 8) * 10, (byte)Util.nextInt(1, 6), (byte)3);
                                            if (Util.nextInt(15) <= 1) {
                                                item2.upgradeNext((byte)Util.nextInt(8, 11));
                                            } else if (Util.nextInt(500) <= 1) {
                                                item2.upgradeNext((byte)Util.nextInt(12, 14));
                                            } else if (Util.nextInt(5000) <= 1) {
                                                item2.upgradeNext((byte)Util.nextInt(15, 16));
                                            }
                                            AitemMap.add(new ItemMap(item2, playerId, 65000, x, y, false));
                                        }
                                    }
                                    for (byte j = 0; j < 20; ++j) {
                                        short x = mob.x;
                                        final short y = mob.y;
                                        if (tileMap.map.isMonsterMap()) {
                                            x += (short)Util.nextInt(-300, 300);
                                        }
                                        else {
                                            x += (short)Util.nextInt(-25, 25);
                                        }
                                        final Item item2 = new Item(null, (short)12, Util.nextInt(15000, 30000), -1, false, (byte)0, 0);
                                        AitemMap.add(new ItemMap(item2, playerId, 65000, x, y, false));
                                    }
                                }
                                for (short l = 0; l < AitemMap.size(); ++l) {
                                    final ItemMap itemMap = AitemMap.get(l);
                                    if (itemMap != null) {
                                        if (mob.templateId != 114 && mob.templateId != 115 && mob.templateId != 116 && mob.templateId != 139 && mob.templateId != 198 && mob.templateId != 199 && mob.templateId != 139 && mob.templateId != 200 && mob.templateId != 201 && mob.templateId != 203 && mob.templateId != 204) {
                                            itemMap.playerId = 0;
                                        }
                                        tileMap.ItemMapADD(itemMap);
                                        try {
                                            for (short k2 = 0; k2 < tileMap.aCharInMap.size(); ++k2) {
                                                if (tileMap.aCharInMap.get(k2).user != null && tileMap.aCharInMap.get(k2).user.session != null) {
                                                    Service.ItemMapAdd(tileMap.aCharInMap.get(k2), itemMap, null);
                                                }
                                            }
                                        }
                                        catch (Exception ex) {}
                                    }
                                }
                                if (!tileMap.map.isBackCaveMap() && mob.templateId != 230) {
                                    Client.alertServer(String.format(Text.get(0, 153), myChar.cName, Mob.arrMobTemplate[mob.templateId].name));
                                    //Top.sortTop(4, _char.cName, null, (_char.user.player.epoint + 100), null);
                                    if (Util.nextInt(10) <3 ) {
                                        _char.user.player.upGold(Util.nextInt(500, 1000), (byte)2);
                                    }
                                }
                            }
                            if (tileMap.map.isBackCaveMap()) {
                                tileMap.map.backCave.nexPoint(tileMap, mob, myChar);
                            }
                            if (tileMap.map.isClanManor()) {
                                if (mob.templateId == 82) {
                                    _char.clan.clanManor.addCoinManor(Util.nextInt(40,50), _char.charID);
                                } else {
                                    int mapId = _char.tileMap.mapID;
                                    ClanManor manor = _char.clan.clanManor;
                                    Item item = null;
                                    manor.refesh[mapId-80]++;
                                    manor.addCoinManor(1, _char.charID);
                                    if (mob.levelBoss == 0) {
                                        if (mob.templateId == 81) {
                                            item = new Item(null, (short) 261, 1, 600, true, (byte) 0, 0);
                                        } else {
                                            if (Util.nextInt(10) > 5) {
                                                item = new Item(null, (short) 12, 10000, -1, true, (byte) 0, 0);
                                            } else if (Util.nextInt(10) > 5) {
                                                item = new Item(null, (short) Util.nextInt(6, 8), 1, -1, true, (byte) 0, 0);
                                            }
                                        }
                                    }
                                    if (item != null) {
                                        itemmap = new ItemMap(item, playerId, 65000, mob.x, mob.y, false);
                                        tileMap.ItemMapADD(itemmap);
                                    }
                                    if (manor.refesh[mapId-80] == (_char.tileMap.aMob.size() - 1)) {
                                        manor.liveMob(tileMap.map);
                                    }
                                }
                            }
                        }
                    }
                    try {
                        for (short i2 = 0; i2 < tileMap.aCharInMap.size(); ++i2) {
                            if (tileMap.aCharInMap.get(i2).user != null && tileMap.aCharInMap.get(i2).user.session != null) {
                                if (mob.injureThenDie || mob.hp <= 0) {
                                    Service.MobDie(tileMap.aCharInMap.get(i2), mob, dame, flag, itemmap);
                                }
                                else if (dame <= 0) {
                                    Service.MobMiss(tileMap.aCharInMap.get(i2), mob);
                                }
                                else {
                                    Service.MobHP(tileMap.aCharInMap.get(i2), mob, dame, flag);
                                }
                            }
                        }
                    }
                    catch (Exception ex2) {}
                    if (type == 0) {
                        if (!mob.injureThenDie) {
                            if (_char.percentFire() > Util.nextInt(100)) {
                                Mob.upFire(mob, true, _char.timeFire());
                            }
                            if (_char.percentIce() > Util.nextInt(100)) {
                                Mob.upIce(mob, true, _char.timeIce());
                            }
                            if (_char.percentWind() > Util.nextInt(100)) {
                                Mob.upWind(mob, true, _char.timeWind());
                            }
                        }
                        synchronized (myChar.aBurn) {
                            if (myChar.isBurn && !mob.injureThenDie && !myChar.aBurn.contains(mob)) {
                                myChar.aBurn.add(mob);
                            }
                        }
                    }
                    if (mob.templateId != 0 && Math.abs(mob.level - myChar.cLevel) <= 10 && dame > 0) {
                        int downEXP = myChar.cLevel - mob.level;
                        if (downEXP < -5) {
                            downEXP = -5;
                        }
                        else if (downEXP > 5) {
                            downEXP = 5;
                        }
                        int EXPup = dame / (9 + downEXP);
                        int ExpX = 1;
                        if (mob.levelBoss == 1) {
                            ExpX *= 3;
                        }
                        else if (mob.levelBoss == 2) {
                            ExpX *= 5;
                        }
                        else if (mob.levelBoss == 3) {
                            EXPup /= 2;
                        }
                        else if (mob.isBoss) {
                            EXPup /= 2;
                        }
                        if (tileMap.map.isMapLangCo()) {
                            ExpX *= (int)1.5;
                        }
                        else if (tileMap.map.isMonsterMap()) {
                            ExpX *= (int)2.5;
                        }
                        EXPup *= GameScr.EXPMobX(mob.level) * ExpX;
                        final Effect effect2 = myChar.getEffType((byte)18);
                        if (effect2 != null) {
                            EXPup *= effect2.param;
                        }
                        EXPup += EXPup * myChar.percentUpEXP() / 100;
                        
                        if (GameScr.up_exp > 0) {
                            EXPup *= GameScr.up_exp;
                        }
                        
                        myChar.updateExp(EXPup);
                        if (tileMap.map.isBackCaveMap()) {
                            EXPup /= 8;
                            final BackCave cave = tileMap.map.backCave;
                            synchronized (cave.ACharInCave) {
                                for (byte i3 = 0; i3 < cave.ACharInCave.size(); ++i3) {
                                    final Char player2 = cave.ACharInCave.get(i3);
                                    if (player2 != null && player2.charID != myChar.charID && Math.abs(player2.cLevel - myChar.cLevel) <= 10) {
                                        player2.updateExp(EXPup);
                                    }
                                }
                            }
                        }
                        else if (myChar.party != null) {
                            EXPup /= 10;
                            for (short i4 = 0; i4 < tileMap.numPlayer; ++i4) {
                                final Char player3 = tileMap.aCharInMap.get(i4);
                                if (player3 != null && player3.party != null && player3.charID != myChar.charID && player3.party.partyId == myChar.party.partyId && Math.abs(player3.user.player.cLevel - myChar.user.player.cLevel) <= 10) {
                                    player3.updateExp(EXPup);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected static void SkillUse(final TileMap tileMap, final Char _char, final Skill skill, final Mob mob, final Char player) {
        if (skill != null && !_char.isIce && !_char.isWind) {
            switch (skill.template.id) {
                case 4: {
                    if (mob == null || mob.status == 0 || Math.abs(_char.cx - mob.x) > skill.dx + Util.nextInt(40) || Math.abs(_char.cy - mob.y) > skill.dy + Util.nextInt(40)) {
                        break;
                    }
                    if (mob.isBoss || mob.levelBoss > 0) {
                        Service.ServerMessage(_char, Text.get(0, 117));
                        break;
                    }
                    mob.startDie();
                    if (_char.mobFocus != null && _char.mobFocus.mobId == mob.mobId) {
                        _char.mobFocus = null;
                    }
                    if (Task.isExtermination(_char, mob)) {
                        _char.uptaskMaint();
                        if (_char.party != null) {
                            try {
                                for (short i = 0; i < tileMap.numPlayer; ++i) {
                                    final Char player2 = tileMap.aCharInMap.get(i);
                                    if (player2 != null && player2.user != null && player2.party != null && player2.charID != _char.charID && player2.party.partyId == _char.party.partyId && player2.ctaskId == _char.ctaskId && player2.ctaskIndex == _char.ctaskIndex) {
                                        player2.uptaskMaint();
                                    }
                                }
                            }
                            catch (Exception ex) {}
                        }
                    }
                    final Item item = new Item(null, (short)218, _char.PramSkillTotal(50), -1, true, (byte)0, 0);
                    final ItemMap itemMap = new ItemMap(item, _char.playerId, 65000, mob.x, mob.y, false);
                    _char.tileMap.ItemMapADD(itemMap);
                    try {
                        for (short j = 0; j < tileMap.aCharInMap.size(); ++j) {
                            if (tileMap.aCharInMap.get(j).user != null && tileMap.aCharInMap.get(j).user.session != null) {
                                Service.MobChange(tileMap.aCharInMap.get(j), mob.mobId, itemMap);
                            }
                        }
                    }
                    catch (Exception ex2) {}
                    break;
                }
                case 24: {
                    if (mob != null && mob.status != 0 && mob.status != 1 && Math.abs(_char.cx - mob.x) <= skill.dx + Util.nextInt(40) && Math.abs(_char.cy - mob.y) <= skill.dy + Util.nextInt(40)) {
                        synchronized (mob.LOCK) {
                            mob.isDontMove = true;
                            mob.timeDontMove = _char.PramSkillTotal(55) * 1000;
                            try {
                                for (short k = 0; k < _char.tileMap.aCharInMap.size(); ++k) {
                                    if (_char.tileMap.aCharInMap.get(k).user != null && _char.tileMap.aCharInMap.get(k).user.session != null) {
                                        Service.mobIsMove(_char.tileMap.aCharInMap.get(k), mob.mobId, true);
                                    }
                                }
                            }
                            catch (Exception ex3) {}
                        }
                        break;
                    }
                    if (player != null && player.statusMe != 14 && Math.abs(_char.cx - player.cx) <= skill.dx + Util.nextInt(40) && Math.abs(_char.cy - player.cy) <= skill.dy + Util.nextInt(40)) {
                        Char.setEffect(player, (byte)18, -1, _char.PramSkillTotal(55) * 1000, (short)0, null, (byte)0);
                        break;
                    }
                    break;
                }
                case 40: {
                    if (mob == null || mob.status == 0 || Math.abs(_char.cx - mob.x) > skill.dx + Util.nextInt(40) || Math.abs(_char.cy - mob.y) > skill.dy + Util.nextInt(40)) {
                        break;
                    }
                    if (mob.isBoss || mob.levelBoss > 0) {
                        Service.ServerMessage(_char, Text.get(0, 117));
                        break;
                    }
                    synchronized (mob.LOCK) {
                        mob.isDisable = true;
                        mob.timeDisable = _char.PramSkillTotal(48) * 1000;
                        try {
                            for (short k = 0; k < _char.tileMap.aCharInMap.size(); ++k) {
                                if (_char.tileMap.aCharInMap.get(k).user != null && _char.tileMap.aCharInMap.get(k).user.session != null) {
                                    Service.mobIsDisable(_char.tileMap.aCharInMap.get(k), mob.mobId, true);
                                }
                            }
                        }
                        catch (Exception ex4) {}
                    }
                    break;
                }
                case 42: {
                    if (mob != null) {
                        _char.cx = mob.x;
                        _char.cy = mob.y;
                        try {
                            for (short i = 0; i < _char.tileMap.aCharInMap.size(); ++i) {
                                if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null) {
                                    Service.FastMove(_char.tileMap.aCharInMap.get(i), _char.charID, 0, _char.cx, _char.cy);
                                }
                            }
                        }
                        catch (Exception e) {}
                        break;
                    }
                    if (player != null) {
                        Char.setEffect(player, (byte)18, -1, 3000 + _char.PramSkillTotal(62), (short)0, null, (byte)0);
                        _char.cx = player.cx;
                        _char.cy = player.cy;
                        try {
                            for (short i = 0; i < _char.tileMap.aCharInMap.size(); ++i) {
                                if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null) {
                                    Service.FastMove(_char.tileMap.aCharInMap.get(i), _char.charID, player.charID, _char.cx, _char.cy);
                                }
                            }
                        }
                        catch (Exception ex5) {}
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    protected static void myBuff(final TileMap tileMap, final Char _char, final Char myChar, final Skill skill, final byte cdir) {
        if (_char.statusMe != 14 && !_char.isIce && !_char.isWind && skill != null && skill.lastTimeUseThisSkill <= System.currentTimeMillis()) {
            if (!_char.isNhanban) {
                _char.upMP(-skill.manaUse);
            }
            switch (skill.template.id) {
                case 6: {
                    if (!_char.isNhanban) {
                        Char.setEffect(_char, (byte)15, -1, _char.PramSkillTotal(53) * 1000, (short)0, null, (byte)0);
                        break;
                    }
                    break;
                }
                case 13: {
                    if (!_char.isNhanban) {
                        Char.setEffect(_char, (byte)9, -1, 30000, (short)_char.PramSkillTotal(51), null, (byte)0);
                        _char.dXYBurn = skill.dy;
                        break;
                    }
                    break;
                }
                case 15: {
                    if (!_char.isNhanban) {
                        Char.setEffect(_char, (byte)16, -1, 5000, (short)_char.PramSkillTotal(52), null, (byte)0);
                        break;
                    }
                    break;
                }
                case 22: {
                    if (!_char.isNhanban) {
                        short x = _char.cx;
                        final short y = _char.cy;
                        if (cdir == 1) {
                            x += 24;
                        }
                        else if (cdir == -1) {
                            x -= 24;
                        }
                        if (!_char.tileMap.tileTypeAt(x, y, 2)) {
                            Service.ServerMessage(_char, Text.get(0, 118));
                        }
                        else {
                            try {
                                _char.tileMap.lock.lock("Bufff skilll");
                                try {
                                    final BuNhin bunhin = new BuNhin(_char.charID, _char.cName, x, y, _char.cMaxHP(), skill.dx, skill.dy, _char.PramSkillTotal(49) * 1000);
                                    _char.tileMap.aBuNhin.add(bunhin);
                                    for (short i = 0; i < _char.tileMap.aCharInMap.size(); ++i) {
                                        if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null) {
                                            Service.createBunhin(_char.tileMap.aCharInMap.get(i), bunhin);
                                        }
                                    }
                                }
                                finally {
                                    _char.tileMap.lock.unlock();
                                }
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    break;
                }
                case 31: {
                    if (!_char.isNhanban) {
                        Char.setEffect(_char, (byte)10, -1, 90000, (short)_char.PramSkillTotal(30), null, (byte)0);
                        break;
                    }
                    break;
                }
                case 33: {
                    if (!_char.isNhanban) {
                        Char.setEffect(_char, (byte)17, -1, 5000, (short)_char.PramSkillTotal(56), null, (byte)0);
                        break;
                    }
                    break;
                }
                case 47: {
                    short paramMe = (short)_char.PramSkillTotal(27);
                    paramMe += (short)(paramMe * _char.PramSkillTotal(66) / 100);
                    short param = (short)_char.PramSkillTotal(43);
                    param += (short)(param * _char.PramSkillTotal(66) / 100);
                    if (_char.isNhanban) {
                        Char.setEffect(myChar, (byte)8, -1, 5000, paramMe, null, (byte)0);
                        break;
                    }
                    Char.setEffect(_char, (byte)8, -1, 5000, paramMe, null, (byte)0);
                    if (_char.party != null) {
                        try {
                            tileMap.lock.lock("Bufff skill");
                            try {
                                for (short j = 0; j < tileMap.numPlayer; ++j) {
                                    final Char player = tileMap.aCharInMap.get(j);
                                    if (player != null && player.party != null && player.charID != _char.charID && player.party.partyId == _char.party.partyId && Math.abs(player.cx - _char.cx) <= skill.dx && Math.abs(player.cy - _char.cy) <= skill.dy) {
                                        Char.setEffect(player, (byte)8, -1, 5000, param, null, (byte)0);
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
                        break;
                    }
                    break;
                }
                case 51: {
                    int res = _char.PramSkillTotal(45);
                    res += res * _char.PramSkillTotal(66) / 100;
                    int percentDame = _char.PramSkillTotal(46);
                    percentDame += percentDame * _char.PramSkillTotal(66) / 100;
                    if (_char.isNhanban) {
                        Char.setEffect(myChar, (byte)19, -1, 90000, (short)0, new int[] { res, percentDame }, (byte)0);
                        break;
                    }
                    Char.setEffect(_char, (byte)19, -1, 90000, (short)0, new int[] { res, percentDame }, (byte)0);
                    if (_char.party != null) {
                        try {
                            tileMap.lock.lock("Bufff skilll");
                            try {
                                for (short k = 0; k < tileMap.numPlayer; ++k) {
                                    final Char player2 = tileMap.aCharInMap.get(k);
                                    if (player2 != null && player2.party != null && player2.charID != _char.charID && player2.party.partyId == _char.party.partyId && Math.abs(player2.cx - _char.cx) <= skill.dx && Math.abs(player2.cy - _char.cy) <= skill.dy) {
                                        Char.setEffect(player2, (byte)19, -1, 90000, (short)0, new int[] { res, percentDame }, (byte)0);
                                    }
                                }
                            }
                            finally {
                                tileMap.lock.unlock();
                            }
                        }
                        catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        break;
                    }
                    break;
                }
                case 52: {
                    final int time = _char.PramSkillTotal(40) * 1000;
                    int timeMedownFire = _char.PramSkillTotal(40);
                    timeMedownFire += timeMedownFire * _char.PramSkillTotal(66) / 100;
                    int timedownFire = _char.PramSkillTotal(47);
                    timedownFire += timedownFire * _char.PramSkillTotal(66) / 100;
                    int timeMedownIce = _char.PramSkillTotal(41);
                    timeMedownIce += timeMedownIce * _char.PramSkillTotal(66) / 100;
                    int timedownIce = _char.PramSkillTotal(54);
                    timedownIce += timedownIce * _char.PramSkillTotal(66) / 100;
                    if (_char.isNhanban) {
                        Char.setEffect(myChar, (byte)20, -1, time, (short)0, new int[] { (timeMedownFire > 0) ? timeMedownFire : 0, (timeMedownIce > 0) ? timeMedownIce : 0 }, (byte)0);
                        break;
                    }
                    Char.setEffect(_char, (byte)20, -1, time, (short)0, new int[] { (timeMedownFire > 0) ? timeMedownFire : 0, (timeMedownIce > 0) ? timeMedownIce : 0 }, (byte)0);
                    if (_char.party != null) {
                        try {
                            tileMap.lock.lock("Buff skilll");
                            try {
                                for (short l = 0; l < tileMap.numPlayer; ++l) {
                                    final Char player3 = tileMap.aCharInMap.get(l);
                                    if (player3 != null && player3.party != null && player3.charID != _char.charID && player3.party.partyId == _char.party.partyId && Math.abs(player3.cx - _char.cx) <= skill.dx && Math.abs(player3.cy - _char.cy) <= skill.dy) {
                                        Char.setEffect(player3, (byte)20, -1, time, (short)0, new int[] { (timedownFire > 0) ? timedownFire : 0, (timedownIce > 0) ? timedownIce : 0 }, (byte)0);
                                    }
                                }
                            }
                            finally {
                                tileMap.lock.unlock();
                            }
                        }
                        catch (InterruptedException e3) {
                            e3.printStackTrace();
                        }
                        break;
                    }
                    break;
                }
                case 58: {
                    if (!_char.isNhanban) {
                        Char.setEffect(_char, (byte)11, -1, _char.PramSkillTotal(64), (short)20000, null, (byte)0);
                        break;
                    }
                    break;
                }
                case 67:
                case 68:
                case 69:
                case 70:
                case 71:
                case 72: {
                    if (!_char.isHuman) {
                        break;
                    }
                    if (_char.user.player.ItemBagQuantity((short)545) > 0 || (_char.Nhanban != null && _char.Nhanban.timeLiveNhanban != -1)) {
                        _char.Nhanban.percentPow = _char.PramSkillTotal(71);
                        if (_char.user.player.ItemBagQuantity((short)545) > 0) {
                            _char.Nhanban.timeLiveNhanban = (int)(System.currentTimeMillis() / 1000L + 60 * _char.PramSkillTotal(68));
                            _char.user.player.ItemBagUses((short)545, 1);
                        }
                        else {
                            final Char nhanban = _char.Nhanban;
                            nhanban.percentPow /= 2;
                        }
                        Player.CallNhanban(tileMap, _char);
                        break;
                    }
                    Service.ServerMessage(_char, String.format(Text.get(0, 309), GameScr.itemTemplates[545].name));
                    break;
                }
            }
            skill.lastTimeUseThisSkill = System.currentTimeMillis() + skill.coolDown;
        }
    }
    
    protected static void BuffLive(final Char _char, final TileMap tileMap, final Skill skill, final Char player) {
        if (_char.statusMe != 14 && !_char.isIce && !_char.isWind && skill != null && skill.lastTimeUseThisSkill <= System.currentTimeMillis()) {
            _char.upMP(-skill.manaUse);
            if (player != null && player.statusMe == 14 && Math.abs(player.cx - _char.cx) <= skill.dx && Math.abs(player.cy - _char.cy) <= skill.dy) {
                player.cHP = player.cMaxHP();
                player.cMP = player.cMaxMP();
                player.statusMe = 1;
                Service.MeLive(player);
                try {
                    for (int i = 0; i < tileMap.numPlayer; ++i) {
                        if (tileMap.aCharInMap.get(i).user != null && tileMap.aCharInMap.get(i).user.session != null && player.charID != tileMap.aCharInMap.get(i).charID) {
                            Service.PlayerLoadLive(tileMap.aCharInMap.get(i), player.charID, player.cHP, _char.cMaxHP(), player.cx, player.cy);
                        }
                    }
                }
                catch (Exception ex) {}
                Char.setEffect(player, (byte)11, -1, 5000, (short)_char.PramSkillTotal(28), null, (byte)0);
            }
            skill.lastTimeUseThisSkill = System.currentTimeMillis() + skill.coolDown;
        }
    }
}
