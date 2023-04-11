 package com.Server.ninja.Server;

import org.json.simple.parser.ParseException;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import com.Server.ninja.template.ItemTemplate;
import com.Server.ninja.option.ItemOption;
import java.util.ArrayList;

public class Item
{
    protected ArrayList<ItemOption> options;
    protected ItemTemplate template;
    protected short itemId;
    protected int playerId;
    protected int indexUI;
    protected int quantity;
    protected long expires;
    protected boolean isLock;
    protected byte sys;
    protected byte upgrade;
    protected int buyCoin;
    protected int buyCoinLock;
    protected int buyGold;
    protected int buyGoldLock;
    protected int saleCoinLock;
    protected int typeUI;
    protected boolean isExpires;
    
    protected Item() {
    }
    
    protected Item(final ItemOption[] options, final short templateid, final int quantity, final int expires, final boolean isLock, final byte sys, final int saleCoinLock) {
        final ItemTemplate[] itemTemplates = GameScr.itemTemplates;
        this.itemId = templateid;
        this.template = itemTemplates[templateid];
        this.quantity = quantity;
        if (expires != -1) {
            this.expires = System.currentTimeMillis() + expires * 1000L;
            this.isExpires = true;
        }
        else {
            this.expires = -1L;
            this.isExpires = false;
        }
        this.isLock = isLock;
        this.sys = sys;
        this.saleCoinLock = saleCoinLock;
        this.options = new ArrayList<>();
        if (options != null) {
            for (short i = 0; i < options.length; ++i) {
                final ItemOption option = options[i];
                if (option != null && (option.param > 0 || option.optionTemplate.type == 8 || this.template.type >= 10)) {
                    this.options.add(options[i]);
                }
            }
        }
    }
    
    @Override
    protected Item clone() {
        final Item item = new Item();
        item.template = this.template;
        if (this.options != null) {
            item.options = new ArrayList<>();
            for (short i = 0; i < this.options.size(); ++i) {
                final ItemOption itemOption = new ItemOption();
                itemOption.optionTemplate = this.options.get(i).optionTemplate;
                itemOption.param = this.options.get(i).param;
                item.options.add(itemOption);
            }
        }
        item.itemId = this.itemId;
        item.playerId = this.playerId;
        item.indexUI = this.indexUI;
        item.quantity = this.quantity;
        item.expires = this.expires;
        item.isLock = this.isLock;
        item.sys = this.sys;
        item.upgrade = this.upgrade;
        item.buyCoin = this.buyCoin;
        item.buyCoinLock = this.buyCoinLock;
        item.buyGold = this.buyGold;
        item.buyGoldLock = this.buyGoldLock;
        item.saleCoinLock = this.saleCoinLock;
        item.typeUI = this.typeUI;
        item.isExpires = this.isExpires;
        return item;
    }
    
