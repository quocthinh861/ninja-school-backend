 package com.Server.ninja.Server;

import com.Server.ninja.template.MapTemplate;

import java.util.ArrayList;

import com.Server.ninja.template.TaskTemplate;
import com.Server.ninja.option.ItemOption;
import com.Server.io.Message;
import com.Server.ninja.template.MobTemplate;
import lombok.val;

public class Service {
    protected static Message messageSubCommand(final byte command) throws Exception {
        final Message message = new Message((byte) (-30));
        message.writer().writeByte(command);
        return message;
    }

    protected static Message messageNotLogin(final byte command) throws Exception {
        final Message message = new Message((byte) (-29));
        message.writer().writeByte(command);
        return message;
    }

    protected static Message messageNotMap(final byte command) throws Exception {
        final Message message = new Message((byte) (-28));
        message.writer().writeByte(command);
        return message;
    }

    protected static void ClearCache(final Session_ME session) throws Exception {
        final Message msg = messageNotLogin((byte) 2);
        session.sendMessage(msg);
        msg.cleanup();
    }

    private static void loadInfoMap(final Char _char, final TileMap tileMap, final Message msg) {
        try {
            msg.writer().writeShort(_char.cx);
            msg.writer().writeShort(_char.cy);
            msg.writer().writeByte(tileMap.aVgo.size());
            for (byte i = 0; i < tileMap.aVgo.size(); ++i) {
                final Waypoint vgo = tileMap.aVgo.get(i);
                msg.writer().writeShort(vgo.minX);
                msg.writer().writeShort(vgo.minY);
                msg.writer().writeShort(vgo.maxX);
                msg.writer().writeShort(vgo.maxY);
            }
            int size = 0;
            for (short j = 0; j < tileMap.aMob.size(); ++j) {
                final Mob mob = tileMap.aMob.get(j);
                if (mob != null && (mob.playerId == -1 || mob.playerId == _char.user.player.playerId)) {
                    ++size;
                }
            }
            msg.writer().writeByte(size);
            for (short j = 0; j < tileMap.aMob.size(); ++j) {
                final Mob mob = tileMap.aMob.get(j);
                if (mob != null && (mob.playerId == -1 || mob.playerId == _char.user.player.playerId)) {
                    msg.writer().writeBoolean(mob.isDisable);
                    msg.writer().writeBoolean(mob.isDontMove);
                    msg.writer().writeBoolean(mob.isFire);
                    msg.writer().writeBoolean(mob.isIce);
                    msg.writer().writeBoolean(mob.isWind);
                    if (_char.user.session.versionNja < 203) {
                        msg.writer().writeByte(mob.templateId);
                    } else {
                        msg.writer().writeShort(mob.templateId);
                    }
                    msg.writer().writeByte(mob.sys);
                    msg.writer().writeInt(mob.hp);
                    msg.writer().writeByte(mob.level);
                    if (mob.levelBoss == 1 || mob.levelBoss == 2) {
                        msg.writer().writeInt(Mob.arrMobTemplate[mob.templateId].hp);
                    } else {
                        msg.writer().writeInt(mob.maxHp);
                    }
                    msg.writer().writeShort(mob.x);
                    msg.writer().writeShort(mob.y);
                    msg.writer().writeByte(mob.status);
                    if (mob.levelBoss == 1 || mob.levelBoss == 2) {
                        msg.writer().writeByte(0);
                    } else {
                        msg.writer().writeByte(mob.levelBoss);
                    }
                    msg.writer().writeBoolean(mob.isBoss);
                }
            }
            msg.writer().writeByte(tileMap.aBuNhin.size());
            for (byte k = 0; k < tileMap.aBuNhin.size(); ++k) {
                msg.writer().writeUTF(tileMap.aBuNhin.get(k).name);
                msg.writer().writeShort(tileMap.aBuNhin.get(k).x);
                msg.writer().writeShort(tileMap.aBuNhin.get(k).y);
            }
            msg.writer().writeByte(tileMap.aNpc.size());
            for (byte k = 0; k < tileMap.aNpc.size(); ++k) {
                msg.writer().writeByte(tileMap.aNpc.get(k).type);
                msg.writer().writeShort(tileMap.aNpc.get(k).cx);
                msg.writer().writeShort(tileMap.aNpc.get(k).cy);
                msg.writer().writeByte(tileMap.aNpc.get(k).template.npcTemplateId);
            }
            msg.writer().writeByte(tileMap.aItemMap.size());
            for (byte k = 0; k < tileMap.aItemMap.size(); ++k) {
                final ItemMap itemMap = tileMap.aItemMap.get(k);
                msg.writer().writeShort(itemMap.itemMapID);
                msg.writer().writeShort(itemMap.item.template.id);
                msg.writer().writeShort(itemMap.x);
                msg.writer().writeShort(itemMap.y);
            }
            msg.writer().writeUTF(tileMap.map.template.mapName);
            msg.writer().writeByte(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void MELoadALL(final Char _char) {
        Message msg = null;
        try {
            msg = new Message(-30);
            msg.writer().writeByte(-127);
            msg.writer().writeInt(_char.charID);
            msg.writer().writeUTF(_char.cClanName);
            if (!_char.cClanName.isEmpty()) {
                msg.writer().writeByte(_char.ctypeClan);
            }
            msg.writer().writeByte(_char.ctaskId);
            msg.writer().writeByte(_char.cgender);
            if (CaiTrang.Head(_char, _char.partCaiTrang()) != -1) {
                msg.writer().writeShort(CaiTrang.Head(_char, _char.partCaiTrang()));
            } else if (_char.ItemBody[11] != null) {
                msg.writer().writeShort(_char.ItemBody[11].template.part);
            } else {
                msg.writer().writeShort(_char.head);
            }
            msg.writer().writeByte(_char.cSpeed());
            msg.writer().writeUTF(_char.cName);
            msg.writer().writeByte(_char.cPk);
            msg.writer().writeByte(_char.cTypePk);
            msg.writer().writeInt(_char.cMaxHP());
            msg.writer().writeInt(_char.cHP);
            msg.writer().writeInt(_char.cMaxMP());
            msg.writer().writeInt(_char.cMP);
            msg.writer().writeLong(_char.cEXP);
            msg.writer().writeLong(_char.cExpDown);
            msg.writer().writeShort(_char.eff5BuffHp());
            msg.writer().writeShort(_char.eff5BuffMp());
            msg.writer().writeByte(_char.nClass);
            msg.writer().writeShort(_char.pPoint);
            msg.writer().writeShort(_char.potential[0]);// oday
            msg.writer().writeShort(_char.potential[1]);// oday
            msg.writer().writeInt(_char.potential[2]);
            msg.writer().writeInt(_char.potential[3]);
            msg.writer().writeShort(_char.sPoint);
            msg.writer().writeByte(_char.ASkill.size());
            for (byte i = 0; i < _char.ASkill.size(); ++i) {
                msg.writer().writeShort(_char.ASkill.get(i).skillId);
            }
            msg.writer().writeInt(_char.user.player.xu);
            msg.writer().writeInt(_char.user.player.yen);
            msg.writer().writeInt(_char.user.luong);
            msg.writer().writeByte(_char.user.player.ItemBag.length);
            for (byte i = 0; i < _char.user.player.ItemBag.length; ++i) {
                final Item item = _char.user.player.ItemBag[i];
                if (item != null) {
                    msg.writer().writeShort(item.itemId);
                    msg.writer().writeBoolean(item.isLock);
                    if (item.isTypeBody() || item.isTypeMounts() || item.isTypeNgocKham()) {
                        msg.writer().writeByte(item.upgrade);
                    }
                    msg.writer().writeBoolean(item.isExpires);
                    msg.writer().writeShort(item.quantity);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            for (int j = 0; j < 16; ++j) {
                final Item item = _char.ItemBody[j];
                if (item != null) {
                    msg.writer().writeShort(item.itemId);
                    msg.writer().writeByte(item.upgrade);
                    msg.writer().writeByte(item.sys);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            msg.writer().writeBoolean(_char.isHuman);
            msg.writer().writeBoolean(_char.isNhanban);
            msg.writer().writeShort(CaiTrang.Head(_char, _char.partCaiTrang()));
            msg.writer().writeShort(CaiTrang.Weapon(_char.partCaiTrang()));
            msg.writer().writeShort(CaiTrang.Body(_char, _char.partCaiTrang()));
            msg.writer().writeShort(CaiTrang.Leg(_char, _char.partCaiTrang()));
            for (byte i = 0; i < _char.setThoiTrang.length; ++i) {
                msg.writer().writeShort(_char.setThoiTrang[i]);
            }
            for (int j = 0; j < 16; ++j) {
                final Item item = _char.ItemBody[j + 16];
                if (item != null) {
                    msg.writer().writeShort(item.itemId);
                    msg.writer().writeByte(item.upgrade);
                    msg.writer().writeByte(item.sys);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            if (_char.taskHangNgay[5] == 1 && _char.taskHangNgay[0] != -1) {
                getTaskOrder(_char, (byte) 0);
            }
            if (_char.taskTaThu[5] == 1 && _char.taskTaThu[0] != -1) {
                getTaskOrder(_char, (byte) 1);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MELoadClass(final Char _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-126));
            _char.writeParam(msg);
            msg.writer().writeShort(_char.potential[0]);// oday
            msg.writer().writeShort(_char.potential[1]);// oday
            msg.writer().writeInt(_char.potential[2]);
            msg.writer().writeInt(_char.potential[3]);
            msg.writer().writeByte(_char.nClass);
            msg.writer().writeShort(_char.sPoint);
            msg.writer().writeShort(_char.pPoint);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void potentialUp(final Char _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-109));
            _char.writeParam(msg);
            msg.writer().writeShort(_char.pPoint);
            msg.writer().writeShort(_char.potential[0]);// oday
            msg.writer().writeShort(_char.potential[1]);// oday
            msg.writer().writeInt(_char.potential[2]);
            msg.writer().writeInt(_char.potential[3]);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MELoadSkill(final Char _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-125));
            _char.writeParam(msg);
            msg.writer().writeShort(_char.sPoint);
            msg.writer().writeByte(_char.ASkill.size());
            for (byte i = 0; i < _char.ASkill.size(); ++i) {
                final Skill skill = _char.ASkill.get(i);
                if (skill != null) {
                    msg.writer().writeShort(skill.skillId);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MELoadLevel(final Char _char, final long xp) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-124));
            _char.writeParam(msg);
            msg.writer().writeLong(xp);
            msg.writer().writeShort(_char.sPoint);
            msg.writer().writeShort(_char.pPoint);
            msg.writer().writeShort(_char.potential[0]);// oday
            msg.writer().writeShort(_char.potential[1]);// oday
            msg.writer().writeInt(_char.potential[2]);
            msg.writer().writeInt(_char.potential[3]);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MELoadHP(final Char _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-122));
            msg.writer().writeInt(_char.cHP);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MELoadMP(final Char _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-121));
            msg.writer().writeInt(_char.cMP);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerLoadAll(final Char _char, final Char player) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-120));
            msg.writer().writeInt(player.charID);
            msg.writer().writeUTF(player.cClanName);
            if (!player.cClanName.isEmpty()) {
                msg.writer().writeByte(player.ctypeClan);
            }
            msg.writer().writeBoolean(player.isDisable);
            msg.writer().writeByte(player.cTypePk);
            msg.writer().writeByte(player.nClass);
            msg.writer().writeByte(player.cgender);
            if (CaiTrang.Head(_char, player.partCaiTrang()) != -1) {
                msg.writer().writeShort(CaiTrang.Head(_char, player.partCaiTrang()));
            } else if (player.ItemBody[11] != null) {
                msg.writer().writeShort(player.ItemBody[11].template.part);
            } else {
                msg.writer().writeShort(player.head);
            }
            msg.writer().writeUTF(player.cName);
            msg.writer().writeInt(player.cHP);
            msg.writer().writeInt(player.cMaxHP());
            msg.writer().writeByte(player.cLevel);
            if (player.ItemBody[1] != null) {
                msg.writer().writeShort(player.ItemBody[1].template.part);
            } else {
                msg.writer().writeShort(-1);
            }
            if (player.ItemBody[2] != null) {
                msg.writer().writeShort(player.ItemBody[2].template.part);
            } else {
                msg.writer().writeShort(-1);
            }
            if (player.ItemBody[6] != null) {
                msg.writer().writeShort(player.ItemBody[6].template.part);
            } else {
                msg.writer().writeShort(-1);
            }
            msg.writer().writeByte(-1);
            msg.writer().writeShort(player.cx);
            msg.writer().writeShort(player.cy);
            msg.writer().writeShort(player.eff5BuffHp());
            msg.writer().writeShort(player.eff5BuffMp());
            msg.writer().writeByte(player.aEff.size());
            for (byte i = 0; i < player.aEff.size(); ++i) {
                final Effect effect = player.aEff.get(i);
                msg.writer().writeByte(effect.template.id);
                if (effect.timeStart != -1) {
                    msg.writer().writeInt((int) (System.currentTimeMillis() / 1000L - effect.timeStart));
                } else {
                    msg.writer().writeInt(0);
                }
                msg.writer().writeInt(effect.timeLenght);
                msg.writer().writeShort(effect.param);
            }
            msg.writer().writeBoolean(player.isHuman);
            msg.writer().writeBoolean(player.isNhanban);
            msg.writer().writeShort(CaiTrang.Head(_char, player.partCaiTrang()));
            msg.writer().writeShort(CaiTrang.Weapon(player.partCaiTrang()));
            msg.writer().writeShort(CaiTrang.Body(_char, player.partCaiTrang()));
            msg.writer().writeShort(CaiTrang.Leg(_char, player.partCaiTrang()));
            for (byte i = 0; i < player.setThoiTrang.length; ++i) {
                msg.writer().writeShort(player.setThoiTrang[i]);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerLoadLevel(final Char _char, final int charID, final int cHP, final int cMaxHP, final int level) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-128));
            msg.writer().writeInt(charID);
            msg.writer().writeInt(cHP);
            msg.writer().writeInt(cMaxHP);
            msg.writer().writeByte(level);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerLoadVukhi(final Char _char, final int charID, final int cHP, final int cMaxHP, final int eff5BuffHp, final int eff5BuffMp, final short wp) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-117));
            msg.writer().writeInt(charID);
            msg.writer().writeInt(cHP);
            msg.writer().writeInt(cMaxHP);
            msg.writer().writeShort(eff5BuffHp);
            msg.writer().writeShort(eff5BuffMp);
            msg.writer().writeShort(wp);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerLoadAo(final Char _char, final int charID, final int cHP, final int cMaxHP, final int eff5BuffHp, final int eff5BuffMp, final short body) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-116));
            msg.writer().writeInt(charID);
            msg.writer().writeInt(cHP);
            msg.writer().writeInt(cMaxHP);
            msg.writer().writeShort(eff5BuffHp);
            msg.writer().writeShort(eff5BuffMp);
            msg.writer().writeShort(body);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerLoadQuan(final Char _char, final int charID, final int cHP, final int cMaxHP, final int eff5BuffHp, final int eff5BuffMp, final short leg) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-113));
            msg.writer().writeInt(charID);
            msg.writer().writeInt(cHP);
            msg.writer().writeInt(cMaxHP);
            msg.writer().writeShort(eff5BuffHp);
            msg.writer().writeShort(eff5BuffMp);
            msg.writer().writeShort(leg);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerLoadBody(final Char _char, final int charID, final int cHP, final int cMaxHP, final int eff5BuffHp, final int eff5BuffMp) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-112));
            msg.writer().writeInt(charID);
            msg.writer().writeInt(cHP);
            msg.writer().writeInt(cMaxHP);
            msg.writer().writeShort(eff5BuffHp);
            msg.writer().writeShort(eff5BuffMp);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerLoadHP(final Char _char, final int charID, final int cHP) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-111));
            msg.writer().writeInt(charID);
            msg.writer().writeInt(cHP);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerLoadLive(final Char _char, final int charID, final int cHP, final int cMaxHP, final short cx, final short cy) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-110));
            msg.writer().writeInt(charID);
            msg.writer().writeInt(cHP);
            msg.writer().writeInt(cMaxHP);
            msg.writer().writeShort(cx);
            msg.writer().writeShort(cy);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void BagSort(final Char _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-107));
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void BoxSort(final Char _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-106));
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void BoxIn(final Char _char, final int coin) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-105));
            msg.writer().writeInt(coin);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void BoxOut(final Char _char, final int coin) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-104));
            msg.writer().writeInt(coin);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void useBookSkill(final Char _char, final byte indexUI, final short skillId) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-102));
            msg.writer().writeByte(indexUI);
            msg.writer().writeShort(skillId);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MeAddEfect(final Char _char, final Effect effect) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-101));
            msg.writer().writeByte(effect.template.id);
            if (effect.timeStart != -1) {
                msg.writer().writeInt((int) (System.currentTimeMillis() / 1000L - effect.timeStart));
            } else {
                msg.writer().writeInt(0);
            }
            msg.writer().writeInt(effect.timeLenght);
            msg.writer().writeShort(effect.param);
            if (effect.template.type == 2 || effect.template.type == 3 || effect.template.type == 14) {
                msg.writer().writeShort(_char.cx);
                msg.writer().writeShort(_char.cy);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MeEditEfect(final Char _char, final Effect effect) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-100));
            msg.writer().writeByte(effect.template.id);
            if (effect.timeStart != -1) {
                msg.writer().writeInt((int) (System.currentTimeMillis() / 1000L - effect.timeStart));
            } else {
                msg.writer().writeInt(0);
            }
            msg.writer().writeInt(effect.timeLenght);
            msg.writer().writeShort(effect.param);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MeRemoveEfect(final Char _char, final Effect effect) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-99));
            msg.writer().writeByte(effect.template.id);
            if (effect.template.type == 0 || effect.template.type == 12) {
                msg.writer().writeInt(_char.cHP);
                msg.writer().writeInt(_char.cMP);
            } else if (effect.template.type == 11) {
                msg.writer().writeShort(_char.cx);
                msg.writer().writeShort(_char.cy);
            } else if (effect.template.type == 4 || effect.template.type == 13 || effect.template.type == 17) {
                msg.writer().writeInt(_char.cHP);
            } else if (effect.template.type == 23) {
                msg.writer().writeInt(_char.cHP);
                msg.writer().writeInt(_char.cMaxHP());
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerAddEfect(final Char _char, final Char player, final Effect effect) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-98));
            msg.writer().writeInt(player.charID);
            msg.writer().writeByte(effect.template.id);
            if (effect.timeStart != -1) {
                msg.writer().writeInt((int) (System.currentTimeMillis() / 1000L - effect.timeStart));
            } else {
                msg.writer().writeInt(0);
            }
            msg.writer().writeInt(effect.timeLenght);
            msg.writer().writeShort(effect.param);
            if (effect.template.type == 2 || effect.template.type == 3 || effect.template.type == 14) {
                msg.writer().writeShort(player.cx);
                msg.writer().writeShort(player.cy);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerEditEfect(final Char _char, final int charId, final Effect effect) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-97));
            msg.writer().writeInt(charId);
            msg.writer().writeByte(effect.template.id);
            if (effect.timeStart != -1) {
                msg.writer().writeInt((int) (System.currentTimeMillis() / 1000L - effect.timeStart));
            } else {
                msg.writer().writeInt(0);
            }
            msg.writer().writeInt(effect.timeLenght);
            msg.writer().writeShort(effect.param);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerRemoveEfect(final Char _char, final Char player, final Effect effect) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-96));
            msg.writer().writeInt(player.charID);
            msg.writer().writeByte(effect.template.id);
            if (effect.template.type == 0 || effect.template.type == 12) {
                msg.writer().writeInt(player.cHP);
                msg.writer().writeInt(player.cMP);
            } else if (effect.template.type == 11) {
                msg.writer().writeShort(player.cx);
                msg.writer().writeShort(player.cy);
            } else if (effect.template.type == 4 || effect.template.type == 13 || effect.template.type == 17) {
                msg.writer().writeInt(player.cHP);
            } else if (effect.template.type == 23) {
                msg.writer().writeInt(player.cHP);
                msg.writer().writeInt(player.cMaxHP());
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void updateTypePk(final Char _char, final int charId, final byte typePk) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-92));
            msg.writer().writeInt(charId);
            msg.writer().writeByte(typePk);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void mapTime(final Char _char, final int timeLengthMap) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-95));
            msg.writer().writeInt(timeLengthMap);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void npcPlayerUpdate(final Char _char, final byte index, final byte statusMe) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-94));
            msg.writer().writeByte(index);
            msg.writer().writeByte(statusMe);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void updateBagCount(final Char _char, final byte count, final byte indexUI) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-91));
            msg.writer().writeByte(count);
            msg.writer().writeByte(indexUI);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void requestFriend(final Player _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-85));
            for (short i = 0; i < _char.vFriend.size(); ++i) {
                final Friend friend = _char.vFriend.get(i);
                if (friend != null && friend.type != 2) {
                    msg.writer().writeUTF(friend.friendName);
                    byte type = friend.type;
                    if (friend.type == 1) {
                        final Player player = Client.getPlayer(friend.friendName);
                        if (player != null) {
                            type = 3;
                        }
                    }
                    msg.writer().writeByte(type);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ItemBodyClear(final Char _char, final byte indexUI) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-80));
            msg.writer().writeByte(indexUI);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void requestEnemies(final Player _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-84));
            for (short i = 0; i < _char.vFriend.size(); ++i) {
                final Friend friend = _char.vFriend.get(i);
                if (friend != null && friend.type == 2) {
                    msg.writer().writeUTF(friend.friendName);
                    byte type = friend.type;
                    final Player player = Client.getPlayer(friend.friendName);
                    if (player != null) {
                        type = 3;
                    }
                    msg.writer().writeByte(type);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void removeFriend(final Char _char, final String name) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-83));
            msg.writer().writeUTF(name);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void removeEnemies(final Char _char, final String name) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-82));
            msg.writer().writeUTF(name);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void findParty(final Char _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-77));
            for (short i = 0; i < _char.tileMap.numParty; ++i) {
                final Party party = _char.tileMap.aParty.get(i);
                if (party != null) {
                    final Char player = party.findChar(party.charID);
                    if (player != null) {
                        msg.writer().writeByte(player.nClass);
                        msg.writer().writeByte(player.cLevel);
                        msg.writer().writeUTF(player.cName);
                        msg.writer().writeByte(party.numPlayer);
                    }
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void lockParty(final Char _char, final boolean isLock) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-76));
            msg.writer().writeBoolean(isLock);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void showWait(final Char _char, final String text) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-74));
            msg.writer().writeUTF(text);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void thieuDot(final Char _char, final short mobId) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-73));
            msg.writer().writeByte(mobId);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void loadGold(final Player _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-72));
            msg.writer().writeInt(_char.user.luong);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void upGold(final Player _char, final int num) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-71));
            msg.writer().writeInt(num);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MELoadThuNuoi(final Char _char, final Mob mob) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-69));
            if (mob != null) {
                msg.writer().writeByte(mob.templateId);
                msg.writer().writeByte(Mob.arrMobTemplate[mob.templateId].isBoss ? 1 : 0);
            } else {
                msg.writer().writeByte(0);
                msg.writer().writeByte(0);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerLoadThuNuoi(final Char _char, final int charID, final Mob mob) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-68));
            msg.writer().writeInt(charID);
            if (mob != null) {
                msg.writer().writeByte(mob.templateId);
                msg.writer().writeByte(Mob.arrMobTemplate[mob.templateId].isBoss ? 1 : 0);
            } else {
                msg.writer().writeByte(0);
                msg.writer().writeByte(0);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void loadRMS(final Char _char, final byte[] array, final String key) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-65));
            msg.writer().writeUTF(key);
            NinjaUtil.writeByteArray(msg, array);
            msg.writer().writeByte(0);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerLoadMatNa(final Char _char, final int charID, final int cHP, final int cMaxHP, final int eff5BuffHp, final int eff5BuffMp, final short head) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-64));
            msg.writer().writeInt(charID);
            msg.writer().writeInt(cHP);
            msg.writer().writeInt(cMaxHP);
            msg.writer().writeShort(eff5BuffHp);
            msg.writer().writeShort(eff5BuffMp);
            msg.writer().writeShort(head);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void clanInvite(final Char _char, final int charID, final String clanName) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-63));
            msg.writer().writeInt(charID);
            msg.writer().writeUTF(clanName);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void clanInviteAccept(final Char _char, final int charID, final String clanName, final byte typeClan) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-62));
            msg.writer().writeInt(charID);
            msg.writer().writeUTF(clanName);
            msg.writer().writeByte(typeClan);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void clanPlease(final Char _char, final int charID) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-61));
            msg.writer().writeInt(charID);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void clanPleaseAccept(final Char _char, final int charID, final String clanName, final byte typeClan) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-62));
            msg.writer().writeInt(charID);
            msg.writer().writeUTF(clanName);
            msg.writer().writeByte(typeClan);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerLoadAoChoang(final Char _char, final int charID, final int cHP, final int cMaxHP, final short coat) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-56));
            msg.writer().writeInt(charID);
            msg.writer().writeInt(cHP);
            msg.writer().writeInt(cMaxHP);
            msg.writer().writeShort(coat);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerLoadGiaToc(final Char _char, final int charID, final int cHP, final int cMaxHP, final short glove) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-55));
            msg.writer().writeInt(charID);
            msg.writer().writeInt(cHP);
            msg.writer().writeInt(cMaxHP);
            msg.writer().writeShort(glove);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void LoadThuCuoi(final Char _char, final int charID, final Item[] ItemMounts) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) (-54));
            msg.writer().writeInt(charID);
            for (byte i = 0; i < ItemMounts.length; ++i) {
                final Item item = ItemMounts[i];
                if (item != null) {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeByte(item.upgrade);
                    msg.writer().writeLong(item.expires);
                    msg.writer().writeByte(item.sys);
                    msg.writer().writeByte(item.options.size());
                    for (short j = 0; j < item.options.size(); ++j) {
                        final ItemOption option = item.options.get(j);
                        msg.writer().writeByte(option.optionTemplate.id);
                        msg.writer().writeInt(option.param);
                    }
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void updateInfoMe(final Char _char) {
        Message msg = null;
        try {
            msg = messageSubCommand((byte) 115);
            msg.writer().writeInt(_char.charID);
            msg.writer().writeUTF(_char.cClanName);
            if (!_char.cClanName.isEmpty()) {
                msg.writer().writeByte(_char.ctypeClan);
            }
            msg.writer().writeByte(_char.ctaskId);
            msg.writer().writeByte(_char.cgender);
            if (CaiTrang.Head(_char, _char.partCaiTrang()) != -1) {
                msg.writer().writeShort(CaiTrang.Head(_char, _char.partCaiTrang()));
            } else if (_char.ItemBody[11] != null) {
                msg.writer().writeShort(_char.ItemBody[11].template.part);
            } else {
                msg.writer().writeShort(_char.head);
            }
            msg.writer().writeByte(_char.cSpeed());
            msg.writer().writeUTF(_char.cName);
            msg.writer().writeByte(_char.cPk);
            msg.writer().writeByte(_char.cTypePk);
            msg.writer().writeInt(_char.cMaxHP());
            msg.writer().writeInt(_char.cHP);
            msg.writer().writeInt(_char.cMaxMP());
            msg.writer().writeInt(_char.cMP);
            msg.writer().writeLong(_char.cEXP);
            msg.writer().writeLong(_char.cExpDown);
            msg.writer().writeShort(_char.eff5BuffHp());
            msg.writer().writeShort(_char.eff5BuffMp());
            msg.writer().writeByte(_char.nClass);
            msg.writer().writeShort(_char.pPoint);
            msg.writer().writeShort(_char.potential[0]);// oday
            msg.writer().writeShort(_char.potential[1]);// oday
            msg.writer().writeInt(_char.potential[2]);
            msg.writer().writeInt(_char.potential[3]);
            msg.writer().writeShort(_char.sPoint);
            msg.writer().writeByte(_char.ASkill.size());
            for (byte i = 0; i < _char.ASkill.size(); ++i) {
                msg.writer().writeShort(_char.ASkill.get(i).skillId);
            }
            msg.writer().writeInt(_char.user.player.xu);
            msg.writer().writeInt(_char.user.player.yen);
            msg.writer().writeInt(_char.user.luong);
            msg.writer().writeByte(_char.user.player.ItemBag.length);
            for (byte i = 0; i < _char.user.player.ItemBag.length; ++i) {
                final Item item = _char.user.player.ItemBag[i];
                if (item != null) {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeBoolean(item.isLock);
                    if (item.isTypeBody() || item.isTypeMounts() || item.isTypeNgocKham()) {
                        msg.writer().writeByte(item.upgrade);
                    }
                    msg.writer().writeBoolean(item.isExpires);
                    msg.writer().writeShort(item.quantity);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            for (int j = 0; j < 16; ++j) {
                final Item item = _char.ItemBody[j];
                if (item != null) {
                    msg.writer().writeShort(item.itemId);
                    msg.writer().writeByte(item.upgrade);
                    msg.writer().writeByte(item.sys);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            msg.writer().writeBoolean(_char.isHuman);
            msg.writer().writeBoolean(_char.isNhanban);
            msg.writer().writeShort(CaiTrang.Head(_char, _char.partCaiTrang()));
            msg.writer().writeShort(CaiTrang.Weapon(_char.partCaiTrang()));
            msg.writer().writeShort(CaiTrang.Body(_char, _char.partCaiTrang()));
            msg.writer().writeShort(CaiTrang.Leg(_char, _char.partCaiTrang()));
            for (byte i = 0; i < _char.setThoiTrang.length; ++i) {
                msg.writer().writeShort(_char.setThoiTrang[i]);
            }
            for (int j = 0; j < 16; ++j) {
                final Item item = _char.ItemBody[j + 16];
                if (item != null) {
                    msg.writer().writeShort(item.itemId);
                    msg.writer().writeByte(item.upgrade);
                    msg.writer().writeByte(item.sys);
                } else {
                    msg.writer().writeShort(-1);
                }
            }

            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void initSelectChar(final User user) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-126));
            msg.writer().writeByte(user.numPlayer);
            for (byte num = 0; num < user.numPlayer; ++num) {
                final Player _char = user.players[num];
                msg.writer().writeByte(_char.cgender);
                msg.writer().writeUTF(_char.cName);
                msg.writer().writeUTF(GameScr.nClasss[_char.nClass].name);
                msg.writer().writeByte(_char.cLevel);
                if (CaiTrang.Head(_char, _char.partCaiTrang()) != -1) {
                    msg.writer().writeShort(CaiTrang.Head(_char, _char.partCaiTrang()));
                } else if (_char.ItemBody[11] != null) {
                    msg.writer().writeShort(_char.ItemBody[11].template.part);
                } else {
                    msg.writer().writeShort(_char.head);
                }
                if (CaiTrang.Weapon(_char.partCaiTrang()) != -1) {
                    msg.writer().writeShort(CaiTrang.Weapon(_char.partCaiTrang()));
                } else if (_char.ItemBody[1] != null) {
                    msg.writer().writeShort(_char.ItemBody[1].template.part);
                } else {
                    msg.writer().writeShort(-1);
                }
                if (CaiTrang.Body(_char, _char.partCaiTrang()) != -1) {
                    msg.writer().writeShort(CaiTrang.Body(_char, _char.partCaiTrang()));
                } else if (_char.ItemBody[2] != null) {
                    msg.writer().writeShort(_char.ItemBody[2].template.part);
                } else {
                    msg.writer().writeShort(-1);
                }
                if (CaiTrang.Leg(_char, _char.partCaiTrang()) != -1) {
                    msg.writer().writeShort(CaiTrang.Leg(_char, _char.partCaiTrang()));
                } else if (_char.ItemBody[6] != null) {
                    msg.writer().writeShort(_char.ItemBody[6].template.part);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void requestLogin(final Session_ME session) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-123));
            msg.writer().writeByte(GameScr.vData);
            msg.writer().writeByte(GameScr.vMap);
            msg.writer().writeByte(GameScr.vSkill);
            msg.writer().writeByte(GameScr.vItem);
            msg.writer().writeByte(GameScr.head_Jump.length);
            for (int i = 0; i < GameScr.head_Jump.length; i++) {
                msg.writer().writeByte(GameScr.head_Jump[i].charPart.length * 3 + 2);
                msg.writer().writeShort(GameScr.head_Jump[i].idPart);
                msg.writer().writeShort(GameScr.head_Jump[i].icon);
                for (int j = 0; j < GameScr.head_Jump[i].charPart.length; j++) {
                    msg.writer().writeShort(GameScr.head_Jump[i].charPart[j].idItem);
                    msg.writer().writeShort(GameScr.head_Jump[i].charPart[j].dx);
                    msg.writer().writeShort(GameScr.head_Jump[i].charPart[j].dy);
                }
            }
            for (int i = 0; i < GameScr.head_Normal.length; i++) {
                msg.writer().writeByte(GameScr.head_Normal[i].charPart.length * 3 + 2);
                msg.writer().writeShort(GameScr.head_Normal[i].idPart);
                msg.writer().writeShort(GameScr.head_Normal[i].icon);
                for (int j = 0; j < GameScr.head_Normal[i].charPart.length; j++) {
                    msg.writer().writeShort(GameScr.head_Normal[i].charPart[j].idItem);
                    msg.writer().writeShort(GameScr.head_Normal[i].charPart[j].dx);
                    msg.writer().writeShort(GameScr.head_Normal[i].charPart[j].dy);
                }
            }
            for (int i = 0; i < GameScr.head_Boc_Dau.length; i++) {
                msg.writer().writeByte(GameScr.head_Boc_Dau[i].charPart.length * 3 + 2);
                msg.writer().writeShort(GameScr.head_Boc_Dau[i].idPart);
                msg.writer().writeShort(GameScr.head_Boc_Dau[i].icon);
                for (int j = 0; j < GameScr.head_Boc_Dau[i].charPart.length; j++) {
                    msg.writer().writeShort(GameScr.head_Boc_Dau[i].charPart[j].idItem);
                    msg.writer().writeShort(GameScr.head_Boc_Dau[i].charPart[j].dx);
                    msg.writer().writeShort(GameScr.head_Boc_Dau[i].charPart[j].dy);
                }
            }
            msg.writer().writeByte(GameScr.Legs.length * 2);
            for (int i = 0; i < GameScr.Legs.length; i++) {
                msg.writer().writeShort(GameScr.Legs[i].idPart);
                msg.writer().writeShort(GameScr.Legs[i].idImg);
            }
            msg.writer().writeByte(GameScr.body_Jump.length);
            for (int i = 0; i < GameScr.body_Jump.length; i++) {
                msg.writer().writeByte(GameScr.body_Jump[i].charPart.length * 3 + 2);
                msg.writer().writeShort(GameScr.body_Jump[i].idPart);
                msg.writer().writeShort(GameScr.body_Jump[i].icon);
                for (int j = 0; j < GameScr.body_Jump[i].charPart.length; j++) {
                    msg.writer().writeShort(GameScr.body_Jump[i].charPart[j].idItem);
                    msg.writer().writeShort(GameScr.body_Jump[i].charPart[j].dx);
                    msg.writer().writeShort(GameScr.body_Jump[i].charPart[j].dy);
                }
            }
            for (int i = 0; i < GameScr.body_Normal.length; i++) {
                msg.writer().writeByte(GameScr.body_Normal[i].charPart.length * 3 + 2);
                msg.writer().writeShort(GameScr.body_Normal[i].idPart);
                msg.writer().writeShort(GameScr.body_Normal[i].icon);
                for (int j = 0; j < GameScr.body_Normal[i].charPart.length; j++) {
                    msg.writer().writeShort(GameScr.body_Normal[i].charPart[j].idItem);
                    msg.writer().writeShort(GameScr.body_Normal[i].charPart[j].dx);
                    msg.writer().writeShort(GameScr.body_Normal[i].charPart[j].dy);
                }
            }
            for (int i = 0; i < GameScr.body_Boc_Dau.length; i++) {
                msg.writer().writeByte(GameScr.body_Boc_Dau[i].charPart.length * 3 + 2);
                msg.writer().writeShort(GameScr.body_Boc_Dau[i].idPart);
                msg.writer().writeShort(GameScr.body_Boc_Dau[i].icon);
                for (int j = 0; j < GameScr.body_Boc_Dau[i].charPart.length; j++) {
                    msg.writer().writeShort(GameScr.body_Boc_Dau[i].charPart[j].idItem);
                    msg.writer().writeShort(GameScr.body_Boc_Dau[i].charPart[j].dx);
                    msg.writer().writeShort(GameScr.body_Boc_Dau[i].charPart[j].dy);
                }
            }
            msg.writer().writeByte(GameScr.mount_New.length);
            for (int i = 0; i < GameScr.mount_New.length; i++) {
                msg.writer().writeShort(GameScr.mount_New[i].idItem);
                for (int j = 0; j < 6; j++) {
                    msg.writer().writeByte(GameScr.mount_New[i].Frame[j].count);
                    for (int k = 0; k < GameScr.mount_New[i].Frame[j].count; k++) {
                        msg.writer().writeShort(GameScr.mount_New[i].Frame[j].idIcon[k]);
                    }
                }
            }
            //final byte[] ab = NinjaUtil.getFile("cache/request");
            //msg.writer().write(ab);
            session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }
    
    protected static void sendData(final User user) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-122));
            msg.writer().writeByte(GameScr.vData);
            byte[] ab = NinjaUtil.getFile("cache/data/nj_arrow");
            NinjaUtil.writeByteArray(msg, ab);
            ab = NinjaUtil.getFile("cache/data/nj_effect");
            NinjaUtil.writeByteArray(msg, ab);
            ab = NinjaUtil.getFile("cache/data/nj_image");
            NinjaUtil.writeByteArray(msg, ab);
            ab = NinjaUtil.getFile("cache/data/nj_part");
            NinjaUtil.writeByteArray(msg, ab);
            ab = NinjaUtil.getFile("cache/data/nj_skill");
            NinjaUtil.writeByteArray(msg, ab);
            msg.writer().writeByte(GameScr.tasks.length);
            for (byte i = 0; i < GameScr.tasks.length; ++i) {
                msg.writer().writeByte(GameScr.tasks[i].length);
                for (byte j = 0; j < GameScr.tasks[i].length; ++j) {
                    msg.writer().writeByte(GameScr.tasks[i][j]);
                    msg.writer().writeByte(GameScr.mapTasks[i][j]);
                }
            }
            msg.writer().writeByte(GameScr.exps.length);
            for (short k = 0; k < GameScr.exps.length; ++k) {
                msg.writer().writeLong(GameScr.exps[k]);
            }
            msg.writer().writeByte(GameScr.crystals.length);
            for (byte l = 0; l < GameScr.crystals.length; ++l) {
                msg.writer().writeInt(GameScr.crystals[l]);
            }
            msg.writer().writeByte(GameScr.upClothe.length);
            for (byte l = 0; l < GameScr.upClothe.length; ++l) {
                msg.writer().writeInt(GameScr.upClothe[l]);
            }
            msg.writer().writeByte(GameScr.upAdorn.length);
            for (byte l = 0; l < GameScr.upAdorn.length; ++l) {
                msg.writer().writeInt(GameScr.upAdorn[l]);
            }
            msg.writer().writeByte(GameScr.upWeapon.length);
            for (byte l = 0; l < GameScr.upWeapon.length; ++l) {
                msg.writer().writeInt(GameScr.upWeapon[l]);
            }
            msg.writer().writeByte(GameScr.coinUpCrystals.length);
            for (byte l = 0; l < GameScr.coinUpCrystals.length; ++l) {
                msg.writer().writeInt(GameScr.coinUpCrystals[l]);
            }
            msg.writer().writeByte(GameScr.coinUpClothes.length);
            for (byte l = 0; l < GameScr.coinUpClothes.length; ++l) {
                msg.writer().writeInt(GameScr.coinUpClothes[l]);
            }
            msg.writer().writeByte(GameScr.coinUpAdorns.length);
            for (byte l = 0; l < GameScr.coinUpAdorns.length; ++l) {
                msg.writer().writeInt(GameScr.coinUpAdorns[l]);
            }
            msg.writer().writeByte(GameScr.coinUpWeapons.length);
            for (byte l = 0; l < GameScr.coinUpWeapons.length; ++l) {
                msg.writer().writeInt(GameScr.coinUpWeapons[l]);
            }
            msg.writer().writeByte(GameScr.goldUps.length);
            for (byte l = 0; l < GameScr.goldUps.length; ++l) {
                msg.writer().writeInt(GameScr.goldUps[l]);
            }
            msg.writer().writeByte(GameScr.maxPercents.length);
            for (byte l = 0; l < GameScr.maxPercents.length; ++l) {
                msg.writer().writeInt(GameScr.maxPercents[l]);
            }
            msg.writer().writeByte(Effect.effTemplates.length);
            for (short m = 0; m < Effect.effTemplates.length; ++m) {
                msg.writer().writeByte(Effect.effTemplates[m].id);
                msg.writer().writeByte(Effect.effTemplates[m].type);
                msg.writer().writeUTF(Mob.arrMobTemplate[m].name);
                msg.writer().writeShort(Effect.effTemplates[m].iconId);
            }
            user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void sendMap(final User user) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-121));
            msg.writer().writeByte(GameScr.vMap);
            msg.writer().writeByte(GameScr.mapTemplates.length);
            for (short i = 0; i < GameScr.mapTemplates.length; ++i) {
                msg.writer().writeUTF(GameScr.mapTemplates[i].mapName);
            }
            msg.writer().writeByte(Npc.arrNpcTemplate.length);
            for (byte j = 0; j < Npc.arrNpcTemplate.length; ++j) {
                msg.writer().writeUTF(Npc.arrNpcTemplate[j].name);
                msg.writer().writeShort(Npc.arrNpcTemplate[j].headId);
                msg.writer().writeShort(Npc.arrNpcTemplate[j].bodyId);
                msg.writer().writeShort(Npc.arrNpcTemplate[j].legId);
                msg.writer().writeByte(Npc.arrNpcTemplate[j].menu.length);
                for (short k = 0; k < Npc.arrNpcTemplate[j].menu.length; ++k) {
                    msg.writer().writeByte(Npc.arrNpcTemplate[j].menu[k].length);
                    for (short m = 0; m < Npc.arrNpcTemplate[j].menu[k].length; ++m) {
                        msg.writer().writeUTF(Npc.arrNpcTemplate[j].menu[k][m]);
                    }
                }
            }
            if (user.session.versionNja < 203) {
                if (Mob.arrMobTemplate.length > 255) {
                    msg.writer().writeByte(255);
                } else {
                    msg.writer().writeByte(Mob.arrMobTemplate.length);
                }
            } else {
                msg.writer().writeShort(Mob.arrMobTemplate.length);
            }
            for (short l = 0; l < Mob.arrMobTemplate.length; ++l) {
                msg.writer().writeByte(Mob.arrMobTemplate[l].type);
                msg.writer().writeUTF(Mob.arrMobTemplate[l].name);
                msg.writer().writeInt(Mob.arrMobTemplate[l].hp);
                msg.writer().writeByte(Mob.arrMobTemplate[l].rangeMove);
                msg.writer().writeByte(Mob.arrMobTemplate[l].speed);
            }
            user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void sendSkill(final User user) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-120));
            msg.writer().writeByte(GameScr.vSkill);
            msg.writer().writeByte(GameScr.sOptionTemplates.length);
            for (short i = 0; i < GameScr.sOptionTemplates.length; ++i) {
                msg.writer().writeUTF(GameScr.sOptionTemplates[i].name);
            }
            msg.writer().writeByte(GameScr.nClasss.length);
            for (short j = 0; j < GameScr.nClasss.length; ++j) {
                msg.writer().writeUTF(GameScr.nClasss[j].name);
                msg.writer().writeByte(GameScr.nClasss[j].skillTemplates.length);
                for (short k = 0; k < GameScr.nClasss[j].skillTemplates.length; ++k) {
                    msg.writer().writeByte(GameScr.nClasss[j].skillTemplates[k].id);
                    msg.writer().writeUTF(GameScr.nClasss[j].skillTemplates[k].name);
                    msg.writer().writeByte(GameScr.nClasss[j].skillTemplates[k].maxPoint);
                    msg.writer().writeByte(GameScr.nClasss[j].skillTemplates[k].type);
                    msg.writer().writeShort(GameScr.nClasss[j].skillTemplates[k].iconId);
                    msg.writer().writeUTF(GameScr.nClasss[j].skillTemplates[k].description);
                    msg.writer().writeByte(GameScr.nClasss[j].skillTemplates[k].skills.length);
                    for (short l = 0; l < GameScr.nClasss[j].skillTemplates[k].skills.length; ++l) {
                        msg.writer().writeShort(GameScr.nClasss[j].skillTemplates[k].skills[l].skillId);
                        msg.writer().writeByte(GameScr.nClasss[j].skillTemplates[k].skills[l].point);
                        msg.writer().writeByte(GameScr.nClasss[j].skillTemplates[k].skills[l].level);
                        msg.writer().writeShort(GameScr.nClasss[j].skillTemplates[k].skills[l].manaUse);
                        msg.writer().writeInt(GameScr.nClasss[j].skillTemplates[k].skills[l].coolDown);
                        msg.writer().writeShort(GameScr.nClasss[j].skillTemplates[k].skills[l].dx);
                        msg.writer().writeShort(GameScr.nClasss[j].skillTemplates[k].skills[l].dy);
                        msg.writer().writeByte(GameScr.nClasss[j].skillTemplates[k].skills[l].maxFight);
                        msg.writer().writeByte(GameScr.nClasss[j].skillTemplates[k].skills[l].options.length);
                        for (short m = 0; m < GameScr.nClasss[j].skillTemplates[k].skills[l].options.length; ++m) {
                            msg.writer().writeShort(GameScr.nClasss[j].skillTemplates[k].skills[l].options[m].param);
                            msg.writer().writeByte(GameScr.nClasss[j].skillTemplates[k].skills[l].options[m].optionTemplate.id);
                        }
                    }
                }
            }
            user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void sendItem(final User user) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-119));
            msg.writer().writeByte(GameScr.vItem);
            msg.writer().writeByte(GameScr.iOptionTemplates.length);
            for (short i = 0; i < GameScr.iOptionTemplates.length; ++i) {
                msg.writer().writeUTF(GameScr.iOptionTemplates[i].name);
                msg.writer().writeByte(GameScr.iOptionTemplates[i].type);
            }
            msg.writer().writeShort(GameScr.itemTemplates.length);
            for (short j = 0; j < GameScr.itemTemplates.length; ++j) {
                msg.writer().writeByte(GameScr.itemTemplates[j].type);
                msg.writer().writeByte(GameScr.itemTemplates[j].gender);
                msg.writer().writeUTF(GameScr.itemTemplates[j].name);
                msg.writer().writeUTF(GameScr.itemTemplates[j].description);
                msg.writer().writeByte(GameScr.itemTemplates[j].level);
                msg.writer().writeShort(GameScr.itemTemplates[j].iconID);
                msg.writer().writeShort(GameScr.itemTemplates[j].part);
                msg.writer().writeBoolean(GameScr.itemTemplates[j].isUpToUp);
            }
            user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void requestClanLog(final Char _char, final Clan clan) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-114));
            msg.writer().writeUTF(clan.log);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void requestClanInfo(final Char _char, final Clan clan) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-113));
            msg.writer().writeUTF(clan.name);
            msg.writer().writeUTF(clan.main_name);
            msg.writer().writeUTF(clan.assist_name);
            msg.writer().writeShort(clan.members.size());
            msg.writer().writeByte(clan.openDun);
            msg.writer().writeByte(clan.level);
            msg.writer().writeInt(clan.exp);
            msg.writer().writeInt(Clan.getexpNext(clan.level));
            msg.writer().writeInt(clan.coin);
            msg.writer().writeInt(Clan.getfreeCoin(clan.members.size()));
            msg.writer().writeInt(Clan.getCoinUp(clan.level));
            msg.writer().writeUTF(clan.reg_date);
            msg.writer().writeUTF(clan.alert);
            msg.writer().writeInt(clan.use_card);
            msg.writer().writeByte(clan.itemLevel);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void requestClanMember(final Char _char, final Clan clan) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-112));
            msg.writer().writeShort(clan.members.size());
            for (short i = 0; i < clan.members.size(); ++i) {
                msg.writer().writeByte(clan.members.get(i).nClass);
                msg.writer().writeByte(clan.members.get(i).cLevel);
                msg.writer().writeByte(clan.members.get(i).typeClan);
                msg.writer().writeUTF(clan.members.get(i).cName);
                msg.writer().writeInt(clan.members.get(i).pointClan);
                msg.writer().writeBoolean(Client.getPlayer(clan.members.get(i).cName) != null);
            }
            for (short i = 0; i < clan.members.size(); ++i) {
                msg.writer().writeInt(clan.members.get(i).pointClanWeek);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void requestClanItem(final Char _char, final Clan clan) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-111));
            msg.writer().writeByte(clan.items.size());
            for (byte i = 0; i < clan.items.size(); ++i) {
                final Item item = clan.items.get(i);
                if (item != null) {
                    msg.writer().writeShort(item.quantity);
                    msg.writer().writeShort(item.template.id);
                }
            }
            msg.writer().writeByte(clan.clan_thanThu.size());
            for (byte i = 0; i < clan.clan_thanThu.size(); ++i) {
                Clan_ThanThu clanThanThu = clan.clan_thanThu.get(i);
                final Item item = clanThanThu.items;
                int time = (int) (clanThanThu.time_aptrung - System.currentTimeMillis());
                if (clanThanThu.level > 0) {
                    msg.writer().writeUTF(item.template.name + " cấp " + clanThanThu.level);
                } else {
                    msg.writer().writeUTF(item.template.name); // tên
                }
                msg.writer().writeShort(clanThanThu.iconMini); // icon dưới
                msg.writer().writeShort(clanThanThu.iconThanThu); // icon trên
                if (clanThanThu.time_aptrung != 0) {
                    msg.writer().writeInt(time); // thời gian trứng nở
                } else {
                    msg.writer().writeInt((int) System.currentTimeMillis());
                }
                msg.writer().writeByte(item.options.size()); // options item size
                if (clanThanThu.time_aptrung != 0) {
                    msg.writer().writeUTF("Thời gian nở: ");
                } else {
                    for (ItemOption option : item.options) {
                        if (option.optionTemplate.id == 102) {
                            msg.writer().writeUTF("Sát thưởng lên quái " + option.param);
                        } else if (option.optionTemplate.id == 103) {
                            msg.writer().writeUTF("Sát thương lên người " + option.param);
                        }
                    }
                    msg.writer().writeInt(clanThanThu.curExp);
                    msg.writer().writeInt(clanThanThu.maxExp);
                }
                msg.writer().writeByte(clanThanThu.stars); // sao
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void requestMapTemplate(final Char _char, final int mapTemplateId) {
        Message msg = null;
        try {
            final TileMap tileMap = _char.tileMap;
            msg = messageNotMap((byte) (-109));
            msg.writer().writeByte(tileMap.tmw);
            msg.writer().writeByte(tileMap.tmh);
            for (short i = 0; i < tileMap.maps.length; ++i) {
                msg.writer().writeByte(tileMap.maps[i]);
            }
            loadInfoMap(_char, tileMap, msg);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void selectCard(final Char _char, final short[] arrTemplateId) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-72));
            for (byte i = 0; i < arrTemplateId.length; ++i) {
                msg.writer().writeShort(arrTemplateId[i]);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void requestModTemplate(final Char _char, final int modTemplateId) {
        Message msg = null;
        try {
            msg = new Message(-28);
            final byte[] ab = NinjaUtil.getFile("cache/mob/" + _char.user.session.zoomLevel + "/" + modTemplateId);
            msg.writer().write(ab);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void createClan(final Char _char, final String clanName) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-96));
            msg.writer().writeUTF(clanName);
            msg.writer().writeInt(_char.user.luong);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void clanMoveOutMem(final Char _char, final int charID) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-93));
            msg.writer().writeInt(charID);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void inputCoinClan(final Char _char) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-90));
            msg.writer().writeInt(_char.user.player.xu);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ConvertUpgrade(final Char _char, final Item item1, final Item item2) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-88));
            msg.writer().writeByte(item1.indexUI);
            msg.writer().writeByte(item1.upgrade);
            msg.writer().writeByte(item2.indexUI);
            msg.writer().writeByte(item2.upgrade);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void pointPB(final Char _char, final short point) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-84));
            msg.writer().writeShort(point);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void reviewPB(final Char _char) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-83));
            msg.writer().writeShort(_char.pointPB);
            msg.writer().writeShort(_char.timeFinishCave);
            msg.writer().writeByte(_char.countPartyPB);
            msg.writer().writeShort(BackCave.reWaed(_char.pointPB, _char.timeFinishCave, _char.countPartyPB));
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void pointChienTruong(final Char _char, final short point) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-81));
            msg.writer().writeShort(point);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void reviewChienTruong(final Char _char, final ChienTruong ct) {
        Message msg = null;
        try {
            String win = "";
            if (ct.typeWin != -1) {
                if (ct.typeWin == 0) {
                    win = String.format(Text.get(0, 343), Text.get(0, 342));
                } else if (ct.typeWin == 1) {
                    win = String.format(Text.get(0, 343), Text.get(0, 341));
                } else if (ct.typeWin == 2) {
                    win = Text.get(0, 345);
                }
            }
            final String str = String.format(Text.get(0, 336), win, ct.totalWhite, ct.totalBlack, Top.getStringBXH(_char, 5));
            msg = messageNotMap((byte) (-80));
            msg.writer().writeUTF(str);
            msg.writer().writeBoolean(_char.pointChienTruong != 0 && ct.level == 2 && _char.getCT() > 0);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void openClanItem(final Char _char, final byte itemLevel) {
        Message msg = null;
        try {
            msg = messageNotMap((byte) (-62));
            msg.writer().writeByte(itemLevel);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ServerMessage(final Char _char, final String text) {
        Message msg = null;
        try {
            if (text.length() > 0) {
                msg = new Message((byte) (-24));
                msg.writer().writeUTF(text);
                _char.user.session.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void chatMap(final Char _char, final int charID, final String text) {
        Message msg = null;
        try {
            if (text.length() > 0) {
                msg = new Message((byte) (-23));
                msg.writer().writeInt(charID);
                msg.writer().writeUTF(text);
                _char.user.session.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void chatPrivate(final Char _char, final String who, final String text) {
        Message msg = null;
        try {
            if (text.length() > 0) {
                msg = new Message((byte) (-22));
                msg.writer().writeUTF(who);
                msg.writer().writeUTF(text);
                _char.user.session.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void chatServer(final Player _char, final String who, final String text) {
        Message msg = null;
        try {
            if (text.length() > 0) {
                msg = new Message((byte) (-21));
                msg.writer().writeUTF(who);
                msg.writer().writeUTF(text);
                _char.user.session.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void chatParty(final Char _char, final String who, final String text) {
        Message msg = null;
        try {
            if (text.length() > 0) {
                msg = new Message((byte) (-20));
                msg.writer().writeUTF(who);
                msg.writer().writeUTF(text);
                _char.user.session.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void chatClan(final Char _char, final String who, final String text) {
        Message msg = null;
        try {
            if (text.length() > 0) {
                msg = new Message((byte) (-19));
                msg.writer().writeUTF(who);
                msg.writer().writeUTF(text);
                _char.user.session.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MapInfo(final TileMap tileMap, final Char _char) {
        Message msg = null;
        try {
            msg = new Message((byte) (-18));
            msg.writer().writeByte(tileMap.mapID);
            msg.writer().writeByte(tileMap.tileID);
            msg.writer().writeByte(tileMap.bgID);
            msg.writer().writeByte(tileMap.typeMap);
            msg.writer().writeUTF(tileMap.mapName);
            msg.writer().writeByte(tileMap.zoneID);
            loadInfoMap(_char, tileMap, msg);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ClearMap(final Char _char) {
        Message msg = null;
        try {
            msg = new Message((byte) (-16));
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void removeItemMap(final Char _char, final short itemMapID) {
        Message msg = null;
        try {
            msg = new Message((byte) (-15));
            msg.writer().writeShort(itemMapID);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MyPickItemMap(final Char _char, final ItemMap itemMap) {
        Message msg = null;
        try {
            msg = new Message((byte) (-14));
            msg.writer().writeShort(itemMap.itemMapID);
            if (itemMap.item.template.type == 19) {
                msg.writer().writeShort(itemMap.item.quantity);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerPickItemMap(final Char _char, final short itemMapID, final int charID) {
        Message msg = null;
        try {
            msg = new Message((byte) (-13));
            msg.writer().writeShort(itemMapID);
            msg.writer().writeInt(charID);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void throwItem(final Char _char, final byte indexUI, final ItemMap itemMap) {
        Message msg = null;
        try {
            msg = new Message((byte) (-12));
            msg.writer().writeByte(indexUI);
            msg.writer().writeShort(itemMap.itemMapID);
            msg.writer().writeShort(itemMap.x);
            msg.writer().writeShort(itemMap.y);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MeDie(final Char _char, final byte isNextXp) {
        if (_char.isBot) {
            return;
        }
        Message msg = null;
        try {
            msg = new Message((byte) (-11));
            msg.writer().writeByte(_char.cPk);
            msg.writer().writeShort(_char.cx);
            msg.writer().writeShort(_char.cy);
            if (isNextXp == 1) {
                msg.writer().writeLong(_char.cEXP);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MeLive(final Char _char) {
        Message msg = null;
        try {
            msg = new Message((byte) (-10));
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void upCoinLock(final Char _char, final int num) {
        Message msg = null;
        try {
            msg = new Message((byte) (-8));
            msg.writer().writeInt(num);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerThow(final Char _char, final int charID, final ItemMap itemMap) {
        Message msg = null;
        try {
            msg = new Message((byte) (-6));
            msg.writer().writeInt(charID);
            msg.writer().writeShort(itemMap.itemMapID);
            msg.writer().writeShort(itemMap.item.template.id);
            msg.writer().writeShort(itemMap.x);
            msg.writer().writeShort(itemMap.y);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MobLive(final Char _char, final Mob mob) {
        Message msg = null;
        try {
            msg = new Message((byte) (-5));
            msg.writer().writeByte(mob.mobId);
            msg.writer().writeByte(mob.sys);
            msg.writer().writeByte(mob.levelBoss);
            msg.writer().writeInt(mob.maxHp);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MobDie(final Char _char, final Mob mob, final int dame, final boolean flag, final ItemMap itemMap) {
        Message msg = null;
        try {
            msg = new Message((byte) (-4));
            msg.writer().writeByte(mob.mobId);
            msg.writer().writeInt(dame);
            msg.writer().writeBoolean(flag);
            if (itemMap != null) {
                msg.writer().writeShort(itemMap.itemMapID);
                msg.writer().writeShort(itemMap.item.template.id);
                msg.writer().writeShort(itemMap.x);
                msg.writer().writeShort(itemMap.y);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MobAttackMe(final Char _char, final short mobId, final int dame, final int dameMp, final short idSkill_atk, final byte typeAtk, final byte typeTool) {
        Message msg = null;
        try {
            msg = new Message((byte) (-3));
            msg.writer().writeByte(mobId);
            msg.writer().writeInt(dame);
            msg.writer().writeInt(dameMp);
            msg.writer().writeShort(idSkill_atk);
            msg.writer().writeByte(typeAtk);
            msg.writer().writeByte(typeTool);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MobAttackPlayer(final Char _char, final short mobId, final Char player, final short idSkill_atk, final byte typeAtk, final byte typeTool) {
        Message msg = null;
        try {
            msg = new Message((byte) (-2));
            msg.writer().writeByte(mobId);
            msg.writer().writeInt(player.charID);
            msg.writer().writeInt(player.cHP);
            msg.writer().writeInt(player.cMP);
            msg.writer().writeShort(idSkill_atk);
            msg.writer().writeByte(typeAtk);
            msg.writer().writeByte(typeTool);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MobHP(final Char _char, final Mob mob, final int dame, final boolean flag) {
        Message msg = null;
        try {
            msg = new Message((byte) (-1));
            msg.writer().writeByte(mob.mobId);
            msg.writer().writeInt(mob.hp);
            msg.writer().writeInt(dame);
            msg.writer().writeBoolean(flag);
            if (mob.levelBoss > 0) {
                msg.writer().writeByte(mob.levelBoss);
                msg.writer().writeInt(mob.maxHp);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerDie(final Char _char, final Char _charDie) {
        Message msg = null;
        try {
            msg = new Message((byte) 0);
            msg.writer().writeInt(_charDie.charID);
            msg.writer().writeByte(_charDie.cPk);
            msg.writer().writeShort(_charDie.cx);
            msg.writer().writeShort(_charDie.cy);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerMove(final Char _char, final int charID, final short cxMoveLast, final short cyMoveLast) {
        Message msg = null;
        try {
            msg = new Message((byte) 1);
            msg.writer().writeInt(charID);
            msg.writer().writeShort(cxMoveLast);
            msg.writer().writeShort(cyMoveLast);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerRemove(final Char _char, final int charID) {
//        if (_char.isBot) {
//            return;
//        }
        Message msg = null;
        try {
            msg = new Message((byte) 2);
            msg.writer().writeInt(charID);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerADD(final Char _char, final Char player) {
        Message msg = null;
        try {
            msg = new Message((byte) 3);
            msg.writer().writeInt(player.charID);
            writeCharInfo(player, msg);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerAttack(final Char _char, final int charID, final byte skill, final Mob[] arrMob, final Char[] arrChar) {
        Message msg = null;
        try {
            msg = new Message((byte) 4);
            msg.writer().writeInt(charID);
            msg.writer().writeByte(skill);
            byte num = 0;
            for (byte i = 0; i < arrMob.length; ++i) {
                if (arrMob[i] != null) {
                    ++num;
                }
            }
            msg.writer().writeByte(num);
            for (byte i = 0; i < arrMob.length; ++i) {
                if (arrMob[i] != null) {
                    msg.writer().writeByte(arrMob[i].mobId);
                }
            }
            for (byte i = 0; i < arrChar.length; ++i) {
                if (arrChar[i] != null) {
                    msg.writer().writeInt(arrChar[i].charID);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void upExp(final Char _char, final long xp) {
        Message msg = null;
        try {
            msg = new Message((byte) 5);
            msg.writer().writeLong(xp);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ItemMapAdd(final Char _char, final ItemMap itemMap, final byte[] imgCaptcha) {
        Message msg = null;
        try {
            msg = new Message((byte) 6);
            msg.writer().writeShort(itemMap.itemMapID);
            msg.writer().writeShort(itemMap.item.template.id);
            msg.writer().writeShort(itemMap.x);
            msg.writer().writeShort(itemMap.y);
            if (imgCaptcha != null) {
                NinjaUtil.writeByteArray(msg, imgCaptcha);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ItemBagADD(final Char _char, final Item item) {
        Message msg = null;
        try {
            msg = new Message((byte) 8);
            msg.writer().writeByte(item.indexUI);
            msg.writer().writeShort(item.template.id);
            msg.writer().writeBoolean(item.isLock);
            if (item.isTypeBody() || item.isTypeNgocKham()) {
                msg.writer().writeByte(item.upgrade);
            }
            msg.writer().writeBoolean(item.isExpires);
            msg.writer().writeShort(item.quantity);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ItemBagADDQuantity(final Char _char, final Item item) {
        Message msg = null;
        try {
            msg = new Message((byte) 9);
            msg.writer().writeByte(item.indexUI);
            msg.writer().writeShort(item.quantity);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ItemBagClear(final Char _char, final byte indexUI) {
        Message msg = null;
        try {
            msg = new Message((byte) 10);
            msg.writer().writeByte(indexUI);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ItemUse(final Char _char, final byte indexUI) {
        Message msg = null;
        try {
            msg = new Message((byte) 11);
            msg.writer().writeByte(indexUI);
            _char.writeParam(msg);
            msg.writer().writeInt(_char.eff5BuffHp());
            msg.writer().writeInt(_char.eff5BuffHp());
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void itemBodyToBag(final Char _char, final byte indexitem, final byte indexUI) {
        Message msg = null;
        try {
            msg = new Message((byte) 15);
            _char.writeParam(msg);
            msg.writer().writeShort(_char.eff5BuffHp());
            msg.writer().writeShort(_char.eff5BuffMp());
            msg.writer().writeByte(indexitem);
            msg.writer().writeByte(indexUI);
            msg.writer().writeShort(_char.head);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void itemBoxToBag(final Char _char, final byte indexitem, final byte indexUI) {
        Message msg = null;
        try {
            msg = new Message((byte) 16);
            msg.writer().writeByte(indexitem);
            msg.writer().writeByte(indexUI);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void itemBagToBox(final Char _char, final byte indexitem, final byte indexUI) {
        Message msg = null;
        try {
            msg = new Message((byte) 17);
            msg.writer().writeByte(indexitem);
            msg.writer().writeByte(indexUI);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ItemSale(final Char _char, final byte indexUI, final short quantity) {
        Message msg = null;
        try {
            msg = new Message((byte) 14);
            msg.writer().writeByte(indexUI);
            msg.writer().writeInt(_char.user.player.yen);
            if (quantity > 1) {
                msg.writer().writeShort(quantity);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void Buy(final Char _char) {
        Message msg = null;
        try {
            msg = new Message((byte) 13);
            msg.writer().writeInt(_char.user.player.xu);
            msg.writer().writeInt(_char.user.player.yen);
            msg.writer().writeInt(_char.user.luong);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ItemUseUptoup(final Char _char, final byte indexUI, final int quantity) {
        Message msg = null;
        try {
            msg = new Message((byte) 18);
            msg.writer().writeByte(indexUI);
            msg.writer().writeShort(quantity);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void Uppearl(final Char _char, final byte num, final byte indexUI, final Item item) {
        Message msg = null;
        try {
            msg = new Message((byte) 19);
            msg.writer().writeByte(num);
            msg.writer().writeByte(indexUI);
            msg.writer().writeShort(item.template.id);
            msg.writer().writeBoolean(item.isLock);
            msg.writer().writeBoolean(item.isExpires);
            msg.writer().writeInt(_char.user.player.xu);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void UppearlLock(final Char _char, final byte num, final byte indexUI, final Item item) {
        Message msg = null;
        try {
            msg = new Message((byte) 20);
            msg.writer().writeByte(num);
            msg.writer().writeByte(indexUI);
            msg.writer().writeShort(item.template.id);
            msg.writer().writeBoolean(item.isLock);
            msg.writer().writeBoolean(item.isExpires);
            msg.writer().writeInt(_char.user.player.yen);
            if (_char.user.player.yen <= 0) {
                msg.writer().writeInt(_char.user.player.xu);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void upGrade(final Char _char, final byte num, final byte upgrade) {
        Message msg = null;
        try {
            msg = new Message((byte) 21);
            msg.writer().writeByte(num);
            msg.writer().writeInt(_char.user.luong);
            msg.writer().writeInt(_char.user.player.xu);
            msg.writer().writeInt(_char.user.player.yen);
            if (upgrade != -1) {
                msg.writer().writeByte(upgrade);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void splitItem(final Char _char, final byte num, final Item[] arrItem) {
        Message msg = null;
        try {
            msg = new Message((byte) 22);
            msg.writer().writeByte(num);
            for (byte i = 0; i < arrItem.length; ++i) {
                final Item item = arrItem[i];
                if (item != null) {
                    msg.writer().writeByte(item.indexUI);
                    msg.writer().writeShort(item.template.id);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void pleaseInputParty(final Char _char, final String name) {
        Message msg = null;
        try {
            msg = new Message((byte) 23);
            msg.writer().writeUTF(name);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void openUI(final Char _char, final byte typeUI, final String svTitle, final String svAction) {
        Message msg = null;
        try {
            msg = new Message((byte) 30);
            msg.writer().writeByte(typeUI);
            if (svTitle != null) {
                msg.writer().writeUTF(svTitle);
                msg.writer().writeUTF(svAction);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void openUIBox(final Char _char) {
        Message msg = null;
        try {
            _char.user.player.menuCaiTrang = 0;
            Service.openUI(_char, (byte) 4, null, null);
            msg = new Message((byte) 31);
            msg.writer().writeInt(_char.user.player.xuBox);
            msg.writer().writeByte(_char.user.player.ItemBox.length);
            for (byte i = 0; i < _char.user.player.ItemBox.length; ++i) {
                final Item item = _char.user.player.ItemBox[i];
                if (item != null) {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeBoolean(item.isLock);
                    if (item.isTypeBody() || item.isTypeNgocKham()) {
                        msg.writer().writeByte(item.upgrade);
                    }
                    msg.writer().writeBoolean(item.isExpires);
                    msg.writer().writeShort(item.quantity);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void openUIShop(final Char _char, final byte typeUI, final Item[] arrItem) {
        Message msg = null;
        try {
            msg = new Message((byte) 33);
            msg.writer().writeByte(typeUI);
            msg.writer().writeByte(arrItem.length);
            for (short i = 0; i < arrItem.length; ++i) {
                final Item item = arrItem[i];
                if (item != null) {
                    msg.writer().writeByte(i);
                    msg.writer().writeShort(item.template.id);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void openUIZone(final Char _char, final Map map) {
        Message msg = null;
        try {
            msg = new Message((byte) 36);
            msg.writer().writeByte(map.tileMaps.length);
            for (byte i = 0; i < map.tileMaps.length; ++i) {
                msg.writer().writeByte(map.tileMaps[i].numPlayer);
                msg.writer().writeByte(map.tileMaps[i].numParty);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void openUITrade(final Char _char, final String name) {
        Message msg = null;
        try {
            msg = new Message((byte) 37);
            msg.writer().writeUTF(name);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void openUISay(final Char _char, final short npcTemplateId, final String chat) {
        Message msg = null;
        try {
            msg = new Message((byte) 38);
            msg.writer().writeShort(npcTemplateId);
            msg.writer().writeUTF(chat);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void openUIConfirm(final Char _char, final short npcTemplateId, final String chat, final String[] menu) {
        Message msg = null;
        try {
            msg = new Message((byte) 39);
            msg.writer().writeShort(npcTemplateId);
            msg.writer().writeUTF(chat);
            msg.writer().writeByte(menu.length);
            for (byte i = 0; i < menu.length; ++i) {
                msg.writer().writeUTF(menu[i]);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void openUIMenu(final Char _char, final String[] menu) {
        Message msg = null;
        try {
            msg = new Message((byte) 40);
            if (menu != null) {
                for (byte i = 0; i < menu.length; ++i) {
                    if (menu[i] != null) {
                        msg.writer().writeUTF(menu[i]);
                    }
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }
    protected static void ItemInfo(final Char _char, final Item item, final byte typeUI, final int indexUI) {
        Message msg = null;
        try {
            msg = new Message((byte) 42);
            msg.writer().writeByte(typeUI);
            msg.writer().writeByte(indexUI);
            msg.writer().writeLong(item.expires);
            val a = NinjaUtil.getFile("assets/diado2.png");
            if (item.isTypeUIMe()) {
                msg.writer().writeInt(item.saleCoinLock);
            } else if (item.isTypeUIShop() || item.isTypeUIShopLock() || item.isTypeUIStore() || item.isTypeUIBook() || item.isTypeUIFashion() || item.isTypeUIClanShop()) {
                msg.writer().writeInt(item.buyCoin);
                msg.writer().writeInt(item.buyCoinLock);
                msg.writer().writeInt(item.buyGold);
            }
            if (item.isTypeBody() || item.isTypeMounts() || item.isTypeNgocKham()) {
                msg.writer().writeByte(item.sys);
                for (byte i = 0; i < item.options.size(); ++i) {
                    if (!item.isTypeNgocKham() && ((item.options.get(i).isKhamVuKhi() && !item.isTypeWeapon()) || (item.options.get(i).isKhamTrangBi() && !item.isTypeClothe()) || (item.options.get(i).isTrangSuc() && !item.isTypeAdorn()))) {
                        i += 2;
                    } else if (item.isTypeNgocKham() || (item.options.get(i).optionTemplate.id != 104 && item.options.get(i).optionTemplate.id != 123 && (item.options.get(i).optionTemplate.id < 106 || item.options.get(i).optionTemplate.id > 108))) {
                        msg.writer().writeByte(item.options.get(i).optionTemplate.id);
                        msg.writer().writeInt(item.options.get(i).param);
                    }
                }
            } else if (item.template.id == 233) {
                NinjaUtil.writeByteArray(msg, a);
            } else if (item.template.id == 234) {
                NinjaUtil.writeByteArray(msg, a);
            } else if (item.template.id == 235) {
                NinjaUtil.writeByteArray(msg, a);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void TradeInvite(final Char _char, final int charId) {
        Message msg = null;
        try {
            msg = new Message((byte) 43);
            msg.writer().writeInt(charId);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void TradeLockItem(final Char _char, final int coin, final Item[] arrItem) {
        Message msg = null;
        try {
            byte num = 0;
            for (byte i = 0; i < arrItem.length; ++i) {
                if (arrItem[i] != null) {
                    ++num;
                }
            }
            msg = new Message((byte) 45);
            msg.writer().writeInt(coin);
            msg.writer().writeByte(num);
            for (byte i = 0; i < arrItem.length; ++i) {
                final Item item = arrItem[i];
                if (item != null) {
                    msg.writer().writeShort(item.template.id);
                    if (item.isTypeBody() || item.isTypeNgocKham()) {
                        msg.writer().writeByte(item.upgrade);
                    }
                    msg.writer().writeBoolean(item.isExpires);
                    msg.writer().writeShort(item.quantity);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void TradeAccept(final Char _char) {
        Message msg = null;
        try {
            msg = new Message((byte) 46);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void getTask(final Char _char) {
        Message msg = null;
        try {
            final TaskTemplate taskTemplate = GameScr.taskTemplates[_char.ctaskId];
            msg = new Message((byte) 47);
            msg.writer().writeShort(taskTemplate.taskId);
            msg.writer().writeByte(_char.ctaskIndex);
            msg.writer().writeUTF(taskTemplate.name);
            msg.writer().writeUTF(taskTemplate.detail);
            msg.writer().writeByte(taskTemplate.subNames.length);
            for (byte i = 0; i < taskTemplate.subNames.length; ++i) {
                msg.writer().writeUTF(taskTemplate.subNames[i]);
            }
            msg.writer().writeShort(_char.ctaskCount);
            for (short j = 0; j < taskTemplate.counts.length; ++j) {
                msg.writer().writeShort(taskTemplate.counts[j]);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void nextTask(final Char _char) {
        Message msg = null;
        try {
            msg = new Message((byte) 48);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void finishTask(final Char _char) {
        Message msg = null;
        try {
            msg = new Message((byte) 49);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void updateTask(final Char _char) {
        Message msg = null;
        try {
            msg = new Message((byte) 50);
            msg.writer().writeShort(_char.ctaskCount);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MobMiss(final Char _char, final Mob mob) {
        Message msg = null;
        try {
            msg = new Message((byte) 51);
            msg.writer().writeByte(mob.mobId);
            msg.writer().writeInt(mob.hp);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void AlertMessage(final Char _char, final String title, final String str) {
        Message msg = null;
        try {
            msg = new Message((byte) 53);
            msg.writer().writeUTF(title);
            msg.writer().writeUTF(str);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void AlertLuck(final Player _char, final Lucky lucky) {
        Message msg = null;
        try {
            msg = new Message((byte) 53);
            msg.writer().writeUTF("typemoi");
            msg.writer().writeUTF(lucky.title);
            msg.writer().writeShort(lucky.time);
            msg.writer().writeUTF(String.format(Text.get(0, 57), Util.getFormatNumber(lucky.totalMoney)));
            msg.writer().writeShort((short) lucky.percentWin(_char.playerId));
            msg.writer().writeUTF((Util.parseString("" + lucky.percentWin(_char.playerId), ".") == null) ? "0" : Util.parseString("" + lucky.percentWin(_char.playerId), "."));
            msg.writer().writeShort(lucky.numPlayer);
            if (lucky.winnerInfo != null) {
                msg.writer().writeUTF(lucky.winnerInfo);
            } else {
                msg.writer().writeUTF(Text.get(0, 56));
            }
            msg.writer().writeByte(lucky.typeLucky);
            msg.writer().writeUTF(Util.getFormatNumber(lucky.myMoney(_char.playerId)));
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void restPoint(final Char _char) {
        Message msg = null;
        try {
            msg = new Message((byte) 52);
            msg.writer().writeShort(_char.cx);
            msg.writer().writeShort(_char.cy);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void CancelTrade(final Char _char) {
        Message msg = null;
        try {
            msg = new Message((byte) 57);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void okTrade(final Char _char) {
        Message msg = null;
        try {
            msg = new Message((byte) 58);
            msg.writer().writeInt(_char.user.player.xu);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void FriendInvate(final Char _char, final String charName) {
        Message msg = null;
        try {
            msg = new Message((byte) 59);
            msg.writer().writeUTF(charName);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerAttack(final Char _char, final int charID, final byte skill, final Mob[] arrMob) {
        Message msg = null;
        try {
            msg = new Message((byte) 60);
            msg.writer().writeInt(charID);
            msg.writer().writeByte(skill);
            for (byte i = 0; i < arrMob.length; ++i) {
                if (arrMob[i] != null) {
                    msg.writer().writeByte(arrMob[i].mobId);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerAttack(final Char _char, final int charID, final byte skill, final Char[] arrChar) {
        Message msg = null;
        try {
            msg = new Message((byte) 61);
            msg.writer().writeInt(charID);
            msg.writer().writeByte(skill);
            for (byte i = 0; i < arrChar.length; ++i) {
                if (arrChar[i] != null) {
                    msg.writer().writeInt(arrChar[i].charID);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void HavePlayerAttack(final Char _char, final Char player, final int dame) {
        Message msg = null;
        try {
            msg = new Message((byte) 62);
            msg.writer().writeInt(player.charID);
            msg.writer().writeInt(player.cHP);
            msg.writer().writeInt(dame);
            msg.writer().writeInt(player.cMP);
            msg.writer().writeInt(0);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void openUIMenuNew(final Char _char, final String[] menu) {
        Message msg = null;
        try {
            msg = new Message((byte) 63);
            if (menu != null) {
                for (byte i = 0; i < menu.length; ++i) {
                    if (menu[i] != null) {
                        msg.writer().writeUTF(menu[i]);
                    }
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void FastMove(final Char _char, final int charID1, final int charID2, final short cx, final short cy) {
        Message msg = null;
        try {
            msg = new Message((byte) 64);
            msg.writer().writeInt(charID1);
            msg.writer().writeShort(cx);
            msg.writer().writeShort(cy);
            if (charID2 != 0) {
                msg.writer().writeInt(charID2);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void TestInvite(final Char _char, final int charId) {
        Message msg = null;
        try {
            msg = new Message((byte) 65);
            msg.writer().writeInt(charId);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void TestAccept(final Char _char, final int playerId, final int playerId2) {
        Message msg = null;
        try {
            msg = new Message((byte) 66);
            msg.writer().writeInt(playerId);
            msg.writer().writeInt(playerId2);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void TestEnd(final Char _char, final int playerId, final int playerId2, final int num) {
        Message msg = null;
        try {
            msg = new Message((byte) 67);
            msg.writer().writeInt(playerId);
            msg.writer().writeInt(playerId2);
            if (num > 0) {
                msg.writer().writeInt(num);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void AddCuuSat(final Char _char, final int charId) {
        Message msg = null;
        try {
            msg = new Message((byte) 68);
            msg.writer().writeInt(charId);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MeCuuSat(final Char _char, final int charId) {
        Message msg = null;
        try {
            msg = new Message((byte) 69);
            msg.writer().writeInt(charId);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ClearCuuSat(final Char _char, final int charId) {
        Message msg = null;
        try {
            msg = new Message((byte) 70);
            if (_char.charID != charId) {
                msg.writer().writeInt(charId);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void createBunhin(final Char _char, final BuNhin bunhin) {
        Message msg = null;
        try {
            msg = new Message((byte) 75);
            msg.writer().writeUTF(bunhin.name);
            msg.writer().writeShort(bunhin.x);
            msg.writer().writeShort(bunhin.y);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void attackBunhin(final Char _char, final short mobId, final short indexBunhin, final short idSkill_atk, final byte typeAtk, final byte typeTool) {
        Message msg = null;
        try {
            msg = new Message((byte) 76);
            msg.writer().writeByte(mobId);
            msg.writer().writeShort(indexBunhin);
            msg.writer().writeShort(idSkill_atk);
            msg.writer().writeByte(typeAtk);
            msg.writer().writeByte(typeTool);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ClearBunhin(final Char _char, final short indexBunhin) {
        Message msg = null;
        try {
            msg = new Message((byte) 77);
            msg.writer().writeShort(indexBunhin);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MobChange(final Char _char, final short mobId, final ItemMap itemMap) {
        Message msg = null;
        try {
            msg = new Message((byte) 78);
            msg.writer().writeByte(mobId);
            if (itemMap != null) {
                msg.writer().writeShort(itemMap.itemMapID);
                msg.writer().writeShort(itemMap.item.template.id);
                msg.writer().writeShort(itemMap.x);
                msg.writer().writeShort(itemMap.y);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void inviteParty(final Char _char, final String cName, final int num) {
        Message msg = null;
        try {
            msg = new Message((byte) 79);
            msg.writer().writeInt(num);
            msg.writer().writeUTF(cName);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void PlayerInParty(final Char _char, final Party party) {
        Message msg = null;
        try {
            msg = new Message((byte) 82);
            msg.writer().writeBoolean(party.isLock);
            for (byte i = 0; i < party.numPlayer; ++i) {
                final Char player = party.aChar.get(i);
                msg.writer().writeInt(player.charID);
                msg.writer().writeByte(player.nClass);
                msg.writer().writeUTF(player.cName);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void OutParty(final Char _char) {
        Message msg = null;
        try {
            msg = new Message((byte) 83);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void addFriend(final Char _char, final String friendName, final byte type) {
        Message msg = null;
        try {
            msg = new Message((byte) 84);
            msg.writer().writeUTF(friendName);
            msg.writer().writeByte(type);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void mobIsDisable(final Char _char, final short mobId, final boolean isDisable) {
        Message msg = null;
        try {
            msg = new Message((byte) 85);
            msg.writer().writeByte(mobId);
            msg.writer().writeBoolean(isDisable);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void mobIsMove(final Char _char, final short mobId, final boolean isMove) {
        Message msg = null;
        try {
            msg = new Message((byte) 86);
            msg.writer().writeByte(mobId);
            msg.writer().writeBoolean(isMove);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void upExpDown(final Char _char, final long xp) {
        Message msg = null;
        try {
            msg = new Message((byte) 71);
            msg.writer().writeLong(xp);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void MeDieDownEXP(final Char _char) {
        Message msg = null;
        try {
            msg = new Message((byte) 72);
            msg.writer().writeByte(_char.cPk);
            msg.writer().writeShort(_char.cx);
            msg.writer().writeShort(_char.cy);
            msg.writer().writeLong(_char.cExpDown);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ThuNuoiAttack(final Char _char, final int charId, final short mobId, final short idSkill_atk, final byte typeAtk, final byte typeTool, final byte type, final int playerMapId) {
        Message msg = null;
        try {
            msg = new Message((byte) 87);
            msg.writer().writeInt(charId);
            msg.writer().writeByte(mobId);
            msg.writer().writeShort(idSkill_atk);
            msg.writer().writeByte(typeAtk);
            msg.writer().writeByte(typeTool);
            if (type == 1) {
                msg.writer().writeByte(type);
                msg.writer().writeInt(playerMapId);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void mobIsFire(final Char _char, final short mobId, final boolean isFire) {
        Message msg = null;
        try {
            msg = new Message((byte) 89);
            msg.writer().writeByte(mobId);
            msg.writer().writeBoolean(isFire);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void mobIsIce(final Char _char, final short mobId, final boolean isIce) {
        Message msg = null;
        try {
            msg = new Message((byte) 90);
            msg.writer().writeByte(mobId);
            msg.writer().writeBoolean(isIce);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void mobIsWind(final Char _char, final short mobId, final boolean isWind) {
        Message msg = null;
        try {
            msg = new Message((byte) 91);
            msg.writer().writeByte(mobId);
            msg.writer().writeBoolean(isWind);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void openTextBoxUI(final Char _char, final String info, final short num) {
        Message msg = null;
        try {
            msg = new Message((byte) 92);
            msg.writer().writeUTF(info);
            msg.writer().writeShort(num);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void viewInfo(final Char _char, final Char player) {
        // thông tin nhân vật
        Message msg = null;
        try {
            msg = new Message((byte) 93);
            msg.writer().writeInt(player.charID);
            msg.writer().writeUTF(player.cName);
            if (CaiTrang.Head(player, player.partCaiTrang()) != -1) {
                msg.writer().writeShort(CaiTrang.Head(player, player.partCaiTrang()));
            } else if (player.ItemBody[11] != null) {
                msg.writer().writeShort(player.ItemBody[11].template.part);
            } else {
                msg.writer().writeShort(player.head);
            }
            msg.writer().writeByte(player.cgender);
            msg.writer().writeByte(player.nClass);
            msg.writer().writeByte(player.cPk);
            msg.writer().writeInt(player.cHP);
            msg.writer().writeInt(player.cMaxHP());
            msg.writer().writeInt(player.cMP);
            msg.writer().writeInt(player.cMaxMP());
            msg.writer().writeByte(player.cSpeed());
            msg.writer().writeShort(player.ResFire());
            msg.writer().writeShort(player.ResIce());
            msg.writer().writeShort(player.ResWind());
            msg.writer().writeInt(player.cDame((byte) 0, 0, 0, 0, 0, 0, 0, 0, 0, false, 0, 0));
            msg.writer().writeInt(player.cdameDown());
            msg.writer().writeShort(player.cExactly());
            msg.writer().writeShort(player.cMiss());
            msg.writer().writeShort(player.cFatal());
            msg.writer().writeShort(player.cReactDame());
            msg.writer().writeShort(player.sysUp());
            msg.writer().writeShort(player.sysDown());
            msg.writer().writeByte(player.cLevel);
            msg.writer().writeShort(0);
            msg.writer().writeUTF(player.cClanName);
            if (!player.cClanName.isEmpty()) {
                msg.writer().writeByte(player.ctypeClan);
            }
            msg.writer().writeShort(player.pointHoatDong); // điểm hoạt động
            msg.writer().writeShort(player.pointNon);
            msg.writer().writeShort(player.pointAo);
            msg.writer().writeShort(player.pointGangtay);
            msg.writer().writeShort(player.pointQuan);
            msg.writer().writeShort(player.pointGiay);
            msg.writer().writeShort(player.pointVukhi);
            msg.writer().writeShort(player.pointLien);
            msg.writer().writeShort(player.pointNhan);
            msg.writer().writeShort(player.pointNgocboi);
            msg.writer().writeShort(player.pointPhu);
            msg.writer().writeByte(player.countTaskHangNgay);
            msg.writer().writeByte(player.countTaskTaThu);
            msg.writer().writeByte(player.countPB);
            byte limitTiemnangso = (byte) Player.ItemUseLimit(player, (short) 253);
            if (limitTiemnangso < 0) {
                limitTiemnangso = 0;
            }
            byte limitKynangso = (byte) Player.ItemUseLimit(player, (short) 252);
            if (limitKynangso < 0) {
                limitKynangso = 0;
            }
            msg.writer().writeByte(limitTiemnangso);
            msg.writer().writeByte(limitKynangso);
            for (byte i = 0; i < 16; ++i) {
                if (player.ItemBody[i] != null) {
                    msg.writer().writeShort(player.ItemBody[i].template.id);
                    msg.writer().writeByte(player.ItemBody[i].upgrade);
                    msg.writer().writeByte(player.ItemBody[i].sys);
                } else {
                    if (_char.user.session.versionNja >= 180) {
                        msg.writer().writeShort(-1);
                    }
                }
            }
            for (byte i = 16; i < player.ItemBody.length; ++i) {
                if (player.ItemBody[i] != null) {
                    msg.writer().writeShort(player.ItemBody[i].template.id);
                    msg.writer().writeByte(player.ItemBody[i].upgrade);
                    msg.writer().writeByte(player.ItemBody[i].sys);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            _char.user.session.sendMessage(msg);
            byte limitPhongLoi = (byte) Player.ItemUseLimit(player, (short) 308);
            if (limitPhongLoi < 0) {
                limitPhongLoi = 0;
            }
            byte limitBangHoa = (byte) Player.ItemUseLimit(player, (short) 309);
            if (limitBangHoa < 0) {
                limitBangHoa = 0;
            }
            viewInfo(_char, 0, limitPhongLoi, limitBangHoa);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void requestItemPlayer(final Char _char, final Item item) {
        Message msg = null;
        try {
            msg = new Message((byte) 94);
            msg.writer().writeByte(item.indexUI);
            msg.writer().writeLong(item.expires);
            msg.writer().writeInt(item.saleCoinLock);
            msg.writer().writeByte(item.sys);
            for (short i = 0; i < item.options.size(); ++i) {
                final ItemOption option = item.options.get(i);
                if (!item.isTypeNgocKham() && (option.isKhamVuKhi() || option.isKhamTrangBi() || option.isTrangSuc())) {
                    i += 2;
                } else if (option.optionTemplate.id != 104 && option.optionTemplate.id != 123 && (option.optionTemplate.id < 109 || option.optionTemplate.id > 112)) {
                    msg.writer().writeByte(option.optionTemplate.id);
                    msg.writer().writeInt(option.param);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void upCoinBag(final Player _char, final int num) {
        Message msg = null;
        try {
            msg = new Message((byte) 95);
            msg.writer().writeInt(num);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void openUIConfirmID(final Char _char, final String info, final byte id) {
        Message msg = null;
        try {
            msg = new Message((byte) 107);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(info);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void ItemMonToBag(final Char _char, final byte indexitem, final byte indexUI) {
        Message msg = null;
        try {
            msg = new Message((byte) 108);
            _char.writeParam(msg);
            msg.writer().writeShort(_char.eff5BuffHp());
            msg.writer().writeShort(_char.eff5BuffMp());
            msg.writer().writeByte(indexitem);
            msg.writer().writeByte(indexUI);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }
    protected static void testDunInvite(final Char _char, final int charID) {
        Message msg = null;
        try {
            msg = new Message((byte) 99);
            msg.writer().writeInt(charID);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void testDunList(final Char _char, final ArrayList<TestDun> arrTestDun) {
        Message msg = null;
        try {
            msg = new Message((byte) 100);
            msg.writer().writeByte(arrTestDun.size());
            for (byte i = 0; i < arrTestDun.size(); ++i) {
                final TestDun test = arrTestDun.get(i);
                if (test != null && test.isFinght) {
                    msg.writer().writeByte(test.testdunID);
                    msg.writer().writeUTF(test.white);
                    msg.writer().writeUTF(test.black);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void viewInfo(final Char _char, final int pointTinhTu, final byte limitPhongLoi, final byte limitBangHoa) {
        Message msg = null;
        try {
            msg = new Message((byte) 101);
            msg.writer().writeInt(pointTinhTu);
            msg.writer().writeByte(limitPhongLoi);
            msg.writer().writeByte(limitBangHoa);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void sendItemToAuction(final Char _char, final byte indexMenu, final int coinSale) {
        Message msg = null;
        try {
            msg = new Message((byte) 102);
            msg.writer().writeByte(indexMenu);
            msg.writer().writeInt(coinSale);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void loadItemAuction(final Char _char, final byte indexMenu, final ArrayList<ItemStands> arrItemStands) {
        Message msg = null;
        try {
            msg = new Message((byte) 103);
            msg.writer().writeByte(indexMenu);
            msg.writer().writeInt(arrItemStands.size());
            for (int i = arrItemStands.size() - 1; i >= 0; --i) {
                final ItemStands itemStands = arrItemStands.get(i);
                msg.writer().writeInt(itemStands.itemId);
                msg.writer().writeInt((int) (itemStands.timeStart + itemStands.timeEnd - System.currentTimeMillis() / 1000L));
                msg.writer().writeShort(itemStands.item.quantity);
                msg.writer().writeUTF(itemStands.seller);
                msg.writer().writeInt(itemStands.price);
                msg.writer().writeShort(itemStands.item.template.id);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void viewItemAuction(final Char _char, final ItemStands itemStands) {
        Message msg = null;
        try {
            msg = new Message((byte) 104);
            msg.writer().writeInt(itemStands.itemId);
            msg.writer().writeInt(itemStands.item.saleCoinLock);
            if (itemStands.item.isTypeBody() || itemStands.item.isTypeNgocKham()) {
                msg.writer().writeByte(itemStands.item.upgrade);
                msg.writer().writeByte(itemStands.item.sys);
                for (int i = 0; i < itemStands.item.options.size(); ++i) {
                    msg.writer().writeByte(itemStands.item.options.get(i).optionTemplate.id);
                    msg.writer().writeInt(itemStands.item.options.get(i).param);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void DoiOption(final Char _char, final byte indexUI, final byte upgrade) {
        Message msg = null;
        try {
            msg = new Message((byte) 112);
            msg.writer().writeByte(indexUI);
            msg.writer().writeByte(upgrade);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void updateInfoPlayer(final Char _char, final Char player) {
        Message msg = null;
        try {
            msg = new Message((byte) 116);
            msg.writer().writeInt(player.charID);
            writeCharInfo(player, msg);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void mapItem(final Char _char, final TileMap tileMap) {
        Message msg = null;
        try {
            final MapTemplate template = tileMap.map.template;
            msg = new Message((byte) 117);
            msg.writer().writeByte(template.itemMap.length);
            for (byte i = 0; i < template.itemMap.length; ++i) {
                final short num = template.itemMap[i];
                msg.writer().writeShort(num);
                final byte[] data = NinjaUtil.getFile("assets/x" + _char.user.session.zoomLevel + "/icon/item/" + num + ".png");
                msg.writer().writeInt(data.length);
                msg.writer().write(data);
            }
            msg.writer().writeByte(template.ItemTreeBehind.length);
            for (short j = 0; j < template.ItemTreeBehind.length; ++j) {
                msg.writer().writeByte(template.ItemTreeBehind[j].idTree);
                msg.writer().writeByte(template.ItemTreeBehind[j].x);
                msg.writer().writeByte(template.ItemTreeBehind[j].y);
            }
            msg.writer().writeByte(template.ItemTreeBetwen.length);
            for (short j = 0; j < template.ItemTreeBetwen.length; ++j) {
                msg.writer().writeByte(template.ItemTreeBetwen[j].idTree);
                msg.writer().writeByte(template.ItemTreeBetwen[j].x);
                msg.writer().writeByte(template.ItemTreeBetwen[j].y);
            }
            msg.writer().writeByte(template.ItemTreeFront.length);
            for (short j = 0; j < template.ItemTreeFront.length; ++j) {
                msg.writer().writeByte(template.ItemTreeFront[j].idTree);
                msg.writer().writeByte(template.ItemTreeFront[j].x);
                msg.writer().writeByte(template.ItemTreeFront[j].y);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void addMob(final Char _char, final ArrayList<Mob> mobs) {
        Message msg = null;
        try {
            msg = new Message((byte) 122);
            msg.writer().writeByte(0);
            msg.writer().writeByte(mobs.size());
            for (short i = 0; i < mobs.size(); ++i) {
                final Mob mob = mobs.get(i);
                if (mob != null) {
                    msg.writer().writeByte(mob.mobId);
                    msg.writer().writeBoolean(mob.isDisable);
                    msg.writer().writeBoolean(mob.isDontMove);
                    msg.writer().writeBoolean(mob.isFire);
                    msg.writer().writeBoolean(mob.isIce);
                    msg.writer().writeBoolean(mob.isWind);
                    msg.writer().writeByte(mob.templateId);
                    msg.writer().writeByte(mob.sys);
                    msg.writer().writeInt(mob.hp);
                    msg.writer().writeByte(mob.level);
                    msg.writer().writeInt(mob.maxHp);
                    msg.writer().writeShort(mob.x);
                    msg.writer().writeShort(mob.y);
                    msg.writer().writeByte(mob.status);
                    msg.writer().writeByte(mob.levelBoss);
                    msg.writer().writeBoolean(mob.isBoss);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void Kiemduyet(final Session_ME session, final byte b) {
        Message msg = null;
        try {
            msg = new Message((byte) 123);
            msg.writer().writeByte(b);
            session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void KhamNgoc(final Char _char, final byte b, final byte upgrade) {
        Message msg = null;
        try {
            msg = new Message((byte) 124);
            msg.writer().writeByte(b);
            msg.writer().writeInt(_char.user.luong);
            msg.writer().writeInt(_char.user.player.xu);
            msg.writer().writeInt(_char.user.player.yen);
            if (b == 1) {
                msg.writer().writeByte(upgrade);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void GiaoDo(final Char _char, final byte b) {
        Message msg = null;
        try {
            msg = new Message((byte) 126);
            msg.writer().writeByte(b);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    protected static void writeCharInfo(final Char player, final Message msg) {
        try {
            msg.writer().writeUTF(player.cClanName);
            if (!player.cClanName.isEmpty()) {
                msg.writer().writeByte(player.ctypeClan);
            }
            msg.writer().writeBoolean(player.isDisable);
            msg.writer().writeByte(player.cTypePk);
            msg.writer().writeByte(player.nClass);
            msg.writer().writeByte(player.cgender);
            if (CaiTrang.Head(player, player.partCaiTrang()) != -1) {
                msg.writer().writeShort(CaiTrang.Head(player, player.partCaiTrang()));
            } else if (player.ItemBody[11] != null) {
                msg.writer().writeShort(player.ItemBody[11].template.part);
            } else {
                msg.writer().writeShort(player.head);
            }
            msg.writer().writeUTF(player.cName);
            msg.writer().writeInt(player.cHP);
            msg.writer().writeInt(player.cMaxHP());
            msg.writer().writeByte(player.cLevel);
            if (player.ItemBody[1] != null) {
                msg.writer().writeShort(player.ItemBody[1].template.part);
            } else {
                msg.writer().writeShort(-1);
            }
            if (player.ItemBody[2] != null) {
                msg.writer().writeShort(player.ItemBody[2].template.part);
            } else {
                msg.writer().writeShort(-1);
            }
            if (player.ItemBody[6] != null) {
                msg.writer().writeShort(player.ItemBody[6].template.part);
            } else {
                msg.writer().writeShort(-1);
            }
            msg.writer().writeByte(-1);
            msg.writer().writeShort(player.cx);
            msg.writer().writeShort(player.cy);
            msg.writer().writeShort(player.eff5BuffHp());
            msg.writer().writeShort(player.eff5BuffMp());
            msg.writer().writeByte(player.aEff.size());
            for (byte i = 0; i < player.aEff.size(); ++i) {
                final Effect effect = player.aEff.get(i);
                msg.writer().writeByte(effect.template.id);
                if (effect.timeStart != -1) {
                    msg.writer().writeInt((int) (System.currentTimeMillis() / 1000L - effect.timeStart));
                } else {
                    msg.writer().writeInt(0);
                }
                msg.writer().writeInt(effect.timeLenght);
                msg.writer().writeShort(effect.param);
            }
            msg.writer().writeBoolean(player.isHuman);
            msg.writer().writeBoolean(player.isNhanban);
            msg.writer().writeShort(CaiTrang.Head(player, player.partCaiTrang()));
            msg.writer().writeShort(CaiTrang.Weapon(player.partCaiTrang()));
            msg.writer().writeShort(CaiTrang.Body(player, player.partCaiTrang()));
            msg.writer().writeShort(CaiTrang.Leg(player, player.partCaiTrang()));
            for (byte i = 0; i < player.setThoiTrang.length; ++i) {
                msg.writer().writeShort(player.setThoiTrang[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getTaskOrder(Char c, byte taskId) {
        try {
            Message m = new Message(96);
            m.writer().writeByte(taskId);
            if (taskId == 0) {
                m.writer().writeInt(c.taskHangNgay[1]);
                m.writer().writeInt(c.taskHangNgay[2]);
                m.writer().writeUTF("Nhiệm vụ hàng ngày:");
                m.writer().writeUTF("Hãy hoàn thành nhiệm vụ, sau đó trở về gặp NPC Rikudou để nhận thưởng.");
                m.writer().writeByte(c.taskHangNgay[3]);
                m.writer().writeByte(c.taskHangNgay[4]);
            } else if (taskId == 1) {
                m.writer().writeInt(c.taskTaThu[1]);
                m.writer().writeInt(c.taskTaThu[2]);
                m.writer().writeUTF("Nhiệm vụ tà thú:");
                m.writer().writeUTF("Hãy hoàn thành nhiệm vụ, sau đó trở về gặp NPC Rikudou để nhận thưởng.");
                m.writer().writeByte(c.taskTaThu[3]);
                m.writer().writeByte(c.taskTaThu[4]);
            }

            m.writer().flush();
            c.user.session.sendMessage(m);
            m.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public static void clearTaskOrder(Char c, byte taskId) {
        try {
            Message m = new Message(98);
            m.writer().writeByte(taskId);
            m.writer().flush();
            c.user.session.sendMessage(m);
            m.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void updateTaskOrder(Char c, byte taskId) {
        try {
            Message m = new Message(97);
            m.writer().writeByte(taskId);
            if (taskId == 0) {
                m.writer().writeInt(c.taskHangNgay[1]);
            } else if (taskId == 1) {
                m.writer().writeInt(c.taskTaThu[1]);
            }

            m.writer().flush();
            c.user.session.sendMessage(m);
            m.cleanup();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static MobTemplate getMobIdByLevel(int level) {

        synchronized (MapServer.mapServer) {
            Map map;
            short i;
            int j;
            MobTemplate mobData = null;

            for (i = 0; i < MapServer.mapServer.length; ++i) {
                map = MapServer.mapServer[i];
                if (map != null && map.mapThuong()) {
                    for (j = 0; j < map.template.mobID.length; j++) {
                        if (level >= 100 && map.template.mobLevel[j] >= 95 + Util.nextInt(-3, 3) && map.template.mobStatus[j] == 5) {
                            mobData = Mob.arrMobTemplate[map.template.mobID[j]];
                        } else if (map.template.mobLevel[j] >= 10 && Math.abs(map.template.mobLevel[j] - level) <= Util.nextInt(-3, 3) && map.template.mobStatus[j] == 5) {
                            mobData = Mob.arrMobTemplate[map.template.mobID[j]];
                        }
                        if (mobData != null) {
                            return mobData;
                        }
                    }
                }
            }
            return null;
        }
    }

    public static MapTemplate getMobMapId(int id) {

        synchronized (MapServer.mapServer) {
            Map map;
            short i;
            int j;
            for (i = 0; i < MapServer.mapServer.length; ++i) {
                map = MapServer.mapServer[i];
                if (map != null) {
                    for (j = 0; j < map.template.mobID.length; j++) {
                        if (map.template.mobID[j] == id) {
                            return map.template;
                        }
                    }
                }
            }
            return null;
        }
    }

    public static MobTemplate getMobIdTaThu(int level) {
        synchronized (MapServer.mapServer) {
            MobTemplate mobData = null;
            int i;
            if (level >= 30 && level < 40) {
                for (i = 0; i < GameScr.arrModIdTaThu30.length; i++) {
                    if (Math.abs(GameScr.arrModIdTaThu30[i] - level) <= 2) {
                        mobData = Mob.arrMobTemplate[GameScr.arrModIdTaThu30[i]];
                    }
                }
            } else if (level >= 40 && level < 50) {
                for (i = 0; i < GameScr.arrModIdTaThu40.length; i++) {
                    if (Math.abs(GameScr.arrModIdTaThu40[i] - level) <= 2) {
                        mobData = Mob.arrMobTemplate[GameScr.arrModIdTaThu40[i]];
                    }
                }
            } else if (level >= 50 && level < 60) {
                for (i = 0; i < GameScr.arrModIdTaThu50.length; i++) {
                    if (Math.abs(GameScr.arrModIdTaThu50[i] - level) <= 2) {
                        mobData = Mob.arrMobTemplate[GameScr.arrModIdTaThu50[i]];
                    }
                }
            } else if (level >= 60 && level < 70) {
                for (i = 0; i < GameScr.arrModIdTaThu60.length; i++) {
                    if (Math.abs(GameScr.arrModIdTaThu60[i] - level) <= 2) {
                        mobData = Mob.arrMobTemplate[GameScr.arrModIdTaThu60[i]];
                    }
                }
            } else if (level >= 70 && level < 80) {
                for (i = 0; i < GameScr.arrModIdTaThu70_2.length; i++) {
                    if (Math.abs(GameScr.arrModIdTaThu70_2[i] - level) <= 3) {
                        mobData = Mob.arrMobTemplate[GameScr.arrModIdTaThu70[i]];
                    }
                }
            } else if (level >= 80 && level < 97) {
                for (i = 0; i < GameScr.arrModIdTaThu80_2.length; i++) {
                    if (Math.abs(GameScr.arrModIdTaThu80_2[i] - level) <= 8) {
                        mobData = Mob.arrMobTemplate[GameScr.arrModIdTaThu80[i]];
                    }
                }
            } else if (level >= 97) {
                mobData = Mob.arrMobTemplate[GameScr.arrModIdTaThu100[0]];
            }

            if (mobData != null) {
                return mobData;
            }
            return null;
        }
    }

    public static MapTemplate getMobMapIdTaThu(int id) {
        synchronized (MapServer.mapServer) {
            Map map;
            short i;
            int j;
            for (i = 0; i < MapServer.mapServer.length; ++i) {
                map = MapServer.mapServer[i];
                if (map != null) {
                    for (j = 0; j < map.template.mobID.length; j++) {
                        if (map.template.mobID[j] == id && map.template.moblevelBoss[j] == 3) {
                            return map.template;
                        }
                    }
                }
            }
            return null;
        }
    }

    public static void openMenuBST(final Char _char) {
        Message msg = null;
        try {
            _char.user.player.menuCaiTrang = 1;
            Service.openUI(_char, (byte) 4, "Bộ Sưu Tập", "Sử dụng");
            msg = new Message((byte) 31);
            msg.writer().writeInt(_char.user.player.xuBox);
            msg.writer().writeByte(_char.user.player.ItemBST.length);
            for (byte i = 0; i < _char.user.player.ItemBST.length; ++i) {
                final Item item = _char.user.player.ItemBST[i];
                if (item != null) {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeBoolean(item.isLock);
                    if (item.isTypeBody() || item.isTypeNgocKham()) {
                        msg.writer().writeByte(item.upgrade);
                    }
                    msg.writer().writeBoolean(item.isExpires);
                    msg.writer().writeShort(item.quantity);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void openMenuCaiTrang(final Char _char) {
        Message msg = null;
        try {
            _char.user.player.menuCaiTrang = 2;
            Service.openUI(_char, (byte) 4, "Cải Trang", "Sử dụng");
            msg = new Message((byte) 31);
            msg.writer().writeInt(_char.user.player.xuBox);
            msg.writer().writeByte(_char.user.player.ItemCaiTrang.length);
            for (byte i = 0; i < _char.user.player.ItemCaiTrang.length; ++i) {
                final Item item = _char.user.player.ItemCaiTrang[i];
                if (item != null) {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeBoolean(item.isLock);
                    if (item.isTypeBody() || item.isTypeNgocKham()) {
                        msg.writer().writeByte(item.upgrade);
                    }
                    msg.writer().writeBoolean(item.isExpires);
                    msg.writer().writeShort(item.quantity);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void onVithuInfo(Char _char, int charID, final Item[] arrItemViThu) {
        Message msg = null;
        try {
            msg = new Message(117);
            msg.writer().writeByte(-1);
            msg.writer().writeByte(0);
            msg.writer().writeInt(charID);
            for (byte i = 0; i < arrItemViThu.length; ++i) {
                final Item item = arrItemViThu[i];
                if (item != null) {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeByte(item.upgrade);
                    msg.writer().writeLong(item.expires);
                    msg.writer().writeByte(item.sys);
                    msg.writer().writeByte(item.options.size());
                    for (short j = 0; j < item.options.size(); ++j) {
                        final ItemOption option = item.options.get(j);
                        msg.writer().writeByte(option.optionTemplate.id);
                        msg.writer().writeInt(option.param);
                    }
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void onChangeVithu(Char _char, int charID , Mob mob) {
        Message msg = null;
        try {
            msg = new Message(117);
            msg.writer().writeByte(-1);
            msg.writer().writeByte(1);
            msg.writer().writeInt(charID);
            if (mob != null) {
                msg.writer().writeShort(mob.templateId);
                msg.writer().writeByte(Mob.arrMobTemplate[mob.templateId].isBoss ? 1 : 0);
            } else {
                msg.writer().writeShort(0);
                msg.writer().writeByte(0);
            }
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void createNewPlayer(final Session_ME session, String uname, String passw) {
        Message msg = null;
        try {
            msg = new Message(118);
            msg.writer().writeUTF(uname);
            msg.writer().writeUTF(passw);
            session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void sendEffectAuto(final Char _char, final byte id, final int x, final int y, final byte count, final short time) {
        Message msg = null;
        try {
            msg = new Message(122);
            msg.writer().writeByte(1);
            msg.writer().writeByte(id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeByte(count);
            msg.writer().writeShort(time);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }
    public static void typeActive(final Char _char) {
        Message msg = null;
        try {
            msg = new Message(-106);
            msg.writer().writeByte(1);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public static void clearForcus(final Char _char) {
        Message msg = null;
        try {
            msg = new Message(-16);
            _char.user.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }
}
