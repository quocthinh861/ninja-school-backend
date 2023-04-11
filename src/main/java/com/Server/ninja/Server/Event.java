 package com.Server.ninja.Server;

import com.Server.ninja.template.MobTemplate;
import java.util.ArrayList;
import com.Server.ninja.option.ItemOption;

public class Event
{
    protected static final int MAX_COUNT_INPUT = 1000;
    
    protected static Item itemEventMob(final Char _char, final Mob mob) {
        switch (GameScr.vEvent) {
            case 1: { // trung thu
                if (Math.abs(_char.cLevel - mob.level) <= 7 && Util.nextInt(10) < 2) {
                    return new Item(null, (short)Util.nextInt(292, 297), 1, -1, false, (byte)0, 0);
                }
                break;
            }
            case 2: { // halloween
                int percent = 25;
                int dlevel = 5;
                if (_char.getEffId((byte)40) != null || _char.getEffId((byte)41) != null) {
                    percent *= 2;
                    dlevel = 10;
                }
                if (mob.level >= 20 && _char.getEffId((byte)43) != null &&  Util.nextInt(100) < percent && Math.abs(_char.cLevel - mob.level) <= 10) {
                    return new Item(null, (short)Util.nextInt(806, 812), 1, -1, true, (byte)0, 0);
                }
                if (_char.cLevel >= 20 && Math.abs(_char.cLevel - mob.level) <= dlevel && Util.nextInt(100) < percent) {
                    if (_char.getEffId((byte)40) != null || _char.getEffId((byte)41) != null ) {
                        if (Util.nextInt(1,2) == 1) {
                            return new Item(null, (short)Util.nextInt(609, 610), 1, -1, false, (byte)0, 0);
                        } else {
                            return new Item(null, (short)Util.nextInt(607, 608), 1, -1, false, (byte)0, 0);
                        }
                    } else {
                        return new Item(null, (short)Util.nextInt(607, 610), 1, -1, true, (byte)0, 0);
                    }
                }
                break;
            }
            case 3: { // noel
                int percent2 = 10;
                int dlevel2 = 10;
                if (_char.getEffId((byte)40) != null || _char.getEffId((byte)41) != null) {
                    if (_char.getEffId((byte)40) != null) {
                        dlevel2 = 20;
                    }
                    percent2 *= 2;
                }
                if (Math.abs(_char.cLevel - mob.level) <= 5 && Util.nextInt(100) < 15) {
                    return new Item(null, (short)Util.nextInt(666, 668), 1, -1, false, (byte)0, 0);
                }
                if (Math.abs(_char.cLevel - mob.level) <= dlevel2 && Util.nextInt(100) < percent2) {
                    return new Item(null, (short)Util.nextInt(481, 482), 1, -1, false, (byte)0, 0);
                }
                if (Math.abs(_char.cLevel - mob.level) <= dlevel2 && Util.nextInt(100) < percent2) {
                    return new Item(null, (short)829, 1, -1, false, (byte)0, 0);
                }
                break;
            }
            case 4: { // tết
                int percent3 = 20;
                if (_char.getEffId((byte)40) != null || _char.getEffId((byte)41) != null) {
                    if (_char.getEffId((byte)40) != null) {
                        percent3 = 50;
                    }
                    else {
                        percent3 = 30;
                    }
                }
                if (Math.abs(_char.cLevel - mob.level) <= 10 && Util.nextInt(100) < percent3) {
                    short idtnd;
                    do {
                        idtnd = (short)Util.nextInt(638, 642);
                    } while (idtnd == 640);
                    return new Item(null, idtnd, 1, -1, false, (byte)0, 0);
                }
                break;
            }
        }
        return null;
    }
    
    protected static Item itemBuyStore(final Item item) {
        switch (GameScr.vEvent) {
            default: {
                return item;
            }
        }
    }
    