    protected void upgradeNext(final byte next) {
        this.upgrade += next;
        if (this.options != null) {
            for (short i = 0; i < this.options.size(); ++i) {
                final ItemOption itemOption = this.options.get(i);
                switch (itemOption.optionTemplate.id) {
                    case 6, 7 -> {
                        final ItemOption itemOption2 = itemOption;
                        itemOption2.param += 15 * next;
                    }
                    case 8, 9, 19 -> {
                        final ItemOption itemOption3 = itemOption;
                        itemOption3.param += 10 * next;
                    }
                    case 10, 11, 12, 13, 14, 15, 17, 18, 20 -> {
                        final ItemOption itemOption4 = itemOption;
                        itemOption4.param += 5 * next;
                    }
                    case 21, 22, 23, 24, 25, 26 -> {
                        final ItemOption itemOption5 = itemOption;
                        itemOption5.param += 150 * next;
                    }
                    case 16 -> {
                        final ItemOption itemOption6 = itemOption;
                        itemOption6.param += 3 * next;
                    }
                    default -> {
                    }
                }
            }
        }
    }
    protected void upLVViThu(final byte next) {
        this.upgrade += next;
        if (this.options != null) {
            for (short i = 0; i < this.options.size(); ++i) {
                final ItemOption itemOption = this.options.get(i);
                if (itemOption.optionTemplate.id == 151) {
                    itemOption.param = 0;
                }
            }
        }
    }
    protected void upTNViThu(final Char _char, final byte next, int level) {
        int max = ViThu.getMax(level);
        if (this.options != null) {
            OUTER:
            for (short i = 0; i < this.options.size(); ++i) {
                final ItemOption itemOption = this.options.get(i);
                switch (itemOption.optionTemplate.id) {
                    case 144 -> {
                        if (itemOption.param >= max) {
                            Service.ServerMessage(_char, "Vĩ thú đã đạt tối đa tiềm năng của cấp độ");
                            break OUTER;
                        }
                        itemOption.param += next;
                    }
                    case 145 -> itemOption.param += next;
                    case 146 -> itemOption.param += next;
                    case 147 -> itemOption.param += next;
                    case 154, 6 -> itemOption.param += 10;
                    case 87 -> itemOption.param += 10;
                    case 148, 149 -> itemOption.param += 10;
                    default -> {
                    }
                }
            }
        }
    }
    protected void upgradeCT(final byte next, final short itemId) {
        this.upgrade += next;
        if (this.options != null) {
            for (short i = 0; i < this.options.size(); ++i) {
                final ItemOption itemOption = this.options.get(i);
                if (itemOption.optionTemplate.id == 100 && (itemId == 714 || itemId == 711)) {
                    final ItemOption itemOption2 = itemOption;
                    itemOption2.param += 5*next;
                }
                if (itemOption.optionTemplate.id == 127 || itemOption.optionTemplate.id == 130 || itemOption.optionTemplate.id == 131 ){
                    final ItemOption itemOption2 = itemOption;
                    itemOption2.param += 3 *next;
                }
            }
        }
    }

    protected boolean isTypeBody() {
        return 0 <= this.template.type && this.template.type <= 15;
    }
    
    protected boolean isTypeNgocKham() {
        return this.template.type == 34;
    }
    
    protected boolean isTypeMounts() {
        return 29 <= this.template.type && this.template.type <= 33;
    }
    
    protected boolean isTypeTask() {
        return (this.template.type == 23 || this.template.type == 24 || this.template.type == 25) && this.template.id != 743;
    }

    protected void clearExpire() {
        if (!this.isTypeMounts()) {
            this.expires = 0L;
        }
    }
    
    protected boolean isTypeUIMe() {
        return this.typeUI == 5 || this.typeUI == 3 || this.typeUI == 4 || this.typeUI == 39;
    }
    
    protected boolean isTypeUIShop() {
        return this.typeUI == 20 || this.typeUI == 21 || this.typeUI == 22 || this.typeUI == 23 || this.typeUI == 24 || this.typeUI == 25 || this.typeUI == 26 || this.typeUI == 27 || this.typeUI == 28 || this.typeUI == 29 || this.typeUI == 16 || this.typeUI == 17 || this.typeUI == 18 || this.typeUI == 19 || this.typeUI == 2 || this.typeUI == 6 || this.typeUI == 8 || this.typeUI == 34;
    }
    
    protected boolean isTypeUIShopLock() {
        return this.typeUI == 7 || this.typeUI == 9;
    }
    
    protected boolean isTypeUIStore() {
        return this.typeUI == 14;
    }
    
    protected boolean isTypeUIBook() {
        return this.typeUI == 15;
    }
    
    protected boolean isTypeUIFashion() {
        return this.typeUI == 32;
    }
    
    protected boolean isTypeUIClanShop() {
        return this.typeUI == 34;
    }
    
    protected boolean isTypeUIClan() {
        return this.typeUI == 39;
    }
    
    protected boolean isUpMax() {
        return this.getUpMax() == this.upgrade;
    }
    
    protected boolean isTypeDish() {
        return this.template.type == 18;
    }
    
