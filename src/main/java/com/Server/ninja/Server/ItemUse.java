 package com.Server.ninja.Server;

import com.Server.ninja.option.ItemOption;

public class ItemUse
{
    private static final int[] openThucuoiOptionId;
    private static final int[] openThucuoiOptionParam;
    private static final byte[] arrBagCount;
    private static final short[] arrBagTemplateId;
    private static final int[] optionsIdViThu;
    protected static void ItemUse(final Char _char, final byte indexUI) {
        final Item item = _char.user.player.ItemBag(indexUI);
        if (item != null) {
            if (item.template.level > _char.cLevel) {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 33));
            }
            else if (item.template.gender != 2 && item.template.gender != _char.cgender) {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 34));
            }
            else if (Limit.getMaxItemLimit(item.template.id) != -1 && Player.ItemUseLimit(_char, item.template.id) >= Limit.getMaxItemLimit(item.template.id)) {
                Service.ServerMessage(_char, String.format(Text.get(0, 168), Limit.getMaxItemLimit(item.template.id), item.template.name));
            }
            else if ((item.isItemFire() && _char.sys() != 1) || (item.isItemIce() && _char.sys() != 2) || (item.isItemWind() && _char.sys() != 3)) {
                Service.ServerMessage(_char, Text.get(0, 319));
            }
            else if (item.isTypeBody() && !item.isViThu()) {
                byte type = item.template.type;
                if (item.isEquip2()) {
                    type += 16;
                }
                if ((type != 1 && type != 15) || item.isEquip2() || (item.isItemClass0() && _char.nClass == 0) || (item.isItemClass1() && _char.nClass == 1) || (item.isItemClass2() && _char.nClass == 2) || (item.isItemClass3() && _char.nClass == 3) || (item.isItemClass4() && _char.nClass == 4) || (item.isItemClass5() && _char.nClass == 5) || (item.isItemClass6() && _char.nClass == 6)) {
                    if (!item.isLock) {
                        item.optionAdd(ItemOption.openOptionAdd(item.template.id));
                        item.isLock = true;
                    }
                    final Item item2 = _char.ItemBody[type];
                    _char.ItemBody[type] = item;
                    _char.ItemBody[type].isLock = true;
                    _char.ItemBody[type].indexUI = type;
                    _char.ItemBody[type].typeUI = 5;
                    if (item2 != null) {
                        _char.user.player.ItemBag[indexUI] = item2;
                        _char.user.player.ItemBag[indexUI].indexUI = indexUI;
                        _char.user.player.ItemBag[indexUI].typeUI = 3;
                        ThoiTrang.removeThoiTrang(_char, item2.template.id);
                        if (item2.isItemBodyEffect()) {
                            Char.removeEffect(_char, Effect.itemEffectId(item2.template.id));
                        }
                    }
                    else {
                        _char.user.player.ItemBag[indexUI] = null;
                    }
                    ThoiTrang.setThoiTrang(_char, item.template.id);
                    if (type == 1) {
                        if (_char.ctaskId == 2 && _char.ctaskIndex == 0) {
                            _char.uptaskMaint();
                        }
                    }
                    else if (type == 10) {
                        final short mobTemplateId = Mob.itemMob(item);
                        Mob mob = null;
                        if (mobTemplateId > 0) {
                            final Mob mobMe = new Mob(_char.tileMap, (short)(-1), mobTemplateId, (byte)1, 0, 0, _char.cx, (short)(_char.cy - 40), (byte)5, (byte)0, Mob.arrMobTemplate[mobTemplateId].isBoss, -1);
                            _char.mobMe = mobMe;
                            mob = mobMe;
                        }
                        Service.MELoadThuNuoi(_char, mob);
                    }
                    Service.ItemUse(_char, indexUI);
                    Service.updateInfoMe(_char);
                    if (item.isItemBodyEffect()) {
                        short param = 0;
                        if (item.options != null && item.options.size() > 0) {
                            param = (short)item.options.get(0).param;
                        }
                        Char.setEffect(_char, Effect.itemEffectId(item.template.id), (int)(System.currentTimeMillis() / 1000L), (int)(item.expires - System.currentTimeMillis()), param, null, (byte)1);
                    }
                    Player.updateInfoPlayer(_char);
                    if (_char.ItemBody[10] != null) {
                        Player.updateThuNuoiPlayer(_char);
                    }
                    if (type == 12) {
                        Player.updateGiaTocPlayer(_char);
                    }
                    if (item.template.type == 13) {
                        Player.updateCoatPlayer(_char);
                    }
                }
                else if (item.template.type == 1) {
                    Service.ServerMessage(_char, Text.get(0, 32));
                }
                else {
                    Service.ServerMessage(_char, Text.get(0, 387));
                }
            }
            else if (item.isTypeMounts() && item.saleCoinLock > 0 && !item.isTBViThu()) {
                final byte index = (byte)(item.template.type - 29);
                final Item itemM = _char.ItemMounts[index];
                if (index == 4) {
                    if (_char.ItemMounts[0] != null || _char.ItemMounts[1] != null || _char.ItemMounts[2] != null || _char.ItemMounts[3] != null) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 21));
                    }
                    else {
                        if (!item.isLock) {
                            for (byte i = 0; i < 4; ++i) {
                                int op = -1;
                                do {
                                    op = Util.nextInt(ItemUse.openThucuoiOptionId.length);
                                    for (short j = 0; j < item.options.size(); ++j) {
                                        final ItemOption option = item.options.get(j);
                                        if (ItemUse.openThucuoiOptionId[op] == option.optionTemplate.id) {
                                            op = -1;
                                            break;
                                        }
                                    }
                                } while (op == -1);
                                final int idOp = ItemUse.openThucuoiOptionId[op];
                                int par = ItemUse.openThucuoiOptionParam[op];
                                if (item.template.id == 523 || item.template.id == 798 || (item.template.id >= 801 && item.template.id <= 803)) {
                                    par *= 10;
                                }
                                else if (item.template.id == 827) {
                                    par *= 12;
                                }
                                else if (item.template.id == 831) {
                                    par *= 11;
                                }
                                else if (item.template.id == 968) {
                                    par *= 13;
                                }
                                item.options.add(new ItemOption(idOp, par));
                            }
                        }
                        if (!item.isLock) {
                            item.optionAdd(ItemOption.openOptionAdd(item.template.id));
                            item.isLock = true;
                        }
                        _char.ItemMounts[index] = item;
                        item.isLock = true;
                        item.indexUI = index;
                        item.typeUI = 41;
                        if (itemM != null) {
                            _char.user.player.ItemBag[indexUI] = itemM;
                            itemM.indexUI = indexUI;
                            itemM.typeUI = 3;
                            ThoiTrang.removeThoiTrang(_char, itemM.template.id);
                        }
                        else {
                            _char.user.player.ItemBag[indexUI] = null;
                        }
                        ThoiTrang.setThoiTrang(_char, item.template.id);
                        Service.ItemUse(_char, indexUI);
                        Player.LoadThuCuoi(_char);
                    }
                }
                else if (_char.ItemMounts[4] == null) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 22));
                }
                else if (!_char.ItemMounts[4].isItemAnimals1() && item.isItemAnimals1()) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 128));
                }
                else if (!_char.ItemMounts[4].isItemMoto1() && item.isItemMoto1()) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 129));
                }
                else {
                    _char.ItemMounts[index] = item;
                    item.isLock = true;
                    item.indexUI = index;
                    item.typeUI = 41;
                    if (itemM != null) {
                        _char.user.player.ItemBag[indexUI] = itemM;
                        itemM.indexUI = indexUI;
                        itemM.typeUI = 3;
                    }
                    else {
                        _char.user.player.ItemBag[indexUI] = null;
                    }
                    Service.ItemUse(_char, indexUI);
                    Player.LoadThuCuoi(_char);
                }
                if (itemM != null && itemM.isExpires) {
                    _char.isThuCuoiHetHan = false;
                }
            } else if (item.isViThu()) {
                final Item itemViThu = _char.arrItemViThu[4];
                if (_char.arrItemViThu[0] != null || _char.arrItemViThu[1] != null || _char.arrItemViThu[2] != null || _char.arrItemViThu[3] != null) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 409));
                }
                else {
                    item.indexUI = 4;
                    _char.arrItemViThu[4] = item;
                    item.isLock = true;
                    item.typeUI = 51;
                    if (itemViThu != null) {
                        _char.user.player.ItemBag[indexUI] = itemViThu;
                        itemViThu.indexUI = indexUI;
                        itemViThu.typeUI = 3;
                    }
                    else {
                        _char.user.player.ItemBag[indexUI] = null;
                    }
                    final short mobTemplateId = Mob.itemMob(item);
                    if (mobTemplateId > 0) {
                        final Mob mobViThu = new Mob(_char.tileMap, (short)(-1), mobTemplateId, (byte)1, 0, 0, _char.cx, (short)(_char.cy - 40), (byte)5, (byte)0, Mob.arrMobTemplate[mobTemplateId].isBoss, -1);
                        _char.mobViThu = mobViThu;
                        Player.updateViThu(_char);
                    }
                    Service.ItemUse(_char, indexUI);
                    Player.LoadViThu(_char);
                }
            } else if (item.isTBViThu()) {
                final byte index = (byte)(item.template.type - 29);
                final Item itemViThu = _char.arrItemViThu[index];
                if (_char.arrItemViThu[4] == null) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 410));
                } else {
                    _char.arrItemViThu[index] = item;
                    item.isLock = true;
                    item.indexUI = index;
                    item.typeUI = 51;
                    if (itemViThu != null) {
                        _char.user.player.ItemBag[indexUI] = itemViThu;
                        itemViThu.indexUI = indexUI;
                        itemViThu.typeUI = 3;
                    }
                    else {
                        _char.user.player.ItemBag[indexUI] = null;
                    }
                    Player.updateViThu(_char);
                    Player.LoadViThu(_char);
                    Service.ItemUse(_char, indexUI);
                }
            } else if (item.isTypeDish()) {
                final Effect effect = _char.getEffType((byte)0);
                if (_char.cPk >= 15) {
                    Service.ServerMessage(_char, Text.get(0, 49));
                }
                else if (effect != null && effect.param > Effect.itemEffectParam(item.template.id)) {
                    Service.ServerMessage(_char, Text.get(0, 30));
                }
                else {
                    Char.setEffect(_char, Effect.itemEffectId(item.template.id), -1, Effect.itemEffectTimeLength(item.template.id), Effect.itemEffectParam(item.template.id), null, (byte)1);
                    if (_char.ctaskId == 3 && _char.ctaskIndex == 1) {
                        _char.uptaskMaint();
                        Service.openUISay(_char, (short)4, Talk.getTask(0, 33));
                    }
                    _char.user.player.ItemBagClear(indexUI);
                }
            }
            else if (item.isItemExpQ()) {
                final Effect effect = _char.getEffType((byte)18);
                int timeLength = Effect.itemEffectTimeLength(item.template.id);
                if (effect != null && effect.template.id == 22 && item.template.id == 248) {
                    timeLength += effect.timeLenght;
                }
                Char.setEffect(_char, Effect.itemEffectId(item.template.id), -1, timeLength, Effect.itemEffectParam(item.template.id), null, (byte)1);
                _char.user.player.ItemBagClear(indexUI);
            }
            else if (item.isItemBuffHP()) {
                final Effect effect = _char.getEffType((byte)17);
                if (_char.cPk >= 15) {
                    Service.ServerMessage(_char, Text.get(0, 49));
                }
                else if (_char.cHP >= _char.cMaxHP()) {
                    Service.MELoadHP(_char);
                    Service.ServerMessage(_char, Text.get(0, 54));
                }
                else {
                    final int timeLength = Effect.itemEffectTimeLength(item.template.id);
                    if (effect == null) {
                        Char.setEffect(_char, Effect.itemEffectId(item.template.id), -1, timeLength, Effect.itemEffectParam(item.template.id), null, (byte)1);
                        _char.user.player.ItemBagUse(indexUI, 1);
                    }
                }
            }
            else if (item.isItemBuffMP()) {
                if (_char.cPk >= 15) {
                    Service.ServerMessage(_char, Text.get(0, 49));
                }
                else if (_char.cMP >= _char.cMaxMP()) {
                    Service.MELoadMP(_char);
                    Service.ServerMessage(_char, Text.get(0, 55));
                }
                else {
                    _char.upMP(ItemServer.itemParam(item.template.id));
                    Service.MELoadMP(_char);
                    _char.user.player.ItemBagUse(indexUI, 1);
                }
            }
            else if (item.isItemCanvasBag()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                else if (_char.user.player.bagOpen.contains(item.template.id)) {
                    Service.ServerMessage(_char, Text.get(0, 82));
                }
                else {
                    final short TemplateId = ItemUse.arrBagTemplateId[_char.user.player.bagOpen.size()];
                    if (TemplateId != item.template.id) {
                        Service.ServerMessage(_char, String.format(Text.get(0, 83), GameScr.itemTemplates[TemplateId].name, item.template.name));
                    }
                    else {
                        _char.user.player.ItemBag[item.indexUI] = null;
                        final Player player2 = _char.user.player;
                        player2.bagCount += ItemUse.arrBagCount[_char.user.player.bagOpen.size()];
                        _char.user.player.bagOpen.add(item.template.id);
                        final Item[] ItemBag = new Item[_char.user.player.bagCount];
                        for (byte i = 0; i < _char.user.player.ItemBag.length; ++i) {
                            ItemBag[i] = _char.user.player.ItemBag[i];
                        }
                        _char.user.player.ItemBag = ItemBag;
                        Service.updateBagCount(_char, _char.user.player.bagCount, indexUI);
                    }
                }
            } else if (item.template.id == 856) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                else if (_char.user.player.bagCount >= 120) {
                    Service.ServerMessage(_char, "Chỉ có thể mở tối đa 120 ô.");
                } else if (_char.user.player.bagCount < 54) {
                    Service.ServerMessage(_char, "Phải sử dụng túi vải cấp 3 trước.");
                } else {
                    _char.user.player.ItemBag[item.indexUI] = null;
                    final Player player2 = _char.user.player;
                    player2.bagCount += 6;
                    final Item[] ItemBag = new Item[_char.user.player.bagCount];
                    for (byte i = 0; i < _char.user.player.ItemBag.length; ++i) {
                        ItemBag[i] = _char.user.player.ItemBag[i];
                    }
                    _char.user.player.ItemBag = ItemBag;
                    Service.updateBagCount(_char, _char.user.player.bagCount, indexUI);
                }
            } else if (item.itemId == 28) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                } else {
                    long exp = GameScr.exps[_char.cLevel]/10;
                    _char.user.player.updateExp(exp);
                   _char.user.player.ItemBagUse(indexUI, 1);
                }
            } else if (item.isItemBook()) {
                if (!_char.isHuman && item.template.id == 547) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                else {
                    final byte itemClass = Skill.ClassBook(item.itemId);
                    byte skillTemplateId = Skill.itemSkillTemplateId(item.template.id);
                    if (item.template.id == 547) {
                        skillTemplateId += _char.nClass;
                    }
                    if ((itemClass != 0 && itemClass != _char.nClass) || _char.nClass == 0) {
                        Service.ServerMessage(_char, Text.get(0, 90));
                    }
                    else if (_char.getSkill(skillTemplateId) != null) {
                        Service.ServerMessage(_char, Text.get(0, 89));
                    }
                    else {
                        _char.user.player.ItemBag[indexUI] = null;
                        if (item.template.id == 547) {
                            User.CreateNhanBan(_char.user.player);
                        }
                        if (_char.ctaskId == 9 && _char.ctaskIndex == 0) {
                            _char.uptaskMaint();
                        }
                        _char.upPointSkill(skillTemplateId, indexUI, (byte)1);
                    }
                }
            }
            else if (item.isItemReturn()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                else {
                    final Map map = MapServer.getMapServer(_char.mapLTDId);
                    if (map != null) {
                        final TileMap tileMap = map.getSlotZone(_char);
                        if (tileMap == null) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                        }
                        else {
                            if (item.template.id == 34) {
                                _char.user.player.ItemBagUse(indexUI, 1);
                            }
                            _char.tileMap.Exit(_char);
                            _char.cx = tileMap.map.template.goX;
                            _char.cy = tileMap.map.template.goY;
                            tileMap.Join(_char);
                        }
                    }
                }
            }
            else if (item.isItemRefreshPoint()) {
                if (item.template.id == 240) {
                    if (_char.refreshpPoint <= 100) {
                        ++_char.refreshpPoint;
                        _char.user.player.ItemBagClear(indexUI);
                        Service.ServerMessage(_char, String.format(Text.get(0, 124), _char.refreshpPoint));
                    }
                }
                else if (item.template.id == 241) {
                    if (_char.refreshsPoint <= 100) {
                        ++_char.refreshsPoint;
                        _char.user.player.ItemBagClear(indexUI);
                        Service.ServerMessage(_char, String.format(Text.get(0, 125), _char.refreshsPoint));
                    }
                }
            }
            else if (item.isItemGoFearriMap()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                else {
                    final Map map = MapServer.getMapServer(160);
                    if (map != null) {
                        final TileMap tileMap = map.getSlotZone(_char);
                        if (tileMap == null) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                        }
                        else {
                            _char.user.player.ItemBagUse(indexUI, 1);
                            _char.tileMap.Exit(_char);
                            _char.cx = tileMap.map.template.goX;
                            _char.cy = tileMap.map.template.goY;
                            tileMap.Join(_char);
                        }
                    }
                }
            }
            
            else if (item.isItemGoFearriMap1()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                else {
                    final Map map = MapServer.getMapServer(161);
                    if (map != null) {
                        final TileMap tileMap = map.getSlotZone(_char);
                        if (tileMap == null) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                        }
                        else {
                            _char.user.player.ItemBagUse(indexUI, 1);
                            _char.tileMap.Exit(_char);
                            _char.cx = tileMap.map.template.goX;
                            _char.cy = tileMap.map.template.goY;
                            tileMap.Join(_char);
                        }
                    }
                }
            }
            
            else if (item.isItemtuiluong()) {
                     int luong = Util.nextInt(10,30);
                    _char.user.player.upGold(luong, (byte) 1);
                    _char.user.player.ItemBagUse(indexUI, 1);
                
            }
            
            else if (item.isItemOpenEyes()) {
                Char.setEffect(_char, Effect.itemEffectId(item.template.id), -1, Effect.itemEffectTimeLength(item.template.id), Effect.itemEffectParam(item.template.id), null, (byte)1);
                _char.user.player.ItemBagUse(indexUI, 1);
            }
            else if (item.isItemMedicine()) {
                Char.setEffect(_char, Effect.itemEffectId(item.template.id), -1, Effect.itemEffectTimeLength(item.template.id), Effect.itemEffectParam(item.template.id), null, (byte)0);
                if (item.template.id == 278) {
                    Service.updateInfoMe(_char);
                    Player.updateInfoPlayer(_char);
                    if (_char.ItemBody[10] != null) {
                        Player.updateThuNuoiPlayer(_char);
                    }
                    if (_char.arrItemViThu[4] != null) {
                        Player.updateThuNuoiPlayer(_char);
                    }
                }
                _char.user.player.ItemBagUse(indexUI, 1);
            }
            else if (item.isItemOpenItemBody()) {
                if (_char.nClass == 0) {
                    Service.ServerMessage(_char, Text.get(0, 173));
                }
                else if (_char.user.player.ItemBagIndexNull() == -1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
                else {
                    final byte itemType = (byte)Util.nextInt(10);
                    int level = 0;
                    if (_char.cLevel >= 70) {
                        level = 70;
                    }
                    else if (_char.cLevel >= 60) {
                        level = 60;
                    }
                    else if (_char.cLevel >= 50) {
                        level = 50;
                    }
                    else if (_char.cLevel >= 40) {
                        level = 40;
                    }
                    byte upgrade = 0;
                    if (item.template.id == 383) {
                        upgrade = 12;
                        if (level < 40) {
                            level = 40;
                        }
                    }
                    else if (item.template.id == 384) {
                        upgrade = 14;
                        if (level < 50) {
                            level = 50;
                        }
                    }
                    else if (item.template.id == 385) {
                        upgrade = 16;
                        if (level < 60) {
                            level = 60;
                        }
                    }
                    Item it = null;
                    if (itemType == 1) {
                        it = ItemServer.itemWPShop(level, _char.nClass, (byte)1);
                    }
                    else {
                        final byte sys = (byte)Util.nextInt(1, 3);
                        for (byte n = 16; n <= 29; ++n) {
                            final Item[] arrItem = GameScr.itemStores.get(n);
                            for (byte k = 0; k < arrItem.length; ++k) {
                                final Item itemStore = arrItem[k];
                                if (itemStore != null && itemStore.template.type == itemType && itemStore.sys == sys && itemStore.template.level / 10 == level / 10 && (itemStore.template.gender == 2 || itemStore.template.gender == _char.cgender)) {
                                    it = itemStore.clone();
                                    it.quantity = 1;
                                    break;
                                }
                            }
                        }
                    }
                    if (it != null) {
                        it.upgradeNext(upgrade);
                        _char.user.player.ItemBagAdd(it);
                    }
                    _char.user.player.ItemBagUse(indexUI, 1);
                }
            }
            else if (item.isItemAnimalsHP() || item.isItemMotoHP()) {
                if (_char.ItemMounts[4] != null) {
                    if (!_char.ItemMounts[4].isItemAnimals2() && item.isItemAnimalsHP()) {
                        Service.ServerMessage(_char, Text.get(0, 175));
                    }
                    else if (!_char.ItemMounts[4].isItemMoto2() && item.isItemMotoHP()) {
                        Service.ServerMessage(_char, Text.get(0, 176));
                    }
                    else {
                        final Item itemMounts = _char.ItemMounts[4];
                        int hp = 0;
                        for (short l = 0; l < itemMounts.options.size(); ++l) {
                            final ItemOption option2 = itemMounts.options.get(l);
                            if (option2 != null && option2.optionTemplate.id == 66) {
                                hp = option2.param;
                                break;
                            }
                        }
                        if (hp >= 1000) {
                            if (item.isItemAnimalsHP()) {
                                Service.ServerMessage(_char, Text.get(0, 315));
                            }
                            if (item.isItemMotoHP()) {
                                Service.ServerMessage(_char, Text.get(0, 316));
                            }
                        }
                        else {
                            for (short l = 0; l < itemMounts.options.size(); ++l) {
                                final ItemOption option2 = itemMounts.options.get(l);
                                if (option2 != null && option2.optionTemplate.id == 66) {
                                    final ItemOption itemOption = option2;
                                    itemOption.param += ItemServer.itemParam(item.template.id);
                                    if (option2.param > 1000) {
                                        option2.param = 1000;
                                        break;
                                    }
                                }
                            }
                            Player.LoadThuCuoi(_char);
                            _char.user.player.ItemBagUse(indexUI, 1);
                        }
                    }
                }
            }
            else if (item.isItemAnimalsExp() || item.isItemMotoExp()) {
                if (_char.ItemMounts[4] != null && !_char.ItemMounts[4].isExpires) {
                    if (!_char.ItemMounts[4].isItemAnimals2() && item.isItemAnimalsExp()) {
                        Service.ServerMessage(_char, Text.get(0, 175));
                    }
                    else if (!_char.ItemMounts[4].isItemMoto2() && item.isItemMotoExp()) {
                        Service.ServerMessage(_char, Text.get(0, 176));
                    }
                    else if (_char.ItemMounts[4].upgrade >= 99) {
                        Service.ServerMessage(_char, Text.get(0, 177));
                    }
                    else {
                        final Item itemMounts = _char.ItemMounts[4];
                        for (short m = 0; m < itemMounts.options.size(); ++m) {
                            final ItemOption option3 = itemMounts.options.get(m);
                            if (option3 != null && option3.optionTemplate.id == 65) {
                                final ItemOption itemOption2 = option3;
                                itemOption2.param += ItemServer.itemParam(item.template.id);
                                if (option3.param >= 1000) {
                                    option3.param = 0;
                                    final Item item3 = itemMounts;
                                    ++item3.upgrade;
                                    for (short j2 = 0; j2 < itemMounts.options.size(); ++j2) {
                                        final ItemOption option4 = itemMounts.options.get(j2);
                                        if (option4 != null && option4.optionTemplate.id != 65 && option4.optionTemplate.id != 66) {
                                            for (byte k2 = 0; k2 < ItemUse.openThucuoiOptionId.length; ++k2) {
                                                if (ItemUse.openThucuoiOptionId[k2] == option4.optionTemplate.id) {
                                                    option4.param = Item.setMountsLevel((byte)(itemMounts.upgrade + 1), option4);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        Player.LoadThuCuoi(_char);
                        GameCanvas.callEffect(_char, (short)58);
                        _char.user.player.ItemBagUse(indexUI, 1);
                    }
                }
            }
            else if (item.isItemNextStar()) {
                if (_char.ItemMounts[4] != null) {
                    if (_char.ItemMounts[4].sys <= 3) {
                        if (_char.ItemMounts[4].upgrade < 99) {
                            Service.ServerMessage(_char, Text.get(0, 178));
                        }
                        else {
                            final Item itemMounts = _char.ItemMounts[4];
                            final boolean success = Util.nextInt(100) < 50 / (_char.ItemMounts[4].sys + 1);
                            if (success) {
                                Service.ServerMessage(_char, Text.get(0, 179));
                                itemMounts.upgrade = 0;
                                final Item item4 = itemMounts;
                                ++item4.sys;
                                for (short l = 0; l < itemMounts.options.size(); ++l) {
                                    final ItemOption option2 = itemMounts.options.get(l);
                                    if (option2 != null && option2.optionTemplate.id != 65 && option2.optionTemplate.id != 66) {
                                        for (byte k3 = 0; k3 < ItemUse.openThucuoiOptionId.length; ++k3) {
                                            if (ItemUse.openThucuoiOptionId[k3] == option2.optionTemplate.id) {
                                                option2.param = Item.setMountsStar(option2);
                                                break;
                                            }
                                        }
                                    }
                                }
                                Player.LoadThuCuoi(_char);
                            }
                            else {
                                Service.ServerMessage(_char, Text.get(0, 180));
                            }
                            _char.user.player.ItemBagUse(indexUI, 1);
                        }
                    }
                }
            }
            else if (item.isItemEventUse()) {
                Event.UseItem(_char, item);
            } else if (item.isItemViThuUse()) {
                ViThu.UseItem(_char, item);
            } else if (item.isItemTaskUse()) {
                Task.useItemTask(_char, item);
            } else if (item.template.id == 251) {
                _char.menuType = -124;
                Service.openUIMenuNew(_char, new String[] { "Sách tiềm năng","Sách kỹ năng"});
            } else if (item.isItemUpskill()) {
                final int point = ItemServer.itemParam(item.template.id);
                Char.upPoint(_char, (byte)1, point);
                Service.ServerMessage(_char, String.format(Text.get(0, 182), point));
                _char.user.player.ItemBagClear(indexUI);
                Player.nextItemUseLimit(_char, item.template.id);
            }
            else if (item.isItemUppotential() && _char.nClass != 0) {
                final int point = ItemServer.itemParam(item.template.id);
                Char.upPoint(_char, (byte)0, point);
                Service.ServerMessage(_char, String.format(Text.get(0, 183), point));
                _char.user.player.ItemBagClear(indexUI);
                Player.nextItemUseLimit(_char, item.template.id);
            }
            else if (item.isItemClanPoint()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                else if (_char.clan != null && !_char.cClanName.isEmpty()) {
                    if (_char.clan.level < Clan.useItemLevel(item.template.id)) {
                        Service.ServerMessage(_char, Text.get(0, 210));
                    }
                    else {
                        Char.upPointClan(_char, Clan.useItemParam(item.template.id));
                        _char.user.player.ItemBagUse(indexUI, 1);
                    }
                }
                else {
                    Service.ServerMessage(_char, Text.get(0, 233));
                }
            }
            else if (item.isItemReward()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                else if (_char.user.player.ItemBagIndexNull() == -1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
                else {
                    BackCave.ItemUse(_char, item);
                    _char.user.player.ItemBagUse(indexUI, 1);
                    if (_char.ctaskId == 40 && _char.ctaskIndex == 1) {
                        _char.uptaskMaint();
                    }
                }
            }
            else if (item.isItemPkDown()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                else if (_char.cPk > 0) {
                    _char.cPk -= (byte)ItemServer.itemParam(item.template.id);
                    if (_char.cPk < 0) {
                        _char.cPk = 0;
                    }
                    _char.user.player.ItemBagUse(indexUI, 1);
                }
                else {
                    Service.ServerMessage(_char, String.format(Text.get(0, 278), _char.cPk));
                }
            }
            else if (item.isItemCaveCard()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                else {
                    _char.countPB += (byte)ItemServer.itemParam(item.template.id);
                    _char.user.player.ItemBagUse(indexUI, 1);
                    Player.nextItemUseLimit(_char, item.template.id);
                    GameCanvas.addInfoDlg(_char, String.format(Text.get(0, 277), _char.countPB));
                }
            }
            else if (item.isItemClearExpDown()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                else if (_char.cExpDown > 0L) {
                    if (item.template.id == 254 && _char.cLevel > 30) {
                        Service.ServerMessage(_char, String.format(Text.get(0, 314), 1, 30));
                    }
                    else if (item.template.id == 255 && _char.cLevel > 60) {
                        Service.ServerMessage(_char, String.format(Text.get(0, 314), 30, 60));
                    }
                    else {
                        _char.updateExp(_char.cExpDown);
                        _char.user.player.ItemBagUse(indexUI, 1);
                    }
                }
            }
            else if (item.isItemRod()) {
                if (_char.tileMap.tileTypeAt(_char.cx, _char.cy - 1, 64)) {
                    Service.ServerMessage(_char, Text.get(0, 326));
                    _char.delaythacau = 3000;
                }
                else {
                    Service.ServerMessage(_char, Text.get(0, 327));
                }
            }
            else if (item.template.id == 268) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }else {
                    _char.countTaskTaThu++;
                    _char.user.player.ItemBagClear(indexUI);
                    Player.nextItemUseLimit(_char, item.template.id);
                    Service.ServerMessage(_char, String.format("Số lần nhận nhiệm vụ tà thú hôm nay của bạn là %d.",_char.countTaskTaThu));
                }
            }
            else if (item.isItemNgocRong()) {
                if (_char.user.player.ItemBagQuantity((short)222) < 1 || _char.user.player.ItemBagQuantity((short)223) < 1 || _char.user.player.ItemBagQuantity((short)224) < 1 || _char.user.player.ItemBagQuantity((short)225) < 1 || _char.user.player.ItemBagQuantity((short)226) < 1 || _char.user.player.ItemBagQuantity((short)227) < 1 || _char.user.player.ItemBagQuantity((short)228) < 1) {
                    Service.ServerMessage(_char, Text.get(0, 318));
                }
                else if (_char.user.player.ItemBagIndexNull() == -1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
                else {
                    GameCanvas.callEffectBall(_char);
                    short templateId = -1;
                    if (_char.sys() == 1) {
                        templateId = 420;
                    }
                    else if (_char.sys() == 2) {
                        templateId = 421;
                    }
                    else if (_char.sys() == 3) {
                        templateId = 422;
                    }
                    if (templateId != -1) {
                        _char.user.player.ItemBagUses((short)222, 1);
                        _char.user.player.ItemBagUses((short)223, 1);
                        _char.user.player.ItemBagUses((short)224, 1);
                        _char.user.player.ItemBagUses((short)225, 1);
                        _char.user.player.ItemBagUses((short)226, 1);
                        _char.user.player.ItemBagUses((short)227, 1);
                        _char.user.player.ItemBagUses((short)228, 1);
                        _char.user.player.ItemBagAdd(new Item(ItemOption.arrOptionDefault(templateId, (byte)0), templateId, 1, -1, true, (byte)0, 5));
                    }
                    try {
                        for (short m = 0; m < _char.tileMap.numPlayer; ++m) {
                            final Char player = _char.tileMap.aCharInMap.get(m);
                            if (player != null && player.user != null && player.user.session != null && player.charID != _char.charID) {
                                GameCanvas.callEffectBall1(player);
                            }
                        }
                    }
                    catch (Exception ex) {}
                }
            }
            else if (item.isItemPrecious()) {
                if (_char.user.player.ItemBagIndexNull() == -1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
                else {
                    Item it2 = null;
                    if (item.template.id == 535) {
                        if (Util.nextInt(100) < 50) {
                            it2 = new Item(null, (short)Util.nextInt(449, 453), 1, -1, item.isLock, (byte)0, 0);
                        }
                        else if (Util.nextInt(100) < 50) {
                            it2 = new Item(null, (short)449, 1, -1, item.isLock, (byte)0, 0);
                        }
                        else if (Util.nextInt(100) < 25) {
                            it2 = new Item(null, (short)Util.nextInt(573, 575), 1, -1, item.isLock, (byte)0, 0);
                        }
                        else if (Util.nextInt(100) < 10) {
                            final short templateId2 = (short)Util.nextInt(439, 442);
                            it2 = new Item(ItemOption.arrOptionDefault(templateId2, (byte)0), templateId2, 1, -1, item.isLock, (byte)0, 5);
                        }
                        else if (Util.nextInt(100) < 5) {
                            final byte[] days = { 3, 7, 30 };
                            it2 = new Item(ItemOption.arrOptionDefault((short)523, (byte)0), (short)523, 1, 86400 * days[Util.nextInt(days.length)], false, (byte)0, 5);
                        }
                        else if (Util.nextInt(100) < 5) {
                            it2 = new Item(ItemOption.arrOptionDefault((short)443, (byte)0), (short)443, 1, -1, false, (byte)0, 5);
                        }
                        else {
                            it2 = new Item(null, (short)444, 1, -1, item.isLock, (byte)0, 0);
                        }
                    }
                    else if (item.template.id == 536) {
                        if (Util.nextInt(100) < 50) {
                            it2 = new Item(null, (short)Util.nextInt(470, 472), 1, -1, item.isLock, (byte)0, 0);
                        }
                        else if (Util.nextInt(100) < 50) {
                            it2 = new Item(null, (short)Util.nextInt(576, 578), 1, -1, item.isLock, (byte)0, 0);
                        }
                        else if (Util.nextInt(100) < 20) {
                            final short templateId2 = (short)Util.nextInt(486, 489);
                            it2 = new Item(ItemOption.arrOptionDefault(templateId2, (byte)0), templateId2, 1, -1, item.isLock, (byte)0, 5);
                        }
                        else if (Util.nextInt(100) < 5) {
                            it2 = new Item(ItemOption.arrOptionDefault((short)485, (byte)0), (short)485, 1, -1, false, (byte)0, 5);
                        }
                        else {
                            it2 = new Item(null, (short)469, 1, -1, item.isLock, (byte)0, 0);
                        }
                    }
                    if (it2 != null) {
                        if (it2.template.isUpToUp) {
                            _char.user.player.ItemBagAddQuantity(it2);
                        }
                        else {
                            _char.user.player.ItemBagAdd(it2);
                        }
                    }
                    _char.user.player.ItemBagUse(indexUI, 1);
                }
            }
            else if (item.isItemDaDanhVong()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                else if (_char.user.player.ItemBagSlotNull() < 1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
                else if (item.template.id != 704) {
                    final int num = item.quantity / 10;
                    if (num > 0) {
                        final Item itnew = new Item(null, (short)(item.template.id + 1), num, -1, true, (byte)0, 0);
                        _char.user.player.ItemBagAddQuantity(itnew);
                        _char.user.player.ItemBagUse(indexUI, num * 10);
                    }
                }
            }
            else if (item.isItemDVPhu()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                else {
                    final short[] nvDV = _char.nvDV;
                    final int n2 = 0;
                    nvDV[n2] += 5;
                    _char.user.player.ItemBagUse(indexUI, 1);
                    Player.nextItemUseLimit(_char, item.template.id);
                    Service.ServerMessage(_char, String.format(Text.get(0, 367), _char.nvDV[0]));
                }
            }
            else if (item.isItemEventUse_1()) {
                Event_1.UseItem(_char, item);
            }
            else if (item.isItemJirai()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                int index = item.itemId - 733;
                if(_char.user.player.ItemBST[index] == null) {
                    if(_char.user.player.ItemBagQuantity(item.itemId) < 100) {
                        Service.ServerMessage(_char,"Bạn không đủ 100 mảnh " + item.template.name + " để ghép.");
                        return;
                    }
                    _char.user.player.ItemBagUses(item.itemId,100);
                    Item itadd = new Item(null,(short) IdJirai(index),1,-1,true,(byte) 0,0);
                    _char.user.player.ItemBST[index] = itadd;
                    _char.user.player.ItemBST[index].upgrade = 1;
                    _char.user.player.ItemBST[index].indexUI = index;
                    Service.ServerMessage(_char, String.format(Text.get(0,398),itadd.template.name));
                } else {
                    Item checkIt = _char.user.player.ItemBST[index];
                    if (checkIt.upgrade >= 10) {
                        Service.ServerMessage(_char,String.format(Text.get(0,400),checkIt.template.name));
                    } else if (_char.user.player.ItemBagQuantity(item.itemId) < 100*(checkIt.upgrade+1)) {
                        Service.ServerMessage(_char,String.format(Text.get(0,399),item.template.name));
                    } else {
                        _char.user.player.ItemBagUses(item.itemId,100*(checkIt.upgrade+1));
                        _char.user.player.ItemBST[index].upgrade += 1;
                        _char.user.player.ItemBST[index].indexUI = index;
                        Service.ServerMessage(_char,String.format(Text.get(0,401),checkIt.template.name,checkIt.upgrade));
                    }
                }
            }
            else if (item.isItemJumito()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                int index = item.itemId - 760;
                if(_char.user.player.ItemBST[index] == null) {
                    if(_char.user.player.ItemBagQuantity(item.itemId) < 100) {
                        Service.ServerMessage(_char,"Bạn không đủ 100 mảnh " + item.template.name + " để ghép.");
                        return;
                    }
                    _char.user.player.ItemBagUses(item.itemId,100);
                    Item itadd = new Item(null,(short) IdJumito(index),1,-1,true,(byte) 0,0);
                    _char.user.player.ItemBST[index] = itadd;
                    _char.user.player.ItemBST[index].upgrade = 1;
                    _char.user.player.ItemBST[index].indexUI = index;
                    Service.ServerMessage(_char, String.format(Text.get(0,398),itadd.template.name));
                } else {
                    Item checkIt = _char.user.player.ItemBST[index];
                    if (checkIt.upgrade >= 10) {
                        Service.ServerMessage(_char,String.format(Text.get(0,400),checkIt.template.name));
                    } else if (_char.user.player.ItemBagQuantity(item.itemId) < 100*(checkIt.upgrade+1)) {
                        Service.ServerMessage(_char,String.format(Text.get(0,399),item.template.name));
                    } else {
                        _char.user.player.ItemBagUses(item.itemId,100*(checkIt.upgrade+1));
                        _char.user.player.ItemBST[index].upgrade += 1;
                        _char.user.player.ItemBST[index].indexUI = index;
                        Service.ServerMessage(_char,String.format(Text.get(0,401),checkIt.template.name,checkIt.upgrade));
                    }
                }
            }
            else if (item.itemId == 775) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                if(_char.user.player.ItemCaiTrang[0] == null) {
                    if(_char.user.player.ItemBagQuantity(item.itemId) < 1000) {
                        Service.ServerMessage(_char,"Bạn không đủ 1000 mảnh " + item.template.name + " để ghép.");
                        return;
                    }
                    _char.user.player.ItemBagUses(item.itemId,1000);
                    Item itadd = new Item(ItemOption.arrOptionDefault((short) 774,(byte) 0),(short) 774,1,-1,true,(byte) 0,0);
                    _char.user.player.ItemCaiTrang[0] = itadd;
                    _char.user.player.ItemCaiTrang[0].upgradeCT((byte)1,(short) 774);
                    _char.user.player.ItemCaiTrang[0].indexUI = 0;
                    Service.ServerMessage(_char, String.format(Text.get(0,398),itadd.template.name));
                } else {
                    Item checkIt = _char.user.player.ItemCaiTrang[0];
                    if (checkIt.upgrade >= 10) {
                        Service.ServerMessage(_char,String.format(Text.get(0,400),checkIt.template.name));
                    } else if (_char.user.player.ItemBagQuantity(item.itemId) < 1000*(checkIt.upgrade+1)) {
                        Service.ServerMessage(_char,String.format(Text.get(0,399),item.template.name));
                    } else {
                        _char.user.player.ItemBagUses(item.itemId,1000*(checkIt.upgrade+1));
                        _char.user.player.ItemCaiTrang[0].upgradeCT((byte)1,(short) 774);
                        _char.user.player.ItemCaiTrang[0].indexUI = 0;
                        Service.ServerMessage(_char,String.format(Text.get(0,401),checkIt.template.name,checkIt.upgrade));
                    }
                }
            } else if (item.itemId == 788 || item.itemId == 789) {
                int index = item.itemId - 787;
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                if(_char.user.player.ItemCaiTrang[index] == null) {
                    if(_char.user.player.ItemBagQuantity(item.itemId) < 1000) {
                        Service.ServerMessage(_char,"Bạn không đủ 1000 mảnh " + item.template.name + " để ghép.");
                        return;
                    }
                    _char.user.player.ItemBagUses(item.itemId,1000);
                    Item itadd = new Item(ItemOption.arrOptionDefault((short) (item.itemId-2),(byte) 0),(short) (item.itemId-2),1,-1,true,(byte) 0,0);
                    _char.user.player.ItemCaiTrang[index] = itadd;
                    _char.user.player.ItemCaiTrang[index].upgradeCT((byte) 1,(short) (item.itemId-2));
                    _char.user.player.ItemCaiTrang[index].indexUI = index;
                    Service.ServerMessage(_char, String.format(Text.get(0,398),itadd.template.name));
                } else {
                    Item checkIt = _char.user.player.ItemCaiTrang[index];
                    if (checkIt.upgrade >= 10) {
                        Service.ServerMessage(_char,String.format(Text.get(0,400),checkIt.template.name));
                    } else if (_char.user.player.ItemBagQuantity(item.itemId) < 1000 * (checkIt.upgrade+1)) {
                        Service.ServerMessage(_char,String.format(Text.get(0,399),item.template.name));
                    } else {
                        _char.user.player.ItemBagUses(item.itemId,1000*(checkIt.upgrade+1));
                        _char.user.player.ItemCaiTrang[index].upgradeCT((byte)1,(short) (item.itemId-2));
                        _char.user.player.ItemCaiTrang[index].indexUI = index;
                        Service.ServerMessage(_char,String.format(Text.get(0,401),checkIt.template.name,checkIt.upgrade));
                    }
                }
            } else if (item.isFoodTT()) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                Clan clan = _char.clan;
                if (clan.clan_thanThu.size() < 1) {
                    Service.ServerMessage(_char,"Gia tộc bạn chưa có thần thú.");
                } else if (clan.clan_thanThu.get(0).items.itemId == 596 || clan.clan_thanThu.get(0).items.itemId == 601) {
                    Service.ServerMessage(_char,"Thần thú chưa nở");
                } else if (clan.clan_thanThu.get(0).level >= 120) {
                    Service.ServerMessage(_char,"Thần thú đã đạt cấp tối đa.");
                } else {
                    if (item.itemId == 598) {
                        clan.clan_thanThu.get(0).upExpThanThu(_char, 50);
                        _char.user.player.ItemBagUse(indexUI, 1);
                    } else if (item.itemId == 599) {
                        clan.clan_thanThu.get(0).upExpThanThu(_char, 150);
                        _char.user.player.ItemBagUse(indexUI, 1);
                    } else if (item.itemId == 600) {
                        clan.clan_thanThu.get(0).upExpThanThu(_char, 1000);
                        _char.user.player.ItemBagUse(indexUI, 1);
                    }
                }
            } else if (item.itemId == 604) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                Clan clan = _char.clan;
                if (clan.clan_thanThu.size() < 1) {
                    Service.ServerMessage(_char,"Gia tộc bạn chưa có thần thú.");
                } else if (clan.clan_thanThu.get(0).items.itemId == 596 || clan.clan_thanThu.get(0).items.itemId == 601) {
                    Service.ServerMessage(_char,"Thần thú chưa nở");
                } else {
                    Item itemUp = clan.clan_thanThu.get(0).items;
                    itemUp.expires = System.currentTimeMillis() + 86400000L;
                    itemUp.isExpires = true;
                    _char.user.player.ItemBagAdd(itemUp);
                    _char.user.player.ItemBagUse(indexUI, 1);
                }
            } else if (item.itemId == 605) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                }
                Clan clan = _char.clan;
                if (clan.clan_thanThu.size() < 1) {
                    Service.ServerMessage(_char,"Gia tộc bạn chưa có thần thú.");
                } else if (clan.clan_thanThu.get(0).items.itemId == 596 || clan.clan_thanThu.get(0).items.itemId == 601) {
                    Service.ServerMessage(_char,"Thần thú chưa nở");
                } else {
                    if (clan.clan_thanThu.get(0).stars >= 3) {
                        Service.ServerMessage(_char, "Thần thú đã được tiến hoá tối đa.");
                        return;
                    }
                    clan.clan_thanThu.get(0).upStarsThanThu(_char);
                    _char.user.player.ItemBagUse(indexUI, 1);
                }
            } else if (item.itemId == 261) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                } else if (_char.tileMap.map.isClanManor()) {
                    Char.setEffect(_char, (byte) 23, -1, 300000, (short) 0, null, (byte)0);
                    _char.user.player.ItemBagUse(indexUI,1);
                } else {
                    Service.ServerMessage(_char,"Vật phầm này chỉ dùng trong Lãnh Địa Gia Tộc.");
                }
            } else if (item.itemId == 597) {
                if (!_char.isHuman) {
                    Service.ServerMessage(_char, Text.get(0, 310));
                } else if (_char.user.player.ItemBagSlotNull() < 1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                } else {
                    fishing(_char);
                }
            } else if (item.isEggViThu()) {
                ViThu.useEgg(_char, item, indexUI);
            } else if (item.template.id == 288) {
                _char.uptaskMaint();
            }
        }
    }
    
    protected static void useItemChangeMap(final Char _char, final byte indexUI, final byte indexMenu) {
        final Item item = _char.user.player.ItemBag(indexUI);
        if (item != null && item.isItemChangeMap()) {
            Map map = null;
            if (indexMenu == 0 || indexMenu == 1 || indexMenu == 2) {
                map = MapServer.getMapServer(Map.arrTruong[indexMenu]);
            }
            else if (indexMenu == 3 || indexMenu == 4 || indexMenu == 5 || indexMenu == 6 || indexMenu == 7 || indexMenu == 8 || indexMenu == 9) {
                map = MapServer.getMapServer(Map.arrLang[indexMenu - 3]);
            }
            if (map != null) {
                final TileMap tileMap = map.getSlotZone(_char);
                if (tileMap == null) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                }
                else if (Task.isLockChangeMap2(tileMap.mapID, _char.ctaskId)) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 84));
                }
                else {
                    _char.tileMap.Exit(_char);
                    _char.cx = tileMap.map.template.goX;
                    _char.cy = tileMap.map.template.goY;
                    tileMap.Join(_char);
                    if (item.template.id == 35) {
                        _char.user.player.ItemBagUse(indexUI, 1);
                    }
                }
            }
        }
    }

    public static void fishing(Char _char) {
        if (_char.tileMap.mapID == 32) {
            if (_char.cy == 456 && _char.cx > 107 && _char.cx < 2701) {
                if (_char.user.player.ItemBagQuantity((short) 602) >= 1 || _char.user.player.ItemBagQuantity((short) 603) >= 1) {
                    boolean coMoicau = false;
                    short idMoicau = 602;
                    for (Item itemMoi : _char.user.player.ItemBag) {
                        if (itemMoi != null && (itemMoi.itemId == 602 || itemMoi.itemId == 603)) {
                            _char.user.player.ItemBagUses(itemMoi.itemId,1);
                            idMoicau = itemMoi.itemId;
                            coMoicau = true;
                            break;
                        }
                    }

                    if (coMoicau) {
                        int percent = Util.nextInt(100);
                        if (idMoicau == 602) {
//                                    try {
//                                        Service.ServerMessage(_char, "Đang thả câu...");
//                                        Thread.sleep(2000L);
//                                    } catch (Exception ex) {}
                            if (percent > 55) {
                                Item itemUp;
                                if (Util.nextInt(10) > 7) {
                                    itemUp = new Item(null, (short) 599, 1, -1, false, (byte) 0, 0);
                                } else {
                                    itemUp = new Item(null, (short) 598, 1, -1, false, (byte) 0, 0);
                                }
                                _char.user.player.ItemBagAddQuantity(itemUp);
                            } else {
                                Service.ServerMessage(_char, "Không câu được gì cả.");
                            }
                        } else if (idMoicau == 603) {
//                                    try {
//                                        Service.ServerMessage(_char, "Đang thả câu...");
//                                        Thread.sleep(2000L);
//                                    } catch (Exception ex) {}
                            if (percent > 55) {
                                if (Util.nextInt(120) == 31) {
                                    Item itemUp = new Item(null, (short) 600, 1, -1, false, (byte) 0, 0);
                                    _char.user.player.ItemBagAddQuantity(itemUp);
                                } else {
                                    Item itemUp;
                                    if (Util.nextInt(10) > 7) {
                                        itemUp = new Item(null, (short) 599, 1, -1, false, (byte) 0, 0);
                                    } else {
                                        itemUp = new Item(null, (short) 598, 1, -1, false, (byte) 0, 0);
                                    }
                                    _char.user.player.ItemBagAddQuantity(itemUp);
                                }
                            } else {
                                Service.ServerMessage(_char, "Không câu được gì cả.");
                            }
                        }
                    }
                } else {
                    Service.ServerMessage(_char, "Không có mồi để câu.");
                }
            } else {
                Service.ServerMessage(_char, "Không thể câu ở đây.");
            }
        } else {
            Service.ServerMessage(_char, "Hãy đi đến vùng nước ở làng chài để câu cá.");
        }
    }

    public static int IdJirai(int id) { // Bộ sưu tập Khổng Minh Tiến
        switch (id) {
            case 0: return 746;
            case 1: return 747;
            case 2: return 712;
            case 3: return 713;
            case 4: return 748;
            case 5: return 752;
            case 6: return 751;
            case 7: return 750;
            case 8: return 749;
        }
        return -1;
    }

    public static int IdJumito(int id) { // Bộ sưu tập Khổng Minh Tiến
        switch (id) {
            case 0: return 753;
            case 1: return 754;
            case 2: return 715;
            case 3: return 716;
            case 4: return 755;
            case 5: return 759;
            case 6: return 758;
            case 7: return 757;
            case 8: return 756;
        }
        return -1;
    }

    static {
        openThucuoiOptionId = new int[] { 6, 7, 10, 67, 68, 69, 70, 71, 72, 73, 74 };
        openThucuoiOptionParam = new int[] { 50, 50, 10, 5, 10, 10, 5, 5, 5, 100, 50 };
        arrBagCount = new byte[] { 6, 6, 12, 66};
        arrBagTemplateId = new short[] { 215, 229, 283};
        optionsIdViThu = new int[] {140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151};
    }
}