    protected static void doEventMenu(final Char _char, final byte menuId, final byte optionId, final short npcTemplateId) {
        final Player player = _char.user.player;
        switch (GameScr.vEvent) {
            case 1: {
                if (menuId == 0) {
                    if (player.yen < 10000) {
                        Service.openUISay(_char, npcTemplateId, String.format(Text.get(0, 171), 10000));
                        break;
                    }
                    if (player.ItemBagSlotNull() < 1) {
                        Service.openUISay(_char, npcTemplateId, String.format(Text.get(0, 167), 1));
                        break;
                    }
                    if (optionId == 0) {
                        if (player.ItemBagQuantity((short)292) < 20 || player.ItemBagQuantity((short)293) < 10 || player.ItemBagQuantity((short)295) < 10 || player.ItemBagQuantity((short)294) < 10 || player.ItemBagQuantity((short)297) < 10) {
                            Service.openUISay(_char, npcTemplateId, Text.get(0, 170));
                            break;
                        }
                        player.ItemBagAddQuantity(new Item(null, (short)298, 1, -1, false, (byte)0, 0));
                        player.ItemBagUses((short)292, 20);
                        player.ItemBagUses((short)293, 10);
                        player.ItemBagUses((short)295, 10);
                        player.ItemBagUses((short)294, 10);
                        player.ItemBagUses((short)297, 10);
                        player.upCoinLock(-10000L, (byte)1);
                        break;
                    }
                    else if (optionId == 1) {
                        if (player.ItemBagQuantity((short)292) < 20 || player.ItemBagQuantity((short)295) < 10 || player.ItemBagQuantity((short)294) < 10 || player.ItemBagQuantity((short)297) < 10) {
                            Service.openUISay(_char, npcTemplateId, Text.get(0, 170));
                            break;
                        }
                        player.ItemBagAddQuantity(new Item(null, (short)299, 1, -1, false, (byte)0, 0));
                        player.ItemBagUses((short)292, 20);
                        player.ItemBagUses((short)295, 10);
                        player.ItemBagUses((short)294, 10);
                        player.ItemBagUses((short)297, 10);
                        player.upCoinLock(-10000L, (byte)1);
                        break;
                    }
                    else if (optionId == 2) {
                        if (player.ItemBagQuantity((short)292) < 20 || player.ItemBagQuantity((short)293) < 10 || player.ItemBagQuantity((short)294) < 10 || player.ItemBagQuantity((short)296) < 10) {
                            Service.openUISay(_char, npcTemplateId, Text.get(0, 170));
                            break;
                        }
                        player.ItemBagAddQuantity(new Item(null, (short)300, 1, -1, false, (byte)0, 0));
                        player.ItemBagUses((short)292, 20);
                        player.ItemBagUses((short)293, 10);
                        player.ItemBagUses((short)294, 10);
                        player.ItemBagUses((short)296, 10);
                        player.upCoinLock(-10000L, (byte)1);
                        break;
                    }
                    else {
                        if (optionId != 3) {
                            break;
                        }
                        if (player.ItemBagQuantity((short)292) < 20 || player.ItemBagQuantity((short)293) < 10 || player.ItemBagQuantity((short)294) < 10 || player.ItemBagQuantity((short)296) < 10) {
                            Service.openUISay(_char, npcTemplateId, Text.get(0, 170));
                            break;
                        }
                        player.ItemBagAddQuantity(new Item(null, (short)301, 1, -1, false, (byte)0, 0));
                        player.ItemBagUses((short)292, 20);
                        player.ItemBagUses((short)293, 10);
                        player.ItemBagUses((short)294, 10);
                        player.ItemBagUses((short)296, 10);
                        player.upCoinLock(-10000L, (byte)1);
                        break;
                    }
                }
                else {
                    if (menuId != 1) {
                        break;
                    }
                    if (optionId == 0) {
                        if (player.ItemBagQuantity((short)298) < 1 || player.ItemBagQuantity((short)299) < 1 || player.ItemBagQuantity((short)300) < 1 || player.ItemBagQuantity((short)301) < 1 || player.ItemBagQuantity((short)304) < 1) {
                            Service.openUISay(_char, npcTemplateId, Text.get(0, 170));
                            break;
                        }
                        player.ItemBagAddQuantity(new Item(null, (short)302, 1, -1, true, (byte)0, 0));
                        player.ItemBagUses((short)298, 1);
                        player.ItemBagUses((short)299, 1);
                        player.ItemBagUses((short)300, 1);
                        player.ItemBagUses((short)301, 1);
                        player.ItemBagUses((short)304, 1);
                        break;
                    }
                    else {
                        if (optionId != 1) {
                            break;
                        }
                        if (player.ItemBagQuantity((short)298) < 1 || player.ItemBagQuantity((short)299) < 1 || player.ItemBagQuantity((short)300) < 1 || player.ItemBagQuantity((short)301) < 1 || player.ItemBagQuantity((short)305) < 1) {
                            Service.openUISay(_char, npcTemplateId, Text.get(0, 170));
                            break;
                        }
                        player.ItemBagAddQuantity(new Item(null, (short)303, 1, -1, false, (byte)0, 0));
                        player.ItemBagUses((short)298, 1);
                        player.ItemBagUses((short)299, 1);
                        player.ItemBagUses((short)300, 1);
                        player.ItemBagUses((short)301, 1);
                        player.ItemBagUses((short)305, 1);
                        break;
                    }
                }
                //break;
            }
            case 2: {
                // Halloween
                // [["Làm hộp ma quỷ"],["Làm kẹo táo"]["Đổi chìa khoá"],["Đổi ma vật"],["Hướng dẫn"],["Xem top"]]
                if (menuId == 0) {
                    Service.openTextBoxUI(_char, Text.get(0, 279), (short)505);
                    break;
                }
                if (menuId == 1) {
                    Service.openTextBoxUI(_char, Text.get(0, 280), (short)506);
                    break;
                }
                if (menuId == 2) {
                    Service.openTextBoxUI(_char, Text.get(0, 281), (short)507);
                    break;
                }
                if (menuId == 3) {
                    Service.openTextBoxUI(_char, Text.get(0, 282), (short)508);
                    break;
                }
                if (menuId == 4) {
                    Service.AlertMessage(_char, Text.get(0, 130), Text.get(0, 287));
                    break;
                }
                if (menuId == 5) {
                    Service.AlertMessage(_char, Text.get(0, 185), Top.getStringBXH(_char, 4));
                }
                break;
            }
            case 3: {
                if (menuId == 0) {
                    Service.openTextBoxUI(_char, Text.get(0, 322), (short)509);
                    break;
                }
                if (menuId == 1) {
                    Service.openTextBoxUI(_char, Text.get(0, 323), (short)510);
                    break;
                }
                if (menuId == 2) {
                    Service.openTextBoxUI(_char, Text.get(0, 380), (short)511);
                    break;
                }
                if (menuId == 3) {
                    Service.AlertMessage(_char, Text.get(0, 130), Text.get(0, 324));
                    break;
                }
                if (menuId == 4) {
                    Service.AlertMessage(_char, Text.get(0, 185), Top.getStringBXH(_char, 4));
                    break;
                }
                break;
            }
            case 4: {
                if (menuId == 0) {
                    Service.openTextBoxUI(_char, Text.get(0, 394), (short)512);
                    break;
                }
                if (menuId == 1) {
                    Service.openTextBoxUI(_char, Text.get(0, 395), (short)513);
                    break;
                }
                if (menuId == 2) {
                    Service.AlertMessage(_char, Text.get(0, 130), Text.get(0, 396));
                    break;
                }
                if (menuId == 3) {
                    Service.AlertMessage(_char, Text.get(0, 185), Top.getStringBXH(_char, 4));
                    break;
                }
                break;
            }
            default: {
                Service.openUISay(_char, npcTemplateId, Text.get(0, 169));
                break;
            }
        }
    }
    