    protected int getUpMax() {
        if (this.template.level >= 1 && this.template.level < 20) {
            return 4;
        }
        if (this.template.level >= 20 && this.template.level < 40) {
            return 8;
        }
        if (this.template.level >= 40 && this.template.level < 50) {
            return 12;
        }
        if (this.template.level >= 50 && this.template.level < 60) {
            return 14;
        }
        return 16;
    }
    
    protected byte KeepUpgrade() {
        if (this.upgrade >= 14) {
            return 14;
        }
        if (this.upgrade >= 12) {
            return 12;
        }
        if (this.upgrade >= 8) {
            return 8;
        }
        if (this.upgrade >= 4) {
            return 4;
        }
        return this.upgrade;
    }
    
    protected boolean isTypeClothe() {
        return this.template.type == 0 || this.template.type == 2 || this.template.type == 4 || this.template.type == 6 || this.template.type == 8;
    }
    
    protected boolean isTypeAdorn() {
        return this.template.type == 3 || this.template.type == 5 || this.template.type == 7 || this.template.type == 9;
    }
    
    protected boolean isTypeStack() {
        return this.expires == -1L && (this.template.type == 16 || this.template.type == 17);
    }
    
    protected boolean isTypeCrystal() {
        return this.template.type == 26;
    }
    
    protected boolean isTypeWeapon() {
        return this.template.type == 1;
    }
    
    protected boolean isItemClass0() {
        return this.itemId == 194 || this.itemId == 94 || this.itemId == 95 || this.itemId == 96 || this.itemId == 97 || this.itemId == 98 || this.itemId == 331 || this.itemId == 369 || this.itemId == 506 || this.itemId == 632;
    }
    
    protected boolean isItemClass1() {
        return this.itemId == 194 || this.itemId == 94 || this.itemId == 95 || this.itemId == 96 || this.itemId == 97 || this.itemId == 98 || this.itemId == 331 || this.itemId == 369 || this.itemId == 506 || this.itemId == 632 || this.itemId == 397;
    }
    
    protected boolean isItemClass2() {
        return this.itemId == 114 || this.itemId == 115 || this.itemId == 116 || this.itemId == 117 || this.itemId == 118 || this.itemId == 332 || this.itemId == 370 || this.itemId == 507 || this.itemId == 633 || this.itemId == 398;
    }
    
    protected boolean isItemClass3() {
        return this.itemId == 99 || this.itemId == 100 || this.itemId == 101 || this.itemId == 102 || this.itemId == 103 || this.itemId == 333 || this.itemId == 371 || this.itemId == 508 || this.itemId == 634 || this.itemId == 399;
    }
    
    protected boolean isItemClass4() {
        return this.itemId == 109 || this.itemId == 110 || this.itemId == 111 || this.itemId == 112 || this.itemId == 113 || this.itemId == 334 || this.itemId == 372 || this.itemId == 509 || this.itemId == 635 || this.itemId == 400;
    }
    
    protected boolean isItemClass5() {
        return this.itemId == 104 || this.itemId == 105 || this.itemId == 106 || this.itemId == 107 || this.itemId == 108 || this.itemId == 335 || this.itemId == 373 || this.itemId == 510 || this.itemId == 636 || this.itemId == 401;
    }
    
    protected boolean isItemClass6() {
        return this.itemId == 119 || this.itemId == 120 || this.itemId == 121 || this.itemId == 122 || this.itemId == 123 || this.itemId == 336 || this.itemId == 374 || this.itemId == 511 || this.itemId == 637 || this.itemId == 402;
    }
    
    protected boolean isItemExpQ() {
        return this.itemId == 248 || this.itemId == 539 || this.itemId == 540;
    }
    
    protected boolean isItemBuffHP() {
        return this.itemId == 13 || this.itemId == 14 || this.itemId == 15 || this.itemId == 16 || this.itemId == 17 || this.itemId == 565;
    }
    
