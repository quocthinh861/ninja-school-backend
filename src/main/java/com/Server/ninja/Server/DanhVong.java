 package com.Server.ninja.Server;

import com.Server.ninja.option.ItemOption;

public class DanhVong
{
    protected static final byte TIEU_DIET_TA = 0;
    protected static final byte TIEU_DIET_TL = 1;
    protected static final byte TIEU_DIET_MOB = 2;
    protected static String[] TILE_NV;
    protected static String[] DESC_NV;
    protected static String[] TILE_TYPE;
    protected static final int[] COIN;
    protected static final int[] GOLD;
    protected static final int[] PERCENT;
    protected static final short[] GAN;
    protected static final short[] POINT_DV;
    protected static final short[] DA_DV;
    
    protected static void viewNV(final Char _char) {
        Service.AlertMessage(_char.user.player, Text.get(0, 355), String.format(Text.get(0, 356), DanhVong.TILE_NV[_char.nvDV[1]], GameScr.itemTemplates[_char.nvDV[4]].name, String.format(DanhVong.DESC_NV[_char.nvDV[1]], _char.nvDV[2], _char.nvDV[3])));
    }
    
    protected static void clearNV(final Char _char) {
        _char.nvDV[1] = 0;
        _char.nvDV[2] = 0;
        _char.nvDV[3] = 0;
        _char.nvDV[4] = -1;
    }
    
    protected static void reset(final Char _char) {
        _char.nvDV[0] = 20;
    }
    
