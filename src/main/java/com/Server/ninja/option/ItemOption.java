// 
// Decompiled by Procyon v0.5.36
// 

package com.Server.ninja.option;

import java.util.ArrayList;
import com.Server.ninja.Server.Util;
import com.Server.ninja.Server.GameScr;
import com.Server.ninja.template.ItemOptionTemplate;

public class ItemOption
{
    public int param;
    protected byte active;
    public ItemOptionTemplate optionTemplate;
    
    public ItemOption() {
    }
    
    public ItemOption(final int optionTemplateId, final int param) {
        this.param = param;
        this.optionTemplate = GameScr.iOptionTemplates[optionTemplateId];
    }
    
    public int getOptionShopParam() {
        int num;
        if (this.optionTemplate.id == 0 || this.optionTemplate.id == 1 || this.optionTemplate.id == 21 || this.optionTemplate.id == 22 || this.optionTemplate.id == 23 || this.optionTemplate.id == 24 || this.optionTemplate.id == 25 || this.optionTemplate.id == 26) {
            num = this.param - 50 + 1;
        }
        else if (this.optionTemplate.id == 6 || this.optionTemplate.id == 7 || this.optionTemplate.id == 8 || this.optionTemplate.id == 9 || this.optionTemplate.id == 19) {
            num = this.param - 10 + 1;
        }
        else if (this.optionTemplate.id == 2 || this.optionTemplate.id == 3 || this.optionTemplate.id == 4 || this.optionTemplate.id == 5 || this.optionTemplate.id == 10 || this.optionTemplate.id == 11 || this.optionTemplate.id == 12 || this.optionTemplate.id == 13 || this.optionTemplate.id == 14 || this.optionTemplate.id == 15 || this.optionTemplate.id == 17 || this.optionTemplate.id == 18 || this.optionTemplate.id == 20) {
            num = this.param - 5 + 1;
        }
        else if (this.optionTemplate.id == 16) {
            num = this.param - 3 + 1;
        }
        else {
            num = this.param;
        }
        return num;
    }
    
    public boolean isKhamVuKhi() {
        return this.optionTemplate.id == 106;
    }
    
    public boolean isKhamTrangBi() {
        return this.optionTemplate.id == 107;
    }
    
    public boolean isTrangSuc() {
        return this.optionTemplate.id == 108;
    }
    