    protected boolean isItemBuffMP() {
        return this.itemId == 18 || this.itemId == 19 || this.itemId == 20 || this.itemId == 21 || this.itemId == 22 || this.itemId == 566;
    }
    
    protected boolean isItemConvertUpgrade() {
        return this.itemId == 269 || this.itemId == 270 || this.itemId == 271;
    }
    
    protected boolean isItemCanvasBag() {
        return this.itemId == 215 || this.itemId == 229 || this.itemId == 283;
    }
    
    protected boolean isItemBook() {
        return (this.itemId >= 40 && this.itemId <= 48) || this.itemId == 311 || this.itemId == 375 || this.itemId == 397 || this.itemId == 552 || this.itemId == 558 || (this.itemId >= 49 && this.itemId <= 57) || this.itemId == 312 || this.itemId == 376 || this.itemId == 398 || this.itemId == 553 || this.itemId == 559 || (this.itemId >= 58 && this.itemId <= 66) || this.itemId == 313 || this.itemId == 377 || this.itemId == 399 || this.itemId == 554 || this.itemId == 560 || (this.itemId >= 67 && this.itemId <= 75) || this.itemId == 314 || this.itemId == 378 || this.itemId == 400 || this.itemId == 555 || this.itemId == 561 || (this.itemId >= 76 && this.itemId <= 84) || this.itemId == 315 || this.itemId == 379 || this.itemId == 401 || this.itemId == 556 || this.itemId == 562 || (this.itemId >= 85 && this.itemId <= 93) || this.itemId == 316 || this.itemId == 380 || this.itemId == 402 || this.itemId == 547 || this.itemId == 557 || this.itemId == 563;
    }
    
    protected boolean isItemReturn() {
        return this.itemId == 34 || this.itemId == 36;
    }
    
    protected boolean isItemBodyEffect() {
        return this.itemId == 569 || this.itemId == 772 || this.itemId == 773;
    }
    
    protected boolean isItemRare() {
        return (this.template.id >= 222 && this.template.id <= 228) || (this.template.id >= 420 && this.template.id <= 422);
    }
    
    protected boolean isItemNgoc() {
        return (this.template.id >= 652 && this.template.id <= 655);
    }
    
    protected boolean isItemNgocRong() {
        return this.template.id >= 222 && this.template.id <= 228;
    }
    
    protected boolean isItemChangeMap() {
        return this.itemId == 35 || this.itemId == 37 || this.itemId == 279;
    }
    
    protected boolean isItemRefreshPoint() {
        return this.itemId == 240 || this.itemId == 241;
    }
    
    protected boolean isItemClanOpen() {
        return this.itemId >= 423 && this.itemId <= 427;
    }
    
    protected boolean isItemEgg() {
        return this.itemId == 596 || this.itemId == 601;
    }
    
    protected boolean isItemGoFearriMap() {
        return this.itemId == 490;
    }
    
    protected boolean isItemGoFearriMap1() {
        return this.itemId == 1003;
    }
    protected boolean isItemtuiluong() {
        return this.itemId == 1004;
    }
    
    protected boolean isItemOpenEyes() {
        return this.itemId == 537 || this.itemId == 538;
    }
    
    protected boolean isItemAnimals1() {
        return (this.itemId >= 439 && this.itemId <= 444) || this.itemId == 523 || this.itemId == 524 || this.itemId == 798 || (this.itemId >= 801 && this.itemId <= 803) || this.itemId == 827 || this.itemId == 831 || this.itemId == 968;
    }
    
    protected boolean isItemAnimals2() {
        return (this.itemId >= 439 && this.itemId <= 444) || this.itemId == 523 || this.itemId == 524 || this.itemId == 798 || (this.itemId >= 801 && this.itemId <= 803);
    }
    
    protected boolean isItemMoto1() {
        return (this.itemId >= 485 && this.itemId <= 489) || this.itemId == 523 || this.itemId == 524 || this.itemId == 831 || this.itemId == 968;
    }
    