    protected static void doNVDV(final Char _char, final short npcTemplateId, final byte optionId) {
        if (!_char.isHuman) {
            Service.ServerMessage(_char, Text.get(0, 310));
        }
        else {
            if (optionId == 0) {
                if (_char.cLevel < 50) {
                    Service.ServerMessage(_char, Text.get(0, 353));
                }
                else if (_char.nvDV[4] != -1) {
                    Service.ServerMessage(_char, Text.get(0, 360));
                }
                else if (_char.nvDV[0] <= 0) {
                    Service.openUISay(_char, npcTemplateId, Text.get(0, 357));
                }
                else {
                    final short[] nvDV2 = _char.nvDV;
                    final int n = 0;
                    --nvDV2[n];
                    _char.nvDV[1] = (short) 2;
                    _char.nvDV[2] = 0;
                    if (_char.nvDV[1] == 0) {
                        _char.nvDV[3] = 1;
                    }
                    if (_char.nvDV[1] == 1) {
                        _char.nvDV[3] = 1;
                    }
                    if (_char.nvDV[1] == 2) {
                        _char.nvDV[3] = (short)Util.nextInt(100, 400);
                    }
                    final byte type = (byte)Util.nextInt(10);
                    int xlevel = _char.cLevel / 10;
                    if (xlevel > 8) {
                        xlevel = 8;
                    }
                    else if (xlevel < 1) {
                        xlevel = 1;
                    }
                    xlevel = (byte)Util.nextInt(1, xlevel);
                    for (short i = 0; i < GameScr.itemTemplates.length; ++i) {
                        if (GameScr.itemTemplates[i].level / 10 == xlevel && GameScr.itemTemplates[i].type == type && (GameScr.itemTemplates[i].gender == 2 || GameScr.itemTemplates[i].gender == _char.cgender) && (!GameScr.itemTemplates[i].isItemClass0() || _char.nClass == 0 || !GameScr.itemTemplates[i].isItemClass1() || _char.nClass == 1) && (!GameScr.itemTemplates[i].isItemClass2() || _char.nClass == 2) && (!GameScr.itemTemplates[i].isItemClass3() || _char.nClass == 3) && (!GameScr.itemTemplates[i].isItemClass4() || _char.nClass == 4) && (!GameScr.itemTemplates[i].isItemClass5() || _char.nClass == 5) && (!GameScr.itemTemplates[i].isItemClass6() || _char.nClass == 6)) {
                            _char.nvDV[4] = GameScr.itemTemplates[i].id;
                            viewNV(_char);
                            break;
                        }
                    }
                }
            }
            if (optionId == 1) {
                if (_char.cLevel < 50) {
                    Service.openUISay(_char, npcTemplateId, Text.get(0, 353));
                }
                else if (_char.nvDV[4] == -1) {
                    Service.ServerMessage(_char, Text.get(0, 361));
                }
                else if (_char.nvDV[2] < _char.nvDV[3]) {
                    Service.ServerMessage(_char, Text.get(0, 360));
                }
                else {
                    String str = "";
                    final short point = (short) 5;
                    addPoint(_char, GameScr.itemTemplates[_char.nvDV[4]].type, point);
                    if (Util.nextInt(100) < 50 && _char.user.player.ItemBagIndexNull() != -1) {
                        final Item it = new Item(null, DanhVong.DA_DV[1], 1, -1, true, (byte)0, 0);
                        _char.user.player.ItemBagAddQuantity(it);
                        str = "\n- " + it.template.name;
                    }
                    Service.AlertMessage(_char.user.player, Text.get(0, 377), String.format(Text.get(0, 378), point, DanhVong.TILE_TYPE[GameScr.itemTemplates[_char.nvDV[4]].type].toLowerCase(), str));
                    clearNV(_char);
                }
            }
            if (optionId == 2) {
                if (_char.cLevel < 50) {
                    Service.openUISay(_char, npcTemplateId, Text.get(0, 353));
                }
                else if (_char.nvDV[4] == -1) {
                    Service.openUISay(_char, npcTemplateId, Text.get(0, 358));
                }
                else {
                    Service.openUIConfirmID(_char, String.format(Text.get(0, 362), DanhVong.TILE_NV[_char.nvDV[1]]), (byte)(-128));
                }
            }
            if (optionId == 3) {
                final byte upgrade = 1;
                final short pointMax = DanhVong.POINT_DV[upgrade];
                if (_char.cLevel < 50) {
                    Service.openUISay(_char, npcTemplateId, Text.get(0, 353));
                }
                else if (_char.pointNon < pointMax || _char.pointVukhi < pointMax || _char.pointAo < pointMax || _char.pointLien < pointMax || _char.pointGangtay < pointMax || _char.pointNhan < pointMax || _char.pointQuan < pointMax || _char.pointNgocboi < pointMax || _char.pointGiay < pointMax || _char.pointPhu < pointMax) {
                    for (byte j = 0; j < 10; ++j) {
                        if (getPoint(_char, j) < pointMax) {
                            Service.ServerMessage(_char, String.format(Text.get(0, 354), pointMax, DanhVong.TILE_TYPE[j]));
                            return;
                        }
                    }
                }
                else if (_char.user.player.ItemBagSlotNull() < 1) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
                else {
                    final Item item = new Item(ItemOption.arrOptionDefault((short)685, (byte)0), (short)685, 1, -1, true, (byte)0, 5);
                    item.upgrade = upgrade;
                    _char.user.player.ItemBagAdd(item);
                }
            }
            if (optionId == 4 || optionId == 5) {
                final Item item2 = _char.ItemBody[14];
                if (_char.cLevel < 50) {
                    Service.openUISay(_char, npcTemplateId, Text.get(0, 353));
                }
                else if (item2 == null) {
                    Service.ServerMessage(_char, Text.get(0, 368));
                }
                else if (_char.ItemBody[14].upgrade >= 10) {
                    Service.ServerMessage(_char, Text.get(0, 369));
                }
                else {
                    final int coin = DanhVong.COIN[item2.upgrade];
                    int gold = 0;
                    int percent = DanhVong.PERCENT[item2.upgrade];
                    if (optionId == 5) {
                        gold = DanhVong.GOLD[item2.upgrade];
                        percent *= 2;
                        Service.openUIConfirmID(_char, String.format(Text.get(0, 370), item2.template.name, coin, gold, percent, "%"), (byte)(-126));
                    }
                    else {
                        Service.openUIConfirmID(_char, String.format(Text.get(0, 376), item2.template.name, coin, percent, "%"), (byte)(-127));
                    }
                }
            }
            if (optionId == 6) {
                if (_char.cLevel < 50) {
                    Service.openUISay(_char, npcTemplateId, Text.get(0, 353));
                }
                else {
                    String nvDV = "";
                    if (_char.nvDV[4] != -1) {
                        nvDV = String.format(Text.get(0, 356), DanhVong.TILE_NV[_char.nvDV[1]], GameScr.itemTemplates[_char.nvDV[4]].name, String.format(DanhVong.DESC_NV[_char.nvDV[1]], _char.nvDV[2], _char.nvDV[3])) + "\n";
                    }
                    Service.AlertMessage(_char.user.player, Text.get(0, 363), String.format(Text.get(0, 365), nvDV, _char.nvDV[0], Limit.getMaxItemLimit((short)705) - ((Player.ItemUseLimit(_char, (short)705) != -1) ? Player.ItemUseLimit(_char, (short)705) : 0)));
                }
            }
        }
    }
    
