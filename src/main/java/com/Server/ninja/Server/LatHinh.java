 package com.Server.ninja.Server;

import com.Server.ninja.template.ItemTemplate;
import com.Server.ninja.option.ItemOption;

public class LatHinh
{
    private static final int[] YenCards;
    private static final byte[] SoiDays;
    private static final byte[] PetDays;
    private static final byte[] MatNaDays;
    private static final short[] arrTemplateId1;
    private static final short[] arrTemplateId2;
    private static final short[] arrTemplateId3;
    private static final short[] arrTemplateId4;
    private static final short[] arrTemplateId5;
    private static final short[] arrTemplateId6;
    private static final short[] arrTemplateId7;
    private static final short[] arrTemplateId8;
    
    protected static void selectCard(final Char _char, byte index) {
        if (index < 0 && index > 8) {
            index = 0;
        }
        if (_char.user.player.ItemBag((short)340) == null) {
            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 68));
        }
        else if (_char.user.player.ItemBagIndexNull() == -1) {
            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
        }
        else {
            _char.user.player.ItemBagUses((short)340, 1);
            if (_char.ctaskId == 40 && _char.ctaskIndex == 3) {
                _char.uptaskMaint();
            }
            final short[] arrTemplateId = new short[9];
            short templateId = 12;
            final byte type = 0;
            for (byte i = 0; i < 9; ++i) {
                arrTemplateId[i] = 12;
                if (Util.nextInt(100) < 50) {
                    arrTemplateId[i] = LatHinh.arrTemplateId1[Util.nextInt(LatHinh.arrTemplateId1.length)];
                }
                else if (Util.nextInt(1000) < 10) {
                    arrTemplateId[i] = LatHinh.arrTemplateId2[Util.nextInt(LatHinh.arrTemplateId2.length)];
                }
                else if (Util.nextInt(100) < 25) {
                    arrTemplateId[i] = LatHinh.arrTemplateId3[Util.nextInt(LatHinh.arrTemplateId3.length)];
                }
                else if (Util.nextInt(100) < 20) {
                    arrTemplateId[i] = LatHinh.arrTemplateId4[Util.nextInt(LatHinh.arrTemplateId4.length)];
                }
                else if (Util.nextInt(100) < 30) {
                    arrTemplateId[i] = LatHinh.arrTemplateId5[Util.nextInt(LatHinh.arrTemplateId5.length)];
                }
                else if (Util.nextInt(800) < 50) {
                    arrTemplateId[i] = LatHinh.arrTemplateId6[Util.nextInt(LatHinh.arrTemplateId6.length)];
                }
                else if (Util.nextInt(100) < 15) {
                    arrTemplateId[i] = LatHinh.arrTemplateId7[Util.nextInt(LatHinh.arrTemplateId7.length)];
                }
                else if (Util.nextInt(100) < 10) {
                    arrTemplateId[i] = LatHinh.arrTemplateId8[Util.nextInt(LatHinh.arrTemplateId8.length)];
                }
                if (i == index) {
                    templateId = arrTemplateId[i];
                }
            }
            Service.selectCard(_char, arrTemplateId);
            final ItemTemplate template = GameScr.itemTemplates[templateId];
            if (template.type == 1) {
                final Item[] arrItem = GameScr.itemStores.get((byte)2);
                for (byte j = 0; j < arrItem.length; ++j) {
                    final Item itemStore = arrItem[j];
                    if (itemStore != null && itemStore.template.id == template.id) {
                        final Item item = itemStore.clone();
                        item.quantity = 1;
                        _char.user.player.ItemBagAdd(item);
                        return;
                    }
                }
            }
            else if (template.type < 10) {
                for (byte n = 16; n <= 29; ++n) {
                    final Item[] arrItem2 = GameScr.itemStores.get(n);
                    final byte sys = (byte)Util.nextInt(1, 3);
                    for (byte k = 0; k < arrItem2.length; ++k) {
                        final Item itemStore2 = arrItem2[k];
                        if (itemStore2 != null && itemStore2.template.id == template.id && itemStore2.sys == sys) {
                            final Item item2 = itemStore2.clone();
                            item2.quantity = 1;
                            _char.user.player.ItemBagAdd(item2);
                            return;
                        }
                    }
                }
            }
            else if (template.type == 19) {
                int quantity = LatHinh.YenCards[0];
                for (byte j = 1; j < LatHinh.YenCards.length; ++j) {
                    if (Util.nextInt(100) < 70) {
                        quantity = LatHinh.YenCards[j];
                        break;
                    }
                }
                _char.user.player.upCoinLock(quantity, (byte)2);
                if (quantity >= 500000) {
                    Client.alertServer(String.format(Text.get(0, 75), _char.cName, quantity, template.name));
                }
            }
            else {
                Item item3 = null;
                switch (templateId) {
                    case 12: {
                        break;
                    }
                    case 403:
                    case 404: {
                        item3 = new Item(ItemOption.arrOptionDefault(templateId, (byte)0), templateId, 1, 86400 * LatHinh.MatNaDays[Util.nextInt(LatHinh.MatNaDays.length)], false, (byte)0, 5);
                        break;
                    }
                    case 407:
                    case 408: {
                        item3 = new Item(ItemOption.arrOptionDefault(templateId, (byte)0), templateId, 1, 86400 * LatHinh.MatNaDays[Util.nextInt(LatHinh.MatNaDays.length)], false, (byte)0, 5);
                        break;
                    }
                    case 419: {
                        item3 = new Item(ItemOption.arrOptionDefault(templateId, (byte)0), templateId, 1, 86400 * LatHinh.PetDays[Util.nextInt(LatHinh.PetDays.length)], false, (byte)0, 5);
                        break;
                    }
                    case 523: {
                        item3 = new Item(ItemOption.arrOptionDefault(templateId, (byte)0), templateId, 1, 86400 * LatHinh.SoiDays[Util.nextInt(LatHinh.SoiDays.length)], false, (byte)0, 5);
                        break;
                    }
                    default: {
                        item3 = new Item(null, templateId, 1, -1, false, (byte)0, 0);
                        break;
                    }
                }
                if (item3 != null) {
                    if (item3.template.isUpToUp) {
                        _char.user.player.ItemBagAddQuantity(item3);
                    }
                    else {
                        _char.user.player.ItemBagAdd(item3);
                    }
                }
            }
        }
    }
    
    static {
        YenCards = new int[] { 10000, 20000, 30000, 50000, 100000, 200000, 500000, 1000000, 5000000 };
        SoiDays = new byte[] { 3, 7, 15 };
        PetDays = new byte[] { 7, 15 };
        MatNaDays = new byte[] { 7 };
        arrTemplateId1 = new short[] { 4, 5, 6, 242, 254, 255 };
        arrTemplateId2 = new short[] { 8, 9, 10, 284, 491, 540 };
        arrTemplateId3 = new short[] { 7, 30, 256, 257, 419 };
        arrTemplateId4 = new short[] { 249, 250, 283, 409, 410 };
        arrTemplateId5 = new short[] { 311, 312, 313, 314, 315, 316, 375, 376, 377, 378, 379, 380 };
        arrTemplateId6 = new short[] { 403, 404, 407, 408, 523, 539 };
        arrTemplateId7 = new short[] { 130, 131, 132, 133, 140, 141, 142, 143, 150, 151, 152, 153, 160, 161, 162, 163, 170, 171, 172, 173, 177, 178, 182, 183, 187, 188, 192, 193, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 355, 356, 357, 358, 359, 360, 361, 362, 363, 364, 365, 366, 367, 368 };
        arrTemplateId8 = new short[] { 97, 98, 102, 103, 107, 108, 112, 113, 117, 118, 122, 123, 331, 332, 333, 334, 334, 335, 336, 369, 370, 371, 372, 373, 374 };
    }
}
