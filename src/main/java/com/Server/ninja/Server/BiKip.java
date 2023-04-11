 package com.Server.ninja.Server;

import java.util.ArrayList;

import com.Server.ninja.option.ItemOption;

public class BiKip {
    private static final int Gold_Luyen = 1000;
    private static final int[] arr_Id_Option;
    private static final int[][] arr_Param_Option;
    static final byte maxUpgrade = 10;
    static final int[] goldUps;
    static final int[] percents;
    static final int[] SL_NL;
    static final short item_NLChinh = 457;

    protected static void LuyenBiKip(final Char _char) {
        if (_char.user.player.ItemBagIndexNull() < 0) {
            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
        } else if (_char.nClass == 0) {
            Service.ServerMessage(_char, Text.get(0, 383));
        } else if (_char.user.luong < 1000) {
            Service.ServerMessage(_char, Text.get(0, 384));
        } else if (_char.user.player.ItemBagQuantity((short) 11) < 1 || _char.user.player.ItemBagQuantity((short) 454) < 1) {
            Service.ServerMessage(_char, Text.get(0, 386));
        } else {
            _char.user.player.ItemBagUses((short) 11, 1);
            _char.user.player.ItemBagUses((short) 454, 1);
            _char.user.player.upGold(-1000L, (byte) 1);
            final short bikipId = (short) (396 + _char.nClass);
            final Item it = new Item(null, bikipId, 1, -1, true, (byte) 0, 5);
            final ArrayList<Byte> indexs = new ArrayList<Byte>();
            for (byte m = 0; m < 5; ++m) {
                byte index = -1;
                while (index == -1) {
                    final byte index2 = (byte) Util.nextInt(BiKip.arr_Id_Option.length);
                    if (!indexs.contains(index2)) {
                        indexs.add(index2);
                        index = index2;
                    }
                }
                final ItemOption option = new ItemOption(BiKip.arr_Id_Option[index], Util.nextInt(BiKip.arr_Param_Option[index][0], BiKip.arr_Param_Option[index][1]));
                it.options.add(option);
            }
            _char.user.player.ItemBagAdd(it);
        }
    }

    protected static void NangCap(final Char _char) {
        final Item it = _char.ItemBody[15];
        if (it == null) {
            Service.ServerMessage(_char, Text.get(0, 389));
        } else if (it.upgrade >= 10) {
            Service.ServerMessage(_char, Text.get(0, 390));
        } else if (_char.user.luong < BiKip.goldUps[it.upgrade]) {
            Service.ServerMessage(_char, String.format(Text.get(0, 384), BiKip.goldUps[it.upgrade]));
        } else if (_char.user.player.ItemBagQuantity((short) 457) < BiKip.SL_NL[it.upgrade]) {
            Service.ServerMessage(_char, String.format(Text.get(0, 391), BiKip.SL_NL[it.upgrade], GameScr.itemTemplates[457].name, it.template.name));
        } else {
            _char.user.player.upGold(-BiKip.goldUps[it.upgrade], (byte) 1);
            _char.user.player.ItemBagUses((short) 457, BiKip.SL_NL[it.upgrade]);
            final boolean success = Util.nextInt(1, Util.nextInt(70, 100)) <= BiKip.percents[it.upgrade];
            if (success) {
                final Item item = it;
                ++item.upgrade;
                for (int m = 0; m < it.options.size(); ++m) {
                    final ItemOption option = it.options.get(m);
                    if (option != null) {
                        if (option.optionTemplate.id >= 70 && option.optionTemplate.id <= 72) {
                            final ItemOption itemOption = option;
                            itemOption.param += 5;
                        }
                        if (option.optionTemplate.id >= 6 && option.optionTemplate.id <= 7) {
                            final ItemOption itemOption2 = option;
                            itemOption2.param += 100;
                        }
                        if (option.optionTemplate.id == 10) {
                            final ItemOption itemOption3 = option;
                            itemOption3.param += 20;
                        }
                        if (option.optionTemplate.id == 69) {
                            final ItemOption itemOption4 = option;
                            itemOption4.param += 25;
                        }
                        if (option.optionTemplate.id >= 73 && option.optionTemplate.id <= 74) {
                            final ItemOption itemOption5 = option;
                            itemOption5.param += 50;
                        }
                        if (option.optionTemplate.id == 67 || option.optionTemplate.id == 120) {
                            final ItemOption itemOption6 = option;
                            ++itemOption6.param;
                        }
                    }
                }
                Service.updateInfoMe(_char);
                Player.updateInfoPlayer(_char);
                Service.ServerMessage(_char, String.format(Text.get(0, 392), it.template.name));
            } else {
                Service.ServerMessage(_char, String.format(Text.get(0, 393), it.template.name));
            }
        }
    }

    static {
        arr_Id_Option = new int[]{70, 71, 72, 6, 7, 10, 73, 69, 67, 120};
        arr_Param_Option = new int[][]{{5, 25}, {5, 25}, {5, 25}, {50, 100}, {50, 100}, {10, 50}, {50, 100}, {25, 50}, {10, 10}, {10, 10}};
        goldUps = new int[]{250, 500, 750, 1000, 1400, 2000, 2500, 3000, 3500, 4000};
        percents = new int[]{80, 70, 60, 50, 40, 30, 20, 15, 10, 5};
        SL_NL = new int[]{2, 2, 3, 3, 4, 4, 5, 6, 7, 8};
    }
}