    protected static void addPoint(final Char _char, final byte type, final short point) {
        switch (type) {
            case 0: {
                _char.pointNon += point;
                if (_char.pointNon > 1000) {
                    _char.pointNon = 1000;
                    break;
                }
                break;
            }
            case 1: {
                _char.pointVukhi += point;
                if (_char.pointVukhi > 1000) {
                    _char.pointVukhi = 1000;
                    break;
                }
                break;
            }
            case 2: {
                _char.pointAo += point;
                if (_char.pointAo > 1000) {
                    _char.pointAo = 1000;
                    break;
                }
                break;
            }
            case 3: {
                _char.pointLien += point;
                if (_char.pointLien > 1000) {
                    _char.pointLien = 1000;
                    break;
                }
                break;
            }
            case 4: {
                _char.pointGangtay += point;
                if (_char.pointGangtay > 1000) {
                    _char.pointGangtay = 1000;
                    break;
                }
                break;
            }
            case 5: {
                _char.pointNhan += point;
                if (_char.pointNhan > 1000) {
                    _char.pointNhan = 1000;
                    break;
                }
                break;
            }
            case 6: {
                _char.pointQuan += point;
                if (_char.pointQuan > 1000) {
                    _char.pointQuan = 1000;
                    break;
                }
                break;
            }
            case 7: {
                _char.pointNgocboi += point;
                if (_char.pointNgocboi > 1000) {
                    _char.pointNgocboi = 1000;
                    break;
                }
                break;
            }
            case 8: {
                _char.pointGiay += point;
                if (_char.pointGiay > 1000) {
                    _char.pointGiay = 1000;
                    break;
                }
                break;
            }
            case 9: {
                _char.pointPhu += point;
                if (_char.pointPhu > 1000) {
                    _char.pointPhu = 1000;
                    break;
                }
                break;
            }
        }
    }
    
    protected static short getPoint(final Char _char, final byte type) {
        switch (type) {
            case 0: {
                return _char.pointNon;
            }
            case 1: {
                return _char.pointVukhi;
            }
            case 2: {
                return _char.pointAo;
            }
            case 3: {
                return _char.pointLien;
            }
            case 4: {
                return _char.pointGangtay;
            }
            case 5: {
                return _char.pointNhan;
            }
            case 6: {
                return _char.pointQuan;
            }
            case 7: {
                return _char.pointNgocboi;
            }
            case 8: {
                return _char.pointGiay;
            }
            case 9: {
                return _char.pointPhu;
            }
            default: {
                return 0;
            }
        }
    }
    
    static {
        DanhVong.TILE_NV = new String[] { "Ti\u00eau di\u1ec7t tinh anh", "Ti\u00eau di\u1ec7t th\u1ee7 l\u0129nh", "Ti\u00eau di\u1ec7t qu\u00e1i" };
        DanhVong.DESC_NV = new String[] { "- Ti\u00eau di\u1ec7t %d/%d qu\u00e1i tinh anh l\u1ec7ch 10 c\u1ea5p \u0111\u1ed9.", "- Ti\u00eau di\u1ec7t %d/%d qu\u00e1i th\u1ee7 l\u0129nh l\u1ec7ch 10 c\u1ea5p \u0111\u1ed9.", "- Ti\u00eau di\u1ec7t %d/%d qu\u00e1i l\u1ec7ch 10 c\u1ea5p \u0111\u1ed9." };
        DanhVong.TILE_TYPE = new String[] { "Danh v\u1ecdng n\u00f3n", "Danh v\u1ecdng v\u0169 kh\u00ed", "Danh v\u1ecdng \u00e1o", "Danh v\u1ecdng d\u00e2y chuy\u1ec1n", "Danh v\u1ecdng g\u0103ng tay", "Danh v\u1ecdng nh\u1eabn", "Danh v\u1ecdng qu\u1ea7n", "Danh v\u1ecdng ng\u1ecdc b\u1ed9i", "Danh v\u1ecdng gi\u00e0y", "Danh v\u1ecdng b\u00f9a" };
        COIN = new int[] { 0, 500000, 750000, 1250000, 2000000, 4000000, 6000000, 8000000, 12000000, 15000000, 20000000 };
        GOLD = new int[] { 0, 50, 60, 85, 100, 120, 150, 200, 250, 300, 400 };
        PERCENT = new int[] { 100, 50, 35, 25, 20, 15, 10, 8, 5, 3 };
        GAN = new short[] { -1, 685, 686, 687, 688, 689, 690, 691, 692, 693, 694 };
        POINT_DV = new short[] { 0, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000 };
        DA_DV = new short[] { -1, 695, 696, 697, 698, 699, 700, 701, 702, 703, 704 };
    }
}