    protected static void Lighting(final Char _char, final Npc npc) {
        if (_char.user.player.ItemBagIndexNull() == -1) {
            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
        }
        else {
            Item it = null;
            if (Util.nextInt(100) < 15) {
                it = new Item(null, (short)Util.nextInt(3, 5), 1, -1, false, (byte)0, 0);
            }
            else if (Util.nextInt(100) < 10) {
                it = new Item(ItemOption.arrOptionDefault((short)541, (byte)0), (short)541, 1, 604800, false, (byte)0, 5);
            }
            else if (Util.nextInt(100) < 15) {
                _char.user.player.upCoinLock(Util.nextInt(1000, 10000), (byte)2);
            }
            else if (Util.nextInt(100) < 25) {
                _char.updateExp(500000L);
            }
            else if (Util.nextInt(100) < 30) {
                it = new Item(null, (short)Util.nextInt(292, 297), Util.nextInt(10, 30), -1, false, (byte)0, 0);
            }
            else if (Util.nextInt(100) < 25) {
                it = new Item(null, (short)Util.nextInt(275, 278), 1, -1, false, (byte)0, 0);
            }
            else if (Util.nextInt(100) < 10) {
                it = new Item(null, (short)Util.nextInt(449, 453), 1, -1, false, (byte)0, 0);
            }
            else if (Util.nextInt(100) < 10) {
                it = new Item(null, (short)Util.nextInt(470, 472), 1, -1, false, (byte)0, 0);
            }
            else if (Util.nextInt(1000) < 5) {
                it = new Item(null, (short)454, 1, -1, false, (byte)0, 0);
            }
            else if (Util.nextInt(1000) < 3) {
                it = new Item(ItemOption.arrOptionDefault((short)443, (byte)0), (short)443, 1, -1, false, (byte)0, 5);
            }
            else if (Util.nextInt(1000) < 3) {
                it = new Item(ItemOption.arrOptionDefault((short)485, (byte)0), (short)485, 1, -1, false, (byte)0, 5);
            }
            else if (Util.nextInt(1000) < 3) {
                final short idtemplate = (short)Util.nextInt(652, 655);
                it = new Item(ItemOption.arrOptionDefault(idtemplate, (byte)0), idtemplate, 1, -1, false, (byte)0, 5);
                it.upgrade = 1;
            }
            else {
                _char.updateExp(500000L);
            }
            if (it != null) {
                if (it.template.isUpToUp) {
                    _char.user.player.ItemBagAddQuantity(it);
                }
                else {
                    _char.user.player.ItemBagAdd(it);
                }
            }
            _char.user.player.ItemBagUses((short)310, 1);
            npc.Lighttime = 1000;
            _char.tileMap.NpcPlayerUpdate((byte)npc.index, (byte)15);
            Top.sortTop(4, _char.cName, null, ++_char.user.player.epoint, null);
        }
    }
    
    protected static void finghtBoss(final Char _char, final Mob mob) {
        switch (GameScr.vEvent) {
            case 3: {
                if (mob.templateId == 230) {
                    _char.updateExp(15000000L);
                    final byte indexUI = _char.user.player.ItemBagIndexNull();
                    if (indexUI != -1) {
                        Item it = null;
                        if (Util.nextInt(100) < 20) {
                            it = new Item(null, (short)Util.nextInt(275, 278), 1, -1, false, (byte)0, 0);
                        }
                        else if (Util.nextInt(100) < 10) {
                            it = new Item(ItemOption.arrOptionDefault((short)485, (byte)0), (short)485, 1, -1, false, (byte)0, 5);
                        }
                        else if (Util.nextInt(100) < 10) {
                            it = new Item(ItemOption.arrOptionDefault((short)524, (byte)0), (short)524, 1, -1, false, (byte)0, 5);
                        }
                        else if (Util.nextInt(100) < 5) {
                            it = new Item(ItemOption.arrOptionDefault((short)744, (byte)0), (short)744, 1, 604800, false, (byte)0, 5);
                        }
                        else if (Util.nextInt(100) < 3) {
                            it = new Item(null, (short)454, 1, -1, false, (byte)0, 0);
                        }
                        else if (Util.nextInt(100) < 3) {
                            it = new Item(ItemOption.arrOptionDefault((short)541, (byte)0), (short)541, 1, 604800, false, (byte)0, 5);
                        }
                        else if (Util.nextInt(100) < 1) {
                            final short idtemplate = (short)Util.nextInt(652, 655);
                            it = new Item(ItemOption.arrOptionDefault(idtemplate, (byte)0), idtemplate, 1, -1, false, (byte)0, 5);
                            it.upgrade = 1;
                        } if (it != null) {
                            if (it.template.isUpToUp) {
                                _char.user.player.ItemBagAddQuantity(it);
                            }
                            else {
                                _char.user.player.ItemBagAdd(indexUI, it);
                            }
                        }
                    }
                    Top.sortTop(4, _char.cName, null, ++_char.user.player.epoint, null);
                    break;
                }
                break;
            }
        }
    }
    