    protected boolean isItemMoto2() {
        return (this.itemId >= 485 && this.itemId <= 489) || this.itemId == 523 || this.itemId == 524;
    }
    
    protected boolean isItemEventUse() {
        return (this.itemId >= 298 && this.itemId <= 303) || this.itemId == 606 || this.itemId == 611 || this.itemId == 612 || (this.itemId >= 671 && this.itemId <= 672) || this.itemId == 743 || (this.itemId >= 643 && this.itemId <= 644) || this.itemId == 582;
    }
    protected boolean isItemViThuUse() {
        return (this.itemId >= 894 && this.itemId <= 898);
    }
    protected boolean isItemTaskUse() {
        return this.itemId == 231 || this.itemId == 219 || (this.itemId >= 233 && this.itemId <= 235) || this.itemId == 266;
    }
    protected boolean isItemAnimalsExp() {
        return (this.itemId >= 449 && this.itemId <= 453) || (this.itemId >= 573 && this.itemId <= 575);
    }
    
    protected boolean isItemMotoExp() {
        return (this.itemId >= 470 && this.itemId <= 472) || (this.itemId >= 576 && this.itemId <= 578);
    }
    
    protected boolean isItemMedicine() {
        return this.itemId >= 275 && this.itemId <= 278;
    }
    
    protected boolean isItemAnimalsHP() {
        return this.itemId == 444;
    }
    
    protected boolean isItemMotoHP() {
        return this.itemId == 469;
    }
    
    protected boolean isItemNextStar() {
        return this.itemId == 454;
    }
    
    protected boolean isItemOpenItemBody() {
        return this.itemId >= 383 && this.itemId <= 385;
    }
    
    protected boolean isItemDisguise() {
        return this.itemId == 541 || this.itemId == 552 || this.itemId == 745 || this.itemId == 771 || this.itemId == 786 || this.itemId == 787;
    }
    
    protected boolean isItemUpskill() {
        return this.itemId == 252 || this.itemId == 308;
    }
    
    protected boolean isItemUppotential() {
        return this.itemId == 253 || this.itemId == 309;
    }
    
    protected boolean isItemClanPoint() {
        return this.itemId >= 436 && this.itemId <= 438;
    }
    
    protected boolean isItemReward() {
        return this.itemId == 272 || this.itemId == 282 || this.itemId == 647;
    }
    
    protected boolean isItemPkDown() {
        return this.itemId == 257;
    }
    
    protected boolean isItemCaveCard() {
        return this.itemId == 280;
    }
    
    protected boolean isItemClearExpDown() {
        return this.itemId >= 254 && this.itemId <= 256;
    }
    
    protected boolean isItemFire() {
        return this.itemId == 420;
    }
    
    protected boolean isItemIce() {
        return this.itemId == 421;
    }
    
    protected boolean isItemWind() {
        return this.itemId == 422;
    }
    
    protected boolean isItemRod() {
        return this.itemId == 548;
    }
    
    protected boolean isItemPrecious() {
        return this.itemId == 535 || this.itemId == 536;
    }
    
    protected boolean isItemDaDanhVong() {
        return this.itemId >= 695 && this.itemId <= 704;
    }
    
    protected boolean isItemDVPhu() {
        return this.itemId == 705;
    }
    
    protected boolean isItemResPk() {
        return this.itemId == 744;
    }
    protected boolean isFoodTT() {return this.itemId >= 598 && this.itemId <= 600;}
    protected boolean isItemJirai() {return this.itemId >= 733 && this.itemId <= 741;} // bộ sưu tập Khổng Minh Tiến
    protected boolean isItemJumito() {return this.itemId >= 760 && this.itemId <= 768;} // cải trang Khổng Minh Tiến
    protected boolean isViThu() {
        return this.itemId>=904 && this.itemId <= 957 || this.itemId>=833 && this.itemId <= 850;
    }
    protected boolean isTBViThu() {
        return this.itemId>= 857 && this.itemId <= 892;
    }
    protected boolean isEggViThu() {
        return this.itemId>= 899 && this.itemId <= 903;
    }
    protected boolean isEquip2() {
        return (this.itemId >= 795 && this.itemId <= 805 && this.itemId != 797) || (this.itemId >= 813 && this.itemId <= 817) || (this.itemId >= 825 && this.itemId <= 826) || this.itemId == 830 || this.itemId == 972 || this.itemId == 973|| (this.itemId >= 988 && this.itemId <= 993);
    }
    
