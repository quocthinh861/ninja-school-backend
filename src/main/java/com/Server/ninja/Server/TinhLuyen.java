package com.Server.ninja.Server;

import com.Server.ninja.option.ItemOption;
import com.Server.io.Message;

public class TinhLuyen
{
    private static final short[] ItemTinhThachIds;
    private static final byte[] QuantityTinhThachs;
    private static final byte[] PercentTinhLuyens;
    private static final int[] CoinLockTinhLuyens;
    private static final int[] TBSCxs;
    private static final int[] TBSHps;
    private static final int[] TBSDames;
    private static final int[] TBSNes;
    private static final int[] dames;
    private static final int[] dameSyss;
    private static final int[] Khangs;
    private static final int[] KhangSTCMs;
    private static final int[] GSTs;
    private static final int[] PhanDons;
    private static final int[] damePs;
    private static final int[] Cxs;
    private static final int[] KhangSyss;
    private static final int[] Cms;
    private static final int[] MpHps;
    private static final int[] Nes;
    
    protected static void DichChuyen(final Char _char, final Message msg) {
        try {
            final Item item = _char.user.player.ItemBag(msg.reader().readByte());
            if (item != null) {
                if (item.template.type < 10) {
                    if (item.upgrade >= 12) {
                        if (Item.isTinhLuyen(item)) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 133));
                        }
                        else {
                            try {
                                final Item[] items = new Item[20];
                                for (byte i = 0; i < items.length; ++i) {
                                    final byte indexUI = msg.reader().readByte();
                                    final Item it = _char.user.player.ItemBag[indexUI];
                                    if (it == null || it.template.id != 454 || Item.isIndexUI(indexUI, items)) {
                                        return;
                                    }
                                    items[i] = it;
                                }
                                GameCanvas.addInfoDlg(_char, Text.get(0, 132));
                                for (byte i = 0; i < items.length; ++i) {
                                    final Item it2 = items[i];
                                    if (it2 != null) {
                                        _char.user.player.ItemBagUse((byte)it2.indexUI, 1);
                                    }
                                }
                                item.options.add(new ItemOption(85, 0));
                                switch (item.template.type) {
                                    case 0: {
                                        if (item.sys == 1) {
                                            item.options.add(new ItemOption(96, 5));
                                        }
                                        else if (item.sys == 2) {
                                            item.options.add(new ItemOption(95, 5));
                                        }
                                        else if (item.sys == 3) {
                                            item.options.add(new ItemOption(97, 5));
                                        }
                                        item.options.add(new ItemOption(79, 5));
                                        break;
                                    }
                                    case 1: {
                                        item.options.add(new ItemOption(87, Util.nextInt(250, 400)));
                                        if (item.sys == 1) {
                                            item.options.add(new ItemOption(88, Util.nextInt(350, 600)));
                                            break;
                                        }
                                        if (item.sys == 2) {
                                            item.options.add(new ItemOption(89, Util.nextInt(350, 600)));
                                            break;
                                        }
                                        if (item.sys == 3) {
                                            item.options.add(new ItemOption(90, Util.nextInt(350, 600)));
                                            break;
                                        }
                                        break;
                                    }
                                    case 2: {
                                        item.options.add(new ItemOption(80, Util.nextInt(24, 28)));
                                        item.options.add(new ItemOption(91, Util.nextInt(10, 14)));
                                        break;
                                    }
                                    case 3: {
                                        item.options.add(new ItemOption(81, 5));
                                        item.options.add(new ItemOption(79, 5));
                                        break;
                                    }
                                    case 4: {
                                        item.options.add(new ItemOption(86, Util.nextInt(76, 124)));
                                        item.options.add(new ItemOption(94, Util.nextInt(76, 124)));
                                        break;
                                    }
                                    case 5: {
                                        if (item.sys == 1) {
                                            item.options.add(new ItemOption(96, 5));
                                        }
                                        else if (item.sys == 2) {
                                            item.options.add(new ItemOption(95, 5));
                                        }
                                        else if (item.sys == 3) {
                                            item.options.add(new ItemOption(97, 5));
                                        }
                                        item.options.add(new ItemOption(92, Util.nextInt(9, 11)));
                                        break;
                                    }
                                    case 6: {
                                        item.options.add(new ItemOption(83, Util.nextInt(250, 450)));
                                        item.options.add(new ItemOption(82, Util.nextInt(250, 450)));
                                        break;
                                    }
                                    case 7: {
                                        if (item.sys == 1) {
                                            item.options.add(new ItemOption(96, 5));
                                        }
                                        else if (item.sys == 2) {
                                            item.options.add(new ItemOption(95, 5));
                                        }
                                        else if (item.sys == 3) {
                                            item.options.add(new ItemOption(97, 5));
                                        }
                                        if (item.sys == 1) {
                                            item.options.add(new ItemOption(88, Util.nextInt(350, 600)));
                                            break;
                                        }
                                        if (item.sys == 2) {
                                            item.options.add(new ItemOption(89, Util.nextInt(350, 600)));
                                            break;
                                        }
                                        if (item.sys == 3) {
                                            item.options.add(new ItemOption(90, Util.nextInt(350, 600)));
                                            break;
                                        }
                                        break;
                                    }
                                    case 8: {
                                        item.options.add(new ItemOption(83, Util.nextInt(250, 450)));
                                        item.options.add(new ItemOption(84, Util.nextInt(76, 124)));
                                        break;
                                    }
                                    case 9: {
                                        item.options.add(new ItemOption(84, Util.nextInt(76, 124)));
                                        item.options.add(new ItemOption(82, Util.nextInt(250, 450)));
                                        break;
                                    }
                                }
                                Service.DoiOption(_char, (byte)item.indexUI, item.upgrade);
                            }
                            catch (Exception ex) {}
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected static void Tinhluyen(final Char _char, final Message msg) {
        try {
            final Item item = _char.user.player.ItemBag(msg.reader().readByte());
            if (item != null) {
                final int Tinhluyen = Item.paramId(item, (short)85);
                if (!Item.isTinhLuyen(item)) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 134));
                }
                else if (Tinhluyen >= TinhLuyen.ItemTinhThachIds.length) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 135));
                }
                else {
                    final Item[] items = new Item[TinhLuyen.QuantityTinhThachs[Tinhluyen]];
                    int num = 0;
                    try {
                        while (true) {
                            final byte indexUI = msg.reader().readByte();
                            final Item it = _char.user.player.ItemBag[indexUI];
                            if (it != null && !Item.isIndexUI(indexUI, items) && it.template.id == TinhLuyen.ItemTinhThachIds[Tinhluyen]) {
                                items[num] = it;
                                ++num;
                            }
                        }
                    }
                    catch (Exception e2) {
                        if (TinhLuyen.CoinLockTinhLuyens[Tinhluyen] > _char.user.player.yen) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 138));
                        }
                        else if (num != items.length) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 139));
                        }
                        else {
                            _char.user.player.upCoinLock(-TinhLuyen.CoinLockTinhLuyens[Tinhluyen], (byte)1);
                            final boolean isSuccess = Util.nextInt(1, Util.nextInt(30, 100)) <= TinhLuyen.PercentTinhLuyens[Tinhluyen];
                            if (!isSuccess) {
                                GameCanvas.addInfoDlg(_char, Text.get(0, 136));
                            }
                            else {
                                GameCanvas.addInfoDlg(_char, Text.get(0, 137));
                            }
                            for (byte i = 0; i < items.length; ++i) {
                                final Item it2 = items[i];
                                if (it2 != null) {
                                    _char.user.player.ItemBagUse((byte)it2.indexUI, 1);
                                }
                            }
                            if (isSuccess) {
                                for (short j = 0; j < item.options.size(); ++j) {
                                    final ItemOption option = item.options.get(j);
                                    if (option != null) {
                                        if (option.optionTemplate.id == 85) {
                                            option.param = Tinhluyen + 1;
                                        }
                                        else {
                                            switch (option.optionTemplate.id) {
                                                case 75: {
                                                    final ItemOption itemOption = option;
                                                    itemOption.param += TinhLuyen.TBSCxs[Tinhluyen];
                                                    break;
                                                }
                                                case 76: {
                                                    final ItemOption itemOption2 = option;
                                                    itemOption2.param += TinhLuyen.TBSDames[Tinhluyen];
                                                    break;
                                                }
                                                case 77: {
                                                    final ItemOption itemOption3 = option;
                                                    itemOption3.param += TinhLuyen.TBSHps[Tinhluyen];
                                                    break;
                                                }
                                                case 78: {
                                                    final ItemOption itemOption4 = option;
                                                    itemOption4.param += TinhLuyen.TBSNes[Tinhluyen];
                                                    break;
                                                }
                                                case 79: {
                                                    final ItemOption itemOption5 = option;
                                                    itemOption5.param += TinhLuyen.KhangSTCMs[Tinhluyen];
                                                    break;
                                                }
                                                case 80: {
                                                    final ItemOption itemOption6 = option;
                                                    itemOption6.param += TinhLuyen.GSTs[Tinhluyen];
                                                    break;
                                                }
                                                case 81: {
                                                    final ItemOption itemOption7 = option;
                                                    itemOption7.param += TinhLuyen.Khangs[Tinhluyen];
                                                    break;
                                                }
                                                case 82:
                                                case 83: {
                                                    final ItemOption itemOption8 = option;
                                                    itemOption8.param += TinhLuyen.MpHps[Tinhluyen];
                                                    break;
                                                }
                                                case 84: {
                                                    final ItemOption itemOption9 = option;
                                                    itemOption9.param += TinhLuyen.Nes[Tinhluyen];
                                                    break;
                                                }
                                                case 86: {
                                                    final ItemOption itemOption10 = option;
                                                    itemOption10.param += TinhLuyen.Cxs[Tinhluyen];
                                                    break;
                                                }
                                                case 87: {
                                                    final ItemOption itemOption11 = option;
                                                    itemOption11.param += TinhLuyen.dames[Tinhluyen];
                                                    break;
                                                }
                                                case 88:
                                                case 89:
                                                case 90: {
                                                    final ItemOption itemOption12 = option;
                                                    itemOption12.param += TinhLuyen.dameSyss[Tinhluyen];
                                                    break;
                                                }
                                                case 91: {
                                                    final ItemOption itemOption13 = option;
                                                    itemOption13.param += TinhLuyen.PhanDons[Tinhluyen];
                                                    break;
                                                }
                                                case 92: {
                                                    final ItemOption itemOption14 = option;
                                                    itemOption14.param += TinhLuyen.Cms[Tinhluyen];
                                                    break;
                                                }
                                                case 94: {
                                                    final ItemOption itemOption15 = option;
                                                    itemOption15.param += TinhLuyen.damePs[Tinhluyen];
                                                    break;
                                                }
                                                case 95:
                                                case 96:
                                                case 97: {
                                                    final ItemOption itemOption16 = option;
                                                    itemOption16.param += TinhLuyen.KhangSyss[Tinhluyen];
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                Service.DoiOption(_char, (byte)item.indexUI, item.upgrade);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected static void LuyenTinhThach(final Char _char, final Message msg) {
        try {
            final byte indexUI = _char.user.player.ItemBagIndexNull();
            if (indexUI == -1) {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
            }
            else {
                final Item[] items = new Item[24];
                byte num = 0;
                try {
                    while (true) {
                        final byte index = msg.reader().readByte();
                        final Item item = _char.user.player.ItemBag[index];
                        if (item == null || Item.isIndexUI(index, items) || (item.template.id != 10 && !(item.template.id == 11 | item.template.id == 455) && item.template.id != 456) || (num != 0 && (items[0].isTypeCrystal() || num >= 4 || item.isTypeCrystal() || ((items[0].template.id != 10 || item.template.id != 455) && (items[0].template.id != 11 || item.template.id != 456))) && (items[0].isTypeCrystal() || items[0].template.id != item.template.id || num >= 9))) {
                            break;
                        }
                        final Item[] array = items;
                        final byte b = num;
                        ++num;
                        array[b] = item;
                    }
                }
                catch (Exception e2) {
                    if (num == 4 || num == 9) {
                        GameCanvas.addInfoDlg(_char, Text.get(0, 140));
                        short templateId = 456;
                        if (items[0].template.id == 11 || items[0].template.id == 456) {
                            templateId = 457;
                        }
                        for (byte i = 0; i < items.length; ++i) {
                            final Item item2 = items[i];
                            if (item2 != null) {
                                _char.user.player.ItemBagUse((byte)item2.indexUI, 1);
                            }
                        }
                        _char.user.player.ItemBagAdd(indexUI, new Item(null, templateId, 1, -1, false, (byte)0, 0));
                    }
                    else {
                        GameCanvas.addInfoDlg(_char, Text.get(0, 141));
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        ItemTinhThachIds = new short[] { 455, 455, 455, 456, 456, 456, 457, 457, 457 };
        QuantityTinhThachs = new byte[] { 3, 5, 9, 4, 7, 10, 5, 7, 9 };
        PercentTinhLuyens = new byte[] { 60, 45, 34, 26, 20, 15, 11, 8, 6 };
        CoinLockTinhLuyens = new int[] { 150000, 247500, 408375, 673819, 1111801, 2056832, 4010922, 7420021, 12243035 };
        TBSCxs = new int[] { 25, 30, 35, 40, 50, 60, 80, 115, 165 };
        TBSHps = new int[] { 40, 60, 80, 100, 140, 220, 300, 420, 590 };
        TBSDames = new int[] { 50, 60, 70, 90, 130, 180, 250, 350, 500 };
        TBSNes = new int[] { 25, 30, 35, 40, 50, 60, 80, 115, 165 };
        dames = new int[] { 50, 60, 70, 90, 130, 180, 250, 350, 500 };
        dameSyss = new int[] { 50, 70, 100, 140, 190, 250, 320, 400, 500 };
        Khangs = new int[] { 5, 5, 5, 5, 10, 10, 10, 15, 20 };
        KhangSTCMs = new int[] { 1, 1, 2, 2, 2, 2, 3, 3, 4 };
        GSTs = new int[] { 5, 5, 5, 10, 10, 15, 20, 25, 30 };
        PhanDons = new int[] { 5, 5, 5, 5, 5, 10, 10, 15, 20 };
        damePs = new int[] { 10, 10, 10, 20, 20, 30, 40, 50, 60 };
        Cxs = new int[] { 25, 30, 35, 40, 50, 60, 80, 155, 165 };
        KhangSyss = new int[] { 5, 5, 5, 5, 5, 5, 10, 10, 15 };
        Cms = new int[] { 5, 5, 5, 5, 5, 5, 10, 10, 20 };
        MpHps = new int[] { 40, 60, 80, 100, 140, 220, 300, 420, 590 };
        Nes = new int[] { 25, 30, 35, 40, 50, 60, 80, 115, 165 };
    }
}
