 package com.Server.ninja.Server;

public class Admission {
    protected static void Admission(final Char _char, final byte npcTemplateId, final byte nClass) {
        if (_char.ctaskId < 9) {
            Service.openUISay(_char, npcTemplateId, Text.get(0, 85));
        } else if (_char.nClass != 0) {
            Service.openUISay(_char, npcTemplateId, Text.get(0, 86));
        } else if (_char.ItemBody[1] != null) {
            Service.openUISay(_char, npcTemplateId, Text.get(0, 87));
        } else if (_char.user.player.ItemBagSlotNull() < 2) {
            Service.openUISay(_char, npcTemplateId, String.format(Text.get(0, 95), 2));
        } else {
            _char.ASkill.clear();
            _char.myskill = null;
            _char.CSkill[0] = -1;
            _char.nClass = nClass;
            _char.sPoint += (short) (_char.cLevel - 9);
            _char.pPoint = 0;
            for (int i = 1; i <= _char.cLevel; ++i) {
                _char.pPoint += (short) GameScr.getMaxpPoint(i);
            }
            if (_char.s()) {
                _char.potential[0] = 5;
                _char.potential[1] = 5;
                _char.potential[2] = 10;
                _char.potential[3] = 10;
            } else {
                _char.potential[0] = 15;
                _char.potential[1] = 5;
                _char.potential[2] = 5;
                _char.potential[3] = 5;
            }
            _char.cHP = _char.cMaxHP();
            _char.cMP = _char.cMaxMP();
            switch (nClass) {
                case 1: {
                    _char.user.player.ItemBagAdd(new Item(null, (short) 40, 1, -1, true, (byte) 0, 0));
                    final Item item = ItemServer.getItemStore((short) 94, (byte) nClass, (byte) 1);
                    item.isLock = true;
                    item.saleCoinLock = 5;
                    _char.user.player.ItemBagAdd(item);
                    break;
                }
                case 2: {
                    _char.user.player.ItemBagAdd(new Item(null, (short) 49, 1, -1, true, (byte) 0, 0));
                    final Item item = ItemServer.getItemStore((short) 114, (byte) nClass, (byte) 1);
                    item.isLock = true;
                    item.saleCoinLock = 5;
                    _char.user.player.ItemBagAdd(item);
                    break;
                }
                case 3: {
                    _char.user.player.ItemBagAdd(new Item(null, (short) 58, 1, -1, true, (byte) 0, 0));
                    final Item item = ItemServer.getItemStore((short) 99, (byte) nClass, (byte) 1);
                    item.isLock = true;
                    item.saleCoinLock = 5;
                    _char.user.player.ItemBagAdd(item);
                    break;
                }
                case 4: {
                    _char.user.player.ItemBagAdd(new Item(null, (short) 67, 1, -1, true, (byte) 0, 0));
                    final Item item = ItemServer.getItemStore((short) 109, (byte) nClass, (byte) 1);
                    item.isLock = true;
                    item.saleCoinLock = 5;
                    _char.user.player.ItemBagAdd(item);
                    break;
                }
                case 5: {
                    _char.user.player.ItemBagAdd(new Item(null, (short) 76, 1, -1, true, (byte) 0, 0));
                    final Item item = ItemServer.getItemStore((short) 104, (byte) nClass, (byte) 1);
                    item.isLock = true;
                    item.saleCoinLock = 5;
                    _char.user.player.ItemBagAdd(item);
                    break;
                }
                case 6: {
                    _char.user.player.ItemBagAdd(new Item(null, (short) 85, 1, -1, true, (byte) 0, 0));
                    final Item item = ItemServer.getItemStore((short) 119, (byte) nClass, (byte) 1);
                    item.isLock = true;
                    item.saleCoinLock = 5;
                    _char.user.player.ItemBagAdd(item);
                    break;
                }
            }
            Service.openUISay(_char, npcTemplateId, Text.get(0, 88));
            Service.MELoadClass(_char);
            try {
                for (int j = 0; j < _char.tileMap.numPlayer; ++j) {
                    if (_char.tileMap.aCharInMap.get(j).user != null && _char.tileMap.aCharInMap.get(j).user.session != null && _char.charID != _char.tileMap.aCharInMap.get(j).charID) {
                        Service.PlayerLoadAll(_char.tileMap.aCharInMap.get(j), _char);
                    }
                }
            } catch (Exception ex) {
            }
        }
    }
}