    protected boolean isItemEventUse_1() {
        return this.itemId == 824;
    }
    
    protected static boolean isTinhLuyen(final Item item) {
        for (short i = 0; i < item.options.size(); ++i) {
            final ItemOption option = item.options.get(i);
            if (option != null && option.optionTemplate.id == 85) {
                return true;
            }
        }
        return false;
    }
    
    protected static boolean isUpgradeHide(final int id, final byte upgrade) {
        return ((id == 27 || id == 30 || id == 60) && upgrade < 4) || ((id == 28 || id == 31 || id == 37 || id == 61) && upgrade < 8) || ((id == 29 || id == 32 || id == 38 || id == 62) && upgrade < 12) || ((id == 33 || id == 34 || id == 35 || id == 36 || id == 39) && upgrade < 14) || (((id >= 40 && id <= 46) || (id >= 48 && id <= 56)) && upgrade < 16);
    }
    
    protected static boolean isIndexUI(final int indexUI, final Item[] arrItem) {
        try {
            for (byte i = 0; i < arrItem.length; ++i) {
                final Item item = arrItem[i];
                if (item != null && item.indexUI == indexUI) {
                    return true;
                }
            }
        }
        catch (Exception ex) {}
        return false;
    }
    
    protected static int paramId(final Item item, final short optionTemplateId) {
        for (short i = 0; i < item.options.size(); ++i) {
            final ItemOption option = item.options.get(i);
            if (option != null && option.optionTemplate.id == optionTemplateId) {
                return option.param;
            }
        }
        return 0;
    }
    
    protected void optionAdd(final int[] optionId, final int[] optionParam) {
        try {
            for (byte i = 0; i < optionId.length; ++i) {
                final ItemOption option = new ItemOption(optionId[i], optionParam[i]);
                this.options.add(option);
            }
        }
        catch (Exception ex) {}
    }
    
    protected void optionAdd(final ItemOption[] arrOption) {
        try {
            for (byte i = 0; i < arrOption.length; ++i) {
                final ItemOption option = new ItemOption(arrOption[i].optionTemplate.id, arrOption[i].param);
                this.options.add(option);
            }
        }
        catch (Exception ex) {}
    }
    
    protected void optionAdd(final ArrayList<ItemOption> options) {
        try {
            for (byte i = 0; i < options.size(); ++i) {
                final ItemOption option = new ItemOption(options.get(i).optionTemplate.id, options.get(i).param);
                this.options.add(option);
            }
        }
        catch (Exception ex) {}
    }
    
    protected int saleCoinLockDefault() {
        if (this.isTypeMounts()) {
            return 5;
        }
        if (!this.isTypeBody()) {
            return 0;
        }
        if (this.isTypeWeapon() && this.template.level > 1) {
            return 5 * (this.template.level / 10) * 10;
        }
        if (this.isTypeClothe() && this.template.level > 1) {
            return 3 * (this.template.level / 10) * 10;
        }
        if (this.isTypeAdorn() && this.template.level > 1) {
            return 4 * (this.template.level / 10) * 10;
        }
        return 5;
    }
    
    protected static int setMountsLevel(final byte level, final ItemOption option) {
        switch (option.optionTemplate.id) {
            case 6, 7, 73 -> {
                if (level % 10 == 0) {
                    return option.param + 100;
                }
            }
            case 10, 68 -> {
                if (level % 10 == 0) {
                    return option.param + 10;
                }
            }
            case 67, 70, 71, 72 ->  {
                if (level % 20 == 0) {
                    return option.param + 5;
                }
            }
            case 69 -> {
                if (level % 10 == 0) {
                    return option.param + 5;
                }
            }
            case 74 ->  {
                if (level % 10 == 0) {
                    return option.param + 20;
                }
            }
        }
        return option.param;
    }
    
