 package com.Server.ninja.Server;

import com.Server.ninja.option.ItemOption;

public class Event_1
{
    protected static final int setTimeSale = 7200000;
    protected static final int set_BuyCount = 1000;
    protected static final int setTime = 3600000;
    protected static long timeRefresh;
    protected static long timeSale;
    protected static int buy_Count;
    protected static boolean isRaxu;
    protected static int indexTrung;
    protected static int indexDung;
    
    protected static synchronized void UseItem(final Char _char, final Item item) {
        if (GameScr.vEvent_1 != 1 && item.template.id == 824) {
            Service.ServerMessage(_char, Text.get(0, 166));
        }
        else if (item.template.id == 824) {
            if (!_char.isHuman) {
                Service.ServerMessage(_char, Text.get(0, 310));
            }
            else if (_char.user.player.ItemBagSlotNull() < 3) {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
            }
            else {
                Item it = null;
                if (Event_1.indexTrung == Event_1.indexDung++) {
                    it = new Item(null, (short)Util.nextInt(819, 823), 1, -1, true, (byte)0, 0);
                    Event_1.timeRefresh = 0L;
                    Event_1.buy_Count = 0;
                    Event_1.timeSale = 7200000L;
                }
                else if (Util.nextInt(100) < 10) {
                    it = new Item(null, (short)251, 1, -1, false, (byte)0, 0);
                }
                else if (Util.nextInt(100) < 5) {
                    it = new Item(ItemOption.arrOptionDefault((short)825, (byte)0), (short)825, 1, 604800, false, (byte)0, 5);
                }
                else if (Util.nextInt(100) < 5) {
                    it = new Item(ItemOption.arrOptionDefault((short)826, (byte)0), (short)826, 1, 604800, false, (byte)0, 5);
                }
                else if (Util.nextInt(100) < 5) {
                    it = ItemServer.getItemStore((short)Util.nextInt(632, 637), (byte)Util.nextInt(1, 3), (byte)1);
                }
                else if (Util.nextInt(100) < 5) {
                    it = ItemServer.getItemStore((short)Util.nextInt(632, 637), (byte)Util.nextInt(1, 3), (byte)0);
                }
                if (Util.nextInt(100) < 100) {
                    if (Util.nextInt(2) < 1) {
                        _char.user.player.ItemBagAdd(new Item(ItemOption.arrOptionDefault((short)407, (byte)0), (short)Util.nextInt(407, 408), 1, 7776000, false, (byte)0, 5));
                    }
                    else {
                        _char.user.player.ItemBagAdd(new Item(ItemOption.arrOptionDefault((short)403, (byte)0), (short)Util.nextInt(403, 404), 1, 7776000, false, (byte)0, 5));
                    }
                    _char.user.player.ItemBagAddQuantity(new Item(null, (short)Util.nextInt(535, 536), 1, -1, false, (byte)0, 0));
                    _char.user.player.ItemBagAddQuantity(new Item(null, (short)573, 3, -1, false, (byte)0, 0));
                }
                if (it != null) {
                    if (it.template.isUpToUp && _char.user.player.ItemBagIndexNull() != -1) {
                        _char.user.player.ItemBagAddQuantity(it);
                    }
                    else if (_char.user.player.ItemBagIndexNull() != -1) {
                        _char.user.player.ItemBagAdd(it);
                    }
                    if (it.template.id >= 819 && it.template.id <= 823) {
                        Client.alertServer(String.format(Text.get(0, 172), _char.cName, item.template.name, it.template.name));
                    }
                }
                _char.user.player.ItemBagClear((byte)item.indexUI);
            }
        }
    }
    
    protected static void doEventMenu(final Char _char, final byte menuId, final byte optionId, final short npcTemplateId) {
        final Player player = _char.user.player;
        switch (GameScr.vEvent_1) {
            case 1: {
                if (player.ItemBagSlotNull() < 1) {
                    Service.openUISay(_char, npcTemplateId, String.format(Text.get(0, 167), 1));
                    break;
                }
                if (menuId == 0) {
                    if (player.ItemBagQuantity((short)821) < 1) {
                        Service.openUISay(_char, npcTemplateId, Text.get(0, 329));
                        break;
                    }
                    player.ItemBagAdd(new Item(null, (short)383, 1, -1, false, (byte)0, 0));
                    player.ItemBagUses((short)821, 1);
                    break;
                }
                else if (menuId == 1) {
                    if (player.ItemBagQuantity((short)822) < 1) {
                        Service.openUISay(_char, npcTemplateId, Text.get(0, 329));
                        break;
                    }
                    player.ItemBagAdd(new Item(null, (short)384, 1, -1, false, (byte)0, 0));
                    player.ItemBagUses((short)822, 1);
                    break;
                }
                else if (menuId == 2) {
                    if (player.ItemBagQuantity((short)823) < 1) {
                        Service.openUISay(_char, npcTemplateId, Text.get(0, 329));
                        break;
                    }
                    player.ItemBagAdd(new Item(null, (short)385, 1, -1, false, (byte)0, 0));
                    player.ItemBagUses((short)823, 1);
                    break;
                }
                else if (menuId == 3) {
                    if (player.ItemBagQuantity((short)820) < 1) {
                        Service.openUISay(_char, npcTemplateId, Text.get(0, 329));
                        break;
                    }
                    player.ItemBagAdd(new Item(ItemOption.arrOptionDefault((short)825, (byte)0), (short)825, 1, -1, false, (byte)0, 5));
                    player.ItemBagUses((short)820, 1);
                    break;
                }
                else {
                    if (menuId != 4) {
                        break;
                    }
                    if (player.ItemBagQuantity((short)819) < 1) {
                        Service.openUISay(_char, npcTemplateId, Text.get(0, 329));
                        break;
                    }
                    player.ItemBagAdd(new Item(ItemOption.arrOptionDefault((short)826, (byte)0), (short)826, 1, -1, false, (byte)0, 5));
                    player.ItemBagUses((short)819, 1);
                    break;
                }
                //break;
            }
            default: {
                Service.openUISay(_char, npcTemplateId, Text.get(0, 169));
                break;
            }
        }
    }
    
    static {
        Event_1.timeRefresh = 0L;
        Event_1.timeSale = 60000L;
        Event_1.buy_Count = 0;
        Event_1.isRaxu = false;
        Event_1.indexTrung = -1;
        Event_1.indexDung = 0;
    }
}
