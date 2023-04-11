 package com.Server.ninja.Server;

import com.Server.ninja.option.ItemOption;
import com.Server.io.Message;

public class NgocKham
{
    private static final int maxUpgrade = 10;
    private static final int[][] arrNgocKhamEXP;
    private static final int[] coinLockKham;
    private static final int[] htn1;
    private static final int[] htn2;
    private static final int[] htn3;
    private static final int[] htn4;
    private static final int[] htn5;
    private static final int[] htn6;
    private static final int[] hn1;
    private static final int[] hn2;
    private static final int[] hn3;
    private static final int[] hn4;
    private static final int[] hn5;
    private static final int[] hn6;
    private static final int[] ltn1;
    private static final int[] ltn2;
    private static final int[] ltn3;
    private static final int[] ltn4;
    private static final int[] ltn5;
    private static final int[] ltn6;
    private static final int[] ln1;
    private static final int[] ln2;
    private static final int[] ln3;
    private static final int[] ln4;
    private static final int[] ln5;
    private static final int[] ln6;
    
    protected static short getItemTemplateIdOption(final short optionTemplateId) {
        switch (optionTemplateId) {
            case 109: {
                return 652;
            }
            case 110: {
                return 653;
            }
            case 111: {
                return 654;
            }
            case 112: {
                return 655;
            }
            default: {
                return -1;
            }
        }
    }
    
    protected static short getOptionIdItem(final short itemTemplateId) {
        switch (itemTemplateId) {
            case 652: {
                return 109;
            }
            case 653: {
                return 110;
            }
            case 654: {
                return 111;
            }
            case 655: {
                return 112;
            }
            default: {
                return -1;
            }
        }
    }
    