    protected static void UseItem(final Char _char, final Item item) {
        if ((GameScr.vEvent != 1 && item.template.id >= 298 && item.template.id <= 303) || (GameScr.vEvent != 2 && (item.template.id == 606 || (item.template.id >= 611 && item.template.id <= 612))) || (GameScr.vEvent != 3 && (item.template.id == 743 || (item.template.id >= 671 && item.template.id <= 672))) || (GameScr.vEvent != 4 && ((item.template.id >= 643 && item.template.id <= 644) || item.template.id == 582))) {
            Service.ServerMessage(_char, Text.get(0, 166));
        }
        else {
            if (item.template.id >= 298 && item.template.id <= 301) {
//                if (!_char.isHuman) {
//                    Service.ServerMessage(_char, Text.get(0, 310));
//                }
//                else {
                    _char.updateExp(3000000L);
                    Player.nextItemUseLimit(_char, item.template.id);
                    _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                //}
            }
            else if (item.template.id == 302) {
                if (_char.user.player.ItemBagIndexNull() == -1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
                else {
                    _char.updateExp(15000000L);
                    Item it = null;
                    if (Util.nextInt(100) <= 8) {
                        it = new Item(null, (short)Util.nextInt(275, 278), 1, -1, true, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(null, (short)Util.nextInt(3, 5), 1, -1, true, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(436, 437), 1, -1, true, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 2) {
                        it = new Item(ItemOption.arrOptionDefault((short)568, (byte)0), (short)568, 1, 604800, true, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 2) {
                        it = new Item(null, (short)340, 1, -1, true, (byte)0, 0);
                    }
                    else if (Util.nextInt(1000) < 2) {
                        it = new Item(null, (short)Util.nextInt(308, 309), 1, -1, true, (byte)0, 0);
                    }
                    else if (Util.nextInt(1000) < 1) {
                        it = new Item(null, (short)383, 1, -1, true, (byte)0, 0);
                    }
                    if (it != null) {
                        if (it.template.isUpToUp) {
                            _char.user.player.ItemBagAddQuantity(it);
                        }
                        else {
                            _char.user.player.ItemBagAdd(it);
                        }
                        if (it.template.id == 383 || it.template.id == 308 || it.template.id == 309) {
                            Client.alertServer(String.format(Text.get(0, 172), _char.cName, item.template.name, it.template.name));
                        }
                    }
                    _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                }
            }
            else if (item.template.id == 303) {
                if (_char.user.player.ItemBagIndexNull() == -1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
                else {
                    _char.updateExp(25000000L);
                    Item it = null;
                    if (Util.nextInt(100) <= 10) {
                        it = new Item(null, (short)Util.nextInt(275, 278), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 35) {
                        it = new Item(null, (short)Util.nextInt(3, 5), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(409, 410), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(ItemOption.arrOptionDefault((short)795, (byte)0), (short)Util.nextInt(795, 796), 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(ItemOption.arrOptionDefault((short)797, (byte)0), (short)797, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(ItemOption.arrOptionDefault((short)798, (byte)0), (short)798, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 2) {
                        it = new Item(ItemOption.arrOptionDefault((short)800, (byte)0), (short)Util.nextInt(799, 800), 1, 259200, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(6, 7), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 10) {
                        it = new Item(null, (short)Util.nextInt(436, 437), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = ItemServer.getItemStore((short)Util.nextInt(618, 637), (byte)Util.nextInt(1, 3), (byte)1);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(8, 9), 1, 86400 * Util.nextInt(1, 5), false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(ItemOption.arrOptionDefault((short)337, (byte)0), (short)Util.nextInt(337, 338), 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 5) {
                        final short idtemplate = (short)Util.nextInt(652, 655);
                        it = new Item(ItemOption.arrOptionDefault(idtemplate, (byte)0), idtemplate, 1, -1, false, (byte)0, 5);
                        it.upgrade = 1;
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(ItemOption.arrOptionDefault((short)568, (byte)0), (short)568, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(1000) < 10) {
                        it = new Item(ItemOption.arrOptionDefault((short)443, (byte)0), (short)443, 1, -1, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(1000) < 8) {
                        it = new Item(ItemOption.arrOptionDefault((short)524, (byte)0), (short)524, 1, -1, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(1000) < 5) {
                        it = new Item(null, (short)Util.nextInt(308, 309), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(1000) < 3) {
                        it = new Item(null, (short)383, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(1000) < 1) {
                        it = new Item(null, (short)384, 1, -1, false, (byte)0, 0);
                    }
                    if (it != null) {
                        if (it.template.isUpToUp) {
                            _char.user.player.ItemBagAddQuantity(it);
                        }
                        else {
                            _char.user.player.ItemBagAdd(it);
                        }
                        if (it.template.id == 8 || it.template.id == 9 || it.template.id == 383 || it.template.id == 384 || it.template.id == 308 || it.template.id == 309 || it.template.id == 443 || it.template.id == 524) {
                            Client.alertServer(String.format(Text.get(0, 172), _char.cName, item.template.name, it.template.name));
                        }
                    }
                    _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                }
            }
            if (item.template.id == 606) {
                if (_char.getEffId((byte)43) == null) {
                    Char.setEffect(_char, Effect.itemEffectId(item.template.id), (int)(System.currentTimeMillis() / 1000L), Effect.itemEffectTimeLength(item.template.id), Effect.itemEffectParam(item.template.id), null, (byte)2);
                    _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                }
            }
            else if (item.template.id == 611) {
                if (_char.user.player.ItemBagIndexNull() == -1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
//                else if (!_char.isHuman) {
//                    Service.ServerMessage(_char, Text.get(0, 310));
//                }
                else {
                    _char.updateExp(15000000L);
                    Item it = null;
                    if (Util.nextInt(100) <= 15) {
                        _char.user.player.upCoinLock(Util.nextInt(10000, 20000), (byte)2);
                    }
                    else if (Util.nextInt(100) < 3) {
                        final short templateId = (short)Util.nextInt(801, 803);
                        it = new Item(ItemOption.arrOptionDefault(templateId, (byte)0), templateId, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 40) {
                        it = new Item(null, (short)Util.nextInt(436, 437), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        short arr[] = new short[] {739,740,760,761,767,768,695};
                        it = new Item(null, arr[Util.nextInt(arr.length)], 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 3) {
                        it = new Item(ItemOption.arrOptionDefault((short)337, (byte)0), (short)Util.nextInt(337, 338), 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 2) {
                        it = new Item(null, (short)455, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 2) {
                        it = new Item(null, (short)456, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 2) {
                        it = new Item(null, (short)(short)Util.nextInt(788, 789), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(10000) < 3) {
                        it = new Item(null, (short)383, 1, -1, false, (byte)0, 0);
                    }
                    if (it != null) {
                        if (it.template.isUpToUp) {
                            _char.user.player.ItemBagAddQuantity(it);
                        }
                        else {
                            _char.user.player.ItemBagAdd(it);
                        }
                        if (it.template.id == 8 || it.template.id == 9 || it.template.id == 383 || it.template.id == 384 || it.template.id == 385 || it.template.id == 308 || it.template.id == 309 || it.template.id == 443 || it.template.id == 523 || it.template.id == 524) {
                            Client.alertServer(String.format(Text.get(0, 172), _char.cName, item.template.name, it.template.name));
                        }
                    }
                    _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                    Top.sortTop(4, _char.cName, null, ++_char.user.player.epoint, null);
                }
            }
            else if (item.template.id == 612) {
                if (_char.user.player.ItemBagIndexNull() == -1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
                else if (_char.user.player.ItemBagQuantity((short)818) <= 0) {
                    Service.ServerMessage(_char, Text.get(0, 286));
                }
//                else if (!_char.isHuman) {
//                    Service.ServerMessage(_char, Text.get(0, 310));
//                }
                else {
                    _char.updateExp(25000000L);
                    Item it = null;
                    if (Util.nextInt(100) <= 30) {
                        _char.user.player.upCoinLock(Util.nextInt(10000, 20000), (byte)2);
                    }
                    else if (Util.nextInt(100) <= 10) {
                        _char.updateExp(10000000L);
                    }
                    else if (Util.nextInt(100) < 3) {
                        it = new Item(null, (short)257, 1, -1, false, (byte)0, 0);
                    }
//                    else if (Util.nextInt(100) <= 20 && _char.clan != null) {
//                        Char.upPointClan(_char, Util.nextInt(30, 50));
//                    }
                    else if (Util.nextInt(100) < 35) {
                        it = new Item(null, (short)695, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(null, (short)449, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 10) {
                        it = new Item(null, (short)578, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(409, 410), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        short arr[] = new short[] {733,734,760,761,695};
                        it = new Item(null, arr[Util.nextInt(arr.length)], 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(ItemOption.arrOptionDefault((short)804, (byte)0), (short)(804 + _char.cgender), 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 5) {
                        final short id4 = (short)832;
                        it = new Item(ItemOption.arrOptionDefault(id4, (byte)0), id4, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 2) {
                        final short templateId = (short)Util.nextInt(801, 803);
                        it = new Item(ItemOption.arrOptionDefault(templateId, (byte)0), templateId, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(6, 7), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(436, 437), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(9, 11), 1, 86400 * Util.nextInt(1, 5), false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(813, 817), 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(200) < 3) {
                        it = new Item(null, (short)696, 1, -1, false, (byte)0, 0);
                    }
//                    else if (Util.nextInt(100) < 5) {
//                        final short idtemplate = (short)Util.nextInt(652, 655);
//                        it = new Item(ItemOption.arrOptionDefault(idtemplate, (byte)0), idtemplate, 1, -1, false, (byte)0, 5);
//                        it.upgrade = 1;
//                    }
                    else if (Util.nextInt(1000) < 10) {
                        it = new Item(ItemOption.arrOptionDefault((short)443, (byte)0), (short)443, 1, -1, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(1000) < 10) {
                        it = new Item(ItemOption.arrOptionDefault((short)523, (byte)0), (short)523, 1, 2592000, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(1000) < 3) {
                        it = new Item(null, (short)697, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(1000) < 1) {
                        it = new Item(null, (short)968, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(1000) < 3) {
                        it = new Item(null, (short)383, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(1000) < 1) {
                        it = new Item(null, (short)384, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(10000) == 21) {
                        it = new Item(null, (short)385, 1, -1, false, (byte)0, 0);
                    }
                    if (it != null) {
                        if (it.template.isUpToUp) {
                            _char.user.player.ItemBagAddQuantity(it);
                        }
                        else {
                            _char.user.player.ItemBagAdd(it);
                        }
                        if (it.template.id == 8 || it.template.id == 9 || it.template.id == 383 || it.template.id == 384 || it.template.id == 385 || it.template.id == 308 || it.template.id == 309 || it.template.id == 443 || it.template.id == 523 || it.template.id == 524 || it.template.id == 968) {
                            Client.alertServer(String.format(Text.get(0, 172), _char.cName, item.template.name, it.template.name));
                        }
                    }
                    _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                    _char.user.player.ItemBagUses((short)818, 1);
                    Top.sortTop(4, _char.cName, null, ++_char.user.player.epoint, null);
                }
            }
            if (item.template.id == 671) {
                if (_char.user.player.ItemBagIndexNull() == -1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
//                else if (!_char.isHuman) {
//                    Service.ServerMessage(_char, Text.get(0, 310));
//                }
                else {
                    _char.updateExp(25000000L);
                    Item it = null;
                    if (Util.nextInt(100) <= 30) {
                        _char.user.player.upCoinLock(Util.nextInt(10000, 20000), (byte)2);
                    }
                    else if (Util.nextInt(100) <= 10) {
                        _char.updateExp(5000000L);
                    }
                    else if (Util.nextInt(100) <= 10) {
                        _char.updateExp(10000000L);
                    }
                    else if (Util.nextInt(100) <= 10) {
                        _char.updateExp(20000000L);
                    }
                    else if (Util.nextInt(100) < 30) {
                        it = new Item(null, (short)257, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) <= 20 && _char.clan != null) {
                        Char.upPointClan(_char, Util.nextInt(30, 50));
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(null, (short)449, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 10) {
                        it = new Item(null, (short)578, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(409, 410), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(ItemOption.arrOptionDefault((short)804, (byte)0), (short)(804 + _char.cgender), 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 3) {
                        final short templateId = (short)Util.nextInt(801, 803);
                        it = new Item(ItemOption.arrOptionDefault(templateId, (byte)0), templateId, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(ItemOption.arrOptionDefault((short)827, (byte)0), (short)827, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(6, 7), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(436, 437), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(ItemOption.arrOptionDefault((short)797, (byte)0), (short)797, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(9, 11), 1, 86400 * Util.nextInt(1, 5), false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        final short idtemplate = (short)Util.nextInt(652, 655);
                        it = new Item(ItemOption.arrOptionDefault(idtemplate, (byte)0), idtemplate, 1, -1, false, (byte)0, 5);
                        it.upgrade = 1;
                    }
                    else if (Util.nextInt(100) < 2) {
                        it = new Item(null, (short)695, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(1000) < 10) {
                        it = new Item(ItemOption.arrOptionDefault((short)443, (byte)0), (short)443, 1, -1, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(1000) < 10) {
                        it = new Item(ItemOption.arrOptionDefault((short)523, (byte)0), (short)523, 1, 2592000, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(1000) < 3) {
                        it = new Item(null, (short)383, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(1000) < 1) {
                        it = new Item(null, (short)384, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(10000) < 1) {
                        it = new Item(null, (short)385, 1, -1, false, (byte)0, 0);
                    }
                    if (it != null) {
                        if (it.template.isUpToUp) {
                            _char.user.player.ItemBagAddQuantity(it);
                        }
                        else {
                            _char.user.player.ItemBagAdd(it);
                        }
                        if (it.template.id == 8 || it.template.id == 9 || it.template.id == 383 || it.template.id == 384 || it.template.id == 385 || it.template.id == 308 || it.template.id == 309 || it.template.id == 443 || it.template.id == 523 || it.template.id == 524) {
                            Client.alertServer(String.format(Text.get(0, 172), _char.cName, item.template.name, it.template.name));
                        }
                    }
                    _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                }
            }
            else if (item.template.id == 672) {
                if (_char.user.player.ItemBagIndexNull() == -1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
//                else if (!_char.isHuman) {
//                    Service.ServerMessage(_char, Text.get(0, 310));
//                }
                else {
                    _char.updateExp(15000000L);
                    Item it = null;
                    if (Util.nextInt(100) <= 30) {
                        _char.user.player.upCoinLock(Util.nextInt(10000, 20000), (byte)2);
                    }
                    else if (Util.nextInt(100) < 3) {
                        final short templateId = (short)Util.nextInt(801, 803);
                        it = new Item(ItemOption.arrOptionDefault(templateId, (byte)0), templateId, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(null, (short)Util.nextInt(436, 437), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = new Item(ItemOption.arrOptionDefault((short)337, (byte)0), (short)Util.nextInt(337, 338), 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 1) {
                        it = new Item(null, (short)455, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 1) {
                        it = new Item(null, (short)456, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(10000) < 5) {
                        it = new Item(null, (short)383, 1, -1, false, (byte)0, 0);
                    }
                    if (it != null) {
                        if (it.template.isUpToUp) {
                            _char.user.player.ItemBagAddQuantity(it);
                        }
                        else {
                            _char.user.player.ItemBagAdd(it);
                        }
                        if (it.template.id == 8 || it.template.id == 9 || it.template.id == 383 || it.template.id == 384 || it.template.id == 385 || it.template.id == 308 || it.template.id == 309 || it.template.id == 443 || it.template.id == 523 || it.template.id == 524) {
                            Client.alertServer(String.format(Text.get(0, 172), _char.cName, item.template.name, it.template.name));
                        }
                    }
                    _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                }
            }
            else if (item.template.id == 743) {
                if (_char.tileMap.map.isMapLangCo()) {
                    Service.ServerMessage(_char, Text.get(0, 346));
                }
                else {
                    try {
                        _char.tileMap.lock.lock("Dung tuan loc");
                        try {
                            final short mobId = _char.tileMap.getIdMobAdd();
                            if (mobId != -1) {
                                boolean isThaythe = false;
                                for (short i = 0; i < _char.tileMap.aMob.size(); ++i) {
                                    final Mob mob = _char.tileMap.aMob.get(i);
                                    if (mob != null && mob.templateId == 230 && mob.playerId == _char.user.player.playerId) {
                                        isThaythe = true;
                                        GameCanvas.EndWait(_char, Text.get(0, 321));
                                        break;
                                    }
                                }
                                if (!isThaythe) {
                                    final MobTemplate mobTemplate = Mob.arrMobTemplate[230];
                                    final Mob mob = new Mob(_char.tileMap, mobId, mobTemplate.mobTemplateId, (byte)Util.nextInt(1, 3), mobTemplate.hp, 40, _char.cx, _char.cy, (byte)5, (byte)0, true, -1);
                                    mob.playerId = _char.user.player.playerId;
                                    mob.removeWhenDie_1 = true;
                                    _char.tileMap.aMob.add(mob);
                                    final ArrayList mobs = new ArrayList();
                                    mobs.add(mob);
                                    Service.addMob(_char, mobs);
                                    _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                                }
                            }
                            else {
                                GameCanvas.EndWait(_char, Text.get(0, 320));
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
            }
            if (item.template.id == 643) {
                if (_char.user.player.ItemBagIndexNull() == -1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
                else {
                    _char.updateExp(25000000L);
                    Item it = null;
                    if (Util.nextInt(100) < 25) {
                        it = new Item(null, (short)Util.nextInt(4, 11), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(null, (short)Util.nextInt(409, 410), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(null, (short)437, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(null, (short)Util.nextInt(451, 452), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(null, (short)Util.nextInt(275, 278), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 15) {
                        final short arr[] = new short[] {443,485,524};
                        final short ids = (short) arr[Util.nextInt(arr.length)];
                        it = new Item(ItemOption.arrOptionDefault(ids, (byte)0), ids, 1, -1, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 15) {
                        final short id4 = (short)Util.nextInt(568, 569);
                        it = new Item(ItemOption.arrOptionDefault(id4, (byte)0), id4, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 15) {
                        final short id4 = (short)Util.nextInt(830, 832);
                        it = new Item(ItemOption.arrOptionDefault(id4, (byte)0), id4, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(ItemOption.arrOptionDefault((short)541, (byte)0), (short)541, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(null, (short)695, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(null, (short)Util.nextInt(284, 285), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 10) {
                        it = new Item(null, (short)696, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 8) {
                        final short idtemplate = (short)Util.nextInt(652, 655);
                        it = new Item(ItemOption.arrOptionDefault(idtemplate, (byte)0), idtemplate, 1, -1, false, (byte)0, 5);
                        it.upgrade = 1;
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = ItemServer.getItemStore((short)Util.nextInt(618, 637), (byte)Util.nextInt(1, 3), (byte)1);
                        it.options.clear();
                    }
                    else if (Util.nextInt(100) < 5) {
                        it = ItemServer.getItemStore((short)Util.nextInt(632, 637), (byte)Util.nextInt(1, 3), (byte)1);
                    }
                    else if (Util.nextInt(100) < 2) {
                        it = ItemServer.getItemStore((short)Util.nextInt(632, 637), (byte)Util.nextInt(1, 3), (byte)0);
                    }
                    else if (Util.nextInt(1000) < 3) {
                        it = new Item(null, (short)383, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(1000) < 1) {
                        it = new Item(null, (short)384, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(10000) < 5) {
                        it = new Item(null, (short)385, 1, -1, false, (byte)0, 0);
                    }
                    if (it != null) {
                        if (it.template.isUpToUp) {
                            _char.user.player.ItemBagAddQuantity(it);
                        }
                        else {
                            _char.user.player.ItemBagAdd(it);
                        }
                        if (it.template.id == 8 || it.template.id == 9 || it.template.id == 383 || it.template.id == 384 || it.template.id == 385 || it.template.id == 308 || it.template.id == 309 || it.template.id == 443 || it.template.id == 523 || it.template.id == 524) {
                            Client.alertServer(String.format(Text.get(0, 172), _char.cName, item.template.name, it.template.name));
                        }
                    }
                    _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                }
            }
            else if (item.template.id == 644) {
                if (_char.user.player.ItemBagIndexNull() == -1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
                else {
                    _char.updateExp(17000000L);
                    Item it = null;
                    if (Util.nextInt(100) < 40) {
                        it = new Item(null, (short)Util.nextInt(4, 6), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 20) {
                        it = new Item(null, (short)Util.nextInt(409, 410), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(null, (short)437, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(null, (short)Util.nextInt(451, 452), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(null, (short)Util.nextInt(275, 278), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 15) {
                        final short id4 = (short)Util.nextInt(568, 569);
                        it = new Item(ItemOption.arrOptionDefault(id4, (byte)0), id4, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 15) {
                        it = new Item(null, (short)695, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(1000) < 3) {
                        it = new Item(null, (short)383, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(1000) < 1) {
                        it = new Item(null, (short)384, 1, -1, false, (byte)0, 0);
                    }
                    if (it != null) {
                        if (it.template.isUpToUp) {
                            _char.user.player.ItemBagAddQuantity(it);
                        }
                        else {
                            _char.user.player.ItemBagAdd(it);
                        }
                        if (it.template.id == 8 || it.template.id == 9 || it.template.id == 383 || it.template.id == 384 || it.template.id == 385 || it.template.id == 308 || it.template.id == 309 || it.template.id == 443 || it.template.id == 523 || it.template.id == 524) {
                            Client.alertServer(String.format(Text.get(0, 172), _char.cName, item.template.name, it.template.name));
                        }
                    }
                    _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                }
            }
            else if (item.template.id == 582) {
                if (_char.user.player.ItemBagIndexNull() == -1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
                else {
                    _char.updateExp(15000000L);
                    Item it = null;
                    if (Util.nextInt(100) < 25) {
                        it = new Item(null, (short)Util.nextInt(5, 6), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 20) {
                        it = new Item(null, (short)Util.nextInt(275, 278), 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 10) {
                        it = new Item(null, (short)449, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(100) < 10) {
                        it = new Item(ItemOption.arrOptionDefault((short)419, (byte)0), (short)419, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 10) {
                        it = new Item(ItemOption.arrOptionDefault((short)569, (byte)0), (short)569, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 10) {
                        it = new Item(ItemOption.arrOptionDefault((short)568, (byte)0), (short)568, 1, 604800, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(100) < 6) {
                        it = new Item(null, (short)454, 1, -1, false, (byte)0, 0);
                    }
                    else if (Util.nextInt(1000) < 5) {
                        it = new Item(ItemOption.arrOptionDefault((short)485, (byte)0), (short)485, 1, -1, false, (byte)0, 5);
                    }
                    else if (Util.nextInt(1000) < 1) {
                        it = new Item(ItemOption.arrOptionDefault((short)524, (byte)0), (short)524, 1, -1, false, (byte)0, 5);
                    }
                    if (it != null) {
                        if (it.template.isUpToUp) {
                            _char.user.player.ItemBagAddQuantity(it);
                        }
                        else {
                            _char.user.player.ItemBagAdd(it);
                        }
                        if (it.template.id == 8 || it.template.id == 9 || it.template.id == 383 || it.template.id == 384 || it.template.id == 385 || it.template.id == 308 || it.template.id == 309 || it.template.id == 443 || it.template.id == 523 || it.template.id == 524) {
                            Client.alertServer(String.format(Text.get(0, 172), _char.cName, item.template.name, it.template.name));
                        }
                    }
                    Top.sortTop(4, _char.cName, null, ++_char.user.player.epoint, null);
                    _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                }
            }
        }
    }
    
    protected static void cayThong(final Char _char, final byte menuId, final byte optionId, final short npcId) {
        if (_char.cLevel < 30) {
            Service.openUISay(_char, npcId, String.format(Text.get(0, 381), 30));
        }
        else if (menuId == 0) {
            if (_char.user.player.ItemBagQuantity((short)828) < 1) {
                Service.openUISay(_char, npcId, String.format(Text.get(0, 382), GameScr.itemTemplates[828].name));
            }
            else if (_char.user.player.ItemBagIndexNull() == -1) {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
            }
            else {
                _char.user.player.ItemBagUses((short)828, 1);
                _char.updateExp(5000000L);
                Item it = null;
                if (Util.nextInt(100) <= 30) {
                    _char.user.player.upCoinLock(Util.nextInt(10000, 20000), (byte)2);
                }
                else if (Util.nextInt(100) <= 10) {
                    _char.updateExp(500000L);
                } else if (Util.nextInt(100) <= 20 && _char.clan != null) {
                    Char.upPointClan(_char, Util.nextInt(30, 50));
                } else if (Util.nextInt(100) < 2) {
                    it = new Item(null, (short)695, 1, -1, false, (byte)0, 0);
                }
                else if (Util.nextInt(100) < 500) {
                    it = new Item(ItemOption.arrOptionDefault((short)801, (byte)0), (short)968, 1, 604800, false, (byte)0, 5);
                }
                else if (Util.nextInt(100) < 5) {
                    it = new Item(ItemOption.arrOptionDefault((short)804, (byte)0), (short)(804 + _char.cgender), 1, 604800, false, (byte)0, 5);
                } else if (Util.nextInt(100) < 5) {
                    it = new Item(null, (short)Util.nextInt(6, 7), 1, -1, false, (byte)0, 0);
                }
                else if (Util.nextInt(100) < 5) {
                    it = new Item(null, (short)Util.nextInt(436, 437), 1, -1, false, (byte)0, 0);
                }
                else if (Util.nextInt(100) < 5) {
                    it = new Item(ItemOption.arrOptionDefault((short)797, (byte)0), (short)797, 1, 604800, false, (byte)0, 5);
                }
                else if (Util.nextInt(100) < 5) {
                    it = new Item(null, (short)Util.nextInt(9, 11), 1, 86400 * Util.nextInt(1, 5), false, (byte)0, 0);
                }
                else if (Util.nextInt(100) < 5) {
                    it = new Item(null, (short)Util.nextInt(813, 817), 1, 604800, false, (byte)0, 5);
                }
                else if (Util.nextInt(100) < 3) {
                    final short idtemplate = (short)Util.nextInt(652, 655);
                    it = new Item(ItemOption.arrOptionDefault(idtemplate, (byte)0), idtemplate, 1, -1, false, (byte)0, 5);
                    it.upgrade = 1;
                } else if (Util.nextInt(1000) < 10) {
                    it = new Item(ItemOption.arrOptionDefault((short)443, (byte)0), (short)443, 1, -1, false, (byte)0, 5);
                }
                else if (Util.nextInt(1000) < 10) {
                    it = new Item(ItemOption.arrOptionDefault((short)523, (byte)0), (short)523, 1, 2592000, false, (byte)0, 5);
                }
                else if (Util.nextInt(1000) < 3) {
                    it = new Item(null, (short)383, 1, -1, false, (byte)0, 0);
                }
                else if (Util.nextInt(1000) < 1) {
                    it = new Item(null, (short)384, 1, -1, false, (byte)0, 0);
                }
                else if (Util.nextInt(10000) < 1) {
                    it = new Item(null, (short)385, 1, -1, false, (byte)0, 0);
                }
                if (it != null) {
                    if (it.template.isUpToUp) {
                        _char.user.player.ItemBagAddQuantity(it);
                    }
                    else {
                        _char.user.player.ItemBagAdd(it);
                    }
                    if (it.template.id == 8 || it.template.id == 9 || it.template.id == 383 || it.template.id == 384 || it.template.id == 385 || it.template.id == 308 || it.template.id == 309 || it.template.id == 443 || it.template.id == 523 || it.template.id == 524) {
                        Client.alertServer(String.format(Text.get(0, 172), _char.cName, GameScr.itemTemplates[828].name, it.template.name));
                    }
                }
            }
        }
        else if (menuId == 1) {
            if (_char.user.player.ItemBagQuantity((short)673) < 1) {
                Service.openUISay(_char, npcId, String.format(Text.get(0, 382), GameScr.itemTemplates[673].name));
            }
            else if (_char.user.player.ItemBagIndexNull() == -1) {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
            }
            else {
                _char.user.player.ItemBagUses((short)673, 1);
                Top.sortTop(4, _char.cName, null, ++_char.user.player.epoint, null);
                _char.updateExp(1000000L);
                Item it = null;
                if (Util.nextInt(100) <= 30) {
                    _char.user.player.upCoinLock(Util.nextInt(10000, 20000), (byte)2);
                }
                else if (Util.nextInt(100) < 3) {
                    final short templateId = (short)Util.nextInt(801, 803);
                    it = new Item(ItemOption.arrOptionDefault(templateId, (byte)0), templateId, 1, 604800, false, (byte)0, 5);
                }
                else if (Util.nextInt(100) < 5) {
                    it = new Item(null, (short)Util.nextInt(436, 437), 1, -1, false, (byte)0, 0);
                }
                else if (Util.nextInt(100) < 5) {
                    it = new Item(ItemOption.arrOptionDefault((short)337, (byte)0), (short)Util.nextInt(337, 338), 1, 604800, false, (byte)0, 5);
                }
                else if (Util.nextInt(100) < 1) {
                    it = new Item(null, (short)455, 1, -1, false, (byte)0, 0);
                }
                else if (Util.nextInt(100) < 1) {
                    it = new Item(null, (short)456, 1, -1, false, (byte)0, 0);
                }
                else if (Util.nextInt(10000) < 5) {
                    it = new Item(null, (short)383, 1, -1, false, (byte)0, 0);
                }
                if (it != null) {
                    if (it.template.isUpToUp) {
                        _char.user.player.ItemBagAddQuantity(it);
                    }
                    else {
                        _char.user.player.ItemBagAdd(it);
                    }
                    if (it.template.id == 8 || it.template.id == 9 || it.template.id == 383 || it.template.id == 384 || it.template.id == 385 || it.template.id == 308 || it.template.id == 309 || it.template.id == 443 || it.template.id == 523 || it.template.id == 524) {
                        Client.alertServer(String.format(Text.get(0, 172), _char.cName, GameScr.itemTemplates[673].name, it.template.name));
                    }
                }
            }
        }
    }
    
    protected static void hoaDao(final Char _char, final byte menuId, final byte optionId, final short npcId) {
        if (_char.cLevel < 30) {
            Service.openUISay(_char, npcId, String.format(Text.get(0, 381), 30));
        }
        else if (menuId == 0) {
            if (_char.user.player.ItemBagQuantity((short)646) < 1) {
                Service.openUISay(_char, npcId, String.format(Text.get(0, 382), GameScr.itemTemplates[646].name));
            }
            else if (_char.user.player.ItemBagIndexNull() == -1) {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
            }
            else {
                _char.user.player.ItemBagUses((short)646, 1);
                _char.updateExp(15000000L);
                Item it = null;
                if (Util.nextInt(100) < 15) {
                    it = new Item(null, (short)Util.nextInt(409, 410), 1, -1, false, (byte)0, 0);
                }
                else if (Util.nextInt(100) < 15) {
                    it = new Item(null, (short)437, 1, -1, false, (byte)0, 0);
                }
                else if (Util.nextInt(100) < 15) {
                    it = new Item(null, (short)Util.nextInt(451, 452), 1, -1, false, (byte)0, 0);
                }
                else if (Util.nextInt(100) < 15) {
                    it = new Item(null, (short)Util.nextInt(275, 278), 1, -1, false, (byte)0, 0);
                }
                else if (Util.nextInt(100) < 15) {
                    final short id4 = (short)Util.nextInt(568, 569);
                    it = new Item(ItemOption.arrOptionDefault(id4, (byte)0), id4, 1, 604800, false, (byte)0, 5);
                }
                else if (Util.nextInt(100) < 1) {
                    it = new Item(ItemOption.arrOptionDefault((short)541, (byte)0), (short)541, 1, 604800, false, (byte)0, 5);
                }
                if (it != null) {
                    if (it.template.isUpToUp) {
                        _char.user.player.ItemBagAddQuantity(it);
                    }
                    else {
                        _char.user.player.ItemBagAdd(it);
                    }
                    if (it.template.id == 8 || it.template.id == 9 || it.template.id == 383 || it.template.id == 384 || it.template.id == 385 || it.template.id == 308 || it.template.id == 309 || it.template.id == 443 || it.template.id == 523 || it.template.id == 524) {
                        Client.alertServer(String.format(Text.get(0, 172), _char.cName, GameScr.itemTemplates[828].name, it.template.name));
                    }
                }
            }
        }
    }
}