    protected static int setMountsStar(final ItemOption option) {
        switch (option.optionTemplate.id) {
            case 6, 7 -> {
                return option.param - 900;
            }
            case 10, 68 -> {
                return option.param - 80;
            }
            case 67, 70, 71, 72 -> {
                return option.param - 15;
            }
            case 69 -> {
                return option.param - 30;
            }
            case 73 -> {
                return option.param - 800;
            }
            case 74 -> {
                return option.param - 100;
            }
            default -> {
                return option.param;
            }
        }
    }
    
    protected static Item parseItem(final String s) {
        try {
            final Item item = new Item();
            final JSONArray jitem = (JSONArray)JSONValue.parseWithException(s);
            final Item item2 = item;
            final ItemTemplate[] itemTemplates = GameScr.itemTemplates;
            final Item item3 = item;
            final short short1 = Short.parseShort(jitem.get(0).toString());
            item3.itemId = short1;
            item2.template = itemTemplates[short1];
            item.indexUI = Integer.parseInt(jitem.get(1).toString());
            item.quantity = Integer.parseInt(jitem.get(2).toString());
            item.expires = Long.parseLong(jitem.get(3).toString());
            item.isLock = (Byte.parseByte(jitem.get(4).toString()) == 1);
            item.sys = Byte.parseByte(jitem.get(5).toString());
            item.upgrade = Byte.parseByte(jitem.get(6).toString());
            item.buyCoin = Integer.parseInt(jitem.get(7).toString());
            item.buyCoinLock = Integer.parseInt(jitem.get(8).toString());
            item.buyGold = Integer.parseInt(jitem.get(9).toString());
            item.buyGoldLock = Integer.parseInt(jitem.get(10).toString());
            item.saleCoinLock = Integer.parseInt(jitem.get(11).toString());
            item.typeUI = Integer.parseInt(jitem.get(12).toString());
            item.isExpires = (Byte.parseByte(jitem.get(13).toString()) == 1);
            final JSONArray joptionid = (JSONArray)jitem.get(14);
            final JSONArray joptionparam = (JSONArray)jitem.get(15);
            item.options = new ArrayList<>();
            for (short i = 0; i < joptionid.size(); ++i) {
                final ItemOption option = new ItemOption(Integer.parseInt(joptionid.get((int)i).toString()), Integer.parseInt(joptionparam.get((int)i).toString()));
                item.options.add(option);
            }
            return item;
        }
        catch (ParseException | NumberFormatException ex2) {
            final Exception ex = null;
            final Exception e = ex;
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String toString() {
        String a = "[" + this.itemId;
        a = a + "," + this.indexUI;
        a = a + "," + this.quantity;
        a = a + "," + this.expires;
        a = a + "," + (this.isLock ? 1 : 0);
        a = a + "," + this.sys;
        a = a + "," + this.upgrade;
        a = a + "," + this.buyCoin;
        a = a + "," + this.buyCoinLock;
        a = a + "," + this.buyGold;
        a = a + "," + this.buyGoldLock;
        a = a + "," + this.saleCoinLock;
        a = a + "," + this.typeUI;
        a = a + "," + (this.isExpires ? 1 : 0);
        String optionids = "";
        String optionparams = "";
        for (short i = 0; i < this.options.size(); ++i) {
            final ItemOption option = this.options.get(i);
            if (i == 0) {
                optionids = optionids + "" + option.optionTemplate.id;
                optionparams = optionparams + "" + option.param;
            }
            else {
                optionids = optionids + "," + option.optionTemplate.id;
                optionparams = optionparams + "," + option.param;
            }
        }
        a = a + ",[" + optionids + "]";
        a = a + ",[" + optionparams + "]";
        return a + "]";
    }
}