    protected static void LuyenNgocKham(final Char _char, final Message msg) {
        try {
            final Item ngoc = _char.user.player.ItemBag[msg.reader().readByte()];
            if (ngoc != null && ngoc.isTypeNgocKham()) {
                final Item[] items = new Item[24];
                int num = 0;
                int ngockham_exp = 0;
                try {
                    for (byte i = 0; i < items.length; ++i) {
                        final Item it = _char.user.player.ItemBag[msg.reader().readByte()];
                        if (it == null || !it.isTypeNgocKham() || Item.isIndexUI(it.indexUI, items) || it.indexUI == ngoc.indexUI) {
                            return;
                        }
                        if (ngoc.upgrade < it.upgrade) {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 334));
                            return;
                        }
                        items[i] = it;
                        ngockham_exp += NgocKham.arrNgocKhamEXP[it.upgrade][1];
                        ++num;
                    }
                }
                catch (Exception ex) {}
                if (num > 0) {
                    ngoc.isLock = true;
                    final byte upgradeold = ngoc.upgrade;
                    byte upgrade = ngoc.upgrade;
                    for (byte j = 0; j < items.length; ++j) {
                        final Item it2 = items[j];
                        if (it2 != null) {
                            _char.user.player.ItemBag[it2.indexUI] = null;
                        }
                    }
                    for (short k = 0; k < ngoc.options.size(); ++k) {
                        final ItemOption option = ngoc.options.get(k);
                        if (option != null && option.optionTemplate.id == 104) {
                            final ItemOption itemOption = option;
                            itemOption.param += ngockham_exp;
                            while (option.param > NgocKham.arrNgocKhamEXP[upgrade][0]) {
                                final ItemOption itemOption2 = option;
                                itemOption2.param -= NgocKham.arrNgocKhamEXP[upgrade][0];
                                ++upgrade;
                                if (upgrade > 10) {
                                    upgrade = 10;
                                }
                                else {
                                    if (upgrade <= ngoc.upgrade) {
                                        continue;
                                    }
                                    ngoc.upgrade = upgrade;
                                    try {
                                        if (ngoc.template.id == 652) {
                                            final ItemOption itemOption3 = ngoc.options.get(1);
                                            itemOption3.param += NgocKham.htn1[ngoc.upgrade];
                                            final ItemOption itemOption4 = ngoc.options.get(2);
                                            itemOption4.param += NgocKham.htn2[ngoc.upgrade];
                                            final ItemOption itemOption5 = ngoc.options.get(4);
                                            itemOption5.param += NgocKham.htn3[ngoc.upgrade];
                                            final ItemOption itemOption6 = ngoc.options.get(5);
                                            itemOption6.param += NgocKham.htn4[ngoc.upgrade];
                                            final ItemOption itemOption7 = ngoc.options.get(7);
                                            itemOption7.param += NgocKham.htn5[ngoc.upgrade];
                                            final ItemOption itemOption8 = ngoc.options.get(8);
                                            itemOption8.param += NgocKham.htn6[ngoc.upgrade];
                                        }
                                        if (ngoc.template.id == 653) {
                                            final ItemOption itemOption9 = ngoc.options.get(1);
                                            itemOption9.param += NgocKham.hn1[ngoc.upgrade];
                                            final ItemOption itemOption10 = ngoc.options.get(2);
                                            itemOption10.param += NgocKham.hn2[ngoc.upgrade];
                                            final ItemOption itemOption11 = ngoc.options.get(4);
                                            itemOption11.param += NgocKham.hn3[ngoc.upgrade];
                                            final ItemOption itemOption12 = ngoc.options.get(5);
                                            itemOption12.param += NgocKham.hn4[ngoc.upgrade];
                                            final ItemOption itemOption13 = ngoc.options.get(7);
                                            itemOption13.param += NgocKham.hn5[ngoc.upgrade];
                                            final ItemOption itemOption14 = ngoc.options.get(8);
                                            itemOption14.param += NgocKham.hn6[ngoc.upgrade];
                                        }
                                        if (ngoc.template.id == 654) {
                                            final ItemOption itemOption15 = ngoc.options.get(1);
                                            itemOption15.param += NgocKham.ltn1[ngoc.upgrade];
                                            final ItemOption itemOption16 = ngoc.options.get(2);
                                            itemOption16.param += NgocKham.ltn2[ngoc.upgrade];
                                            final ItemOption itemOption17 = ngoc.options.get(4);
                                            itemOption17.param += NgocKham.ltn3[ngoc.upgrade];
                                            final ItemOption itemOption18 = ngoc.options.get(5);
                                            itemOption18.param += NgocKham.ltn4[ngoc.upgrade];
                                            final ItemOption itemOption19 = ngoc.options.get(7);
                                            itemOption19.param += NgocKham.ltn5[ngoc.upgrade];
                                            final ItemOption itemOption20 = ngoc.options.get(8);
                                            itemOption20.param += NgocKham.ltn6[ngoc.upgrade];
                                        }
                                        if (ngoc.template.id != 655) {
                                            continue;
                                        }
                                        final ItemOption itemOption21 = ngoc.options.get(1);
                                        itemOption21.param += NgocKham.ln1[ngoc.upgrade];
                                        final ItemOption itemOption22 = ngoc.options.get(2);
                                        itemOption22.param += NgocKham.ln2[ngoc.upgrade];
                                        final ItemOption itemOption23 = ngoc.options.get(4);
                                        itemOption23.param += NgocKham.ln3[ngoc.upgrade];
                                        final ItemOption itemOption24 = ngoc.options.get(5);
                                        itemOption24.param += NgocKham.ln4[ngoc.upgrade];
                                        final ItemOption itemOption25 = ngoc.options.get(7);
                                        itemOption25.param += NgocKham.ln5[ngoc.upgrade];
                                        final ItemOption itemOption26 = ngoc.options.get(8);
                                        itemOption26.param += NgocKham.ln6[ngoc.upgrade];
                                    }
                                    catch (Exception e2) {}
                                }
                            }
                            break;
                        }
                    }
                    for (short k = 0; k < ngoc.options.size(); ++k) {
                        final ItemOption option = ngoc.options.get(k);
                        if (option != null && option.optionTemplate.id == 123) {
                            option.param = NgocKham.coinLockKham[ngoc.upgrade];
                            break;
                        }
                    }
                    Service.KhamNgoc(_char, (byte)1, ngoc.upgrade);
                    Service.ItemInfo(_char, ngoc, (byte)3, ngoc.indexUI);
                    if (upgradeold < ngoc.upgrade) {
                        ngoc.upgrade = upgrade;
                        GameCanvas.startOKDlg(_char.user.session, String.format(Text.get(0, 254), upgrade));
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected static void KhamNgoc(final Char _char, final Message msg) {
        try {
            final Item item = _char.user.player.ItemBag[msg.reader().readByte()];
            final Item ngoc = _char.user.player.ItemBag[msg.reader().readByte()];
            if (item != null && ngoc != null && item.template.type < 10 && ngoc.isTypeNgocKham() && item.upgrade >= 8) {
                int giaKham = 0;
                for (short i = 0; i < ngoc.options.size(); ++i) {
                    final ItemOption option = ngoc.options.get(i);
                    if (option != null && option.optionTemplate.id == 123) {
                        giaKham += option.param;
                    }
                }
                boolean isCungLoai = false;
                for (short j = 0; j < item.options.size(); ++j) {
                    final ItemOption option2 = item.options.get(j);
                    if (option2 != null && option2.optionTemplate.id == getOptionIdItem(ngoc.template.id)) {
                        isCungLoai = true;
                    }
                }
                if (isCungLoai) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 251));
                }
                else if (giaKham > _char.user.player.yen) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 26));
                }
                else {
                    final Item[] items = new Item[18];
                    int crys = 0;
                    try {
                        for (byte k = 0; k < items.length; ++k) {
                            final Item it = _char.user.player.ItemBag[msg.reader().readByte()];
                            if (it == null || Item.isIndexUI(it.indexUI, items) || !it.isTypeCrystal()) {
                                return;
                            }
                            crys += GameScr.crystals[it.template.id];
                            items[k] = it;
                        }
                    }
                    catch (Exception ex) {}
                    int percen = crys * 100 / GameScr.upWeapon[ngoc.upgrade];
                    if (percen > GameScr.maxPercents[ngoc.upgrade]) {
                        percen = GameScr.maxPercents[ngoc.upgrade];
                    }
                    item.isLock = true;
                    _char.user.player.upCoinLock(-giaKham, (byte)1);
                    for (byte l = 0; l < items.length; ++l) {
                        final Item it2 = items[l];
                        if (it2 != null) {
                            _char.user.player.ItemBag[it2.indexUI] = null;
                        }
                    }
                    final boolean success = Util.nextInt(1, Util.nextInt(50, 100)) <= percen;
                    if (success) {
                        _char.user.player.ItemBag[ngoc.indexUI] = null;
                        boolean isCreate = true;
                        for (short m = 0; m < item.options.size(); ++m) {
                            final ItemOption option3 = item.options.get(m);
                            if (option3 != null && option3.optionTemplate.id == 122) {
                                final ItemOption itemOption = option3;
                                itemOption.param += giaKham;
                                isCreate = false;
                                break;
                            }
                        }
                        if (isCreate) {
                            final ItemOption ngoc_gia = new ItemOption(122, giaKham);
                            item.options.add(ngoc_gia);
                        }
                        final ItemOption ngoc_option = new ItemOption(getOptionIdItem(ngoc.template.id), ngoc.upgrade);
                        item.options.add(ngoc_option);
                        for (short i2 = 0; i2 < ngoc.options.size(); ++i2) {
                            final ItemOption option4 = ngoc.options.get(i2);
                            if (option4 != null) {
                                item.options.add(option4);
                            }
                        }
                    }
                    Service.upGrade(_char, (byte)(success ? 5 : 6), item.upgrade);
                    Service.KhamNgoc(_char, (byte)0, (byte)(-1));
                }
            }
            else {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 252));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected static void GotNgoc(final Char _char, final byte indexUI) {
        try {
            final Item item = _char.user.player.ItemBag(indexUI);
            if (item != null && item.isTypeNgocKham()) {
                final int coinGot = GameScr.coinGotngoc[item.upgrade];
                if (coinGot > _char.user.player.xu) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 27));
                }
                else {
                    _char.user.player.upCoin(-coinGot, (byte)0);
                    for (short i = 0; i < item.options.size(); ++i) {
                        final ItemOption option = item.options.get(i);
                        if (option != null && option.optionTemplate.id >= 106 && option.optionTemplate.id <= 108) {
                            final ItemOption option_down = item.options.get(i + 2);
                            if (option_down != null && option_down.param < -1) {
                                final ItemOption itemOption = option_down;
                                itemOption.param += Util.nextInt(15);
                                if (option_down.param > -1) {
                                    option_down.param = -1;
                                }
                            }
                            i += 2;
                        }
                    }
                    Service.KhamNgoc(_char, (byte)2, (byte)(-1));
                    Service.ItemInfo(_char, item, (byte)3, item.indexUI);
                    GameCanvas.startOKDlg(_char.user.session, String.format(Text.get(0, 253), item.template.name));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected static void Split(final Char _char, final byte indexUI) {
        try {
            final Item item = _char.user.player.ItemBag(indexUI);
            if (item != null) {
                int num = 0;
                int giaThao = 0;
                for (short i = 0; i < item.options.size(); ++i) {
                    final ItemOption option = item.options.get(i);
                    if (option.optionTemplate.id >= 109 && option.optionTemplate.id <= 112) {
                        ++num;
                    }
                }
                if (num > _char.user.player.ItemBagSlotNull()) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
                else {
                    for (short i = 0; i < item.options.size(); ++i) {
                        final ItemOption option = item.options.get(i);
                        if (option != null && option.optionTemplate.id == 122) {
                            giaThao += option.param;
                        }
                    }
                    if (giaThao > _char.user.player.yen) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 26));
                    }
                    else {
                        _char.user.player.upCoinLock(-giaThao, (byte)1);
                        for (short i = 0; i < item.options.size(); ++i) {
                            final ItemOption option = item.options.get(i);
                            if (option.optionTemplate.id >= 109 && option.optionTemplate.id <= 112) {
                                final short templateId = getItemTemplateIdOption((short)option.optionTemplate.id);
                                final Item it = new Item(null, templateId, 1, -1, true, (byte)0, 5);
                                it.upgrade = (byte)option.param;
                                int lenth = i + 11;
                                for (short j = (short)(i + 1); j <= lenth; --j, --lenth, ++j) {
                                    it.options.add(item.options.get(j));
                                    item.options.remove(j);
                                }
                                _char.user.player.ItemBagAdd(it);
                                item.options.remove(i);
                                --i;
                            }
                            else if (option.optionTemplate.id == 122) {
                                item.options.remove(i);
                                --i;
                            }
                        }
                        Service.KhamNgoc(_char, (byte)3, (byte)(-1));
                        Service.ItemInfo(_char, item, (byte)3, item.indexUI);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        arrNgocKhamEXP = new int[][] { new int[2], { 200, 10 }, { 500, 20 }, { 1000, 50 }, { 2000, 100 }, { 5000, 200 }, { 10000, 500 }, { 20000, 1000 }, { 50000, 2000 }, { 100000, 5000 }, { 100000, 10000 } };
        coinLockKham = new int[] { 0, 800000, 1600000, 2400000, 3200000, 4800000, 7200000, 10800000, 15600000, 22400000, 28100000 };
        htn1 = new int[] { 0, 0, 100, 200, 400, 600, 800, 1000, 1400, 1600, 1800 };
        htn2 = new int[] { 0, 0, -50, -50, -50, -50, -50, -50, -50, -50, -50 };
        htn3 = new int[] { 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        htn4 = new int[] { 0, 0, -100, -100, -100, -100, -100, -100, -100, -100, -100 };
        htn5 = new int[] { 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        htn6 = new int[] { 0, 0, -20, -20, -20, -20, -20, -20, -20, -20, -20 };
        hn1 = new int[] { 0, 0, 50, 100, 150, 200, 250, 300, 350, 400, 450 };
        hn2 = new int[] { 0, 0, -5, -5, -5, -5, -5, -5, -5, -5, -5 };
        hn3 = new int[] { 0, 0, 10, 15, 20, 25, 30, 35, 40, 45, 50 };
        hn4 = new int[] { 0, 0, -50, -50, -50, -50, -50, -50, -50, -50, -50 };
        hn5 = new int[] { 0, 0, 10, 20, 20, 40, 50, 60, 70, 80, 90 };
        hn6 = new int[] { 0, 0, -5, -5, -5, -5, -5, -5, -5, -5, -5 };
        ltn1 = new int[] { 0, 0, 100, 200, 300, 400, 500, 600, 800, 900, 1000 };
        ltn2 = new int[] { 0, 0, -100, -100, -100, -100, -100, -100, -100, -100, -100 };
        ltn3 = new int[] { 0, 0, 1, 2, 3, 4, 5, 6, 7, 7, 7 };
        ltn4 = new int[] { 0, 0, -5, -5, -5, -5, -5, -5, -5, -5, -5 };
        ltn5 = new int[] { 0, 0, 10, 20, 20, 40, 50, 60, 70, 80, 90 };
        ltn6 = new int[] { 0, 0, -5, -5, -5, -5, -5, -5, -5, -5, -5 };
        ln1 = new int[] { 0, 0, 100, 200, 300, 400, 500, 600, 800, 900, 1000 };
        ln2 = new int[] { 0, 0, -50, -50, -50, -50, -50, -50, -50, -50, -50 };
        ln3 = new int[] { 0, 0, 20, 30, 40, 60, 80, 110, 150, 190, 230 };
        ln4 = new int[] { 0, 0, -100, -100, -100, -100, -100, -100, -100, -100, -100 };
        ln5 = new int[] { 0, 0, 20, 30, 40, 60, 80, 110, 150, 190, 230 };
        ln6 = new int[] { 0, 0, -5, -5, -5, -5, -5, -5, -5, -5, -5 };
    }
}