    public static ItemOption[] arrOptionDefault(final short itemTemplateId, final byte sys) {
        switch (itemTemplateId) {
            case 337, 338, 407, 408 -> {
                return new ItemOption[]{new ItemOption(58, 20), new ItemOption(6, 500)};
            }
            case 403, 404 -> {
                return new ItemOption[]{new ItemOption(57, 80)};
            }
            case 419 -> {
                return new ItemOption[]{new ItemOption(0, 1000), new ItemOption(1, 1000)};
            }
            case 420, 421, 422 -> {
                return new ItemOption[]{new ItemOption(85, 0), new ItemOption(82, 350), new ItemOption(83, 350), new ItemOption(84, 100), new ItemOption(81, 5), new ItemOption(80, 25), new ItemOption(79, 5)};
            }
            case 439, 486 -> {
                return new ItemOption[]{new ItemOption(85, 0), new ItemOption(75, 50)};
            }
            case 440, 487 -> {
                return new ItemOption[]{new ItemOption(85, 0), new ItemOption(76, 500)};
            }
            case 441, 488 -> {
                return new ItemOption[]{new ItemOption(85, 0), new ItemOption(77, 500)};
            }
            case 442, 489 -> {
                return new ItemOption[]{new ItemOption(85, 0), new ItemOption(78, 50)};
            }
            case 443, 485, 523, 524, 831 -> {
                return new ItemOption[]{new ItemOption(65, 0), new ItemOption(66, 1000)};
            }
            case 968 -> {
                return new ItemOption[]{new ItemOption(65, 0), new ItemOption(66, 1000)};
            }
            case 541 -> {
                return new ItemOption[]{new ItemOption(125, 5000), new ItemOption(87, 5000)};
            }
            case 584, 587 -> {
                return new ItemOption[]{new ItemOption(102, 150), new ItemOption(103, 30)};
            }
            case 568 -> {
                return new ItemOption[]{new ItemOption(100, 50)};
            }
            case 569 -> {
                return new ItemOption[]{new ItemOption(99, 300)};
            }
            case 652 -> {
                return new ItemOption[]{new ItemOption(106, 0), new ItemOption(102, Util.nextInt(300, 500)), new ItemOption(115, Util.nextInt(-50, -10)), new ItemOption(107, 0), new ItemOption(126, Util.nextInt(1, 5)), new ItemOption(105, Util.nextInt(-100, -10)), new ItemOption(108, 0), new ItemOption(114, Util.nextInt(1, 5)), new ItemOption(118, Util.nextInt(-20, -1)), new ItemOption(104, 0), new ItemOption(123, 800000)};
            }
            case 653 -> {
                return new ItemOption[]{new ItemOption(106, 0), new ItemOption(73, Util.nextInt(50, 100)), new ItemOption(114, Util.nextInt(-5, -1)), new ItemOption(107, 0), new ItemOption(124, Util.nextInt(5, 10)), new ItemOption(73, Util.nextInt(-50, -10)), new ItemOption(108, 0), new ItemOption(115, Util.nextInt(5, 10)), new ItemOption(119, Util.nextInt(-5, -1)), new ItemOption(104, 0), new ItemOption(123, 800000)};
            }
            case 654 -> {
                return new ItemOption[]{new ItemOption(106, 0), new ItemOption(103, Util.nextInt(100, 200)), new ItemOption(125, Util.nextInt(-100, -10)), new ItemOption(107, 0), new ItemOption(121, Util.nextInt(1, 5)), new ItemOption(120, Util.nextInt(-5, -1)), new ItemOption(108, 0), new ItemOption(116, Util.nextInt(5, 10)), new ItemOption(126, Util.nextInt(-5, -1)), new ItemOption(104, 0), new ItemOption(123, 800000)};
            }
            case 655 -> {
                return new ItemOption[]{new ItemOption(106, 0), new ItemOption(105, Util.nextInt(100, 500)), new ItemOption(116, Util.nextInt(-50, -10)), new ItemOption(107, 0), new ItemOption(125, Util.nextInt(10, 50)), new ItemOption(117, Util.nextInt(-100, -20)), new ItemOption(108, 0), new ItemOption(117, Util.nextInt(10, 50)), new ItemOption(124, Util.nextInt(-5, -1)), new ItemOption(104, 0), new ItemOption(123, 800000)};
            }
            case 685 -> {
                return new ItemOption[]{new ItemOption(6, 1000), new ItemOption(87, 500)};
            }
            case 686 -> {
                return new ItemOption[]{new ItemOption(6, 2000), new ItemOption(87, 750)};
            }
            case 687 -> {
                return new ItemOption[]{new ItemOption(6, 3000), new ItemOption(87, 1000), new ItemOption(79, 25)};
            }
            case 688 -> {
                return new ItemOption[]{new ItemOption(6, 4000), new ItemOption(87, 1250), new ItemOption(79, 25)};
            }
            case 689 -> {
                return new ItemOption[]{new ItemOption(6, 5000), new ItemOption(87, 1500), new ItemOption(79, 25)};
            }
            case 690 -> {
                return new ItemOption[]{new ItemOption(6, 6000), new ItemOption(87, 1750), new ItemOption(79, 25), new ItemOption(64, 0)};
            }
            case 691 -> {
                return new ItemOption[]{new ItemOption(6, 7000), new ItemOption(87, 2000), new ItemOption(79, 25), new ItemOption(64, 0)};
            }
            case 692 -> {
                return new ItemOption[]{new ItemOption(6, 8000), new ItemOption(87, 2250), new ItemOption(79, 25), new ItemOption(64, 0)};
            }
            case 693 -> {
                return new ItemOption[]{new ItemOption(6, 9000), new ItemOption(87, 2500), new ItemOption(79, 25), new ItemOption(64, 0)};
            }
            case 694 -> {
                return new ItemOption[]{new ItemOption(6, 10000), new ItemOption(87, 2750), new ItemOption(79, 25), new ItemOption(64, 0), new ItemOption(113, 5000)};
            }
            case 774 -> {
                return new ItemOption[]{new ItemOption(127, 0), new ItemOption(100, 50)};
            }
            case 786 -> {
                return new ItemOption[]{new ItemOption(130, 0), new ItemOption(100, 50)};
            }
            case 787 -> {
                return new ItemOption[]{new ItemOption(131, 0), new ItemOption(100, 50)};
            }
            case 711, 714 -> {
                return new ItemOption[]{new ItemOption(100, 0)};
            }
            case 744 -> {
                return new ItemOption[]{new ItemOption(6, 5000), new ItemOption(87, 5000)};
            }
            case 745 -> {
                return new ItemOption[]{new ItemOption(6, 2000), new ItemOption(58, 20)};
            }
            case 795, 796 -> {
                return new ItemOption[]{new ItemOption(6, 3000), new ItemOption(7, 3000), new ItemOption(94, 10), new ItemOption(130, 10), new ItemOption(131, 10), new ItemOption(127, 10)};
            }
            case 797 -> {
                return new ItemOption[]{new ItemOption(85, 0), new ItemOption(82, 1000), new ItemOption(83, 350), new ItemOption(84, 100), new ItemOption(81, 10), new ItemOption(80, 30)};
            }
            case 799, 800 -> {
                return new ItemOption[]{new ItemOption(94, 15), new ItemOption(92, 100), new ItemOption(86, 200)};
            }
            case 801 -> {
                return new ItemOption[]{new ItemOption(65, 1000), new ItemOption(66, 1000)};
            }
            case 802 -> {
                return new ItemOption[]{new ItemOption(65, 1000), new ItemOption(66, 1000)};
            }
            case 803 -> {
                return new ItemOption[]{new ItemOption(65, 1000), new ItemOption(66, 1000)};
            }
            case 804, 805 -> {
                return new ItemOption[]{new ItemOption(6, 3000), new ItemOption(7, 3000), new ItemOption(94, 10), new ItemOption(130, 10), new ItemOption(131, 10), new ItemOption(127, 10)};
            }
            case 825 -> {
                return new ItemOption[]{new ItemOption(6, 3000), new ItemOption(7, 2000), new ItemOption(98, 15)};
            }
            case 826 -> {
                return new ItemOption[]{new ItemOption(6, 2000), new ItemOption(7, 3000), new ItemOption(94, 10)};
            }
            case 827 -> {
                return new ItemOption[]{new ItemOption(65, 0), new ItemOption(66, 1000)};
            }
            case 830 -> {
                return new ItemOption[]{new ItemOption(6, 3000), new ItemOption(7, 3000), new ItemOption(94, 10), new ItemOption(136, 100), new ItemOption(103, 10), new ItemOption(131, 10), new ItemOption(127, 10)};
            }
            case 832 -> {
                return new ItemOption[]{new ItemOption(6, 2000), new ItemOption(7, 3000), new ItemOption(137, 100), new ItemOption(92, 100), new ItemOption(94, 10), new ItemOption(120, 200), new ItemOption(119, 200)};
            }
            default -> {
                return null;
            }
        }
    }
    public static ArrayList<ItemOption> openOptionAdd(final short itemTemplateId) {
        final ArrayList<ItemOption> options = new ArrayList<ItemOption>();
        switch (itemTemplateId) {
            case 798: {
                options.add(new ItemOption(119, 200));
                options.add(new ItemOption(120, 200));
                break;
            }
            case 801: {
                options.add(new ItemOption(130, 20));
                break;
            }
            case 802: {
                options.add(new ItemOption(131, 20));
                break;
            }
            case 803: {
                options.add(new ItemOption(127, 20));
                break;
            }
            case 813:
            case 814:
            case 815:
            case 816:
            case 817: {
                options.add(new ItemOption(6, Util.nextInt(10, 5000)));
                options.add(new ItemOption(73, Util.nextInt(10, 5000)));
                if (Util.nextInt(100) < 25) {
                    options.add(new ItemOption(Util.nextInt(2, 4), Util.nextInt(10, 200)));
                }
                else if (Util.nextInt(100) < 25) {
                    options.add(new ItemOption(Util.nextInt(8, 9), Util.nextInt(10, 20)));
                }
                else if (Util.nextInt(100) < 25) {
                    options.add(new ItemOption(Util.nextInt(2, 4), Util.nextInt(10, 200)));
                }
                else {
                    options.add(new ItemOption(86, Util.nextInt(500, 1000)));
                }
                if (Util.nextInt(10) < 1) {
                    options.add(new ItemOption(100, Util.nextInt(1, 20)));
                    break;
                }
                break;
            }
            case 827: {
                options.add(new ItemOption(134, 3));
                if (Util.nextInt(10) < 2) {
                    options.add(new ItemOption(135, 3));
                    break;
                }
                break;
            }
            case 832: {
                options.add(new ItemOption(58, 20));
                options.add(new ItemOption(94, 15));
                break;
            }
        }
        return options;
    }
}
